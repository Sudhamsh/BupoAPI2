package com.bupo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.bupo.dao.model.UserAuto;

public class DBTest {

	private static final String PERSISTENCE_UNIT_NAME = "todos";
	private static EntityManagerFactory factory;

	public static void main(String[] args) {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();
		// read the existing entries and write to console
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object> criteriaQuery = cb.createQuery();

		Root<UserAuto> from = criteriaQuery.from(UserAuto.class);
		CriteriaQuery<Object> select = criteriaQuery.select(from);
		TypedQuery<Object> typedQuery = em.createQuery(select);
		List<Object> resultlist = typedQuery.getResultList();
		System.out.println(resultlist.size());

		em.close();
	}

}
