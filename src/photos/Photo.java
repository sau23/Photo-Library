package photos;
import java.util.Calendar;
import java.util.ArrayList;

/**
 * Photo is an object that holds the actual
 * image and the various information about
 * it: Time and Date, tags, etc
 * 
 * @author Samuel Uganiza
 * @author Nicholas Petriello
 */
public class Photo {
	
	ArrayList<Tag> tags;
	Calendar date;
	String name;
	
	public Photo(Calendar photoDate, String photoName){
		
		this.tags = new ArrayList<Tag>();
		this.date = photoDate;
		this.name = photoName;
		
	}
	
	/**
	 * addTag() inserts a given addTag into the tags
	 * list of a Photo
	 * @param addTag Tag to be added to the Photo's tag list
	 * @return true if the add was successful, false otherwise
	 */
	boolean addTag(Tag addTag){
		
		if(this.tags.contains(addTag))
			return false;
		else
			this.tags.contains(addTag);
			return true;
		
	}
	
	/**
	 * deletTag() finds the tag and deletes it, if its in the list.
	 * @param delTag The tag String to be deleted
	 * @return true if it was removed; false otherwise
	 */
	boolean deleteTag(Tag delTag){
		
		return this.tags.remove(delTag);
		
	}
	

}


