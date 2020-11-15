package Photos.controller;

import Photos.model.Album;
import Photos.model.Photo;
import Photos.model.Photos_Model;
import Photos.model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Base class for all controllers
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Controller_Base {

    /**
     * Admin's help pop-up
     */
    public static final String ADMIN_HELP_FXML 	= "/Photos/view/AdminHelp.fxml";

    /**
     * App's help pop-up
     */
    public static final String PHOTOS_HELP_FXML = "/Photos/view/PhotosHelp.fxml";

    /**
     * File when storing data
     */
	public final static String DATA_FILE_PATH 	= "photo46.dat";

    /**
     * Login scene
     */
    protected static Scene scene_Login = null;

    /**
     * Admin scene
     */
    public static Scene scene_Admin = null;

    /**
     * User scene
     */
    public static Scene scene_User = null;

    /**
     * Album scene
     */
    public static Scene scene_Album = null;

    /**
     * login Controller
     */
    public static Controller_Interface controller_Login = null;

    /**
     * admin Controller
     */
    public static Controller_Interface controller_Admin = null;

    /**
     * user Controller
     */
    public static Controller_Interface controller_User = null;

    /**
     * album Controller
     */
    public static Controller_Interface controller_Album = null;

    /**
     * Login stage
     */
    public static Stage login_Stage = null;

    /**
     * Album stage
     */
    public static Stage album_Stage = null;


    /**
     * @param filename Filename for help pop-up
     */
    public static void Help(String filename) {
    	Parent root;
		try {
			root = FXMLLoader.load(Controller_Base.class.getResource(filename));
	    	Stage window = new Stage();
	    	window.initModality(Modality.APPLICATION_MODAL);

			Scene scene = new Scene(root);
			window.setScene(scene);

			// Set properties of stage
			window.setTitle("Help");
			window.setResizable(false);

			// Render it
			window.show();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
    }


    /**
     * @param main_Stage Main stage of app
     * @throws Exception For if the stage does not start
     */
    public static void start(Stage main_Stage) throws Exception {
		{	FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Controller_Base.class.getResource("/Photos/view/Login.fxml"));
			Parent root = loader.load();
			scene_Login = new Scene(root);
			controller_Login = loader.getController();
		}
		//
		{	FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Controller_Base.class.getResource("/Photos/view/Admin.fxml"));
			Parent root = loader.load();
			scene_Admin = new Scene(root);
			controller_Admin = loader.getController();
		}
		//
		{	FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Controller_Base.class.getResource("/Photos/view/User.fxml"));
			Parent root = loader.load();
			scene_User = new Scene(root);
			controller_User = loader.getController();
		}
		//
		{	FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Controller_Base.class.getResource("/Photos/view/Album.fxml"));
			Parent root = loader.load();
			scene_Album = new Scene(root);
			controller_Album = loader.getController();
		}
		//
		main_Stage.setTitle("Not yet");
		main_Stage.setResizable(false);
		//
		login_Stage = main_Stage;
		//
		album_Stage = new Stage();
		main_Stage.setTitle("Not yet");
		album_Stage.setResizable(false);
		//
		goto_Login();
	}

    /**
     * Model for photos
     */
    private static Photos_Model model;

    /**
     * @return model Retrieve the model from file
     */
    public static Photos_Model get_Model() {
        if (model == null) {
            try {
                FileInputStream fileIn = new FileInputStream(DATA_FILE_PATH);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                model = (Photos_Model)in.readObject();
                model.do_CleanUp(false);
                in.close();
                fileIn.close();
            }
            catch(IOException | ClassNotFoundException i) {
                model = null;
            }

            if (model==null) {
                model = new Photos_Model();

                model.add_User("admin");
                model.add_User("stock");

                model.setCurrentUser(model.getUser("stock"));

                User user = model.getCurrentUser();                //
                Album album1 = user.add_Album("spring");
                album1.add_Photo_To_Album(Photo.create_Photo("stockphotos/spring1.jpg", null));
                album1.add_Photo_To_Album(Photo.create_Photo("stockphotos/spring2.jpg", null));
                album1.add_Photo_To_Album(Photo.create_Photo("stockphotos/spring3.jpg", null));
                album1.add_Photo_To_Album(Photo.create_Photo("stockphotos/spring4.jpg", null));
                album1.add_Photo_To_Album(Photo.create_Photo("stockphotos/spring5.jpg", null));

                Album album2 = user.add_Album("fall");
                album2.add_Photo_To_Album(Photo.create_Photo("stockphotos/fall1.jpg", null));
                album2.add_Photo_To_Album(Photo.create_Photo("stockphotos/fall2.jpg", null));
                album2.add_Photo_To_Album(Photo.create_Photo("stockphotos/fall3.jpg", null));
                album2.add_Photo_To_Album(Photo.create_Photo("stockphotos/fall4.jpg", null));
                album2.add_Photo_To_Album(Photo.create_Photo("stockphotos/fall5.jpg", null));
            }
        }
        //
        return model;
    }


    /**
     * Stores the Photos_Model to file
     */
    public static void store_Model() {
		 if (model!=null) {
	        model.do_CleanUp(true);
			 //
	         try {
	            FileOutputStream fileOut = new FileOutputStream(DATA_FILE_PATH);
	            ObjectOutputStream out = new ObjectOutputStream(fileOut);
	            out.writeObject(model);
	            out.close();
	            fileOut.close();
	         }
	         catch (IOException i) {
	             i.printStackTrace();
	         }
		 }
	 }


    /**
     * Goes to the user's album stage from user
     */
    public void gotoAlbumFromUser() {
        album_Stage.setScene(scene_Album);
        controller_Album.initBeforeShow();
        album_Stage.setTitle("Album " + model.getCurrentUser().getCurrentAlbum().getAlbumName());
        album_Stage.show();
    }


    /**
     * Goes to the Admin's stage from Login
     */
    public static void goto_Admin_From_Login() {
		login_Stage.setScene(scene_Admin);
		//
		controller_Admin.initBeforeShow();
		login_Stage.setTitle("Welcome to the Admin Tool!");
		login_Stage.show();
    }

    /**
     * Goes to Login stage from Admin
     */
    public static void goto_Login_From_Admin() {
    	goto_Login();
    }

    /**
     * Goes to Login stage from Album
     */
    public static void goto_Login_From_Album() {
    	album_Stage.hide();
    	goto_Login();
    }

    /**
     * Goes to Login stage from User
     */
    public static void goto_Login_From_User() {
    	album_Stage.hide();
    	goto_Login();
    }

    /**
     * Goes to the Login stage
     */
    private static void goto_Login() {
    	login_Stage.setScene(scene_Login);
		controller_Login.initBeforeShow();
		login_Stage.setTitle("Welcome to our Photos App");
		login_Stage.show();
    }


    /**
     * Goes to User stage from Login
     */
    public static void goto_User_From_Login() {
		login_Stage.hide();
		goto_User();
    }

    /**
     * Goes to User from Admin
     */
    public static void goto_User_From_Admin() {
		login_Stage.hide();
		goto_User();
    }

    /**
     * Goes to User stage from Album
     */
    public static void goto_User_From_Album() {
		goto_User();
	}

    /**
     * Goes to User stage
     */
    private static void goto_User() {
		album_Stage.setScene(scene_User);
		controller_User.initBeforeShow();
		album_Stage.setTitle("Welcome " + model.getCurrentUser().getUsername() + "!");
		album_Stage.show();
	}
}
