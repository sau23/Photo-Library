package photos;

import classes.Lists;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Photos extends Application {
	
	// classy debug boolean
	public static final boolean DEBUG = true;
	
	/**
	 * Overall reference to primaryStage accessible to switching functions.
	 */
	public static Stage window;
	
	/**
	 * The variables to hold the scenes and their respective loaders;
	 */
	public static FXMLLoader loginLoader, adminLoader, userLoader;
	public static Scene loginScene, adminScene, userScene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			window = primaryStage;
			
			showLogin();
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Switches display to login scene.
	 * 
	 * @throws Exception
	 */
	public static void showLogin() throws Exception{
		loginLoader = new FXMLLoader();
		loginLoader.setLocation(Photos.class.getResource("/Login.fxml"));

		// read values from user data ser file if it exists
		Lists.readFromDatabase();
		
		loginScene = new Scene((AnchorPane)loginLoader.load());
		
		window.setTitle("Login");
		window.setScene(loginScene);
		window.show();
	}
	
	/**
	 * Switches display to admin scene.
	 * 
	 * @throws Exception
	 */
	public static void showAdmin() throws Exception{
		adminLoader = new FXMLLoader();
		adminLoader.setLocation(Photos.class.getResource("/Admin.fxml"));
		
		adminScene = new Scene((AnchorPane)adminLoader.load());

		AdminController ac = adminLoader.getController();
		ac.start();
		
		window.setTitle("Admin");
		window.setScene(adminScene);
		window.show();
	}
	
	/**
	 * Switches display to user scene.
	 * 
	 * @throws Exception
	 */
	public static void showUser(int index) throws Exception{
		userLoader = new FXMLLoader();
		userLoader.setLocation(Photos.class.getResource("/User.fxml"));

		userScene = new Scene((AnchorPane)userLoader.load());
		
		UserController uc = userLoader.getController();
		uc.setUserIndex(index);
		
		window.setTitle(Lists.users.get(index) + "'s Albums");
		window.setScene(userScene);
		window.show();
	}
}
