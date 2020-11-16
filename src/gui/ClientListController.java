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
import model.entities.User;
import model.services.UserService;

public class ClientListController implements Initializable, DataChangeListener{
	
	private UserService service;
	
    @FXML
    private Button btNew;

    @FXML
    private TableView<User> tableViewUser;

    @FXML
    private TableColumn<User, Integer> tableColumnId;

    @FXML
    private TableColumn<User, String> tableColumnName;

    @FXML
    private TableColumn<User, String> tableColumnEmail;

    @FXML
    private TableColumn<User, String> tableColumnUser;

    @FXML
    private TableColumn<User, String> tableColumnSenha;
    
    @FXML
    private TableColumn<User, String> tableColumnRua;

    @FXML
    private TableColumn<User, Integer> tableColumnNumero;
    
    @FXML
    private TableColumn<User, String> tableColumnComp;
    
    @FXML
    private TableColumn<User, User> tableColumnEDIT;

    @FXML
    private TableColumn<User, User> tableColumnREMOVE;
    
    private ObservableList<User> obsList;

    @FXML
    void onBtNewAction(ActionEvent event) {
    	Stage parentStage = Utils.currentStage(event);
		User obj = new User();
		createDialogForm(obj, "/gui/ClientFormScreen.fxml", parentStage);
    }
    
    public void setUserService(UserService service) {
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
		tableColumnUser.setCellValueFactory(new PropertyValueFactory<>("name_user"));
		tableColumnSenha.setCellValueFactory(new PropertyValueFactory<>("senha"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnRua.setCellValueFactory(new PropertyValueFactory<>("rua"));
		tableColumnNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		tableColumnComp.setCellValueFactory(new PropertyValueFactory<>("complemento"));
		
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewUser.prefHeightProperty().bind(stage.heightProperty());
	}
	
	  public void updateTableView() {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			List<User> list = service.findAll();
			obsList = FXCollections.observableArrayList(list);
			tableViewUser.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}
	    
	    private void initEditButtons() {
			tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
			tableColumnEDIT.setCellFactory(param -> new TableCell<User, User>() {
				private final Button button = new Button("edit");

				@Override
				protected void updateItem(User obj, boolean empty) {
					super.updateItem(obj, empty);
					if (obj == null) {
						setGraphic(null);
						return;
					}
					setGraphic(button);
					button.setOnAction(
							event -> createDialogForm(obj, "/gui/ClientFormScreen.fxml", Utils.currentStage(event)));
				}
			});
		}

		private void initRemoveButtons() {
			tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
			tableColumnREMOVE.setCellFactory(param -> new TableCell<User, User>() {
				private final Button button = new Button("remove");

				@Override
				protected void updateItem(User obj, boolean empty) {
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

		private void removeEntity(User obj) {
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
					e.printStackTrace();
				}	
			}
		}
		
		private void createDialogForm(User obj, String absoluteName, Stage parentStage) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
				Pane pane = loader.load();

				ClientFormController controller = loader.getController();
				controller.setUser(obj);
				controller.setService(new UserService());
				controller.subscribeDataChangeListener(this);
				controller.updateFormData();

				Stage dialogStage = new Stage();
				dialogStage.setTitle("Enter User data");
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
