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
	private static Scene mainuser;
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		
		try {
			Parent Loginfxml = FXMLLoader.load(getClass().getResource("/gui/LoginScreen.fxml"));
			loginScene = new Scene(Loginfxml);
			primaryStage.setScene(loginScene);
			
			Parent LoginFormfxml = FXMLLoader.load(getClass().getResource("/gui/LoginFormScreen.fxml"));
			form = new Scene(LoginFormfxml);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainScreen.fxml"));
			ScrollPane scrollPane = loader.load();
			
			Parent mainp = FXMLLoader.load(getClass().getResource("/gui/MainScreenUsuario.fxml"));
			mainuser = new Scene(mainp);
			
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			mainScene = new Scene(scrollPane);
		
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
				stage.setScene(mainuser);
			break;
		}	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
