package no.uio.inf5750.assignment2.service;

import static junit.framework.Assert.*;

import java.util.Collection;
import java.util.Iterator;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import no.uio.inf5750.assignment2.model.Student;
import no.uio.inf5750.assignment2.model.Course;
import no.uio.inf5750.assignment2.model.Degree;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath*:/META-INF/assignment2/beans.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class StudentSystemTest{
		
	@Autowired
	private StudentSystem studentSystem;
	
	public void setStudentSystem(StudentSystem studentSystem){
		this.studentSystem = studentSystem;
	}
	
	@Test
	public void testAddCourse(){
		Course course = new Course("INF5750", "Open Source Development");
		int inf5750 = studentSystem.addCourse("INF5750",
				"Open Source Development");
		
		assertEquals(course, studentSystem.getCourse(inf5750));
		assertEquals(course, studentSystem.getCourseByCourseCode("INF5750"));
		assertEquals(course, studentSystem.getCourseByName("Open Source Development"));
		assertNull(studentSystem.getCourse(-1));
		assertNull(studentSystem.getCourseByCourseCode(null));
		assertNull(studentSystem.getCourseByName(null));
	}
	
	@Test
	public void studentLocation(){
		int id = studentSystem.addStudent("kangul");
		
		assertNull(studentSystem.getStudent(id).getLatitude());
		assertNull(studentSystem.getStudent(id).getLongitude());
		
		studentSystem.setStudentLocation(id, "latitude", "longitude");
		
		assertEquals(studentSystem.getStudent(id).getLatitude(), "latitude");
		assertEquals(studentSystem.getStudent(id).getLongitude(), "longitude");
	}
	
	@Test
	public void testUpdateCourse(){
		Course course = new Course("INF5761", "Health management information systems");
		int id = studentSystem.addCourse("INF5750",
				"Open Source Development");
		
		studentSystem.updateCourse(id, "INF5761", "Health management information systems");
		
		assertNull(studentSystem.getCourseByCourseCode("INF5750"));
		assertNull(studentSystem.getCourseByName("Open Source Development"));
		assertEquals(course, studentSystem.getCourse(id));
		assertEquals(course, studentSystem.getCourseByCourseCode("INF5761"));
		assertEquals(course, studentSystem.getCourseByName("Health management information systems"));		
	}
	
	@Test
	public void testGetAllCourses(){
		Course course1 = new Course("INF1000", "aaa");
		Course course2 = new Course("INF2000", "bbb");
		Course course3 = new Course("INF3000", "ccc");
		studentSystem.addCourse("INF1000", "aaa");
		studentSystem.addCourse("INF2000", "bbb");
		studentSystem.addCourse("INF3000", "ccc");
		
		Collection<Course> courses = studentSystem.getAllCourses();
		assertTrue((courses.size()==3));
		assertTrue(courses.contains(course1));
		assertTrue(courses.contains(course2));
		assertTrue(courses.contains(course3));		
		
		Iterator<Course> courses_iterator = courses.iterator();
		assertEquals(course1, courses_iterator.next());
		assertEquals(course2, courses_iterator.next());
		assertEquals(course3, courses_iterator.next());
		assertFalse(courses_iterator.hasNext());
	}
	
	@Test
	public void addDegree(){
		Degree master = new Degree("Master");
		Degree bachelor = new Degree("Bachelor");
		Degree phd = new Degree("PhD");
		int id1 = studentSystem.addDegree("Master");
		int id2 = studentSystem.addDegree("Bachelor");
		int id3 = studentSystem.addDegree("PhD");
		
		assertEquals(master, studentSystem.getDegree(id1));
		assertEquals(master, studentSystem.getDegreeByType("Master"));
		assertEquals(bachelor, studentSystem.getDegree(id2));
		assertEquals(bachelor, studentSystem.getDegreeByType("Bachelor"));
		assertEquals(phd, studentSystem.getDegree(id3));
		assertEquals(phd, studentSystem.getDegreeByType("PhD"));
		assertNull(studentSystem.getCourse(-1));
		assertNull(studentSystem.getCourseByName(null));
	}
	
	@Test
	public void testUpdateDegree(){
		Degree degree = new Degree("Master");
		int id = studentSystem.addDegree("Bachelor");
		
		studentSystem.updateDegree(id, "Master");
		
		assertNull(studentSystem.getDegreeByType("Bachelor"));
		assertEquals(degree, studentSystem.getDegree(id));
		assertEquals(degree, studentSystem.getDegreeByType("Master"));
	}

	@Test
	public void testGetAllDegrees(){
		Degree degree1 = new Degree("Master");
		Degree degree2 = new Degree("Bachelor");
		Degree degree3 = new Degree("PhD");
		studentSystem.addDegree("Master");
		studentSystem.addDegree("Bachelor");
		studentSystem.addDegree("PhD");
		
		Collection<Degree> degrees = studentSystem.getAllDegrees();
		assertTrue((degrees.size()==3));
		assertTrue(degrees.contains(degree1));
		assertTrue(degrees.contains(degree2));
		assertTrue(degrees.contains(degree3));		
		
		Iterator<Degree> degrees_iterator = degrees.iterator();
		assertEquals(degree1, degrees_iterator.next());
		assertEquals(degree2, degrees_iterator.next());
		assertEquals(degree3, degrees_iterator.next());
		assertFalse(degrees_iterator.hasNext());
	}
	
	@Test
	public void testAddStudent(){
		Student student = new Student("John McClane");
		int id = studentSystem.addStudent("John McClane");
		
		assertEquals(student, studentSystem.getStudent(id));
		assertEquals(student, studentSystem.getStudentByName("John McClane"));
		
		assertNull(studentSystem.getStudent(-1));
		assertNull(studentSystem.getStudentByName("Jane Fonda"));
	}
	
	@Test
	public void testUpdateStudent(){
		Student student = new Student("John McClane");
		int id = studentSystem.addStudent("Jane Fonda");
		
		studentSystem.updateStudent(id, "John McClane");
		
		assertNull(studentSystem.getStudentByName("Jane Fonda"));
		assertEquals(student, studentSystem.getStudent(id));
		assertEquals(student, studentSystem.getStudentByName("John McClane"));
	}
	
	@Test
	public void testGetAllStudents(){
		Student student1 = new Student("test1");
		Student student2 = new Student("test2");
		Student student3 = new Student("test3");
		studentSystem.addStudent("test1");
		studentSystem.addStudent("test2");
		studentSystem.addStudent("test3");
		
		Collection<Student> students = studentSystem.getAllStudents();
		
		assertTrue((students.size()==3));
		assertTrue(students.contains(student1));
		assertTrue(students.contains(student2));
		assertTrue(students.contains(student3));	
		
		Iterator<Student> students_iterator = students.iterator();
		assertEquals(student1, students_iterator.next());
		assertEquals(student2, students_iterator.next());
		assertEquals(student3, students_iterator.next());
		assertFalse(students_iterator.hasNext());
	}
	
	@Test
	public void testAddAttendantToCourse(){
		Student student = new Student("John McClane");
		int john = studentSystem.addStudent("John McClane");
		Course course = new Course("INF5750", "Open Source Development");
		int inf5750 = studentSystem.addCourse("INF5750",
				"Open Source Development");
		
		assertTrue(studentSystem.getStudent(john).getCourses().isEmpty());
		assertTrue(studentSystem.getCourse(inf5750).getAttendants().isEmpty());
		
		studentSystem.addAttendantToCourse(inf5750, john);
		
		assertFalse(studentSystem.getStudent(john).getCourses().isEmpty());
		assertFalse(studentSystem.getCourse(inf5750).getAttendants().isEmpty());
		
		assertEquals(student, studentSystem.getCourse(inf5750).getAttendants().iterator().next());
		assertEquals(course, studentSystem.getStudent(john).getCourses().iterator().next());
		
		studentSystem.removeAttendantFromCourse(inf5750, john);
		
		assertTrue(studentSystem.getStudent(john).getCourses().isEmpty());
		assertTrue(studentSystem.getCourse(inf5750).getAttendants().isEmpty());
	}
	
	@Test
	public void testAddRequiredCourseToDegree(){
		Course course = new Course("INF5750", "Open Source Development");
		int inf5750 = studentSystem.addCourse("INF5750",
				"Open Source Development");
		int master = studentSystem.addDegree("Master");
		
		assertTrue(studentSystem.getDegree(master).getRequiredCourses().isEmpty());
		
		studentSystem.addRequiredCourseToDegree(master, inf5750);
		
		assertFalse(studentSystem.getDegree(master).getRequiredCourses().isEmpty());		
		assertEquals(course, studentSystem.getDegree(master).getRequiredCourses().iterator().next());
		
		studentSystem.removeRequiredCourseFromDegree(master, inf5750);
		
		assertTrue(studentSystem.getDegree(master).getRequiredCourses().isEmpty());
	}
	
	@Test
	public void testAddDegreeToStudent(){
		Degree degree = new Degree("Master");
		int master = studentSystem.addDegree("Master");
		int john = studentSystem.addStudent("John McClane");
		
		assertTrue(studentSystem.getStudent(john).getDegrees().isEmpty());
		
		studentSystem.addDegreeToStudent(john, master);
		
		assertFalse(studentSystem.getStudent(john).getDegrees().isEmpty());
		assertEquals(degree, studentSystem.getStudent(john).getDegrees().iterator().next());
		
		studentSystem.removeDegreeFromStudent(john, master);
		
		assertTrue(studentSystem.getStudent(john).getDegrees().isEmpty());
	}
	
	@Test
	public void testStudentFulfillsDegreeRequirements(){
		int john = studentSystem.addStudent("John McClane");
		int inf5750 = studentSystem.addCourse("INF5750",
				"Open Source Development");
		int master = studentSystem.addDegree("Master");
		
		assertTrue(studentSystem.studentFulfillsDegreeRequirements(john, master));
		
		studentSystem.addRequiredCourseToDegree(master, inf5750);
		
		assertFalse(studentSystem.studentFulfillsDegreeRequirements(john, master));
		
		studentSystem.addAttendantToCourse(inf5750, john);
		
		assertTrue(studentSystem.studentFulfillsDegreeRequirements(john, master));
	}
	
	@Test
	public void testDelCourse(){
		int john = studentSystem.addStudent("John McClane");
		Course course = new Course("INF5750", "Open Source Development");
		int inf5750 = studentSystem.addCourse("INF5750",
				"Open Source Development");
		int master = studentSystem.addDegree("Master");
		
		studentSystem.addAttendantToCourse(inf5750, john);
		studentSystem.addRequiredCourseToDegree(master, inf5750);
		
		assertEquals(course, studentSystem.getDegree(master).getRequiredCourses().iterator().next());
		assertEquals(course, studentSystem.getStudent(john).getCourses().iterator().next());
		
		studentSystem.delCourse(inf5750);
		
		assertNull(studentSystem.getCourse(inf5750));
		assertTrue(studentSystem.getDegree(master).getRequiredCourses().isEmpty());
		assertTrue(studentSystem.getStudent(john).getCourses().isEmpty());
	}
	
	@Test
	public void testDelDegree(){
		int john = studentSystem.addStudent("John McClane");
		Degree degree = new Degree("Master");
		int master = studentSystem.addDegree("Master");
		
		studentSystem.addDegreeToStudent(john, master);

		assertEquals(degree, studentSystem.getStudent(john).getDegrees().iterator().next());
		
		studentSystem.delDegree(master);
		
		assertNull(studentSystem.getDegree(master));
		assertTrue(studentSystem.getStudent(john).getDegrees().isEmpty());
	}
	
	@Test
	public void testDelStudent(){
		Student student = new Student("John McClane");
		int john = studentSystem.addStudent("John McClane");
		Course course = new Course("INF5750", "Open Source Development");
		int inf5750 = studentSystem.addCourse("INF5750",
				"Open Source Development");
		
		studentSystem.addAttendantToCourse(inf5750, john);
		
		assertEquals(student, studentSystem.getCourse(inf5750).getAttendants().iterator().next());
		assertEquals(course, studentSystem.getStudent(john).getCourses().iterator().next());
		
		studentSystem.delStudent(john);
		
		assertNull(studentSystem.getStudent(john));
		assertTrue(studentSystem.getCourse(inf5750).getAttendants().isEmpty());
	}
}
