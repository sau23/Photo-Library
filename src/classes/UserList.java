package classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * The UserList class contains the global list of users in one machine,
 * read in by .ser files stored in the data directory. This class also
 * reads and writes to the .ser files as well as checks to see if a user
 * attempting to log in has provided valid information.
 * 
 * @author Nicholas Petriello
 * @author Samuel Uganiza
 */
public class UserList {

	// classy debug boolean
	public static boolean DEBUG = true;

	/**
	 * Global list of users that holds all users in one machine.
	 */
	private static ArrayList<User> users;
	
	/**
	 * Controls whether or not the stock user is generated.
	 */
	public static boolean generateStock = true;
	
	/**
	 * Returns the user list.
	 * 
	 * @return The user list
	 */
	public static ArrayList<User> getUsers() {
		return users;
	}
	
	/**
	 * Returns a user at the given index.
	 * 
	 * @param index The index of user to get
	 * @return The user at the given index
	 */
	public static User getUser(int index) {
		return users.get(index);
	}
	
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
		niko.setCaption("A picture of a not-cat");
		niko.addTag(new Tag("family", "feline"));
		stock.checkInPhotos(niko, album1);
		stock.getAlbums().add(album1);
		
		// set album 2 contents
		Album album2 = new Album("Album 2");
		Photo bird, catarpillar, hedhogs, lions, orbweaver, owl;
		bird = new Photo("data/stock/bird.jpg");
		bird.setCaption("A picture of a bird");
		bird.addTag(new Tag("family", "avian"));
		stock.checkInPhotos(bird, album2);
		catarpillar = new Photo("data/stock/catarpillar.jpg");
		catarpillar.setCaption("A picture of a caterpillar");
		catarpillar.addTag(new Tag("family", "lepidoptera"));
		stock.checkInPhotos(catarpillar, album2);
		hedhogs = new Photo("data/stock/hedhogs.jpg");
		hedhogs.setCaption("A picture of a hedgehog");
		hedhogs.addTag(new Tag("family", "erinaceinae"));
		stock.checkInPhotos(hedhogs, album2);
		lions = new Photo("data/stock/lions.jpg");
		lions.setCaption("A picture of a lion");
		lions.addTag(new Tag("family", "feline"));
		stock.checkInPhotos(lions, album1);
		orbweaver = new Photo("data/stock/substock/orbweaver.jpg");
		orbweaver.setCaption("A picture of a spider");
		orbweaver.addTag(new Tag("family", " araneid"));
		stock.checkInPhotos(orbweaver, album1);
		owl = new Photo("data/stock/substock/owl.jpg");
		owl.setCaption("A picture of an owl");
		owl.addTag(new Tag("family", "avian"));
		stock.checkInPhotos(owl, album1);
		stock.checkInPhotos(niko, album2);
		stock.getAlbums().add(album2);
		
		// set album 3 contents
		Album album3 = new Album("Album 3");
		Photo cat, fox, frog;
		cat = new Photo("data/stock/substock/cat.jpg");
		cat.setCaption("A picture of a cat");
		cat.addTag(new Tag("family", "feline"));
		stock.checkInPhotos(cat, album3);
		stock.checkInPhotos(niko, album3);
		fox = new Photo("data/stock/substock/fox.jpg");
		fox.setCaption("A picture of a fox");
		fox.addTag(new Tag("family", "canine"));
		stock.checkInPhotos(fox, album3);
		frog = new Photo("data/stock/substock/frog.jpg");
		frog.setCaption("A picture of a frog");
		frog.addTag(new Tag("family", "amphibian"));
		stock.checkInPhotos(frog, album3);
		stock.getAlbums().add(album3);
		
		// delete old stock user file and replace with new copy
		int i;
		if((i = UserList.verifyFromUserDatabase("stock", "")) > -1) {
			UserList.deleteUser(i);
		}
		UserList.users.add(stock);
		UserList.writeToUserDatabase(stock);
	}
}
