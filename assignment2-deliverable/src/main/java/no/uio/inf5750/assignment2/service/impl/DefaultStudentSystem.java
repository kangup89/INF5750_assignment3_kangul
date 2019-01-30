package no.uio.inf5750.assignment2.service.impl;
import java.util.Collection;

import no.uio.inf5750.assignment2.model.*;
import no.uio.inf5750.assignment2.dao.*;
import no.uio.inf5750.assignment2.service.StudentSystem;
import org.springframework.beans.factory.annotation.*;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DefaultStudentSystem implements StudentSystem
{
	
	@Autowired
	private CourseDAO courseDao;
	@Autowired
	private DegreeDAO degreeDao;
	@Autowired
	private StudentDAO studentDao;	

	public void setCourseDao(CourseDAO courseDao){
		this.courseDao = courseDao;
	}

	public void setDegreeDao(DegreeDAO degreeDao){
		this.degreeDao = degreeDao;
	}

    public void setStudentDao(StudentDAO studentDao){
    	this.studentDao = studentDao;
	}	
    
    public void setStudentLocation(int studentId, String latitude, String longitude){
    	Student student = studentDao.getStudent(studentId);
    	student.setLatitude(latitude);
    	student.setLongitude(longitude);
    }
	
    /**
     * Adds a course.
     * 
     * @param courseCode the course code of the course to add.
     * @param name the name of the course to add.
     * @return the generated id of the added course.
     */
    public int addCourse( String courseCode, String name ){
		Course course = new Course(courseCode, name);
		return courseDao.saveCourse(course);
	}

    /**
     * Updates a course.
     * 
     * @param courseId the id of the course to update.
     * @param courseCode the course code to update.
     * @param name the name to update.
     */
    public void updateCourse( int courseId, String courseCode, String name ){
		Course course = courseDao.getCourse(courseId);
		course.setCourseCode(courseCode);
		course.setName(name);
	}

    /**
     * Returns a course.
     * 
     * @param courseId the id of the course to return.
     * @return the course or null if it doesn't exist.
     */
    public Course getCourse( int courseId ){
		return courseDao.getCourse(courseId);
	}

    /**
     * Returns a course with a specific course code.
     * 
     * @param courseCode the course code of the course to return.
     * @return the course code or null if it doesn't exist.
     */
    public Course getCourseByCourseCode( String courseCode ){
		return courseDao.getCourseByCourseCode(courseCode);
	}

    /**
     * Returns a course with a specific name.
     * 
     * @param name the name of the course that needs to be found
     * @return the course code or null if it doesn't exist.
     */
    public Course getCourseByName( String name ){
		return courseDao.getCourseByName(name);
	}
	
    /**
     * Returns all courses.
     * 
     * @return all courses or an empty Collection if no course exists.
     */
    public Collection<Course> getAllCourses(){
		return courseDao.getAllCourses();
	}

    /**
     * Removes all references to the course from degrees and students and
     * deletes the course.
     * 
     * @param courseId the id of the course to delete.
     */
    public void delCourse( int courseId ){
    	Course course = courseDao.getCourse(courseId);
    	
    	Collection<Degree> degrees = getAllDegrees();    	
    	if(degrees.size() != 0){
    		for(int i = 0; i < degrees.size()+1; i++){
    			removeRequiredCourseFromDegree( degrees.iterator().next().getId(), courseId );
    		}
    	}
		Collection<Student> students = course.getAttendants();
		while (students.iterator().hasNext()){
			removeAttendantFromCourse(courseId, students.iterator().next().getId());
		}
    	   	
		courseDao.delCourse(course);
	}
	
    /**
     * Adds an attendant to a course and a course to a student.
     * 
     * @param courseId the id of the course.
     * @param studentId the id of the student.
     */
    public void addAttendantToCourse( int courseId, int studentId ){
		Course course = courseDao.getCourse(courseId);
		Student student = studentDao.getStudent(studentId);
		course.getAttendants().add(student);
		student.getCourses().add(course);
	}
		
    /**
     * Removes an attendant from a course and a course from a student.
     * 
     * @param courseId the id of the course.
     * @param studentId the id of the student.
     */
    public void removeAttendantFromCourse( int courseId, int studentId ){
		Course course = courseDao.getCourse(courseId);
		Student student = studentDao.getStudent(studentId);
		course.getAttendants().remove(student);
		student.getCourses().remove(course);
	}

    /**
     * Adds a degree.
     * 
     * @param type the type of the degree to add.
     * @return the generated id of the added degree.
     */
    public int addDegree( String type ){
		Degree degree = new Degree(type);
		return degreeDao.saveDegree(degree);
	}

    /**
     * Updates a degree.
     * 
     * @param degreeId the id of the degree to update.
     * @param type the type to update.
     */
    public void updateDegree( int degreeId, String type ){
		Degree degree = degreeDao.getDegree(degreeId);
		degree.setType(type);
	}

    /**
     * Returns a degree.
     * 
     * @param degreeId the id of the degree to return.
     * @return the degree or null if it doesn't exist.
     */
    public Degree getDegree( int degreeId ){
		return degreeDao.getDegree(degreeId);
	}

    /**
     * Returns a degree with a specific type.
     * 
     * @param type the type of the degree to return.
     * @return the degree or null if it doesn't exist.
     */
    public Degree getDegreeByType( String type ){
		return degreeDao.getDegreeByType(type);
	}

    /**
     * Returns all degrees.
     * 
     * @return all degrees or an empty Collection if no degree exists.
     */
    public Collection<Degree> getAllDegrees(){
		return degreeDao.getAllDegrees();
	}

    /**
     * Removes all references to the degree from students and deletes the
     * degree.
     * 
     * @param degreeId the id of the degree to delete.
     */
    public void delDegree( int degreeId ){    	
    	Degree degree = degreeDao.getDegree(degreeId);
    	
    	Collection<Student> students = getAllStudents();   	
    	if(students.size() != 0){
    		for(int i = 0; i < students.size()+1; i++){
    			removeDegreeFromStudent(students.iterator().next().getId(), degreeId) ;
    		}	   
    	}
    	
		degreeDao.delDegree(degree);
	}

    /**
     * Adds a required course to a degree.
     * 
     * @param degreeId the id of the degree.
     * @param courseId the id of the course.
     */
    public void addRequiredCourseToDegree( int degreeId, int courseId ){
		Degree degree = degreeDao.getDegree(degreeId);
		Course course = courseDao.getCourse(courseId);
		degree.getRequiredCourses().add(course);
	}

    /**
     * Removes a required course from a degree.
     * 
     * @param degreeId the id of the degree.
     * @param courseId the id of the course.
     */
    public void removeRequiredCourseFromDegree( int degreeId, int courseId ){
		Degree degree = degreeDao.getDegree(degreeId);
		Course course = courseDao.getCourse(courseId);
		degree.getRequiredCourses().remove(course);
	}

    /**
     * Adds a student.
     * 
     * @param name the name of the student to add.
     * @return the generated id of the added student.
     */
    public int addStudent( String name ){
		Student student = new Student(name);
		return studentDao.saveStudent(student);
	}

    /**
     * Updates a student.
     * 
     * @param studentId the id of the student to update.
     * @param name the name to update.
     */
    public void updateStudent( int studentId, String name ){
		Student student = studentDao.getStudent(studentId);
		student.setName(name);
	}

    /**
     * Returns a student.
     * 
     * @param studentId the id of the student to return.
     * @return the student or null if it doesn't exist.
     */
    public Student getStudent( int studentId ){
		return studentDao.getStudent(studentId);
	}

    /**
     * Returns a student with a specific name.
     * 
     * @param name the name of the student to return.
     * @return the student or null if it doesn't exist.
     */
    public Student getStudentByName( String name ){
		return studentDao.getStudentByName(name);
	}

    /**
     * Returns all students.
     * 
     * @return all students or an empty Collection if no student exists.
     */
    public Collection<Student> getAllStudents(){
		return studentDao.getAllStudents();
	}

    /**
     * Removes all references to the student from courses and deletes the
     * student.
     * 
     * @param studentId the id of the student to delete.
     */
    public void delStudent( int studentId ){
		Student student = studentDao.getStudent(studentId);
 
		Collection<Course> courses = student.getCourses();
		while (courses.iterator().hasNext()){
			removeAttendantFromCourse(courses.iterator().next().getId(), studentId);
		}
		
		studentDao.delStudent(student);
	}

    /**
     * Adds a degree to a student.
     * 
     * @param studentId the id of the student.
     * @param degreeId the id of the degree.
     */
    public void addDegreeToStudent( int studentId, int degreeId ){
		Student student = studentDao.getStudent(studentId);
		Degree degree = degreeDao.getDegree(degreeId);
		student.getDegrees().add(degree);
	}		

    /**
     * Removes a degree from a student.
     * 
     * @param studentId the id of the student.
     * @param degreeId the id of the degree.
     */
    public void removeDegreeFromStudent( int studentId, int degreeId ){
		Student student = studentDao.getStudent(studentId);
		Degree degree = degreeDao.getDegree(degreeId);
		student.getDegrees().remove(degree);
	}

    /**
     * Checks if a student has the required courses of a degree.
     * 
     * @param studentId the id of the student.
     * @param degreeId the id of the degree.
     * @return true/false.
     */
    public boolean studentFulfillsDegreeRequirements( int studentId, int degreeId ){
    	return studentDao.getStudent(studentId).getCourses().containsAll(degreeDao.getDegree(degreeId).getRequiredCourses());
	}
}
