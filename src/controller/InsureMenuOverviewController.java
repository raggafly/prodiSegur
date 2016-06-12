package controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.github.daytron.simpledialogfx.data.DialogResponse;
import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.github.daytron.simpledialogfx.dialog.DialogType;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.IbCuotesInsure;
import model.IbCustomer;
import model.IbCustomerRelation;
import model.IbInsurance;
import model.IbInsuranceDetail;
import model.MasterTypes;
import model.TableInfoInsures;
import util.JDBCConnection;

public class InsureMenuOverviewController {
	@FXML
	private TextField tfPoliza;
	@FXML
	private TextField tfOrden;
	@FXML
	private TextField tfDNITitular;
	@FXML
	private Button btBuscar;
	@FXML
	private TextField tfApellidos;
	@FXML
	private TableView<TableInfoInsures> tbInsures;
	@FXML
	private TableColumn ColumnOrden;
	@FXML
	private TableColumn ColumnNumeroPoliza;
	@FXML
	private TableColumn ColumnDNITitular;
	@FXML
	private TableColumn ColumnCompania;
	@FXML
	private TableColumn ColumnPrimaNeta;
	@FXML
	private TableColumn ColumnFechaVigor;
	@FXML
	private TableColumn ColumnFechaEfecto;
	@FXML
	private TableColumn ColumnFechaFinVigor;
	@FXML
	private TableColumn ColumnTipo;
	@FXML
	private TableColumn ColumnEstado;
	@FXML
	private Button btVerCuotas;

	// Event Listener on Button[#btBuscar].onAction
	@FXML
	public void handleBuscar(ActionEvent event) {
		buscar();
	}

