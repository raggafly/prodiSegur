package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.crypto.spec.IvParameterSpec;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.github.daytron.simpledialogfx.dialog.DialogType;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CustomersTypes;
import model.IbAccountBank;
import model.IbCuotesInsure;
import model.IbCustomer;
import model.IbCustomerRelation;
import model.IbCustomerType;
import model.IbMasterValue;
import model.MasterTypes;
import model.TableInfo;
import model.TableInfoRelation;
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

	private boolean saveBank = true;

	// Event Listener on TextField[#tfEntidad].onKeyPressed
	private int maxLengthEntidad = 4;
	private int maxLengthOficina = 3;
	private int maxLengthDC = 2;

	public void addTextLimiter(final TextField tf, final int maxLength, final TextField tfNext) {
	
	    tf.textProperty().addListener(new ChangeListener(tf, maxLength) {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (tf.getText().length() > maxLength) {
	                String s = tf.getText().substring(0, maxLength);
	                tf.setText(s);
	                tfNext.requestFocus();
	            }
	        }
	    });
	
	}
	
	@FXML
	public void keyPressedEntidad(KeyEvent event) {
	   addTextLimiter(tfEntidad, maxLengthEntidad, tfOficina);
	}

	// Event Listener on TextField[#tfOficina].onKeyPressed
	@FXML
	public void keyPressedOficina(KeyEvent event) {
		addTextLimiter(tfOficina, maxLengthEntidad, tfDC);
//      forma vieja		
//		tfOficina.textProperty().addListener(new ChangeListener(tfOficina, 4));
//		if (tfOficina.getText().length() > maxLengthOficina) {
//			tfDC.requestFocus();
//
//		}
	}

	// Event Listener on TextField[#tfDC].onKeyPressed
	@FXML
	public void keyPressedDC(KeyEvent event) {
		addTextLimiter(tfDC, maxLengthDC, tfCuenta);
//		tfDC.textProperty().addListener(new ChangeListener(tfDC, 2));
//		if (tfDC.getText().length() > maxLengthDC) {
//			tfCuenta.requestFocus();
//		}
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
		cbBanco.setDisable(false);
		rellenarBancos();
	}

	@FXML
	public void handleGuardar(ActionEvent event) {
		if (tfEntidad.getText().length() == 4 && tfOficina.getText().length() == 4 && tfDC.getText().length() == 2
				&& tfCuenta.getText().length() == 10) {
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
		} else {
			Dialog dialog = new Dialog(DialogType.ERROR, "INFORMACIÓN", "El número de cuenta no es correcto.");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
		}
	}

	// Event Listener on Button[#btFinalizar].onAction
	@FXML
	public void handleFinalizar(ActionEvent event) {
		// metemos en icVO los datos bancarios
		if (saveBank) {
			setDatosBancarios();
		}
		// guarda
		saveInsureComplete(event);
		((Node) (event.getSource())).getScene().getWindow().hide();
	}

	public void setDatosBancarios() {
		IbAccountBank datosBancarios = new IbAccountBank();
		if (null != cbBanco.getSelectionModel().getSelectedItem()) {
			String ab = (String) cbBanco.getSelectionModel().getSelectedItem();
			IbMasterValue mv = util.MasterValueUtil.getMasterValueByValor(ab);
			datosBancarios.setBanco(mv.getValor());
			// datosBancarios.setBanco(cbBanco.getSelectionModel().getSelectedItem().toString());
		}
		datosBancarios.setEntidad(tfEntidad.getText());
		datosBancarios.setOficina(tfOficina.getText());
		datosBancarios.setDc(tfDC.getText());
		datosBancarios.setNumeroCuenta(tfCuenta.getText());

		icVO.setDatosBancarios(datosBancarios);
		if (null != icVO.getDatosCliente() && null != icVO.getDatosCliente().getIbAccountBank2()){
		icVO.getDatosCliente().setIbAccountBank2(datosBancarios);
		}else{
//			datosBancarios.setIbCustomer(icVO.getDatosCliente());
			icVO.getDatosCliente().setIbAccountBank2(datosBancarios);
//			datosBancarios.addIbCustomer(icVO.getDatosCliente());
			icVO.setDatosBancarios(datosBancarios);
		}
	}

	private void saveInsureComplete(ActionEvent event) {

		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		// IbAccountBank iab = icVO.getDatosBancarios();
		List<IbCustomerRelation> listaRelacionCliente;
		tx.begin();

		// em.persist(icVO.getDatosBancarios());
		// em.persist(iab);
		if (saveBank) {
			em.persist(icVO.getDatosBancarios());
			em.flush();
			em.merge(icVO.getDatosCliente());
			em.flush();
		}else{
			em.merge(icVO.getDatosCliente().getIbAccountBank2());
			em.flush();
		}
		listaRelacionCliente = getRelation();
		icVO.getDatosCliente().setIbCustomerRelations(listaRelacionCliente);

		List<CustomersTypes> lisct = icVO.getListaCustomersType();

		for (int i = 0; i < lisct.size(); i++) {
			if (lisct.get(i).isInsertar()) {
				em.persist(lisct.get(i).getIbCustomer());
			}
		}

		em.persist(icVO.getDatosSeguro());
		em.flush();
		if (null != icVO.getDatosDetalleSeguro()) {
			icVO.getDatosDetalleSeguro().setIdSeguro(icVO.getDatosSeguro().getIdibInsurance());
			em.persist(icVO.getDatosDetalleSeguro());
			em.flush();
		}
		for (int i = 0; i < listaRelacionCliente.size(); i++) {
			IbCustomerRelation cr = listaRelacionCliente.get(i);
			em.persist(cr);
			em.flush();
		}

		for (int i = 0; i < icVO.getListCuotesInsure().size(); i++) {
			IbCuotesInsure ici = icVO.getListCuotesInsure().get(i);
			ici.setIbInsurance(icVO.getDatosSeguro());
			em.persist(ici);
			em.flush();
		}
		tx.commit();
		/*
		 * Iniciamos la pantalla para ver el detalle del seguro.
		 */
		if (null != icVO.getDatosSeguro().getNumeroPoliza() && !icVO.getDatosSeguro().getNumeroPoliza().isEmpty()) {
			try {
				TableInfo tableInfo = new TableInfo();
				tableInfo.setDni(icVO.getDniTitular());
				tableInfo.setNumeroPoliza(icVO.getDatosSeguro().getNumeroPoliza());
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/detalleSeguroLayout.fxml"));
				Parent root;
				root = (Parent) loader.load();
				Stage stage = new Stage();
				stage.setTitle("Detalle de Poliza");
				FXMLParentLayoutController controller = loader.<FXMLParentLayoutController> getController();

				stage.setScene(new Scene(root, 600, 600));

				stage.setScene(stage.getScene());
				try {
					controller.initData(tableInfo.getNumeroPoliza());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stage.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Dialog dialog = new Dialog(DialogType.ERROR, "ERROR",
						"El proceso ha fallado a la hora de guardar los datos. Puede ser que la que haya algún problema con la base de datos.");
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(((Node)event.getSource()).getScene().getWindow());
				dialog.showAndWait();
			}
		}
	}

	private List<IbCustomerRelation> getRelation() {
		// TODO Auto-generated method stub
		List<IbCustomerRelation> licr = new ArrayList<IbCustomerRelation>();
		// lo viejo
		// Iterator itr = icVO.getDatosClienteRelation().iterator();
		// while (itr.hasNext()) {
		// IbCustomerRelation icr = new IbCustomerRelation();
		// TableInfoRelation element = (TableInfoRelation) itr.next();
		// IbCustomerType ctype = new IbCustomerType();
		//
		// icr.setIbInsurance(icVO.getDatosSeguro());
		// icr.setIbCustomer(icVO.getDatosCliente());
		// ctype = getCodeByDescription(element.getTipo());
		// icr.setIbCustomerType1(ctype);
		//
		// if (icr != null) {
		// licr.add(icr);
		// }
		// }
		// fin de lo viejo

		List<CustomersTypes> listct = icVO.getListaCustomersType();
		for (int i = 0; i < listct.size(); i++) {
			IbCustomerRelation icr = new IbCustomerRelation();
			IbCustomerType ctype = new IbCustomerType();
			icr.setIbInsurance(icVO.getDatosSeguro());
			icr.setIbCustomer(listct.get(i).getIbCustomer());
			ctype = getCodeByDescription(listct.get(i).getTipo());

			icr.setIbCustomerType(ctype);
			if (icr != null) {
				licr.add(icr);
			}
		}
		return licr;
	}

	private IbCustomerType getCodeByDescription(String desc) {
		// TODO Auto-generated method stub
		int code = 0;
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		if (desc.equals("TITULAR")) {
			desc = "PROPIETARIO";
		}
		TypedQuery<IbCustomerType> query = em.createNamedQuery("IbCustomerType.findByDescription",
				IbCustomerType.class);
		query.setParameter("desc", desc);
		List<IbCustomerType> listRelation = query.getResultList();
		IbCustomerType icb = null;
		if (listRelation.size() > 0) {
			icb = listRelation.get(0);
			// code = icb.getIdibCustomerRelation();
		}
		return icb;
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

		if (null != icVO.getDatosCliente() && null != icVO.getDatosCliente().getIbAccountBank2()) {
			IbAccountBank acb = icVO.getDatosCliente().getIbAccountBank2();
			banco = acb.getBanco();
			entidad = acb.getEntidad();
			oficina = acb.getOficina();
			dc = acb.getDc();
			cuenta = acb.getNumeroCuenta();

			// cbBanco.setValue(banco);
			tfEntidad.setText(String.valueOf(entidad));
			tfOficina.setText(String.valueOf(oficina));
			tfDC.setText(String.valueOf(dc));
			tfCuenta.setText(String.valueOf(cuenta));

			tfEntidad.setEditable(false);
			tfOficina.setEditable(false);
			tfDC.setEditable(false);
			tfCuenta.setEditable(false);
			ObservableList<String> listaObservableBancos = rellenarBancoCliente(banco);
			cbBanco.setValue(listaObservableBancos);

			cbBanco.setDisable(true);
			saveBank = false;
		} else {
			btEditar.setVisible(false);
			rellenarBancos();
		}

	}

	public void rellenarBancos() {
		String tipo = "BANKA00";
		// actualizar combo
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		TypedQuery<String> query = em.createNamedQuery("IbMasterValue.findByType", String.class);
		query.setParameter("type", tipo);
		List<String> listBancos = query.getResultList();

		ObservableList<String> listaObservableBancos = FXCollections.observableArrayList(listBancos);
		cbBanco.setItems(listaObservableBancos);

	}

	public ObservableList<String> rellenarBancoCliente(String codBanco) {
		// actualizar combo
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		TypedQuery<String> query = em.createNamedQuery("IbMasterValue.findByValue", String.class);
		query.setParameter("valor", codBanco);
		List<String> listBancos = query.getResultList();
		ObservableList<String> listaObservableBancos = FXCollections.observableArrayList(listBancos);
		return listaObservableBancos;
	}
	
	@FXML
	public void handleAnadirBanco(ActionEvent event) {
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MasterValuesOverview.fxml"));

		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle("Información maestro de valores.");
			MasterValuesOverviewController controller = loader.<MasterValuesOverviewController> getController();
			stage.setScene(new Scene(root, 750, 480));
			stage.setScene(stage.getScene());
			controller.initData(MasterTypes.TYPE_BANCO, null);
			stage.showAndWait();
			rellenarBancos();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
