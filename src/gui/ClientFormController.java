package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.User;
import model.exceptions.ValidationException;
import model.services.UserService;

public class ClientFormController implements Initializable {
	
	private User entity;
	
	private UserService service;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
    private TextField txtName;

    @FXML
    private TextField txtName_User;

    @FXML
    private TextField txtSenha;
    
    @FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtRua;
	
	@FXML
	private TextField txtNumero;
	
	@FXML
	private TextField txtComp;

    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorNameUser;

    @FXML
    private Label labelErrorSenha;
    
    @FXML
    private Label labelErrorEmail;

    @FXML
    private Label labelErrorRua;
    
    @FXML
    private Label labelErrorNumero;
    
    @FXML 
    private Label labelErrorComp;
    
    @FXML
    private Button btSalvar;

    @FXML
    private Button btCancelar;

    @FXML
    void onBtCancelarAction(ActionEvent event) {
    	Utils.currentStage(event).close();
    }

    @FXML
    void onBtSalvarAction(ActionEvent event) {
    	if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {

			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
			
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		catch(ValidationException e) {
			setErrorsMessages(e.getErrors());
		}
		
    }

	public void setUser(User entity) {
    	this.entity = entity;
    }
    
    public void setService(UserService service) {
    	this.service = service;
    }

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
    
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtName_User.setText(entity.getName_user());
		txtSenha.setText(entity.getSenha());
		txtEmail.setText(entity.getEmail());
		txtRua.setText(entity.getRua());
		txtNumero.setText(String.valueOf(entity.getNumero()));
		if(txtComp.getText() != null) {
			txtComp.setText(entity.getComplemento());
		}
	}
	
	private void initializeNodes() {
		
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName_User, 20);
		Constraints.setTextFieldMaxLength(txtSenha, 20);
		Constraints.setTextFieldMaxLength(txtName, 45);
		Constraints.setTextFieldMaxLength(txtEmail, 30);
		Constraints.setTextFieldMaxLength(txtRua, 50);
		Constraints.setTextFieldInteger(txtNumero);
		Constraints.setTextFieldMaxLength(txtComp, 25);
	}
	
	private User getFormData() {
		User obj = new User();

		ValidationException exception = new ValidationException("Validation error!");
		
		
			obj.setId(Utils.tryParsetoInt(txtId.getText()));
			
			if (txtName_User.getText() == null || txtName_User.getText().trim().equals("")) {
				exception.addError("name_user", "Field can't be empty");
			}
			obj.setName_user(txtName_User.getText());
			
			if (txtSenha.getText() == null || txtSenha.getText().trim().equals("")) {
				exception.addError("senha", "Field can't be empty");
			}
			obj.setSenha(txtSenha.getText());
			
			if (txtName.getText() == null || txtName.getText().trim().equals("")) {
				exception.addError("name", "Field can't be empty");
			}
			obj.setName(txtName.getText());
			
			if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
				exception.addError("email", "Field can't be empty");
			}
			obj.setEmail(txtEmail.getText());
			
			if (txtRua.getText() == null || txtRua.getText().trim().equals("")) {
				exception.addError("rua", "Field can't be empty");
			}
			obj.setRua(txtRua.getText());
			
			if (txtNumero.getText() == null || txtNumero.getText().trim().equals("")) {
				exception.addError("numero", "Field can't be empty");
			}
			obj.setNumero(Utils.tryParsetoInt(txtNumero.getText()));
			
			obj.setComplemento(txtComp.getText());
	
			if (exception.getErrors().size() > 0) {
				throw exception;
			}
			
			return obj;
	}
	
	private void setErrorsMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
		
		labelErrorNameUser.setText((fields.contains("name_user") ? errors.get("name_user") : ""));
		
		labelErrorSenha.setText((fields.contains("senha") ? errors.get("senha") : ""));
		
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
		
		labelErrorRua.setText((fields.contains("rua") ? errors.get("rua") : ""));
		
	}
}