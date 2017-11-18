package photos;

import java.util.Calendar;

import classes.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.File;

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
			
			// add stock user if the list is empty or first user is not stock user
			if(UserList.users.isEmpty() || !UserList.users.get(0).getName().equals("stock")) {
				addStockUser();
			}
		
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
	
	public static void showDisplay() throws Exception{
		displayLoader = new FXMLLoader();
		displayLoader.setLocation(Photos.class.getResource("/Display.fxml"));
		
		displayScene = new Scene((AnchorPane)displayLoader.load());

		DisplayController dc = displayLoader.getController();
		
		window.setTitle("");
		window.setScene(displayScene);
		window.show();
	}
	
	public static void addStockUser() {
		User stock = new User("stock", "");
		
		Album album1 = new Album("Album 1");
		
		Calendar date = Calendar.getInstance();
		File f = new File("data/stock/niko.png");
		date.setTimeInMillis(f.lastModified());
		Photo niko = new Photo(date, "data/stock/niko.png");
		album1.getPhotos().add(niko);
		stock.getAlbums().add(album1);
		UserList.users.add(stock);
		
		
		
		UserList.writeToUserDatabase();
	}
}
