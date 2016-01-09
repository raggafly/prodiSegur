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

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.IbCuotesInsure;
import model.IbCustomer;
import model.IbInsurance;
import model.TableInfoInsures;
import util.JDBCConnection;

public class InsureMenuOverviewController {
	@FXML
	private TextField tfPoliza;
	@FXML
	private TextField tfDNITitular;
	@FXML
	private Button btBuscar;
	@FXML
	private TextField tfApellidos;
	@FXML
	private TableView tbInsures;
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
	private TableColumn ColumnFechaInicioPago;
	@FXML
	private TableColumn ColumnFechaFinPago;
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
		List<TableInfoInsures> lti = new ArrayList<TableInfoInsures>();
		Statement stmt = null;
		JDBCConnection con = new JDBCConnection();
		TableInfoInsures tiInsure = new TableInfoInsures();
		String query = "select numero_poliza,dni_cif,(select descripcion from ib_master_values mv where ins.tipo_riesgo = mv.valor) as tipo_riesgo,compania,(select descripcion from ib_master_values mv where ins.estado = mv.valor) as estado,prima_neta,fecha_entrada_vigor,fecha_fin_entrada_vigor,fecha_inicio,fecha_fin from ib_insurance ins, ib_customer cus, ib_customer_relation rel where rel.id_cliente = cus.idib_customer and rel.id_seguro = ins.idib_insurance and cus.idib_customer = rel.id_cliente and rel.id_tipo =10  ";
		if (null != tfDNITitular.getText() && !tfDNITitular.getText().isEmpty()) {
			query += (" and cus.dni_cif = '" + tfDNITitular.getText() + "'");
		}

		if (null != tfPoliza.getText() && !tfPoliza.getText().isEmpty()) {
			query += (" and ins.numero_poliza = '" + tfPoliza.getText() + "'");
		}

		if (null != tfApellidos.getText() && !tfApellidos.getText().isEmpty()) {
			query += (" and cus.apellidos= '" + tfApellidos.getText() + "'");
		}

		try {
			stmt = con.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				tiInsure = new TableInfoInsures();
				tiInsure.setNumeroPoliza(rs.getString("numero_poliza"));
				tiInsure.setDni(rs.getString("dni_cif"));
				tiInsure.setTipo(rs.getString("tipo_riesgo"));
				tiInsure.setCompania(rs.getString("compania"));
				tiInsure.setPrimaNeta(rs.getDouble("prima_neta"));
				tiInsure.setEstado(rs.getString("estado"));
				tiInsure.setFechaVigor(rs.getDate("fecha_entrada_vigor"));
				tiInsure.setFechaFinVigor(rs.getDate("fecha_fin_entrada_vigor"));
				tiInsure.setFechaInicioPago(rs.getDate("fecha_inicio"));
				tiInsure.setFechaFinPago(rs.getDate("fecha_fin"));
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
//		tbInsures.setItems(obsInfoInsures);

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
		ColumnFechaInicioPago.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TableInfoInsures, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TableInfoInsures, String> info) {
						SimpleStringProperty property = new SimpleStringProperty();
						if (null != info.getValue().getFechaInicioPago()) {
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							property.setValue(dateFormat.format(info.getValue().getFechaInicioPago()));
						}
						return property;
					}
				});
		ColumnFechaFinPago.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<TableInfoInsures, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<TableInfoInsures, String> info) {
						SimpleStringProperty property = new SimpleStringProperty();
						if (null != info.getValue().getFechaFinPago()) {
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							property.setValue(dateFormat.format(info.getValue().getFechaFinPago()));
						}
						return property;
					}
				});
		tbInsures.getColumns().clear();
		tbInsures.setItems(obsInfoInsures);
		tbInsures.getColumns().addAll(ColumnNumeroPoliza);
		tbInsures.getColumns().addAll(ColumnDNITitular);
		tbInsures.getColumns().addAll(ColumnTipo);
		tbInsures.getColumns().addAll(ColumnEstado);
		tbInsures.getColumns().addAll(ColumnCompania);
		tbInsures.getColumns().addAll(ColumnPrimaNeta);
		tbInsures.getColumns().addAll(ColumnFechaVigor);
		tbInsures.getColumns().addAll(ColumnFechaFinVigor);
		tbInsures.getColumns().addAll(ColumnFechaInicioPago);
		tbInsures.getColumns().addAll(ColumnFechaFinPago);
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
				stage.setScene(new Scene(root, 1157, 870));
				stage.setScene(stage.getScene());
				InsureCuotesMenuOverviewController controller = (InsureCuotesMenuOverviewController) loader.getController();
				controller.initData(getInsurance(ti.getNumeroPoliza()), getCustomer(ti.getDni()),false);
				stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		if(listInsurance.size()>0){
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
		if(listCustomer.size()>0){
			cus = listCustomer.get(0);
		}		
		return cus;
	}
}
