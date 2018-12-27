package model;
import java.io.*;
import java.util.ArrayList;

/**
 * Menages all tags
 * @author QiZeng, yangfanli, yuyuexin
 * @version IntelliJ IDEA 2017.2.4
 */
public class Tags {
    private static ArrayList<Tag> listTags = new ArrayList<>();
    private static final String TAG_PATH = System.getProperty("user.dir") + File.separator +  "tags";

    /**
     * Gets listTags
     * @return ArrayList<Tag> list of tags
     */
    public ArrayList<Tag> getListTags() {
        return listTags;
    }

    /**
     * Adds a tag into listTags if it's a new tag
     * @param tagName: the name of this new tag
     */
    public void addTag(String tagName){
        Tag newTag = new Tag(tagName);
        if (!listTags.contains(newTag)) {
            listTags.add(newTag);
        }
    }

    /**
     * Adds new tags from new images into listTags
     * @param tagNames: a list of the names of the new tags
     * @return ArrayList a list of tags
     */
    ArrayList<Tag> newFoundTags(ArrayList<String> tagNames) {
        ArrayList<Tag> tagList = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag newTag = new Tag(tagName);
            tagList.add(newTag);
            if (!listTags.contains(newTag)) {
                listTags.add(newTag);
            }
        }
        return tagList;
    }

    /**
     * Deletes a tag from listTags
     * @param tags: the tag asked to be deleted
     */
    public void deleteTag(ArrayList<Tag> tags){
        listTags.removeAll(tags);
    }

    /**
     * Saves all tags from listTags into directory tags by serialization
     */
    public void saveTags(){
        File tags = new File(TAG_PATH);
        if (!tags.exists() && !tags.mkdir()) {
            System.out.println("Cannot create tags file");
        }
        deleteAllTags();
        for (Tag t: listTags) {
            try
            { FileOutputStream fileOut =
                        new FileOutputStream(TAG_PATH + File.separator + t.toString() + ".ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(t);
                out.close();
                fileOut.close();
            }catch(IOException i) {
                i.printStackTrace();
            }
        }
    }

    /**
     * Delete all file from directory tags
     */
    private void deleteAllTags(){
        File[] files = new File(TAG_PATH).listFiles();
        if(files != null) {// list() for all file names
            for (File file : files) {//listFiles() for all file paths
                if(!file.delete()) {
                    System.out.println("Something going wrong in method deleteAllTags");
                }
            }
        }
    }

    /**
     * Reads all files from directory tags, store them into listTags, and delete all of them.
     */
    public void loadTags(){ //read all files in tags (directoryName should always is tags).
        File dirFile = new File(TAG_PATH);
        //obtain all files's name in this directory.
        String[] fileList = dirFile.list();
        if(fileList != null) {
            for (String tagName : fileList) {// read all files.
                //tag t = null;
                try {
                    FileInputStream fileIn = new FileInputStream(TAG_PATH + File.separator + tagName);
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    Tag t = (Tag) in.readObject(); //assign the information in file to tag t.
                    in.close();
                    fileIn.close();
                    if (!listTags.contains(t)) {
                        listTags.add(t); //add tag into listTags.
                    }
                } catch (IOException | ClassNotFoundException j) {
                    j.printStackTrace();
                }
            }
        }
    }
}
