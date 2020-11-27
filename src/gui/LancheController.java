package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.dao.DaoFactory;
import model.dao.ProductDao;
import model.entities.ClientOrder;
import model.entities.Product;
import model.services.OrderService;

public class LancheController implements Initializable, DataChangeListener{
	
    @FXML
    private Button btVoltar;

    @FXML
    private Button btFinalizar;
    
    @FXML
    private TableView<Product> tableView;

    @FXML
    private TableColumn<Product, String> tableColumnName;

    @FXML
    private TableColumn<Product, Double> tableColumnPrice;

    @FXML
    private TableColumn<Product, Product> tableColumnBuy;
    
    private ObservableList<Product> obsList;
    
    @FXML
    private void onBtFinalizarAction(ActionEvent event) {
    	Stage parentStage = Utils.currentStage(event);
		ClientOrder obj = new ClientOrder();
		createDialogF(obj, "/gui/FinallyScreen.fxml", parentStage);
    }
    
    @FXML
    private void onBtVoltarAction(ActionEvent event) {
    	Main.ChangeScreen("mainp");
    }
    
    @Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		initBuyButtons();
    }
    
    private void initializeNodes() {
    	
    	tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

		Utils.formatTableColumnDouble(tableColumnPrice, 2);
		
		tableView.setItems(attTable());
				
    }
    
    public ObservableList<Product> attTable(){

    	ProductDao dao = DaoFactory.createProductDao();
    	
    	List<Product> list = dao.findAll();
    	obsList = FXCollections.observableArrayList(list);
    	return obsList;
    }
    
    private void initBuyButtons() {
		tableColumnBuy.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnBuy.setCellFactory(param -> new TableCell<Product, Product>() {
			private final Button Button = new Button("Comprar");

			@Override
			protected void updateItem(Product obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(Button);
				Button.setOnAction(event -> createDialogForm(obj, "/gui/BuyScreen.fxml", Utils.currentStage(event)));
			
			}
		});
	}  
      
	private void createDialogForm(Product obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			BuyController controller = loader.getController();
			controller.setProduct(obj);
			controller.setService(new OrderService());
			//controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Compra");
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
	
	private void createDialogF(ClientOrder obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			FinallyScreenController controller = loader.getController();
			//controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Compra");
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

	@Override
	public void onDataChanged() {
		attTable();
	}
	
	
}	
