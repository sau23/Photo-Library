package photos;

import java.util.Optional;
import javafx.fxml.FXML;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	@FXML private Button add, delete, toggle, logout;
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
		listView.setItems(userList);
		
		// default to first option in list
		if(!userList.isEmpty()) {
			listView.getSelectionModel().select(0);
		}
		
		// set up event handler for when item is selected in list view
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
			@Override
			public void changed(ObservableValue<? extends User> obs, User o, User n) {
				if(n != null) {
					// enable delete button
					enableDelete(true);
				} else {
					// disable delete button
					enableDelete(false);
				}
			}
		});
		
		if(UserList.generateStock) {
			toggle.setText("Stock User: ON");
		} else {
			toggle.setText("Stock User: OFF");
		}
	}
	
	/**
	 * Attempts to add a new User object using Strings provided in the user and
	 * pass TextFields. Will prohibit if the input username is empty or "admin".
	 * If an invalid input was provided, then no changes will occur to either the
	 * list view or the global list of Users.
	 */
	public void addUser() {
		String userName = user.getText();
		user.setText("");
		if(userName.isEmpty()) {
			user.setPromptText("Cannot be empty");
			if (Photos.DEBUG) System.out.println("Cannot add user with empty field.");
			return;
		}
		
		if(userName.equalsIgnoreCase("admin")) {
			user.setPromptText("Cannot be admin");
			if (Photos.DEBUG) System.out.println("Cannot add a new admin user.");
			return;
		}
		
		String passWord = pass.getText();

		pass.setText("");
		if(UserList.addUser(userName, passWord)) {
			userList.add(new User(userName,passWord));
		} else {
			user.setPromptText("Username taken");
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
		}
		if(!userList.isEmpty()) {
			listView.getSelectionModel().select(0);
		}
	}
	/**
	 * Toggles generation of the stock user.
	 */
	public void toggleStock() {
		UserList.generateStock = !UserList.generateStock;
		// if switched back on
		if(UserList.generateStock) {
			
			toggle.setText("Stock User: ON");
			
			// add fresh stock user to user list
			UserList.addStockUser();
			userList.add(new User("stock", ""));
			listView.getSelectionModel().select(userList.size() - 1);
			
		// if switched off
		} else {
			
			toggle.setText("Stock User: OFF");
			
			// find stock user from list and delete
			int i = UserList.verifyFromUserDatabase("stock", "");
			if(i > -1) {
				UserList.users.remove(i);
				userList.remove(i);
				if(Photos.DEBUG) System.out.println("Deleted stock user from user list.");
			}
			
			// update stock user .ser file to have null password field
			UserList.writeToUserDatabase(new User("stock", null));
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
	private void enableDelete(boolean isEnabled) {
		delete.setDisable(!isEnabled);
	}
}
