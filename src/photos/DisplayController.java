package photos;

import classes.Photo;
import classes.Tag;
import classes.User;
import classes.UserList;
import java.io.File;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * DisplayController displays each photo individually; 
 * it shows their dates in a label, captions in a textField and lists
 * the Tags in a ListView. It also gives the ability to 
 * go through all photos in an album the photo is in 
 * 
 * @author Nicholas Petriello
 * @author Samuel Uganiza
 */
public class DisplayController {

	/**
	 * FXML references to devices in the Display.fxml
	 */
	@FXML private Label photoLabel, dateField;
	@FXML private ImageView imageView;
	@FXML private Button prev, next, reCaption, addTag, deleteTag;
	@FXML private TextArea captionArea;
	@FXML private ListView<Tag> tagsList = new ListView<Tag>();
	
	/**
	 * Fields which correspond to the current photo 
	 * being displayed. 
	 */
	private File f;
	private ObservableList<Photo> photos;//pool of photos from user
	private Photo currentPhoto;
	private User currentUser;
	
	private ObservableList<Tag> displayTags = FXCollections.observableArrayList();//used to set the Listview after modifications are made
	
	/**
	 * setAlbum() establishes a direct line between the DisplayController
	 * to the photos list, current photo, and current instance of a given user.
	 * 
	 * @param userIndex current index in UserList
	 * @param photoIndex current index in Photo pool
	 * @param photos reference to the album's Photo pool
	 */
	public void setAlbum(int userIndex, int photoIndex, ObservableList<Photo> photos) {
		this.photos = photos;
		setData(photoIndex);
		currentPhoto = photos.get(photoIndex);
		currentUser = UserList.getUser(userIndex);
	}
	
	/**
	 * prevPhoto() changes the view of the Photo that is 
	 * before the current photo in the album.
	 */
	public void prevPhoto() {	
		if(photos.indexOf(currentPhoto) == 0){//check if the current photo is first in the list, goes to last
			setData(photos.size() - 1);
			currentPhoto = photos.get(photos.size() - 1);
		}else{
			setData(photos.indexOf(currentPhoto) - 1);
			currentPhoto = photos.get(photos.indexOf(currentPhoto) - 1);
		}
	}
	
	/**
	 * nextPhoto() changes the view of the Photo that is
	 * after the current photo in the album.
	 */
	public void nextPhoto() {
		if(photos.indexOf(currentPhoto) == photos.size() - 1){//check if the current photo is the last in the list, goes to first
			setData(0);
			currentPhoto = photos.get(0);
		}else{
			setData(photos.indexOf(currentPhoto) + 1);
			currentPhoto = photos.get(photos.indexOf(currentPhoto) + 1);
		}
	}
	
	/**
	 * reCaption() allows the user to make or replace a caption
	 * for a given photo.
	 */
	public void reCaption(){
		
		String newCaption = captionArea.getText();
		
		//if the given string is empty, the textField is made empty
		//and the prompt "add a caption" is set
		if(newCaption == null || newCaption.compareTo("") == 0){
			captionArea.setText("");
			captionArea.setPromptText("Add a caption");
		}
		
		currentPhoto.setCaption(newCaption);
		UserList.writeToUserDatabase(currentUser);
		captionArea.setText(currentPhoto.getCaption());
		
	}
	
	/**
	 * addTag() adds a tag to the photos pool, the album and also 
	 * to the current ListView tagsList.
	 */
	public void addTag(){

		String name = "";
		String value = "";
		
		//Dialog box to get type for new Tag
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("New Tag");
		dialog.setContentText("Enter new tag name, i.e. Location, Person, etc.");
		
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()){
			name = result.get();
		}
		
		//Dialog box to get value for new Tag
		TextInputDialog newValue = new TextInputDialog(null);
		newValue.setTitle("New Tag Value");
		newValue.setContentText("Enter the value for the tag");
			
		Optional<String> newTagValue = newValue.showAndWait();
		if(newTagValue.isPresent()){
			value = newTagValue.get();
		}
		
		Tag newTag = new Tag(name, value);
		
		//check if the photo has any tags
		if(currentPhoto.getTags().size() == 0){
			displayTags.clear();//clears "-add some tags-" tag from ObservableList
		}
		currentPhoto.addTag(newTag);
		displayTags.clear();
		UserList.writeToUserDatabase(currentUser);
		displayTags.addAll(currentPhoto.getTags());
		tagsList.setItems(displayTags);

	}
	
	/**
	 * deleteTag() removes a selected tag from a photo.
	 */
	public void deleteTag(){
		
		Tag delTag = tagsList.getSelectionModel().getSelectedItem();
		if(delTag.compareTo(new Tag("-Add some tags", "")) == 0 && currentPhoto.getTags().size() == 0){
			return;
		}
		currentPhoto.getTags().remove(delTag);
		displayTags.remove(delTag);
		if(currentPhoto.getTags().size() == 0){
			displayTags.add(new Tag("-Add some tags", ""));
		}
		tagsList.setItems(displayTags);
		
	}
	
	/**
	 * setData() sets the fields of the Display view by getting
	 * the information of a user via index.
	 * 
	 * @param photoIndex Index of current photo in a user's photo list
	 */
	private void setData(int photoIndex) {
		
		Photo photo = photos.get(photoIndex);
		
		// set image view
		f = new File(photo.getFilePath());
		imageView.setImage(new Image(f.toURI().toString()));
		
		// set label
		photoLabel.setText(photo.toString());
		
		// set date
		dateField.setText(photo.getDateString());
		
		// set captions
		if(photos.get(photoIndex).getCaption() == null || photos.get(photoIndex).getCaption().compareTo("") == 0){
			captionArea.setText("");
			captionArea.setPromptText("Add a caption");
		}else{
			captionArea.setText(photos.get(photoIndex).getCaption());
		}
		// set tags

		tagsList.getItems().clear();
		if(photo.getTags().size() == 0){
			displayTags.add(new Tag("-Add some tags", ""));
		}else{
			
			displayTags.addAll(photo.getTags());
		}
		//initialize tagsList with current photo info
		tagsList.setItems(displayTags);
		
	}
	
}
