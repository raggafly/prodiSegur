package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.github.daytron.simpledialogfx.dialog.DialogType;

import algorit.algoNumber;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.IbCuotesInsure;
import model.IbCuotesTime;
import model.IbInsurance;
import model.IbMasterValue;
import model.MasterTypes;
import model.TableInfoRelation;
import util.CalculateCuotes;
import util.DateUtil;
import util.InsureCompleteVO;
import util.MasterValueUtil;

public class InsureOverviewController {

	private InsureCompleteVO icVO = null;
	@FXML
	private ComboBox cbCompania;
	@FXML
	private TableView<IbCuotesInsure> tbCuotes;

	public ComboBox getcbCompania() {
		return cbCompania;
	}

	@FXML
	private DatePicker dpFechaEntradaVigor;
	@FXML
	private DatePicker dpFechafecto;

	@FXML
	private TextField tvPoliza;
	@FXML
	private ComboBox cbFormaPago;

	public ComboBox getcbFormaPago() {
		return cbFormaPago;
	}

	// @FXML
	// private DatePicker dpFechaInicio;
	@FXML
	private DatePicker dpFechaFin;
	@FXML
	private TextField tvPrimaNeta;
	@FXML
	private TextField tvLiquidez;
	@FXML
	private TextField tvComision;
	@FXML
	private ComboBox cbDuracion;

	public ComboBox getcbDuracion() {
		return cbDuracion;
	}

	@FXML
	private Label lbCliente;

	@FXML
	private TableColumn<IbCuotesInsure, String> ColumnNumCuota;
	@FXML
	private TableColumn<IbCuotesInsure, String> ColumnFechaPagoCuota;
	@FXML
	private TableColumn<IbCuotesInsure, Byte> ColumnPagado;
	@FXML
	private TableColumn<IbCuotesInsure, String> ColumnFechaPago;
	@FXML
	private TableColumn<IbCuotesInsure, Double> ColumnTotal;
	@FXML
	private Label lbCuota;
	@FXML
	private DatePicker dpFechaPagoCuota;
	@FXML
	private DatePicker dpFechaPago;
	@FXML
	private CheckBox cbPagado;
	@FXML
	private TextField tfTotalCuota;
	@FXML
	private Button btModificar;

