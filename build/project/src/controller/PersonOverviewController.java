package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.controlsfx.dialog.Dialogs;
import org.hibernate.SessionFactory;

import com.github.daytron.simpledialogfx.data.DialogResponse;
import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.github.daytron.simpledialogfx.dialog.DialogType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.IbCustomer;
import util.DateUtil;

public class PersonOverviewController {
	@FXML
	private TableView<IbCustomer> personTable;

	private ObservableList<IbCustomer> datosCliente = FXCollections.observableArrayList();

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

	// Reference to the main application.

	public String tipoSeguro;

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

		firstNameColumn.setCellValueFactory(new PropertyValueFactory<IbCustomer, String>("nombre"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<IbCustomer, String>("apellidos"));

		personTable.getColumns().clear();
		personTable.setItems(listaObservableCli);
		personTable.getColumns().addAll(firstNameColumn);
		personTable.getColumns().addAll(lastNameColumn);

		// Clear person details.
		showPersonDetails(null);

		// Listen for selection changes and show the person details when
		// changed.
		personTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
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
	private void handleDeletePerson() {
		
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
		IbCustomer clienteSeleccionado = personTable.getSelectionModel().getSelectedItem();
		if (selectedIndex >= 0) {
			Dialog dialog = new Dialog(DialogType.CONFIRMATION, "INFORMACIÓN",
					"¿Estás seguro qué desear eliminar el cliente "+ clienteSeleccionado.getNombre()+ " " +clienteSeleccionado.getApellidos()+"?");
			dialog.showAndWait();
			if (dialog.getResponse() == DialogResponse.YES) {
			    // Rest of the code
			
			personTable.getItems().remove(selectedIndex);
			IbCustomer cliente = em.find(IbCustomer.class, clienteSeleccionado.getIdibCustomer());
			em.getTransaction().begin();
			em.remove(cliente);
			em.getTransaction().commit();
			em.close();
			}
		} else {
			// Nothing selected.
			Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN",
					"Debes de seleccionar un cliente de la tabla para poder borrar.");
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
		if (okClicked) {
			getPersonData().add(tempPerson);
		}
		initData(tipoSeguro);
	}

	public ObservableList<IbCustomer> getPersonData() {
		return datosCliente;
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

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			initialize();
			showPersonDetails(cliente);
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
	private void handleEditPerson() {

		IbCustomer selectedPerson = personTable.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			boolean okClicked = showPersonEditDialog(selectedPerson, true);
			if (okClicked) {
				showPersonDetails(selectedPerson);
				initData(tipoSeguro);
			}

		} else {
			// Nothing selected.
			Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN",
					"Debes de seleccionar un cliente de la tabla para poder editar.");
			dialog.showAndWait();

		}
	}
	@FXML
	private void handleSiguiente() {
		
	}
	@FXML
	private void handleAnadir() {
		
	}
	@FXML
	private void handleBorrar() {
		
	}

	public void initData(String tipoSeguro) {
		// TODO Auto-generated method stub
		this.tipoSeguro = tipoSeguro;

		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		Collection<IbCustomer> listClientes = findAllClientes(em);
		ObservableList<IbCustomer> listaObservableCli = FXCollections.observableArrayList(listClientes);

		firstNameColumn.setCellValueFactory(new PropertyValueFactory<IbCustomer, String>("nombre"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<IbCustomer, String>("apellidos"));

		personTable.getColumns().clear();
		personTable.setItems(listaObservableCli);
		personTable.getColumns().addAll(firstNameColumn);
		personTable.getColumns().addAll(lastNameColumn);
	}
}