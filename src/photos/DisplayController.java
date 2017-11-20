package photos;

import classes.Photo;
import classes.Tag;
import classes.User;
import classes.UserList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DisplayController {

	@FXML private Label photoLabel, dateField;
	@FXML private ImageView imageView;
	@FXML private Button prev, next, reCaption, addTag, deleteTag;
	@FXML private TextArea captionArea;
	@FXML private ListView tagsList;
	
	private File f;
	private ArrayList<Photo> photos;
	private Photo currentPhoto;
	private User currentUser;
	
	public void setAlbum(int userIndex, int albumIndex, int photoIndex) {
		photos = UserList.users.get(userIndex).getAlbums().get(albumIndex).getPhotos();
		setData(photoIndex);
		currentPhoto = photos.get(photoIndex);
		currentUser = UserList.users.get(userIndex);
	}
	
	public void prevPhoto() {
		
	}
	
	public void nextPhoto() {
		
	}
	
	private void setData(int photoIndex) {
		
		Photo photo = photos.get(photoIndex);
		ObservableList<Tag> tags = FXCollections.observableArrayList();
		
		
		// set image view
		f = new File(photo.getFilePath());
		imageView.setImage(new Image(f.toURI().toString()));
		
		// set label
		photoLabel.setText(photo.toString());
		
		// set date
		dateField.setText(photo.getDate());
		
		// set captions
		if(photos.get(photoIndex).getCaption() == null || photos.get(photoIndex).getCaption().compareTo("") == 0){
			captionArea.setPromptText("Add a caption");
		}else{
			captionArea.setText(photos.get(photoIndex).getCaption());
		}
		// set tags
		ListView tagList = new ListView(tags);
		
	}
	
	public void reCaption(){
		String newCaption = captionArea.getText();
		if(newCaption == null || newCaption.compareTo("") == 0){
			return;
		}
		currentPhoto.setCaption(newCaption);
		UserList.writeToUserDatabase(currentUser);
		captionArea.setText(currentPhoto.getCaption());
		
	}
	
	public void addTag(){
		
		String name = "";
		String value = "";
		
		List<String> names = new ArrayList();
		names.add("Location");
		names.add("Person");
		names.add("Event");
		names.add("Custom");
		
		ChoiceDialog<String> dialog = new ChoiceDialog<>("Location", names);
		dialog.setTitle("New Tag");
		dialog.setContentText("Pick a type name for your tag");
		
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()){
			name = result.get();
			if(result.get().compareTo("Custom") == 0){
				TextInputDialog newName = new TextInputDialog(null);
				newName.setTitle("New Tag Name");
				newName.setContentText("Enter new tag name, i.e. Location, Person, etc.");
				
				Optional<String> newTagName = dialog.showAndWait();
				if(newTagName.isPresent()){
					name = newTagName.get(); 
				}
			}
			TextInputDialog newValue = new TextInputDialog(null);
			newValue.setTitle("New Tag Value");
			newValue.setContentText("Enter the value for the tag");
			
			Optional<String> newTagValue = newValue.showAndWait();
			if(newTagValue.isPresent()){
				value = newTagValue.get();
			}
		}
		
		Tag newTag = new Tag(name, value);
		
		currentPhoto.addTag(newTag);		
		UserList.writeToUserDatabase(currentUser);
		
		
	}
	
	public void deleteTag(){
		
	}
}
