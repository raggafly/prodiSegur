package controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.controlsfx.dialog.Dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.IbCustomer;
import util.DateUtil;
import util.InsureCompleteVO;

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
	private DatePicker dpFechaCarnet;
	@FXML
	private TextField dnicifField;
	@FXML
	private TextField telephoneField;
	@FXML
	private TextField telephone2Field;
	@FXML
	private DatePicker dpFechaNacimiento;
	@FXML
	private TextField emailField;

    private Stage dialogStage;
    private IbCustomer person;
    private boolean okClicked = false;
    private boolean isEdit =false;
    private static InsureCompleteVO icVO = new InsureCompleteVO();

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
        Date input = new Date();
        LocalDate sysdate = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        firstNameField.setText(person.getNombre());
        lastNameField.setText(person.getApellidos());
        streetField.setText(person.getDireccion());
        townField.setText(person.getPoblacion());
        cityField.setText(person.getProvincia());
        postalCodeField.setText(person.getCodPostal());
        if (null != person.getFechaCarnet()){        	
        	LocalDate fechaCarnet =person.getFechaCarnet().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        	dpFechaCarnet.setValue(fechaCarnet);
        }else{        	
        	dpFechaCarnet.setValue(sysdate);
        }
        dnicifField.setText(person.getDniCif());
        telephoneField.setText(person.getTelefono());
        telephone2Field.setText(person.getTelefono2());
        if (null != person.getFechaNacimiento()){
        	LocalDate fechaNacimiento =person.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        	dpFechaNacimiento.setValue(fechaNacimiento);        	
        }else{
        	dpFechaNacimiento.setValue(sysdate);
        }
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

    public void setInsureComplete (InsureCompleteVO icVO){
    	this.icVO = icVO;
    }
    
    public static InsureCompleteVO getInsureComplete (){
    	return icVO;
    }
    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
        	Date input = new Date();
            LocalDate sysdate = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
            
            if(null!=dpFechaCarnet.getValue()){
            	Date fechaCarnet = Date.from(dpFechaCarnet.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            	person.setFechaCarnet(fechaCarnet);
            }else{
            	dpFechaCarnet.setValue(sysdate);
            }
            
            person.setDniCif(dnicifField.getText());
            person.setTelefono(telephoneField.getText());
            person.setTelefono2(telephone2Field.getText());
            
            if(null!=dpFechaNacimiento.getValue()){
            Date fechaNacimiento = Date.from(dpFechaNacimiento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());	
            person.setFechaNacimiento(fechaNacimiento);
            }else{
            	dpFechaNacimiento.setValue(sysdate);
            }
            person.setEmail(emailField.getText());
            
            tx.begin();
            if (isEdit){
    		em.find(IbCustomer.class, person.getIdibCustomer());
    		em.merge(person);    		
            }else{
            	em.persist(person);
//            	icVO.setDatosCliente(person);
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

//        if (fechaNacimientoField.getText() == null || fechaNacimientoField.getText().length() == 0) {
//            errorMessage += "No valid birthday!\n";
//        } else {
//            if (!DateUtil.validDate(fechaNacimientoField.getText())) {
//                errorMessage += "No valid birthday. Use the format dd.mm.yyyy!\n";
//            }
//        }

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