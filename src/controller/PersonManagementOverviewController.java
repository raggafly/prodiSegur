package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CustomersTypes;
import model.IbCustomer;
import model.IbCustomerRelation;
import model.IbCustomerType;
import model.IbInsurance;
import model.IbMasterValue;
import model.TableInfoRelation;
import util.DateUtil;
import util.InsureCompleteVO;
import util.MasterValueUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.github.daytron.simpledialogfx.data.DialogResponse;
import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.github.daytron.simpledialogfx.dialog.DialogType;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.CheckBox;

import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class PersonManagementOverviewController {
	@FXML
	private TableView<IbCustomer> personTable;
	@FXML
	private TableColumn firstNameColumn;
	@FXML
	private TableColumn lastNameColumn;
	@FXML
	private TextField filterField;
	@FXML
	private Label firstNameLabel;
	@FXML
	private Label lastNameLabel;
	@FXML
	private Label streetLabel;
	@FXML
	private Label postalCodeLabel;
	@FXML
	private Label birthdayLabel;
	@FXML
	private Label cityLabel;
	@FXML
	private Label DNILabel;
	@FXML
	private Button btNuevo;
	@FXML
	private Button btEditar;
	@FXML
	private Button btBorrar;
	@FXML
	private Button btSiguiente;
	@FXML
	private CheckBox cbTitular;
	@FXML
	private CheckBox cbConductor;
	@FXML
	private CheckBox cbTomador;
	@FXML
	private TableView<TableInfoRelation> personRelationTable;
	@FXML
	private TableColumn nombreRelationColumn;
	@FXML
	private TableColumn apellidosRelationColumn;
	@FXML
	private TableColumn dniRelationColumn;
	@FXML
	private TableColumn tipoRelationColumn;
	@FXML
	private Button btAnadir;
	@FXML
	private Button btBorrarRelation;

	Stage primaryStage;

	@FXML
	private Label lbTelefono;
	@FXML
	private Label lbEmail;
	@FXML
	private Label tfFechaCarnet;
	
	InsureCompleteVO icVO = new InsureCompleteVO();
	int contTiposClientes = 2;
	private ObservableList<IbCustomer> datosCliente = FXCollections.observableArrayList();

	private ObservableList<IbCustomer> filteredData = FXCollections.observableArrayList();

	private ObservableList<TableInfoRelation> datosClienteRelation = FXCollections.observableArrayList();

	public ObservableList<TableInfoRelation> getPersonRelationData() {
		return datosClienteRelation;
	}

	@FXML
	private TextField tfNumeroPoliza;
	@FXML
	private TextField tfNumeroOrden;

	List<CustomersTypes> listaCustomerComplete = new ArrayList<CustomersTypes>();

	@FXML
	public void handleBuscar(ActionEvent event) {

		if (null != tfNumeroPoliza.getText() && !tfNumeroPoliza.getText().isEmpty() && null != tfNumeroOrden.getText()
				&& !tfNumeroOrden.getText().isEmpty()) {
			String errorMessage = "Sólo se puede filtrar por un campo.";
			Dialog dialog = new Dialog(DialogType.ERROR, "INFORMACIÓN", errorMessage);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
		} else {
			IbCustomer cus = new IbCustomer();
			IbInsurance seguro = new IbInsurance();
			if (null != tfNumeroPoliza.getText() && !tfNumeroPoliza.getText().isEmpty()) {
				seguro = MasterValueUtil.getInsurance(tfNumeroPoliza.getText());
				cus = MasterValueUtil.getRelationCustomerInsurance(seguro);
				icVO.setDatosCliente(cus);
				// loadData();
				cargarPersonTable();
			}
			if (null != tfNumeroOrden.getText() && !tfNumeroOrden.getText().isEmpty()
					&& DateUtil.isNumericInteger(tfNumeroOrden.getText())) {
				seguro = MasterValueUtil.getInsuranceById(tfNumeroOrden.getText());
				cus = MasterValueUtil.getRelationCustomerInsurance(seguro);

			}
			icVO.setDatosCliente(cus);
			// loadData();
			cargarPersonTable();
		}
	}

	private void cargarPersonTable() {
		datosClienteRelation.clear();
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		TypedQuery<IbCustomerRelation> query = em.createNamedQuery("IbCustomerRelation.findSeguro",
				IbCustomerRelation.class);
		IbInsurance seguro = null;
		if (null != tfNumeroPoliza.getText() && !tfNumeroPoliza.getText().isEmpty()) {
			seguro = MasterValueUtil.getInsurance(tfNumeroPoliza.getText());
			query.setParameter("idseguro", MasterValueUtil.getInsurance(tfNumeroPoliza.getText()));
		}else{
			if (DateUtil.isNumericInteger(tfNumeroOrden.getText())){
			seguro = MasterValueUtil.getInsuranceById(tfNumeroOrden.getText());
			}
		}
		query.setParameter("idseguro", seguro);
		List<IbCustomerRelation> listInsuranceRelation = query.getResultList();

		TableInfoRelation tempPersonRelation = new TableInfoRelation();
		IbCustomer cus = new IbCustomer();
		for (int i = 0; i < listInsuranceRelation.size(); i++) {
			cus = listInsuranceRelation.get(i).getIbCustomer();
			tempPersonRelation.setNombre(cus.getNombre());
			tempPersonRelation.setApellidos(cus.getApellidos());
			tempPersonRelation.setDni(cus.getDniCif());
			tempPersonRelation.setTipo(listInsuranceRelation.get(i).getIbCustomerType().getDescripcion());
			getPersonRelationData().add(tempPersonRelation);
			tempPersonRelation = new TableInfoRelation();
		}

		// personRelationTable.getItems().clear();

		nombreRelationColumn.setCellValueFactory(new PropertyValueFactory<TableInfoRelation, String>("nombre"));
		apellidosRelationColumn.setCellValueFactory(new PropertyValueFactory<TableInfoRelation, String>("apellidos"));
		dniRelationColumn.setCellValueFactory(new PropertyValueFactory<TableInfoRelation, String>("dni"));
		tipoRelationColumn.setCellValueFactory(new PropertyValueFactory<TableInfoRelation, String>("tipo"));

		personRelationTable.setItems(datosClienteRelation);
		personRelationTable.refresh();

		List<IbMasterValue> listTipoDescripcionRiesgo = null;
//		seguro = null;
//		if (null != tfNumeroPoliza.getText() && !tfNumeroPoliza.getText().isEmpty()) {
//			seguro =MasterValueUtil.getInsurance(tfNumeroPoliza.getText());
//		} else{		
//			
//			seguro =MasterValueUtil.getInsuranceById(tfNumeroOrden.getText());			
//		}
		if(seguro != null){
		TypedQuery<IbMasterValue> queryByValue = em.createNamedQuery("IbMasterValue.findByObjectValue",
				IbMasterValue.class);
		queryByValue.setParameter("valor", seguro.getTipoRiesgo());
		listTipoDescripcionRiesgo = queryByValue.getResultList();
		}
		
		if (null != listTipoDescripcionRiesgo && null != listTipoDescripcionRiesgo.get(0)
				&& listTipoDescripcionRiesgo.get(0).getDescripcion2().equals("CONDUCTOR")) {
			contTiposClientes = 3;
		} else {
			cbConductor.setDisable(true);
		}

		em.close();

	}

	// Event Listener on Button[#btNuevo].onAction
	@FXML
	public void handleNewPerson(ActionEvent event) {

		IbCustomer tempPerson = new IbCustomer();

		boolean okClicked = showPersonEditDialog(tempPerson, false);
		// if (okClicked) {
		// getPersonData().add(icVO.getDatosCliente());
		// personTable.setItems(getPersonData());
		// }
		// initData(tipoSeguro);
	}

	// Event Listener on Button[#btEditar].onAction
	@FXML
	public void handleEditPerson(ActionEvent event) {

		IbCustomer selectedPerson = personTable.getSelectionModel().getSelectedItem();
		boolean okClicked = false;
		if (selectedPerson != null) {
			if (selectedPerson.getIdibCustomer() != 0) {
				okClicked = showPersonEditDialog(selectedPerson, true);
			}
			if (okClicked) {
				showPersonDetails(icVO.getDatosCliente());
			}

		} else {
			// Nothing selected.
			Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN",
					"Debes de seleccionar un cliente de la tabla para poder editar.");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();

		}
	}

	// Event Listener on Button[#btBorrar].onAction
	@FXML
	public void handleDeletePerson(ActionEvent event) {
		int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		if (selectedIndex >= 0) {
			IbCustomer clienteSeleccionado = personTable.getSelectionModel().getSelectedItem();
			IbCustomer cliente = em.find(IbCustomer.class, clienteSeleccionado.getIdibCustomer());

			TypedQuery<IbCustomerRelation> query = em.createNamedQuery("IbCustomerRelation.findCliente",
					IbCustomerRelation.class);
			query.setParameter("idcliente", clienteSeleccionado);
			List<IbCustomerRelation> listClienteRelation = query.getResultList();

			if (listClienteRelation.size() == 0) {

				Dialog dialog = new Dialog(DialogType.CONFIRMATION, "INFORMACIÓN",
						"¿Estás seguro qué desear eliminar el cliente " + clienteSeleccionado.getNombre() + " "
								+ clienteSeleccionado.getApellidos() + "?");
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
				dialog.showAndWait();
				if (dialog.getResponse() == DialogResponse.YES) {
					// Rest of the code
					personTable.getItems().remove(selectedIndex);
					em.getTransaction().begin();
					em.remove(cliente);
					em.getTransaction().commit();
					em.close();
				}
			} else {
				Dialog dialog = new Dialog(DialogType.ERROR, "INFORMACIÓN",
						"Este cliente tiene seguros contratados, en la parte de alta de seguros no se permite este tipo de acciones.");
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
				dialog.showAndWait();
			}
		} else {
			// Nothing selected.
			Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN",
					"Debes de seleccionar un cliente de la tabla para poder borrar.");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
		}
	}

	// Event Listener on Button[#btSiguiente].onAction
	@FXML
	public void handleActualizar(ActionEvent event) {
		String messageInfo = "No se han detectado cambios para actualizar la poliza.";

		if (personRelationTable.getItems().size() == contTiposClientes) {
			IbInsurance seguro = MasterValueUtil.getInsurance(tfNumeroPoliza.getText());
			ObservableList<TableInfoRelation> obsPerson = personRelationTable.getItems();
			EntityManagerFactory emf;
			EntityManager em;
			emf = Persistence.createEntityManagerFactory("prodiSegur");
			em = emf.createEntityManager();
			TypedQuery<IbCustomerRelation> query = em.createNamedQuery("IbCustomerRelation.findSeguro",
					IbCustomerRelation.class);
			query.setParameter("idseguro", seguro);
			List<IbCustomerRelation> listInsuranceRelation = query.getResultList();
			IbCustomer cus = new IbCustomer();
			TableInfoRelation ti = new TableInfoRelation();
			List<IbCustomerRelation> listIbCustomerRelations = new ArrayList<IbCustomerRelation>();
			boolean change = false;
			for (int i = 0; i < obsPerson.size(); i++) {
				ti = obsPerson.get(i);
				TypedQuery<IbCustomerType> queryType = em.createNamedQuery("IbCustomerType.findByDescription",
						IbCustomerType.class);
				queryType.setParameter("desc", ti.getTipo());
				List<IbCustomerType> listType = queryType.getResultList();
				int tipo = listType.get(0).getIdibCustomerType();
				cus = MasterValueUtil.getCustomer(ti.getDni());
				IbCustomerRelation cr = new IbCustomerRelation();
				for (int j = 0; j < listInsuranceRelation.size(); j++) {
					cr = listInsuranceRelation.get(j);
					if (cr.getIbCustomerType().getIdibCustomerType() == tipo) {
						if (cus.getIdibCustomer() != cr.getIbCustomer().getIdibCustomer()) {
							change = true;
							cr.setIbCustomer(MasterValueUtil.getCustomer(cus.getDniCif()));
							// listIbCustomerRelations.add(cr);
							em.getTransaction().begin();
							em.merge(cr);
							// javax.persistence.Query queryUp =
							// em.createNativeQuery("UPDATE Ib_Customer_Relation
							// SET ID_CLIENTE ="+cus.getIdibCustomer()+" WHERE
							// ID_SEGURO
							// ="+cr.getIbInsurance().getIdibInsurance()+ " AND
							// ID_TIPO
							// ="+cr.getIbCustomerType().getIdibCustomerType());
							// queryUp.executeUpdate();
							em.getTransaction().commit();
							messageInfo = "Se ha a actualizado la relación de clientes.";
						}
						// else{
						// listIbCustomerRelations.add(cr);
						// }
					}
				}
			}
			// em.getTransaction().begin();
			// seguro.setIbCustomerRelations(listIbCustomerRelations);
			// em.merge(seguro);
			//// javax.persistence.Query queryUp = em.createNativeQuery("UPDATE
			// Ib_Customer_Relation SET ID_CLIENTE ="+cus.getIdibCustomer()+"
			// WHERE ID_SEGURO ="+cr.getIbInsurance().getIdibInsurance()+ " AND
			// ID_TIPO ="+cr.getIbCustomerType().getIdibCustomerType());
			//// queryUp.executeUpdate();
			// em.getTransaction().commit();
		} else {
			messageInfo = "Falta algún tipo por incluir en la relación.";
		}
		if (!messageInfo.isEmpty()) {
			Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN", messageInfo);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
		}
	}

	// Event Listener on Button[#btAnadir].onAction
	@FXML
	public void handleAnadir(ActionEvent event) {
		HashMap<String, Boolean> checksTipos = new HashMap<String, Boolean>();
		int cont = 0;
		boolean contieneTipo = false;

		List tipos = new ArrayList<String>();
		for (int i = 0; i < personRelationTable.getItems().size(); i++) {
			TableInfoRelation icr = personRelationTable.getItems().get(i);
			tipos.add(icr.getTipo());
		}

		if (cbTitular.isSelected()) {
			checksTipos.put(cbTitular.getText().toUpperCase(), true);
			if (tipos.contains(cbTitular.getText().toUpperCase())) {
				contieneTipo = true;
			}
			cont++;
		} else {
			checksTipos.put(cbTitular.getText().toUpperCase(), false);
		}
		if (cbTomador.isSelected()) {
			checksTipos.put(cbTomador.getText().toUpperCase(), true);
			if (tipos.contains(cbTomador.getText().toUpperCase())) {
				contieneTipo = true;
			}
			cont++;
		} else {
			checksTipos.put(cbTomador.getText().toUpperCase(), false);
		}
		if (cbConductor.isSelected()) {
			if (tipos.contains(cbConductor.getText().toUpperCase())) {
				contieneTipo = true;
			}
			checksTipos.put(cbConductor.getText().toUpperCase(), true);
			cont++;
		} else {
			checksTipos.put(cbConductor.getText().toUpperCase(), false);
		}
		if (!contieneTipo) {
			int countItems = datosClienteRelation.size();
			if (countItems < contTiposClientes && cont + countItems <= contTiposClientes) {

				TableInfoRelation tempPersonRelation = new TableInfoRelation();
				Iterator it = checksTipos.entrySet().iterator();
				while (it.hasNext()) {

					Map.Entry pair = (Map.Entry) it.next();
					System.out.println(pair.getKey() + " = " + pair.getValue());
					if (Boolean.parseBoolean(pair.getValue().toString())) {
						if (null != firstNameLabel.getText() && !firstNameLabel.getText().isEmpty()) {
							tempPersonRelation.setNombre(firstNameLabel.getText());
							tempPersonRelation.setApellidos(lastNameLabel.getText());
							tempPersonRelation.setDni(DNILabel.getText());
							tempPersonRelation.setTipo(pair.getKey().toString());
							getPersonRelationData().add(tempPersonRelation);
							IbCustomer icu = personTable.getSelectionModel().getSelectedItem();
							CustomersTypes ctypes = new CustomersTypes();
							ctypes.setIbCustomer(icu);
							ctypes.setTipo(pair.getKey().toString());
							if (0 == icu.getIdibCustomer()) {
								ctypes.setInsertar(true);
							} else {
								ctypes.setInsertar(false);
							}
							listaCustomerComplete.add(ctypes);

							if (pair.getKey().toString().equals("TITULAR")) {
								icVO.setDatosCliente(icu);
							}

							tempPersonRelation = new TableInfoRelation();
						} else {
							Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN",
									"No hay seleccionado ningún cliente.");
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
							dialog.showAndWait();
						}
					}

				}
				nombreRelationColumn.setCellValueFactory(new PropertyValueFactory<TableInfoRelation, String>("nombre"));
				apellidosRelationColumn
						.setCellValueFactory(new PropertyValueFactory<TableInfoRelation, String>("apellidos"));
				dniRelationColumn.setCellValueFactory(new PropertyValueFactory<TableInfoRelation, String>("dni"));
				tipoRelationColumn.setCellValueFactory(new PropertyValueFactory<TableInfoRelation, String>("tipo"));

				personRelationTable.setItems(datosClienteRelation);
			} else {
				Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN",
						"Por favor, revisa los datos de entrada para los clientes a utilizar.");
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
				dialog.showAndWait();
			}
		} else {
			Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN",
					"Se ha detectado que se está intentando duplicar el tipo de cliente para una misma poliza.");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
		}
	}

	// Event Listener on Button[#btBorrarRelation].onAction
	@FXML
	public void handleBorrar(ActionEvent event) {
		TableInfoRelation tir = personRelationTable.getSelectionModel().getSelectedItem();
		datosClienteRelation.remove(tir);
	}

	public boolean showPersonEditDialog(IbCustomer cliente, boolean isEdit) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PersonOverviewController.class.getResource("/views/PersonEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Person");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			PersonEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPerson(cliente);
			controller.setIsEdit(isEdit);
			controller.setInsureComplete(icVO);
			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();
			icVO = controller.getInsureComplete();
			initialize();
			// showPersonDetails(icVO.getDatosCliente());
			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@FXML
	private void initialize() {

		loadData();
	}

	private void loadData() {

		// Initialize the person table with the two columns.
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		Collection<IbCustomer> listClientes = findAllClientes(em);
		ObservableList<IbCustomer> listaObservableCli = FXCollections.observableArrayList(listClientes);
		if (null != icVO.getDatosCliente()) {
			listaObservableCli.add(icVO.getDatosCliente());
		}
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<IbCustomer, String>("nombre"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<IbCustomer, String>("apellidos"));

		personTable.getColumns().clear();
		personTable.setItems(listaObservableCli);
		personTable.getColumns().addAll(firstNameColumn);
		personTable.getColumns().addAll(lastNameColumn);

		datosCliente = listaObservableCli;
		filteredData.addAll(datosCliente);

		// Clear person details.
		showPersonDetails(null);

		// Listen for selection changes and show the person details when
		// changed.
		personTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));

		filterField.textProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				updateFilteredData();
			}
		});

	}

	private void updateFilteredData() {
		datosCliente.clear();

		for (IbCustomer p : filteredData) {
			if (matchesFilter(p)) {
				datosCliente.add(p);
			}
		}

		// Must re-sort table after items changed
		reapplyTableSortOrder();
	}

	private void showPersonDetails(IbCustomer cliente) {
		if (cliente != null) {
			// Fill the labels with info from the person object.
			firstNameLabel.setText(cliente.getNombre());
			lastNameLabel.setText(cliente.getApellidos());
			streetLabel.setText(cliente.getDireccion() + " (" + cliente.getPoblacion() + ")");
			postalCodeLabel.setText(cliente.getCodPostal());
			cityLabel.setText(cliente.getProvincia());
			if (null != cliente.getFechaNacimiento() && !cliente.getFechaNacimiento().toString().isEmpty()) {
				LocalDate fechaNacimiento = cliente.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDate();
				birthdayLabel.setText(DateUtil.format(fechaNacimiento));
			} else {
				birthdayLabel.setText("");
			}
			DNILabel.setText(cliente.getDniCif());
			tfFechaCarnet.setText(DateUtil.formatUtilDate(cliente.getFechaCarnet()));
			lbTelefono.setText(cliente.getTelefono());
			lbEmail.setText(cliente.getEmail());
		} else {
			// Person is null, remove all the text.
			firstNameLabel.setText("");
			lastNameLabel.setText("");
			streetLabel.setText("");
			postalCodeLabel.setText("");
			cityLabel.setText("");
			birthdayLabel.setText("");
			DNILabel.setText("");
			lbTelefono.setText("");
			lbEmail.setText("");
			tfFechaCarnet.setText("");
		}
	}

	private boolean matchesFilter(IbCustomer person) {
		String filterString = filterField.getText();
		if (filterString == null || filterString.isEmpty()) {
			return true;
		}

		String lowerCaseFilterString = filterString.toLowerCase();

		if (person.getNombre().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
			return true;
		} else if (person.getApellidos().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
			return true;
		}

		return false; // Does not match
	}

	private void reapplyTableSortOrder() {
		ArrayList<TableColumn<IbCustomer, ?>> sortOrder = new ArrayList<>(personTable.getSortOrder());
		personTable.getSortOrder().clear();
		personTable.getSortOrder().addAll(sortOrder);
	}

	public Collection<IbCustomer> findAllClientes(EntityManager em) {
		return (Collection<IbCustomer>) em.createNamedQuery("IbCustomer.findAll").getResultList();
	}
}
