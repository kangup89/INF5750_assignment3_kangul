package no.uio.inf5750.assignment2.dao;

import static junit.framework.Assert.*;

import java.util.Collection;
import java.util.Iterator;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import no.uio.inf5750.assignment2.model.Student;

@ContextConfiguration(locations = { "classpath*:/META-INF/assignment2/beans.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class StudentDAOTest{
	
	@Autowired
	StudentDAO studentDao;
	
	public void setStudentDao(StudentDAO studentDao){
		this.studentDao = studentDao;
	}
	
	@Test
	public void testSaveStudent(){
		Student student = new Student("kangul");
		int id = studentDao.saveStudent(student);
		
		assertEquals(student, studentDao.getStudent(id));
		assertEquals(student, studentDao.getStudentByName("kangul"));
		
		assertNull(studentDao.getStudent(-1));
		assertNull(studentDao.getStudentByName("John"));
	}
	
	public void testSetLocation(){
		Student student = new Student("kangul");
		student.setLongitude("longitude");
		student.setLatitude("latitude");
		int id = studentDao.saveStudent(student);
		
		assertEquals("longitudet", studentDao.getStudent(id).getLongitude());
		assertEquals("latitude", studentDao.getStudent(id).getLatitude());
	}

	@Test
	public void testGetAllStudents(){
		Student student1 = new Student("test1");
		Student student2 = new Student("test2");
		Student student3 = new Student("test3");
		studentDao.saveStudent(student1);
		studentDao.saveStudent(student2);
		studentDao.saveStudent(student3);
		
		Collection<Student> students = studentDao.getAllStudents();
		
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
	public void testDelStudent(){
		Student student1 = new Student("test1");
		Student student2 = new Student("test2");
		Student student3 = new Student("test3");
		int id1 = studentDao.saveStudent(student1);
		int id2 = studentDao.saveStudent(student2);
		int id3 = studentDao.saveStudent(student3);
		
		studentDao.delStudent(student1);
		
		assertNull(studentDao.getStudent(id1));
		assertNull(studentDao.getStudentByName("test1"));
		
		Collection<Student> students = studentDao.getAllStudents();
		
		assertFalse(students.contains(student1));
		assertNotSame(student1, students.iterator().next());
		
		studentDao.delStudent(student2);
		studentDao.delStudent(student3);
		
		students = studentDao.getAllStudents();
		
		assertNull(studentDao.getStudent(id2));
		assertNull(studentDao.getStudentByName("test2"));
		assertNull(studentDao.getStudent(id3));
		assertNull(studentDao.getStudentByName("test3"));
		
		Iterator<Student> students_iterator = students.iterator();
		assertFalse(students_iterator.hasNext());
	}	
}
	
	