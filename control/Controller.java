package control;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import model.ImageFile;
import model.Images;
import model.Tag;
import model.Tags;
import viewer.AlertBox;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

/**
 * Is used to control model and viewer
 * @author QiZeng, yangfanli, yuyuexin
 * @version IntelliJ IDEA 2017.2.4
 */
public class Controller {

    private static Images images = new Images();
    private static Tags tags = new Tags();
    private static viewer.SingleImage singleImage = new viewer.SingleImage();
    private static  AlertBox alert = new AlertBox();

    /**
     * Gets all the image names under this path
     * @param dir: target directory
     * @return ArrayList names
     */
    public ArrayList<ImageFile> showImage(File dir) {
        images.setDirectory(dir);
        images.loadImages();
        images.findImage();
        return new ArrayList<>(images.getImageList().values());
    }

    /**
     * Opens the chosen image file and shows it in a new window.
     * @param selected: target image file
     */
    public void showSingleImage(ImageFile selected) {
        // the chosen image to open
        //show new window for a single image.
        if (selected == null) {
            alert.display("WHY??", "Select before click!");
        } else {
            singleImage(selected);
        }
    }

    /**
     * Shows the image according to the display method in viewer
     * @param img: target image
     */
    private void singleImage(ImageFile img){
        try {
            singleImage.display(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tells tags to add tag
     * @param newTag user input tag
     * @return ArrayList tagList
     */
    public ArrayList<Tag> startAddTag(TextField newTag) {
        if (newTag.getText().trim().length() == 0) {
            alert.display("Why?", "Illegal Tag");
        } else {
            tags.addTag(newTag.getText());
        }
        return tags.getListTags();
    }

    /**
     * Shows tagList to viewer
     * @return ArrayList tagList
     */
    public ArrayList<Tag> getTagList() {
        tags.loadTags();
        return tags.getListTags();
    }

    /**
     * Tells model to delete user selected tag
     * @param tag selected tag
     * @return ArrayList tagList
     */
    public ArrayList<Tag> deleteTag(ObservableList<Tag> tag) {
        tags.deleteTag(new ArrayList<>(tag));
        return tags.getListTags();
    }

    /**
     * Finds corresponding image file
     * @param img Image class
     * @return FileInputStream image file
     * @throws IOException cannot found image file
     */
    public FileInputStream findFile(ImageFile img) throws IOException {
        return images.findFile(img);
    }

    /**
     * Renames image file to tagList
     * @param tagList tag set use to rename
     * @param img image to rename
     * @param addOrDel: a true or false determination by codes in viewer, true for add, false for delete
     */
    public boolean changeTagsName(ObservableList<Tag> tagList, ImageFile img, boolean addOrDel){
        if(tagList.size() == 0) {
            alert.display("Why?", "Select before click!");
            return false;
        } else {
            ArrayList<Tag> names = new ArrayList<>(tagList);
            images.tagImages(names, img, addOrDel);
            singleImage(img);
            return true;
        }
    }

    /**
     * Changes the image's path
     * @param img image to change path
     * @param path new path
     */
    public void changeDir(ImageFile img, File path) {
        if (path == null) {
            alert.display("Warning", "Please select");
        } else {
            boolean move = images.changePath(img, path.getAbsolutePath());
            if (!move) {
                alert.display("Why???", "failed to move");
            } else {
                alert.display("YES!", "Success!");
            }
        }
    }

    /**
     * Names image to old tag set
     * @param oldTags old tag set
     * @param img image to rename
     * @return boolean true if successfully changes image to any old name
     */
    public boolean changeToOldName(ArrayList<Tag> oldTags, ImageFile img){
        if (oldTags == null) {
            alert.display("Why?", "Select before click!");
            return false;
        } else {
            images.changeBack(oldTags, img);
            singleImage(img);
            return true;
        }
    }

    /**
     * Saves any changes to tags and images
     */
    public void saveFiles() {
        images.saveImages();
        tags.saveTags();
    }

    /**
     * Opens parent directory if this target img exists
     * @param img target image
     */
    public void openParentFolder(ImageFile img) {
        String osName = System.getProperty("os.name");
        try {
            if (osName.toLowerCase().startsWith("windows")) {
                Runtime.getRuntime().exec(new String[] {"cmd","/c","start","",img.getDir()});
            } else {
                String[] osOpen = new String[2];
                osOpen[1] = img.getDir();
                if (osName.toLowerCase().startsWith("linux")) {
                    osOpen[0] = "nautilus";
                } else {
                    osOpen[0] = "open";
                }
                Runtime.getRuntime().exec(osOpen);
            }
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * Changes image name
     * @param newName changed new name with tags
     * @param img the image file asked to change name
     * @return boolean true if successfully change the image name
     */
    public boolean changeOriName(TextField newName, ImageFile img) {
        String name = newName.getText();
        if (name.length() == 0 || name.contains("@") || name.contains(".")) {
            alert.display("You are trying to destroy this program", "Illegal name");
        } else {
            if (!images.renameImage(img, name)) {
                alert.display("Why", "failed to rename");
            } else {
                alert.display("GOOD", "success");
                singleImage(img);
                return true;
            }
        }
        return false;
    }

    /**
     * Call the getImageWithTags method in Images class
     * @param tags: a list of tags that the customer asked to see all images with this tag
     * @return ArrayList list of image files
     */
    public ArrayList<ImageFile> targetImages(ObservableList<Tag> tags) {
        return images.getImagesWithTags(new ArrayList<>(tags));
    }

    /**
     * Gets all history from the change of names in this image file
     * @param img: target image
     * @return String showing the change in file names
     */
    public String history(ImageFile img) {
        return img.getHistory();
    }
}
