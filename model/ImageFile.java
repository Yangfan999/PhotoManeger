package model;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Saves information about a corresponding image file.
 * @author QiZeng, yangfanli, yuyuexin
 * @version IntelliJ IDEA 2017.2.4
 */
public class ImageFile implements java.io.Serializable{
    private String oriName;
    private ArrayList<ArrayList<Tag>> oldTags = new ArrayList<>(); // older tags
    private ArrayList<Tag> currentTags = new ArrayList<>(); // current attached tags
    private String dir; // e.g C:/xxx
    private String history = "";

    /**
     * Gets a new ImageFile
     * @param name: this image's name
     */
    ImageFile(String name) {
        oriName = name; // e.g xxx.jpg
    }

    /**
     * Renames this image with new tags
     * @param tags: new tags to be attached
     */
    void editTags(ArrayList<Tag> tags, boolean addOrDel) {
        ArrayList<Tag> oldName = new ArrayList<>(currentTags);
        if (addOrDel) {
            for (Tag t : tags) {
                if (!currentTags.contains(t)) {
                    currentTags.add(t);
                }
            }
        } else {
            currentTags.removeAll(tags);
        }
        if (!oldTags.contains(oldName) && oldName.size() != 0 && oldName.size() != currentTags.size()) {
            oldTags.add(oldName);
        }
    }

    /**
     * Updates the old tags if the current tags not in the old,
     * and updates current tags to new tags
     * @param tags: new tags to be attached
     */
    void changeBack(ArrayList<Tag> tags) {
        if (!oldTags.contains(currentTags) && currentTags.size() != 0) {
            oldTags.add(currentTags);
        }
        currentTags = tags;
    }

    /**
     * Gets the current tags
     * @return ArrayList current tags
     */
    ArrayList<Tag> getCurrentTags() {
        return currentTags;
    }

    /**
     * Gets all older tags
     * @return ArrayList old tags
     */
    public ArrayList<ArrayList<Tag>> getTags() {
        return oldTags;
    }

    /**
     * Sets/changes parameter directory to dir
     * @param dir: new directory
     */
    void setDir(String dir) {
        this.dir = dir;
    }

    /**
     * Gets parameter directory
     * @return String this directory
     */
    public String getDir() {
        return dir;
    }

    /**
     * Changes/sets the original name
     * @param name: new name
     */
    void changeOriName(String name) {
        oriName = name + oriName.substring(oriName.indexOf("."), oriName.length());
    }

    /**
     * Gets original name of this image class
     * @return String the original name
     */
    public String getOriName() {
        return this.oriName;
    }

    /**
     * Represents this image class by string
     * @return String this image's name
     */
    @Override
    public String toString() {
        StringBuilder name = new StringBuilder(oriName.substring(0, oriName.lastIndexOf(".")));
        for (Tag t: currentTags) {
            name.append("@").append(t.toString());
        }
        name.append(oriName.substring(oriName.lastIndexOf("."), oriName.length()));
        return name.toString();
    }

    /**
     * Adds the change in file name into history
     * @param s: a String called in other class showing the history of changing in file name
     */
    void addHistory(String s) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        history += timestamp + "\n" + s + "\n";
    }

    /**
     * Gets history showing all changes in file names
     * @return String history
     */
    public String getHistory() {
        return history;
    }
}
