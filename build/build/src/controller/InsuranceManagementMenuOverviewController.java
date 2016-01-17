package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.IbAccountBank;
import model.IbCustomer;
import model.IbInsurance;
import model.IbInsuranceDetail;
import model.IbMasterValue;
import model.MasterTypes;
import util.ChangeListener;
import util.DateUtil;
import util.MasterValueUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
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

import javafx.scene.control.Label;

import javafx.scene.control.ComboBox;

import javafx.scene.control.Tab;

import javafx.scene.control.CheckBox;

import javafx.scene.control.DatePicker;

public class InsuranceManagementMenuOverviewController {
	@FXML
	private Tab tabDatosSeguro;
	@FXML
	private Label lbTitular;
	@FXML
	private Label lbPoliza;
	@FXML
	private ComboBox cbTipoRiesgo;
	@FXML
	private TextField tfComision;
	@FXML
	private TextField tfLiquidez;
	@FXML
	private ComboBox cbCompania;
	@FXML
	private ComboBox cbEstado;
	@FXML
	private Tab tabDetalleSeguro;
	@FXML
	private TextField tfMarca;
	@FXML
	private TextField tfModelo;
	@FXML
	private TextField tfMatricula;
	@FXML
	private TextField tfCaballos;
	@FXML
	private TextField tfCilindrada;
	@FXML
	private TextField tfPMA;
	@FXML
	private TextField tfRemolque;
	@FXML
	private TextField tfAccesorios;
	@FXML
	private DatePicker dpFechaMatricula;
	@FXML
	private CheckBox chkPublico;
	@FXML
	private ComboBox cbCobertura;
	@FXML
	private ComboBox cbVehiculo;
	@FXML
	private Button btnAnadirTipoVehiculo;
	@FXML
	private Button btnAnadirCobertura;
	@FXML
	private Button btActualizar;
	@FXML
	private Button handleBorrar;
	private IbInsurance seguro;
	private IbAccountBank bank;
	private IbInsuranceDetail detalleSeguro;
	@FXML
	private Tab tabDatosBancarios;
	@FXML
	private TextField tfEntidad;
	@FXML
	private TextField tfOficina;
	@FXML
	private TextField tfDC;
	@FXML
	private TextField tfCuenta;
	@FXML
	private ComboBox cbBanco;
	private IbCustomer customer;

