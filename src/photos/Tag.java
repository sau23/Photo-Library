package photos;

/**
 * Tag object stores two strings that are ascribed 
 * to each picture as per the user
 * 
 * @author le09idas
 *
 */
public class Tag {

	String name;
	String value;
	
	/**
	 * The Tag takes in two string to be created
	 * 
	 * @param tagName This is name of the Tag
	 * @param tagValue This is the value of the Tag
	 */
	public Tag(String tagName, String tagValue){
		
		this.name = tagName;
		this.value = tagValue;
		
	}
	
	/**
	 * compareTo() is just to find a exact matching
	 * Tag
	 * 
	 * @param compare The Tag to be compared with
	 * @return int showing how similar to the caller Tag
	 * is to compare. 0 = equal.
	 */
	int compareTo(Tag compare){
		
		return this.name.compareTo(compare.name) + 
				this.value.compareTo(compare.value);
		
	}
	
	/**
	 * compareNames() does what {@link #compareTo(Tag)}
	 * does but restricts the comparison to the names
	 * of the Tags.
	 * 
	 * @param compare The Tag whose name is to be compared
	 * with
	 * @return int showing how similar to the caller Tag
	 * is to compare. 0 = equal.
	 */
	int compareNames(Tag compare){
		
		return this.name.compareTo(compare.name);
		
	}
	
	/**
	 * compareValues() does what {@link #compareTo(Tag)}
	 * does but restricts the comparison to the values of the
	 * Tags.
	 * @param compare The Tag whose value is to be compared with
	 * @return int showing how similar to the caller Tag 
	 * is to compare. 0 = equal.
	 */
	int compareValues(Tag compare){
		
		return this.value.compareTo(compare.value);
		
	}
	
	
		
	
}
