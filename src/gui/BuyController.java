package gui;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DB;
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
import model.entities.ClientOrder;
import model.entities.Product;
import model.entities.User;
import model.exceptions.ValidationException;
import model.services.OrderService;

public class BuyController implements Initializable {
	
	private Connection conn;
	
	private ClientOrder entityO;
	
	private Product entity;
	
	private OrderService service;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
    private TextField txtQuantidade;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtPreço;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtIdProduct;
	
	@FXML
	private TextField txtClient;
	
    @FXML
    private Label labelErrorQtd;
    
    @FXML
    private Label labelProduto;
    
    @FXML
    private Label labelPreço;

    @FXML
    private Button btAdd;

    @FXML
    private Button btCancel;
    
    public void setProduct(Product entity) {
    	this.entity = entity;
    }
    
    public void setOrder(ClientOrder entityO) {
    	this.entityO = entityO;
    }
    public void setService(OrderService service) {
    	this.service = service;
    }
    
    public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}
    
    @FXML
    void onBtAddAction(ActionEvent event) {
    	
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			entityO = getFormDataOrder();
			service.saveOrUpdate(entityO);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		catch(ValidationException e) {
			setErrorsMessages(e.getErrors());
		}
    }

    @FXML
    void onBtCancelAction(ActionEvent event) {
    	Utils.currentStage(event).close();
    }
	
    private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}
    
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtQuantidade);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtName.setText(entity.getName());
		txtPreço.setText(String.format("%.2f", entity.getPrice()));
		txtIdProduct.setText(String.valueOf(entity.getId()));
		txtClient.setText(String.valueOf(service.orderIdClient(LoginScreenController.loginClient())));

	}	
	
	private Product getFormaDataProduct() {
		Product obj = new Product();
		ValidationException exception = new ValidationException("Validation error!");
		
		obj.setId(entity.getId());
		
		if(txtQuantidade.getText() == null || txtQuantidade.getText().trim().equals("")) {
			exception.addError("quantidade", "Field can't be empty");
		}
		obj.setQtd(Utils.tryParsetoInt(txtQuantidade.getText()));
		
		return obj;
	}
	
	private User getFormDataUser() {		
		User obj = new User();
		
		obj.setId(service.orderIdClient(LoginScreenController.loginClient()));
		return obj;
	}
	
	private ClientOrder getFormDataOrder() {
		ClientOrder obj = new ClientOrder();
		getFormaDataProduct();
		
		obj.setid(Utils.tryParsetoInt(txtId.getText()));
		
		obj.setCliente(getFormDataUser());
		
		obj.setPd(getFormaDataProduct());
		
		return obj;
	}
	
	private void setErrorsMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorQtd.setText((fields.contains("qtd") ? errors.get("qtd") : ""));
		
	}
	
	public User findByNameUser(String login) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM cliente WHERE name_user = ?");
			st.setString(1, login);
			
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
}
