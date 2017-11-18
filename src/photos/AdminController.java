package photos;

import java.util.Optional;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;

import photos.Photos;
import classes.UserList;
import classes.User;

/**
 * AdminController class controls the functions of buttons held in the Admin.fxml
 * and is responsible for displaying the full list of users held in the User class.
 * 
 * @author Nicholas Petriello
 * @author Samuel Uganiza
 *
 */
public class AdminController {
	
	/**
	 * FXML references to nodes in Admin.fxml.
	 */
	@FXML private Button add, delete, logout;
	@FXML private TextField user, pass;
	@FXML private ListView<User> listView = new ListView<User>();
	
	/**
	 * ObservableList that tracks the changes made in the global list of Users.
	 */
	private ObservableList<User> userList;
	
	/**
	 * Alert window for confirmation of deletion.
	 */
	private Alert alert;

	/**
	 * Instantiates the list view when switching to this controller.
	 */
	public void start() {
		userList = FXCollections.observableArrayList(UserList.users);
		if(userList.isEmpty()) {
			enableButtons(false);
		}
		listView.setItems(userList);
		listView.getSelectionModel().select(0);
	}
	
	/**
	 * Attempts to add a new User object using Strings provided in the user and
	 * pass TextFields. Will prohibit if the input username is empty or "admin".
	 * If an invalid input was provided, then no changes will occur to either the
	 * list view or the global list of Users.
	 */
	public void addUser() {
		String userName = user.getText();
		
		if(userName.isEmpty()) {
			if (Photos.DEBUG) System.out.println("Cannot add user with empty field.");
			return;
		}
		
		if(userName.equalsIgnoreCase("admin")) {
			if (Photos.DEBUG) System.out.println("Cannot add a new admin user.");
			return;
		}
		
		String passWord = pass.getText();
		
		if(userList.isEmpty()) {
			enableButtons(true);
		}
		
		if(UserList.addUser(userName, passWord)) {
			userList.add(new User(userName,passWord));
		}
	}
	
	/**
	 * Deletes a selected user and prompts an alert for confirmation.
	 */
	public void deleteUser() {
		int index = listView.getSelectionModel().getSelectedIndex();
		alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this user?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			UserList.deleteUser(index);
			userList.remove(index);
			if(userList.isEmpty()) {
				enableButtons(false);
			}
		}
	}
	
	/**
	 * Switches back to the login screen.
	 * 
	 * @throws Exception
	 */
	public void logout() throws Exception{
		Photos.showLogin();
	}
	
	/**
	 * Helper function to turn off delete button when no user is detected from
	 * reading in the database.
	 * 
	 * @param isEnabled True enables buttons, false disables buttons
	 */
	private void enableButtons(boolean isEnabled) {
		delete.setDisable(!isEnabled);
	}
}
