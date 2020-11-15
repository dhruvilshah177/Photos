package Photos.controller;

import Photos.model.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;


/**
 * This class represents all the functionality an album has
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Album_Controller extends Controller_Base implements EventHandler<MouseEvent>, ChangeListener<Tab>, Controller_Interface {

    /**
     * Style for photo when not selected
     */
    static final String style_white = "-fx-border-color: white;\n" + "-fx-border-style: solid;\n" + "-fx-border-width: 3;\n";
    
    /**
     * Style for photo when selected
     */
    static final String style_blue = "-fx-border-color: blue;\n" + "-fx-border-style: solid;\n" +"-fx-border-width: 3;\n";

    /**
     * Tab for album and photos
     */
    @FXML
    TabPane tab;

    /**
     * Tile for photo thumbnail
     */
    @FXML
    TilePane tile;

    /**
     * ListView of tags
     */
    @FXML ListView<Tag> list_Tag;

    /**
     * Pagination to navigate photos
     */
    @FXML Pagination pagination;

    /**
     * Textfield to add the tag name
     */
	@FXML TextField tagName;

    /**
     * Textfield to add the tag value
     */
    @FXML TextField tagValue;


    /**
     * List of nodes for photo thumbnails
     */
    ObservableList<Node> list_nodes;

    /**
     * ContextMenu for photos
     */
    final ContextMenu cm = new ContextMenu();

    /**
     * Menu for copying photos to other albums
     */
    Menu cmCopy = new Menu("Copy to");

    /**
     * Menu moving photos to other albums
     */
    Menu cmMove = new Menu("Move to");
    
    
    /**
     * ContextMenu for the tile
     */
	final ContextMenu cm_tile = new ContextMenu();


    /**
     * Initial with album to be shown
     */
    @Override
	public void initBeforeShow() {
    	tab.getSelectionModel().select(0);

    	User user = get_Model().getCurrentUser();
    	Album album = user.getCurrentAlbum();
    	ObservableList<Album> albums = user.getListAlbums();

    	list_nodes.clear();

    	List<Photo> photo_List = album.getPhoto_List();
        for (Photo onePhoto : photo_List) {
            BorderPane viewWrapper = onePhoto.get_ThumbNail_Image_View(this, style_white);
            list_nodes.add(viewWrapper);
        }

    	int index = album.getIndexCurrentPhoto();
        setup_CurrentPhoto(index, true);
	}


    /**
     * constructor of Album_Controller
     */
    public Album_Controller() {
    	Album_Controller controller = this;

    	{	MenuItem cmItemAdd = new MenuItem("Add");
	    	cmItemAdd.setOnAction(e -> {
                User user = get_Model().getCurrentUser();
                Album album = user.getCurrentAlbum();

                FileChooser chooser = new FileChooser();
                chooser.setTitle("Open Photo File");
                File file = chooser.showOpenDialog(new Stage());

                if (file!=null) {
                    Photo photo = Photo.create_Photo(file.getAbsolutePath(), file);

                    int index = album.add_Photo_To_Album(null, photo);

                    BorderPane viewWrapper = null;
                    if (photo != null) {
                        viewWrapper = photo.get_ThumbNail_Image_View(controller, style_white);
                    }

                    list_nodes.add(index, viewWrapper);
                    setup_CurrentPhoto(index, true);
                }
            });
	    	cm_tile.getItems().add(cmItemAdd);
    	}

    	MenuItem cmItemAdd = new MenuItem("Add");
    	cmItemAdd.setOnAction(e -> {
            Photo onePhoto = (Photo)cm.getUserData();

            User user = get_Model().getCurrentUser();
            Album album = user.getCurrentAlbum();

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open File");
            File file = chooser.showOpenDialog(new Stage());

            if (file!=null) {
                Photo photo = Photo.create_Photo(file.getAbsolutePath(), file);
                int old_currIndex = album.getIndexCurrentPhoto();
                int index = album.add_Photo_To_Album(onePhoto, photo);
				BorderPane viewWrapper = null;
				if (photo != null) {
					viewWrapper = photo.get_ThumbNail_Image_View(controller, style_white);
				}
				list_nodes.add(index, viewWrapper);

                if (index <= old_currIndex) {
                	BorderPane vw = (BorderPane) list_nodes.get(old_currIndex+1);
                    vw.setStyle(style_white);
                }

                setup_CurrentPhoto(index, true);
            }
        });
    	cm.getItems().add(cmItemAdd);
    	cm.getItems().add(cmCopy);
    	cm.getItems().add(cmMove);

    	MenuItem cmItem_Delete = new MenuItem("Delete");
    	cmItem_Delete.setOnAction(e -> {
            Photo onePhoto = (Photo)cm.getUserData();

            User user = get_Model().getCurrentUser();
            Album album = user.getCurrentAlbum();

            int index = album.delete_Photo_From_Album(onePhoto);
            list_nodes.remove(index);

            int indexCurr = album.getIndexCurrentPhoto();

            setup_CurrentPhoto(indexCurr, true);
        });
    	cm.getItems().add(cmItem_Delete);
	}


    /**
     * Initialize the album controller
     */
    @FXML
    public void initialize() {
    	list_nodes = tile.getChildren();
    	tile.setOnMouseClicked(this);

        tab.getSelectionModel().selectedItemProperty().addListener(this);
        tab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        pagination.setPageFactory(pageIndex -> {
            User user = get_Model().getCurrentUser();
            Album album = user.getCurrentAlbum();

            if (album.getSize() > 0) {
                Album_Controller.this.setup_CurrentPhoto(pageIndex, false);
                Photo currPhoto = album.getCurrentPhoto();
                return currPhoto.get_Image_Node(arg0 -> tab.getSelectionModel().select(0));
            }
            else {
                return null;
            }
        });
    }

    
    /**
     * @param photo Input photo to setup
     */
    public void setup_CurrentPhoto(Photo photo) {
    	User user = get_Model().getCurrentUser();
    	Album album = user.getCurrentAlbum();
    	ObservableList<Album> albums =user.getListAlbums();
    	int index = album.getPhotoIndex(photo);

    	setup_CurrentPhoto(index, true);
    }

    /**
     * @param newIndex Index for the current photo user has in the album
     * @param isFromAlbum Initializes pagination if true
     */
    public void setup_CurrentPhoto(int newIndex, boolean isFromAlbum) {
    	User user = get_Model().getCurrentUser();
    	Album album = user.getCurrentAlbum();
    	ObservableList<Album> albums =user.getListAlbums();
    	
		int oldIndex = album.getIndexCurrentPhoto();
    	
		newIndex = album.setIndexCurrentPhoto(newIndex);
		Photo newPhoto = album.getCurrentPhoto();
		
    	if (newIndex > -1) {
	    	if (list_nodes.size()>0) {
	    		if (oldIndex==-1) {
			        BorderPane currViewWrapper = (BorderPane)list_nodes.get(newIndex);
			        currViewWrapper.setStyle(style_blue);
	    		}
	    		else if (oldIndex!=newIndex) {
			        BorderPane oldViewWrapper = (BorderPane)list_nodes.get(oldIndex);
			        BorderPane currViewWrapper = (BorderPane)list_nodes.get(newIndex);

			        oldViewWrapper.setStyle(style_white);
			        currViewWrapper.setStyle(style_blue);
		    	}
		    	else {
			        BorderPane currViewWrapper = (BorderPane)list_nodes.get(newIndex);
			        currViewWrapper.setStyle(style_blue);
		    	}
	    	}
	        list_Tag.setItems(newPhoto.getTags());	    	
	        if (newPhoto.getTags().size() > 0) {
	        	list_Tag.getSelectionModel().select(0);
	        }

	        if (isFromAlbum) {
	        	pagination.setPageCount(album.getSize());
	        	pagination.setCurrentPageIndex(newIndex);
	        }
    	}
    }


    /**
     * @param event Mouse clicks events
     */
    @Override
	public void handle(MouseEvent event) {
		Object obj = event.getSource();
		if (obj instanceof TilePane) {
	        if (event.getButton() == MouseButton.SECONDARY) {
	        	cm_tile.show(tile, event.getScreenX(), event.getScreenY());
	        }

	        event.consume();
		}
		else if (obj instanceof ImageView) {
			ImageView view = (ImageView)obj;

	        if (event.getButton().equals(MouseButton.PRIMARY)) {
	            if (event.getClickCount() == 1) {
	            	Object obj1 = view.getUserData();
	            	Photo onePhoto = (Photo)obj1;

	            	setup_CurrentPhoto(onePhoto);
	            }
	            else {
	            	Object obj1 = view.getUserData();
	            	Photo onePhoto = (Photo)obj1;
	            	setup_CurrentPhoto(onePhoto);

	            	tab.getSelectionModel().select(1);
	            }
	        }
	        else if (event.getButton() == MouseButton.SECONDARY) {
            	Object obj1 = view.getUserData();
            	Photo onePhoto = (Photo)obj1;
            	
            	Photos_Model model = get_Model();
            	User user = model.getCurrentUser();
            	ObservableList<Album> albums =user.getListAlbums();
            	Album album = user.getCurrentAlbum();

                List<Album> albumList = Helper.filter(albums, album, (t,u)->t!=u&&(!t.getAlbumName().equalsIgnoreCase(Album.ALBUM_NAME_SEARCH_BY_DATE) && !t.getAlbumName().equalsIgnoreCase(Album.ALBUM_NAME_SEARCH_BY_TAG)));

            	cmCopy.getItems().clear();
            	for (Album a : albumList) {
                	MenuItem cmItemAlbum = new MenuItem(a.getAlbumName());
                	cmItemAlbum.setOnAction(e -> {
                        MenuItem mi =  (MenuItem)e.getSource();

                        User user1 = get_Model().getCurrentUser();

                        Photo onePhoto1 = (Photo)cm.getUserData();
                        String albumTarget = mi.getText();

                        user1.copyPhoto(onePhoto1, albumTarget);
                    });
                	cmCopy.getItems().add(cmItemAlbum);
            	}
            	 
            	cmMove.getItems().clear();
            	for (Album a : albumList) {
                	MenuItem cmItemAlbum = new MenuItem(a.getAlbumName());
                	cmItemAlbum.setOnAction(e -> {
                        MenuItem mi =  (MenuItem)e.getSource();
                         
                        Photo onePhoto12 = (Photo)cm.getUserData();
                        String albumTarget = mi.getText();
                         
                        User user12 = get_Model().getCurrentUser();
                        Album album1 = user12.getCurrentAlbum();
                         
                        int index = album1.getPhotoIndex(onePhoto12);
                         
                        list_nodes.remove(index);
                         
                        user12.movePhoto(onePhoto12, albumTarget);
                         
                        int iCurr = album1.getIndexCurrentPhoto();
                        setup_CurrentPhoto(iCurr, true);
                    });
                	cmMove.getItems().add(cmItemAlbum);
            	}
            	 
	        	cm.setUserData(onePhoto);
	        	cm.show(view, event.getScreenX(), event.getScreenY());
	        }
	         
	        event.consume();
		}
	}

    /**
     * Logs off album scene and goes to login
     */
    public void do_Logoff() {
	    goto_Login_From_Album();
	}

    /**
     * Exits album
     */
    public void do_Exit() {
		Platform.exit();
	}


    /**
     * Tab switching event handling
     */
	@Override
	public void changed(ObservableValue<? extends Tab> arg0, Tab arg1, Tab arg2) {
		if (arg2.getText().equals("Photo")) {
	    	User user = get_Model().getCurrentUser();
	    	Album album = user.getCurrentAlbum();
        	Photo currPhoto = album.getCurrentPhoto();
			 
        	if (currPhoto!=null) {
        		pagination.setVisible(true);
        		 
	        	int index = album.getPhotoIndex(currPhoto);
	        	 
				pagination.setPageCount(album.getSize());
				pagination.setCurrentPageIndex(index);
				 			     
        	}
        	else {
        		pagination.setVisible(false);
        	}
		}
		else {

		}
	}


    /**
     * Deletes tag
     */
    public void do_Delete_Tag() {
    	User user = get_Model().getCurrentUser();
    	Album album = user.getCurrentAlbum();
    	 
    	Photo currPhoto = album.getCurrentPhoto();
    	 
    	int index = list_Tag.getSelectionModel().getSelectedIndex();
    	 
    	currPhoto.delete_Tag(index);
    	 
    	list_Tag.refresh();
	}

    /**
     * Adds tag
     */
    public void do_Add_Tag() {
    	String name = tagName.getText().trim();
    	String value = tagValue.getText().trim();
    	 
    	if (name.length()>0 && value.length()>0) {
	    	User user = get_Model().getCurrentUser();
	    	Album album = user.getCurrentAlbum();
	    	 
	    	Photo currPhoto = album.getCurrentPhoto();
	    	boolean ret = currPhoto.add_Tag(name, value);
	    	 
			if (ret) {
                list_Tag.refresh();
                 
                tagName.setText("");
                tagValue.setText("");
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Not complete");
                alert.setContentText("Duplicate Name and Value Pair. Please try again");
                alert.showAndWait();
            }
    	}
    	else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Not complete");
            alert.setContentText("Name and Value Fields are empty. Please Enter again");
            alert.showAndWait();
    	}
    	
    }


    /**
     * Goes to list of albums
     */
    public void goto_AlbumList() {
    	goto_User_From_Album();
    }


    /**
     * Shows Photo's help
     */
    public void doHelp() {
		Help(PHOTOS_HELP_FXML);
	}
}