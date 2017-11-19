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
import javafx.scene.control.ChoiceDialog;
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
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class UserController {

	/**
	 * FXML references to scene.
	 */
	@FXML Button add, remove, caption, display, edit, copy, move, addAlbum, search, logout;
	@FXML Label userLabel;
	@FXML TabPane tabPane;
	
	private int index;
	
	/**
	 * Sets the private reference user to the incoming User object.
	 * 
	 * @param index Index of the logged in user
	 */
	public void setUserIndex(int index) {
		this.index = index;
		userLabel.setText(UserList.users.get(index).getName() + "'s Albums");
		setupTabPane();
		updateAlbumButtons();
		updatePhotoButtons();
		updateCopyMove();
		updateSearch();
	}
	
	/**
	 * Initializes the tab pane to hold any albums that the user at the
	 * stored index has when the database is read.
	 */
	@SuppressWarnings("unchecked")
	private void setupTabPane() {
		
		// toggle buttons besides add button depending on whether the album is empty
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> obs, Tab o, Tab n) {
				if(n != null) {
					ListView<Photo> lv = (ListView<Photo>)n.getContent();
					lv.setItems(FXCollections.observableArrayList(UserList.users.get(index).getAlbums().get(tabPane.getSelectionModel().getSelectedIndex()).getPhotos()));
				}
				updateAlbumButtons();
				updatePhotoButtons();
			}
		});

		// if user has any albums, initialize tab pane to present them
		ArrayList<Album> albums = UserList.users.get(index).getAlbums();
		if(!albums.isEmpty()) {
			for(Album album : albums) {
				addNewTab(album);
			}
			tabPane.getSelectionModel().select(0);
			if(Photos.DEBUG) System.out.println("Successfully read albums for " + UserList.users.get(index).getName() + ".");
			
		// otherwise, disable buttons
		} else {
			if(Photos.DEBUG) System.out.println(UserList.users.get(index).getName() + " has no stored albums.");
		}
	}

	// Photo Controls
	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void addPhoto() throws Exception {
		//create a file chooser instance
				FileChooser photoChooser = new FileChooser();
				photoChooser.setTitle("Choose an Image");
				
				//Set filter for images only
				//TODO need to make filters work
				FileChooser.ExtensionFilter exten = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg", "*.jpe", "*.jif", "*.jfif", ".jfi");
				photoChooser.getExtensionFilters();
				
				//get image
				File image = photoChooser.showOpenDialog(null);
				
				//set photo date
				if(image == null){
					
					return;
				}
				//TODO comment further
				System.out.println(image.getAbsolutePath());
				Photo newPhoto = new Photo(image.getAbsolutePath());
				int i = tabPane.getSelectionModel().getSelectedIndex();
				UserList.users.get(index).checkInPhotos(newPhoto, UserList.users.get(index).getAlbums().get(i));
				
				//UserList.users.get(index).getAlbums().get(i).addPhoto(newPhoto);
				
				((ListView<Photo>)tabPane.getSelectionModel().getSelectedItem().getContent()).getItems().add(newPhoto);
				UserList.writeToUserDatabase(UserList.users.get(index));
	}
	
	/**
	 * Prompts the user with a pop-up confirmation box to delete a selected
	 * photo.
	 */
	@SuppressWarnings("unchecked")
	public void removePhoto() {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this photo?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			int albumIndex = tabPane.getSelectionModel().getSelectedIndex();
			int photoIndex = ((ListView<Photo>)tabPane.getSelectionModel().getSelectedItem().getContent()).getSelectionModel().getSelectedIndex();

			// remove photo from user pool if necessary
			Photo photo = ((ListView<Photo>)tabPane.getSelectionModel().getSelectedItem().getContent()).getSelectionModel().getSelectedItem();
			UserList.users.get(index).deletePhoto(photo);

			// remove photo from user's album
			Album album = UserList.users.get(index).getAlbums().get(albumIndex);
			album.getPhotos().remove(photoIndex);
			
			// update list view in tab
			((ListView<Photo>)tabPane.getSelectionModel().getSelectedItem().getContent()).setItems(FXCollections.observableArrayList(UserList.users.get(index).getAlbums().get(albumIndex).getPhotos()));
			
			UserList.writeToUserDatabase(UserList.users.get(index));
			
			updatePhotoButtons();
			updateSearch();
			
			if(Photos.DEBUG) System.out.println("Successfully deleted " + photo.getName() + " from album " + album.toString());
		}
	}
	
	public void captionPhoto() {
		
	}
	
	/**
	 * Pops up display window for viewing all the photos in one album.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void displayPhoto() throws Exception {
		// create new window
		int photoIndex = ((ListView<Photo>)tabPane.getSelectionModel().getSelectedItem().getContent()).getSelectionModel().getSelectedIndex();
		if(photoIndex > -1) {
			Photos.showDisplay(index, tabPane.getSelectionModel().getSelectedIndex(), photoIndex);
		}
	}
	
	public void editPhotoTags() {
		
	}
	
	/**
	 * Copies the selected photo to a chosen album. The list of chosen albums
	 * is determined by the albums the user currently has available without
	 * the currently selected album. If the user has no albums, the button to
	 * copy a selected photo will not be selectable. The photo selected will
	 * remain in the chosen album and be referenced in the new album.
	 */
	@SuppressWarnings("unchecked")
	public void copyPhoto() {
		
		ArrayList<Album> albums = UserList.users.get(index).getAlbums();
		Photo photo = ((ListView<Photo>)tabPane.getSelectionModel().getSelectedItem().getContent()).getSelectionModel().getSelectedItem();
		
		// get current album name
		String curAlbum = tabPane.getSelectionModel().getSelectedItem().getText();
		
		// populate choice list for dialog box
		ArrayList<String> choices = new ArrayList<String>(UserList.users.get(index).getAlbums().size());
		for(Album a : albums) {
			
			// add new choice only if it is not the current album selected
			if(!curAlbum.equals(a.toString())) {
				choices.add(a.toString());
			}
		}

		// create choice dialog box
		ChoiceDialog<String> dialog = new ChoiceDialog<String>(choices.get(0), choices);
		dialog.setTitle("Copy Photo to Different Album");
		dialog.setHeaderText(null);
		dialog.setContentText("Choose an album to copy to:");
		
		// edit ok button validation
		final Button ok = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
		ok.addEventFilter(ActionEvent.ACTION,
			event -> {
				String name = dialog.getSelectedItem().toString();
				
				Album album = null;
				for(Album a : albums) {
					if(name.equals(a.toString())) {
						album = a;
					}
				}
				
				for(Photo p : album.getPhotos()) {
					if(photo.getName().equals(p.getName())) {
						dialog.setContentText("Album already has file of same name");
						if(Photos.DEBUG )System.out.println("Album already has file of same name");
						event.consume();
					}
				}
			}
		);
		
		// pop up dialog box
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			
			// search for input name in list of albums
			String toMove = result.get();
			for(int i = 0; i < albums.size(); i++) {

				// perform copy when you find the album
				if(toMove.equals(albums.get(i).toString())) {
					UserList.users.get(index).checkInPhotos(photo, albums.get(i));
					UserList.writeToUserDatabase(UserList.users.get(index));
					if(Photos.DEBUG) System.out.println("Successfully copied " + photo.toString() + " to " + albums.get(i).toString());
					break;
				}
			}
		}
	}
	
	/**
	 * Moves the selected photo to a chosen album. The list of chosen albums
	 * is determined by the albums the user currently has available without
	 * the currently selected album. If the user has no albums, the button
	 * to move a selected photo will not be selectable.
	 */
	@SuppressWarnings("unchecked")
	public void movePhoto() {
		
		ArrayList<Album> albums = UserList.users.get(index).getAlbums();
		Photo photo = ((ListView<Photo>)tabPane.getSelectionModel().getSelectedItem().getContent()).getSelectionModel().getSelectedItem();
		
		// get current album name
		Album curAlbum = UserList.users.get(index).getAlbums().get(tabPane.getSelectionModel().getSelectedIndex());
		
		// populate choice list for dialog box
		ArrayList<String> choices = new ArrayList<String>(UserList.users.get(index).getAlbums().size());
		for(Album a : albums) {
			
			// add new choice only if it is not the current album selected
			if(!curAlbum.toString().equals(a.toString())) {
				choices.add(a.toString());
			}
		}

		// create choice dialog box
		ChoiceDialog<String> dialog = new ChoiceDialog<String>(choices.get(0), choices);
		dialog.setTitle("Move Photo to Different Album");
		dialog.setHeaderText(null);
		
		// edit ok button validation
		final Button ok = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
		ok.addEventFilter(ActionEvent.ACTION,
			event -> {
				String name = dialog.getSelectedItem().toString();

				Album album = null;
				for(Album a : albums) {
					if(name.equals(a.toString())) {
						album = a;
					}
				}

				for(Photo p : album.getPhotos()) {
					if(photo.getName().equals(p.getName())) {
						dialog.setContentText("Album already has file of same name");
						if(Photos.DEBUG )System.out.println("Album already has file of same name");
						event.consume();
					}
				}
			}
		);
		
		// pop up dialog box
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			
			// search for input name in list of albums
			String toMove = result.get();
			for(int i = 0; i < albums.size(); i++) {
				
				// perform copy when you find the album
				if(toMove.equals(albums.get(i).toString())) {
					
					
					UserList.users.get(index).checkInPhotos(photo, albums.get(i));
					
					// remove photo from current album
					curAlbum.getPhotos().remove(photo);
					
					// remove photo from list view
					((ListView<Photo>)tabPane.getSelectionModel().getSelectedItem().getContent()).getItems().remove(photo);
					
					// update the photo buttons depending on whether the deletion was the last photo
					updatePhotoButtons();
					
					UserList.writeToUserDatabase(UserList.users.get(index));
					
					if(Photos.DEBUG) System.out.println("Successfully moved " + photo.toString() + " to " + albums.get(i).toString());
					break;
				}
			}
		}
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
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("New Album");
		dialog.setHeaderText(null);
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
			
			addNewTab(album);
			
			updateAlbumButtons();
			updatePhotoButtons();
			
			if(Photos.DEBUG) System.out.println("Successfully added new album " + album.toString() + ".");
		}
	}
	
	// Helper Functions
	
	/**
	 * Adds a new tab to the tab pane using the given album to create the tab's
	 * label and contents.
	 * 
	 * @param album The album whose contents are used in the tab creation
	 */
	private void addNewTab(Album album) {
		
		// make a new tab with given name
		Tab toAdd = new Tab(album.toString());

		// set up list view
		ObservableList<Photo> photosList = FXCollections.observableArrayList(album.getPhotos());
		ListView<Photo> listView = new ListView<Photo>();
		listView.setItems(photosList);
		
		// edit list view contents
		changeCellFactory(listView);
		toAdd.setContent(listView);
		
		// set event handler for when item is selected on list view
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Photo>() {
			@Override
			public void changed(ObservableValue<? extends Photo> obs, Photo o, Photo n) {
				updatePhotoButtons();
			}
		});
		
		// set event handler for when tab is closed
		toAdd.setOnCloseRequest(new EventHandler<Event>() {
			@Override
			public void handle(Event e) {
				Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this album?");
				Optional<ButtonType> result = alert.showAndWait();
				
				// if user confirms, delete tab and album entry
				if (result.isPresent() && result.get() == ButtonType.OK) {
					int i = tabPane.getSelectionModel().getSelectedIndex();
					
					// delete photos from user's master pool if they aren't referenced else where
					for(Photo p : UserList.users.get(index).getAlbums().get(i).getPhotos()) {
						UserList.users.get(index).deletePhoto(p);
					}
					
					UserList.users.get(index).getAlbums().remove(i);
					UserList.writeToUserDatabase(UserList.users.get(index));
					updateAlbumButtons();
					updateCopyMove();
					updateSearch();
					
					if(Photos.DEBUG) System.out.println("Successfully deleted album at index " + i + ".");
					
				// otherwise do nothing (consume event to prevent closing)
				} else {
					e.consume();
				}
			}
		});
		
		// add tab to tab pane
		tabPane.getTabs().add(toAdd);
		if(!photosList.isEmpty()) {
			listView.getSelectionModel().select(0);
		} else {
			listView.getSelectionModel().clearSelection();
		}
		
		// if there was 0 or 1 tabs, enable/disable copy and move buttons
		updateCopyMove();
	}
	
	/**
	 * Helper function to turn off buttons when no albums are detected. Prevents
	 * adding photos to a non-existent album.
	 */
	private void updateAlbumButtons() {
		if(tabPane.getTabs().isEmpty()) {
			add.setDisable(true);
			remove.setDisable(true);
			caption.setDisable(true);
			display.setDisable(true);
			edit.setDisable(true);
			copy.setDisable(true);
			move.setDisable(true);
		} else {
			add.setDisable(false);
			remove.setDisable(false);
			caption.setDisable(false);
			display.setDisable(false);
			edit.setDisable(false);
			copy.setDisable(false);
			move.setDisable(false);
		}
	}

	/**
	 * Helper function to turn off buttons when no photos are detected. Prevents
	 * accessing non-existent photos from an empty album.
	 */
	@SuppressWarnings("unchecked")
	private void updatePhotoButtons() {
		if(tabPane.getSelectionModel().getSelectedIndex() < 0) {
			return;
		}
		if(((ListView<Photo>)tabPane.getSelectionModel().getSelectedItem().getContent()).getItems().isEmpty()) {
			remove.setDisable(true);
			caption.setDisable(true);
			display.setDisable(true);
			edit.setDisable(true);
			copy.setDisable(true);
			move.setDisable(true);
		} else {
			remove.setDisable(false);
			caption.setDisable(false);
			display.setDisable(false);
			edit.setDisable(false);
			copy.setDisable(false);
			move.setDisable(false);
		}
	}
	
	/**
	 * Helper function to turn off copy and move buttons when no albums are
	 * detected. Prevents attempting to move or copy photos to non-existent
	 * albums.
	 */
	private void updateCopyMove() {
		if(tabPane.getTabs().size() < 2) {
			copy.setDisable(true);
			move.setDisable(true);
		} else {
			copy.setDisable(false);
			move.setDisable(false);
		}
	}
	
	/**
	 * Helper function to turn off search button if no photos are detected in a
	 * user's photo pool. Prevents searching for photos if a user has no albums
	 * or empty albums.
	 */
	private void updateSearch() {
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
	           	image.setFitWidth(30);
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
