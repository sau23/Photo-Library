package photos;

import classes.Photo;
import classes.Tag;
import classes.User;
import classes.UserList;
import java.io.File;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
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
	@FXML private ListView<Tag> tagsList = new ListView<Tag>();
	
	private File f;
	private ObservableList<Photo> photos;
	private Photo currentPhoto;
	private User currentUser;
	
	private ObservableList<Tag> displayTags = FXCollections.observableArrayList();
	
	public void setAlbum(int userIndex, int photoIndex, ObservableList<Photo> photos) {
		this.photos = photos;
		setData(photoIndex);
		currentPhoto = photos.get(photoIndex);
		currentUser = UserList.getUser(userIndex);
	}
	
	public void prevPhoto() {
		
		if(photos.indexOf(currentPhoto) == 0){
			setData(photos.size() - 1);
			currentPhoto = photos.get(photos.size() - 1);
		}else{
			setData(photos.indexOf(currentPhoto) - 1);
			currentPhoto = photos.get(photos.indexOf(currentPhoto) - 1);
		}
	}
	
	public void nextPhoto() {
		if(photos.indexOf(currentPhoto) == photos.size() - 1){
			setData(0);
			currentPhoto = photos.get(0);
		}else{
			setData(photos.indexOf(currentPhoto) + 1);
			currentPhoto = photos.get(photos.indexOf(currentPhoto) + 1);
		}
	}
	
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
		displayTags.clear();
		if(photo.getTags().size() == 0){
			
			displayTags.add(new Tag("-Add some tags", ""));
		}else{
			
			displayTags.addAll(photo.getTags());
		}
		
		tagsList.setItems(displayTags);

		
	}
	
	public void reCaption(){
		String newCaption = captionArea.getText();
		if(newCaption == null || newCaption.compareTo("") == 0){
			captionArea.setText("");
			captionArea.setPromptText("Add a caption");
		}
		currentPhoto.setCaption(newCaption);
		UserList.writeToUserDatabase(currentUser);
		captionArea.setText(currentPhoto.getCaption());
		
	}
	
	public void addTag(){

		String name = "";
		String value = "";
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("New Tag");
		dialog.setContentText("Enter new tag name, i.e. Location, Person, etc.");
		
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()){
			name = result.get();
		}
		
		TextInputDialog newValue = new TextInputDialog(null);
		newValue.setTitle("New Tag Value");
		newValue.setContentText("Enter the value for the tag");
			
		Optional<String> newTagValue = newValue.showAndWait();
		if(newTagValue.isPresent()){
			value = newTagValue.get();
		}
		
		Tag newTag = new Tag(name, value);
		
		if(currentPhoto.getTags().size() == 0){
			displayTags.clear();
		}
		displayTags.add(newTag);
		currentPhoto.addTag(newTag);
		UserList.writeToUserDatabase(currentUser);
		tagsList.setItems(displayTags);

	}
	
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
	
	
}
