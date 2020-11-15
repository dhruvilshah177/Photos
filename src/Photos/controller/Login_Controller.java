package Photos.controller;

import Photos.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert;



/**
 * This class represents all the functionality login has
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Login_Controller extends Controller_Base implements Controller_Interface {

    /**
     * Username to login
     */
    @FXML TextField userID;


    /**
     * Initializes the login stage
     */
    public void initBeforeShow() {
		get_Model().setCurrentUser(null);
		userID.clear();
	}


    /**
     * logs in after enter key is hit
     * @param keyEvent Key pressed. We only care about the 'enter' key
     */
    public void userID_KeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
        	do_Login();
        }
	}


    /**
     * To log in
     */
    public void do_Login() {
    	String user_id = userID.getText().trim();
    	 
    	User user = get_Model().getUser(user_id);
    	if (user!=null) {
    		get_Model().setCurrentUser(user);
        	if (user_id.equalsIgnoreCase("admin")) {
            	goto_Admin_From_Login();
        	}
        	else {
            	goto_User_From_Login();
        	}
    	}
    	else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Not registered User");
			alert.setContentText("Username is not found");
			alert.showAndWait();
    		userID.setText("");
    	}
    }


    /**
     * To exit app
     */
    public void do_Exit() {
		Platform.exit();
	}


    /**
     * To show Photo's help
     */
    public void doHelp() {
		Help(PHOTOS_HELP_FXML);
	}
}