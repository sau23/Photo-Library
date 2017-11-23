package classes;

import java.io.Serializable;

/**
 * Tag object stores two strings that are ascribed 
 * to each picture as per the user
 * 
 * @author Nicholas Petriello
 * @author Samuel Uganiza
 */
public class Tag implements Serializable{

	/**
	 * Autogenerated serial number
	 */
	private static final long serialVersionUID = 8216510983416262539L;
	
	/**
	 * Tag object's type and value.
	 */
	private String type;
	private String value;
	
	/**
	 * The Tag takes in two string to be created
	 * 
	 * @param tagType This is type of the Tag
	 * @param tagValue This is the value of the Tag
	 */
	public Tag(String tagType, String tagValue){
		this.type = tagType;
		this.value = tagValue;
	}
	
	/**
	 * compareTo() is just to find a exact matching
	 * Tag
	 * 
	 * @param compare The Tag to be compared with
	 * 
	 * @return int showing how similar to the caller Tag
	 * is to compare. 0 = equal.
	 */
	public int compareTo(Tag compare){
		return this.type.compareTo(compare.type) + 
				this.value.compareTo(compare.value);
	}

	/**
	 * compareNames() does what {@link #compareTo(Tag)}
	 * does but restricts the comparison to the names
	 * of the Tags.
	 * 
	 * @param compare The Tag whose name is to be compared
	 * with
	 * 
	 * @return int showing how similar to the caller Tag
	 * is to compare. 0 = equal.
	 */
	public int compareNames(Tag compare){
		return this.type.compareTo(compare.type);
	}
	
	/**
	 * compareValues() does what {@link #compareTo(Tag)}
	 * does but restricts the comparison to the values of the
	 * Tags.
	 * 
	 * @param compare The Tag whose value is to be compared with
	 * 
	 * @return int showing how similar to the caller Tag 
	 * is to compare. 0 = equal.
	 */
	public int compareValues(Tag compare){
		return this.value.compareTo(compare.value);
	}
	
	/**
	 * Returns this tag's type.
	 * 
	 * @return Tag's type
	 */
	public String getTagType(){
		return this.type;
	}
	
	/**
	 * Returns this tag's value.
	 * 
	 * @return Tag's value
	 */
	public String getTagValue(){
		return this.value;
	}

	/**
	 * Returns this tag's type and value with a hyphen in
	 * between.
	 * 
	 * @return Formatted to print "tag-value"
	 */
	@Override
	public String toString(){
		return this.type + "-" + this.value;
	}
}
