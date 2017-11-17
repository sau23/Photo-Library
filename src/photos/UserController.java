package photos;

import classes.Album;
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


public class UserController {

	@FXML Button add, remove, caption, display, edit, copy, move, addAlbum, search, logout;
	@FXML Label userLabel;
	@FXML TabPane tabPane;
	
	private Alert alert;
	private TextInputDialog dialog;
	private int index;
	private ListView<Album> listView;
	private ObservableList<Album> albumList;
	private SingleSelectionModel<Tab> selectionModel;
	
	/**
	 * Sets the private reference user to the incoming User object.
	 * 
	 * @param index
	 */
	public void setUserIndex(int index) {
		this.index = index;
		userLabel.setText(Lists.users.get(index).getName() + "'s Albums");
		selectionModel = tabPane.getSelectionModel();
		selectionModel.clearSelection();
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
			ArrayList<Album> temp = Lists.users.get(index).getAlbums();
			temp.add(new Album(result.get()));

			// set up list view
			albumList = FXCollections.observableArrayList(Lists.users.get(index).getAlbums());
			listView = new ListView<Album>();
			listView.setItems(albumList);

			tab.setContent(listView);

			// add tab to tab pane then switch selection
			tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
			tabPane.getSelectionModel().select(tab);

		}
	}
	
	public void removeAlbum() {
		
	}
}
