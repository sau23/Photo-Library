package classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class UserList {

	// classy debug boolean
	public static boolean DEBUG = true;

	/**
	 * Global list of users that holds all users in one machine.
	 */
	public static ArrayList<User> users;
	
	/**
	 * Controls whether or not the stock user is generated.
	 */
	public static boolean generateStock = true;
	
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
		User toAdd = new User(name, pass);
		users.add(toAdd);
		writeToUserDatabase(toAdd);
		if(DEBUG) System.out.println("Successfully added new user " + name + ", " + pass + ".");
		return true;
	}

	/**
	 * Searches database for the given user name and deletes it, deleting the
	 * corresponding .ser file from the database.
	 * 
	 * @param index The index of user to remove
	 */
	public static void deleteUser(int index) {
		File f = new File("data/" + users.get(index).getName() + ".ser");
		f.delete();
		users.remove(index);
		if(DEBUG) System.out.println("Successfully deleted user at index " + index);
	}

	/**
	 * Instantiates the user list from the data directory by reading the names
	 * of .ser files. If no data directory was detected, the data directory is
	 * empty, or the data directory only contains the stock photos directory,
	 * this will simply create an empty static reference to the user list.
	 */
	public static void readFromUserDatabase() {
		File f = new File("data");
		if(!(f.exists() && f.isDirectory())) {

			// first time read, if not found then create a new file.
			users = new ArrayList<User>();
			f.mkdir();

			if(DEBUG) System.out.println("User database not found, creating new directory.");
			return;
		}
		
		// check if it only contains the stock photos directory
		if(f.list().length < 2) {
			users = new ArrayList<User>();
			if(DEBUG) System.out.println("User database found, but no files exist yet.");
			return;
		}
		
		// otherwise it builds the user list on whatever files are inside
		users = new ArrayList<User>();
		for(File file : f.listFiles()) {
			if(file.isFile()) {
				String name = removeExtension(file.getName());
				try {
					ObjectInputStream in = new ObjectInputStream(new FileInputStream("data/" + name + ".ser"));
					User user = (User)in.readObject();
					if(user.getName().equals("stock")) {
						if(user.getPass() != null) {
							users.add(user);
						} else {
							UserList.generateStock = false;
						}
					} else {
						users.add(user);
					}
					in.close();
					if(DEBUG) System.out.println("Successfully read " + name + " from user database.");
				} catch (IOException e) {
					if(DEBUG) System.out.println("Error reading in " + name + ".ser file.");
				} catch (ClassNotFoundException c) {
					if(DEBUG) System.out.println("User class not found");
					c.printStackTrace();
				}
			} else {
				if(DEBUG) System.out.println("File \"" + file.getName() + "\" found was not normal file.");
			}
		}
	}

	/**
	 * Updates the user database by writing changes to the given user's .ser
	 * file.
	 */
	public static void writeToUserDatabase(User user) {
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data/" + user.getName() + ".ser"));
			out.writeObject(user);
			out.close();
			if(DEBUG) System.out.println("Successfully wrote " + user.getName() + " to database.");
		} catch(IOException e) {
			if(DEBUG) System.out.println("Error writing " + user.getName() + ".ser file");
			e.printStackTrace();
		}
	}

	/**
	 * Checks to see if the given user name and corresponding password exists in the
	 * user list. Returns the index of a user if it finds an exact match, and an 
	 * error code if it does not find any match.
	 * 
	 * @param name The user name to verify
	 * @param pass The password to verify
	 * 
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

	/**
	 * Removes the file extension from the given file name.
	 * 
	 * @param fileName The file name with an extension
	 * 
	 * @return The file name without the extension
	 */
	public static String removeExtension(String fileName) {
		int i = fileName.lastIndexOf(".");
		if(i > 0) {
			return fileName.substring(0, i);
		}
		return "";
	}
	
	/**
	 * Adds a stock user account to the list of users with pre-determined file
	 * locations. Only run once per program start-up.
	 */
	public static void addStockUser() {
		User stock = new User("stock", "");
		
		// set album 1 contents
		Album album1 = new Album("Album 1");
		Photo niko = new Photo("data/stock/niko.png");
		stock.checkInPhotos(niko, album1);
		stock.getAlbums().add(album1);
		
		// set album 2 contents
		Album album2 = new Album("Album 2");
		Photo bird, catarpillar, hedhogs, lions, orbweaver, owl;
		bird = new Photo("data/stock/bird.jpg");
		stock.checkInPhotos(bird, album2);
		catarpillar = new Photo("data/stock/catarpillar.jpg");
		stock.checkInPhotos(catarpillar, album2);
		hedhogs = new Photo("data/stock/hedhogs.jpg");
		stock.checkInPhotos(hedhogs, album2);
		lions = new Photo("data/stock/lions.jpg");
		stock.checkInPhotos(lions, album1);
		orbweaver = new Photo("data/stock/substock/orbweaver.jpg");
		stock.checkInPhotos(orbweaver, album1);
		owl = new Photo("data/stock/substock/owl.jpg");
		stock.checkInPhotos(owl, album1);
		stock.checkInPhotos(niko, album2);
		stock.getAlbums().add(album2);
		
		// delete old stock user file and replace with new copy
		int i;
		if((i = UserList.verifyFromUserDatabase("stock", "")) > -1) {
			UserList.deleteUser(i);
		}
		UserList.users.add(stock);
		UserList.writeToUserDatabase(stock);
	}
}
