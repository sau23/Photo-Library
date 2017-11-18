package photos;

import classes.Album;
import classes.Photo;
import classes.Lists;

import java.util.Optional;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


public class UserController {

	@FXML Button add, remove, caption, display, edit, copy, move, addAlbum, search, logout;
	@FXML Label userLabel;
	@FXML TabPane tabPane;
	
	private Alert alert;
	private TextInputDialog dialog;
	private int index;
	private ListView<Photo> listView;
	private ObservableList<Photo> albumList;
	
	/**
	 * Sets the private reference user to the incoming User object.
	 * 
	 * @param index Index of the logged in user
	 */
	public void setUserIndex(int index) {
		this.index = index;
		userLabel.setText(Lists.users.get(index).getName() + "'s Albums");
		setupAlbumTabs();
	}
	
	/**
	 * Initializes the tab pane to hold any albums that the user at the
	 * stored index has when the database is read.
	 */
	private void setupAlbumTabs() {
		
		// if user has any albums, initalize tab pane to present them
		ArrayList<Album> albums = Lists.users.get(index).getAlbums();
		if(!albums.isEmpty()) {
			for(Album album : albums) {
				addNewTab(album);
			}
			tabPane.getSelectionModel().select(0);
			if(Photos.DEBUG) System.out.println("Sucessfully read albums for user " + Lists.users.get(index).getName() + ".");
			
		// otherwise, do nothing
		} else {
			if(Photos.DEBUG) System.out.println(Lists.users.get(index).getName() + " has no stored albums.");
		}
	}

	// Photo Controls
	public void addPhoto() {
		
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
	
	/**
	 * Prompts user to create a new album requesting the album's name through
	 * an input text field as a pop-up dialog box.
	 */
	public void addNewAlbum() {
		
		dialog = new TextInputDialog();
		dialog.setTitle("New Album");
		dialog.setHeaderText("Create a new album");
		dialog.setContentText("Enter a name for your new album:");

		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {

			// create new album for user
			Album album = new Album(result.get());
			Lists.users.get(index).getAlbums().add(album);
		
			// update database
			Lists.writeToDatabase();
			
			// switch to new tab as selection
			tabPane.getSelectionModel().select(addNewTab(album));
			if(Photos.DEBUG) System.out.println("Succesfully added new album " + album.toString() + ".");
		}
	}
	
	/**
	 * Adds a new tab to the tab pane using the given album to create the tab's
	 * label and contents.
	 * 
	 * @param album The album whose contents are used in the tab creation
	 * @return A reference to the newly created album for selection purposes
	 */
	private Tab addNewTab(Album album) {
		// make a new tab with given name
		Tab ret = new Tab(album.toString());

		// set up list view
		// TODO: create thumbnails for each entry
		ObservableList<Photo> photosList = FXCollections.observableArrayList(album.getPhotos());
		listView = new ListView<Photo>();
		listView.setItems(photosList);
		ret.setContent(listView);

		// set event handler for when tab is closed
		ret.setOnCloseRequest(new EventHandler<Event>() {
			@Override
			public void handle(Event e) {
				int i = tabPane.getSelectionModel().getSelectedIndex();
				Lists.users.get(index).getAlbums().remove(i);
				Lists.writeToDatabase();
				if(Photos.DEBUG) System.out.println("Succesfully deleted album at index " + i + ".");
			}
		});

		// add tab to tab pane
		tabPane.getTabs().add(ret);
		return ret;
	}
}
