package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ib_customer_relation database table.
 * 
 */
@Entity
@Table(name="ib_customer_relation")
@NamedQuery(name="IbCustomerRelation.findAll", query="SELECT i FROM IbCustomerRelation i")
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
	private IbCustomerType ibCustomerType1;

	//bi-directional many-to-one association to IbInsurance
	@ManyToOne
	@JoinColumn(name="id_seguro")
	private IbInsurance ibInsurance;

	//bi-directional one-to-one association to IbCustomerType
	@OneToOne(mappedBy="ibCustomerRelation")
	private IbCustomerType ibCustomerType2;

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

	public IbCustomerType getIbCustomerType1() {
		return this.ibCustomerType1;
	}

	public void setIbCustomerType1(IbCustomerType ibCustomerType1) {
		this.ibCustomerType1 = ibCustomerType1;
	}

	public IbInsurance getIbInsurance() {
		return this.ibInsurance;
	}

	public void setIbInsurance(IbInsurance ibInsurance) {
		this.ibInsurance = ibInsurance;
	}

	public IbCustomerType getIbCustomerType2() {
		return this.ibCustomerType2;
	}

	public void setIbCustomerType2(IbCustomerType ibCustomerType2) {
		this.ibCustomerType2 = ibCustomerType2;
	}

}