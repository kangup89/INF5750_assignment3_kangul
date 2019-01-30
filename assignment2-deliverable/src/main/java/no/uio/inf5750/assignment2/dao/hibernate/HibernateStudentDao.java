package no.uio.inf5750.assignment2.dao.hibernate;
import java.util.Collection;
import no.uio.inf5750.assignment2.model.Student;
import no.uio.inf5750.assignment2.dao.*;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

public class HibernateStudentDao implements StudentDAO {
	
	private SessionFactory sessionFactory;	
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	
    /**
     * Persists a student. An unique id is generated if the object is persisted
     * for the first time, and which is both set in the given student object and
     * returned.
     * 
     * @param student the student to add for persistence.
     * @return the id of the student.
     */
    public int saveStudent( Student student ){
		return (Integer) sessionFactory.getCurrentSession().save(student);
	}

    /**
     * Returns a student.
     * 
     * @param id the id of the student to return.
     * @return the student or null if it doesn't exist.
     */
    public Student getStudent( int id ){ 
		return (Student) sessionFactory.getCurrentSession().get(Student.class, id);
	}

    /**
     * Returns a student with a specific name.
     * 
     * @param name the name of the student to return.
     * @return the student or null if it doesn't exist.
     */
    public Student getStudentByName( String name ){
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Student where name = :name");
		query.setString("name", name);
		return (Student) query.uniqueResult();
	}

    /**
     * Returns all students.
     * 
     * @return all students.
     */
    @SuppressWarnings("unchecked")
    public Collection<Student> getAllStudents(){
		Session session = sessionFactory.getCurrentSession();
		Collection<Student> students = session.createQuery("from Student").list();
		return students;
	}

    /**
     * Deletes a student.
     * 
     * @param student the student to delete.
     */
    public void delStudent( Student student ){
		Session session = sessionFactory.getCurrentSession();
		session.delete(student);
	}
}
