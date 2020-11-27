package gui;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Main;
import db.DB;
import db.DbException;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.User;
import model.services.UserService;

public class LoginScreenController {

	private Connection conn = DB.getConnection();

	@FXML
	private TextField tfUsuario;

	@FXML
	private PasswordField tfSenha;
	
	public static String saveLogin;
 

	@FXML
	void onBtAcessarAction(ActionEvent event) {

		if (checkAdmin(tfUsuario.getText(), tfSenha.getText())) {
			
			Main.ChangeScreen("main");
			saveLogin = tfUsuario.getText();
			tfUsuario.setText(null);
			tfSenha.setText(null);
			
		} else if (checkLogin(tfUsuario.getText(), tfSenha.getText())) {
			
			Main.ChangeScreen("mainp");
			saveLogin = tfUsuario.getText();
			tfUsuario.setText(null);
			tfSenha.setText(null);
			
		} else {
			Alerts.showAlert("Login", null, "Login Failed", AlertType.INFORMATION);
		}
		
	}

	@FXML
	void onBtSairAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@FXML
	void onBtRegisterAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		User obj = new User();
		createDialogForm(obj, "/gui/LoginFormScreen.fxml", parentStage);
	}

	private void createDialogForm(User obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			LoginFormController controller = loader.getController();
			controller.setUser(obj);
			controller.setServices(new UserService());
			//controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter new user data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro loading view", e.getMessage(), AlertType.ERROR);
		}

	}
	
	private boolean checkLogin(String name_user, String senha) {
		PreparedStatement st = null;
		ResultSet rs = null;
		boolean check = false;

		if (!name_user.isEmpty()) {

			try {
				st = conn.prepareStatement("SELECT * FROM cliente WHERE name_user = ? and senha = ?");
				st.setString(1, name_user);
				st.setString(2, senha);
				rs = st.executeQuery();

				if (rs.next()) {
					check = true;
				}
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			} finally {
				DB.closeStatement(st);
				DB.closeResultSet(rs);
			}
		}
		return check;
	}

	private boolean checkAdmin(String name_user, String senha) {

		PreparedStatement st = null;
		ResultSet rs = null;
		boolean check = false;

		if (!name_user.isEmpty()) {

			try {
				st = conn.prepareStatement("SELECT * FROM admin WHERE name_user = ? and senha = ?");
				st.setString(1, name_user);
				st.setString(2, senha);
				rs = st.executeQuery();

				if (rs.next()) {
					check = true;
				}
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			} finally {
				DB.closeStatement(st);
				DB.closeResultSet(rs);
			}
		}
		return check;
	}
	
	public User Login() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM cliente WHERE name_user = ?");
			st.setString(1, tfUsuario.getText());
			rs = st.executeQuery();
			
			if (rs.next()) {
				
				User obj = new User();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("name"));
				obj.setName_user(rs.getString("name_user"));
				obj.setSenha(rs.getString("senha"));
				obj.setEmail(rs.getString("email"));
				obj.setRua(rs.getString("rua"));
				obj.setNumero(rs.getInt("numero"));
				obj.setComplemento(rs.getString("complemento"));
				
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public static String loginClient() {
		return saveLogin;
	}
}