	public void buscar() {
		List<TableInfoInsures> lti = new ArrayList<TableInfoInsures>();
		Statement stmt = null;
		JDBCConnection con = new JDBCConnection();
		TableInfoInsures tiInsure = new TableInfoInsures();
		String query = "select idib_insurance as orden,cus.nombre, cus.apellidos,numero_poliza,dni_cif,(select descripcion from ib_master_values mv where ins.tipo_riesgo = mv.valor) as tipo_riesgo,(select descripcion from ib_master_values mv where ins.compania = mv.valor) as compania,(select descripcion from ib_master_values mv where ins.estado = mv.valor) as estado,prima_neta,fecha_entrada_vigor,fecha_fin_entrada_vigor,fecha_efecto,fecha_inicio,fecha_fin from ib_insurance ins, ib_customer cus, ib_customer_relation rel where rel.id_cliente = cus.idib_customer and rel.id_seguro = ins.idib_insurance and cus.idib_customer = rel.id_cliente and rel.id_tipo =11  ";
		if (null != tfDNITitular.getText() && !tfDNITitular.getText().isEmpty()) {
			query += (" and cus.dni_cif = '" + tfDNITitular.getText() + "'");
		}

		if (null != tfPoliza.getText() && !tfPoliza.getText().isEmpty()) {
			query += (" and ins.numero_poliza = '" + tfPoliza.getText() + "'");
		}
		
		if (null != tfOrden.getText() && !tfOrden.getText().isEmpty()) {
			query += (" and ins.idib_insurance = '" + tfOrden.getText() + "'");
		}

		if (null != tfApellidos.getText() && !tfApellidos.getText().isEmpty()) {
			query += (" and cus.apellidos= '" + tfApellidos.getText() + "'");
		}

		try {
			stmt = con.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				tiInsure = new TableInfoInsures();
				tiInsure.setOrden(rs.getString("orden"));
				tiInsure.setNumeroPoliza(rs.getString("numero_poliza"));
				tiInsure.setDni(rs.getString("dni_cif"));
				tiInsure.setTipo(rs.getString("tipo_riesgo"));
				tiInsure.setCompania(rs.getString("compania"));
				tiInsure.setPrimaNeta(rs.getDouble("prima_neta"));
				tiInsure.setEstado(rs.getString("estado"));
				tiInsure.setFechaVigor(rs.getDate("fecha_entrada_vigor"));
				tiInsure.setFechaFinVigor(rs.getDate("fecha_fin_entrada_vigor"));
				tiInsure.setFechaEfecto(rs.getDate("fecha_efecto"));
				tiInsure.setNombre(rs.getString("nombre") + " " + rs.getString("apellidos"));
				lti.add(tiInsure);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		ObservableList<TableInfoInsures> obsInfoInsures = FXCollections.observableArrayList(lti);
		// tbInsures.setItems(obsInfoInsures);
		ColumnOrden.setCellValueFactory(new PropertyValueFactory<TableInfoInsures, String>("orden"));
		ColumnNumeroPoliza.setCellValueFactory(new PropertyValueFactory<TableInfoInsures, String>("numeroPoliza"));
		ColumnDNITitular.setCellValueFactory(new PropertyValueFactory<TableInfoInsures, String>("dni"));
		ColumnTipo.setCellValueFactory(new PropertyValueFactory<TableInfoInsures, String>("tipo"));
		ColumnEstado.setCellValueFactory(new PropertyValueFactory<TableInfoInsures, String>("estado"));
		ColumnCompania.setCellValueFactory(new PropertyValueFactory<TableInfoInsures, String>("compania"));
		ColumnPrimaNeta.setCellValueFactory(new PropertyValueFactory<TableInfoInsures, Double>("primaNeta"));
		ColumnFechaVigor.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TableInfoInsures, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TableInfoInsures, String> info) {
						SimpleStringProperty property = new SimpleStringProperty();
						if (null != info.getValue().getFechaVigor()) {
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							property.setValue(dateFormat.format(info.getValue().getFechaVigor()));
						}
						return property;
					}
				});
		ColumnFechaFinVigor.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TableInfoInsures, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TableInfoInsures, String> info) {
						SimpleStringProperty property = new SimpleStringProperty();
						if (null != info.getValue().getFechaFinVigor()) {
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							property.setValue(dateFormat.format(info.getValue().getFechaFinVigor()));
						}
						return property;
					}
				});
		ColumnFechaEfecto.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TableInfoInsures, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TableInfoInsures, String> info) {
						SimpleStringProperty property = new SimpleStringProperty();
						if (null != info.getValue().getFechaEfecto()) {
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							property.setValue(dateFormat.format(info.getValue().getFechaEfecto()));
						}
						return property;
					}
				});
		tbInsures.getColumns().clear();
		tbInsures.setItems(obsInfoInsures);
		tbInsures.getColumns().addAll(ColumnOrden);
		tbInsures.getColumns().addAll(ColumnNumeroPoliza);
		tbInsures.getColumns().addAll(ColumnDNITitular);
		tbInsures.getColumns().addAll(ColumnTipo);
		tbInsures.getColumns().addAll(ColumnEstado);
		tbInsures.getColumns().addAll(ColumnCompania);
		tbInsures.getColumns().addAll(ColumnPrimaNeta);
		tbInsures.getColumns().addAll(ColumnFechaVigor);
		tbInsures.getColumns().addAll(ColumnFechaFinVigor);
		tbInsures.getColumns().addAll(ColumnFechaEfecto);
	}

	// Event Listener on Button[#btVerCuotas].onAction
	@FXML
	public void handleVerCuotas(ActionEvent event) {
		TableInfoInsures ti = (TableInfoInsures) tbInsures.getSelectionModel().getSelectedItem();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/InsureCuotesMenuOverview.fxml"));
		Parent root;
		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle("Gestion de Cuotas");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(root, 1157, 870));
			stage.setScene(stage.getScene());
			InsureCuotesMenuOverviewController controller = (InsureCuotesMenuOverviewController) loader.getController();
			if(ti != null && !ti.getNumeroPoliza().isEmpty()){
			controller.initData(getInsurance(ti.getNumeroPoliza()), getCustomer(ti.getDni()), false);
			stage.showAndWait();
			buscar();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleEliminar(ActionEvent event) {
		IbCuotesInsure ci = new IbCuotesInsure();
		String message = "";
		TableInfoInsures ti = (TableInfoInsures) tbInsures.getSelectionModel().getSelectedItem();

		/*
		 * Se procede ha eliminar el seguro siempre y cuando: 1º No tenga cuotas
		 * pagadas. 2º Esté en estado vigente ¿finalizado?
		 */
		if (ti != null) {
			IbInsurance seguro = getInsurance(ti.getNumeroPoliza());
			Dialog dialog = new Dialog(DialogType.CONFIRMATION, "INFORMACIÓN",
					"¿Estás seguro qué desear eliminar la poliza: " + ti.getNumeroPoliza() + "\n Perteneciente a: "
							+ ti.getNombre() + "?");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
			if (dialog.getResponse() == DialogResponse.YES) {
				EntityManagerFactory emf;
				EntityManager em;
				emf = Persistence.createEntityManagerFactory("prodiSegur");
				em = emf.createEntityManager();
				if (!seguro.getEstado().equals(MasterTypes.DESCRIPTION_ESTADO_BAJA)) {
					if (!cuotasPagadas(seguro)) {
						// borramos todo lo que tiene que ver con el seguro.

						TypedQuery<IbCuotesInsure> queryCuotes = em
								.createNamedQuery("IbCuotesInsure.findCoutesByInsure", IbCuotesInsure.class);
						queryCuotes.setParameter("seguro", seguro);
						List<IbCuotesInsure> listOfficialCuotes = queryCuotes.getResultList();
						// borramos las cuotas
						// for (int i = 0; i < listOfficialCuotes.size(); i++) {
						// em.getTransaction().begin();
						// ci = listOfficialCuotes.get(i);
						// em.remove(ci);
						// em.getTransaction().commit();
						// }

						// borramos la relacion de seguros con clientes.
						List<IbCustomerRelation> lcr = new ArrayList<IbCustomerRelation>();
						TypedQuery<IbCustomerRelation> query = em.createNamedQuery("IbCustomerRelation.findSeguro",
								IbCustomerRelation.class);
						query.setParameter("idseguro", seguro);
						lcr = query.getResultList();
						IbCustomerRelation cr = new IbCustomerRelation();

						em.getTransaction().begin();
						for (int i = 0; i < lcr.size(); i++) {
							cr = lcr.get(i);
							em.remove(cr);
							// em.getTransaction().commit();
							em.flush();
						}

						// Borramos el seguro.
						// em.getTransaction().begin();
						// em.remove(seguro);
						// em.getTransaction().commit();
						List<IbInsuranceDetail> lid = null;
						TypedQuery<IbInsuranceDetail> query2 = em.createNamedQuery("IbInsuranceDetail.findBySeguro",
								IbInsuranceDetail.class);
						query2.setParameter("idseguro", seguro.getIdibInsurance());
						lid = query2.getResultList();

						if (null != lid && lid.size() > 0) {
							// em.getTransaction().begin();
							em.remove(lid.get(0));
							// em.getTransaction().commit();
							em.flush();
						}

						// em.getTransaction().begin();
						seguro = em.find(IbInsurance.class, seguro.getIdibInsurance());
						// seguro = em.merge(seguro);
						em.remove(seguro);
						em.flush();
						// em.getTransaction().commit();
						em.getTransaction().commit();

						em.close();
						buscar();
						message = "Se ha eliminado la poliza: " + seguro.getNumeroPoliza();
					} else {
						message = "Existen cuotas con pagos abonados.";
					}

				} else {
					message = "No se puede eliminar una poliza dado de Baja.";
				}

			}

		} else {
			message = "No has seleccionado ninguna poliza.";
		}

		if (!message.isEmpty()) {
			Dialog dialogError = new Dialog(DialogType.INFORMATION, "INFORMACIÓN", message);
			dialogError.initModality(Modality.WINDOW_MODAL);
			dialogError.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialogError.showAndWait();
		}

	}

	private boolean cuotasPagadas(IbInsurance datosSeguro) {
		// saber si tiene cuotas anteriores pagadas.
		boolean cuotasPagadas = false;
		EntityManagerFactory emf;
		EntityManager em;
		Byte pagado = 1;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		TypedQuery<IbCuotesInsure> query = em.createNamedQuery("IbCuotesInsure.findPayCoutes", IbCuotesInsure.class);
		query.setParameter("seguro", datosSeguro);
		query.setParameter("pagado", pagado);
		List<IbCuotesInsure> listCuotasPagadas = query.getResultList();

		if (listCuotasPagadas.size() > 0) {
			cuotasPagadas = true;
		}

		em.close();
		return cuotasPagadas;
	}

	private IbInsurance getInsurance(String numeroPoliza) {
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		TypedQuery<IbInsurance> query = em.createNamedQuery("IbInsurance.findByPoliza", IbInsurance.class);
		query.setParameter("poliza", numeroPoliza);
		List<IbInsurance> listInsurance = query.getResultList();
		IbInsurance insurance = null;
		if (listInsurance.size() > 0) {
			insurance = listInsurance.get(0);
		}
		return insurance;
	}

	private IbCustomer getCustomer(String dni) {
		// TODO Auto-generated method stub
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		TypedQuery<IbCustomer> query = em.createNamedQuery("IbCustomer.findDNI", IbCustomer.class);
		query.setParameter("dni", dni);
		List<IbCustomer> listCustomer = query.getResultList();
		IbCustomer cus = null;
		if (listCustomer.size() > 0) {
			cus = listCustomer.get(0);
		}
		return cus;
	}

	private IbInsurance actualizarSeguro(IbInsurance datosSeguro, String estado) {
		// INEST00-->estados
		datosSeguro.setEstado(estado);

		return datosSeguro;
	}

	@FXML
	public void handleSeleccionarElemento(MouseEvent event) {
		((Node) (event.getSource())).getScene().lookup("#tbInsures").setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {

				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if (mouseEvent.getClickCount() == 2) {
						System.out.println("Double clicked");
						TableInfoInsures tableInfo = (TableInfoInsures) tbInsures.getSelectionModel().getSelectedItem();
						if (tableInfo != null) {
							InsuranceManagementMenuOverviewController parent = new InsuranceManagementMenuOverviewController();
							FXMLLoader loader = new FXMLLoader(
									getClass().getResource("/views/InsuranceManagementMenuOverview.fxml"));

							try {
								Parent root;
								root = (Parent) loader.load();
								Stage stage = new Stage();
								stage.setTitle("Detalle de Poliza");
								InsuranceManagementMenuOverviewController controller = loader
										.<InsuranceManagementMenuOverviewController> getController();
								stage.initModality(Modality.APPLICATION_MODAL);
								stage.setScene(new Scene(root, 600, 543));

								stage.setScene(stage.getScene());
								controller.initData(getInsurance(tableInfo.getNumeroPoliza()),
										getCustomer(tableInfo.getDni()));

								stage.showAndWait();
								buscar();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
	}

	@FXML
	public void handleBaja(Event event) {
		TableInfoInsures tableInfo = (TableInfoInsures) tbInsures.getSelectionModel().getSelectedItem();
		DetalleSeguroAltaBajaOverviewController parent = new DetalleSeguroAltaBajaOverviewController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DetalleSeguroAltaBajaOverview.fxml"));

		try {
			Parent root;
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle("Detalle de Poliza");
			DetalleSeguroAltaBajaOverviewController controller = loader
					.<DetalleSeguroAltaBajaOverviewController> getController();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(root, 600, 511));

			stage.setScene(stage.getScene());
			try {
				controller.initData(tableInfo.getNumeroPoliza());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			stage.showAndWait();
			buscar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
