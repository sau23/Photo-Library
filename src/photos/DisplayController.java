package photos;

import classes.Album;
import classes.UserList;

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
	
	private Image image;
	private Album album;
	
	public void setAlbum(int userIndex, int albumIndex) {
		album = UserList.users.get(userIndex).getAlbums().get(albumIndex);
	}
	
	public void prevPhoto() {
		
	}
	
	public void nextPhoto() {
		
	}
}
