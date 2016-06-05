package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.IbUsuario;
import util.EntityManagerUtil;

public class UserMaintenancerOverviewController {
	@FXML
	private ComboBox<String> cbUsuario;
	@FXML
	private PasswordField tfPassActual;
	@FXML
	private PasswordField tfPassNueva;
	@FXML
	private PasswordField tfRepetirCont;

	// Event Listener on Button.onAction
	@FXML
	public void handleAceptar(ActionEvent event) {
		String message="";
		String passActual =tfPassActual.getText();
		String passNueva = tfPassNueva.getText();
		String passRep = tfRepetirCont.getText();
		
		String nameUser = cbUsuario.getSelectionModel().getSelectedItem().toString();
		EntityManager em =EntityManagerUtil.getEntityManager();
		TypedQuery<IbUsuario> queryLogin = em.createNamedQuery("IbUsuario.findByLoginName", IbUsuario.class);
		queryLogin.setParameter("usuario", nameUser);
		List<IbUsuario> listUser = queryLogin.getResultList();
		if (null!= listUser && listUser.get(0).getPassword().equals(tfPassActual.getText())){
			IbUsuario user = listUser.get(0);
			if (!passActual.isEmpty() && !passNueva.isEmpty() && !passRep.isEmpty() && passRep.equals(passNueva)){
				user.setPassword(passRep);
				em.getTransaction().begin();
				em.merge(user);
				em.getTransaction().commit();
				em.close();
				message="Se ha cambiado la contraseña del usuario:"+user.getLogin();
			}else{
				message="La contraseña nueva establecida no es correcta";
			}
		}else{
			message = "El usuario no es correcto";
		}
		Dialog dialog = new Dialog(DialogType.INFORMATION, "INFORMACIÓN",
				message);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
		dialog.showAndWait();
		((Node) (event.getSource())).getScene().getWindow().hide();
	}
	// Event Listener on Button.onAction
	@FXML
	public void handleCrearUsuario(ActionEvent event) {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/views/UserNewOverview.fxml"));
			root = (Parent) loader.load();
			UserNewOverviewController controller = loader
					.<UserNewOverviewController> getController();
			Stage stage = new Stage();
			stage.setTitle("Creación de usuarios");
			stage.setScene(new Scene(root, 475, 400));
			
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void initData(){
		EntityManager em = EntityManagerUtil.getEntityManager();
		
		List <IbUsuario> listUsers = em.createNamedQuery("IbUsuario.findAll").getResultList();
		List<String> listNameUsers = new ArrayList<String>();
		for (int i = 0; i < listUsers.size(); i++) {
			listNameUsers.add(listUsers.get(i).getLogin());
		}
		
		ObservableList<String> obsUsers = FXCollections.observableArrayList(listNameUsers);
		cbUsuario.setItems(obsUsers);
		em.close();
	}
}
