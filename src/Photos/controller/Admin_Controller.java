package Photos.controller;

import Photos.model.Photos_Model;
import Photos.model.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


/**
 * This class has the functionalities of an admin account
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Admin_Controller extends Controller_Base implements Controller_Interface, ChangeListener<User> {

    /**
     * ListView is the list of all Users
     */
    @FXML
    ListView<User> list_View;

    /**
     * TextField to add the name of the user
     */
    @FXML
    TextField name;

    public Admin_Controller() {

    }

    /**
     * To clear name field
     */
    public void initBeforeShow() {
        name.clear();
	}


    /**
     * To initializes admin scene
     */
    public void initialize() {
    	Photos_Model model = get_Model();

        list_View.setItems(model.getListUsers());
        list_View.getSelectionModel().selectedItemProperty().addListener(this);

        if (model.getListUsers().size() > 0) {
            list_View.getSelectionModel().select(0);
        }
    }


    /**
     * To delete user
     */
    public void do_Delete() {
    	Photos_Model model = get_Model();
    	int index = list_View.getSelectionModel().getSelectedIndex();
    	if (index>=0) {
    		model.delete_User(index);

    		list_View.refresh();
    	}
    }


    /**
     * To add user
     */
    public void do_Add() {
    	String username = name.getText().trim();

    	Photos_Model model = get_Model();
        if (!username.isEmpty()) {
        	User user = model.getUser(username);

            if (user!=null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Name should be unique case insensitive");
                alert.setContentText("Another user with this username exists already. Please choose another");
                alert.showAndWait();
            }
            else {
                model.add_User(username);
                name.clear();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Username");
            alert.setContentText("Username should not be empty");
            alert.showAndWait();
        }
    }


    /**
     * To go to albums list
     */
    public void goto_AlbumList() {
        goto_User_From_Admin();
    }


    /**
     * To log the admin off
     */
    public void do_Logoff() {
	    goto_Login_From_Admin();
	}


    /**
     * To exit app
     */
    public void do_Exit() {
		Platform.exit();
	}


    /**
     * To show admin's help
     */
    public void doHelp() {
		Help(ADMIN_HELP_FXML);
	}
	
	@Override
	public void changed(ObservableValue<? extends User> arg0, User arg1, User arg2) {
	}
}
