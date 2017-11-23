package photos;

import classes.Album;
import classes.Photo;
import classes.User;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

/**
 * UserController class controls the functions of buttons held in the User.fxml
 * and displays the currently logged in user's albums with their respective photos
 * and options.
 * 
 * @author Nicholas Petriello
 * @author Samuel Uganiza
 */
public class UserController {

	/**
	 * FXML references to UserController.fxml.
	 */
	@FXML Button add, remove, display, copy, move, addAlbum, search, create, logout;
	@FXML Label userLabel;
	@FXML TabPane tabPane;
	
	/**
	 * Private references to the currently logged in user's data for convenience.
	 */
	private int index;
	private User user;
	private ArrayList<Album> albums;
	private ArrayList<Photo> photosPool;
	
	/**
	 * The reference the the tabPane's selector.
	 */
	private SingleSelectionModel<Tab> singleSelectionModel;
	
	/**
	 * Sets the private references for user using the given index.
	 * 
	 * @param index Index of the logged in user
	 */
	public void setUser(int index) {
		this.index = index;
		user = UserList.getUser(index);
		albums = user.getAlbums();
		photosPool = user.getPhotosPool();
		singleSelectionModel = tabPane.getSelectionModel();
		
		userLabel.setText(user.getName() + "'s Albums");
		
		// check to see if any photos cannot be linked to properly
		ObservableList<String> errorList = FXCollections.observableArrayList(user.checkPhotosPool());
		if(!errorList.isEmpty()) {
			if(Photos.DEBUG) {
				System.out.println("Photos cannot be found at given file paths:");
				for(String s : errorList) {
					System.out.println(s);
				}
			}
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Photo Loading Error");
			alert.setHeaderText(null);
			alert.setGraphic(null);
			alert.setContentText("Could not find photos: ");
			ListView<String> lv = new ListView<String>();
			lv.setItems(errorList);
			lv.setMouseTransparent(true);
			alert.getDialogPane().setExpandableContent(lv);
			alert.getDialogPane().setExpanded(true);
			alert.showAndWait();
		}
		
		setupTabPane();
		updateAlbumButtons();
		updatePhotoButtons();
		updateCopyMove();
		updateSearch();
	}
	
	// Photo Controls
	
