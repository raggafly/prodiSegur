package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;

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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.IbCustomer;
import model.IbCustomerRelation;
import model.CustomersTypes;
import model.TableInfoRelation;
import util.DateUtil;
import util.InsureCompleteVO;

public class PersonOverviewController {
	InsureCompleteVO icVO = new InsureCompleteVO();
	int contTiposClientes = 3;
	@FXML
	private TextField filterField;
	@FXML
	private TableView<IbCustomer> personTable;

	@FXML
	private TableView<TableInfoRelation> personRelationTable;

	@FXML
	private TableColumn<TableInfoRelation, String> nombreRelationColumn;

	@FXML
	private TableColumn<TableInfoRelation, String> apellidosRelationColumn;

	@FXML
	private TableColumn<TableInfoRelation, String> dniRelationColumn;

	@FXML
	private TableColumn<TableInfoRelation, String> tipoRelationColumn;

	@FXML
	private CheckBox cbTitular;

	@FXML
	private CheckBox cbTomador;

	@FXML
	private CheckBox cbConductor;

	private ObservableList<IbCustomer> datosCliente = FXCollections.observableArrayList();

	private ObservableList<IbCustomer> filteredData = FXCollections.observableArrayList();

	private ObservableList<TableInfoRelation> datosClienteRelation = FXCollections.observableArrayList();

	public TableView getTablaClientes() {
		return personTable;
	}

	@FXML
	private TableColumn<IbCustomer, String> firstNameColumn;

	Stage primaryStage;

	public TableColumn getColumnaNombre() {
		return firstNameColumn;
	}

	@FXML
	private TableColumn<IbCustomer, String> lastNameColumn;

	public TableColumn getColumnaApellidos() {
		return lastNameColumn;
	}

	@FXML
	private Label firstNameLabel;

	public Label getfirstNameLabel() {
		return firstNameLabel;
	}

	@FXML
	private Label lastNameLabel;
	@FXML
	private Label streetLabel;
	@FXML
	private Label postalCodeLabel;
	@FXML
	private Label cityLabel;
	@FXML
	private Label birthdayLabel;
	@FXML
	private Label DNILabel;	
	@FXML
	private Button btAnadir;
	@FXML
	private Button btBorrarRelation;
	@FXML
	private Button btNuevo;
	@FXML
	private Button btEditar;
	@FXML
	private Button btBorrar;
	@FXML
	private Button btSiguiente;

	// Reference to the main application.

	public String tipoSeguro;

	List<CustomersTypes> listaCustomerComplete = new ArrayList<CustomersTypes>();
	
	

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public PersonOverviewController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
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

		} else {
			// Person is null, remove all the text.
			firstNameLabel.setText("");
			lastNameLabel.setText("");
			streetLabel.setText("");
			postalCodeLabel.setText("");
			cityLabel.setText("");
			birthdayLabel.setText("");
			DNILabel.setText("");
		}
	}

	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleDeletePerson(Event event) {
		int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		if (selectedIndex >= 0) {
			EntityTransaction tx = em.getTransaction();

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

	/**
	 * Called when the user clicks the new button. Opens a dialog to edit
	 * details for a new person.
	 */
	@FXML
	private void handleNewPerson() {
		IbCustomer tempPerson = new IbCustomer();

		boolean okClicked = showPersonEditDialog(tempPerson, false);
//		if (okClicked) {
//			getPersonData().add(icVO.getDatosCliente());
//			personTable.setItems(getPersonData());
//		}
		// initData(tipoSeguro);
	}

	public ObservableList<IbCustomer> getPersonData() {
		return datosCliente;
	}

	public ObservableList<TableInfoRelation> getPersonRelationData() {
		return datosClienteRelation;
	}

	@SuppressWarnings("static-access")
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

	/**
	 * Called when the user clicks the edit button. Opens a dialog to edit
	 * details for the selected person.
	 */

	@FXML
	private void handleEditPerson(Event event) {

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

	@FXML
	private void handleSiguiente(ActionEvent event) {
		if (personRelationTable.getItems().size() == contTiposClientes) {
			Parent root;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/InsureOverview.fxml"));

			try {
				root = (Parent) loader.load();
				Stage stage = new Stage();

				stage.setTitle("Alta nueva Poliza.");
				stage.setScene(new Scene(root, 1157, 823));
				stage.setScene(stage.getScene());
				InsureOverviewController controller = (InsureOverviewController) loader.getController();

				icVO.setTipoSeguro(tipoSeguro);
				icVO.setDatosClienteRelation(datosClienteRelation);
				icVO.setListaCustomersType(listaCustomerComplete);

				controller.initData(icVO);
				stage.show();
				((Node) (event.getSource())).getScene().getWindow().hide();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN",
					"Para pasar al siguiente paso se deben añadir los datos necesarios en la tabla relación cliente.");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
		}

	}

	// private List<TableInfoRelation> getDataTableRelation() {
	// // TODO Auto-generated method stub
	// personRelationTable.getItems();
	// return null;
	// }

	@FXML
	private void handleAnadir(Event event) {
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

	@FXML
	private void handleBorrar() {
		TableInfoRelation tir = personRelationTable.getSelectionModel().getSelectedItem();
		datosClienteRelation.remove(tir);

	}

	public void initData(String tipoSeguro, boolean isConductor, boolean isHandleAlta) {
		// TODO Auto-generated method stub
		if (isHandleAlta) {
			this.tipoSeguro = tipoSeguro;
			if (!isConductor) {
				contTiposClientes = 2;
				cbConductor.setVisible(false);
			}
		} else {
			btAnadir.setDisable(true);
			btBorrarRelation.setDisable(true);
			btSiguiente.setDisable(true);
			cbConductor.setDisable(true);
			cbTomador.setDisable(true);
			cbTitular.setDisable(true);
		}
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		Collection<IbCustomer> listClientes = findAllClientes(em);
		datosCliente = FXCollections.observableArrayList(listClientes);

		firstNameColumn.setCellValueFactory(new PropertyValueFactory<IbCustomer, String>("nombre"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<IbCustomer, String>("apellidos"));

		personTable.getColumns().clear();
		personTable.setItems(datosCliente);
		personTable.getColumns().addAll(firstNameColumn);
		personTable.getColumns().addAll(lastNameColumn);

	}
}