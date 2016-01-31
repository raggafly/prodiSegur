package util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;


import model.IbCustomer;
import model.IbCustomerRelation;
import model.IbInsurance;
import model.IbMasterValue;
import model.TableInfoRelation;

public class MasterValueUtil {

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
	
	public static IbMasterValue getMasterFindObejctByValor(String valor){
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		TypedQuery<IbMasterValue> query = em.createNamedQuery("IbMasterValue.findByObjectValue", IbMasterValue.class);
		query.setParameter("valor", valor);
		List<IbMasterValue> listMasterValueByType = query.getResultList();
		IbMasterValue mv = new IbMasterValue();
		if (listMasterValueByType.size()>0){
			mv = listMasterValueByType.get(0);
		}
		
		emf.close();
		return mv;
		
	}
	
	public static IbInsurance getInsurance(String numeroPoliza) {
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		TypedQuery<IbInsurance> query = em.createNamedQuery("IbInsurance.findByPoliza", IbInsurance.class);
		query.setParameter("poliza", numeroPoliza);
		List<IbInsurance> listInsurance = query.getResultList();
		IbInsurance insurance = null;
		if (listInsurance.size() > 0) {
			insurance = listInsurance.get(0);
		}
		em.close();
		return insurance;
	}
	
	public static IbCustomer getCustomer(String dni) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		TypedQuery<IbCustomer> query = em.createNamedQuery("IbCustomer.findDNI", IbCustomer.class);
		query.setParameter("dni", dni);
		List<IbCustomer> listCustomer = query.getResultList();
		IbCustomer cus = null;
		if (listCustomer.size() > 0) {
			cus = listCustomer.get(0);
		}
		return cus;
	}
	
	public static IbCustomer getRelationCustomerInsurance(IbInsurance seguro) {
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		TypedQuery<IbCustomerRelation> query = em.createNamedQuery("IbCustomerRelation.findSeguro", IbCustomerRelation.class);
		query.setParameter("idseguro", seguro);
		List<IbCustomerRelation> listInsurance = query.getResultList();
		IbCustomerRelation insuRelation = null;
		
		IbCustomer cus = null;
		for (int i = 0; i < listInsurance.size(); i++) {

			if (listInsurance.get(i).getIbCustomerType().getDescripcion().equals("PROPIETARIO")) {
				cus = listInsurance.get(i).getIbCustomer();
				
				
			}
		}
		em.close();
		return cus;
	}

	public static IbInsurance getInsuranceById(String idInsurance) {
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		IbInsurance insurance = em.find(IbInsurance.class, Integer.parseInt(idInsurance));
		return insurance;
	}
	
}
