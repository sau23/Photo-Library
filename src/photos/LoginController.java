package photos;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;

public class LoginController {

	@FXML private Button enter;
	@FXML private TextField user, password;
	
	@FXML public String enterCreds(){
		
		String name = user.getText();
		String pass = password.getText();
		
		return name + pass;
		
	}
}
