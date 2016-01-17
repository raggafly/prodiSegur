package controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.util.Callback;
import model.IbCuotesInsure;
import model.IbCuotesTime;
import model.IbInsurance;
import model.MasterTypes;
import util.DateUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.github.daytron.simpledialogfx.dialog.DialogType;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.CheckBox;

import javafx.scene.control.TableView;

import javafx.scene.control.DatePicker;

import javafx.scene.control.TableColumn;

public class CuotesManagementMenuOverviewController {
	@FXML
	private TextField tfPoliza;
	@FXML
	private Button btBuscar;
	@FXML
	private TableView<IbCuotesInsure> tbCuotes;
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
	private Button btActualizar;
	@FXML
	private Button btModificar;
	@FXML
	private DatePicker dpFechaPago;
	@FXML
	private CheckBox chkPagado;
	@FXML
	private Label lbOrden;

	IbInsurance datosSeguro = null;

	// Event Listener on Button[#btBuscar].onAction
	@FXML
	public void handleBuscar(ActionEvent event) {
		// String query = "select cu.* from insurance_broker.ib_cuotes_insure
		// cu, insurance_broker.ib_insurance ins where cu.id_seguro =
		// ins.idib_insurance and ins.numero_poliza = ";
		//
		// if (null != tfPoliza && !tfPoliza.getText().isEmpty()) {
		// query += (" and cu.dni_cif = '" + tfPoliza.getText() + "'");
		// }
		String message = "";

		if (null != tfPoliza && !tfPoliza.getText().isEmpty()) {
			datosSeguro = getInsurance(tfPoliza.getText());
		} else {
			datosSeguro = null;
		}

		if (null != datosSeguro) {
			// aquí rellenamos la tabla con las cuotas del seguro.
			addRowsIni(tbCuotes, datosSeguro);
		} else {
			message = "No existe la poliza solicitada.";
		}

		if (!message.isEmpty()) {
			Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN", message);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
		}

		tbCuotes.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showCuotesDetails(newValue));

	}

	// Event Listener on Button[#btActualizar].onAction
	@FXML
	public void handleActualizar(ActionEvent event) {
		String message = "";
		ObservableList<IbCuotesInsure> obsCuotesInsure = tbCuotes.getItems();
		List<IbCuotesInsure> listCuotesInsure = obsCuotesInsure;
		IbCuotesInsure ci = new IbCuotesInsure();
		EntityManagerFactory emf;
		EntityManager em;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();

		if (null != datosSeguro) {
			IbCuotesInsure ciOfficial = new IbCuotesInsure();
			TypedQuery<IbCuotesInsure> queryCuotes = em.createNamedQuery("IbCuotesInsure.findCoutesByInsure",
					IbCuotesInsure.class);
			queryCuotes.setParameter("seguro", datosSeguro);
			List<IbCuotesInsure> listOfficialCuotes = queryCuotes.getResultList();
			for (int i = 0; i < listCuotesInsure.size(); i++) {
				// recorremos todas las cuotas oficiales
				// comparando por la orden para saber si son
				// iguales

				ci = listCuotesInsure.get(i);
				for (int j = 0; j < listOfficialCuotes.size(); j++) {
					ciOfficial = listOfficialCuotes.get(j);
					// si son iguales actualizamos la cuota
					if (ci.getNumOrden() == ciOfficial.getNumOrden()) {
						// insertamos el id oficial para que a
						// la hora de hacer el merge
						// reconozca cual es.
						em.getTransaction().begin();
						ci.setIdibCuotesInsure(ciOfficial.getIdibCuotesInsure());
						ci.setIbInsurance(datosSeguro);
						em.merge(ci);
						em.getTransaction().commit();
					}
				}

			}
			message = "Se ha actualizado el seguro con la información establecida en las cuotas.";
		}

		// ESTADO FINALIZADO
		/*
		 * Realizamos las comprobaciones necesarias para ver si están todas las
		 * cuotas pagadas, si esto es así ponemos el estado del pago del seguro
		 * en finalizado.
		 */
		if (comprobarFinalizadoPagos()) {
			Dialog dialog = new Dialog(DialogType.GENERIC_OK, "INFORMACIÓN",
					"Se procede a dar por finalizado el pago del seguro, ya que todas sus cuotas están pagadas.");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
			dialog.showAndWait();
			actualizarSeguro(datosSeguro, MasterTypes.DESCRIPTION_ESTADO_FINALIZADO);
		} else {
			actualizarSeguro(datosSeguro, MasterTypes.DESCRIPTION_ESTADO_VIGENTE);
		}
		em.getTransaction().begin();
		em.merge(datosSeguro);
		em.getTransaction().commit();
		em.close();

		Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN", message);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
		dialog.showAndWait();
	}

	// Event Listener on Button[#btModificar].onAction
	@FXML
	public void handleModificarCuota(ActionEvent event) {
		IbCuotesInsure selectedCuote = tbCuotes.getSelectionModel().getSelectedItem();

		List<IbCuotesInsure> list = new ArrayList<IbCuotesInsure>();

		ObservableList<IbCuotesInsure> obsAux = FXCollections.observableList(list);
		selectedCuote.setFechaPagoCuota(DateUtil.LocalDateToDate(dpFechaPago.getValue()));
		byte selected = 0;
		if (chkPagado.isSelected()) {
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

	private void addRowsIni(TableView<IbCuotesInsure> table, IbInsurance seguro) {
		ObservableList<IbCuotesInsure> allData = FXCollections.observableArrayList(seguro.getIbCuotesInsures());
		table.getColumns().clear();
		table.setItems(allData);

		ColumnNumCuota.setCellValueFactory(new PropertyValueFactory<IbCuotesInsure, String>("numOrden"));

		ColumnFechaPagoCuota.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<IbCuotesInsure, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<IbCuotesInsure, String> coutes) {
						SimpleStringProperty property = new SimpleStringProperty();
						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						property.setValue(dateFormat.format(coutes.getValue().getFechaOficialPago()));
						return property;
					}
				});
		ColumnPagado.setCellValueFactory(new PropertyValueFactory<IbCuotesInsure, Byte>("pagado"));

		ColumnFechaPago.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<IbCuotesInsure, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<IbCuotesInsure, String> coutes) {
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
						boolean checked = false;
						if (item == 1) {
							checked = true;
						}
						ck.setSelected(checked);
						setDisable(true);
						setGraphic(ck);
					} else {

					}
				}
			};
		});

	}

	private IbCuotesInsure showCuotesDetails(IbCuotesInsure cuoteSelected) {
		// TODO Auto-generated method stub
		if (cuoteSelected != null) {
			// Fill the labels with info from the person object.
			lbOrden.setText(String.valueOf(cuoteSelected.getNumOrden()));
			dpFechaPago.setValue(DateUtil.dateToLocalDate(cuoteSelected.getFechaPagoCuota()));
			boolean selected = false;
			if (cuoteSelected.getPagado() == 1) {
				selected = true;
			}
			chkPagado.setSelected(selected);

		} else {
			lbOrden.setText("0");
			chkPagado.setSelected(false);
			dpFechaPago.setValue(null);
		}
		return cuoteSelected;
	}

	private boolean comprobarFinalizadoPagos() {
		// saber si tiene cuotas anteriores pagadas.
		boolean cuotaConPagosRealizados = false;
		EntityManagerFactory emf;
		EntityManager em;
		Byte pagado = 1;
		emf = Persistence.createEntityManagerFactory("prodiSegur");
		em = emf.createEntityManager();
		TypedQuery<IbCuotesInsure> query = em.createNamedQuery("IbCuotesInsure.findPayCoutes", IbCuotesInsure.class);
		query.setParameter("seguro", this.datosSeguro);
		query.setParameter("pagado", pagado);
		List<IbCuotesInsure> listCuotesPagadas = query.getResultList();

		if (listCuotesPagadas.size() == tbCuotes.getItems().size()) {
			cuotaConPagosRealizados = true;
		}

		em.close();
		return cuotaConPagosRealizados;
	}

	private IbInsurance actualizarSeguro(IbInsurance datosSeguro, String estado) {
		// INEST00-->estados
		datosSeguro.setEstado(estado);

		return datosSeguro;
	}

}
