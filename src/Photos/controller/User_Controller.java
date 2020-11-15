package Photos.controller;

import Photos.model.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.List;


/**
 * This class helps to control user functionality.
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class User_Controller extends Controller_Base implements EventHandler<MouseEvent>, Controller_Interface, ChangeListener<Album> {

    /**
     * TableView of albums
     */
    @FXML TableView<Album> 	albums;

    /**
     * Columns of album names and among other information
     */
    @FXML TableColumn 	col_albumName;
	@FXML TableColumn 	col_photoCount;
	@FXML TableColumn 	col_startTime;
	@FXML TableColumn 	col_endTime;

    /**
     * ListView of tags which is needed for searching
     */
	@FXML
	ListView<Tag> list_Tag;

    /**
     * Textfield for a tag's name
     */
    @FXML TextField tag_Name;

    /**
     * Textfield for a tag's value
     */
    @FXML TextField tag_Value;

    /**
     * Textfield for a new album name
     */
    @FXML TextField new_AlbumName;

    /**
     * Start date for searching
     */
    @FXML DatePicker startDate;

    /**
     * End date for searching
     */
    @FXML DatePicker endDate;


    /**
     * Initializes for the current user
     */
    public void initBeforeShow() {
    	User user = get_Model().getCurrentUser();
    	ObservableList<Album> albumList = user.getListAlbums();
    	//
    	list_Tag.setItems(user.getTags().getTags());
        if (user.getTags().getTags().size() > 0) {
        	list_Tag.getSelectionModel().select(0);
        }
    	//
    	for (Album a : albumList) {
    		a.set_Counter_Date_Time();
    	}
    	//
    	albums.setItems(albumList);
    	//
        if (user.getListAlbums().size() > 0) {
        	albums.getSelectionModel().select(0);
        }
    	//
    	albums.refresh();
	}

	
	public User_Controller() {
	}


    /**
     * Initializes controller
     */
    @FXML
    public void initialize() {
    	albums.setEditable(true);
    	//
    	col_albumName.setCellFactory(TextFieldTableCell.<Album>forTableColumn());
    	col_albumName.setOnEditCommit(
            new EventHandler<CellEditEvent<Album, String>>() {
                @Override
                public void handle(CellEditEvent<Album, String> t) {
            		String new_AlbumName = t.getNewValue().trim();
            		//
            		if (new_AlbumName.length()>0) {
            			User user = get_Model().getCurrentUser();
            			//
            			int i = albums.getSelectionModel().getSelectedIndex();
            			//
            			user.update_Album_Name(i, new_AlbumName);
                        
            		}
            		//
        			albums.refresh();
                }
            }
        );
    	//
    	albums.setRowFactory(tableView -> {
            final TableRow<Album> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem removeMenuItem = new MenuItem("Remove");
            removeMenuItem.setOnAction(event -> albums.getItems().remove(row.getItem()));
            contextMenu.getItems().add(removeMenuItem);
           
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                    .then((ContextMenu)null)
                    .otherwise(contextMenu)
            );
            return row ;
        });
    	//
    	endDate.setValue(LocalDate.now());
    	startDate.setValue(endDate.getValue().minusDays(30));
    	
    	albums.getSelectionModel().selectedItemProperty().addListener(this);
    	//
    	albums.setOnMouseClicked(event -> {
			if (!event.getButton().equals(MouseButton.PRIMARY) || event.getClickCount() != 1) {
				if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    
                    //
                    gotoAlbumFromUser();
                }
			}
		});
    	
    }

    
    
	@Override
	public void handle(MouseEvent arg0) {
	}


    /**
     * Logs off
     */
    public void do_Logoff() {
	    goto_Login_From_User();
	}


    /**
     * Exits
     */
    public void do_Exit() {
		Platform.exit();
	}


    /**
     * Adds a tag which is needed for searching
     */
    public void do_Add_UserTag() {
		String name = tag_Name.getText().trim();
		String value = tag_Value.getText().trim();
		//
        User user = get_Model().getCurrentUser();
        //
        if (name.length()>0 && value.length()>0) {
            //
            boolean ret = user.getTags().addTag(name, value);
            //
            if (ret) {
                list_Tag.refresh();
                //
                tag_Name.setText("");
                tag_Value.setText("");
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Not complete");
                alert.setContentText("Duplicate Name and Value Pair. Try again.");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Not complete");
            alert.setContentText("Name and Value Fields are empty. Enter again");
            alert.showAndWait();
        }
	}


    /**
     * Deletes a tag which is needed for searching
     */
    public void do_Delete_UserTag() {
		User user = get_Model().getCurrentUser();
		//
		int index = list_Tag.getSelectionModel().getSelectedIndex();
		//
		user.delete_Tag_Search_Condition_Tag(index);
		//
		list_Tag.refresh();
	}


    /**
     * Searches photos from a given date range
     */
    public void do_SearchDate() {
		User user = get_Model().getCurrentUser();
		//
		if (startDate.getValue()==null || endDate.getValue()==null) {
		    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please set search condition (dates) first.", ButtonType.YES);
		    alert.showAndWait();
		}
		else {
			user.getDates().setStartDate(startDate.getValue());
			user.getDates().setEndDate(endDate.getValue());
			//
			List<Photo> lst = user.do_Search_By_Date();
			//
			String message;
			if (lst.size()>0) {
				Album newOne = new Album(Album.ALBUM_NAME_SEARCH_BY_DATE, lst);
				newOne.set_Counter_Date_Time();
				user.add_Or_Overwrite_Album(newOne);
				message = "Album '" + Album.ALBUM_NAME_SEARCH_BY_DATE + "' is created for search results. It will be replaced in next search. To keep it, simply change its name.";
				//
			    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES);
			    alert.showAndWait();
				//
	    		user.setCurrentAlbum(newOne);
	    		gotoAlbumFromUser();
			}
			else {
				message = "No photo matched";
				//
			    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES);
			    alert.showAndWait();
			}
		}
	}


    /**
     * Searches photos with tags
     */
    public void do_SearchTag() {
		User user = get_Model().getCurrentUser();
		//
		Tag_Search_Condition tagCondition = user.getTags();
		//
		if (tagCondition.getTags().size()==0) {
		    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please set search condition of tags first", ButtonType.YES);
		    alert.showAndWait();
		}
		else {
			List<Photo> lst = user.do_Search_By_Tag();
			//
			String message;
			if (lst.size()>0) {
				Album newOne = new Album(Album.ALBUM_NAME_SEARCH_BY_TAG, lst);
				newOne.set_Counter_Date_Time();
				user.add_Or_Overwrite_Album(newOne);
				message = "Album '" + Album.ALBUM_NAME_SEARCH_BY_TAG + "' is created for search results. It will be replaced in next search. To keep it, simply change its name.";
				//
			    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES);
			    alert.showAndWait();
				//
	    		user.setCurrentAlbum(newOne);
	    		gotoAlbumFromUser();
			}
			else {
				message = "No photo matched";
				//
			    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES);
			    alert.showAndWait();
			}
		}
	}


    /**
     * Adds new album
     */
    public void do_Add_NewAlbum() {
		User user = get_Model().getCurrentUser();
		//
		String album_name = new_AlbumName.getText().trim();
		if (album_name.length()>0) {
			user.add_Album(album_name);
	    	albums.refresh();
			//
			new_AlbumName.setText("");
		}
	}

    
    /**
     * When selection in table view is changed, set current album
     */
    @Override
	public void changed(ObservableValue<? extends Album> observable, Album oldValue, Album newValue) {
    	Photos_Model model = get_Model();
    	User user = model.getCurrentUser();
    	//
    	if (newValue!=null) {
    		user.setCurrentAlbum(newValue);
    	}
	}


    /**
     * Shows help for photos application
     */
    public void doHelp() {
		Help(PHOTOS_HELP_FXML);
	}
}