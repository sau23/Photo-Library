package photos;

import classes.Photo;
import classes.UserList;

import java.io.File;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DisplayController {

	@FXML private Label photoLabel, dateField;
	@FXML private ImageView imageView;
	@FXML private Button prev, next;
	@FXML private TextArea captionArea, tagsArea;
	
	private File f;
	private ArrayList<Photo> photos;
	
	public void setAlbum(int userIndex, int albumIndex, int photoIndex) {
		photos = UserList.users.get(userIndex).getAlbums().get(albumIndex).getPhotos();
		setData(photoIndex);
	}
	
	public void prevPhoto() {
		
	}
	
	public void nextPhoto() {
		
	}
	
	private void setData(int photoIndex) {
		
		Photo photo = photos.get(photoIndex);
		
		// set image view
		f = new File(photo.getFilePath());
		imageView.setImage(new Image(f.toURI().toString()));
		
		// set label
		photoLabel.setText(photo.toString());
		
		// set date
		dateField.setText(photo.getDate());
		
		// set captions
		
		// set tags
	}
}
