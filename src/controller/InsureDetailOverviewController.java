package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.IbInsuranceDetail;
import model.IbMasterValue;
import model.MasterTypes;
import util.DateUtil;
import util.EntityManagerUtil;
import util.InsureCompleteVO;
import util.MasterValueUtil;

public class InsureDetailOverviewController {
	private InsureCompleteVO icVO = null;

	@FXML
	private Button btnAnadirTipoVehiculo;
	@FXML
	private Button btnAnadirCobertura;
	@FXML
	private CheckBox chkPublico;
	@FXML
	private ComboBox cbVehiculo;

	public ComboBox getcbVehiculo() {
		return cbVehiculo;
	}

	@FXML
	private TextField tfFranquicia;
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

	public DatePicker getdpFechaMatricula() {
		return dpFechaMatricula;
	}

	@FXML
	private ComboBox cbCobertura;

	public ComboBox getcbCobertura() {
		return cbCobertura;
	}

	@FXML
	public void handleAnadirCobertura(ActionEvent event) {
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MasterValuesOverview.fxml"));

		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle("Informaci�n maestro de valores.");
			MasterValuesOverviewController controller = loader.<MasterValuesOverviewController> getController();
			stage.setScene(new Scene(root, 750, 480));
			stage.setScene(stage.getScene());
			controller.initData(MasterTypes.TYPE_COBERTURA, icVO);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(((Node) event.getSource()).getScene().getWindow());
			stage.showAndWait();
			EntityManager em = EntityManagerUtil.getEntityManager();
			TypedQuery<String> query = em.createNamedQuery("IbMasterValue.findByType", String.class);
			query.setParameter("type", MasterTypes.TYPE_COBERTURA);
			List<String> listCobertura = query.getResultList();
			ObservableList<String> listaObservableCobertura = FXCollections.observableArrayList(listCobertura);
			getcbCobertura().setItems(listaObservableCobertura);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void handleChangeCobertura(ActionEvent event) {
		String cob = cbCobertura.getSelectionModel().getSelectedItem().toString();
		IbMasterValue ib = MasterValueUtil.getMasterValueByValorAndTipo(cob, MasterTypes.TYPE_COBERTURA);
		if (MasterTypes.CODIGO_TERCEROS_TODO_RIESGO_FRANQUICIA.equals(ib.getValor())) {
			tfFranquicia.setEditable(true);
		} else {
			tfFranquicia.setText("");
			tfFranquicia.setEditable(false);
		}
	}

	@FXML
	public void handleAnadirTipVehiculo(ActionEvent event) {
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MasterValuesOverview.fxml"));

		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle("Informaci�n maestro de valores.");
			MasterValuesOverviewController controller = loader.<MasterValuesOverviewController> getController();
			stage.setScene(new Scene(root, 750, 480));
			stage.setScene(stage.getScene());
			controller.initData(MasterTypes.TYPE_VEHICULO, icVO);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(((Node) event.getSource()).getScene().getWindow());
			stage.showAndWait();
			EntityManager em = EntityManagerUtil.getEntityManager();
			TypedQuery<String> query = em.createNamedQuery("IbMasterValue.findByType", String.class);
			query.setParameter("type", MasterTypes.TYPE_VEHICULO);
			List<String> listTipoVehiculo = query.getResultList();
			ObservableList<String> listaObservableTipoVehiculo = FXCollections.observableArrayList(listTipoVehiculo);
			getcbVehiculo().setItems(listaObservableTipoVehiculo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void handleSiguiente(ActionEvent event) {
		String cobertura = "";
		double franquicia = 0D;
		String cob = cbCobertura.getSelectionModel().getSelectedItem().toString();
		IbMasterValue ib = MasterValueUtil.getMasterValueByValorAndTipo(cob, MasterTypes.TYPE_COBERTURA);
		
		if (null != cbCobertura.getSelectionModel().getSelectedItem()
				&& null != cbVehiculo.getSelectionModel().getSelectedItem() && null != dpFechaMatricula.getValue()
				&& !tfMarca.getText().isEmpty() && !tfModelo.getText().isEmpty() && !tfMatricula.getText().isEmpty()) {
			if (tfFranquicia.getText().isEmpty() || DateUtil.isNumeric(tfFranquicia.getText())) {

				if (tfPMA.getText().isEmpty() || DateUtil.isNumeric(tfPMA.getText())) {

					if (!cbCobertura.getSelectionModel().getSelectedItem().toString().isEmpty()) {
						IbMasterValue imv = util.MasterValueUtil
								.getMasterValueByValor(cbCobertura.getSelectionModel().getSelectedItem().toString());
						cobertura = imv.getValor();
					}

					IbInsuranceDetail detail = new IbInsuranceDetail();
					detail.setAccesorios(tfAccesorios.getText());
					detail.setCc(tfCilindrada.getText());
					detail.setCobertura(cobertura);

					detail.setCv(tfCaballos.getText());

					Date fechaPrimeraMatricula = Date
							.from(dpFechaMatricula.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
					long time = fechaPrimeraMatricula.getTime();
					detail.setFechaPrimeraMatricula(new Timestamp(time));
					detail.setMarca(tfMarca.getText());
					detail.setModelo(tfModelo.getText());
					detail.setMatricula(tfMatricula.getText());
					byte particular = 0;
					if (chkPublico.isSelected()) {
						particular = 1;
					}
					detail.setParticularPublico(particular);
					int pma = 0;
					if (null != tfPMA.getText() && !tfPMA.getText().isEmpty()) {
						pma = Integer.parseInt(tfPMA.getText());
					}
					detail.setPmaKgs(pma);
					String tipoVehiculo = "";
					// INDTV00
					if (!cbVehiculo.getSelectionModel().getSelectedItem().toString().isEmpty()) {
						IbMasterValue imv = util.MasterValueUtil
								.getMasterValueByValor(cbVehiculo.getSelectionModel().getSelectedItem().toString());
						tipoVehiculo = imv.getValor();
					}
					detail.setTipoVehiculo(tipoVehiculo);
					detail.setRemolque(tfRemolque.getText());

					if (MasterTypes.CODIGO_TERCEROS_TODO_RIESGO_FRANQUICIA.equals(ib.getValor())) {
						franquicia = Double.parseDouble(tfFranquicia.getText());
					} else {
						franquicia = 0D;
					}
					detail.setFranquicia(franquicia);
					// icVO.getDatosSeguro().setIbInsuranceDetail(detail);
					icVO.setDatosDetalleSeguro(detail);

					Parent root;
					Stage stage = new Stage();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BankAccountOverview.fxml"));
					try {
						root = (Parent) loader.load();
						stage.setTitle("Datos Cuenta Bancaria.");
						stage.initModality(Modality.APPLICATION_MODAL);
						stage.setScene(new Scene(root, 550, 370));
						stage.setScene(stage.getScene());
						BankAccountOverviewController controller = (BankAccountOverviewController) loader
								.getController();
						controller.initData(icVO);
						stage.show();
						((Node) (event.getSource())).getScene().getWindow().hide();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Dialog dialog = new Dialog(DialogType.ERROR, "ERROR",
							"PMA (peso m�ximo autorizado)no es un valor num�rico.");
					dialog.initModality(Modality.WINDOW_MODAL);
					dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
					dialog.showAndWait();
				}
			}else{
				Dialog dialog = new Dialog(DialogType.ERROR, "ERROR",
						"El campo fraquicia no es un valor num�rico.");
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
				dialog.showAndWait();
			}
		} else {
			Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACI�N",
					"Exiten alg�n dato que no est� completo en el formulario.");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
		}

	}

	public void initData(InsureCompleteVO icVO) {
		this.icVO = icVO;
		// lbVehiculo.setText(icVO.getTipoSeguro());
		EntityManager em = EntityManagerUtil.getEntityManager();
		TypedQuery<String> query = em.createNamedQuery("IbMasterValue.findByType", String.class);
		query.setParameter("type", MasterTypes.TYPE_COBERTURA);
		List<String> listCobertura = query.getResultList();
		ObservableList<String> listaObservableCobertura = FXCollections.observableArrayList(listCobertura);
		getcbCobertura().setItems(listaObservableCobertura);

		query = em.createNamedQuery("IbMasterValue.findByType", String.class);
		query.setParameter("type", MasterTypes.TYPE_VEHICULO);
		List<String> listTipoVehiculo = query.getResultList();
		ObservableList<String> listaObservableTipoVehiculo = FXCollections.observableArrayList(listTipoVehiculo);
		getcbVehiculo().setItems(listaObservableTipoVehiculo);
		
		Date input = new Date();
		LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		getdpFechaMatricula().setValue(date);
	}

}
