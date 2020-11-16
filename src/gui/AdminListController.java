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
import model.entities.Admin;
import model.services.AdminService;

public class AdminListController implements Initializable, DataChangeListener{
	
	private AdminService service;
	
    @FXML
    private Button btNew;

    @FXML
    private TableView<Admin> tableViewAdmin;

    @FXML
    private TableColumn<Admin, Integer> tableColumnId;

    @FXML
    private TableColumn<Admin, String> tableColumnName;

    @FXML
    private TableColumn<Admin, String> tableColumnNameUser;

    @FXML
    private TableColumn<Admin, String> tableColumnSenha;

    @FXML
    private TableColumn<Admin, Admin> tableColumnEDIT;

    @FXML
    private TableColumn<Admin, Admin> tableColumnREMOVE;
    
    private ObservableList<Admin> obsList;
    
    @FXML
    void onBtNewAction(ActionEvent event) {
    	Stage parentStage = Utils.currentStage(event);
		Admin obj = new Admin();
		createDialogForm(obj, "/gui/AdminFormScreen.fxml", parentStage);
    }
    
    public void setAdminService(AdminService service) {
		this.service = service;
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnNameUser.setCellValueFactory(new PropertyValueFactory<>("name_user"));
		tableColumnSenha.setCellValueFactory(new PropertyValueFactory<>("senha"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewAdmin.prefHeightProperty().bind(stage.heightProperty());
	}
	
	  public void updateTableView() {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			List<Admin> list = service.findAll();
			obsList = FXCollections.observableArrayList(list);
			tableViewAdmin.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}
	    
	    private void initEditButtons() {
			tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
			tableColumnEDIT.setCellFactory(param -> new TableCell<Admin, Admin>() {
				private final Button button = new Button("edit");

				@Override
				protected void updateItem(Admin obj, boolean empty) {
					super.updateItem(obj, empty);
					if (obj == null) {
						setGraphic(null);
						return;
					}
					setGraphic(button);
					button.setOnAction(
							event -> createDialogForm(obj, "/gui/AdminFormSCreen.fxml", Utils.currentStage(event)));
				}
			});
		}

		private void initRemoveButtons() {
			tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
			tableColumnREMOVE.setCellFactory(param -> new TableCell<Admin, Admin>() {
				private final Button button = new Button("remove");

				@Override
				protected void updateItem(Admin obj, boolean empty) {
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

		private void removeEntity(Admin obj) {
			Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Area you sure to delete?");
			
			if(result.get() == ButtonType.OK) {
				if(service == null) {
					throw new IllegalStateException("Service was null");
				}
				try {
					service.remove(obj);
					updateTableView();
				}
				catch(DbIntegrityException e) {
					Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
				}	
			}
		}
		
		private void createDialogForm(Admin obj, String absoluteName, Stage parentStage) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
				Pane pane = loader.load();

				AdminFormController controller = loader.getController();
				controller.setAdmin(obj);
				controller.setService(new AdminService());
				controller.subscribeDataChangeListener(this);
				controller.updateFormData();

				Stage dialogStage = new Stage();
				dialogStage.setTitle("Enter Admin data");
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
