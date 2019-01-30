package no.uio.inf5750.assignment2.dao.hibernate;
import java.util.Collection;
import no.uio.inf5750.assignment2.model.Degree;
import no.uio.inf5750.assignment2.dao.*;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

public class HibernateDegreeDao implements DegreeDAO {
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
    /**
     * Persists a degree. An unique id is generated if the object is persisted
     * for the first time, and which is both set in the given degree object and
     * returned.
     * 
     * @param degree the degree to add for persistence.
     * @return the id of the degree.
     */
    public int saveDegree( Degree degree ){
		return (Integer) sessionFactory.getCurrentSession().save(degree);
	}

    /**
     * Returns a degree.
     * 
     * @param id the id of the degree to return.
     * @return the degree or null if it doesn't exist.
     */
    public Degree getDegree( int id ){
		return (Degree) sessionFactory.getCurrentSession().get(Degree.class, id);
	}

    /**
     * Returns a degree with a specific type.
     * 
     * @param type the type of the degree to return.
     * @return the degree or null if it doesn't exist.
     */
    public Degree getDegreeByType( String type ){
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Degree where type = :type");
		query.setString("type", type);
		return (Degree) query.uniqueResult();
    }

    /**
     * Returns all degrees.
     * 
     * @return all degrees.
     */
    @SuppressWarnings("unchecked")
	public Collection<Degree> getAllDegrees(){
		Session session = sessionFactory.getCurrentSession();
		Collection<Degree> degrees = session.createQuery("from Degree").list();
		return degrees;
	}
    /**
     * Deletes a degree.
     * 
     * @param degree the degree to delete.
     */
    public void delDegree( Degree degree ){
		Session session = sessionFactory.getCurrentSession();
		session.delete(degree);
	}	
}
