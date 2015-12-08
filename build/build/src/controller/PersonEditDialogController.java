package controller;

import java.time.LocalDate;
import java.time.ZoneId;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.controlsfx.dialog.Dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.IbCustomer;
import util.DateUtil;

/**
 * Dialog to edit details of a person.
 * 
 * @author Marco Jakob
 */
public class PersonEditDialogController {

	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField streetField;
	@FXML
	private TextField townField;
	@FXML
	private TextField cityField;
	@FXML
	private TextField postalCodeField;
	@FXML
	private TextField dateCardField;
	@FXML
	private TextField dnicifField;
	@FXML
	private TextField telephoneField;
	@FXML
	private TextField telephone2Field;
	@FXML
	private TextField fechaNacimientoField;
	@FXML
	private TextField emailField;

    private Stage dialogStage;
    private IbCustomer person;
    private boolean okClicked = false;
    private boolean isEdit =false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     * 
     * @param person
     */
    public void setPerson(IbCustomer person) {
        this.person = person;

        firstNameField.setText(person.getNombre());
        lastNameField.setText(person.getApellidos());
        streetField.setText(person.getDireccion());
        townField.setText(person.getPoblacion());
        cityField.setText(person.getProvincia());
        postalCodeField.setText(person.getCodPostal());
        if (null != person.getFechaCarnet()){
        	LocalDate fechaCarnet =person.getFechaCarnet().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        	dateCardField.setText(DateUtil.format(fechaCarnet));
        }else{        	
        	dateCardField.setText("");
        }
        dateCardField.setPromptText("dd.mm.yyyy");
        
        dnicifField.setText(person.getDniCif());
        telephoneField.setText(person.getTelefono());
        telephone2Field.setText(person.getTelefono2());
        if (null != person.getFechaNacimiento()){
        	LocalDate fechaNacimiento =person.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        	fechaNacimientoField.setText(DateUtil.format(fechaNacimiento));
        	
        }else{
        	fechaNacimientoField.setText("");
        }
        fechaNacimientoField.setPromptText("dd.mm.yyyy");
        
        emailField.setText(person.getEmail());
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
        	EntityManagerFactory emf;
    		EntityManager em;
    		emf = Persistence.createEntityManagerFactory("prodiSegur");
    		em = emf.createEntityManager();
    		EntityTransaction tx = em.getTransaction();
    		if (!isEdit){
    			this.person = new IbCustomer();
    		}
        	person.setNombre(firstNameField.getText());
            person.setApellidos(lastNameField.getText());
            person.setDireccion(streetField.getText());
            person.setPoblacion(townField.getText());
            person.setProvincia(cityField.getText());
            person.setCodPostal(postalCodeField.getText());
            
            if(null!=dateCardField.getText() && !dateCardField.getText().isEmpty()){
            	person.setFechaCarnet(DateUtil.String2utilDate(dateCardField.getText()));
            }else{
            	dateCardField.setText("");
            }
            
            person.setDniCif(dnicifField.getText());
            person.setTelefono(telephoneField.getText());
            person.setTelefono2(telephone2Field.getText());
            
            if(null!=fechaNacimientoField.getText() && !fechaNacimientoField.getText().isEmpty()){
            person.setFechaNacimiento(DateUtil.String2utilDate(fechaNacimientoField.getText()));
            }else{
            	fechaNacimientoField.setText("");
            }
            person.setEmail(emailField.getText());
            
            tx.begin();
            if (isEdit){
    		em.find(IbCustomer.class, person.getIdibCustomer());
    		em.merge(person);
            }else{
            	em.persist(person);
            }
    		tx.commit();
            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errorMessage += "No valid first name!\n"; 
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage += "No valid last name!\n"; 
        }
        if (streetField.getText() == null || streetField.getText().length() == 0) {
            errorMessage += "No valid street!\n"; 
        }

        if (postalCodeField.getText() == null || postalCodeField.getText().length() == 0) {
            errorMessage += "No valid postal code!\n"; 
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(postalCodeField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid postal code (must be an integer)!\n"; 
            }
        }

        if (cityField.getText() == null || cityField.getText().length() == 0) {
            errorMessage += "No valid city!\n"; 
        }

        if (fechaNacimientoField.getText() == null || fechaNacimientoField.getText().length() == 0) {
            errorMessage += "No valid birthday!\n";
        } else {
            if (!DateUtil.validDate(fechaNacimientoField.getText())) {
                errorMessage += "No valid birthday. Use the format dd.mm.yyyy!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
        	Dialogs.create()
		        .title("Invalid Fields")
		        .masthead("Please correct invalid fields")
		        .message(errorMessage)
		        .showError();
            return false;
        }
    }

	public void setIsEdit(boolean isEdit) {
		// TODO Auto-generated method stub
		this.isEdit = isEdit;
	}
}