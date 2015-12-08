package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ib_customer database table.
 * 
 */
@Entity
@Table(name="ib_customer")
@NamedQueries({

	@NamedQuery(name="IbCustomer.findAll", query="SELECT i FROM IbCustomer i"),
	@NamedQuery(name="IbCustomer.findByPK", query = "SELECT m FROM IbCustomer m WHERE m.idibCustomer = :id"),
	@NamedQuery(name="IbCustomer.findDNI", query = "SELECT m FROM IbCustomer m WHERE m.dniCif = :dni")
})
public class IbCustomer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name="idib_customer")
	private int idibCustomer;

	private String apellidos;

	@Column(name="cod_postal")
	private String codPostal;

	private String direccion;

	@Column(name="dni_cif")
	private String dniCif;

	private String email;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_carnet")
	private Date fechaCarnet;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_nacimiento")
	private Date fechaNacimiento;

	private String nombre;

	private String poblacion;

	private String provincia;

	private String telefono;

	private String telefono2;

	//bi-directional one-to-one association to IbAccountBank
	@OneToOne(mappedBy="ibCustomer")
	private IbAccountBank ibAccountBank1;

	//bi-directional many-to-one association to IbAccountBank
	@ManyToOne
	@JoinColumn(name="ib_cuenta")
	private IbAccountBank ibAccountBank2;

	//bi-directional one-to-one association to IbInsurance
	@OneToOne
	@JoinColumn(name="idib_customer",insertable =  false, updatable = false)
	private IbInsurance ibInsurance;

	//bi-directional many-to-one association to IbCustomerRelation
	@OneToMany(mappedBy="ibCustomer")
	private List<IbCustomerRelation> ibCustomerRelations;

	//bi-directional many-to-many association to IbCustomerType
	@ManyToMany(mappedBy="ibCustomers")
	private List<IbCustomerType> ibCustomerTypes;

	public IbCustomer() {
	}

	public int getIdibCustomer() {
		return this.idibCustomer;
	}

	public void setIdibCustomer(int idibCustomer) {
		this.idibCustomer = idibCustomer;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCodPostal() {
		return this.codPostal;
	}

	public void setCodPostal(String codPostal) {
		this.codPostal = codPostal;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDniCif() {
		return this.dniCif;
	}

	public void setDniCif(String dniCif) {
		this.dniCif = dniCif;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getFechaCarnet() {
		return this.fechaCarnet;
	}

	public void setFechaCarnet(Date fechaCarnet) {
		this.fechaCarnet = fechaCarnet;
	}

	public Date getFechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPoblacion() {
		return this.poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTelefono2() {
		return this.telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public IbAccountBank getIbAccountBank1() {
		return this.ibAccountBank1;
	}

	public void setIbAccountBank1(IbAccountBank ibAccountBank1) {
		this.ibAccountBank1 = ibAccountBank1;
	}

	public IbAccountBank getIbAccountBank2() {
		return this.ibAccountBank2;
	}

	public void setIbAccountBank2(IbAccountBank ibAccountBank2) {
		this.ibAccountBank2 = ibAccountBank2;
	}

	public IbInsurance getIbInsurance() {
		return this.ibInsurance;
	}

	public void setIbInsurance(IbInsurance ibInsurance) {
		this.ibInsurance = ibInsurance;
	}

	public List<IbCustomerRelation> getIbCustomerRelations() {
		return this.ibCustomerRelations;
	}

	public void setIbCustomerRelations(List<IbCustomerRelation> ibCustomerRelations) {
		this.ibCustomerRelations = ibCustomerRelations;
	}

	public IbCustomerRelation addIbCustomerRelation(IbCustomerRelation ibCustomerRelation) {
		getIbCustomerRelations().add(ibCustomerRelation);
		ibCustomerRelation.setIbCustomer(this);

		return ibCustomerRelation;
	}

	public IbCustomerRelation removeIbCustomerRelation(IbCustomerRelation ibCustomerRelation) {
		getIbCustomerRelations().remove(ibCustomerRelation);
		ibCustomerRelation.setIbCustomer(null);

		return ibCustomerRelation;
	}

	public List<IbCustomerType> getIbCustomerTypes() {
		return this.ibCustomerTypes;
	}

	public void setIbCustomerTypes(List<IbCustomerType> ibCustomerTypes) {
		this.ibCustomerTypes = ibCustomerTypes;
	}

}