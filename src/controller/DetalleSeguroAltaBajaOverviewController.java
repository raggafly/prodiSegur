package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.github.daytron.simpledialogfx.data.DialogResponse;
import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.github.daytron.simpledialogfx.dialog.DialogType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import model.IbCuotesInsure;
import model.IbInsurance;
import model.MasterTypes;
import util.JDBCConnection;
import util.MasterValueUtil;

public class DetalleSeguroAltaBajaOverviewController {
	@FXML
	private Label lbEnumTittularSeguro;
	@FXML
	private Label lbTitularSeguro;
	@FXML
	private Label lbEnumPropietario;
	@FXML
	private Label lbEnumConductor;
	@FXML
	private Label lbPropietario;
	@FXML
	private Label lbConductor;
	@FXML
	private Label lbNumeroPoliza;
	@FXML
	private Label lbCompania;
	@FXML
	private Label lbFechaInicio;
	@FXML
	private Label lbDuracion;
	@FXML
	private Label lbFechaEntradaVigor;
	@FXML
	private Label lbFechaFin;
	@FXML
	private Label lbPrimaNeta;
	@FXML
	private Label lbEstado;
	@FXML
	private Label lbLiquidez;
	@FXML
	private Label lbComision;
	@FXML
	private Label lbTipoRiesgo;
	@FXML
	private Label lbFormaPago;
	@FXML
	private Label lbTipoVehiculo;
	@FXML
	private Label lbMarca;
	@FXML
	private Label lbModelo;
	@FXML
	private Label lbCC;
	@FXML
	private Label lbParticularPublico;
	@FXML
	private Label lbMatricula;
	@FXML
	private Label lbCV;
	@FXML
	private Label lbFechaPrimeraMatricula;
	@FXML
	private Label lbPMA;
	@FXML
	private Label lbRemolque;
	@FXML
	private Label lbCobertura;
	@FXML
	private Label lbAccesorios;
	@FXML
	private Button btAltaBaja;
	@FXML
	private Label lbBanco;
	@FXML
	private Label lbNumeroCuenta;
	public String particular = "PARTICULAR";
	public String publico = "PÚBLICO";

	// Event Listener on Button[#btAltaBaja].onAction
	@FXML
	public void handleAltaBaja(ActionEvent event) {
		String message = "";
		EntityManagerFactory emf;
		EntityManager em;
		Byte pagado = 1;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		TypedQuery<IbCuotesInsure> query = em.createNamedQuery("IbCuotesInsure.findPayCoutes", IbCuotesInsure.class);
		query.setParameter("seguro", MasterValueUtil.getInsurance(lbNumeroPoliza.getText()));
		query.setParameter("pagado", pagado);
		List<IbCuotesInsure> listCuotesPagadas = query.getResultList();
		IbInsurance insu = MasterValueUtil.getInsurance(lbNumeroPoliza.getText());

		if (listCuotesPagadas.size() == 0 && lbEstado.getText().equals("VIGENTE")) {
			insu = MasterValueUtil.getInsurance(lbNumeroPoliza.getText());
			insu.setEstado(MasterTypes.DESCRIPTION_ESTADO_BAJA);

		} else {
			message = "El seguro tiene cuotas pagadas. No puede dar de baja.";

		}

		if (lbEstado.getText().equals("BAJA")) {
			insu.setEstado(MasterTypes.DESCRIPTION_ESTADO_VIGENTE);
			message = "";
		}

		if (!message.isEmpty()) {
			Dialog dialog = new Dialog(DialogType.ERROR, "INFORMACIÓN", message);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
		} else {
			Dialog dialog = new Dialog(DialogType.CONFIRMATION, "INFORMACIÓN",
					"¿Estás seguro qué desea cambiar el estado a: " + btAltaBaja.getText() + "la poliza: "
							+ lbNumeroPoliza.getText() + "?");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();

			if (dialog.getResponse() == DialogResponse.YES) {
				em.getTransaction().begin();
				em.merge(insu);
				em.getTransaction().commit();
				dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN", "Se ha puesto en estado "
						+ btAltaBaja.getText() + ". La poliza solicitada: " + lbNumeroPoliza.getText());
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
				dialog.showAndWait();
			}
		}
	}

	public void initData(String poliza) throws SQLException {
		Statement stmt = null;
		try {
			JDBCConnection con = new JDBCConnection();
			String query = "select CONCAT(c.nombre, ' ', c.Apellidos) As Nombre, t.descripcion as tipoUsuario , t.cod_tipo as codigo,  i.numero_poliza,compania,fecha_inicio,fecha_fin, (select descripcion from ib_master_values mv where duracion = mv.valor) as duracion ,(select descripcion from ib_master_values mv where ab.banco = mv.valor) as banco , CONCAT(ab.entidad, '    ', ab.oficina,'    ', ab.dc,'    ', ab.numero_cuenta) As cuenta_bancaria,prima_neta,fecha_entrada_vigor, (select descripcion from ib_master_values mv where estado = mv.valor) as estado,liquidez,comision, (select descripcion from ib_master_values mv where tipo_riesgo = mv.valor) as tipo_riesgo,   (select descripcion from ib_master_values mv where tipo_vehiculo = mv.valor) as tipo_vehiculo,  (select descripcion from ib_master_values mv where forma_pago = mv.valor) as forma_pago,   (select descripcion from ib_master_values mv where cobertura = mv.valor) as cobertura, marca,modelo,matricula,cc,cv,particular_publico,fecha_primera_matricula, pma_kgs,remolque,accesorios   FROM ib_insurance i LEFT OUTER JOIN ib_insurance_detail d ON d.id_seguro  = i.idib_insurance, ib_customer c, ib_customer_relation r, ib_customer_type t , ib_account_bank ab where c.ib_cuenta = ab.idib_account_bank AND i.numero_poliza='"
					+ poliza
					+ "'and  c.idib_customer = r.id_cliente and i.idib_insurance = r.id_seguro and t.idib_customer_type = r.id_tipo order by t.cod_tipo";

			stmt = con.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int cont = 0;
			while (rs.next()) {
				if (MasterTypes.TOMADOR.equals(rs.getString("codigo"))) {
					lbEnumTittularSeguro.setText(rs.getString("tipoUsuario") + ":");
					lbTitularSeguro.setText(rs.getString("nombre"));
				}
				if (MasterTypes.PROPIETARIO.equals(rs.getString("codigo"))) {
					lbEnumPropietario.setText(rs.getString("tipoUsuario") + ":");
					lbPropietario.setText(rs.getString("nombre"));
				}
				if (MasterTypes.CONDUCTOR.equals(rs.getString("codigo"))) {
					lbEnumConductor.setText(rs.getString("tipoUsuario") + ":");
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
					lbPrimaNeta.setText(rs.getString("prima_neta") + "€");
					lbEstado.setText(rs.getString("estado"));
					if (!rs.getString("estado").equals(MasterTypes.DESCRIPTION_FINALIZADO)) {
						if (rs.getString("estado").equals(MasterTypes.DESCRIPTION_VIGENTE)) {
							btAltaBaja.setText(MasterTypes.DESCRIPTION_BAJA);
						} else {
							btAltaBaja.setText(MasterTypes.DESCRIPTION_VIGENTE);
						}
					} else {
						btAltaBaja.setVisible(false);
					}
					lbLiquidez.setText(rs.getString("liquidez"));
					if (null != rs.getString("comision")) {
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
			 Logger.getLogger(DetalleSeguroAltaBajaOverviewController.class.getName()).log(Level.SEVERE, null, e);

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
}
