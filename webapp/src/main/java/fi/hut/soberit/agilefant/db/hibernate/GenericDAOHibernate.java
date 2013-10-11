package fi.hut.soberit.agilefant.db.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import fi.hut.soberit.agilefant.db.GenericDAO;

/**
 * Generically implements basic DAO functionality specified by GenericDAO.
 * <p>
 * All the concrete DAOs under this same package inherit from this class. They
 * also implement the corresponding DAO interface, "delegating" method
 * implementations to this class.
 * 
 * @param <T>
 *            type of the entity bean / data model object the DAO is for
 * @see fi.hut.soberit.agilefant.db.GenericDAO
 */
public abstract class GenericDAOHibernate<T> implements GenericDAO<T> {

    private Class<T> clazz;

    protected SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory cannot be null");
        }
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected GenericDAOHibernate(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected Class<T> getPersistentClass() {
        return clazz;
    }

    /** {@inheritDoc} */
    public T get(int id) {
        return (T) this.getCurrentSession().get(getPersistentClass(), id);
    }

    /** {@inheritDoc} */
    public T getAndDetach(int id) {
        T object = (T) this.get(id);
        this.sessionFactory.getCurrentSession().evict(object);
        return object;
    }

    /** {@inheritDoc} */
    public Collection<T> getAll() {
        return this.asList(this.createCriteria(this.getPersistentClass()).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
    }

    /** {@inheritDoc} */
    public Collection<T> getMultiple(Collection<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<T>();
        }
        Criteria c = this.createCriteria(getPersistentClass());
        c.add(Restrictions.in("id", ids));
        return asCollection(c);
    }

    /** {@inheritDoc} */
    public void remove(int id) {
        this.remove(this.get(id));
    }

    /** {@inheritDoc} */
    public void remove(T object) {
        this.getCurrentSession().delete(object);
    }

    /** {@inheritDoc} */
    public void store(T object) {
        this.getCurrentSession().saveOrUpdate(object);
    }

    /** {@inheritDoc} */
    public Serializable create(T object) {
        return this.getCurrentSession().save(object);
    }

    protected T getFirst(Collection<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.iterator().next();
    }

    protected <ResultType> ResultType getFirstTypeSafe(Collection<ResultType> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.iterator().next();
    }

    protected Criteria createCriteria(Class clazz) {
        return this.getCurrentSession().createCriteria(clazz);
    }

    protected Criteria createCriteria(Class clazz, String alias) {
        return this.getCurrentSession().createCriteria(clazz, alias);
    }

    public int count() {
        Criteria criteria = this.createCriteria(this.getPersistentClass());
        criteria.setProjection(Projections.rowCount());
        return ((Long) this.uniqueResult(criteria)).intValue();
    }

    public boolean exists(int id) {
        Criteria criteria = this.createCriteria(this.getPersistentClass());
        criteria.add(Restrictions.idEq(id)).setProjection(Projections.rowCount());
        return ((Long) this.uniqueResult(criteria)).intValue() > 0;
    }

    public Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    protected <ResultType> Collection<ResultType> asCollection(Criteria criteria) {
        Collection<ResultType> list = this.asList(criteria);
        if (list == null) {
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    protected <ResultType> List<ResultType> asList(Criteria criteria) {
        List<ResultType> list = criteria.list();
        if (list == null) {
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    protected <ResultType> ResultType firstResult(Criteria criteria) {
        List<ResultType> list = asList(criteria);
        return getFirstTypeSafe(list);
    }

    @SuppressWarnings("unchecked")
    protected <ResultType> ResultType uniqueResult(Criteria criteria) {
        return (ResultType) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    protected List<T> asList(Query query) {
        return query.list();
    }

    @SuppressWarnings("unchecked")
    protected List<Object[]> asTuplesList(Query query) {
        return query.list();
    }

}
