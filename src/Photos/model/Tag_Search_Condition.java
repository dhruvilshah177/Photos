package Photos.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * This class helps to search through tags.
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Tag_Search_Condition implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3520630274431813396L;


    /**
     * @param isStored Checks to see if we already stored the file or not
     */
    public void do_CleanUp(boolean isStored) {
		if (isStored) {
			list_Of_Tags_To_Keep = new ArrayList<>(list_Tags);
			list_Tags = null;
		}
		else {
			list_Tags = FXCollections.observableList(list_Of_Tags_To_Keep);
			list_Of_Tags_To_Keep = null;
		}
	}


    /**
     * List of tags
     */
    private ObservableList<Tag> list_Tags;

    
    /**
     * List of tags to Keep
     */
    private ArrayList<Tag> list_Of_Tags_To_Keep;


    /**
     * Initialize fields
     */
    public Tag_Search_Condition() {
	    list_Tags = FXCollections.observableArrayList();
	}


    /**
     * @return List of tags
     */
    public ObservableList<Tag> getTags() {
		return list_Tags;
	}


    /**
     * @param tag_Name Tag name
     * @param tag_Value Tag value
	 * @return True if tag is added
     */
    public boolean addTag(String tag_Name, String tag_Value) {
    	Tag t = new Tag(tag_Name, tag_Value);
    	//
        return Helper.addOrRetrieveOrderedList(list_Tags, t);
    }
}