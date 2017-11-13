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
	
	@FXML public void login(ActionEvent event) throws Exception{
		
		String name = useName.getText();
		String passWord = pass.getText();
		
		// write and use User.verifyFromDatabase(name, pass) for checking
		
		if(name.compareTo(passWord) != 0){

			Photos.showUser();
			
		}else if(name.compareTo("admin") == 0 && passWord.compareTo("admin") == 0){
			
			Photos.showAdmin();
		}
	}
}
