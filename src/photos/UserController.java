package photos;

import classes.User;
import classes.Album;

import java.util.Optional;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserController {

	@FXML Button add, remove, caption, display, edit, copy, move, search, logout;
	@FXML Label userLabel;
	@FXML Tab newAlbumButton;
	@FXML TabPane tabPane;
	
	private Alert alert;
	private TextInputDialog dialog;
	private User user;
	private ListView<Album> listView;
	private ObservableList<Album> albumList;
	
	/**
	 * Sets the private reference user to the incoming User object.
	 * 
	 * @param user
	 */
	public void setUser(int index) {
		user = User.users.get(index);
		userLabel.setText(user.getName() + "'s Albums");
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
				
				// make a new tab with a list view
				Tab tab = new Tab(result.get());
	
				System.out.println(user.getName());
				
				// create new album for user
				ArrayList<Album> temp = user.getAlbums();
				temp.add(new Album(result.get()));
				
				// set up list view
				/*
				albumList = FXCollections.observableArrayList(user.getAlbums());
				listView = new ListView<Album>();
				listView.setItems(albumList);
	
				tab.setContent(listView);
				
				// add tab to tab pane then switch selection
				tabPane.getTabs().add(tab);
				tabPane.getSelectionModel().select(tab);
				*/
				
			}
			
		}
	}
	
	public void removeAlbum() {
		
	}
}
