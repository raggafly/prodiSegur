/*
 * Copyright (c) 2011, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.IbCustomer;
import model.IbInsurance;
import model.IbInsuranceDetail;
import model.MasterTypes;
import model.TableInfo;
import util.JDBCConnection;
import util.MasterValueUtil;

public class FXMLLoginController {
	@FXML
	private Text actiontarget;
	@FXML
	private TableColumn<TableInfo, String> columnaPoliza;
	@FXML
	private TableColumn<TableInfo, String> columnaNombre;
	@FXML
	private TableColumn<TableInfo, String> columnaApellidos;
	@FXML
	private TableColumn<TableInfo, String> columnaDniCif;
	@FXML
	private TableColumn<TableInfo, String> columnaTelefono;
	@FXML
	private TableColumn<TableInfo, String> columnaTipo;
	@FXML
	private TableColumn<TableInfo, String> columnaEstado;
	@FXML
	private TableColumn<TableInfo, String> columnaTipoRiesgo;
	@FXML
	private TableColumn<TableInfo, String> columnaPrimaNeta;
	@FXML
	private TableColumn<TableInfo, String> columnaCompania;
	@FXML
	private TableView tablaBusqueda;
	@FXML
	private MenuItem mClienteGestion;
	@FXML
	private MenuItem mSeguroGestion;
	@FXML
	private MenuItem mSeguroBaja;
	@FXML
	private MenuItem mCuotasGestion;
	@FXML
	private MenuItem mAyudaDocumentacion;
	@FXML
	private ComboBox cbTipoRiesgo;

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
		actiontarget.setText("Sign in button pressed");

		Parent root;
		try {
			EntityManagerFactory emf;
			EntityManager em;
			emf = Persistence.createEntityManagerFactory("prodiSegur");
			em = emf.createEntityManager();
			TypedQuery<String> query = em.createNamedQuery("IbMasterValue.findByType", String.class);
			TypedQuery<String> queryCustomerType = em.createNamedQuery("IbCustomerType.findByType", String.class);
			query.setParameter("type", "INEST00");
			List<String> listEstados = query.getResultList();
			listEstados.add(0, "");
			List<String> listCustomerType = queryCustomerType.getResultList();
			listCustomerType.add(0, "");
			TypedQuery<String> queryRiesgo = em.createNamedQuery("IbMasterValue.findByType", String.class);
			queryRiesgo.setParameter("type", MasterTypes.TYPE_RIESGO);
			List<String> listTipoRiesgo = queryRiesgo.getResultList();
			listTipoRiesgo.add(0, "");

			root = FXMLLoader.load(getClass().getResource("/views/parentLayout.fxml"));

			Stage stage = new Stage();
			stage.setTitle("Pantalla Principal");
			stage.setScene(new Scene(root, 1500, 800));

			stage.show();
			ComboBox cbTipoSeguro = (ComboBox) ((Node) (stage.getScene().lookup("#cbTipoSeguro")));
			ComboBox cbTipoUsuario = (ComboBox) ((Node) (stage.getScene().lookup("#cbTipoUsuario")));
			ComboBox cbTipoRiesgo = (ComboBox) ((Node) (stage.getScene().lookup("#cbTipoRiesgo")));

			ObservableList<String> listaObservable = FXCollections.observableArrayList(listEstados);
			ObservableList<String> listaObservableTipoUsuario = FXCollections.observableArrayList(listCustomerType);
			ObservableList<String> listaObservableTipoRiesgo = FXCollections.observableArrayList(listTipoRiesgo);
			cbTipoSeguro.setItems(listaObservable);
			cbTipoUsuario.setItems(listaObservableTipoUsuario);
			cbTipoRiesgo.setItems(listaObservableTipoRiesgo);

			stage.setOnCloseRequest(e -> {
				System.exit(-1);
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		((Node) (event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	protected void seleccionarElemento(MouseEvent event) {

		((Node) (event.getSource())).getScene().lookup("#tablaBusqueda")
				.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {

						if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
							if (mouseEvent.getClickCount() == 2) {
								TableInfo tableInfo = (TableInfo) tablaBusqueda.getSelectionModel().getSelectedItem();
								FXMLParentLayoutController parent = new FXMLParentLayoutController();
								FXMLLoader loader = new FXMLLoader(
										getClass().getResource("/views/detalleSeguroLayout.fxml"));
								try {
									Parent root;
									root = (Parent) loader.load();
									Stage stage = new Stage();
									stage.setTitle("Detalle de Poliza");
									FXMLParentLayoutController controller = loader
											.<FXMLParentLayoutController> getController();
									stage.initModality(Modality.APPLICATION_MODAL);
									stage.setScene(new Scene(root, 600, 600));

									stage.setScene(stage.getScene());
									try {
										controller.initData(tableInfo.getNumeroPoliza());
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									stage.show();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				});

	}

	@FXML
	protected void actionAlta(ActionEvent event) throws SQLException {
		boolean isConductor = false;
		List<String> choices = new ArrayList<>();
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();

		TypedQuery<String> query = em.createNamedQuery("IbMasterValue.findByType", String.class);
		query.setParameter("type", MasterTypes.TYPE_RIESGO);
		List<String> listTipoRiesgo = query.getResultList();

		Parent root;

		int i = 0;
		for (i = 0; i < listTipoRiesgo.size(); i++) {
			choices.add(listTipoRiesgo.get(i));
		}

		ChoiceDialog<String> dialog = new ChoiceDialog<String>(choices.get(0), choices);
		dialog.setTitle("Eleccion Seguro");
		dialog.setHeaderText("Tipo de Seguro");
		dialog.setContentText("Elige tipo de seguro:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			System.out.println("Your choice: " + result.get());
			TypedQuery<String> queryByDescription = em.createNamedQuery("IbMasterValue.findByDescription",
					String.class);
			queryByDescription.setParameter("description", result.get().toString());
			List<String> listTipoDescripcionRiesgo = queryByDescription.getResultList();
			if (null != listTipoDescripcionRiesgo.get(0)
					&& listTipoDescripcionRiesgo.get(0).toString().equals("CONDUCTOR")) {
				isConductor = true;
			}
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PersonOverview.fxml"));

		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			if (result.isPresent()) {
				stage.setTitle("Alta de un seguro de tipo : " + result.get());
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.setScene(new Scene(root, 800, 600));
				stage.setScene(stage.getScene());

				PersonOverviewController controller = (PersonOverviewController) loader.getController();
				controller.initData(result.get(), isConductor, true);
				stage.show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void actionBuscar(ActionEvent event) throws SQLException {

		TableView tb = (TableView) ((Node) (event.getSource())).getScene().lookup("#tablaBusqueda");

		List<TableInfo> list = new ArrayList<TableInfo>();
		JDBCConnection con = new JDBCConnection();
		list = extractInfTableView(con.getConnection(), event);
		tb.getColumns().clear();

		ObservableList<TableInfo> listaObservable = FXCollections.observableArrayList(list);

		columnaPoliza = new TableColumn("Nº Poliza");
		columnaNombre = new TableColumn("Nombre");
		columnaApellidos = new TableColumn("Apellidos");
		columnaDniCif = new TableColumn("DNI/CIF");
		columnaTelefono = new TableColumn("Teléfono");
		columnaTipoRiesgo = new TableColumn("Tipo Riesgo");
		columnaEstado = new TableColumn("Estado");
		columnaTipo = new TableColumn("Tipo");
		columnaPrimaNeta = new TableColumn("Prima Neta");
		columnaCompania = new TableColumn("Companía");

		columnaNombre.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("nombre"));
		columnaApellidos.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("apellidos"));
		columnaDniCif.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("dni"));
		columnaTelefono.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("telefono"));
		columnaPoliza.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("numeroPoliza"));
		columnaTipoRiesgo.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("tipoRiesgo"));
		columnaEstado.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("estado"));
		columnaTipo.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("tipo"));
		columnaPrimaNeta.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("primaNeta"));
		columnaCompania.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("compania"));

		tb.setItems(listaObservable);
		tb.getColumns().addAll(columnaPoliza);
		tb.getColumns().addAll(columnaNombre);
		tb.getColumns().addAll(columnaApellidos);
		tb.getColumns().addAll(columnaDniCif);
		tb.getColumns().addAll(columnaTelefono);
		tb.getColumns().addAll(columnaTipoRiesgo);
		tb.getColumns().addAll(columnaEstado);
		tb.getColumns().addAll(columnaTipo);
		tb.getColumns().addAll(columnaPrimaNeta);
		tb.getColumns().addAll(columnaCompania);

	}

	public Collection<IbCustomer> findAllClientes(EntityManager em) {
		return (Collection<IbCustomer>) em.createNamedQuery("IbCustomer.findAll").getResultList();
	}

	public List<TableInfo> extractInfTableView(java.sql.Connection connection, ActionEvent event) throws SQLException {
		TextField tfDni = (TextField) ((Node) (event.getSource())).getScene().lookup("#tfdni");
		TextField tfPoliza = (TextField) ((Node) (event.getSource())).getScene().lookup("#tfPoliza");
		TextField tfTelefono = (TextField) ((Node) (event.getSource())).getScene().lookup("#tfTelefono");
		TextField tfNombre = (TextField) ((Node) (event.getSource())).getScene().lookup("#tfNombre");
		TextField tfApellidos = (TextField) ((Node) (event.getSource())).getScene().lookup("#tfApellidos");
		ComboBox<String> cbTipoSeguro = (ComboBox<String>) ((Node) (event.getSource())).getScene()
				.lookup("#cbTipoSeguro");
		ComboBox<String> cbTipoRiesgo = (ComboBox<String>) ((Node) (event.getSource())).getScene()
				.lookup("#cbTipoRiesgo");

		ComboBox cbTipoUsuario = (ComboBox<String>) ((Node) (event.getSource())).getScene().lookup("#cbTipoUsuario");
		TableInfo ti;
		Statement stmt = null;
		String query = "select DISTINCT ins.numero_poliza,cu.nombre,cu.apellidos,cu.dni_cif,cu.telefono,ins.prima_neta as prima_neta,(SELECT MV2.DESCRIPCION FROM ib_master_values mv2 WHERE ins.compania = MV2.VALOR) AS compania,(SELECT MV2.DESCRIPCION FROM ib_master_values mv2 WHERE ins.tipo_riesgo = MV2.VALOR) AS tipo_riesgo,(SELECT MV3.DESCRIPCION FROM ib_master_values mv3 WHERE ins.estado = MV3.VALOR) as estado,(select ty.descripcion from ib_customer_type ty where re.id_tipo = ty.idib_customer_type)as tipo "
				+ " from ib_customer_type type,ib_customer cu, ib_insurance ins, ib_customer_relation re, ib_master_values mv "
				+ " where " + "cu.idib_customer = re.id_cliente  "
				+ " and re.id_tipo = type.idib_customer_type and re.id_seguro = ins.idib_insurance ";

		if (null != tfDni.getText() && !tfDni.getText().isEmpty()) {
			query += (" and cu.dni_cif = '" + tfDni.getText() + "'");
		}

		if (null != tfPoliza.getText() && !tfPoliza.getText().isEmpty()) {
			query += (" and ins.numero_poliza = '" + tfPoliza.getText() + "'");
		}

		if (null != tfTelefono.getText() && !tfTelefono.getText().isEmpty()) {
			query += (" and cu.telefono= '" + tfTelefono.getText() + "'");
		}

		if (null != cbTipoSeguro.getValue() && !cbTipoSeguro.getValue().toString().isEmpty()) {
			query += (" and mv.valor = ins.estado and mv.descripcion = '" + cbTipoSeguro.getValue().toString() + "'");
		}

		if (null != cbTipoUsuario.getValue() && !cbTipoUsuario.getValue().toString().isEmpty()) {
			query += (" and type.descripcion = '" + cbTipoUsuario.getValue().toString() + "'");
		}

		if (null != cbTipoRiesgo.getValue() && !cbTipoRiesgo.getValue().toString().isEmpty()) {
			query += (" and mv.valor = ins.tipo_riesgo and mv.descripcion = '" + cbTipoRiesgo.getValue().toString()
					+ "'");
		}

		if (null != tfNombre.getText() && !tfNombre.getText().toString().isEmpty()) {
			query += (" and cu.nombre = '" + tfNombre.getText().toString() + "'");
		}

		if (null != tfApellidos.getText() && !tfApellidos.getText().toString().isEmpty()) {
			query += (" and cu.apellidos = '" + tfApellidos.getText().toString() + "'");
		}

		List<TableInfo> lti = new ArrayList<TableInfo>();
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				ti = new TableInfo();
				ti.setNumeroPoliza(rs.getString("numero_poliza"));
				ti.setNombre(rs.getString("nombre"));
				ti.setApellidos(rs.getString("apellidos"));
				ti.setDni(rs.getString("dni_cif"));
				ti.setTelefono(rs.getString("telefono"));
				ti.setTipoRiesgo(rs.getString("tipo_riesgo"));
				ti.setEstado(rs.getString("estado"));
				ti.setTipo(rs.getString("tipo"));
				ti.setPrimaNeta(rs.getString("prima_Neta"));
				ti.setCompania(rs.getString("compania"));
				lti.add(ti);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return lti;
	}

	// Event Listener on MenuItem[#mClienteGestion].onAction
	@FXML
	public void handleClienteGestion(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PersonOverview.fxml"));
		Parent root;
		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle("Gestion de clientes.");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(root, 800, 600));
			stage.setScene(stage.getScene());
			PersonOverviewController controller = (PersonOverviewController) loader.getController();
			controller.initData(null, false, false);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Event Listener on MenuItem[#mSeguroGestion].onAction
	@FXML
	public void handleSeguroGestion(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/InsureMenuOverview.fxml"));
		Parent root;
		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle("Gestion de Seguros.");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(root, 1300, 420));
			stage.setScene(stage.getScene());
			// InsureMenuOverviewController controller =
			// (InsureMenuOverviewController) loader.getController();
			// controller.initData(null, false, false);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Event Listener on MenuItem[#mCuotasGestion].onAction
	@FXML
	public void handleCuotasGestion(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CuotesManagementMenuOverview.fxml"));
		Parent root;
		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle("Gestión de pagos de Cuotas.");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(root, 887, 648));
			stage.setScene(stage.getScene());
			// InsureMenuOverviewController controller =
			// (InsureMenuOverviewController) loader.getController();
			// controller.initData(null, false, false);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Event Listener on MenuItem[#mAyudaDocumentacion].onAction
	@FXML
	public void handleAyudaDocumentacion(ActionEvent event) {
		// TODO Autogenerated
	}

	@FXML
	public void handleRelacionCliente(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PersonManagementOverview.fxml"));
		Parent root;
		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle("Gestión Cliente-Poliza");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(root, 941, 775));
			stage.setScene(stage.getScene());
			// InsureMenuOverviewController controller =
			// (InsureMenuOverviewController) loader.getController();
			// controller.initData(null, false, false);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
