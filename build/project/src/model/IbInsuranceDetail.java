package model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the ib_insurance_detail database table.
 * 
 */
@Entity
@Table(name="ib_insurance_detail")
@NamedQueries({

	@NamedQuery(name="IbInsuranceDetail.findAll", query="SELECT i FROM IbInsuranceDetail i"),
	@NamedQuery(name = "IbInsuranceDetail.findBySeguro", query = "SELECT i FROM IbInsuranceDetail i WHERE i.idSeguro= :idseguro") }
)

public class IbInsuranceDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idib_insurance_detail")
	private int idibInsuranceDetail;

	private String accesorios;

	private String cc;

	private String cobertura;

	private String cv;

	@Column(name="fecha_primera_matricula")
	private Timestamp fechaPrimeraMatricula;

	@Column(name="id_seguro")
	private int idSeguro;

	private String marca;

	private String matricula;

	private String modelo;

	@Column(name="particular_publico")
	private byte particularPublico;

	@Column(name="pma_kgs")
	private int pmaKgs;

	private String remolque;

	@Column(name="tipo_vehiculo")
	private String tipoVehiculo;

	//bi-directional one-to-one association to IbInsurance
	@OneToOne(mappedBy="ibInsuranceDetail")
	private IbInsurance ibInsurance;

	public IbInsuranceDetail() {
	}

	public int getIdibInsuranceDetail() {
		return this.idibInsuranceDetail;
	}

	public void setIdibInsuranceDetail(int idibInsuranceDetail) {
		this.idibInsuranceDetail = idibInsuranceDetail;
	}

	public String getAccesorios() {
		return this.accesorios;
	}

	public void setAccesorios(String accesorios) {
		this.accesorios = accesorios;
	}

	public String getCc() {
		return this.cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getCobertura() {
		return this.cobertura;
	}

	public void setCobertura(String cobertura) {
		this.cobertura = cobertura;
	}

	public String getCv() {
		return this.cv;
	}

	public void setCv(String cv) {
		this.cv = cv;
	}

	public Timestamp getFechaPrimeraMatricula() {
		return this.fechaPrimeraMatricula;
	}

	public void setFechaPrimeraMatricula(Timestamp fechaPrimeraMatricula) {
		this.fechaPrimeraMatricula = fechaPrimeraMatricula;
	}

	public int getIdSeguro() {
		return this.idSeguro;
	}

	public void setIdSeguro(int idSeguro) {
		this.idSeguro = idSeguro;
	}

	public String getMarca() {
		return this.marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getMatricula() {
		return this.matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getModelo() {
		return this.modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public byte getParticularPublico() {
		return this.particularPublico;
	}

	public void setParticularPublico(byte particularPublico) {
		this.particularPublico = particularPublico;
	}

	public int getPmaKgs() {
		return this.pmaKgs;
	}

	public void setPmaKgs(int pmaKgs) {
		this.pmaKgs = pmaKgs;
	}

	public String getRemolque() {
		return this.remolque;
	}

	public void setRemolque(String remolque) {
		this.remolque = remolque;
	}

	public String getTipoVehiculo() {
		return this.tipoVehiculo;
	}

	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public IbInsurance getIbInsurance() {
		return this.ibInsurance;
	}

	public void setIbInsurance(IbInsurance ibInsurance) {
		this.ibInsurance = ibInsurance;
	}

}