package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ib_master_values database table.
 * 
 */
@Entity
@Table(name="ib_master_values")
@NamedQueries({

	@NamedQuery(name="IbMasterValue.findByType", query = "SELECT m.descripcion FROM IbMasterValue m WHERE m.tipoCodigo = :type"),
	@NamedQuery(name="IbMasterValue.findAllByType", query = "SELECT m FROM IbMasterValue m WHERE m.tipoCodigo = :type"),
	@NamedQuery(name="IbMasterValue.findAllByValor", query = "SELECT m FROM IbMasterValue m WHERE m.descripcion = :desc"),
	@NamedQuery(name="IbMasterValue.findByValue", query = "SELECT m.descripcion FROM IbMasterValue m WHERE m.valor = :valor"),
	@NamedQuery(name="IbMasterValue.findByDescription", query="SELECT m.descripcion2 FROM IbMasterValue m WHERE m.descripcion= :description"),
	@NamedQuery(name="IbMasterValue.findAll", query="SELECT i FROM IbMasterValue i"),
	@NamedQuery(name="IbMasterValue.findAllTypes", query="SELECT distinct i.tipoCodigo FROM IbMasterValue i"),
	@NamedQuery(name="IbMasterValue.findMaxValorByType", query="select max(m.valor) from IbMasterValue m where m.tipoCodigo = :type")
	
})
public class IbMasterValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idib_master_values")
	private int idibMasterValues;

	private String descripcion;

	private String descripcion2;

	@Column(name="tipo_codigo")
	private String tipoCodigo;

	private String valor;

	public IbMasterValue() {
	}

	public int getIdibMasterValues() {
		return this.idibMasterValues;
	}

	public void setIdibMasterValues(int idibMasterValues) {
		this.idibMasterValues = idibMasterValues;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion2() {
		return this.descripcion2;
	}

	public void setDescripcion2(String descripcion2) {
		this.descripcion2 = descripcion2;
	}

	public String getTipoCodigo() {
		return this.tipoCodigo;
	}

	public void setTipoCodigo(String tipoCodigo) {
		this.tipoCodigo = tipoCodigo;
	}

	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}