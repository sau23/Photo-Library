package photos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ListView;

import java.util.ArrayList;

import photos.Photos;
import classes.User;

public class AdminController {
	
	@FXML private Button add, delete, logout;
	@FXML private TextField user, pass;
	
	private ArrayList<User> users;
	private User use;
	
	public void addUser() {
		
	}
	
	public void deleteUser() {
		
	}
	
	public void logout() throws Exception{
		Photos.showLogin();
	}
	
}
