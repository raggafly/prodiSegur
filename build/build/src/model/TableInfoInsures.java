package model;

import java.util.Date;

public class TableInfoInsures {
	String orden;
	String numeroPoliza;
	String dni;
	String tipo;
	String compania;
	Double primaNeta;
	Date fechaVigor;
	Date fechaFinVigor;
	Date fechaEfecto;
	String estado;
	String nombre;
	
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNumeroPoliza() {
		return numeroPoliza;
	}
	public void setNumeroPoliza(String numeroPoliza) {
		this.numeroPoliza = numeroPoliza;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getCompania() {
		return compania;
	}
	public void setCompania(String compania) {
		this.compania = compania;
	}
	public Double getPrimaNeta() {
		return primaNeta;
	}
	public void setPrimaNeta(Double primaNeta) {
		this.primaNeta = primaNeta;
	}
	public Date getFechaVigor() {
		return fechaVigor;
	}
	public void setFechaVigor(Date fechaVigor) {
		this.fechaVigor = fechaVigor;
	}
	public Date getFechaFinVigor() {
		return fechaFinVigor;
	}
	public void setFechaFinVigor(Date fechaFinVigor) {
		this.fechaFinVigor = fechaFinVigor;
	}
	public Date getFechaEfecto() {
		return fechaEfecto;
	}
	public void setFechaEfecto(Date fechaEfecto) {
		this.fechaEfecto = fechaEfecto;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
}
