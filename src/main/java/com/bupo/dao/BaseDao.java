package com.bupo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;

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

	public <T> T findById(Class<T> entityClass, Object primaryKey) {
		EntityManager em = getEMF().createEntityManager();
		try {
			return em.find(entityClass, primaryKey);
		} finally {
			em.close();
		}
	}

	public <T> List<T> findByNamedQuery(String queryName, Map<String, String> paramsMap, Class<T> entityClass) {
		Preconditions.checkNotNull(queryName, "Query Name is null");
		Preconditions.checkNotNull(paramsMap, "paramsMap is null, send empty list if there are no params");

		EntityManager em = getEMF().createEntityManager();
		List<T> resultList = new ArrayList<T>();
		try {
			Query query = em.createNamedQuery(queryName);
			for (Map.Entry<String, String> param : paramsMap.entrySet()) {
				System.out.println("param value" + param.getValue());
				query.setParameter(param.getKey(), param.getValue());
			}

			resultList = query.getResultList();

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
