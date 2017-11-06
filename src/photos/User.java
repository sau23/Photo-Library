package photos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * User class defines the User object that appears on the list
 * in the admin screen. Holds a name, a pass, and an
 * extendable list of albums.
 * 
 * @author Samuel Uganiza
 *
 */

public class User {

	public static ArrayList<User> users;
	
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
		File dir = new File("data/" + name);
		
		// check to see if the directory exists
		if(dir.exists()) {
			
			// get the album directories in the user directory
			File[] foundAlbums = dir.listFiles(), foundPhotos;
			Album albumToAdd;
			
			// create a new album for each listing found
			for(int i = 0;i < foundAlbums.length;i++) {
				
				albumToAdd = new Album(foundAlbums[i].getName());
				foundPhotos = foundAlbums[i].listFiles();
				
				for(int j = 0;j < foundPhotos.length;j++) {

					// create new Calendar for photo
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date(foundPhotos[j].lastModified()));

					// add photo to album
					albumToAdd.addPhoto(new Photo(cal, foundPhotos[j].getName()));
				}	
				
				// add album to returning list
				ret.add(albumToAdd);
			}
			
			if(Photos.DEBUG) System.out.println("Successfully found albums for user " + name);
			
		// otherwise, make one
		} else {
			
			// prohibit creation of an admin folder since admin will not actually hold photos
			if(!name.equalsIgnoreCase("admin")) {
				if(Photos.DEBUG) System.out.println("No albums found for user " + name + ", creating new directory");
				dir.mkdir();
			}
		}
		
		return ret;
	}
	
	/**
	 * Returns name of user.
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns pass of user.
	 * 
	 * @return pass
	 */
	public String getPass() {
		return this.pass;
	}
	
	/**
	 * Returns list of albums of user.
	 * 
	 * @return albums
	 */
	public ArrayList<Album> getAlbums(){
		return this.albums;
	}

	/**
	 * Creates a new user object to add to the list and updates the database.
	 * Called when creating an instance through login add button and admin add button.
	 * 
	 * @param name The user name to add
	 * @param pass The user's chosen password
	 */
	public static void addUser(String name, String pass) {
		User user = new User(name, pass);
		users.add(user);
		User.writeToDatabase();
	}
	
	/**
	 * Searches database for the given user name and deletes it, updating the
	 * database after the removal.
	 * 
	 * @param name The user name to search for
	 */
	public static void deleteUser(String name) {
		// TODO: delete user from user list
	}
	
	/**
	 * Creates a data directory folder directly in the project folder for holding
	 * the various user directories and their respective albums containing photos.
	 */
	public static void createDataDir() {
		File dir = new File("data");
		if(dir.exists()) {
			if(Photos.DEBUG) System.out.println("Data directory already exists.");
			return;
		} else {
			if(Photos.DEBUG) System.out.println("Creating data directory.");
			dir.mkdir();
		}
	}
	
	/**
	 * Instantiates the user list from an existing user database text file if it finds
	 * one.
	 */
	public static void readFromDatabase() {
		// since user list was just created, add the admin user
		users = new ArrayList<User>();
		User.addUser("admin", "admin");
		
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader("data/users.txt"));
			if(Photos.DEBUG) System.out.println("Found user text file");
			
			String line;
			String[] split;
			while((line = br.readLine()) != null) {
				split = line.split(" ");
				users.add(new User(split[0], split[1]));
			}
			br.close();
			
		} catch (IOException e) {
			if(Photos.DEBUG) System.out.println("User text file does not exist, creating");	
		}
	}
	
	/**
	 * Updates the user database text file by writing every entry in the user list
	 * in its current order.
	 */
	public static void writeToDatabase() {
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter("data/users.txt"));
			for(int i = 0; i < users.size(); i++) {
				bw.write(users.get(i).getName() + " " + users.get(i).getPass());
				bw.newLine();
				bw.flush();
			}
			bw.close();

		} catch (IOException e) {
			if(Photos.DEBUG) System.out.println("Cannot write to file.");	
		}
	}
	
	/**
	 * Checks to see if the given user name and corresponding password exists in the
	 * user database text file. Returns true if it finds an exact match, and false if
	 * it does not find any match.
	 * 
	 * 
	 * @param name The user name to verify
	 * @param pass The password to verify
	 * @return Whether or not the name and password combination exists
	 */
	public static boolean verifyFromDatabase(String name, String pass) {
		return false;
	}
}
