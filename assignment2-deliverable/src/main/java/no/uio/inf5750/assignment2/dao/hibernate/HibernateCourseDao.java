package no.uio.inf5750.assignment2.dao.hibernate;
import java.util.Collection;
import no.uio.inf5750.assignment2.model.Course;
import no.uio.inf5750.assignment2.dao.*;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

public class HibernateCourseDao implements CourseDAO {
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	/**
     * Persists a course. An unique id is generated if the object is persisted
     * for the first time, and which is both set in the given course object and
     * returned.
     * 
     * @param course the course to add for persistence.
     * @return the id of the course.
     */
	public int saveCourse( Course course ){
		return (Integer) sessionFactory.getCurrentSession().save(course);
	}
	
    /**
     * Returns a course.
     * 
     * @param id the id of the course to return.
     * @return the course or null if it doesn't exist.
     */
    public Course getCourse( int id ){
		return (Course) sessionFactory.getCurrentSession().get(Course.class, id);
	}
	
    /**
     * Returns a course with a specific course code.
     * 
     * @param courseCode the course code of the course to return.
     * @return the course code or null if it doesn't exist.
     */
    public Course getCourseByCourseCode( String courseCode ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Course where courseCode = :courseCode");
		query.setString("courseCode", courseCode);
		return (Course) query.uniqueResult();
	}

    /**
     * Returns a course with a specific name.
     * 
     * @param courseCode the course code of the course to return.
     * @return the course code or null if it doesn't exist.
     */
    public Course getCourseByName( String name ) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Course where name = :name");
		query.setString("name", name);
		return (Course) query.uniqueResult();
	}

    /**
     * Returns all courses.
     * 
     * @return all courses.
     */
    @SuppressWarnings("unchecked")
    public Collection<Course> getAllCourses() {
    	Session session = sessionFactory.getCurrentSession();
		Collection<Course> courses = session.createQuery("FROM Course").list();
		return courses;
	}

    /**
     * Deletes a course.
     * 
     * @param course the course to delete.
     */
    public void delCourse( Course course ) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(course);
	}
}

