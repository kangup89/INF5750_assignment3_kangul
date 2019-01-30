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
import no.uio.inf5750.assignment2.model.Course;

@ContextConfiguration(locations = { "classpath*:/META-INF/assignment2/beans.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CourseDAOTest{
	
	@Autowired
	CourseDAO courseDao;
	
	public void setCourseDao(CourseDAO courseDao){
		this.courseDao = courseDao;
	}
	
	@Test
	public void testSaveCourse(){
		Course course = new Course("INF5750", "Open Source Development");
		int id = courseDao.saveCourse(course);
		
		assertEquals(course, courseDao.getCourse(id));
		assertEquals(course, courseDao.getCourseByCourseCode("INF5750"));
		assertEquals(course, courseDao.getCourseByName("Open Source Development"));
		
		assertNull(courseDao.getCourse(-1));
		assertNull(courseDao.getCourseByCourseCode("INF5761"));
		assertNull(courseDao.getCourseByName("Health management information systems"));		
	}

	@Test
	public void testGetAllCourses(){
		Course course1 = new Course("INF1000", "aaa");
		Course course2 = new Course("INF2000", "bbb");
		Course course3 = new Course("INF3000", "ccc");
		courseDao.saveCourse(course1);
		courseDao.saveCourse(course2);
		courseDao.saveCourse(course3);
		
		Collection<Course> courses = courseDao.getAllCourses();
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
	public void testDelCourse(){
		Course course1 = new Course("INF1000", "aaa");
		Course course2 = new Course("INF2000", "bbb");
		Course course3 = new Course("INF3000", "ccc");
		int id1 = courseDao.saveCourse(course1);
		int id2 = courseDao.saveCourse(course2);
		int id3 = courseDao.saveCourse(course3);
		
		courseDao.delCourse(course1);
		
		assertNull(courseDao.getCourse(id1));
		assertNull(courseDao.getCourseByCourseCode("INF1000"));
		assertNull(courseDao.getCourseByName("aaa"));
		
		Collection<Course> courses = courseDao.getAllCourses();
		
		assertFalse(courses.contains(course1));
		assertNotSame(course1, courses.iterator().next());
		
		courseDao.delCourse(course2);
		courseDao.delCourse(course3);
		
		courses = courseDao.getAllCourses();
		
		assertNull(courseDao.getCourse(id2));
		assertNull(courseDao.getCourseByCourseCode("INF2000"));
		assertNull(courseDao.getCourseByName("bbb"));
		assertNull(courseDao.getCourse(id3));
		assertNull(courseDao.getCourseByCourseCode("INF3000"));
		assertNull(courseDao.getCourseByName("ccc"));
		
		Iterator<Course> courses_iterator = courses.iterator();
		assertFalse(courses_iterator.hasNext());
	}
}
	
	