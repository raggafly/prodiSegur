package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ib_account_bank database table.
 * 
 */
@Entity
@Table(name="ib_account_bank")
@NamedQueries({

	@NamedQuery(name="IbAccountBank.findAll", query="SELECT i FROM IbAccountBank i"),
	@NamedQuery(name="IbAccountBank.findByPK", query = "SELECT m FROM IbAccountBank m WHERE m.idibAccountBank = :id"),
	@NamedQuery(name="IbAccountBank.findByCustomer", query = "SELECT m FROM IbAccountBank m WHERE m.ibCustomer = :customer")
})
public class IbAccountBank implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idib_account_bank")
	private int idibAccountBank;

	private String banco;

	private String dc;

	private String entidad;

	@Column(name="numero_cuenta")
	private String numeroCuenta;

	private String oficina;

	//bi-directional one-to-one association to IbCustomer
	@OneToOne
	@JoinColumn(name="idib_account_bank",insertable =  false, updatable = false)
	private IbCustomer ibCustomer;

	//bi-directional many-to-one association to IbCustomer
	@OneToMany(mappedBy="ibAccountBank2")
	private List<IbCustomer> ibCustomers;

	public IbAccountBank() {
	}

	public int getIdibAccountBank() {
		return this.idibAccountBank;
	}

	public void setIdibAccountBank(int idibAccountBank) {
		this.idibAccountBank = idibAccountBank;
	}

	public String getBanco() {
		return this.banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getDc() {
		return this.dc;
	}

	public void setDc(String dc) {
		this.dc = dc;
	}

	public String getEntidad() {
		return this.entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getNumeroCuenta() {
		return this.numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getOficina() {
		return this.oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	public IbCustomer getIbCustomer() {
		return this.ibCustomer;
	}

	public void setIbCustomer(IbCustomer ibCustomer) {
		this.ibCustomer = ibCustomer;
	}

	public List<IbCustomer> getIbCustomers() {
		return this.ibCustomers;
	}

	public void setIbCustomers(List<IbCustomer> ibCustomers) {
		this.ibCustomers = ibCustomers;
	}

	public IbCustomer addIbCustomer(IbCustomer ibCustomer) {
		getIbCustomers().add(ibCustomer);
		ibCustomer.setIbAccountBank2(this);

		return ibCustomer;
	}

	public IbCustomer removeIbCustomer(IbCustomer ibCustomer) {
		getIbCustomers().remove(ibCustomer);
		ibCustomer.setIbAccountBank2(null);

		return ibCustomer;
	}

}