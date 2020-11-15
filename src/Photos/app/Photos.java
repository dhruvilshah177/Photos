package Photos.app;

import Photos.controller.Controller_Base;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * launches the application and stores the data after it ends
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Photos extends Application {

	/**
	 * @param mainStage Main stage of the app
	 * @throws Exception For if the stage does not start 
	 */
	@Override
	public void start(Stage mainStage) throws Exception {
		Controller_Base.start(mainStage);
	}

	/**
	 * @param args Input command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Stores data once program ends to file
	 */
	@Override
	public void stop(){
        Controller_Base.store_Model();
	}
}