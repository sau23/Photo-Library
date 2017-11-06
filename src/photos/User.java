package photos;

import java.io.File;
import java.util.ArrayList;

/**
 * User class defines the User object that appears on the list
 * in the admin screen. Holds a name, a pass, and an
 * extendable list of albums.
 * 
 * @author Samuel Uganiza
 *
 */

public class User {

	private String name;
	private String pass;
	private ArrayList<Album> albums;
	
	/**
	 * User constructor. Takes name and pass, instantiates
	 * private list of albums to be empty.
	 * 
	 * @param name Name of user
	 * @param pass Chosen password
	 */
	
	public User(String name, String pass) {
		this.name = name;
		this.pass = pass;
		this.albums = findAlbums(name);
	}
	
	/**
	 * Finds albums for a given user name using breadth-first search. 
	 * If none are found, it will return a newly created list. 
	 * Otherwise, it will return a list of directories it has found 
	 * under the user name.
	 * 
	 * @param name The name to search albums for
	 * @return ret A list of albums found
	 */
	public ArrayList<Album> findAlbums(String name){
		
		// return list
		ArrayList<Album> ret = new ArrayList<Album>();
		
		// attempt to find directory for the given user
		File dir = new File(name);
		
		// check to see if the directory exists
		if(dir.exists()) {
			
			// get the album directories in the user directory
			File[] foundAlbums = dir.listFiles(), foundPhotos;
			Album toAdd;
			
			// create a new album for each listing found
			for(int i = 0;i < foundAlbums.length;i++) {
				
				foundPhotos = foundAlbums[i].listFiles();
				for(int j = 0;j < foundPhotos.length;j++) {
					
					// TODO: call album constructor
					toAdd = new Album(this.name);
					ret.add(toAdd);
					
				}	
			}
			
		// otherwise, make one
		} else {
			dir.mkdir();
		}
		
		return ret;
	}
	
	/**
	 * Returns name of user.
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns pass of user.
	 * @return pass
	 */
	public String getPass() {
		return this.pass;
	}
	
	/**
	 * Returns list of albums of user.
	 * @return albums
	 */
	public ArrayList<Album> getAlbums(){
		return this.albums;
	}

}
