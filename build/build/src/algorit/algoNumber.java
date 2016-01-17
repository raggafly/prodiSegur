package algorit;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.collections.ObservableList;
import model.IbCuotesInsure;
import util.DateUtil;

public class algoNumber {
	public static List<Double> getFirstTotal(Double total, int numCuotas) {
		List<Double> listTotalPorCuota = new ArrayList<Double>();

		double totalPorCuota = total / numCuotas;
		DecimalFormat twoDoubleFormat = new DecimalFormat("#.00");
		totalPorCuota = Double.parseDouble(twoDoubleFormat.format(totalPorCuota).replace(',', '.'));

		double totalTodasLasCuotas = 0;
		totalTodasLasCuotas = totalPorCuota * numCuotas;
		double primeraCuota = 0;

		if (totalTodasLasCuotas != total) {

			double diferencia = total - totalTodasLasCuotas;
			primeraCuota = totalPorCuota + diferencia;
			primeraCuota = Double.parseDouble(twoDoubleFormat.format(primeraCuota).replace(',', '.'));
		} else {
			primeraCuota = totalPorCuota;
		}

		for (int i = 0; i < numCuotas; i++) {
			if (i == 0) {
				listTotalPorCuota.add(primeraCuota);
			} else {
				listTotalPorCuota.add(totalPorCuota);
			}
		}

		return listTotalPorCuota;
	}

	public static boolean comprobarTotal(Double total, ObservableList<IbCuotesInsure> obsCuotesInsure) {
		boolean isCorrect = false;
		double sumTotales = 0;
		for (int i = 0; i < obsCuotesInsure.size(); i++) {
			IbCuotesInsure ci = new IbCuotesInsure();
			ci = obsCuotesInsure.get(i);
			sumTotales += ci.getTotalCuota();
		}

		if(sumTotales==total){
			isCorrect=true;
		}
		
		return isCorrect;
	}

	public static boolean comprobarFechas(LocalDate fechaIni, LocalDate fechaFin,
			ObservableList<IbCuotesInsure> obsCuotesInsure, String formaDePago) {
		int mesesAdicionales =0;
		
		switch (formaDePago) {
		case "ANUAL":
			mesesAdicionales =12;
			break;
		case "SEMESTRAL":
			mesesAdicionales =6;
			break;
		case "TRIMESTRAL":
			mesesAdicionales =3;
			break;	
		case "MENSUAL":
			mesesAdicionales =1;
			break;	
		}
		boolean isCorrect = false;
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date fechaIniDate = DateUtil.LocalDateToDate(fechaIni);
			Date fechaFinDate = DateUtil.LocalDateToDate(fechaFin);
			
			String fInicioDate = dateFormat.format(fechaIniDate).toString();
			String fFinDate = dateFormat.format(fechaFinDate).toString();
			
			IbCuotesInsure ciInicio = new IbCuotesInsure();
			IbCuotesInsure ciFin = new IbCuotesInsure();
			
			ciInicio = obsCuotesInsure.get(0);
			ciFin = obsCuotesInsure.get(obsCuotesInsure.size()-1);
			
			
			
			String fInicio = dateFormat.format(ciInicio.getFechaOficialPago()).toString();
			String fFin = dateFormat.format(DateUtil.LocalDateToDate((DateUtil.dateToLocalDate(ciFin.getFechaOficialPago()).plusMonths(mesesAdicionales)))).toString();
			
			if (fInicio.equals(fInicioDate) && fFinDate.equals(fFin)){
				isCorrect=true;
			}
		
		return isCorrect;
	}

	}
