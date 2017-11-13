package photos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

public class LoginController {

	@FXML private Button enter;
	@FXML private TextField useName, pass;
	
	@FXML public void login(ActionEvent event) throws Exception{
		
		String name = useName.getText();
		String passWord = pass.getText();
		
		if(name.compareTo(passWord) != 0){
			Stage primaryStage = new Stage();
			Photos.root = FXMLLoader.load(getClass().getResource("/User.fxml"));
			Scene scene = new Scene(Photos.root);
		
			// read values from user data ser file if it exists
			//User.readFromDatabase();
		
			primaryStage.setScene(scene);
			primaryStage.show();
		}else if(name.compareTo("admin") == 0 && passWord.compareTo("admin") == 0){
			
			Stage primaryStage = new Stage();
			Photos.root= FXMLLoader.load(getClass().getResource("/Admin.fxml"));
			Scene scene = new Scene(Photos.root);
		
			// read values from user data ser file if it exists
			//User.readFromDatabase();
		
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		
	}

}
