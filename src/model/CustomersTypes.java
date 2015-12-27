package model;

public class CustomersTypes{
	String tipo;
	
	boolean insertar ;
	
	IbCustomer ibCustomer;

	public IbCustomer getIbCustomer() {
		return ibCustomer;
	}

	public void setIbCustomer(IbCustomer ibCustomer) {
		this.ibCustomer = ibCustomer;
	}

	public boolean isInsertar() {
		return insertar;
	}

	public void setInsertar(boolean insertar) {
		this.insertar = insertar;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	

}
