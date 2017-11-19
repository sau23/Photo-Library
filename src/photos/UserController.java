package photos;

import classes.Album;
import classes.Photo;
import classes.UserList;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class UserController {

	/**
	 * FXML references to scene.
	 */
	@FXML Button add, remove, caption, display, edit, copy, move, addAlbum, search, logout;
	@FXML Label userLabel;
	@FXML TabPane tabPane;
	
	private Alert alert;
	private TextInputDialog dialog;
	private int index;
	private Optional<ButtonType> result;
	
	/**
	 * Sets the private reference user to the incoming User object.
	 * 
	 * @param index Index of the logged in user
	 */
	public void setUserIndex(int index) {
		this.index = index;
		userLabel.setText(UserList.users.get(index).getName() + "'s Albums");
		setupTabPane();
		updateSearchButton();
	}
	
	/**
	 * Initializes the tab pane to hold any albums that the user at the
	 * stored index has when the database is read.
	 */
	private void setupTabPane() {
		
		// toggle buttons besides add button depending on whether the album is empty
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> obs, Tab o, Tab n) {
				if(n != null) {
					@SuppressWarnings("unchecked")
					ListView<Photo> lv = (ListView<Photo>)n.getContent();
					enableAlbumButtons(true);
					if(!lv.getItems().isEmpty()) {
						enablePhotoButtons(true);
					} else {
						enablePhotoButtons(false);
					}
				} else {
					enableAlbumButtons(false);
				}
			}
		});

		// if user has any albums, initialize tab pane to present them
		ArrayList<Album> albums = UserList.users.get(index).getAlbums();
		if(!albums.isEmpty()) {
			for(Album album : albums) {
				addNewTab(album);
			}
			tabPane.getSelectionModel().select(0);
			if(Photos.DEBUG) System.out.println("Successfully read albums for user " + UserList.users.get(index).getName() + ".");
			
		// otherwise, disable buttons
		} else {
			enableAlbumButtons(false);
			if(Photos.DEBUG) System.out.println(UserList.users.get(index).getName() + " has no stored albums.");
		}
	}

	// Photo Controls

	public void addPhoto() throws Exception {

	}
	
	/**
	 * Prompts the user with a pop-up confirmation box to delete a selected
	 * photo.
	 */
	@SuppressWarnings("unchecked")
	public void removePhoto() {
		alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this photo?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			int albumIndex = tabPane.getSelectionModel().getSelectedIndex();
			int photoIndex = ((ListView<Photo>)tabPane.getSelectionModel().getSelectedItem().getContent()).getSelectionModel().getSelectedIndex();

			// remove photo from user's album
			Album album = UserList.users.get(index).getAlbums().get(albumIndex);
			album.getPhotos().remove(photoIndex);

			// remove photo from user pool if necessary
			Photo photo = ((ListView<Photo>)tabPane.getSelectionModel().getSelectedItem().getContent()).getSelectionModel().getSelectedItem();
			UserList.users.get(index).deletePhoto(photo);

			// remove entry from list view in tab
			((ListView<Photo>)tabPane.getSelectionModel().getSelectedItem().getContent()).getItems().remove(photoIndex);
			if(Photos.DEBUG) System.out.println("Succesfully deleted " + photo.getName() + " from album " + album.toString());
		}
	}
	
	public void captionPhoto() {
		
	}
	
	/**
	 * Pops up display window for viewing all the photos in one album.
	 * 
	 * @throws Exception
	 */
	public void displayPhoto() throws Exception {
		// create new window
		@SuppressWarnings("unchecked")
		int photoIndex = ((ListView<Photo>)tabPane.getSelectionModel().getSelectedItem().getContent()).getSelectionModel().getSelectedIndex();
		if(photoIndex > -1) {
			Photos.showDisplay(index, tabPane.getSelectionModel().getSelectedIndex(), photoIndex);
		}
	}
	
	public void editPhotoTags() {
		
	}
	
	public void copyPhoto() {
		
	}
	
	public void movePhoto() {
		
	}
	
	public void search() {
		
	}
	
	/**
	 * Switches back to the login screen.
	 * 
	 * @throws Exception
	 */
	public void logout() throws Exception {
		Photos.showLogin();
	}
	
	// Album Controls

	/**
	 * Prompts user to create a new album requesting the album's name through
	 * an input text field as a pop-up dialog box. If the input album name is
	 * already taken by another album, the dialog box will prompt for a new
	 * name.
	 */
	public void addNewAlbum() {
		
		dialog = new TextInputDialog();
		dialog.setTitle("New Album");
		dialog.setHeaderText("Create a new album");
		dialog.setContentText("Enter a name for your new album:");

		// edit ok button validation
		final Button ok = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
		ok.addEventFilter(ActionEvent.ACTION,
			event -> {
				String name = dialog.getEditor().getText();
				if(name.isEmpty()) {
					dialog.getEditor().setText("");
					dialog.getEditor().setPromptText("Name cannot be empty");
					if(Photos.DEBUG) System.out.println("Album name cannot be empty.");
					event.consume();
				}
				for(Album a : UserList.users.get(index).getAlbums()) {
					if(a.toString().equals(name)) {
						dialog.getEditor().setText("");
						dialog.getEditor().setPromptText("Album already exists");
						if(Photos.DEBUG) System.out.println("Album name already exists.");
						event.consume();
					}
				}
			}
		);
		
		// show dialog box
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			
			// create new album for user
			Album album = new Album(result.get());
			UserList.users.get(index).getAlbums().add(album);
		
			// update database
			UserList.writeToUserDatabase(UserList.users.get(index));
			
			// switch to new tab as selection
			tabPane.getSelectionModel().select(addNewTab(album));
			if(Photos.DEBUG) System.out.println("Succesfully added new album " + album.toString() + ".");
		}
	}
	
	// Helper Functions
	
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
		ObservableList<Photo> photosList = FXCollections.observableArrayList(album.getPhotos());
		ListView<Photo> listView = new ListView<Photo>();
		listView.setItems(photosList);
		
		// edit list view contents
		changeCellFactory(listView);
		ret.setContent(listView);
		
		// set event handler for when item is selected on list view
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Photo>() {
			@Override
			public void changed(ObservableValue<? extends Photo> obs, Photo o, Photo n) {
				if(n != null) {
					// enable photo buttons
					enablePhotoButtons(true);
				} else {
					// disable photo buttons
					enablePhotoButtons(false);
				}
			}
		});	
		
		// set event handler for when tab is closed
		ret.setOnCloseRequest(new EventHandler<Event>() {
			@Override
			public void handle(Event e) {
				alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this album?");
				result = alert.showAndWait();
				
				// if user confirms, delete tab and album entry
				if (result.isPresent() && result.get() == ButtonType.OK) {
					int i = tabPane.getSelectionModel().getSelectedIndex();
					
					// delete photos from user's master pool if they aren't referenced else where
					for(Photo p : UserList.users.get(index).getAlbums().get(i).getPhotos()) {
						UserList.users.get(index).deletePhoto(p);
					}
					
					UserList.users.get(index).getAlbums().remove(i);
					UserList.writeToUserDatabase(UserList.users.get(index));
					updateSearchButton();

					if(Photos.DEBUG) System.out.println("Succesfully deleted album at index " + i + ".");
					
				// otherwise do nothing (consume event to prevent closing)
				} else {
					e.consume();
				}
			}
		});
		
		// add tab to tab pane
		tabPane.getTabs().add(ret);
		if(!photosList.isEmpty()) {
			listView.getSelectionModel().select(0);
		} else {
			listView.getSelectionModel().clearSelection();
		}
		return ret;
	}

	/**
	 * Helper function to turn off buttons when no albums are detected. Prevents
	 * adding photos to a non-existent album.
	 * 
	 * @param isEnabled True enables buttons, false disables buttons
	 */
	private void enableAlbumButtons(boolean isEnabled) {
		add.setDisable(!isEnabled);
		remove.setDisable(!isEnabled);
		caption.setDisable(!isEnabled);
		display.setDisable(!isEnabled);
		edit.setDisable(!isEnabled);
		copy.setDisable(!isEnabled);
		move.setDisable(!isEnabled);
	}

	/**
	 * Helper function to turn off buttons when no photos are detected. Prevents
	 * accessing non-existent photos from an empty album.
	 * 
	 * @param isEnabled True enables buttons, false disables buttons
	 */
	private void enablePhotoButtons(boolean isEnabled) {
		remove.setDisable(!isEnabled);
		caption.setDisable(!isEnabled);
		display.setDisable(!isEnabled);
		edit.setDisable(!isEnabled);
		copy.setDisable(!isEnabled);
		move.setDisable(!isEnabled);
	}
	/**
	 * Helper function to turn off search button if no photos are detected in a
	 * user's photo pool. Prevents searching for photos if a user has no albums
	 * or empty albums.
	 */
	private void updateSearchButton() {
		if(UserList.users.get(index).getPhotosPool().isEmpty()) {
			search.setDisable(true);
		} else {
			search.setDisable(false);
		}
	}
	
	/**
	 * Overrides the cell creation algorithm for the given listview to create
	 * cells with graphics instead.
	 * 
	 * @param listview
	 */
	private void changeCellFactory(ListView<Photo> listview) {
		listview.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>(){
			@Override
			public ListCell<Photo> call(ListView<Photo> list){
				return new PhotoCell();
			}
		});
	}
	
	/**
	 * Private class for the use of showing content on the listview for albums.
	 * Contains a thumbnail image scaled down to 30 by 30 pixels and the name
	 * of the file without the extension.
	 */
	private class PhotoCell extends ListCell<Photo> {
		@Override
		public void updateItem(Photo item, boolean empty) {
			super.updateItem(item, empty);
			HBox hbox = new HBox();
			ImageView image = new ImageView();
			Label label = new Label();
			File f;
			if(item != null) {
				f = new File(item.getFilePath());
				
	           	image.setFitHeight(30);
            	image.setPreserveRatio(true);
            	image.setSmooth(true);
            	image.setCache(true);
            	image.setImage(new Image(f.toURI().toString()));
                
                label.setText(item.toString());
                hbox.getChildren().addAll(image, label);
                hbox.setSpacing(10.0);
                hbox.setAlignment(Pos.CENTER_LEFT);
                setGraphic(hbox);
			}
		}
	}
}
