package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;



/**
 * The persistent class for the ib_cuotes_insure database table.
 * 
 */
@Entity
@Table(name="ib_cuotes_insure")
@NamedQuery(name="IbCuotesInsure.findAll", query="SELECT i FROM IbCuotesInsure i")
public class IbCuotesInsure implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IB_CUOTES_INSURE_IDIBCUOTESINSURE_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.AUTO, generator="IB_CUOTES_INSURE_IDIBCUOTESINSURE_GENERATOR")
	@Column(name="idib_cuotes_insure")
	private int idibCuotesInsure;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_oficial_pago")
	private Date fechaOficialPago;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_pago_cuota")
	private Date fechaPagoCuota;
	
	@Column(name="num_orden")
	private int numOrden;

	private byte pagado;

	@Column(name="total_cuota")
	private double totalCuota;

	//bi-directional many-to-one association to IbInsurance
	@ManyToOne
	@JoinColumn(name="id_seguro")
	private IbInsurance ibInsurance;

	public IbCuotesInsure() {
	}

	public int getIdibCuotesInsure() {
		return this.idibCuotesInsure;
	}

	public void setIdibCuotesInsure(int idibCuotesInsure) {
		this.idibCuotesInsure = idibCuotesInsure;
	}

	public int getNumOrden() {
		return this.numOrden;
	}

	public void setNumOrden(int numOrden) {
		this.numOrden = numOrden;
	}

	public byte getPagado() {
		return this.pagado;
	}

	public void setPagado(byte pagado) {
		this.pagado = pagado;
	}

	public double getTotalCuota() {
		return this.totalCuota;
	}

	public void setTotalCuota(double totalCuota) {
		this.totalCuota = totalCuota;
	}

	public IbInsurance getIbInsurance() {
		return this.ibInsurance;
	}

	public void setIbInsurance(IbInsurance ibInsurance) {
		this.ibInsurance = ibInsurance;
	}
	
	public Date getFechaOficialPago() {
		return this.fechaOficialPago;
	}

	public void setFechaOficialPago(Date fechaOficialPago) {
		this.fechaOficialPago = fechaOficialPago;
	}

	public Date getFechaPagoCuota() {
		return this.fechaPagoCuota;
	}

	public void setFechaPagoCuota(Date fechaPagoCuota) {
		this.fechaPagoCuota = fechaPagoCuota;
	}

}