package no.uio.inf5750.assignment2.gui.controller;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import no.uio.inf5750.assignment2.model.Student;
import no.uio.inf5750.assignment2.model.Course;
import no.uio.inf5750.assignment2.model.Degree;
import no.uio.inf5750.assignment2.service.StudentSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApiController{

	static Logger logger = Logger.getLogger(ApiController.class);

	@Autowired
	private StudentSystem studentSystem;
	
	
	@RequestMapping(value="/api/student", method = {RequestMethod.GET})	
	public @ResponseBody Collection<Student> getAllStudents(){
		
		Collection<Student> students = studentSystem.getAllStudents();
		return students;
	}
	
	@RequestMapping(value="/api/student/{studentId}/location", method = {RequestMethod.POST},
					headers = {"Content-type=application/json;charset=UTF-8"})	
	public @ResponseBody Collection<Student> setLocation(@PathVariable("studentId") int studentId,
										@RequestBody String longitude, 
										@RequestBody String latitude){
		Student student = studentSystem.getStudent(studentId);
		student.setLongitude(longitude);
		student.setLatitude(latitude);
				
		Collection<Student> students = studentSystem.getAllStudents();
		return students;
	}
	
	/*@RequestMapping(value = "/student/{studentId}/enrollcourse", method = RequestMethod.POST)
	public String enrollCourse(ModelMap model,
			@PathVariable("studentId") int studentId,
			@RequestParam("courseid") int courseId) {

		logger.debug("Enrolling student " + studentId + " in course "
				+ courseId);
		studentSystem.addAttendantToCourse(courseId, studentId);
		populateModel(model);
		return "student";
	}*/
	
	
	@RequestMapping(value="/api/course", method = RequestMethod.GET)
	@ResponseBody
	public Collection<Course> getAllCourses(){
		
		Collection<Course> courses = studentSystem.getAllCourses();
		return courses;
	}
	
	@RequestMapping(value="/api/degree", method = RequestMethod.GET)
	@ResponseBody
	public Collection<Degree> getAllDegrees(){
		
		Collection<Degree> degrees = studentSystem.getAllDegrees();
		return degrees;
	}
	
}