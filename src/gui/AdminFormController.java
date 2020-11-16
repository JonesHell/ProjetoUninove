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
import model.dao.AdminDao;
import model.dao.DaoFactory;
import model.dao.UserDao;
import model.entities.Admin;
import model.exceptions.ValidationException;
import model.services.AdminService;

public class AdminFormController implements Initializable {
	
	private Admin entity;
	
	private AdminService service;
	
	private UserDao dao2 = DaoFactory.createUserDao();
	
	private AdminDao dao = DaoFactory.createAdminDao();
	
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
    private Label labelErrorName;

    @FXML
    private Label labelErrorNameUser;

    @FXML
    private Label labelErrorSenha;

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
			if(!verification()) {
				if(!verification2()) {
				entity = getFormData();
				service.saveOrUpdate(entity);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
				}
			}else {
				Utils.currentStage(event).close();
			}
			
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		catch(ValidationException e) {
			setErrorsMessages(e.getErrors());
		}
		
    }
    
    private boolean verification() {
    	
    	if(dao.findByName(txtName_User.getText())) {
			
			Alerts.showAlert("Error saving object", null, "Usuário ja esta cadastrado!\n Escolha um diferente!", AlertType.INFORMATION);
			return true;
    	}else {
			return false;
		}
	}
    
    private boolean verification2() {
    	
    	if(dao2.findByName(txtName_User.getText())) {
			
			Alerts.showAlert("Error saving object", null, "Usuário ja esta cadastrado!\n Escolha um diferente!", AlertType.INFORMATION);
			return true;
		}else {
			return false;
		}
    }

	public void setAdmin(Admin entity) {
    	this.entity = entity;
    }
    
    public void setService(AdminService service) {
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
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName_User, 20);
		Constraints.setTextFieldMaxLength(txtSenha, 20);
		Constraints.setTextFieldMaxLength(txtName, 45);
	}
	
	private Admin getFormData() {
		Admin obj = new Admin();

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
		
	}
	
	
    
}
