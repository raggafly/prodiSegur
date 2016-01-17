package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ib_cuotes_time database table.
 * 
 */
@Entity
@Table(name="ib_cuotes_time")
@NamedQueries({

@NamedQuery(name="IbCuotesTime.findAll", query="SELECT i FROM IbCuotesTime i"),
@NamedQuery(name="IbCuotesTime.findAllByDurAndFP", query="select c FROM IbCuotesTime c where c.typeDuracion= :duracion and c.typeFormaPago = :formaPago")

})
public class IbCuotesTime implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idib_cuotes_time")
	private int idibCuotesTime;

	private String descripcion;

	@Column(name="intervalo_meses")
	private int intervaloMeses;

	@Column(name="numero_cuotas")
	private int numeroCuotas;

	@Column(name="type_duracion")
	private String typeDuracion;

	@Column(name="type_forma_pago")
	private String typeFormaPago;

	public IbCuotesTime() {
	}

	public int getIdibCuotesTime() {
		return this.idibCuotesTime;
	}

	public void setIdibCuotesTime(int idibCuotesTime) {
		this.idibCuotesTime = idibCuotesTime;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getIntervaloMeses() {
		return this.intervaloMeses;
	}

	public void setIntervaloMeses(int intervaloMeses) {
		this.intervaloMeses = intervaloMeses;
	}

	public int getNumeroCuotas() {
		return this.numeroCuotas;
	}

	public void setNumeroCuotas(int numeroCuotas) {
		this.numeroCuotas = numeroCuotas;
	}

	public String getTypeDuracion() {
		return this.typeDuracion;
	}

	public void setTypeDuracion(String typeDuracion) {
		this.typeDuracion = typeDuracion;
	}

	public String getTypeFormaPago() {
		return this.typeFormaPago;
	}

	public void setTypeFormaPago(String typeFormaPago) {
		this.typeFormaPago = typeFormaPago;
	}

}