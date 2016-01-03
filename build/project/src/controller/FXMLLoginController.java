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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.IbCustomer;
import model.MasterTypes;
import model.TableInfo;
import util.JDBCConnection;

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
	private TableView tablaBusqueda;

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
			List<String> listCustomerType = queryCustomerType.getResultList();
			root = FXMLLoader.load(getClass().getResource("/views/parentLayout.fxml"));

			Stage stage = new Stage();
			stage.setTitle("Pantalla Principal");
			stage.setScene(new Scene(root, 1500, 800));

			stage.show();
			ComboBox cbTipoSeguro = (ComboBox) ((Node) (stage.getScene().lookup("#cbTipoSeguro")));
			ComboBox cbTipoUsuario = (ComboBox) ((Node) (stage.getScene().lookup("#cbTipoUsuario")));

			ObservableList<String> listaObservable = FXCollections.observableArrayList(listEstados);
			ObservableList<String> listaObservableTipoUsuario = FXCollections.observableArrayList(listCustomerType);
			cbTipoSeguro.setItems(listaObservable);
			cbTipoUsuario.setItems(listaObservableTipoUsuario);
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
								System.out.println("Double clicked");
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

									stage.setScene(new Scene(root, 600, 600));

									stage.setScene(stage.getScene());
									try {
										controller.initData(tableInfo);
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
		query.setParameter("type", MasterTypes.COD_GENERICO_TIPO_RIESGO);
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
				stage.setScene(new Scene(root, 800, 600));
				stage.setScene(stage.getScene());
				PersonOverviewController controller = (PersonOverviewController) loader.getController();
				controller.initData(result.get(), isConductor);
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

		columnaNombre.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("nombre"));
		columnaApellidos.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("apellidos"));
		columnaDniCif.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("dni"));
		columnaTelefono.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("telefono"));
		columnaPoliza.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("numeroPoliza"));
		columnaTipoRiesgo.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("tipoRiesgo"));
		columnaEstado.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("estado"));
		columnaTipo.setCellValueFactory(new PropertyValueFactory<TableInfo, String>("tipo"));

		tb.setItems(listaObservable);
		tb.getColumns().addAll(columnaPoliza);
		tb.getColumns().addAll(columnaNombre);
		tb.getColumns().addAll(columnaApellidos);
		tb.getColumns().addAll(columnaDniCif);
		tb.getColumns().addAll(columnaTelefono);
		tb.getColumns().addAll(columnaTipoRiesgo);
		tb.getColumns().addAll(columnaEstado);
		tb.getColumns().addAll(columnaTipo);

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
		ComboBox cbTipoUsuario = (ComboBox<String>) ((Node) (event.getSource())).getScene().lookup("#cbTipoUsuario");
		TableInfo ti;
		Statement stmt = null;
		String query = "select ins.numero_poliza,cu.nombre,cu.apellidos,cu.dni_cif,cu.telefono,(SELECT MV2.DESCRIPCION FROM ib_master_values mv2 WHERE ins.tipo_riesgo = MV2.VALOR) AS tipo_riesgo,mv.descripcion as estado,(select ty.descripcion from ib_customer_type ty where re.id_tipo = ty.idib_customer_type)as tipo "
				+ " from ib_customer_type type,ib_customer cu, ib_insurance ins, ib_customer_relation re, ib_master_values mv "
				+ " where " + "cu.idib_customer = re.id_cliente and mv.valor = ins.estado "
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
			query += (" and mv.descripcion = '" + cbTipoSeguro.getValue().toString() + "'");
		}

		if (null != cbTipoUsuario.getValue() && !cbTipoUsuario.getValue().toString().isEmpty()) {
			query += (" and type.descripcion = '" + cbTipoUsuario.getValue().toString() + "'");
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

}
