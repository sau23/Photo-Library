package photos;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;

import java.util.ArrayList;

import classes.User;

public class AdminController {
	
	@FXML private Button add, delete, logout;
	@FXML private TextField user, pass;
	
	private ArrayList<User> users;
	private User use;
	
	
}
