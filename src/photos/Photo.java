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
	
	ArrayList<String> tags;
	Calendar date;
	
	/**
	 * 
	 * 
	 * @param addTag
	 */
	void addTag(String addTag){
		
		
	}
	
	/**
	 * deletTag() finds the tag and deletes it, if its in the list.
	 * @param delTag The tag String to be deleted
	 * @return true if it was removed; false otherwise
	 */
	boolean deleteTag(String delTag){
		
		for(int i = 0; i < this.tags.size(); i++)
			if(this.tags.remove(delTag))
				return true;
			
		return false;
	}
	

}
