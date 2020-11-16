package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Admin;
import model.entities.Product;
import model.entities.Type;
import model.exceptions.ValidationException;
import model.services.AdminService;
import model.services.ProductService;
import model.services.TypeService;

public class ProductFormController implements Initializable {

	private Product entity;

	private ProductService service;
	
	private TypeService typeService;
	
	private AdminService admService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtPrice;

	@FXML
	private ComboBox<Type> comboBoxType;
	
	@FXML
	private ComboBox<Admin> comboBoxAdmin;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorType;

	@FXML
	private Label labelErrorAdmin;

	@FXML
	private Label labelErrorPrice;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Type> obslist;
	
	private ObservableList<Admin> obslistAdm;

	public void setProducts(Product entity) {
		this.entity = entity;
	}

	public void setServices(ProductService service, AdminService admService, TypeService typeService) {
		this.service = service;
		this.admService = admService;
		this.typeService = typeService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@FXML
	public void onbtSaveAction(ActionEvent event) {

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
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			setErrorsMessages(e.getErrors());
		}

	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	@FXML
	public void onbtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	//Pega os dados preenchidos no formulario e carrega um objeto com esses dados, retornando o mesmo no final.
	private Product getFormData() {
		Product obj = new Product();

		ValidationException exception = new ValidationException("Validation error!");

		obj.setId(Utils.tryParsetoInt(txtId.getText()));
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if (txtPrice.getText() == null || txtPrice.getText().trim().equals("")) {
			exception.addError("price", "Field can't be empty");
		}
		obj.setPrice(Utils.tryParsetoDouble(txtPrice.getText()));
		
		obj.setType(comboBoxType.getValue());
		obj.setAdm(comboBoxAdmin.getValue());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtPrice);
		initializeComboBoxType();
		initializeComboBoxAdmin();
	}

	// Pega os dados do objeto e joga na caixa do formulário.
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		Locale.setDefault(Locale.US);
		txtPrice.setText(String.format("%.2f", entity.getPrice()));
		
		if(entity.getType() == null) {
			comboBoxType.getSelectionModel().selectFirst();
		}else {
			comboBoxType.setValue(entity.getType());
		}
		
		if(entity.getAdm() == null) {
			comboBoxAdmin.getSelectionModel().selectFirst();
		}else {
			comboBoxAdmin.setValue(entity.getAdm());
		}
	}

	public void loadAssociatedObjects() {		
		if(admService == null ) {
			throw new IllegalStateException("AdmService was null");
		}
		
		List<Type> list = typeService.findAll();
		obslist = FXCollections.observableArrayList(list);	
		comboBoxType.setItems(obslist);
		
		List<Admin> listAdm = admService.findAll();
		obslistAdm = FXCollections.observableArrayList(listAdm);
		comboBoxAdmin.setItems(obslistAdm);
	}
	
	//Testa cada um dos possíveis erros e setar no label correspondente.
	private void setErrorsMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
		
		labelErrorPrice.setText((fields.contains("price") ? errors.get("price") : ""));
		
		labelErrorType.setText((fields.contains("type") ? errors.get("type") : ""));
		
		labelErrorAdmin.setText((fields.contains("admin") ? errors.get("admin") : ""));
		
	}

	private void initializeComboBoxType() {
		Callback<ListView<Type>, ListCell<Type>> factory = lv -> new ListCell<Type>() {
			@Override
			protected void updateItem(Type item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName_type());
			}
		};
		comboBoxType.setCellFactory(factory);
		comboBoxType.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxAdmin() {
		Callback<ListView<Admin>, ListCell<Admin>> factory = lv -> new ListCell<Admin>() {
			@Override
			protected void updateItem(Admin item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName_user());
			}
		};
		comboBoxAdmin.setCellFactory(factory);
		comboBoxAdmin.setButtonCell(factory.call(null));
	}

}