	/**
	 * Adds a photo by popping up a file choosing window.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void addPhoto() throws Exception {
		
		//create a file chooser instance
		FileChooser photoChooser = new FileChooser();
		photoChooser.setTitle("Choose an Image");
			
		//Set filter for images only
		photoChooser.getExtensionFilters().addAll( 
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg", "*.jpe", "*.jif", "*.jfif", ".jfi"));
				
		//get image
		File image;
		try{
			image = photoChooser.showOpenDialog(null);
		}catch(Exception e){
			image = null;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("File Error");
			alert.setContentText("Invalid file: can't make a shortcut an image.");
			alert.showAndWait();
		}
				
		// check if image exists
		if(image == null){		
			return;
		}
		if(Photos.DEBUG) System.out.println(image.getAbsolutePath());

		//create photo instance
		Photo newPhoto = new Photo(image.getAbsolutePath());

		//check if photo exists in album already
		int albumIndex = singleSelectionModel.getSelectedIndex();
		for(Photo p : user.getAlbums().get(albumIndex).getPhotos()) {
			if(newPhoto.toString().equals(p.toString())) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Error Adding Photo");
				alert.setHeaderText(null);
				alert.setContentText("Album already contains same file.");
				alert.showAndWait();
				if(Photos.DEBUG) System.out.println("File of same path already exists in album.");
				return;
			}
		}
		
		//check if there's a duplicate in the pool; if not the photo is added to the pool
		user.checkInPhotos(newPhoto, albums.get(albumIndex));

		//User .ser file is updated for the selected user.
		((ListView<Photo>)singleSelectionModel.getSelectedItem().getContent()).getItems().add(newPhoto);
		((ListView<Photo>)singleSelectionModel.getSelectedItem().getContent()).getSelectionModel().select(newPhoto);
		UserList.writeToUserDatabase(user);
		
		// update buttons
		updatePhotoButtons();
		updateCopyMove();
		updateSearch();
		if(Photos.DEBUG) System.out.println("Successfully added " + newPhoto.toString() + " to " + singleSelectionModel.getSelectedItem().getText());
	}
	
	/**
	 * Prompts the user with a pop-up confirmation box to delete a selected
	 * photo.
	 */
	@SuppressWarnings("unchecked")
	public void removePhoto() {
		int albumIndex = singleSelectionModel.getSelectedIndex();
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this photo?");
		alert.setHeaderText(null);
		alert.setGraphic(null);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			
			ListView<Photo> lv = (ListView<Photo>)singleSelectionModel.getSelectedItem().getContent();

			// remove photo from user pool if necessary
			Photo photo = lv.getSelectionModel().getSelectedItem();
			user.deletePhoto(photo);

			// remove photo from user's album
			Album album = albums.get(albumIndex);
			album.removePhoto(photo);
			
			// update list view in tab
			lv.setItems(FXCollections.observableArrayList(albums.get(albumIndex).getPhotos()));
			
			// update selection after delete
			if(!albums.get(albumIndex).getPhotos().isEmpty()) {
				lv.getSelectionModel().select(0);
			}
			
			UserList.writeToUserDatabase(user);
			
			updatePhotoButtons();
			updateCopyMove();
			updateSearch();
			
			if(Photos.DEBUG) System.out.println("Successfully deleted " + photo.toString() + " from album " + album.toString());
		}
	}
	
	/**
	 * Pops up display window for viewing all the photos in one album and
	 * sets the display to be the currently selected photo.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void displayPhoto() throws Exception {		
		int photoIndex = ((ListView<Photo>)singleSelectionModel.getSelectedItem().getContent()).getSelectionModel().getSelectedIndex();
		if(photoIndex > -1) {
			ObservableList<Photo> photos = ((ListView<Photo>)singleSelectionModel.getSelectedItem().getContent()).getItems();
			Stage window = Photos.showDisplay(index, photoIndex, photos);
			window.focusedProperty().addListener(new ChangeListener<Boolean>()
			{
			  @Override
			  public void changed(ObservableValue<? extends Boolean> obs, Boolean o, Boolean n)
			  {
			    if(o) {
			    	// redraw the current opened tab
			    	((ListView<Photo>)singleSelectionModel.getSelectedItem().getContent()).getItems();
			    	
			    	// get currently selected tab
			    	Tab curTab = singleSelectionModel.getSelectedItem();
			    	
			    	// create a new listview
			    	ListView<Photo> nlv = new ListView<Photo>();
					changeCellFactory(nlv);
					
					// set items in currently selected tab
					nlv.setItems(FXCollections.observableArrayList(((ListView<Photo>)curTab.getContent()).getItems()));
					curTab.setContent(nlv);
			    }
			  }
			});
			window.show();
		}
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
		
		Photo photo = ((ListView<Photo>)singleSelectionModel.getSelectedItem().getContent()).getSelectionModel().getSelectedItem();
		
		// get current album name
		String curAlbum = singleSelectionModel.getSelectedItem().getText();
		
		// populate choice list for dialog box
		ArrayList<String> choices = new ArrayList<String>(albums.size());
		for(Album a : albums) {
			
			// add new choice only if it is not the current album selected
			if(!curAlbum.equals(a.toString())) {
				choices.add(a.toString());
			}
		}

		// create choice dialog box
		ChoiceDialog<String> dialog = new ChoiceDialog<String>(choices.get(0), choices);
		dialog.setTitle("Copy Photo to Different Album");
		dialog.setContentText("Choose an album to copy photo to:");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		
		// edit ok button validation
		final Button ok = (Button)dialog.getDialogPane().lookupButton(ButtonType.OK);
		ok.addEventFilter(ActionEvent.ACTION,
			event -> {
				
				// find the album to add the photo to
				String name = dialog.getSelectedItem().toString();
				Album album = null;
				for(Album a : albums) {
					if(name.equals(a.toString())) {
						album = a;
					}
				}
				
				// check if the album already has a photo of same name
				for(Photo p : album.getPhotos()) {
					if(photo.toString().equals(p.toString())) {
						dialog.setContentText("Album already has same file");
						if(Photos.DEBUG )System.out.println("Album already has same file");
						event.consume();
					}
				}
			}
		);
		
		// pop up dialog box
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			
			// search for input name in list of albums
			String toCopy = result.get();
			for(int i = 0; i < albums.size(); i++) {

				// perform copy when you find the album
				if(toCopy.equals(albums.get(i).toString())) {
					user.checkInPhotos(photo, albums.get(i));
					UserList.writeToUserDatabase(user);
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
		
		Photo photo = ((ListView<Photo>)singleSelectionModel.getSelectedItem().getContent()).getSelectionModel().getSelectedItem();
		
		// get current album reference
		Album curAlbum = albums.get(singleSelectionModel.getSelectedIndex());
		
		// populate choice list for dialog box
		ArrayList<String> choices = new ArrayList<String>(albums.size());
		for(Album a : albums) {
			
			// add new choice only if it is not the current album selected
			if(!curAlbum.toString().equals(a.toString())) {
				choices.add(a.toString());
			}
		}

		// create choice dialog box
		ChoiceDialog<String> dialog = new ChoiceDialog<String>(choices.get(0), choices);
		dialog.setTitle("Move Photo to Different Album");
		dialog.setContentText("Choose an album to move photo to:");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		
		// edit ok button validation
		final Button ok = (Button)dialog.getDialogPane().lookupButton(ButtonType.OK);
		ok.addEventFilter(ActionEvent.ACTION,
			event -> {
				
				// find the album to add the photo to
				String name = dialog.getSelectedItem().toString();
				Album album = null;
				for(Album a : albums) {
					if(name.equals(a.toString())) {
						album = a;
					}
				}

				// check if the album already has a photo of the same name
				for(Photo p : album.getPhotos()) {
					if(photo.toString().equals(p.toString())) {
						dialog.setContentText("Album already has same file");
						if(Photos.DEBUG )System.out.println("Album already has same file");
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
					
					user.checkInPhotos(photo, albums.get(i));
					
					// remove photo from current album
					curAlbum.removePhoto(photo);
					
					// remove photo from list view
					((ListView<Photo>)singleSelectionModel.getSelectedItem().getContent()).getItems().remove(photo);
					
					// update the photo buttons depending on whether the deletion was the last photo
					updatePhotoButtons();
					
					UserList.writeToUserDatabase(user);
					
					if(Photos.DEBUG) System.out.println("Successfully moved " + photo.toString() + " to " + albums.get(i).toString());
					break;
				}
			}
		}
	}
	
	/**
	 * Opens a dialog that prompts the user to choose which type of search to 
	 * commence. After selecting, another dialog box will pop up prompting the
	 * user for two fields depending on their desired search method. Then, the
	 * results of the search will be displayed as a new tab on the tab pane,
	 * but will not count towards the user's list of albums. This tab cannot
	 * be manipulated unless it is added to the user's list of albums, but
	 * photos from the tab can still be displayed normally. 
	 */
	public void search() throws Exception {
		
		// create initial dialog 
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Search in Albums");
		alert.setHeaderText(null);
		alert.setGraphic(null);
		alert.setContentText("Choose your search option.");
		ButtonType buttonTypeOne = new ButtonType("Date Range");
		ButtonType buttonTypeTwo = new ButtonType("Tag/Value");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

		// pop up dialog box
		Optional<ButtonType> result = alert.showAndWait();
		ArrayList<Photo> toDisplay = new ArrayList<Photo>();
		Pair<String, String> response;
		
		// switch to new dialog depending on output
		if (result.get() == buttonTypeOne){
		    response = createDialog(true);
		    if(response != null) {
		    	String startDate = response.getKey();
		    	String endDate = response.getValue();
		    	for(Photo p : photosPool) {
		    		
		    		// only choose photos that are in range
		    		if(p.isWithinRange(startDate, endDate)) {
		    			toDisplay.add(p);
		    			if(Photos.DEBUG) System.out.println("Added " + p.toString() + " to search list.");
		    		}
		    	}
		    }
		} else if (result.get() == buttonTypeTwo) {
			
			ArrayList<Pair<String, String>> results = new ArrayList<Pair<String, String>>();
			
			// create an initial dialog
			response = createDialog(false);
			if(response != null) {
				results.add(response);
			} else {
				return;
			}
			
			// see if the user wants to add any more tags
			while(response != null) {
				response = createDialog(false);
				if(response != null) {
					results.add(response);
				}
			}
			
			// for every tag added, do a recursive-like check on resulting lists
			toDisplay = null;
			ArrayList<Photo> listToAddTo;
			for(Pair<String, String> p : results) {
				if(toDisplay == null) {
					toDisplay = user.getPhotosPool();
				}
				listToAddTo = new ArrayList<Photo>();
				
				for(Photo photo : toDisplay) {
					if(photo.searchTags(p.getKey(), p.getValue())) {
						listToAddTo.add(photo);
					}
				}
				toDisplay = listToAddTo;
			}
			// creates a list of photos who has all the given tags
		}
		
		// if results did not come up empty
		if(!toDisplay.isEmpty()) {
			Tab searchResults = new Tab("Search");
			searchResults.setId("");
			ListView<Photo> lv = new ListView<Photo>();
			changeCellFactory(lv);
			ObservableList<Photo> photos = FXCollections.observableArrayList(toDisplay);
			lv.setItems(photos);
			searchResults.setContent(lv);
			
			// check to see if search tab already exists
			int check = tabPane.getTabs().size() - 1;
			if(check > 0 && tabPane.getTabs().get(check).getId() != null) {
				tabPane.getTabs().remove(check);
				if(Photos.DEBUG) System.out.println("Removed search tab.");
			}
			
			tabPane.getTabs().add(searchResults);
			singleSelectionModel.select(searchResults);
			add.setDisable(true);
			remove.setDisable(true);
			copy.setDisable(true);
			move.setDisable(true);
			
			create.setDisable(false);
		}
	}

	/**
	 * Creates a new album based on the search results.
	 */
	@SuppressWarnings("unchecked")
	public void create() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("New Album");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
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
				for(Album a : albums) {
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

			// get the search results
			ObservableList<Photo> searchResults = ((ListView<Photo>)singleSelectionModel.getSelectedItem().getContent()).getItems();
			
			// force a switch to the last album
			singleSelectionModel.select(albums.size() - 1);
			
			// create new album for user based on the results of the search
			Album album = new Album(result.get());
			albums.add(album);
			for(Photo p : searchResults) {
				album.addPhoto(p);
			}
		
			// update database
			UserList.writeToUserDatabase(user);
				
			addNewTab(album);
			
			// force a switch to the newly added album
			singleSelectionModel.select(albums.size() - 1);

			updateAlbumButtons();
			updatePhotoButtons();
			
			if(Photos.DEBUG) System.out.println("Successfully added new album " + album.toString() + ".");
		}
	}
	
	/**
	 * Switches back to the login screen.
	 * 
	 * @throws Exception Throws IOException
	 */
	public void logout() throws Exception {
		Photos.showLogin();
	}

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
		dialog.setGraphic(null);
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
				for(Album a : albums) {
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
			
			// check if search tab exists
			int check = tabPane.getTabs().size() - 1;
			if(check > 0 && tabPane.getTabs().get(check).getId() != null) {
				// force a switch to the last album
				singleSelectionModel.select(albums.size() - 1);
				if(Photos.DEBUG) System.out.println("Removed search tab.");
			}
			
			// create new album for user
			Album album = new Album(result.get());
			albums.add(album);
		
			// update database
			UserList.writeToUserDatabase(user);
				
			addNewTab(album);
			
			updateAlbumButtons();
			updatePhotoButtons();
			
			if(Photos.DEBUG) System.out.println("Successfully added new album " + album.toString() + ".");
		}
	}
	
	// Helper Functions
	
	/**
	 * Initializes the tab pane to hold any albums that the user at the
	 * stored index has when the database is read.
	 */
	private void setupTabPane() {
		
		// toggle buttons besides add button depending on whether the album is empty
		singleSelectionModel.selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> obs, Tab o, Tab n) {
				if(n != null) {
					
					// check if search tab exists
					if(o != null && o.getId() != null && tabPane.getTabs().size() != albums.size()) {
						tabPane.getTabs().remove(albums.size());
						create.setDisable(true);
						if(Photos.DEBUG) System.out.println("Removed search tab.");
					}	

					int albumIndex = singleSelectionModel.getSelectedIndex();
					if(albumIndex < albums.size()) {
						ListView<Photo> nlv = new ListView<Photo>();
						changeCellFactory(nlv);
						nlv.setItems(FXCollections.observableArrayList(albums.get(albumIndex).getPhotos()));
						n.setContent(nlv);
						if(!albums.get(albumIndex).getPhotos().isEmpty()) {
							nlv.getSelectionModel().select(0);
						} else {
							updateCopyMove();
						}
					}

				}
				updateAlbumButtons();
				updatePhotoButtons();
			}
		});

		// if user has any albums, initialize tab pane to present them
		if(!albums.isEmpty()) {
			for(Album a : albums) {
				addNewTab(a);
			}
			singleSelectionModel.select(0);
			if(Photos.DEBUG) System.out.println("Successfully read albums for " + user.getName() + ".");
			
		// otherwise, disable buttons
		} else {
			if(Photos.DEBUG) System.out.println(user.getName() + " has no stored albums.");
		}
	}
	
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
				updateCopyMove();
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
					int i = singleSelectionModel.getSelectedIndex();
					
					// delete photos from user's master pool if they aren't referenced else where
					for(Photo p : albums.get(i).getPhotos()) {
						user.deletePhoto(p);
					}
					
					albums.remove(i);
					UserList.writeToUserDatabase(user);
					
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
		
		toAdd.setOnClosed(new EventHandler<Event>() {
			@Override
			public void handle(Event e) {
				updateAlbumButtons();
				updatePhotoButtons();
				updateCopyMove();
				updateSearch();
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
			display.setDisable(true);
			copy.setDisable(true);
			move.setDisable(true);
		} else {
			add.setDisable(false);
			remove.setDisable(false);
			display.setDisable(false);
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
		if(singleSelectionModel.getSelectedIndex() < 0) {
			return;
		}
		if(((ListView<Photo>)singleSelectionModel.getSelectedItem().getContent()).getItems().isEmpty()) {
			remove.setDisable(true);
			display.setDisable(true);
			copy.setDisable(true);
			move.setDisable(true);
		} else {
			remove.setDisable(false);
			display.setDisable(false);
			copy.setDisable(false);
			move.setDisable(false);
		}
	}
	
	/**
	 * Helper function to turn off copy and move buttons when no albums are
	 * detected. Prevents attempting to move or copy photos to non-existent
	 * albums.
	 */
	@SuppressWarnings("unchecked")
	private void updateCopyMove() {
		if(albums.size() < 2 || ((ListView<Photo>)singleSelectionModel.getSelectedItem().getContent()).getItems().isEmpty()) {
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
		if(user.getPhotosPool().isEmpty()) {
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
			Label name = new Label();
			Label caption = new Label();
			File f;
			if(item != null) {
				f = new File(item.getFilePath());

	           	image.setFitHeight(30);
	           	image.setFitWidth(30);
				image.setPreserveRatio(true);
            	image.setSmooth(true);
            	image.setCache(true);
            	image.setImage(new Image(f.toURI().toString()));
                
                name.setText(UserList.removeExtension(item.toString()));
                caption.setText(item.getCaption());
                
                hbox.getChildren().addAll(image, name, caption);
                hbox.setSpacing(10.0);
                hbox.setAlignment(Pos.CENTER_LEFT);
                setGraphic(hbox);
			} else {
				setDisable(false);
				setGraphic(null);
			}
		}
	}
	
	/**
	 * Creates a custom dialog depending on the type given. If the type is true,
	 * this will create a search by date range dialog that will return a range of
	 * dates as a pair. If the type is false, this create a search by tag/value
	 * dialog that will return a tag and value as a pair.
	 * 
	 * @param isDateRange Determines whether a date-range or tag/value dialog will
	 * 					  is created
	 * 
	 * @return The the inputs as a pair of strings
	 */
	private Pair<String, String> createDialog(boolean isDateRange) {
		
		// set up dialog settings
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		String title = isDateRange ? "Search Albums by Date Range" : "Search Albums by Tag";
		dialog.setTitle(title);
		
		// set up text fields
		TextField text1 = new TextField();
		TextField text2 = new TextField();
		String prompt1 = isDateRange ? "MM-DD-YYYY" : "Tag type";
		String prompt2 = isDateRange ? "MM-DD-YYYY" : "Tag value";
		text1.setPromptText(prompt1);
		text2.setPromptText(prompt2);
		
		// if is date range, add listeners to ensure date format is correct
		if(isDateRange) {
			((Button)dialog.getDialogPane().lookupButton(ButtonType.OK)).setDisable(true);
			String regex = "([1-9]|0[1-9]|1[0-2])-([1-9]|0[1-9]|1[0-9]|2[0-9]|3[0-1])-([1-2][0-9][0-9][0-9])";
			text1.textProperty().addListener((obs, o, n) -> {
			    if(n.trim().matches(regex) && text2.getText().matches(regex)) {
			    	((Button)dialog.getDialogPane().lookupButton(ButtonType.OK)).setDisable(false);
			    } else {
			    	((Button)dialog.getDialogPane().lookupButton(ButtonType.OK)).setDisable(true);
			    }
			});
			text2.textProperty().addListener((obs, o, n) -> {
				if(n.trim().matches(regex) && text1.getText().matches(regex)) {
					((Button)dialog.getDialogPane().lookupButton(ButtonType.OK)).setDisable(false);
			    } else {
			    	((Button)dialog.getDialogPane().lookupButton(ButtonType.OK)).setDisable(true);
			    }
			});
			
		// otherwise you can ignore the type of input
		}
		
		// set up grid content of dialog
		GridPane grid = new GridPane();
		grid.setHgap(20);
		Label label1 = isDateRange ? new Label("Start date:") : new Label("Enter tag type:");
		Label label2 = isDateRange ? new Label("End date:") : new Label("Enter tag value:");
		grid.add(label1, 0, 0);
		grid.add(text1, 1, 0);
		grid.add(label2, 0, 1);
		grid.add(text2, 1, 1);
		dialog.getDialogPane().setContent(grid);
		
		// set format of result after the OK button has been pressed
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.OK) {
		        return new Pair<>(text1.getText(), text2.getText());
		    }
		    return null;
		});
		
		// pop up dialog box
		Optional<Pair<String, String>> result = dialog.showAndWait();
		Pair<String, String> ret = null;
		if(result.isPresent()) {
			ret = result.get();
		}
		
		return ret;
	}
}
