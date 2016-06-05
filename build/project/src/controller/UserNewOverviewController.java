package controller;

import javax.persistence.EntityManager;

import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.github.daytron.simpledialogfx.dialog.DialogType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import model.IbUsuario;
import util.EntityManagerUtil;

public class UserNewOverviewController {
	@FXML
	private TextField tfNombre;
	@FXML
	private PasswordField tfPassNueva;
	@FXML
	private PasswordField tfRepetirCont;
	@FXML
	private TextField tfLogin;

	// Event Listener on Button.onAction
	@FXML
	public void handleAceptar(ActionEvent event) {
		String message = "";
		if (!tfLogin.getText().isEmpty() && !tfNombre.getText().isEmpty()) {
			if (!tfPassNueva.getText().isEmpty() && !tfRepetirCont.getText().isEmpty()) {
				if (tfPassNueva.getText().equals(tfRepetirCont.getText())) {
					IbUsuario user = new IbUsuario();
					user.setLogin(tfLogin.getText());
					user.setNombre(tfNombre.getText());
					user.setPassword(tfPassNueva.getText());
					EntityManager em = EntityManagerUtil.getEntityManager();
					em.getTransaction().begin();
					em.persist(user);
					em.getTransaction().commit();
					em.close();
					message = "Se ha creado el usuario.";
				} else {
					message = "Las contraseñas no son iguales.";
				}
			} else {
				message = "Algunas de las contraseñas no está completas.";
			}
		} else {
			message = "No se ha escrito Login/nickName o nombre del usuario.";
		}
		Dialog dialog = new Dialog(DialogType.ERROR, "INFORMACIÓN", message);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.showAndWait();
		if (message.equals("Se ha creado el usuario.")) {
			((Node) event.getSource()).getScene().getWindow().hide();
		}
	}
}
