package main.java.com.uwlighthouse.photocontest.daos;
import static com.google.common.collect.Lists.newArrayList;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import main.java.com.uwlighthouse.photocontest.hibernate.HibernateUtil;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

/**
 * {@link GenericDao} that uses Hibernate.
 * @see "http://community.jboss.org/wiki/GenericDataAccessObjects"
 * @param <T> Type of DatabaseObject.
 * @param <ID> Type of ID.
 */
public abstract class GenericDao<T, ID extends Serializable> {

	private final Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public GenericDao() {
		this.persistentClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected Session getSession() {
		return HibernateUtil.getSession();
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	@SuppressWarnings("unchecked")
	public T findById(ID id) {
		return (T)getSession().load(getPersistentClass(), id);
	}

	public List<T> findAll() {
		return findByCriteria();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		Example example =  Example.create(exampleInstance);
		for (String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		crit.add(example);
		return crit.list();
	}

	public T makePersistent(T entity) {
		getSession().saveOrUpdate(entity);
		return entity;
	}

	public List<T> makePersistent(List<T> entities) {
		List<T> perisistedEntities = newArrayList();
		for (T entity : entities) {
			perisistedEntities.add(makePersistent(entity));
		}
		return perisistedEntities;
	}

	public void makeTransient(T entity) {
		getSession().delete(entity);
	}

	public void makeTransient(List<T> entities) {
		for (T entity : entities) {
			getSession().delete(entity);
		}
	}

	public T refresh(T entity) {
		getSession().refresh(entity);
		return entity;
	}

	public void flush() {
		getSession().flush();
	}

	public void clear() {
		getSession().clear();
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		Criteria crit = getCriteria();
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();
	}

	protected Criteria getCriteria() {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		return crit;
	}
}