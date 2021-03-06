package classes;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Photo is an object that holds the actual
 * image and the various information about
 * it: Time and Date, tags, etc
 * 
 * @author Samuel Uganiza
 * @author Nicholas Petriello
 */
public class Photo implements Serializable{
	
	/**
	 * Autogenerated serial number
	 */
	private static final long serialVersionUID = 3063382592784005045L;

	/**
	 * Photo object's calendar, name, file path, caption and list of tags.
	 */
	private ArrayList<Tag> tags;
	private Calendar calendar;
	private String name;
	private String filePath;
	private String caption;
	
	/**
	 * Creates a photo object with the given file path. Automatically sets
	 * the photo's calendar object to use the last modified date of the
	 * given file.
	 * 
	 * @param filePath The file referenced by the photo
	 */
	public Photo(String filePath){
		File f = new File(filePath);		
		this.tags = new ArrayList<Tag>();
		this.calendar = Calendar.getInstance();
		this.calendar.set(Calendar.MILLISECOND, 0);
		this.calendar.setTimeInMillis(f.lastModified());
		this.name = f.getName();
		this.filePath = filePath;
		this.caption = "";
	}
	
	/**
	 * Returns this photo's name without an extension.
	 * 
	 * @return Photo's name
	 */
	@Override
	public String toString() {
		return this.name;
	}
	
	/**
	 * addTag() inserts addTag into the tags
	 * list of a Photo
	 * 
	 * @param addTag Tag to be added to the Photo's tag list
	 */
	public void addTag(Tag addTag) {

		if(this.tags.contains(addTag)){
			return;
		}else{
			this.tags.add(addTag);
			return;
		}
	}
	
	/**
	 * deleteTag() finds the tag and deletes it, if its in the list.
	 * 
	 * @param delTag The tag String to be deleted
	 * 
	 * @return true if it was removed; false otherwise
	 */
	public boolean deleteTag(Tag delTag){
		return this.tags.remove(delTag);
	}
	
	/**
	 * Returns this photo's list of tags.
	 * 
	 * @return Photo's tags
	 */
	public ArrayList<Tag> getTags(){
		return this.tags;
	}

	/**
	 * Returns this photo's calendar object.
	 * 
	 * @return Photo's held time
	 */
	public Calendar getCalendar(){
		return this.calendar;
	}

	/**
	 * Returns this photo's file path.
	 * 
	 * @return Photo's file path
	 */
	public String getFilePath() {
		return this.filePath;
	}
	
	/**
	 * Returns this photo's caption.
	 * 
	 * @return The photo's caption
	 */
	public String getCaption(){
		return this.caption;
	}
	
	/**
	 * Returns the photo's saved date as a string.
	 * 
	 * @return The photo's saved date as a string
	 */
	public String getDateString() {
		this.calendar.setTimeInMillis(new File(this.filePath).lastModified());
		return new SimpleDateFormat("MM-dd-yyyy").format(this.calendar.getTime());
	}
	
	/**
	 * Changes this photo's caption to the given caption.
	 * 
	 * @param newCap The new caption to set
	 */
	public void setCaption(String newCap){
		this.caption = newCap;
	}

	/**
	 * Returns whether or not this photo contains a tag with the given type and
	 * value.
	 * 
	 * @param type The type to search for
	 * @param value The value to search for
	 * 
	 * @return Whether the tag is contained in this photo
	 */
	public boolean searchTags(String type, String value) {
		Tag comp = new Tag(type, value);
		for(Tag t : this.tags) {
			if(t.compareTo(comp) == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether or not this photo's date is contained with a pair of dates.
	 * The start date and end date are converted into Date objects and are
	 * guaranteed to be in the proper format due to regex checking in the search
	 * dialog associated with searching photos by date.
	 * 
	 * @param startDate The start of the range
	 * @param endDate The end of the range
	 * 
	 * @return Whether this photo fits in the given range
	 * 
	 * @throws Exception Parse exception ignored due to proper formatting
	 */
	public boolean isWithinRange(String startDate, String endDate) throws Exception {
		// format end date to be 1 day ahead
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(endDate));
		c.add(Calendar.DATE, 1);
		return sdf.parse(startDate).compareTo(this.calendar.getTime()) * 
				this.calendar.getTime().compareTo(c.getTime()) >= 0;		
	}
	
}


