package photos;

import classes.User;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

		if(name.compareTo("admin") == 0 && passWord.compareTo("admin") == 0){
			
			Photos.showAdmin();
			
		}else{
			int checkForUser = User.verifyFromDatabase(name, passWord);
			if(checkForUser == -1){
				useName.setText("Not a recognized user!");
				pass.setText("");
			}else if(checkForUser == -2){
				pass.setText("Incorrect password ");
			}else{
				Photos.showUser(checkForUser);
			}			
		}
	}
}
