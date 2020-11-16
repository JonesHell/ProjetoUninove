package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Product;
import model.entities.Type;
import model.services.AdminService;
import model.services.ProductService;
import model.services.TypeService;

public class ProductsListController implements Initializable, DataChangeListener{
	
	private ProductService productService;
	
    @FXML
    private Button btNew;

    @FXML
    private TableView<Product> tableViewProduct;

    @FXML
    private TableColumn<Product, Integer> tableColumnId;

    @FXML
    private TableColumn<Product, String> tableColumnName;

    @FXML
    private TableColumn<Product, Double> tableColumnPrice;
    
    @FXML
    private TableColumn<Type, String> tableColumnType;

    @FXML
    private TableColumn<Product, Product> tableColumnEDIT;

    @FXML
    private TableColumn<Product, Product> tableColumnREMOVE;
    
    private ObservableList<Product> obsList;

    @FXML
    void onBtNewAction(ActionEvent event) {
    	Stage parentStage = Utils.currentStage(event);
		Product obj = new Product();
		createDialogForm(obj, "/gui/ProductsFormScreen.fxml", parentStage);
    }
    
    public void setProductService(ProductService productService) {
		this.productService = productService;
	}
    
    @Override
	public void initialize(URL url, ResourceBundle rb) {
    	tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		tableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
		
		Utils.formatTableColumnDouble(tableColumnPrice, 2);
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewProduct.prefHeightProperty().bind(stage.heightProperty());
		
	}
    
    public void updateTableView() {
		if (productService == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Product> list = productService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewProduct.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
    
    private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Product, Product>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Product obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/ProductsFormScreen.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Product, Product>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Product obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Product obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Area you sure to delete?");
		
		if(result.get() == ButtonType.OK) {
			if(productService == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				productService.remove(obj);
				updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
				
			}
			
		}
	}

	
	private void createDialogForm(Product obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ProductFormController controller = loader.getController();
			controller.setProducts(obj);
			controller.setServices(new ProductService(), new AdminService(), new TypeService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Product data");
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
		updateTableView();
	}

}	
