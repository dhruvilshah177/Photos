package Photos.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

/**
 * This class represents an album.
 * @author Dhruvil Shah
 * @author Drashti Metha
 */
public class Album implements Comparable<Album>, Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6324409348554636338L;

    /**
     * Album name for when we search tags
     */
    public static String ALBUM_NAME_SEARCH_BY_TAG = " Tag Search Result";

    /**
     * Album name for when we search dates
     */
    public static String ALBUM_NAME_SEARCH_BY_DATE = " Date Search Result";

    /**
     * Album name for when we store to file 
     */
    private String album_Name_To_Keep;

    /**
     * Name of album
     */
    private  SimpleStringProperty album_Name;

    /**
     * Number of photos
     */
    private  SimpleIntegerProperty photo_Count;

    /**
     * Earliest date of picture
     */
    private  SimpleStringProperty startTime;

    /**
     * Latest d of picture 
     */
    private  SimpleStringProperty endTime;

    /**
     * @return Name of album
     */
    public String getAlbumName() {
        return album_Name.get();
    }

    /**
     * @param album_Name Name of album
     */
    public void setAlbumName(String album_Name) {
        this.album_Name.set(album_Name);
    }

    /**
     * @return Number of photos
     */
    public int getPhotoCount() {
        return photo_Count.get();
    }

    /**
     * @param photo_Count Number of photos
     */
    public void setPhotoCount(int photo_Count) {
        this.photo_Count.set(photo_Count);
    }

    /**
     * @return Earliest date of picture 
     */
    public String getStartTime() {
        return startTime.get();
    }

    /**
     * @param startTime Earliest date of picture taken
     */
    public void setStartTime(String startTime) {
        this.startTime.set(startTime);
    }

    /**
     * @return Latest time of picture taken
     */
    public String getEndTime() {
        return endTime.get();
    }

    /**
     * @param endTime Latest date time of picture taken
     */
    public void setEndTime(String endTime) {
        this.endTime.set(endTime);
    }

    /**
     * @param isStored Checks to see if we already stored the file or not
     */
    public void do_CleanUp(boolean isStored) {
		if (isStored) {
			album_Name_To_Keep = getAlbumName();
			//
			album_Name 	= null;
			photo_Count 	= null;
			startTime 	= null;
			endTime 	= null;
		}
		else {
			album_Name 	= new SimpleStringProperty(album_Name_To_Keep);
		    photo_Count 	= new SimpleIntegerProperty();
		    startTime 	= new SimpleStringProperty();
		    endTime 	= new SimpleStringProperty();
		    //
		    album_Name_To_Keep = null;
		}
		//
		for (Photo p: photo_List) {
			p.do_CleanUp(isStored);
		}
	}
	
    /**
     * All of the photos in the album in a list
     */
    private List<Photo> photo_List;

    /**
     * Index of current photo we are on
     */
    private int index_Current_Photo;


    /**
     * @param _albumName Name of album
     */
    public Album(String _album_Name) {
		album_Name = new SimpleStringProperty(_album_Name);
	    photo_Count = new SimpleIntegerProperty(0);
	    startTime = new SimpleStringProperty("N/A");
	    endTime = new SimpleStringProperty("N/A");
	    //
	    photo_List = new ArrayList<>();
	    index_Current_Photo = -1;
	}


    /**
     * @param _album_Name Name of album
     * @param list_Photo List of photos
     */
    public Album(String _album_Name, List<Photo> list_Photo) {
		this(_album_Name);
	    //
	    for (Photo p : list_Photo) {
		    Photo newOne = new Photo(p);
		    photo_List.add(newOne);
	    }
	    //
		if (photo_List.size()>0) {
			index_Current_Photo = 0;
		}
	}

    /**
     * @param input Input index of the current photo
     * @return index of the current photo
     */
    public int setIndexCurrentPhoto(int input) {
		index_Current_Photo = input;
		return resetIndexCurrentPhoto();
	}

    /**
      * @return Index of the current photo
     */
    private int resetIndexCurrentPhoto() {
		if (photo_List.size()==0) {
		    index_Current_Photo 	= -1;
		}
		else {
			if (index_Current_Photo > photo_List.size() - 1) {
				index_Current_Photo  = photo_List.size() - 1;
			}
			else if (index_Current_Photo < 0) {
				index_Current_Photo = 0;
			}
		}
		//
		return index_Current_Photo;
	}

    /**
     * @return Index of the current photo
     */
    public int getIndexCurrentPhoto() {
		return 	resetIndexCurrentPhoto();
	}


    /**
     * @param photo Input photo to be added to a Album
     */
    public void add_Photo_To_Album(Photo photo) {
    	photo_List.add(photo);
    	index_Current_Photo = photo_List.size() - 1;
	}


    /**
     * @param index Index of photo
     * @return Photo from index
     */
    public Photo get_Photo_From_Album(int index) {
    	if (index < photo_List.size() && index>=0) {
    		return photo_List.get(index);
    	}
    	return null;
    }

    /**
     * @param photo Input photo
     * @return Index of the photo
     */
    public int getPhotoIndex(Photo photo) {
        return IntStream.range(0, photo_List.size()).filter(i -> photo == photo_List.get(i)).findFirst().orElse(-1);
    }


    /**
     * @param photo Input photo
     * @return the index of photo to be  deleted
     */
    public int delete_Photo_From_Album(Photo photo) {
    	int index = -1;
    	for (int i=0; i<photo_List.size(); i++) {
    		if (photo==photo_List.get(i)) {
    			index = i;
    			photo_List.remove(index);
    			break;
    		}
    	}
    	return index;
    }


    /**
     * @param photoAt The index where the new photo will be added to 
     * @param photoAdd Photo to be added
     * @return Index of the photo to be added
     */
    public int add_Photo_To_Album(Photo photoAt, Photo photoAdd) {
    	int index;
        if (photoAt != null) {
            index = IntStream.range(0, photo_List.size())
                    .filter(i -> photoAt == photo_List.get(i))
                    .findFirst()
                    .orElse(-1);
        } else index = photo_List.size();
        //
		photo_List.add(index, photoAdd);
		//
    	return index;
    }


    /**
     * @return List of photos
     */
    public List<Photo> getPhoto_List() {
    	return photo_List;
    }

    /**
     * @return Size of list of photos
     */
    public int getSize() {
    	return photo_List.size();
    }


    /**
     * @return Current photo
     */
    public Photo getCurrentPhoto() {
    	resetIndexCurrentPhoto();
        return index_Current_Photo >= 0 ? photo_List.get(index_Current_Photo) : null;
    }


    /**
     * @param album Input album
     */
    @Override
	public int compareTo(Album album) {
		return getAlbumName().compareToIgnoreCase(album.getAlbumName());
	}


    /**
     * @return Album name
     */
    @Override
	public String toString() {
		return getAlbumName();
	}


    /**
     * @param tags Condition of tags to search
     * @return List of photos found with given tags
     */
    public List<Photo> do_SearchTag(Tag_Search_Condition tags) {
		BiPredicate<Photo,Tag_Search_Condition> bp = (p, c)->Helper.search(p.getTags(), c.getTags(), (t1,t2)->t1.compareTo(t2)==0);
		//
		return Helper.filter(photo_List, tags, bp);
	}


    /**
     * @param dates Condition of dates to search
     * @return List of photos found with given dates
     */
    public List<Photo> do_SearchDate(Time_Search_Condition dates) {
		BiPredicate<Photo,Time_Search_Condition> bp = Photo::is_Within_Range;
		//
		return Helper.filter(photo_List, dates, bp);
	}


    /**
     * Sets photo count and the Earliest and latest times of all the photos for the album
     */
    public void set_Counter_Date_Time() {
		if (photo_List.size()==0) {
			setPhotoCount(0);
		    setStartTime("N/A");
		    setEndTime("N/A");
		}
		else {
			boolean start = true;
			int count 	= 0;
			long min	= 0;
			long max	= 0;
			for (Photo p: photo_List) {
				if (start) {
					count = 1;
					min	= p.getDateOfPhoto();
					max	= p.getDateOfPhoto();
					start = false;
				}
				else {
					count++;
					long pd = p.getDateOfPhoto();
					if (pd > max) {
						max = pd;
					}
					if (pd < min) {
						min = pd;
					}
				}
			}
			//
			setPhotoCount(count);
		    setStartTime(Photo.epoch_To_LocalTime(min));
		    setEndTime(Photo.epoch_To_LocalTime(max));
		}
	}
}