	// Event Listener on Button.onAction
	@FXML
	public void handleAnadirFormaPago(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleAnadirCompania(ActionEvent event) {
		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MasterValuesOverview.fxml"));

		try {
			root = (Parent) loader.load();
			Stage stage = new Stage();
			stage.setTitle("Informaci�n maestro de valores.");
			MasterValuesOverviewController controller = loader.<MasterValuesOverviewController> getController();
			stage.setScene(new Scene(root, 750, 480));
			stage.setScene(stage.getScene());
			controller.initData(MasterTypes.TYPE_COMPANIA, icVO);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void handleSiguiente(ActionEvent event) {
		boolean showNextWindow = true;
		String messageError = "";
		String duracion = "";
		if (null != cbDuracion.getSelectionModel().getSelectedItem()) {
			duracion = cbDuracion.getSelectionModel().getSelectedItem().toString();
		}
		LocalDate dateIniVigor = dpFechaEntradaVigor.getValue();
		LocalDate dateFinVigor = LocalDate.now(ZoneId.of(ZoneId.systemDefault().toString()));
		String formaDePago = "";
		String tipoDeRiesgo = "";

		if (null != cbFormaPago.getSelectionModel().getSelectedItem()) {
			formaDePago = cbFormaPago.getSelectionModel().getSelectedItem().toString();
		}

		if (null == getInsurance(tvPoliza.getText())) {
			if (DateUtil.isNumeric(tvPrimaNeta.getText()) && DateUtil.isNumeric(tvLiquidez.getText())) {
				if (tvComision.getText().isEmpty() || DateUtil.isNumeric(tvComision.getText())) {

					if (showNextWindow) {
						ObservableList<IbCuotesInsure> obsCuotesInsure = tbCuotes.getItems();

						if (!tvPoliza.getText().isEmpty() && cbCompania.getSelectionModel().getSelectedItem() != null
								&& cbDuracion.getSelectionModel().getSelectedItem() != null
								&& cbFormaPago.getSelectionModel().getSelectedItem() != null
								&& !tvPrimaNeta.getText().isEmpty()) {

							if (algoNumber.comprobarTotal(Double.parseDouble(tvPrimaNeta.getText()), obsCuotesInsure)) {
								// if
								// (algoNumber.comprobarFechas(dpFechaInicio.getValue(),
								// dpFechaFin.getValue(),
								// obsCuotesInsure,
								// cbFormaPago.getSelectionModel().getSelectedItem().toString()))
								// {

								try {
									IbInsurance datosSeguro = new IbInsurance();
									IbMasterValue imvCompania = MasterValueUtil.getMasterValueByValorAndTipo(
											cbCompania.getSelectionModel().getSelectedItem().toString(),
											MasterTypes.TYPE_COMPANIA);
									datosSeguro.setCompania(imvCompania.getValor());
									Date utilDateInicio = Date.from(dpFechaEntradaVigor.getValue()
											.atStartOfDay(ZoneId.systemDefault()).toInstant());
									Date utilFechaEfecto = Date.from(
											dpFechafecto.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
									Date utilDateFin = Date.from(
											dpFechaFin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
									Date utilDateFechaEntradaVigor = Date.from(dpFechaEntradaVigor.getValue()
											.atStartOfDay(ZoneId.systemDefault()).toInstant());
									datosSeguro.setFechaEntradaVigor(utilDateInicio);
									datosSeguro.setFechaInicio(utilDateInicio);
									datosSeguro.setFechaFin(utilDateFin);
									datosSeguro.setFechaEfecto(utilFechaEfecto);
									datosSeguro.setFechaEntradaVigor(utilDateFechaEntradaVigor);

									switch (duracion) {
									case "ANUAL":
										dateFinVigor = dateIniVigor.plusYears(1);
										break;
									case "SEMESTRAL":
										dateFinVigor = dateIniVigor.plusMonths(6);
										break;
									case "TRIMESTRAL":
										dateFinVigor = dateIniVigor.plusMonths(3);
										break;
									default:
										break;
									}

									Date utilDateFinVigor = Date.from(
											dpFechaFin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
									datosSeguro.setFechaFinEntradaVigor(utilDateFinVigor);
									// datosSeguro.setFechaFinEntradaVigor(
									// Date.from(dateFinVigor.atStartOfDay(ZoneId.systemDefault()).toInstant()));
									double primaNeta = 0;
									if (null != tvPrimaNeta.getText() && !tvPrimaNeta.getText().isEmpty()) {
										primaNeta = Double.parseDouble(tvPrimaNeta.getText().replace(",", "."));
									}
									datosSeguro.setPrimaNeta(primaNeta);
									double liquidez = 0;
									if (null != tvLiquidez.getText() && !tvLiquidez.getText().isEmpty()) {
										liquidez = Double.parseDouble(tvLiquidez.getText().replace(",", "."));
									}
									datosSeguro.setLiquidez(liquidez);
									double comision = 0;
									if (null != tvComision.getText() && !tvComision.getText().isEmpty()) {
										comision = Double.parseDouble(tvComision.getText().replace(",", "."));
									}
									datosSeguro.setComision(comision);

									if (!cbDuracion.getSelectionModel().getSelectedItem().toString().isEmpty()) {
										IbMasterValue imv = util.MasterValueUtil.getMasterValueByValorAndTipo(duracion,
												MasterTypes.TYPE_DURACION);
										duracion = imv.getValor();
									}
									datosSeguro.setDuracion(duracion);

									if (!formaDePago.isEmpty()) {
										IbMasterValue imv = util.MasterValueUtil
												.getMasterValueByValorAndTipo(formaDePago, MasterTypes.TYPE_FORMA_PAGO);
										formaDePago = imv.getValor();
									}
									datosSeguro.setFormaPago(formaDePago);

									IbMasterValue imv = util.MasterValueUtil
											.getMasterValueByValor(icVO.getTipoSeguro());
									tipoDeRiesgo = imv.getValor();
									datosSeguro.setTipoRiesgo(tipoDeRiesgo);

									// INEST01-->Vigente
									datosSeguro.setEstado(MasterTypes.DESCRIPTION_ESTADO_VIGENTE);
									// mostrar error al no insertar poliza
									datosSeguro.setNumeroPoliza(tvPoliza.getText());
									icVO.setDatosSeguro(datosSeguro);
									/*
									 * Guardamos lista de cuotas generadas
									 */
									List<IbCuotesInsure> listCuotesInsure = obsCuotesInsure;
									icVO.setListCuotesInsure(listCuotesInsure);
									/*
									 * Fin de guardar cuotas
									 */
									Parent root;
									Stage stage = new Stage();
									if (null == imv.getDescripcion2() || imv.getDescripcion2().isEmpty()) {
										FXMLLoader loader = new FXMLLoader(
												getClass().getResource("/views/BankAccountOverview.fxml"));
										root = (Parent) loader.load();

										stage.setTitle("Datos Cuenta Bancaria.");
										stage.initModality(Modality.APPLICATION_MODAL);
										stage.setScene(new Scene(root, 550, 370));
										stage.setScene(stage.getScene());
										BankAccountOverviewController controller = (BankAccountOverviewController) loader
												.getController();
										controller.initData(icVO);
									} else {
										FXMLLoader loader = new FXMLLoader(
												getClass().getResource("/views/InsureDetailOverview.fxml"));
										root = (Parent) loader.load();
										stage.setTitle("Detalle Seguro Veh�culo.");
										stage.initModality(Modality.APPLICATION_MODAL);
										stage.setScene(new Scene(root, 563, 540));
										stage.setScene(stage.getScene());
										InsureDetailOverviewController controller = (InsureDetailOverviewController) loader
												.getController();
										controller.initData(icVO);
									}

									stage.show();
									((Node) (event.getSource())).getScene().getWindow().hide();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// }
								// else {
								// messageError = "La fecha inicio y fin no
								// correspondan a las indicadas en los
								// par�metros de entrada.\n";
								// }
							} else {
								messageError = "Las sumas parciales de las cuotas no son iguales al total de la poliza.\n";
							}
						} else {
							messageError = "Existe alg�n campo que no est� completo. Rev�salos y vuelve a hacer click en siguiente.\n";
						}
					}
				} else {
					messageError = "La comisi�n no es un valor num�rico, solo pueden ser n�meros\n";
				}
			} else {
				messageError = "La prima neta o liquidez no es un valor num�rico\n";
			}
		} else {
			messageError = "La poliza indicada ya existe";
		}
		if (!messageError.isEmpty()) {
			Dialog dialog = new Dialog(DialogType.ERROR, "INFORMACI�N", messageError);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
		}
	}

	private boolean existsPoliza() {
		boolean exists = false;

		return exists;
	}

	@FXML
	public void handleFormaPagoCB(ActionEvent event) {
		if (comprobarComboBoxes()) {
			String formaPago = cbFormaPago.getSelectionModel().getSelectedItem().toString();
			String duracion = cbDuracion.getSelectionModel().getSelectedItem().toString();
			IbCuotesTime ict = CalculateCuotes.getNumberCuotes(duracion, formaPago);
			int numMesesXcuota = 0;
			numMesesXcuota = ict.getIntervaloMeses() * ict.getNumeroCuotas();
			LocalDate date = dpFechaEntradaVigor.getValue();
			if (!formaPago.isEmpty()) {

				date = date.plusMonths(numMesesXcuota);

				dpFechaFin.setValue(date);
			}

			tbCuotes.getItems().clear();
			addRows(tbCuotes, ict, dpFechaEntradaVigor.getValue(), event);
		}

	}

	@FXML
	public void handleDuracionCB(ActionEvent event) {

		if (comprobarComboBoxes()) {
			String formaPago = cbFormaPago.getSelectionModel().getSelectedItem().toString();
			String duracion = cbDuracion.getSelectionModel().getSelectedItem().toString();
			IbCuotesTime ict = CalculateCuotes.getNumberCuotes(duracion, formaPago);
			int numMesesXcuota = 0;
			numMesesXcuota = ict.getIntervaloMeses() * ict.getNumeroCuotas();
			LocalDate date = dpFechaEntradaVigor.getValue();
			if (!formaPago.isEmpty()) {

				date = date.plusMonths(numMesesXcuota);

				dpFechaFin.setValue(date);
			}

			tbCuotes.getItems().clear();
			addRows(tbCuotes, ict, dpFechaEntradaVigor.getValue(), event);
		}
	}

	private boolean comprobarComboBoxes() {
		boolean actualizar = true;
		if (null == cbFormaPago.getSelectionModel().getSelectedItem()
				|| null == cbDuracion.getSelectionModel().getSelectedItem()) {
			actualizar = false;
		}
		return actualizar;
	}

	private void addRows(TableView table, IbCuotesTime ict, LocalDate date, ActionEvent event) {
		ObservableList<IbCuotesInsure> allData = table.getItems();
		IbCuotesInsure dataRow = new IbCuotesInsure();

		int numCuotas = ict.getNumeroCuotas();
		int meses = ict.getIntervaloMeses();
		int numMesesXcuota = 0;
		LocalDate lcAux;
		byte pagado = 0;
		if (null != tvPrimaNeta && !tvPrimaNeta.getText().isEmpty() && DateUtil.isNumeric(tvPrimaNeta.getText())) {

			double total = Double.parseDouble(tvPrimaNeta.getText());

			List<Double> listaTotalCuotas = algoNumber.getFirstTotal(total, numCuotas);

			table.getColumns().clear();
			for (int i = 0; i < numCuotas; i++) {
				dataRow = new IbCuotesInsure();
				numMesesXcuota = (i) * meses;
				lcAux = date.plusMonths(numMesesXcuota);
				Date fechaOficialPago = Date.from(lcAux.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

				dataRow.setNumOrden(i + 1);
				dataRow.setFechaOficialPago(fechaOficialPago);
				dataRow.setPagado(pagado);
				dataRow.setFechaPagoCuota(fechaOficialPago);
				dataRow.setTotalCuota(listaTotalCuotas.get(i));
				allData.add(dataRow);
			}
			// CheckBox cbPagado = new CheckBox();
			ColumnNumCuota.setCellValueFactory(new PropertyValueFactory<IbCuotesInsure, String>("numOrden"));
			// ColumnFechaPagoCuota
			// .setCellValueFactory(new PropertyValueFactory<IbCuotesInsure,
			// String>("fechaOficialPago"));
			ColumnFechaPagoCuota.setCellValueFactory(
					new Callback<TableColumn.CellDataFeatures<IbCuotesInsure, String>, ObservableValue<String>>() {
						@Override
						public ObservableValue<String> call(
								TableColumn.CellDataFeatures<IbCuotesInsure, String> coutes) {
							SimpleStringProperty property = new SimpleStringProperty();
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							property.setValue(dateFormat.format(coutes.getValue().getFechaOficialPago()));
							return property;
						}
					});
			ColumnPagado.setSortable(false);
			ColumnPagado.setCellValueFactory(new PropertyValueFactory<IbCuotesInsure, Byte>("pagado"));

			// ColumnFechaPago.setCellValueFactory(new
			// PropertyValueFactory<IbCuotesInsure, Date>("fechaPagoCuota"));
			ColumnFechaPago.setCellValueFactory(
					new Callback<TableColumn.CellDataFeatures<IbCuotesInsure, String>, ObservableValue<String>>() {
						@Override
						public ObservableValue<String> call(
								TableColumn.CellDataFeatures<IbCuotesInsure, String> coutes) {
							SimpleStringProperty property = new SimpleStringProperty();
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							property.setValue(dateFormat.format(coutes.getValue().getFechaPagoCuota()));
							return property;
						}
					});
			ColumnTotal.setCellValueFactory(new PropertyValueFactory<IbCuotesInsure, Double>("totalCuota"));

			table.getColumns().addAll(ColumnNumCuota, ColumnFechaPagoCuota, ColumnPagado, ColumnFechaPago, ColumnTotal);

			ColumnPagado.setCellFactory(column -> {
				return new TableCell<IbCuotesInsure, Byte>() {
					@Override
					protected void updateItem(Byte item, boolean empty) {
						super.updateItem(item, empty);

						if (item != null || !empty) {
							CheckBox ck = new CheckBox();
							setAlignment(Pos.CENTER);
							setDisable(true);
							boolean checked = false;
							if (item == 1) {
								checked = true;
							}
							ck.setSelected(checked);
							setGraphic(ck);
						} else {

						}
					}
				};
			});

		} else {
			Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACI�N",
					"La prima neta introducida no es un valor num�rico. Recuerda los decimales se indican con un punto. Valor introducido: "
							+ tvPrimaNeta.getText());
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
		}
	}

	@SuppressWarnings("null")
	@FXML
	public void handleModificarCuota(ActionEvent event) {
		IbCuotesInsure selectedCuote = tbCuotes.getSelectionModel().getSelectedItem();
		if (DateUtil.isNumeric(tfTotalCuota.getText())) {
			List<IbCuotesInsure> list = new ArrayList<IbCuotesInsure>();

			ObservableList<IbCuotesInsure> obsAux = FXCollections.observableList(list);
			selectedCuote.setFechaOficialPago(DateUtil.LocalDateToDate(dpFechaPagoCuota.getValue()));
			selectedCuote.setFechaPagoCuota(DateUtil.LocalDateToDate(dpFechaPago.getValue()));
			selectedCuote.setTotalCuota(Double.parseDouble(tfTotalCuota.getText()));
			byte selected = 0;
			if (cbPagado.isSelected()) {
				selected = 1;
			}
			selectedCuote.setPagado(selected);
			ObservableList<IbCuotesInsure> obs = tbCuotes.getItems();
			for (int i = 0; i < obs.size(); i++) {
				IbCuotesInsure cuote = obs.get(i);
				if (cuote.getNumOrden() == selectedCuote.getNumOrden()) {
					cuote = selectedCuote;
				}
				obsAux.add(cuote);
			}
			tbCuotes.getItems().clear();
			tbCuotes.setItems(obsAux);
			tbCuotes.refresh();
		}
	}

	public void initData(InsureCompleteVO icVO) {
		Iterator itr = icVO.getDatosClienteRelation().iterator();
		String dni = "";
		while (itr.hasNext()) {
			TableInfoRelation element = (TableInfoRelation) itr.next();
			if (element.getTipo().equals("TOMADOR")) {
				String apellidos = "";
				if (element.getApellidos() != null) {
					apellidos = element.getApellidos();
				}
				lbCliente.setText(element.getNombre() + " " + apellidos);
				dni = element.getDni();
				icVO.setNombreCompletoTitular(lbCliente.getText());
				icVO.setDniTitular(dni);
			}
		}

		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		TypedQuery<String> query = em.createNamedQuery("IbMasterValue.findByType", String.class);
		query.setParameter("type", "INFPA00");
		List<String> listFormaPago = query.getResultList();
		ObservableList<String> listaObservableFormaPago = FXCollections.observableArrayList(listFormaPago);
		getcbFormaPago().setItems(listaObservableFormaPago);

		TypedQuery<String> queryDuracion = em.createNamedQuery("IbMasterValue.findByType", String.class);
		queryDuracion.setParameter("type", "INDUR00");
		List<String> listDuracion = queryDuracion.getResultList();
		ObservableList<String> listaObsevableDuracion = FXCollections.observableArrayList(listDuracion);
		getcbDuracion().setItems(listaObsevableDuracion);

		TypedQuery<String> queryCompania = em.createNamedQuery("IbMasterValue.findByType", String.class);
		queryCompania.setParameter("type", "INTCS00");
		List<String> listCompania = queryCompania.getResultList();
		ObservableList<String> listaObsevableCompania = FXCollections.observableArrayList(listCompania);
		getcbCompania().setItems(listaObsevableCompania);

		Date input = new Date();
		LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		dpFechafecto.setValue(date);
		dpFechaEntradaVigor.setValue(date);
		dpFechaFin.setValue(date.plusYears(1));

		tbCuotes.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showCuotesDetails(newValue));

		this.icVO = icVO;
	}

	private IbCuotesInsure showCuotesDetails(IbCuotesInsure cuoteSelected) {
		// TODO Auto-generated method stub
		if (cuoteSelected != null) {
			// Fill the labels with info from the person object.
			lbCuota.setText(String.valueOf(cuoteSelected.getNumOrden()));
			dpFechaPago.setValue(DateUtil.dateToLocalDate(cuoteSelected.getFechaPagoCuota()));
			dpFechaPagoCuota.setValue(DateUtil.dateToLocalDate(cuoteSelected.getFechaOficialPago()));
			// cbPagado.setDisable(false);
			boolean selected = false;
			if (cuoteSelected.getPagado() == 1) {
				selected = true;
			}
			cbPagado.setSelected(selected);
			tfTotalCuota.setText(String.valueOf(cuoteSelected.getTotalCuota()));

		} else {
			// Person is null, remove all the text.
			lbCuota.setText("0");
			cbPagado.setSelected(false);
			tfTotalCuota.setText("");
			dpFechaPagoCuota.setValue(null);
			dpFechaPago.setValue(null);
		}
		return cuoteSelected;
	}

	private IbInsurance getInsurance(String numeroPoliza) {
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		TypedQuery<IbInsurance> query = em.createNamedQuery("IbInsurance.findByPoliza", IbInsurance.class);
		query.setParameter("poliza", numeroPoliza);
		List<IbInsurance> listInsurance = query.getResultList();
		IbInsurance insurance = null;
		if (listInsurance.size() > 0) {
			insurance = listInsurance.get(0);
		}
		em.close();
		return insurance;
	}
}
