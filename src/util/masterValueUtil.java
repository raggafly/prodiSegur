package util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import model.IbMasterValue;

public class masterValueUtil {

	public static IbMasterValue getMasterValueByValor (String desc){
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		TypedQuery<IbMasterValue> query = em.createNamedQuery("IbMasterValue.findAllByValor", IbMasterValue.class);
		query.setParameter("desc", desc);
		List<IbMasterValue> listMasterValueByType = query.getResultList();
		IbMasterValue mv = new IbMasterValue();
		if (listMasterValueByType.size()>0){
			mv = listMasterValueByType.get(0);
		}
		
		emf.close();
		return mv;
		
	}
	
	public static IbMasterValue getMasterValueByValorAndTipo (String desc, String tipo){
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		TypedQuery<IbMasterValue> query = em.createNamedQuery("IbMasterValue.findAllByValorAndTipo", IbMasterValue.class);
		query.setParameter("desc", desc);
		query.setParameter("tipo", tipo);
		List<IbMasterValue> listMasterValueByType = query.getResultList();
		IbMasterValue mv = new IbMasterValue();
		if (listMasterValueByType.size()>0){
			mv = listMasterValueByType.get(0);
		}
		
		emf.close();
		return mv;
		
	}
	
	public static String getMasterFindDescriptionByValor(String valor){
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		TypedQuery<String> query = em.createNamedQuery("IbMasterValue.findByValue", String.class);
		query.setParameter("valor", valor);
		List<String> listMasterValueByType = query.getResultList();
		String mv = "";
		if (listMasterValueByType.size()>0){
			mv = listMasterValueByType.get(0);
		}
		
		emf.close();
		return mv;
		
	}
	
}
