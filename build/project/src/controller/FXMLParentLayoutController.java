package controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.print.DocFlavor.URL;

import com.sun.javafx.tk.quantum.MasterTimer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.IbCustomer;
import model.IbInsurance;
import model.IbInsuranceDetail;
import model.MasterTypes;
import model.TableInfo;
import util.JDBCConnection;
import util.MasterValueUtil;

public class FXMLParentLayoutController implements Initializable {
	@FXML
	private Label lbEnumTittularSeguro;

	public Label getlbEnumTittularSeguro() {
		return lbEnumTittularSeguro;
	}

	@FXML
	private Label lbEnumPropietario;

	public Label getlbEnumPropietario() {
		return lbEnumPropietario;
	}

	@FXML
	private Label lbEnumConductor;

	public Label getlbEnumConductor() {
		return lbEnumConductor;
	}

	@FXML
	private Label lbTitularSeguro;

	public Label getlbTitularSeguro() {
		return lbTitularSeguro;
	}

	@FXML
	private Label lbPropietario;

	public Label getlbPropietario() {
		return lbPropietario;
	}

	@FXML
	private Label lbConductor;

	public Label getlbConductor() {
		return lbConductor;
	}

	@FXML
	private Label lbNumeroPoliza;

	public Label getlbNumeroPoliza() {
		return lbNumeroPoliza;
	}

	@FXML
	private Label lbCompania;

	public Label getlbCompania() {
		return lbCompania;
	}

	@FXML
	private Label lbFechaInicio;

	public Label getlbFechaInicio() {
		return lbFechaInicio;
	}

	@FXML
	private Label lbDuracion;

	public Label getlbDuracion() {
		return lbDuracion;
	}

	@FXML
	private Label lbFechaEntradaVigor;

	public Label getlbFechaEntradaVigor() {
		return lbFechaEntradaVigor;
	}

	@FXML
	private Label lbFechaFin;

	public Label getlbFechaFin() {
		return lbFechaFin;
	}

	@FXML
	private Label lbPrimaNeta;

	public Label getlbPrimaNeta() {
		return lbPrimaNeta;
	}

	@FXML
	private Label lbEstado;

	public Label getlbEstado() {
		return lbEstado;
	}

	@FXML
	private Label lbLiquidez;

	public Label getlbLiquidez() {
		return lbLiquidez;
	}

	@FXML
	private Label lbComision;

	public Label getlbComision() {
		return lbComision;
	}

	@FXML
	private Label lbTipoRiesgo;

	public Label getlbTipoRiesgo() {
		return lbTipoRiesgo;
	}

	@FXML
	private Label lbFormaPago;

	public Label getlbFormaPago() {
		return lbFormaPago;
	}

	@FXML
	private Label lbTipoVehiculo;

	public Label getlbTipoVehiculo() {
		return lbTipoVehiculo;
	}

	@FXML
	private Label lbMarca;

	public Label getlbMarca() {
		return lbMarca;
	}

	@FXML
	private Label lbModelo;

	public Label getlbModelo() {
		return lbModelo;
	}

	@FXML
	private Label lbCC;

	public Label getlbCC() {
		return lbCC;
	}

	@FXML
	private Label lbParticularPublico;

	public Label getlbParticularPublico() {
		return lbParticularPublico;
	}

	@FXML
	private Label lbMatricula;

	public Label getlbMatricula() {
		return lbMatricula;
	}

	@FXML
	private Label lbCV;

	public Label getlbCV() {
		return lbCV;
	}

	@FXML
	private Label lbFechaPrimeraMatricula;

	public Label getlbFechaPrimeraMatricula() {
		return lbFechaPrimeraMatricula;
	}

	@FXML
	private Label lbPMA;

	public Label getlbPMA() {
		return lbPMA;
	}

	@FXML
	private Label lbRemolque;

	public Label getlbRemolque() {
		return lbRemolque;
	}

	@FXML
	private Label lbCobertura;

	public Label getlbCobertura() {
		return lbCobertura;
	}

	@FXML
	private Label lbAccesorios;

	public Label getlbAccesorios() {
		return lbAccesorios;
	}
	@FXML
	private Tab tabDetalleSeguro;
	public Tab gettabDetalleSeguro() {
		return tabDetalleSeguro;
	}
	@FXML
	private Label lbBanco;
	@FXML
	private Label lbNumeroCuenta;
	
	public String particular = "PARTICULAR";
	public String publico = "PÚBLICO";

	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {

	}

