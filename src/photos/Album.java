package photos;

import java.util.ArrayList;

/**
 * This class acts as a receptical for Photo objects;
 * It will also perform operations on such collections,
 * such as searches, orderings, appending, merging, and
 * deleting.
 * 
 * @author Samuel Uganiza
 * @author Nicholas Petriello
 */
public class Album {
	
	private ArrayList<Photo> photos;//the list that holds the Photos
	private String user;
	
	public Album(String name){
		
		this.photos = new ArrayList<Photo>();
		this.user = name;
	}
	/**
	 * addPhoto() inserts a Photo to the Album
	 * list.
	 * @param pic The Photo to be added
	 */
	boolean addPhoto(Photo pic){
		
		if(this.photos.contains(pic))
			return false;
		else
			this.photos.add(pic);
			return true;
			
	}
	
	/**
	 * removePhoto() searches the Album list to 
	 * find and remove the target picture.
	 * 
	 * @param pic The target Photo.
	 */
	boolean removePhoto(Photo pic){
		
		return this.photos.remove(pic);
		
	}
	
	/**
	 * movePhoto() takes a given photo and inserts it
	 * into the desAlbum; the copy in the Album list
	 * will be deleted.
	 * 
	 * @param pic The Photo to be moved
	 * @param desAlbum The Album where the Photo will go
	 */
	void movePhoto(Photo pic, Album desAlbum){
		
		this.photos.remove(pic);
		desAlbum.addPhoto(pic);
		return;
	}
	
	void copyPhoto(Photo pic, Album otherAlbum){
		
		
		return;
	}
	
	ArrayList<Photo> getPhotos(){
		
		return this.photos;
		
	}
	
	String getUser(){
		
		return this.user;
	}
	
}
