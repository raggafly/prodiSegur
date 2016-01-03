package util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import model.IbCuotesTime;
import model.IbMasterValue;
import model.MasterTypes;

public class CalculateCuotes {
	public static IbCuotesTime getNumberCuotes (String duracion, String formaPago){
		
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		//sacamos el codigo de la duracion masterValues
		
		IbMasterValue imv = util.masterValueUtil.getMasterValueByValorAndTipo(duracion, MasterTypes.TYPE_DURACION);
		duracion =imv.getValor();
		
		imv = util.masterValueUtil.getMasterValueByValorAndTipo(formaPago, MasterTypes.TYPE_FORMA_PAGO);
		formaPago =imv.getValor();
		
		TypedQuery<IbCuotesTime> query = em.createNamedQuery("IbCuotesTime.findAllByDurAndFP", IbCuotesTime.class);
		query.setParameter("duracion", duracion);
		query.setParameter("formaPago", formaPago);
		List<IbCuotesTime> listCuotes = query.getResultList();
		IbCuotesTime ct = new IbCuotesTime();
		if (listCuotes.size()>0){
			ct = listCuotes.get(0);
		}
		
		emf.close();
		
	
		return ct;
	}
}
