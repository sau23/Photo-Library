package photos;

import classes.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

public class LoginController {

	@FXML private Button enter;
	@FXML private TextField useName, pass;
	
	/**
	 * Login controller checks if the fields correspond to a matching user in
	 * the global users list in User
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML public void login(ActionEvent event) throws Exception{
		
		String name = useName.getText();
		String passWord = pass.getText();
		
		// write and use User.verifyFromDatabase(name, pass) for checking
		if(name.compareTo("admin") == 0 && passWord.compareTo("admin") == 0){
			
			Photos.showAdmin();
			
		}else{
			int checkForUser = User.verifyFromDatabase(name, passWord);
			if(checkForUser == -2){
				useName.setText("Not a recognized user!");
				pass.setText("");
			}else if(checkForUser == -1){
				pass.setText("Incorrect password ");
			}else{
				// after getting reference call User.setUser(user) to set the current user
				// switch to user display
				Photos.showUser();
			}
			
			
			
			
		}
	}
}
