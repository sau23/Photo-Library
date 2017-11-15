package photos;

import classes.User;
import classes.Album;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserController {

	@FXML Button add, remove, caption, display, edit, copy, move, search, logout;
	@FXML Label userLabel;
	@FXML Tab newAlbumButton;
	@FXML TabPane tabPane;
	
	Alert alert;
	TextInputDialog dialog;
	User user;
	Album album;
	
	public void setUser(User user) {
		this.user = user;
	}
	
	// Photo Controls
	public void addPhoto() {
		
	}
	
	public void removePhoto() {
		
	}
	
	public void captionPhoto() {
		
	}
	
	public void displayPhoto() {
		
	}
	
	public void editPhotoTags() {
		
	}
	
	public void copyPhoto() {
		
	}
	
	public void movePhoto() {
		
	}
	
	private void disableButtons() {
		
	}
	
	private void enableButtons() {
		
	}
	
	public void search() {
		
	}
	
	public void logout() {

	}
	
	// Album Controls
	
	public void switchAlbum() {
		
	}
	
	public void addAlbum() {
		if(newAlbumButton.isSelected()) {
			dialog = new TextInputDialog();
			dialog.setTitle("New Album");
			dialog.setHeaderText("Create a new album");
			dialog.setContentText("Enter a name for your new album:");
			
			Optional<String> result = dialog.showAndWait();
			if(result.isPresent()) {
				Tab tab = new Tab(result.get());
				
			}
			
		}
	}
	
	public void removeAlbum() {
		
	}
}
