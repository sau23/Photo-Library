package photos;

import classes.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Photos extends Application {
	
	// classy debug boolean
	public static final boolean DEBUG = true;
	Stage stage;
	Scene login, admin;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			stage = primaryStage;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/Login.fxml"));			
			LoginController logCon = loader.getController();		
			AnchorPane root = (AnchorPane)loader.load();
			login = new Scene(root);
			primaryStage.setScene(login);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
