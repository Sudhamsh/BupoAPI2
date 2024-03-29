package com.bupo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;

public class BaseDao {

	private static final String PERSISTENCE_UNIT_NAME = "todos";
	private static EntityManagerFactory emf;
	static Logger logger = Logger.getLogger(BaseDao.class.getName());

	public EntityManagerFactory getEMF() {
		logger.debug("In getEMF");

		if (emf == null) {
			emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		}
		return emf;
	}

	public <E> void create(E entity) {
		EntityManager em = getEMF().createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(entity);
			em.flush();
			em.getTransaction().commit();

		} finally {
			em.close();
		}
	}

	public <E> void update(E entity) {
		EntityManager em = getEMF().createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(entity);
			em.flush();
			em.getTransaction().commit();

		} finally {
			em.close();
		}
	}

	public <T> T findById(Class<T> entityClass, Object primaryKey) {
		EntityManager em = getEMF().createEntityManager();
		try {
			return em.find(entityClass, primaryKey);
		} finally {
			em.close();
		}
	}

	public <T> List<T> findByField(Class<T> entityClass, String fieldName, Object value, int maxResults) {
		EntityManager em = getEMF().createEntityManager();
		List<T> result = new ArrayList<T>();
		try {
			CriteriaBuilder queryBuilder = em.getCriteriaBuilder();
			CriteriaQuery<T> criteriaQuery = queryBuilder.createQuery(entityClass);
			Root<T> customer = criteriaQuery.from(entityClass);
			criteriaQuery.select(customer).where(queryBuilder.equal(customer.get(fieldName), value));

			result = em.createQuery(criteriaQuery).setMaxResults(maxResults).getResultList();

			if (result.size() == 0) {
				throw new EntityNotFoundException("Result not found!");
			}

		} finally {
			em.close();
		}

		return result;
	}

	public <T> List<T> findByNamedQuery(String queryName, Map<String, String> paramsMap, Class<T> entityClass,
			int maxSize) {
		Preconditions.checkNotNull(queryName, "Query Name is null");
		Preconditions.checkNotNull(paramsMap, "paramsMap is null, send empty list if there are no params");

		EntityManager em = getEMF().createEntityManager();
		List<T> resultList = new ArrayList<T>();
		try {
			Query query = em.createNamedQuery(queryName);
			for (Map.Entry<String, String> param : paramsMap.entrySet()) {
				query.setParameter(param.getKey(), param.getValue());
			}

			resultList = query.setMaxResults(maxSize).getResultList();

			if (resultList.size() == 0) {
				throw new EntityNotFoundException("Result not found!");
			}

		} finally {
			em.close();
		}

		return resultList;
	}

	public List<Object[]> findByNativeQuery(String sqlQuery, int maxResults) {
		Preconditions.checkNotNull(sqlQuery, "Query  is null");

		EntityManager em = getEMF().createEntityManager();
		List<Object[]> resultList;
		try {
			Query query = em.createNativeQuery(sqlQuery);
			resultList = query.setMaxResults(maxResults).getResultList();
			if (resultList.size() == 0) {
				throw new EntityNotFoundException("Result not found!");
			}

		} finally {
			em.close();
		}

		return resultList;
	}

	public <E, T> void update(Class<T> entityClass, E entity) {
		EntityManager em = getEMF().createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(entity);
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}

	public <E> void delete(E entity) {
		EntityManager em = getEMF().createEntityManager();
		try {
			em.getTransaction().begin();
			if (!em.contains(entity)) {
				entity = em.merge(entity);
			}
			em.remove(entity);
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}

}
