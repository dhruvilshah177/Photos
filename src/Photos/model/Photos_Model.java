package Photos.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents all the data for this application.
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Photos_Model implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 2564420476827262671L;

    /**
     * @param isStored Clean up before store to file or after retrieve from file
     */
    public void do_CleanUp(boolean isStored) {
        if (isStored) {
            list_Of_Users_To_Keep = new ArrayList<>(list_Users);
            list_Users = null;
            //
            //
            for (User u : list_Of_Users_To_Keep) {
                u.do_CleanUp(true);
            }
        }
        else {
            list_Users = FXCollections.observableList(list_Of_Users_To_Keep);
            list_Of_Users_To_Keep = null;
            //
            for (User u : list_Users) {
                u.do_CleanUp(false);
            }
        }
    }

    /**
     * List of users
     */
    private ObservableList<User> list_Users;

    /**
     * used when store model to file as ObservableList could not be Serialized
     */
    private ArrayList<User> list_Of_Users_To_Keep;

    /**
     * Current user
     */
    private User currentUser;


    /**
     * Initializes fields
     */
    public Photos_Model() {
        list_Users = FXCollections.observableArrayList();
        list_Of_Users_To_Keep = null;
        currentUser = null;
    }


    /**
     * @return List of users
     */
    public ObservableList<User> getListUsers() {
        return list_Users;
    }


    /**
     * @return Current user
     */
    public User getCurrentUser() {
        return currentUser;
    }


    /**
     * @param currentUser Current user
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }


    /**
     * @param userName Username
     */
    public void add_User(String userName) {
        User user = new User(userName);
        //
        Helper.addOrRetrieveOrderedList(list_Users, user);
    }



    public User delete_User(String userName) {
        if (!userName.equalsIgnoreCase(User.ADMIN_USER)) {
            return Helper.delete(list_Users, userName, (t,k)->t.getUsername().equalsIgnoreCase(k));
        }
        else {
            return null;
        }
    }


    /**
     * @param i Index of user in the list to delete
     */
    public void delete_User(int i) {
        if (i>=0 && i<list_Users.size()) {
            if (!list_Users.get(i).getUsername().equalsIgnoreCase(User.ADMIN_USER)) {
                list_Users.remove(i);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Deleting Admin.");
                alert.setContentText("Admin cannot be deleted.");
                alert.showAndWait();
            }
        }
    }


    /**
     * @param userName Username
     * @return User
     */
    public User getUser(String userName) {
        return list_Users.stream().filter(user->user.getUsername().equalsIgnoreCase(userName)).findFirst().orElse(null);
    }
}