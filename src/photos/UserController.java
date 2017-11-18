package photos;

import classes.Album;
import classes.Photo;
import classes.Lists;

import java.util.Optional;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;


public class UserController {

	@FXML Button add, remove, caption, display, edit, copy, move, addAlbum, search, logout, findFromComputerButton, addNewSongButton;
	@FXML Label userLabel;
	@FXML TabPane tabPane;
	@FXML TextField newSongField, newSongDirectory;
	
	private Alert alert;
	private TextInputDialog dialog;
	private int index;
	private ListView<Photo> listView;
	private ObservableList<Photo> albumList;
	
	/**
	 * Sets the private reference user to the incoming User object.
	 * 
	 * @param index
	 */
	public void setUserIndex(int index) {
		this.index = index;
		userLabel.setText(Lists.users.get(index).getName() + "'s Albums");
		setupAlbumTabs();
	}
	
	private void setupAlbumTabs() {
		// TODO: add thumbnails to listview
		ArrayList<Album> albums = Lists.users.get(index).getAlbums();
		if(!albums.isEmpty()) {
			ObservableList<Tab> tabs = tabPane.getTabs();
			for(Album album : albums) {
				Tab tab = new Tab(album.toString());
				tab.setContent(listView);
			}
		}
	}

	// Photo Controls
	public void addPhoto() throws Exception {
		
		Photos.showNewSong();
		
	}
	
	public void removePhoto() {
		
	}
	
	public void captionPhoto() {
		
	}
	
	public void displayPhoto() {
		// create new window
		
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
	
	public void logout() throws Exception {
		Photos.showLogin();
	}
	
	// Album Controls
	
	public void switchAlbum() {
		
	}
	
	public void addNewAlbum() {
		
		dialog = new TextInputDialog();
		dialog.setTitle("New Album");
		dialog.setHeaderText("Create a new album");
		dialog.setContentText("Enter a name for your new album:");

		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {

			// make a new tab with a list view
			Tab tab = new Tab(result.get());
			tab.setClosable(true);

			// create new album for user
			Album album = new Album(result.get());
			Lists.users.get(index).getAlbums().add(album);

			// set up list view
			ObservableList<Photo> photosList = FXCollections.observableArrayList(album.getPhotos());
			
			// TODO: add thumbnails to listview
			listView = new ListView<Photo>();
			listView.setItems(photosList);
			tab.setContent(listView);
			
			photosList.add(new Photo(null, "Hello", 0));

			Lists.writeToDatabase();
			
			// add tab to tab pane then switch selection
			tabPane.getTabs().add(tab);
			tabPane.getSelectionModel().select(tab);
		}
	}
	
	public void removeAlbum() {
		
	}
	
	public void fileFind(){
		
	}
	
	public void addNewPhoto() throws Exception{
		
		Photos.showUser(this.index);
	}
}
