package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ib_customer_type database table.
 * 
 */
@Entity
@Table(name="ib_customer_type")
@NamedQueries({

	@NamedQuery(name = "IbCustomerType.findByType", query = "SELECT m.descripcion FROM IbCustomerType m"),
	@NamedQuery(name="IbCustomerType.findAll", query="SELECT i FROM IbCustomerType i")
	
})
@NamedQuery(name="IbCustomerType.findAll", query="SELECT i FROM IbCustomerType i")
public class IbCustomerType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name="idib_customer_type")
	private int idibCustomerType;

	@Column(name="cod_tipo")
	private String codTipo;

	private String descripcion;

	//bi-directional many-to-one association to IbCustomerRelation
	@OneToMany(mappedBy="ibCustomerType1")
	private List<IbCustomerRelation> ibCustomerRelations;

	//bi-directional many-to-many association to IbCustomer
	@ManyToMany
	@JoinTable(
		name="ib_customer_relation"
		, joinColumns={
			@JoinColumn(name="id_tipo")
			}
		, inverseJoinColumns={
			@JoinColumn(name="id_cliente")
			}
		)
	private List<IbCustomer> ibCustomers;

	//bi-directional one-to-one association to IbCustomerRelation
	@OneToOne
	@JoinColumn(name="idib_customer_type",insertable =  false, updatable = false)
	private IbCustomerRelation ibCustomerRelation;

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
		ibCustomerRelation.setIbCustomerType1(this);

		return ibCustomerRelation;
	}

	public IbCustomerRelation removeIbCustomerRelation(IbCustomerRelation ibCustomerRelation) {
		getIbCustomerRelations().remove(ibCustomerRelation);
		ibCustomerRelation.setIbCustomerType1(null);

		return ibCustomerRelation;
	}

	public List<IbCustomer> getIbCustomers() {
		return this.ibCustomers;
	}

	public void setIbCustomers(List<IbCustomer> ibCustomers) {
		this.ibCustomers = ibCustomers;
	}

	public IbCustomerRelation getIbCustomerRelation() {
		return this.ibCustomerRelation;
	}

	public void setIbCustomerRelation(IbCustomerRelation ibCustomerRelation) {
		this.ibCustomerRelation = ibCustomerRelation;
	}

}