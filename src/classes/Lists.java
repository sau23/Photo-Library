package classes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Lists {

	// classy debug boolean
	private static boolean DEBUG = true;
	
	/**
	 * Global list of users that holds all users in one machine.
	 */
	public static ArrayList<User> users;
	
	/**
	 * Global list of photos references for all albums in one machine.
	 */
	public static ArrayList<Photo> photos;
	
	/**
	 * Creates a new user object to add to the list and updates the database.
	 * Prohibits adding same user name multiple times, case specific.
	 * Called when creating an instance through login add button and admin add button.
	 * 
	 * @param name The user name to add
	 * @param pass The user's chosen password
	 * 
	 * @return 
	 */
	public static boolean addUser(String name, String pass) {
		
		// check to see if user name already exists in the list
		for(User user : users) {
			if(name.equals(user.getName())) {
				if(DEBUG) System.out.println("User already exists.");
				return false;
			}
		}

		users.add(new User(name, pass));
		writeToUserDatabase();
		if(DEBUG) System.out.println("Successfully added new user " + name + ", " + pass + ".");
		return true;
	}

	/**
	 * Searches database for the given user name and deletes it, updating the
	 * database after the removal.
	 * 
	 * @param index The index of user to remove
	 */
	public static void deleteUser(int index) {
		users.remove(index);
		writeToUserDatabase();
		if(DEBUG) System.out.println("Sucesfully deleted user at index " + index);
	}
	
	/**
	 * Instantiates the user list from an existing user database ser file by
	 * deserializing the information from data.ser if it finds it.
	 */
	@SuppressWarnings("unchecked")
	public static void readFromUserDatabase() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("data/users.ser"));
			Object data = in.readObject();
			if(data instanceof ArrayList<?>) {
				ArrayList<?> arr = (ArrayList<?>)data;
	
				// checks if array list is of right type when reading
				if(!arr.isEmpty() && arr.get(0) instanceof User) {
					users = (ArrayList<User>)arr;
					
				// otherwise the array list is empty, just make a new one
				} else {
					users = new ArrayList<User>();
				}
				
			}
			in.close();
			if(DEBUG) System.out.println("Successfully read from user database.");
		} catch (IOException e) {
			if(DEBUG) System.out.println("User database not found, creating new one.");
			
			// first time read, if not found then create a new file.
			users = new ArrayList<User>();
			writeToUserDatabase();
		} catch (ClassNotFoundException c) {
			if(DEBUG) System.out.println("ArrayList class not found");
			c.printStackTrace();
		}
	}

	/**
	 * Updates the user database text file by writing the current list into a
	 * ser file after every add.
	 */
	public static void writeToUserDatabase() {
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data/users.ser"));
			out.writeObject(users);
			out.close();
			if(DEBUG) System.out.println("Successfully wrote to user database.");
		} catch(IOException e) {
			if(DEBUG) System.out.println("IO error with user database.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks to see if the given user name and corresponding password exists in the
	 * user database text file. Returns the index of a user if it finds an exact match, 
	 * and an error code if it does not find any match.
	 * 
	 * @param name The user name to verify
	 * @param pass The password to verify
	 * @return The index of user object either with valid credentials or an error code
	 */
	public static int verifyFromUserDatabase(String name, String pass) {

		for(int i = 0; i < users.size(); i++) {
			if(name.equals(users.get(i).getName())) {
				if(pass.equals(users.get(i).getPass())) {
					
					// value of index if found
					return i;
					
				} else {
					
					// value of -2 if found but wrong password
					return -2;
				}
			}
		}
		
		// default value -1 if not found
		return -1;
	}

	public static void addPhoto() {
		
	}
	
	public static int checkPhotos() {
		return 0;
	}
	
	/**
	 * Instantiates the master photo list from an existing user database ser file 
	 * by deserializing the information from photos.ser if it finds it.
	 */
	@SuppressWarnings("unchecked")
	public static void readFromPhotosDatabase() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("data/photos.ser"));
			Object data = in.readObject();
			if(data instanceof ArrayList<?>) {
				ArrayList<?> arr = (ArrayList<?>)data;
				if(!arr.isEmpty() && arr.get(0) instanceof Photo) {
					photos = (ArrayList<Photo>)arr;
				}
			}
			in.close();
			if(DEBUG) System.out.println("Sucessfully read from photo database.");
		} catch (IOException e) {
			if(DEBUG) System.out.println("Photo database not found, creating new one.");
			// first time read, if not found then create a new file.
			photos = new ArrayList<Photo>();
			writeToUserDatabase();
		} catch (ClassNotFoundException c) {
			if(DEBUG) System.out.println("ArrayList class not found");
			c.printStackTrace();
		}
	}

	/**
	 * Updates the photo database text file by writing the current list into a
	 * ser file after every add.
	 */
	public static void writeToPhotoDatabase() {
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data/photos.ser"));
			out.writeObject(photos);
			out.close();
			if(DEBUG) System.out.println("Success writing to photo database.");
		} catch(IOException e) {
			if(DEBUG) System.out.println("IO error with photo database.");
			e.printStackTrace();
		}
	}
}
