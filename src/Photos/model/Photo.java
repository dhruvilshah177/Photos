package Photos.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Photo implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 6582167387220829767L;

    /**
     * File type of thumb nail
     */
    private static final String THUMBNAIL_IMAGE_FORMAT 	= "png";

    /**
     * Width of thumb nail
     */
    private static final int THUMBNAIL_WIDTH = 120;

    /**
     * Height of thumb nail
     */
    private static final int THUMBNAIL_HEIGHT= 120;

    /**
     * Path to store thumb nails
     */
    private static final String	THUMBNAIL_PATH = "thumbnails";


    /**
     * creating thumb nail folder
     */
    static {new File(THUMBNAIL_PATH).mkdir();}


    /**
     * @param input Input thumb nail
     * @return Thumb nail file name
     */
    public static String get_ThumbNail_FileName(String input) {
        return THUMBNAIL_PATH + "/" + input + "." + THUMBNAIL_IMAGE_FORMAT;
    }


    /**
     * @param filename File name
     * @param file Photo file
     * @return New photo created
     */
    public static Photo create_Photo(String filename, File file) {
        if (file==null) {
            file = new File(filename);
        }
        long lastModified = file.lastModified();
        String shortFileName = file.getName();
        int pos = shortFileName.indexOf('.');
        if (pos>0) {
            shortFileName = shortFileName.substring(0, pos);
        }
        //
        String thumbnail = String.valueOf(UUID.randomUUID());
        boolean ret = create_ThumbNail(filename, get_ThumbNail_FileName(thumbnail), THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, THUMBNAIL_IMAGE_FORMAT);
        //
        if (ret) {
            return new Photo(filename, thumbnail, shortFileName, lastModified);
        }
        return null;
    }


    /**
     * @param photoFileName File name of origin photo
     * @param thumbnailFileName File name of thumb nail
     * @param width Width of thumb nail
     * @param height Height of thumb nail
     * @param thumbnailFormat Format of thumb nail
     * @return True if thumb nail is created
     */
    private static boolean create_ThumbNail(String photoFileName, String thumbnailFileName, int width, int height, String thumbnailFormat) {
        boolean ret = true;
        //
        Image image = null;
        try {
            image = new Image(new FileInputStream(photoFileName), width, height, true, true);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //
        if (image!=null) {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            //
            try {
                FileOutputStream fos = new FileOutputStream(thumbnailFileName);
                ImageIO.write(bufferedImage, thumbnailFormat, fos);
                fos.close();
            }
            catch (IOException e) {
                ret = false;
                e.printStackTrace();
            }
        }
        else {
            ret = false;
        }
        //
        return ret;
    }

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
     * File name of the photo
     */
    private String file_Name;


    /**
     * Thumbnail name
     */
    private String thumbnail;


    /**
     * Caption that a photo has
     */
    private String caption;


    /**
     * Date of when photo was taken
     */
    private long date_Of_Photo;

    /**
     * List of tags the photo has
     */
    private ObservableList<Tag> list_Tags;

    /**
     * List of tags to keep
     */
    private ArrayList<Tag> list_Of_Tags_To_Keep;


    /**
     * @param _fileName File name of photo
     * @param _thumbnail Thumbnail created from photo
     * @param _caption Caption of photo
     * @param _dateOfPhoto Date of photo
     */
    private Photo(String _fileName, String _thumbnail, String _caption, long _dateOfPhoto) {
        file_Name = _fileName;
        thumbnail = _thumbnail;
        caption = _caption;
        date_Of_Photo = _dateOfPhoto;
        //
        list_Tags = FXCollections.observableArrayList();
        list_Of_Tags_To_Keep = null;
    }

    /**
     * @return List of tags
     */
    public ObservableList<Tag> getTags() {
        return list_Tags;
    }

    /**
     * @param photo Clone a photo
     */
    public Photo(Photo photo) {
        this.file_Name = photo.file_Name;
        this.thumbnail = String.valueOf(UUID.randomUUID());
        this.caption = photo.caption;
        this.date_Of_Photo = photo.date_Of_Photo;
        //
        list_Tags 		= FXCollections.observableArrayList();
        for (Tag t: photo.list_Tags) {
            list_Tags.add(new Tag(t));
        }
        //
        try {
            Files.copy(new File(photo.get_ThumbNail_FileName()).toPath(), new File(get_ThumbNail_FileName()).toPath());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return File name of thumbnail
     */
    private String get_ThumbNail_FileName() {
        return get_ThumbNail_FileName(thumbnail);
    }


    /**
     * @return File name of photo
     */
    public String getFileName() {
        return file_Name;
    }


    /**
     * @param file_Name File name of photo
     */
    public void setFileName(String file_Name) {
        this.file_Name = file_Name;
    }


    /**
     * @return Caption of photo
     */
    public String getCaption() {
        return caption;
    }


    /**
     * @param caption Caption of photo
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }


    /**
     * @return Date of photo taken
     */
    public long getDateOfPhoto() {
        return date_Of_Photo;
    }

    /**
     * @return Localtime of photo taken
     */
    public String getDateOfPhotoString() {
        return epoch_To_LocalTime(date_Of_Photo);
    }


    /**
     * @param tagName Tag name
     * @param tagValue Tag value
     * @return True if tag is added
     */
    public boolean add_Tag(String tagName, String tagValue) {
        Tag t = new Tag(tagName, tagValue);
        //
        return Helper.addOrRetrieveOrderedList(list_Tags, t);
    }


    public Tag delete_Tag(String tagName, String tagValue) {
        Tag t = new Tag(tagName, tagValue);
        //
        return Helper.delete(list_Tags, t, (tt,kk)->tt.compareTo(kk)==0);
    }

    /**
     * @param i Index of tag to delete
     */
    public void delete_Tag(int i) {
        if (i>=0 && i<list_Tags.size()) {
            list_Tags.remove(i);
        }
    }

    /**
     * @param handler Handler for mouse events
     * @param style Style of Image View
     * @return Thumb nail Image View
     */
    public BorderPane get_ThumbNail_Image_View(EventHandler<MouseEvent> handler, String style) {
        Image image = new Image("File:"+get_ThumbNail_FileName(), 0, 0, false, false);
        ImageView view = new ImageView(image);
        view.setFitWidth(THUMBNAIL_WIDTH);
        view.setFitHeight(THUMBNAIL_HEIGHT);
        //
        view.setOnMouseClicked(handler);
        view.setUserData(this);
        //
        Photo thisPhoto = this;
        TextField textfield = new TextField(getCaption());
        textfield.setPrefWidth(THUMBNAIL_WIDTH);
        textfield.setOnAction(event -> {
            TextField textField = (TextField) event.getSource();
            String temp = textField.getText().trim();
            if (temp.length()==0) {
                textField.setText(thisPhoto.getCaption());
            }
            else {
                thisPhoto.setCaption(temp);
            }
        });
        //
        VBox vbox = new VBox(4); 	// spacing = 4
        vbox.getChildren().addAll(view, textfield, new Label(getDateOfPhotoString()));
        //
        BorderPane viewWrapper = new BorderPane(vbox);
        viewWrapper.setStyle(style);
        //
        return viewWrapper;
    }

    /**
     * @param handler Mouse events handler
     * @return Node to render image
     */
    public Node get_Image_Node(EventHandler<MouseEvent> handler) {
        ImageView image;
        Image imageR = null;
        try {
            imageR = new Image(new FileInputStream(getFileName()));
        }
        catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        //
        image = new ImageView();
        //
        image.setFitWidth(650);
        image.setFitHeight(450);
        image.setPreserveRatio(true);
        image.setSmooth(true);
        image.setImage(imageR);
        image.setOnMouseClicked(handler);
        //
        VBox vbox = new VBox(2); 	// spacing = 2
        //
        vbox.getChildren().addAll(new Label("Caption: " + getCaption() + ". This photo was taken at " + getDateOfPhotoString()), image);
        //
        return vbox;
    }


    /**
     * @param dates Range of dates
     * @return True if within range of dates
     */
    public boolean is_Within_Range(Time_Search_Condition dates) {
        ZoneId zoneId = ZoneId.systemDefault();
        //
        LocalDate startDate 	= dates.getStartDate();					//starting of startdate
        LocalDate endDate	 	= dates.getEndDate().plusDays(1);		//end of enddate
        //
        long start 	= startDate.atStartOfDay(zoneId).toEpochSecond();
        long end 	= endDate.atStartOfDay(zoneId).toEpochSecond();
        //
        Date date_Of_Photo = new Date(this.date_Of_Photo);
        LocalDateTime date = date_Of_Photo.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long toCheck = date.atZone(zoneId).toEpochSecond();

        //
        return toCheck >= start && toCheck < end;
    }


    /**
     * @param time Input time
     * @return Last modified time converted to String
     */
    public static String epoch_To_LocalTime(long time) {
        LocalDateTime datetime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        //
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //
        return datetime.format(formatter);
    }
}