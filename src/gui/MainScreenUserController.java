package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Product;
import model.services.AdminService;
import model.services.ProductService;
import model.services.TypeService;

public class MainScreenUserController implements Initializable, DataChangeListener {

	private ProductService service;

	@FXML
	private Button btNew;

	@FXML
	private Tab tabLanche, tabPizza, tabDoces, tabBebidas;

	@FXML
	private TableView<Product> tableViewLanche, tableViewPizza, tableViewDoce, tableViewBebida;

	@FXML
	private TableColumn<Product, String> tableColumnNameL, tableColumnNameP, tableColumnNameD, tableColumnNameB;

	@FXML
	private TableColumn<Product, Double> tableColumnPriceL, tableColumnPriceP, tableColumnPriceD, tableColumnPriceB;

	@FXML
	private TableColumn<Product, Product> tableColumnBuyL, tableColumnBuyP, tableColumnBuyD, tableColumnBuyB;

	@FXML
	private TableColumn<Product, Product> tableColumnEDIT;

	private ObservableList<Product> obsList;

	List<Product> produtos = new ArrayList<>();

	@FXML
	void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Product obj = new Product();
		createDialogForm(obj, "/gui/ProductsFormScreen.fxml", parentStage);
	}

	public void setProductService(ProductService service) {
		this.service = service;
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		initializeLanche();
		initializePizza();
		initializeDoce();
		initializeBebida();

	}

	private Predicate<Product> getType(Integer tipo) {
		if (tipo == 1) {
			return p -> p.getType().getId() == 1;
		} else if (tipo == 2) {
			return p -> p.getType().getId() == 2;
		} else if (tipo == 3) {
			return p -> p.getType().getId() == 3;
		} else if (tipo == 4) {
			return p -> p.getType().getId() == 4;
		} else {
			return null;
		}
	}

	private void initializeLanche() {
		tableColumnNameL.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnPriceL.setCellValueFactory(new PropertyValueFactory<>("price"));

		Utils.formatTableColumnDouble(tableColumnPriceL, 2);
	}

	private void initializePizza() {
		tableColumnNameP.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnPriceP.setCellValueFactory(new PropertyValueFactory<>("price"));

		Utils.formatTableColumnDouble(tableColumnPriceP, 2);
	}

	private void initializeDoce() {
		tableColumnNameD.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnPriceD.setCellValueFactory(new PropertyValueFactory<>("price"));

		Utils.formatTableColumnDouble(tableColumnPriceD, 2);
	}

	private void initializeBebida() {
		tableColumnNameB.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnPriceB.setCellValueFactory(new PropertyValueFactory<>("price"));

		Utils.formatTableColumnDouble(tableColumnPriceB, 2);
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Product> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewLanche.setItems(obsList);
		initEditButtons();
		initRemoveButtons();

		FilteredList<Product> Lanche = new FilteredList<>(FXCollections.observableArrayList(obsList), getType(1));
		tableViewLanche.setItems(Lanche);

		FilteredList<Product> Pizza = new FilteredList<>(FXCollections.observableArrayList(obsList), getType(2));
		tableViewPizza.setItems(Pizza);

		FilteredList<Product> Doce = new FilteredList<>(FXCollections.observableArrayList(obsList), getType(3));
		tableViewDoce.setItems(Doce);

		FilteredList<Product> Bebida = new FilteredList<>(FXCollections.observableArrayList(obsList), getType(4));
		tableViewBebida.setItems(Bebida);

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
						event -> createDialogForm(obj, "/gui/MainScreenUsuario.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnBuyL.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnBuyL.setCellFactory(param -> new TableCell<Product, Product>() {
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
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação",
				"Deseja adicionar esse produto a sua lista?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
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

}
