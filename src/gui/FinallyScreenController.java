package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.dao.DaoFactory;
import model.dao.OrderDao;

public class FinallyScreenController implements Initializable{
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
    private TextField txtTotal;

    @FXML
    private Button btConfirmar;

    @FXML
    void onBtConfirmarAction(ActionEvent event) {
    	
    	Alerts.showAlert("Pedido finalizado", "Muito obrigado por comprar conosco!!", "Seu pedido será entregue em breve!!", AlertType.INFORMATION);
    	OrderDao dao = DaoFactory.createOrderDao();
    	dao.deleteByPedido(dao.OrderIdClient(LoginScreenController.loginClient()));
    	Utils.currentStage(event).close();
    	Main.ChangeScreen("mainp");
    }
    
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldDouble(txtTotal);
		
	}
	
	private Double getTotal() {	
		OrderDao dao = DaoFactory.createOrderDao();
		int Id = dao.OrderIdClient(LoginScreenController.loginClient()); 
		double total = dao.OrderSum(Id);
		return total;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	public void updateFormData() {
		txtTotal.setText(String.valueOf(getTotal()));		
	}
}