	public void initData(String poliza) throws SQLException {
		Statement stmt = null;
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		IbInsurance seguro = MasterValueUtil.getInsurance(poliza);
		List<IbInsuranceDetail> lid = null;
		TypedQuery<IbInsuranceDetail> query2 = em.createNamedQuery("IbInsuranceDetail.findBySeguro",
				IbInsuranceDetail.class);
		query2.setParameter("idseguro",seguro.getIdibInsurance());
		lid = query2.getResultList();
		if (null != lid && lid.size() > 0) {
			
			tabDetalleSeguro.setDisable(false);
		}
		em.close();
		try {
			JDBCConnection con = new JDBCConnection();
			
			String query = "select CONCAT(c.nombre, ' ', c.Apellidos) As Nombre, t.descripcion as tipoUsuario , t.cod_tipo as codigo,  i.numero_poliza,compania,fecha_inicio,fecha_fin, (select descripcion from ib_master_values mv where duracion = mv.valor) as duracion ,(select descripcion from ib_master_values mv where ab.banco = mv.valor) as banco , CONCAT(ab.entidad, '    ', ab.oficina,'    ', ab.dc,'    ', ab.numero_cuenta) As cuenta_bancaria,prima_neta,fecha_entrada_vigor, (select descripcion from ib_master_values mv where estado = mv.valor) as estado,liquidez,comision, (select descripcion from ib_master_values mv where tipo_riesgo = mv.valor) as tipo_riesgo,   (select descripcion from ib_master_values mv where tipo_vehiculo = mv.valor) as tipo_vehiculo,  (select descripcion from ib_master_values mv where forma_pago = mv.valor) as forma_pago,   (select descripcion from ib_master_values mv where cobertura = mv.valor) as cobertura, marca,modelo,matricula,cc,cv,particular_publico,fecha_primera_matricula, pma_kgs,remolque,accesorios   FROM ib_insurance i LEFT OUTER JOIN ib_insurance_detail d ON d.id_seguro  = i.idib_insurance, ib_customer_relation r, ib_customer_type t ,ib_customer c,ib_account_bank AB where i.numero_poliza='" + poliza + "' and  c.idib_customer = r.id_cliente and i.idib_insurance = r.id_seguro and t.idib_customer_type = r.id_tipo AND AB.iDIb_account_bank = C.ib_cuenta order by t.cod_tipo";
			
			stmt = con.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int cont = 0;
			while (rs.next()) {
				if (MasterTypes.TOMADOR.equals(rs.getString("codigo"))) {
					lbEnumTittularSeguro.setText(rs.getString("tipoUsuario")+":");
					lbTitularSeguro.setText(rs.getString("nombre"));
				}
				if (MasterTypes.PROPIETARIO.equals(rs.getString("codigo"))) {
					lbEnumPropietario.setText(rs.getString("tipoUsuario")+":");
					lbPropietario.setText(rs.getString("nombre"));
				}
				if (MasterTypes.CONDUCTOR.equals(rs.getString("codigo"))) {
					lbEnumConductor.setText(rs.getString("tipoUsuario")+":");
					lbConductor.setText(rs.getString("nombre"));
				}
				if (cont == 0) {
					lbNumeroPoliza.setText(rs.getString("numero_poliza"));
					lbCompania.setText(rs.getString("compania"));
					lbMarca.setText(rs.getString("marca"));
					lbModelo.setText(rs.getString("modelo"));
					lbCC.setText(rs.getString("cc"));
					lbMatricula.setText(rs.getString("matricula"));
					lbFechaInicio.setText(String.valueOf(rs.getDate("fecha_inicio")));
					lbFechaFin.setText(String.valueOf(rs.getDate("fecha_fin")));
					lbFechaEntradaVigor.setText(String.valueOf(rs.getDate("fecha_entrada_vigor")));
					lbDuracion.setText(rs.getString("duracion"));
					if (rs.getInt("particular_publico") == 0) {
						lbParticularPublico.setText(particular);
					} else {
						lbParticularPublico.setText(publico);
					}
					lbDuracion.setText(rs.getString("duracion"));
					lbAccesorios.setText(rs.getString("accesorios"));
					lbPrimaNeta.setText(rs.getString("prima_neta")+"€");
					lbEstado.setText(rs.getString("estado"));
					lbLiquidez.setText(rs.getString("liquidez"));
					if (null != rs.getString("comision")){
					lbComision.setText(rs.getString("comision") + "%");
					}
					lbTipoRiesgo.setText(rs.getString("tipo_riesgo"));
					lbFormaPago.setText(rs.getString("forma_pago"));
					lbCV.setText(rs.getString("CV"));
					lbFechaPrimeraMatricula.setText(String.valueOf(rs.getDate("fecha_primera_matricula")));
					lbPMA.setText(rs.getString("pma_kgs"));
					lbRemolque.setText(rs.getString("remolque"));
					lbTipoVehiculo.setText(rs.getString("tipo_vehiculo"));
					lbCobertura.setText(rs.getString("cobertura"));
					lbPMA.setText(rs.getString("pma_kgs"));
					lbBanco.setText(rs.getString("banco"));
					lbNumeroCuenta.setText(rs.getString("cuenta_bancaria"));
				}
				cont++;
			}
		} catch (SQLException e) {
			// JDBCTutorialUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
}
