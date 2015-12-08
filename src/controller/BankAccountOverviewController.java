package controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.github.daytron.simpledialogfx.dialog.DialogType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import model.IbAccountBank;
import model.IbCustomer;
import model.IbCustomerRelation;
import model.IbMasterValue;
import model.MasterTypes;
import util.ChangeListener;
import util.InsureCompleteVO;

public class BankAccountOverviewController {
	InsureCompleteVO icVO = null;

	@FXML
	private TextField tfEntidad;
	@FXML
	private TextField tfOficina;
	@FXML
	private TextField tfDC;
	@FXML
	private TextField tfCuenta;
	@FXML
	private Button btEditar;
	@FXML
	private Label lbNombreCompleto;
	@FXML
	private Label lbDni;
	@FXML
	private Button btFinalizar;
	@FXML
	private Button btGuardar;
	@FXML
	private ComboBox cbBanco;

	// Event Listener on TextField[#tfEntidad].onKeyPressed
	private int maxLengthEntidad = 3;
	private int maxLengthDC = 1;

	@FXML
	public void keyPressedEntidad(KeyEvent event) {
		tfEntidad.textProperty().addListener(new ChangeListener(tfEntidad, 4));
		if (tfEntidad.getText().length() > maxLengthEntidad) {
			tfOficina.requestFocus();

		}

	}

	// Event Listener on TextField[#tfOficina].onKeyPressed
	@FXML
	public void keyPressedOficina(KeyEvent event) {
		tfOficina.textProperty().addListener(new ChangeListener(tfOficina, 4));
		if (tfOficina.getText().length() > maxLengthEntidad) {
			tfDC.requestFocus();

		}
	}

	// Event Listener on TextField[#tfDC].onKeyPressed
	@FXML
	public void keyPressedDC(KeyEvent event) {

		tfDC.textProperty().addListener(new ChangeListener(tfDC, 2));
		if (tfDC.getText().length() > maxLengthDC) {
			tfCuenta.requestFocus();
		}
	}

	// Event Listener on TextField[#tfCuenta].onKeyPressed
	@FXML
	public void keyPressedCuenta(KeyEvent event) {
		tfCuenta.textProperty().addListener(new ChangeListener(tfCuenta, 10));

	}

	// Event Listener on Button[#btEditar].onAction
	@FXML
	public void handleEditar(ActionEvent event) {
		tfEntidad.setEditable(true);
		tfOficina.setEditable(true);
		tfDC.setEditable(true);
		tfCuenta.setEditable(true);

		btGuardar.setVisible(true);
		btEditar.setVisible(false);
		btFinalizar.setDisable(true);
	}
	
	@FXML
	public void handleGuardar(ActionEvent event) {
		if(tfEntidad.getText().length()==4 && tfOficina.getText().length()==4 && tfDC.getText().length()==2 && tfCuenta.getText().length()==10){
			icVO.getDatosCliente().getIbAccountBank2();
			
			icVO.getDatosCliente().getIbAccountBank2().setEntidad(tfEntidad.getText());
			icVO.getDatosCliente().getIbAccountBank2().setOficina(tfOficina.getText());
			icVO.getDatosCliente().getIbAccountBank2().setDc(tfDC.getText());
			icVO.getDatosCliente().getIbAccountBank2().setNumeroCuenta(tfCuenta.getText());
			
			btGuardar.setVisible(false);
			btEditar.setVisible(true);
			
			tfEntidad.setEditable(false);
			tfOficina.setEditable(false);
			tfDC.setEditable(false);
			tfCuenta.setEditable(false);
			btFinalizar.setDisable(false);
		}else{
			Dialog dialog = new Dialog(DialogType.ERROR, "INFORMACIÓN",
					"El número de cuenta no es correcto.");
			dialog.showAndWait();
		}
	}
		

	// Event Listener on Button[#btFinalizar].onAction
	@FXML
	public void handleFinalizar(ActionEvent event) {
		
		IbAccountBank datosBancarios = new IbAccountBank();
		if (null != cbBanco.getSelectionModel().getSelectedItem()) {
			IbMasterValue ab = (IbMasterValue) cbBanco.getSelectionModel().getSelectedItem();
			datosBancarios.setBanco(ab.getValor());
//			datosBancarios.setBanco(cbBanco.getSelectionModel().getSelectedItem().toString());
		}
		datosBancarios.setEntidad(tfEntidad.getText());
		datosBancarios.setOficina(tfOficina.getText());
		datosBancarios.setDc(tfDC.getText());
		datosBancarios.setNumeroCuenta(tfCuenta.getText());

		icVO.setDatosBancarios(datosBancarios);
		
		saveInsueComplete();
	}

	private void saveInsueComplete() {

		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		List <IbCustomerRelation> listaRelacionCliente;
		tx.begin();
		em.merge(icVO.getDatosCliente());		
    	tx.commit();
    	
    	
    	
	}

	public void initData(InsureCompleteVO icVO) {
		this.icVO = icVO;
		String banco = "";
		String entidad;
		String oficina;
		String dc;
		String cuenta;

		lbNombreCompleto.setText(icVO.getNombreCompletoTitular());
		lbDni.setText(icVO.getDniTitular());
		IbAccountBank acb = icVO.getDatosCliente().getIbAccountBank2();
		
		if (acb != null) {
			banco = acb.getBanco();
			entidad = acb.getEntidad();
			oficina = acb.getOficina();
			dc = acb.getDc();
			cuenta = acb.getNumeroCuenta();

			cbBanco.setValue(banco);
			tfEntidad.setText(String.valueOf(entidad));
			tfOficina.setText(String.valueOf(oficina));
			tfDC.setText(String.valueOf(dc));
			tfCuenta.setText(String.valueOf(cuenta));

			tfEntidad.setEditable(false);
			tfOficina.setEditable(false);
			tfDC.setEditable(false);
			tfCuenta.setEditable(false);

		} else {
			btEditar.setVisible(false);

		}
		String tipo = "BANKA00";
		//actualizar combo
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		TypedQuery<IbMasterValue> query = em.createNamedQuery("IbMasterValue.findByType", IbMasterValue.class);
		query.setParameter("type", tipo);
		List<IbMasterValue> listBancos = query.getResultList();

		ObservableList<IbMasterValue> listaObservableBancos = FXCollections.observableArrayList(listBancos);
		cbBanco.setItems(listaObservableBancos);

	}
}
