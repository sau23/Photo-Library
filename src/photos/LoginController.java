package photos;

import classes.UserList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * LoginController class controls the functions of buttons held in the Login.fxml
 * and is responsible for verifying users and creating new users.
 * 
 * @author Nicholas Petriello
 * @author Samuel Uganiza
 */
public class LoginController {

	/**
	 * FXML references to nodes in Login.fxml.
	 */
	@FXML private Button enter, createAccount;
	@FXML private TextField useName, pass, newUsername, newPassword;
	
	/**
	 * Login controller checks if the fields correspond to a matching user in
	 * the global users list in UserList.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML public void login() throws Exception{
		
		String name = useName.getText();
		String passWord = pass.getText();
		
		//checks to see if user is an admin
		if(name.compareTo("admin") == 0 && passWord.compareTo("admin") == 0){
			
			//displays admin view
			Photos.showAdmin();
			
		}else{
			
			//check to see if credentials match those of a returning user
			int checkForUser = UserList.verifyFromUserDatabase(name, passWord);
			
			if(checkForUser == -1){//both username and password are wrong
				useName.setText("");
				useName.setPromptText("Not a recognized user");
				pass.setText("");
				useName.setPromptText("Please try again");
			}else if(checkForUser == -2){//password is wrong
				pass.setText("");
				pass.setPromptText("Incorrect password ");
			}else{
				//if successfully matched, show user display
				Photos.showUser(checkForUser);
			}	
			
		}
		
	}
	
	/**
	 * Attempts to create a new user if the provided user name has not yet
	 * been taken.
	 * 
	 * @throws Exception
	 */
	@FXML public void createNewUser() throws Exception{
		
		String name = newUsername.getText();
		String password = newPassword.getText();
		
		//checking for user
		int checkForUser = UserList.verifyFromUserDatabase(name, password);
		
		//can't make a new account with an empty name
		if(name.isEmpty()) {
			newUsername.setText("");
			newUsername.setPromptText("Username cannot be empty");
			return;
		}
		
		//check to see if given credentials for new
		//user are already taken or not
		if(checkForUser == -1){
			UserList.addUser(name, password);
			newUsername.setText("");
			newUsername.setPromptText("Successfully added");
			newPassword.setText("");
			newPassword.setPromptText("Now login above");
		}else{
			newUsername.setText("");
			newPassword.setText("");
			newUsername.setPromptText("User already exists");
			newPassword.setPromptText("Try another name and pass");
			
		}
		
	}
	
}
