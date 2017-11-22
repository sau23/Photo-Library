package photos;

import classes.Photo;
import classes.UserList;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

/**
 * Main runner class responsible for switching between scenes and provides
 * each scene with their necessary data.
 * 
 * @author Nicholas Petriello
 * @author Samuel Uganiza
 */
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
	
	/**
	 * Program begins here by showing the login screen first and adds the
	 * stock user if the toggle has been turned on in the admin scene.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			
			window = primaryStage;
			
			showLogin();
			
			// add stock user
			if(UserList.generateStock) UserList.addStockUser();
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Main function.
	 * 
	 * @param args
	 */
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
		uc.setUser(index);
		
		window.setTitle(UserList.getUser(index).getName() + "'s Albums");
		window.setScene(userScene);
		window.show();
	}
	
	/**
	 * Creates new display for display scene.
	 * 
	 * @throws Exception
	 */
	public static void showDisplay(int userIndex, int photoIndex, ObservableList<Photo> photos) throws Exception{
		displayLoader = new FXMLLoader();
		displayLoader.setLocation(Photos.class.getResource("/Display.fxml"));
		
		displayScene = new Scene((AnchorPane)displayLoader.load());

		DisplayController dc = displayLoader.getController();
		dc.setAlbum(userIndex, photoIndex, photos);
		
		Stage stage = new Stage();
		
		stage.setTitle("");
		stage.setScene(displayScene);
		stage.show();
	}
}
