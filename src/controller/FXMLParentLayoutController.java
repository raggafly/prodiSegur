package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.print.DocFlavor.URL;

import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.github.daytron.simpledialogfx.dialog.DialogType;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.IbCustomer;
import model.IbInsurance;
import model.IbInsuranceDetail;
import model.MasterTypes;
import model.TableInfo;
import util.DateUtil;
import util.JDBCConnection;
import util.MasterValueUtil;

public class FXMLParentLayoutController implements Initializable {
	@FXML
	private Label lbFranquicia;
	@FXML
	private Label lbTomadorDNI;
	@FXML
	private Label lbPropietarioDNI;
	@FXML
	private Label lbConductorDNI;
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
	private Label lbPropietario;

	public Label getlbPropietario() {
		return lbPropietario;
	}

	@FXML
	private Label lbTomador;

	public Label getlbTomador() {
		return lbTomador;
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

//	@FXML
//	private Label lbFechaInicio;

//	public Label getlbFechaInicio() {
//		return lbFechaInicio;
//	}

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
		query2.setParameter("idseguro", seguro.getIdibInsurance());
		lid = query2.getResultList();
		if (null != lid && lid.size() > 0) {

			tabDetalleSeguro.setDisable(false);
		}
		em.close();
		try {
			JDBCConnection con = new JDBCConnection();

			String query = "select i.idib_insurance as idSeguro,c.DNI_CIF AS DNI,c.nombre As Nombre, c.Apellidos As apellidos, t.descripcion as tipoUsuario , t.cod_tipo as codigo,  i.numero_poliza,(select descripcion from ib_master_values mv where compania = mv.valor) as compania,fecha_inicio,fecha_fin_entrada_vigor, (select descripcion from ib_master_values mv where duracion = mv.valor) as duracion ,(select descripcion from ib_master_values mv where ab.banco = mv.valor) as banco , CONCAT(ab.entidad, '    ', ab.oficina,'    ', ab.dc,'    ', ab.numero_cuenta) As cuenta_bancaria,prima_neta,fecha_entrada_vigor, (select descripcion from ib_master_values mv where estado = mv.valor) as estado,liquidez,comision, (select descripcion from ib_master_values mv where tipo_riesgo = mv.valor) as tipo_riesgo,   (select descripcion from ib_master_values mv where tipo_vehiculo = mv.valor) as tipo_vehiculo,  (select descripcion from ib_master_values mv where forma_pago = mv.valor) as forma_pago,   (select descripcion from ib_master_values mv where cobertura = mv.valor) as cobertura, marca,modelo,matricula,cc,cv,particular_publico,fecha_primera_matricula, pma_kgs,remolque,accesorios,franquicia   FROM ib_customer c left OUTER JOIN ib_account_bank ab ON AB.iDIb_account_bank = C.ib_cuenta,ib_insurance i LEFT OUTER JOIN ib_insurance_detail d ON d.id_seguro  = i.idib_insurance, ib_customer_relation r, ib_customer_type t where i.numero_poliza='"
					+ poliza
					+ "' and  c.idib_customer = r.id_cliente and i.idib_insurance = r.id_seguro and t.idib_customer_type = r.id_tipo order by t.cod_tipo";

			stmt = con.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int cont = 0;
			String idSeguro = "";
			String apellidos = "";
			while (rs.next()) {
				apellidos = "";
				if (null != rs.getString("apellidos")) {
					apellidos = rs.getString("apellidos");
				}
				if (MasterTypes.TOMADOR.equals(rs.getString("codigo"))) {
					lbEnumTittularSeguro.setText(rs.getString("tipoUsuario") + ":");
					lbTomador.setText(rs.getString("nombre") + " " + apellidos);
					lbTomadorDNI.setText(rs.getString("DNI"));
				}
				if (MasterTypes.PROPIETARIO.equals(rs.getString("codigo"))) {
					lbEnumPropietario.setText(rs.getString("tipoUsuario") + ":");
					lbPropietario.setText(rs.getString("nombre") + " " + apellidos);
					lbPropietarioDNI.setText(rs.getString("DNI"));
				}

				if (cont == 0) {
					lbNumeroPoliza.setText(rs.getString("numero_poliza"));
					lbCompania.setText(rs.getString("compania"));
					lbMarca.setText(rs.getString("marca"));
					lbModelo.setText(rs.getString("modelo"));
					lbCC.setText(rs.getString("cc"));
					lbMatricula.setText(rs.getString("matricula"));
//					lbFechaInicio.setText(DateUtil.formatUtilDate(rs.getDate("fecha_inicio")));
					lbFechaFin.setText(String.valueOf(DateUtil.formatUtilDate(rs.getDate("fecha_fin_entrada_vigor"))));
					lbFechaEntradaVigor.setText(DateUtil.formatUtilDate(rs.getDate("fecha_entrada_vigor")));
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
					lbLiquidez.setText(rs.getString("liquidez"));
					if (null != rs.getString("comision")) {
						lbComision.setText(rs.getString("comision") + "%");
					}
					lbTipoRiesgo.setText(rs.getString("tipo_riesgo"));
					lbFormaPago.setText(rs.getString("forma_pago"));
					lbCV.setText(rs.getString("CV"));
					lbFechaPrimeraMatricula.setText(DateUtil.formatUtilDate(rs.getDate("fecha_primera_matricula")));
					lbPMA.setText(rs.getString("pma_kgs"));
					lbRemolque.setText(rs.getString("remolque"));
					lbTipoVehiculo.setText(rs.getString("tipo_vehiculo"));
					lbCobertura.setText(rs.getString("cobertura"));
					if(rs.getDouble("franquicia") != 0D){
					lbFranquicia.setText(rs.getString("franquicia")+" €");
					}else{
						lbFranquicia.setText("");
					}
					lbBanco.setText(rs.getString("banco"));
					lbNumeroCuenta.setText(rs.getString("cuenta_bancaria"));
				}
				idSeguro = rs.getString("idSeguro");
				cont++;
			}
			if (!idSeguro.isEmpty() && !idSeguro.equals("0")) {
				String queryConductor = "select c.nombre As Nombre,c.dni_cif as DNI, c.Apellidos As apellidos,t.descripcion as tipoUsuario,t.cod_tipo as codigo from ib_customer_type t,ib_customer c, ib_customer_relation r where r.id_cliente = c.idib_customer and r.id_seguro ="
						+ idSeguro + " and t.idib_customer_type = r.id_tipo and r.id_tipo =" + 12;
				stmt = con.getConnection().createStatement();
				ResultSet rsConductor = stmt.executeQuery(queryConductor);

				while (rsConductor.next()) {
					if (MasterTypes.CONDUCTOR.equals(rsConductor.getString("codigo"))) {
						lbEnumConductor.setText(rsConductor.getString("tipoUsuario") + ":");
					}
					if (null != rsConductor.getString("apellidos")) {
						apellidos = rsConductor.getString("apellidos");
					}
					lbConductor.setText(rsConductor.getString("Nombre") + " " + apellidos);
					lbConductorDNI.setText(rsConductor.getString("DNI"));
				}
			}
		} catch (SQLException e) {
			// JDBCTutorialUtilities.printSQLException(e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	@FXML
	public void handlePDF(ActionEvent event) throws DocumentException, IOException {
		IbCustomer cusTomador = MasterValueUtil.getCustomer(lbTomadorDNI.getText());
		Date fechaDate = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
		String fecha = formateador.format(fechaDate);
		try {
		if (!tabDetalleSeguro.isDisabled()) {
			PdfReader reader = new PdfReader("image/auto.pdf");
			FileOutputStream fos = new FileOutputStream("image/formato_llenado.pdf");
			PdfStamper stamper = new PdfStamper(reader, fos);
			AcroFields form = stamper.getAcroFields();

			// DATOS TITULAR //TOMADOR
			form.setField("nombreTomador", lbTomador.getText());
			form.setField("DNINIF", lbTomadorDNI.getText());
			form.setField("DIRECCION", cusTomador.getDireccion());
			form.setField("CP", cusTomador.getCodPostal());
			form.setField("POBLACIÓN", cusTomador.getPoblacion());
			form.setField("PROVINCIA", cusTomador.getProvincia());
			form.setField("FECHA NACIMIENTO", DateUtil.formatUtilDate(cusTomador.getFechaNacimiento()));
			form.setField("TELEFONO", cusTomador.getTelefono());
			form.setField("FECHA CARNET", DateUtil.formatUtilDate(cusTomador.getFechaCarnet()));

			// DATOS PROPIETARIO
			cusTomador = MasterValueUtil.getCustomer(lbPropietarioDNI.getText());
			if(cusTomador!=null){
			form.setField("NOMBRE Y APELLIDOS_2", lbPropietario.getText());
			form.setField("DNINIF_2", lbPropietarioDNI.getText());
			form.setField("DIRECCION_2", cusTomador.getDireccion());
			form.setField("CP_2", cusTomador.getCodPostal());
			form.setField("POBLACIÓN_2", cusTomador.getPoblacion());
			form.setField("PROVINCIA_2", cusTomador.getProvincia());
			form.setField("FECHA NACIMIENTO_2", DateUtil.formatUtilDate(cusTomador.getFechaNacimiento()));
			form.setField("TELEFONO_2", cusTomador.getTelefono());
			form.setField("FECHA CARNET_2", DateUtil.formatUtilDate(cusTomador.getFechaCarnet()));
			}
			// DATOS CONDUCTOR
			cusTomador = MasterValueUtil.getCustomer(lbConductorDNI.getText());
			if(cusTomador!=null){
			form.setField("NOMBRE Y APELLIDOS_3", lbConductor.getText());
			form.setField("DNINIF_3", lbConductorDNI.getText());
			form.setField("DIRECCION_3", cusTomador.getDireccion());
			form.setField("CP_3", cusTomador.getCodPostal());
			form.setField("POBLACIÓN_3", cusTomador.getPoblacion());
			form.setField("PROVINCIA_3", cusTomador.getProvincia());
			form.setField("FECHA NACIMIENTO_3", DateUtil.formatUtilDate(cusTomador.getFechaNacimiento()));
			form.setField("TELEFONO_3", cusTomador.getTelefono());
			form.setField("FECHA CARNET_3", DateUtil.formatUtilDate(cusTomador.getFechaCarnet()));
			}
			form.setField("CC", lbCC.getText());
			form.setField("CV", lbCV.getText());
			form.setField("MARCAMODELO", lbMarca.getText() + " " + lbModelo.getText());
			form.setField("MATRICULA", lbMatricula.getText());
			form.setField("TIPO DE VEHICULO", lbTipoVehiculo.getText());
			form.setField("FECHA 1 MATRICULACION", lbFechaPrimeraMatricula.getText());
			if (!lbPMA.getText().equals("0")) {
				form.setField("PMA Kgs", lbPMA.getText());
			}
			form.setField("DESCRIPCIÓN Y VALOR DE ACCESORIOS", lbAccesorios.getText());

			String states[] = reader.getAcroFields().getAppearanceStates("CBTERCEROS");
			if (lbParticularPublico.equals("PÚBLICO")) {
				form.setField("CBPUBLICO", states[0]);
			}

			if (lbCobertura.getText().equals("TODO RIESGO")) {
				form.setField("CBTODORIESGO", states[0]);
			} else if (lbCobertura.getText().equals("TERCEROS")) {
				form.setField("CBTERCEROS", states[0]);
			} else if (lbCobertura.getText().equals("TERCEROS COMBINADO")) {
				form.setField("CBTERCEROSLUNASROBO", states[0]);
			} else if (lbCobertura.getText().equals("TERCEROS+LUNAS")) {
				form.setField("CBTERCEROSLUNAS", states[0]);
			} else if (lbCobertura.getText().equals("TERCEROS COMBINADO (ROBO,INCENDIO,LUNAS)")) {
				form.setField("CBTERCEROSLUNASROBO", states[0]);
			} else if (lbCobertura.getText().equals("TERCEROS COMBINADO+PERDIDA TOTAL")) {
				form.setField("CBTERCEROSPTOTAL", states[0]);
			} else if (lbCobertura.getText().equals("TODO RIESGO FRANQUICIA")) {
				form.setField("CBTODORIESGOF", states[0]);
				form.setField("TODO RIESGOFRANQUICIA", "0");
			}
			if(!lbFranquicia.getText().equals("0")){
			form.setField("FRANQUICIA", lbFranquicia.getText());
			}

			if (null != lbRemolque.getText() && !lbRemolque.getText().isEmpty()) {
				form.setField("CBREMOLQUE", states[0]);
			}

			form.setField("FECHA ENTRADA EN VIGOR", lbFechaEntradaVigor.getText());
			form.setField("DURACIÓN", lbDuracion.getText());
			if (lbFormaPago.getText().equals("ANUAL")) {
				form.setField("CBANUAL", states[0]);
			} else if (lbFormaPago.getText().equals("SEMESTRAL")) {
				form.setField("CBSEMESTRAL", states[0]);
			} else if (lbFormaPago.getText().equals("TRIMESTRAL")) {
				form.setField("CBTRIMESTRAL", states[0]);
			} else if (lbFormaPago.getText().equals("MENSUAL")) {
				form.setField("CBMENSUAL", states[0]);
			}

			// DATOS BANCARIOS
			form.setField("BANCO", lbBanco.getText());
			String cuenta = lbNumeroCuenta.getText().trim();
			cuenta = cuenta.replace(" ", "");
			form.setField("CODIGO_DOMICILIACION", cuenta.substring(0, 4));
			form.setField("OFICINA", cuenta.substring(4, 8));
			form.setField("DC", cuenta.substring(8, 10));
			form.setField("N_CUENTA", cuenta.substring(10, 20));			
			form.setField("FECHA", fecha);

			stamper.close();

			if (Desktop.isDesktopSupported()) {
				try {
					File myFile = new File("image/formato_llenado.pdf");
					Desktop.getDesktop().open(myFile);
				} catch (IOException ex) {
					// no application registered for PDFs
				}
			}
		} else {
			PdfStamper stamper  = null;
			
				PdfReader reader = new PdfReader("image/vida.pdf");
				FileOutputStream fos = new FileOutputStream("image/formato_llenado_vida.pdf");
				stamper= new PdfStamper(reader, fos);
				AcroFields form = stamper.getAcroFields();

				form.setField("nombre", lbTomador.getText());
				form.setField("direccion", cusTomador.getDireccion());
				form.setField("municipio", cusTomador.getPoblacion());
				form.setField("cp", cusTomador.getCodPostal());				
				form.setField("fecha", fecha);
				stamper.close();

				if (Desktop.isDesktopSupported()) {
					try {
						File myFile = new File("image/formato_llenado_vida.pdf");
						Desktop.getDesktop().open(myFile);
					} catch (IOException ex) {
						Dialog dialog = new Dialog(DialogType.ERROR, "ERROR", ex.getMessage());
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
						dialog.showAndWait();
					}
				}
			}
		}
			catch (IOException ex) {

				System.err.println("Existe el documento abierto!");
				Dialog dialog = new Dialog(DialogType.ERROR, "ERROR", ex.getMessage());
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
				dialog.showAndWait();
				

			}
		}
	}
