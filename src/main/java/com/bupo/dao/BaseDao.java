package com.bupo.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

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
