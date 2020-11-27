package gui;

import java.io.IOException;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.AdminService;
import model.services.ProductService;
import model.services.UserService;

public class MainScreenController {

    @FXML private MenuItem menuItemAdmins;
    @FXML private MenuItem menuItemProducts;
    @FXML private MenuItem menuItemAbout;
    @FXML private MenuItem menuItemClient;
    @FXML private MenuItem menuItemDeslog;

    @FXML
    void onMenuItemAboutAction(ActionEvent event) {
    	loadview("/gui/AboutScreen.fxml", x -> {});
    }
    
    @FXML
    void onMenuItemClientAction(ActionEvent event) {
    	loadview("/gui/ClientScreen.fxml", (ClientListController controller) -> {
			controller.setUserService(new UserService());
			controller.updateTableView();
		});
    }

    @FXML
    void onMenuItemProductsAction(ActionEvent event) {
    	loadview("/gui/ProductsScreen.fxml", (ProductsListController controller) -> {
			controller.setProductService(new ProductService());
			controller.updateTableView();
		});
    }

    @FXML
    void onMenuAdminsAction(ActionEvent event) {
    	loadview("/gui/AdminScreen.fxml", (AdminListController controller) -> {
			controller.setAdminService(new AdminService());
			controller.updateTableView();
		});
    }

    @FXML 
    void onMenuItemDeslogACtion(ActionEvent event) {
    	Main.ChangeScreen("login");
    }
    private synchronized <T> void loadview(String absoluteName, Consumer <T> initializingAction) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
}

