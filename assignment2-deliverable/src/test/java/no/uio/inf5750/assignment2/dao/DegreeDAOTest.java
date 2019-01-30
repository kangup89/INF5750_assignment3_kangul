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
import no.uio.inf5750.assignment2.model.Degree;

@ContextConfiguration(locations = { "classpath*:/META-INF/assignment2/beans.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DegreeDAOTest{
	
	@Autowired
	DegreeDAO degreeDao;
	
	public void setDegreeDao(DegreeDAO degreeDao){
		this.degreeDao = degreeDao;
	}
	
	@Test
	public void testSaveDegree(){
		Degree degree = new Degree("master");
		int id = degreeDao.saveDegree(degree);
		
		assertEquals(degree, degreeDao.getDegree(id));
		assertEquals(degree, degreeDao.getDegreeByType("master"));
		
		assertNull(degreeDao.getDegree(-1));
		assertNull(degreeDao.getDegreeByType("bachelor"));
	}

	@Test
	public void testGetAllDegrees(){
		Degree degree1 = new Degree("test1");
		Degree degree2 = new Degree("test2");
		Degree degree3 = new Degree("test3");
		degreeDao.saveDegree(degree1);
		degreeDao.saveDegree(degree2);
		degreeDao.saveDegree(degree3);
		
		Collection<Degree> degrees = degreeDao.getAllDegrees();
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
	public void testDelDegree(){
		Degree degree1 = new Degree("test1");
		Degree degree2 = new Degree("test2");
		Degree degree3 = new Degree("test3");
		int id1 = degreeDao.saveDegree(degree1);
		int id2 = degreeDao.saveDegree(degree2);
		int id3 = degreeDao.saveDegree(degree3);
		
		degreeDao.delDegree(degree1);
		
		assertNull(degreeDao.getDegree(id1));
		assertNull(degreeDao.getDegreeByType("test1"));
		
		Collection<Degree> degrees = degreeDao.getAllDegrees();
		
		assertFalse(degrees.contains(degree1));
		assertNotSame(degree1, degrees.iterator().next());
		
		degreeDao.delDegree(degree2);
		degreeDao.delDegree(degree3);
		
		degrees = degreeDao.getAllDegrees();
		
		assertNull(degreeDao.getDegree(id2));
		assertNull(degreeDao.getDegreeByType("test2"));
		assertNull(degreeDao.getDegree(id3));
		assertNull(degreeDao.getDegreeByType("test3"));
		
		Iterator<Degree> degrees_iterator = degrees.iterator();
		assertFalse(degrees_iterator.hasNext());
	}
}
	
	