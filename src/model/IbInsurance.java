package model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the ib_insurance database table.
 * 
 */
@Entity
@Table(name = "ib_insurance")
@NamedQueries({

		@NamedQuery(name = "IbInsurance.findAll", query = "SELECT i FROM IbInsurance i"),
		@NamedQuery(name = "IbInsurance.findByPoliza", query = "SELECT m FROM IbInsurance m WHERE m.numeroPoliza = :poliza") })

public class IbInsurance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idib_insurance")
	private int idibInsurance;

	private double comision;

	private String compania;

	private String estado;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_efecto")
	private Date fechaEfecto;

	private String duracion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_entrada_vigor")
	private Date fechaEntradaVigor;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_fin_entrada_vigor")
	private Date fechaFinEntradaVigor;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_fin")
	private Date fechaFin;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_inicio")
	private Date fechaInicio;

	@Column(name = "forma_pago")
	private String formaPago;

	private double liquidez;

	@Column(name = "numero_poliza")
	private String numeroPoliza;

	@Column(name = "prima_neta")
	private double primaNeta;

	@Column(name = "tipo_riesgo")
	private String tipoRiesgo;

	// bi-directional one-to-one association to IbCustomer
	@OneToOne(mappedBy = "ibInsurance")
	private IbCustomer ibCustomer;

	// bi-directional many-to-one association to IbCustomerRelation
	@OneToMany(mappedBy = "ibInsurance")
	private List<IbCustomerRelation> ibCustomerRelations;

	// bi-directional one-to-one association to IbInsuranceDetail
	@OneToOne
	@JoinColumn(name = "idib_insurance")
	private IbInsuranceDetail ibInsuranceDetail;

	// bi-directional many-to-one association to IbCuotesInsure
	@OneToMany(mappedBy = "ibInsurance", fetch = FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	private List<IbCuotesInsure> ibCuotesInsures;

	public IbInsurance() {
	}

	public int getIdibInsurance() {
		return this.idibInsurance;
	}

	public void setIdibInsurance(int idibInsurance) {
		this.idibInsurance = idibInsurance;
	}

	public double getComision() {
		return this.comision;
	}

	public void setComision(double comision) {
		this.comision = comision;
	}

	public String getCompania() {
		return this.compania;
	}

	public void setCompania(String compania) {
		this.compania = compania;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Date getFechaEfecto() {
		return this.fechaEfecto;
	}

	public void setFechaEfecto(Date fechaEfecto) {
		this.fechaEfecto = fechaEfecto;
	}

	public Date getFechaEntradaVigor() {
		return this.fechaEntradaVigor;
	}

	public void setFechaEntradaVigor(Date fechaEntradaVigor) {
		this.fechaEntradaVigor = fechaEntradaVigor;
	}

	public Date getFechaFinEntradaVigor() {
		return this.fechaFinEntradaVigor;
	}

	public void setFechaFinEntradaVigor(Date fechaFinEntradaVigor) {
		this.fechaFinEntradaVigor = fechaFinEntradaVigor;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFormaPago() {
		return this.formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public double getLiquidez() {
		return this.liquidez;
	}

	public void setLiquidez(double liquidez) {
		this.liquidez = liquidez;
	}

	public String getNumeroPoliza() {
		return this.numeroPoliza;
	}

	public void setNumeroPoliza(String numeroPoliza) {
		this.numeroPoliza = numeroPoliza;
	}

	public double getPrimaNeta() {
		return this.primaNeta;
	}

	public void setPrimaNeta(double primaNeta) {
		this.primaNeta = primaNeta;
	}

	public String getTipoRiesgo() {
		return this.tipoRiesgo;
	}

	public void setTipoRiesgo(String tipoRiesgo) {
		this.tipoRiesgo = tipoRiesgo;
	}

	public IbCustomer getIbCustomer() {
		return this.ibCustomer;
	}

	public void setIbCustomer(IbCustomer ibCustomer) {
		this.ibCustomer = ibCustomer;
	}

	public List<IbCustomerRelation> getIbCustomerRelations() {
		return this.ibCustomerRelations;
	}

	public void setIbCustomerRelations(List<IbCustomerRelation> ibCustomerRelations) {
		this.ibCustomerRelations = ibCustomerRelations;
	}

	public IbCustomerRelation addIbCustomerRelation(IbCustomerRelation ibCustomerRelation) {
		getIbCustomerRelations().add(ibCustomerRelation);
		ibCustomerRelation.setIbInsurance(this);

		return ibCustomerRelation;
	}

	public IbCustomerRelation removeIbCustomerRelation(IbCustomerRelation ibCustomerRelation) {
		getIbCustomerRelations().remove(ibCustomerRelation);
		ibCustomerRelation.setIbInsurance(null);

		return ibCustomerRelation;
	}

	public IbInsuranceDetail getIbInsuranceDetail() {
		return this.ibInsuranceDetail;
	}

	public void setIbInsuranceDetail(IbInsuranceDetail ibInsuranceDetail) {
		this.ibInsuranceDetail = ibInsuranceDetail;
	}

	public String getDuracion() {
		return this.duracion;
	}

	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	public List<IbCuotesInsure> getIbCuotesInsures() {
		return this.ibCuotesInsures;
	}

	public void setIbCuotesInsures(List<IbCuotesInsure> ibCuotesInsures) {
		this.ibCuotesInsures = ibCuotesInsures;
	}

	public IbCuotesInsure addIbCuotesInsure(IbCuotesInsure ibCuotesInsure) {
		getIbCuotesInsures().add(ibCuotesInsure);
		ibCuotesInsure.setIbInsurance(this);

		return ibCuotesInsure;
	}

	public IbCuotesInsure removeIbCuotesInsure(IbCuotesInsure ibCuotesInsure) {
		getIbCuotesInsures().remove(ibCuotesInsure);
		ibCuotesInsure.setIbInsurance(null);

		return ibCuotesInsure;
	}
}