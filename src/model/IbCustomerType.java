package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the ib_customer_type database table.
 * 
 */
@Entity
@Table(name="ib_customer_type")
@NamedQueries({

	@NamedQuery(name = "IbCustomerType.findByType", query = "SELECT m.descripcion FROM IbCustomerType m"),
	@NamedQuery(name = "IbCustomerType.findByDescription", query = "SELECT m FROM IbCustomerType m where m.descripcion =:desc"),
	@NamedQuery(name = "IbCustomerType.findAll", query="SELECT i FROM IbCustomerType i")
	
})
public class IbCustomerType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idib_customer_type")
	private int idibCustomerType;

	@Column(name="cod_tipo")
	private String codTipo;

	private String descripcion;

	//bi-directional many-to-one association to IbCustomerRelation
	@OneToMany(mappedBy="ibCustomerType")
	private List<IbCustomerRelation> ibCustomerRelations;

	public IbCustomerType() {
	}

	public int getIdibCustomerType() {
		return this.idibCustomerType;
	}

	public void setIdibCustomerType(int idibCustomerType) {
		this.idibCustomerType = idibCustomerType;
	}

	public String getCodTipo() {
		return this.codTipo;
	}

	public void setCodTipo(String codTipo) {
		this.codTipo = codTipo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<IbCustomerRelation> getIbCustomerRelations() {
		return this.ibCustomerRelations;
	}

	public void setIbCustomerRelations(List<IbCustomerRelation> ibCustomerRelations) {
		this.ibCustomerRelations = ibCustomerRelations;
	}

	public IbCustomerRelation addIbCustomerRelation(IbCustomerRelation ibCustomerRelation) {
		getIbCustomerRelations().add(ibCustomerRelation);
		ibCustomerRelation.setIbCustomerType(this);

		return ibCustomerRelation;
	}

	public IbCustomerRelation removeIbCustomerRelation(IbCustomerRelation ibCustomerRelation) {
		getIbCustomerRelations().remove(ibCustomerRelation);
		ibCustomerRelation.setIbCustomerType(null);

		return ibCustomerRelation;
	}

}