	// Event Listener on Button[#btnAnadirTipoVehiculo].onAction
	@FXML
	public void handleAnadirTipVehiculo(ActionEvent event) {
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MasterValuesOverview.fxml"));

		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle("Información maestro de valores.");
			MasterValuesOverviewController controller = loader.<MasterValuesOverviewController> getController();
			stage.setScene(new Scene(root, 750, 480));
			stage.setScene(stage.getScene());
			controller.initData(MasterTypes.TYPE_VEHICULO, null);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	// Event Listener on Button[#btnAnadirCobertura].onAction
	@FXML
	public void handleAnadirCobertura(ActionEvent event) {
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MasterValuesOverview.fxml"));

		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle("Información maestro de valores.");
			MasterValuesOverviewController controller = loader.<MasterValuesOverviewController> getController();
			stage.setScene(new Scene(root, 750, 480));
			stage.setScene(stage.getScene());
			controller.initData(MasterTypes.TYPE_COBERTURA, null);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Event Listener on Button[#btActualizar].onAction
	@FXML
	public void handleActualizar(ActionEvent event) {
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		em.getTransaction().begin();
		String message = actualizarInsurance();
		message = actualizarBank();

		if (!tabDetalleSeguro.isDisable()) {
			message += actualizarInsuranceDetail();
		}
		if (message.isEmpty() && seguro.getEstado().equals(MasterTypes.DESCRIPTION_ESTADO_VIGENTE)) {
			em.merge(seguro);
			em.merge(customer.getIbAccountBank2());
			if (!tabDetalleSeguro.isDisable()) {
				em.merge(detalleSeguro);
			}
			Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN", "Se ha actualizado el seguro.");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
		} else {
			if(message.isEmpty()){
				message = "La poliza no está en estado vigente.";
			}
			Dialog dialog = new Dialog(DialogType.ERROR, "INFORMACIÓN", message);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
		}
		em.getTransaction().commit();
	}

	private String actualizarBank() {
		String message = "";
		String banco = "";
		if (tfEntidad.getText().length() == 4 && tfOficina.getText().length() == 4 && tfDC.getText().length() == 2
				&& tfCuenta.getText().length() == 10 && cbBanco.getSelectionModel().getSelectedItem() != null) {
			banco = cbBanco.getSelectionModel().getSelectedItem().toString();
			customer.getIbAccountBank2().setEntidad(tfEntidad.getText());
			customer.getIbAccountBank2().setOficina(tfOficina.getText());
			customer.getIbAccountBank2().setDc(tfDC.getText());
			customer.getIbAccountBank2().setNumeroCuenta(tfCuenta.getText());

			IbMasterValue imv = MasterValueUtil.getMasterValueByValorAndTipo(banco, MasterTypes.TYPE_BANCO);
			customer.getIbAccountBank2().setBanco(imv.getValor());

		} else {
			message = "El número de cuenta no es correcto.";
		}

		return message;
	}

	private String actualizarInsurance() {
		String message = "";
		double comision = 0D;
		double liquidez = 0D;
		String sComision = "";
		String sLiquidez = "";

		if (null == tfComision.getText() || tfComision.getText().isEmpty()) {
			sComision = "0.0";
		} else {
			sComision = tfComision.getText();
		}

		if (DateUtil.isNumeric(sComision)) {
			comision = Double.parseDouble(sComision);
			seguro.setComision(comision);
		} else {
			message = "Comisión es un número no válido.";
		}

		if (null == tfLiquidez.getText() || tfLiquidez.getText().isEmpty()) {
			sLiquidez = "0.0";
		} else {
			sLiquidez = tfLiquidez.getText();
		}

		if (DateUtil.isNumeric(sLiquidez)) {
			liquidez = Double.parseDouble(sLiquidez);
			seguro.setLiquidez(liquidez);
		} else {
			message = "\n Liquidez es un número no válido.";
		}

		String tipoRiesgo = cbTipoRiesgo.getSelectionModel().getSelectedItem().toString();
		String compania = cbCompania.getSelectionModel().getSelectedItem().toString();
		String estado = cbEstado.getSelectionModel().getSelectedItem().toString();

		IbMasterValue imv = MasterValueUtil.getMasterValueByValorAndTipo(tipoRiesgo, MasterTypes.TYPE_RIESGO);
		seguro.setTipoRiesgo(imv.getValor());

		imv = MasterValueUtil.getMasterValueByValorAndTipo(compania, MasterTypes.TYPE_COMPANIA);
		seguro.setCompania(imv.getValor());

		imv = MasterValueUtil.getMasterValueByValorAndTipo(estado, MasterTypes.TYPE_ESTADO);
		seguro.setEstado(imv.getValor());

		return message;
	}

	private String actualizarInsuranceDetail() {
		String message = "";
		String tipoVehiculo = cbVehiculo.getSelectionModel().getSelectedItem().toString();
		String cobertura = cbCobertura.getSelectionModel().getSelectedItem().toString();
		String sPMA = "";
		IbMasterValue imv = MasterValueUtil.getMasterValueByValorAndTipo(tipoVehiculo, MasterTypes.TYPE_VEHICULO);
		detalleSeguro.setTipoVehiculo(imv.getValor());
		imv = MasterValueUtil.getMasterValueByValorAndTipo(cobertura, MasterTypes.TYPE_COBERTURA);
		detalleSeguro.setCobertura(imv.getValor());
		detalleSeguro.setMarca(tfMarca.getText());
		detalleSeguro.setModelo(tfModelo.getText());
		detalleSeguro.setMatricula(tfMatricula.getText());
		Date date = DateUtil.LocalDateToDate(dpFechaMatricula.getValue());
		long lDate = date.getTime();
		detalleSeguro.setFechaPrimeraMatricula(new Timestamp(lDate));
		detalleSeguro.setCv(tfCaballos.getText());
		detalleSeguro.setCc(tfCilindrada.getText());
		int pma = 0;

		if (null == tfPMA.getText() || tfPMA.getText().isEmpty()) {
			sPMA = "0";
		} else {
			sPMA = tfPMA.getText();
		}

		if (DateUtil.isNumeric(sPMA)) {
			pma = Integer.parseInt(sPMA);
			detalleSeguro.setPmaKgs(pma);
		} else {
			message = "\n PMA es un número no válido.";
		}
		detalleSeguro.setAccesorios(tfAccesorios.getText());
		detalleSeguro.setRemolque(tfRemolque.getText());

		byte particularPublico = 0;
		if (chkPublico.isSelected()) {
			particularPublico = 1;
		}
		detalleSeguro.setParticularPublico(particularPublico);

		return message;

	}

	public void initData(IbInsurance insurance, IbCustomer customer) {
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		this.seguro = insurance;
		this.customer = customer;

		lbTitular.setText(customer.getNombre() + " " + customer.getApellidos());
		lbPoliza.setText(insurance.getNumeroPoliza());

		// combo compania
		TypedQuery<String> query = em.createNamedQuery("IbMasterValue.findByType", String.class);
		query.setParameter("type", MasterTypes.TYPE_COMPANIA);
		List<String> listCompania = query.getResultList();
		ObservableList<String> listaObservableCompania = FXCollections.observableArrayList(listCompania);
		cbCompania.setItems(listaObservableCompania);
		String vCompania = MasterValueUtil.getMasterFindDescriptionByValor(insurance.getCompania());
		cbCompania.setValue(vCompania);

		// combo tipo de riesgo
		IbMasterValue imv = MasterValueUtil.getMasterFindObejctByValor(insurance.getTipoRiesgo());
		String conductor = imv.getDescripcion2();
		query = em.createNamedQuery("IbMasterValue.findByTypeDescConductor", String.class);
		query.setParameter("type", MasterTypes.TYPE_RIESGO);
		query.setParameter("desc2", conductor);
		List<String> listRiesgo = query.getResultList();

		// query = em.createNamedQuery("IbMasterValue.findByType",
		// String.class);
		// query.setParameter("type", MasterTypes.TYPE_RIESGO);
		// List<String> listRiesgo = query.getResultList();
		ObservableList<String> listaObservableRiesgo = FXCollections.observableArrayList(listRiesgo);
		cbTipoRiesgo.setItems(listaObservableRiesgo);
		// String vRiesgo =
		// MasterValueUtil.getMasterFindDescriptionByValor(insurance.getTipoRiesgo());
		cbTipoRiesgo.setValue(imv.getDescripcion());

		// combo estado
		query = em.createNamedQuery("IbMasterValue.findByType", String.class);
		query.setParameter("type", MasterTypes.TYPE_ESTADO);
		List<String> listEstado = query.getResultList();
		ObservableList<String> listaObservableEstado = FXCollections.observableArrayList(listEstado);
		cbEstado.setItems(listaObservableEstado);
		String vEstado = MasterValueUtil.getMasterFindDescriptionByValor(insurance.getEstado());
		cbEstado.setValue(vEstado);

		tfLiquidez.setText(String.valueOf(insurance.getLiquidez()));
		tfComision.setText(String.valueOf(insurance.getComision()));

		// obtenemos el detalle

		List<IbInsuranceDetail> lid = null;
		TypedQuery<IbInsuranceDetail> query2 = em.createNamedQuery("IbInsuranceDetail.findBySeguro",
				IbInsuranceDetail.class);
		query2.setParameter("idseguro", seguro.getIdibInsurance());
		lid = query2.getResultList();

		if (null != lid && lid.size() > 0) {
			IbInsuranceDetail ind = lid.get(0);
			this.detalleSeguro = ind;
			tabDetalleSeguro.setDisable(false);
			// combo cobertura
			query = em.createNamedQuery("IbMasterValue.findByType", String.class);
			query.setParameter("type", MasterTypes.TYPE_COBERTURA);
			List<String> listFormaPago = query.getResultList();
			ObservableList<String> listaObservableFormaPago = FXCollections.observableArrayList(listFormaPago);
			cbCobertura.setItems(listaObservableFormaPago);
			String vCobertura = MasterValueUtil.getMasterFindDescriptionByValor(ind.getCobertura());
			cbCobertura.setValue(vCobertura);

			// combo tipo de vehiculo
			query = em.createNamedQuery("IbMasterValue.findByType", String.class);
			query.setParameter("type", MasterTypes.TYPE_VEHICULO);
			List<String> listTipoVehiculo = query.getResultList();
			ObservableList<String> listaObservableTipoVehiculo = FXCollections.observableArrayList(listTipoVehiculo);
			cbVehiculo.setItems(listaObservableTipoVehiculo);
			String vTipoVehiculo = MasterValueUtil.getMasterFindDescriptionByValor(ind.getTipoVehiculo());
			cbVehiculo.setValue(vTipoVehiculo);

			// demás campos
			tfMarca.setText(ind.getMarca());
			tfModelo.setText(ind.getModelo());
			tfMatricula.setText(ind.getMatricula());
			dpFechaMatricula.setValue(DateUtil.dateToLocalDate(ind.getFechaPrimeraMatricula()));
			tfCaballos.setText(ind.getCv());
			tfCilindrada.setText(ind.getCc());
			if (ind.getPmaKgs() != 0) {
				tfPMA.setText(String.valueOf(ind.getPmaKgs()));
			}
			tfRemolque.setText(ind.getRemolque());
			tfAccesorios.setText(ind.getAccesorios());
			if (ind.getParticularPublico() == 1) {
				chkPublico.setSelected(true);
			}
		}

		// combo banco
		rellenarBancos();
		// Sacamos el customer para saber el banco
		IbAccountBank bank = customer.getIbAccountBank2();
		if (null != bank && null != bank.getEntidad() && null != bank.getDc() && null != bank.getEntidad()
				&& null != bank.getNumeroCuenta() && null != bank.getOficina()) {
			if (bank.getEntidad().length() == 4 && bank.getOficina().length() == 4 && bank.getDc().length() == 2
					&& bank.getNumeroCuenta().length() == 10) {
				tfEntidad.setText(bank.getEntidad());
				tfOficina.setText(bank.getOficina());
				tfDC.setText(bank.getDc());
				tfCuenta.setText(bank.getNumeroCuenta());

				String banco = MasterValueUtil.getMasterFindDescriptionByValor(bank.getBanco());
				cbBanco.setValue(banco);
			}
		}

	}

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
}
