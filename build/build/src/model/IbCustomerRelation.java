package model;

import java.io.Serializable;
import javax.persistence.*;

import model.IbCustomerType;


/**
 * The persistent class for the ib_customer_relation database table.
 * 
 */
@Entity
@Table(name="ib_customer_relation")
@NamedQueries({

	@NamedQuery(name="IbCustomerRelation.findAll", query="SELECT i FROM IbCustomerRelation i"),
	@NamedQuery(name="IbCustomerRelation.findByPK", query = "SELECT m FROM IbCustomerRelation m WHERE m.idibCustomerRelation = :id"),
	@NamedQuery(name="IbCustomerRelation.findCliente", query = "SELECT m FROM IbCustomerRelation m WHERE m.ibCustomer = :idcliente"),
	@NamedQuery(name="IbCustomerRelation.findSeguro", query = "SELECT m FROM IbCustomerRelation m WHERE m.ibInsurance = :idseguro")
})

public class IbCustomerRelation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idib_customer_relation")
	private int idibCustomerRelation;

	//bi-directional many-to-one association to IbCustomer
	@ManyToOne
	@JoinColumn(name="id_cliente")
	private IbCustomer ibCustomer;

	//bi-directional many-to-one association to IbCustomerType
	@ManyToOne
	@JoinColumn(name="id_tipo")
	private IbCustomerType ibCustomerType;

	//bi-directional many-to-one association to IbInsurance
	@ManyToOne
	@JoinColumn(name="id_seguro")
	private IbInsurance ibInsurance;

	public IbCustomerRelation() {
	}

	public int getIdibCustomerRelation() {
		return this.idibCustomerRelation;
	}

	public void setIdibCustomerRelation(int idibCustomerRelation) {
		this.idibCustomerRelation = idibCustomerRelation;
	}

	public IbCustomer getIbCustomer() {
		return this.ibCustomer;
	}

	public void setIbCustomer(IbCustomer ibCustomer) {
		this.ibCustomer = ibCustomer;
	}

	public IbInsurance getIbInsurance() {
		return this.ibInsurance;
	}

	public void setIbInsurance(IbInsurance ibInsurance) {
		this.ibInsurance = ibInsurance;
	}

	public IbCustomerType getIbCustomerType() {
		return this.ibCustomerType;
	}

	public void setIbCustomerType(IbCustomerType ibCustomerType) {
		this.ibCustomerType = ibCustomerType;
	}
}