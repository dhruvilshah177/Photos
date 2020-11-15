package Photos.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * This class represents an user.
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class User implements Comparable<User>, Serializable {

    /**
     * Admin user
     */
    public static final String ADMIN_USER = "admin";

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2849110358758549664L;

    /**
     * @param isStored Clean up before store to file or after retrieve from file
     */
    public void do_CleanUp(boolean isStored) {
		if (isStored) {
			listOfAlbumsToKeep = new ArrayList<>(listAlbums);
			listAlbums = null;
			//
			for (Album a : listOfAlbumsToKeep) {
				a.do_CleanUp(true);
			}
		}
		else {
			listAlbums = FXCollections.observableList(listOfAlbumsToKeep);
			listOfAlbumsToKeep = null;
			//
			for (Album a : listAlbums) {
				a.do_CleanUp(false);
			}
		}
		//
		tags.do_CleanUp(isStored);
	}

    /**
     * Username
     */
    private String username;

    /**
     * List of albums
     */
    private ObservableList<Album> listAlbums;

    /**
     * List of albums for serialization
     */
    private ArrayList<Album> listOfAlbumsToKeep;

    /**
     * Current album
     */
    private Album currentAlbum;

    /**
     * Tags for searching
     */
    private Tag_Search_Condition tags;

    /**
     * Range of dates for searching
     */
    private Time_Search_Condition dates;


    /**
     * @param _username Username
     */
    public User(String _username) {
		username 		= _username;
		listAlbums 		= FXCollections.observableArrayList();
		listOfAlbumsToKeep = null;
		currentAlbum 	= null;
		//
		tags 			= new Tag_Search_Condition();
		dates 			= new Time_Search_Condition();
	}


    /**
     * @param username Username
     * @return New album added
     */
    public Album add_Album(String username) {
		Album album = new Album(username);
		//
        boolean isFound = IntStream.range(0, listAlbums.size())
                .anyMatch(i -> listAlbums.get(i).getAlbumName().equalsIgnoreCase(album.getAlbumName()));
        //
		if (!isFound) {
			listAlbums.add(album);
			return album;
		}
		else {
			return null;
		}
	}


    /**
     * @param album Input album. Overwrite used for search results
     */
    public void add_Or_Overwrite_Album(Album album) {
        IntStream.range(0, listAlbums.size())
                .filter(i -> listAlbums.get(i).getAlbumName().equalsIgnoreCase(album.getAlbumName()))
                .findFirst()
                .ifPresent(i -> listAlbums.remove(i));
		//
		listAlbums.add(album);
	}

    /**
     * @param index Index of album to update
     * @param albumName New album name
     */
    public void update_Album_Name(int index, String albumName) {
		if (index >= 0 && index < listAlbums.size()) {
            boolean isFound = IntStream.range(0, listAlbums.size())
                    .filter(i -> i != index)
                    .anyMatch(i -> listAlbums.get(i).getAlbumName().equalsIgnoreCase(albumName));
            //
			if (!isFound) {
				listAlbums.get(index).setAlbumName(albumName);
			}
		}
	}


    /**
     * @return Current album
     */
    public Album getCurrentAlbum() {
		return currentAlbum;
	}

    /**
     * @param currentAlbum Current album
     */
    public void setCurrentAlbum(Album currentAlbum) {
        this.currentAlbum = currentAlbum;
    }

    /**
     * @return Tags for searching
     */
    public Tag_Search_Condition getTags() {
        return tags;
    }


    /**
     * @param index Index of tag to delete
     */
    public void delete_Tag_Search_Condition_Tag(int index) {
		ObservableList<Tag> tagList = tags.getTags();
		if (tagList.size() > 0 && index >= 0 && index < tagList.size()) {
			tagList.remove(index);
		}
	}

    /**
     * @return Range of dates
     */
    public Time_Search_Condition getDates() {
        return dates;
    }

    /**
     * @return List of albums
     */
    public ObservableList<Album> getListAlbums() {
		return listAlbums;
	}


    /**
     * @return Username
     */
    public String getUsername() {
		return username;
	}

    /**
     * @param albumName album to delete
     * @return album to delete
     */
	public Album delete_Album(String albumName) {
		return Helper.delete(listAlbums, albumName, (t,k)->t.getAlbumName().equalsIgnoreCase(k));
	}


    /**
     * @param albumName Album name to search for
     * @return Album asked for
     */
    public Album getAlbum(String albumName) {
		return Helper.get(listAlbums, albumName, (t,k)->t.getAlbumName().equalsIgnoreCase(k));
	}

    /**
     * @param photo Photo to copy
     * @param targetAlbumName Album to copy to
     */
    public void copyPhoto(Photo photo, String targetAlbumName) {
		Photo newOne = new Photo(photo);
		//
		Album targetAlbum = getAlbum(targetAlbumName);
		//
		targetAlbum.add_Photo_To_Album(newOne);
	}

    /**
     * @param photo Photo to move
     * @param targetAlbumName Album to move to
     */
    public void movePhoto(Photo photo, String targetAlbumName) {
		Album targetAlbum = getAlbum(targetAlbumName);
		//
		currentAlbum.delete_Photo_From_Album(photo);
		//
		targetAlbum.add_Photo_To_Album(photo);
	}


    /**
     * @return Username
     */
    @Override
	public String toString() {
		return username;
	}


    /**
     * @param arg0 Input username
     * @return Checks whether username is equal or not
     */
    @Override
	public int compareTo(User arg0) {
		return username.compareToIgnoreCase(arg0.username);
	}


    /**
     * @return List of photos searched with tags
     */
    public List<Photo> do_Search_By_Tag() {
		List<Photo> lst = new ArrayList<>();
		//
		for (Album a : listAlbums) {
            if (!a.getAlbumName().equalsIgnoreCase(Album.ALBUM_NAME_SEARCH_BY_DATE) && !a.getAlbumName()
                    .equalsIgnoreCase(Album.ALBUM_NAME_SEARCH_BY_TAG)) {
                lst.addAll(a.do_SearchTag(tags));
            }
        }
		//
		return lst;
	}


    /**
     * @return List of photos searched with dates
     */
    public List<Photo> do_Search_By_Date() {
		List<Photo> lst = new ArrayList<>();
		//
		for (Album a : listAlbums) {
            if (!a.getAlbumName().equalsIgnoreCase(Album.ALBUM_NAME_SEARCH_BY_DATE) && !a.getAlbumName()
                    .equalsIgnoreCase(Album.ALBUM_NAME_SEARCH_BY_TAG)) {
                lst.addAll(a.do_SearchDate(dates));
            }
        }
		//
		return lst;
	}
}
