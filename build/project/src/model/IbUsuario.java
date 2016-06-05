package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ib_usuario database table.
 * 
 */
@Entity
@Table(name="ib_usuario")
@NamedQueries({

	@NamedQuery(name="IbUsuario.findByLogin", query = "SELECT i FROM IbUsuario i WHERE  i.login= :usuario and i.password= :password"),
	@NamedQuery(name="IbUsuario.findByLoginName", query = "SELECT i FROM IbUsuario i WHERE  i.login= :usuario"),
	@NamedQuery(name="IbUsuario.findAll", query="SELECT i FROM IbUsuario i")
	
})
public class IbUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idib_usuario")
	private int idibUsuario;

	private String login;

	private String nombre;

	private String password;

	public IbUsuario() {
	}

	public int getIdibUsuario() {
		return this.idibUsuario;
	}

	public void setIdibUsuario(int idibUsuario) {
		this.idibUsuario = idibUsuario;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}