package photos;

import classes.UserList;
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
	public static FXMLLoader loginLoader, adminLoader, userLoader, displayLoader;
	public static Scene loginScene, adminScene, userScene, displayScene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			window = primaryStage;
			
			showLogin();
			
			// add stock user
			if(UserList.stock) UserList.addStockUser();
		
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
		UserList.readFromUserDatabase();
		
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
		
		window.setTitle(UserList.users.get(index).getName() + "'s Albums");
		window.setScene(userScene);
		window.show();
	}
	
	/**
	 * Creates new display for display scene.
	 * 
	 * @throws Exception
	 */
	public static void showDisplay(int userIndex, int albumIndex) throws Exception{
		displayLoader = new FXMLLoader();
		displayLoader.setLocation(Photos.class.getResource("/Display.fxml"));
		
		displayScene = new Scene((AnchorPane)displayLoader.load());

		DisplayController dc = displayLoader.getController();
		dc.setAlbum(userIndex, albumIndex);
		
		Stage stage = new Stage();
		
		stage.setTitle("");
		stage.setScene(displayScene);
		stage.show();
	}
}
