//.

package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	private static Scene mainScene;
	private static Scene loginScene;
	private static Stage stage;
	private static Scene form;
	private static Scene mainUser;
	private static Scene Lanche;
	private static Scene Finally;
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		
		try {
			Parent Loginfxml = FXMLLoader.load(getClass().getResource("/gui/LoginScreen.fxml"));
			loginScene = new Scene(Loginfxml);
			primaryStage.setScene(loginScene);
			
			Parent LoginFormfxml = FXMLLoader.load(getClass().getResource("/gui/LoginFormScreen.fxml"));
			form = new Scene(LoginFormfxml);
			
			Parent LancheFXML = FXMLLoader.load(getClass().getResource("/gui/LancheScreen.fxml"));
			Lanche = new Scene(LancheFXML);
			
			Parent FinallyFXML = FXMLLoader.load(getClass().getResource("/gui/FinallyScreen.fxml"));
			Finally = new Scene(FinallyFXML);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainScreen.fxml"));
			ScrollPane scrollPane = loader.load();
			
			FXMLLoader loaderUser = new FXMLLoader(getClass().getResource("/gui/MainScreenUser.fxml"));
			ScrollPane scrollPaneUser = loaderUser.load();
			
			
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			scrollPaneUser.setFitToHeight(true);
			scrollPaneUser.setFitToWidth(true);
			
			mainScene = new Scene(scrollPane);
			mainUser = new Scene(scrollPaneUser);
		
			primaryStage.setTitle("FoodProject application");
			primaryStage.show();
			
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getMainScene() {
		return mainScene;
	}
	
	public static void ChangeScreen(String scr) {
		switch(scr){
			case "main":
				stage.setScene(mainScene);
			break;
			
			case "login":
				stage.setScene(loginScene);
			break;
			
			case "form":
				stage.setScene(form);
			break;
			
			case "mainp":
				stage.setScene(mainUser);
			break;
			
			case "Lanche":
				stage.setScene(Lanche);
			break;
			
			case "finally":
				stage.setScene(Finally);
			break;
		}	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
