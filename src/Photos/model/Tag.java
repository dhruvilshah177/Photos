package Photos.model;

import java.io.Serializable;

/**
 * This class represents a tag.
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Tag implements Comparable<Tag>, Serializable {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9161460696154678034L;

    /**
     * Tag name
     */
    private String tag_Name;

    /**
     * Tag value
     */
    private String tag_Value;

    /**
     * @param _tagName Tag name
     * @param _tagValue Tag value
     */
    public Tag(String _tagName, String _tagValue) {
        tag_Name = _tagName;
        tag_Value = _tagValue;
    }


    /**
     * @param t Input tag
     */
    public Tag(Tag t) {
        tag_Name = t.tag_Name;
        tag_Value = t.tag_Value;
    }


    /**
     * @return Output for tags
     */
    public String toString() {
    	return tag_Name + "=" + tag_Value;
    }

    /**
     * @param tag Input tag
     * @return Checks whether tags are equal to each other
     */
    @Override
    public int compareTo(Tag tag) {
    	int iRet = tag_Name.compareToIgnoreCase(tag.tag_Name);
    	return (iRet!=0) ? iRet : tag_Value.compareToIgnoreCase(tag.tag_Value);
    }
}