package util;

import java.util.List;

import javafx.collections.ObservableList;
import model.IbAccountBank;
import model.IbCuotesInsure;
import model.IbCustomer;
import model.CustomersTypes;
import model.IbInsurance;
import model.IbInsuranceDetail;
import model.TableInfoRelation;

public class InsureCompleteVO {
	String tipoSeguro;
	
	ObservableList<TableInfoRelation> datosClienteRelation;
	
	IbCustomer datosCliente;
	
	IbInsuranceDetail detalleSeguro;
	
	IbAccountBank datosBancarios;

	String nombreCompletoTitular ="";
	
	String dniTitular="";
	
	List <CustomersTypes> listaCustomersType;
	
	List <IbCuotesInsure> listCuotesInsure;
	
	boolean isHandleAlta;
	
	public boolean isHandleAlta() {
		return isHandleAlta;
	}

	public void setHandleAlta(boolean isHandleAlta) {
		this.isHandleAlta = isHandleAlta;
	}

	public List<IbCuotesInsure> getListCuotesInsure() {
		return listCuotesInsure;
	}

	public void setListCuotesInsure(List<IbCuotesInsure> listCuotesInsure) {
		this.listCuotesInsure = listCuotesInsure;
	}

	public IbInsuranceDetail getDetalleSeguro() {
		return detalleSeguro;
	}

	public void setDetalleSeguro(IbInsuranceDetail detalleSeguro) {
		this.detalleSeguro = detalleSeguro;
	}

	public List<CustomersTypes> getListaCustomersType() {
		return listaCustomersType;
	}

	public void setListaCustomersType(List<CustomersTypes> listaCustomersType) {
		this.listaCustomersType = listaCustomersType;
	}

	public String getNombreCompletoTitular() {
		return nombreCompletoTitular;
	}

	public void setNombreCompletoTitular(String nombreCompletoTitular) {
		this.nombreCompletoTitular = nombreCompletoTitular;
	}

	public String getDniTitular() {
		return dniTitular;
	}

	public void setDniTitular(String dniTitular) {
		this.dniTitular = dniTitular;
	}

	public IbAccountBank getDatosBancarios() {
		return datosBancarios;
	}

	public void setDatosBancarios(IbAccountBank datosBancarios) {
		this.datosBancarios = datosBancarios;
	}

	public IbCustomer getDatosCliente() {
		return datosCliente;
	}

	public void setDatosCliente(IbCustomer datosCliente) {
		this.datosCliente = datosCliente;
	}

	IbInsurance datosSeguro;

	public IbInsurance getDatosSeguro() {
		return datosSeguro;
	}

	public void setDatosSeguro(IbInsurance datosSeguro) {
		this.datosSeguro = datosSeguro;
	}

	public ObservableList<TableInfoRelation> getDatosClienteRelation() {
		return datosClienteRelation;
	}

	public void setDatosClienteRelation(ObservableList<TableInfoRelation> datosClienteRelation) {
		this.datosClienteRelation = datosClienteRelation;
	}

	public String getTipoSeguro() {
		return tipoSeguro;
	}

	public void setTipoSeguro(String tipoSeguro) {
		this.tipoSeguro = tipoSeguro;
	}

	public void setDatosDetalleSeguro(IbInsuranceDetail detalleSeguro) {
		// TODO Auto-generated method stub
		this.detalleSeguro = detalleSeguro;
	}
	
	public IbInsuranceDetail getDatosDetalleSeguro (){
		return detalleSeguro;
	}

}
