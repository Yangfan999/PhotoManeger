package model;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Menages all images
 * @author QiZeng, yangfanli, yuyuexin
 * @version IntelliJ IDEA 2017.2.4
 */
public class Images {
    private static HashMap<String, ImageFile> imageList = new HashMap<>();
    private static HashMap<String , ImageFile> allSavedImage = new HashMap<>();
    private File directory;
    private static final String pattern = ".*?(.jpg|.BMP|.TIFF|.JPEG|.JPG|.jpeg|.bmp|.tiff)"; //regular expression
    private static final ImageLogger LOGGER = new ImageLogger(Images.class.getName());
    private static final String IMG_PATH = System.getProperty("user.dir") + File.separator +  "images";

    /**
     * Sets directory
     * @param dir : set this image to dir
     */
    public void setDirectory(File dir) {
        sync();
        imageList.clear();
        directory = dir;
    }

    /**
     * Updates images in allSavedImage
     */
    private void sync(){
        for (String key: imageList.keySet()) {
            allSavedImage.put(key, imageList.get(key));
        }
    }

    /**
     * Finds all images under this directory.
     */
    public void findImage() {
        find(directory);
    }

    /**
     * Finds all images under dir (private helper method).
     * @param dir File folder
     */
    private void find(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    find(f);
                } else if (f.getName().matches(pattern)) {
                    ImageFile img;
                    String name = f.getName();
                    Tags tagsClass = new Tags();
                    if (!allSavedImage.containsKey(name)) { // new image file
                        if (name.contains("@")) {
                            img = new ImageFile(name.substring(0, name.indexOf("@")) +
                                    name.substring(name.indexOf("."), name.length()));
                            String tagName = name.substring(name.indexOf("@") + 1, name.lastIndexOf("."));
                            ArrayList<String> tagNames = new ArrayList<>(Arrays.asList(tagName.split("@")));
                            img.editTags(tagsClass.newFoundTags(tagNames), true);
                        } else {
                            img = new ImageFile(name); // toString -> xxx.jpg
                        }
                        String path = f.getAbsolutePath();
                        img.setDir(path.substring(0, path.lastIndexOf(File.separator)));
                    } else { // exist image file
                        img = allSavedImage.get(name);
                        if (name.contains("@")) {
                            String tagName = name.substring(name.indexOf("@") + 1, name.lastIndexOf("."));
                            ArrayList<String> tagNames = new ArrayList<>(Arrays.asList(tagName.split("@")));
                            tagsClass.newFoundTags(tagNames);
                        }
                    }
                    imageList.put(name, img);
                }
            }
        }
    }

    /**
     * Gets imageList
     * @return HashMap all images file
     */
    public HashMap<String, ImageFile> getImageList() {
        return imageList;
    }

    /**
     * Saves all images in to folder images
     */
    public void saveImages() {
        File images = new File(IMG_PATH);
        if (!images.exists() && !images.mkdir()) {
            System.out.println("Cannot create images file");
        }
        sync();
            deleteAllImages();
            for (String key: allSavedImage.keySet()) {
                try {
                    FileOutputStream fileOut = new FileOutputStream(
                            IMG_PATH + File.separator + key.substring(0, key.lastIndexOf(".")) + ".ser");
                    ObjectOutputStream outImg = new ObjectOutputStream(fileOut);
                    outImg.writeObject(allSavedImage.get(key));
                    outImg.close();
                    fileOut.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * Deletes all file from directory images
     */
    private void deleteAllImages(){
        File[] files = new File(IMG_PATH).listFiles();
        if(files != null) {// list() for all file names
            for (File file : files) {//listFiles() for all file paths
                if(!file.delete()) {
                    System.out.println("Something wrong in method deleteAllImages in Images.java");
                }
            }
        }
    }

    /**
     * Loads all saved Image classes in to allSavedImage
     */
    public void loadImages() {
        File[] files = new File(IMG_PATH).listFiles();
        if (files != null) {
            for (File f : files) {
                try {
                    FileInputStream fileIn = new FileInputStream(f);
                    ObjectInputStream imgIn = new ObjectInputStream(fileIn);
                    ImageFile img = (ImageFile)imgIn.readObject();
                    allSavedImage.put(img.toString(), img);
                    imgIn.close();
                    fileIn.close();
                } catch (IOException | ClassNotFoundException i) {
                    i.printStackTrace();
                }
            }
        }
    }

    /**
     * Changes the path of target image
     * @param img: target image class
     * @param dir: new path
     * @return boolean either the move is successful or not
     */
    public boolean changePath(ImageFile img, String dir) {
        String name = img.toString();
        ImageFile temp = imageList.get(name); // get target image class
        // change actual file path
        File image = new File(temp.getDir() + File.separator + name); // original file
        if (image.exists()) {
            boolean moved = image.renameTo(new File(dir + File.separator + name)); // move
            if (moved) {
                // change corresponding Image class in imageList
                temp.setDir(dir);
                imageList.put(name, temp);
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all image files having the tags the customer chosen
     * @param tags: a list of tags that the customer asked to see all images with this tag
     * @return ArrayList showing all image files with tags
     */
    public ArrayList<ImageFile> getImagesWithTags(ArrayList<Tag> tags) {
        ArrayList<ImageFile> result = new ArrayList<>();
        for (String name : imageList.keySet()) {
            ImageFile img = imageList.get(name);
            if (img.getCurrentTags().containsAll(tags)) {
                result.add(img);
            }
        }
        return result;
    }

    /**
     * Finds corresponding image file
     * @param img Image class
     * @return FileInputStream image file
     * @throws IOException cannot found image file
     */
    public FileInputStream findFile(ImageFile img) throws IOException {
        return new FileInputStream(img.getDir() + File.separator + img.toString());
    }

    /**
     * Renames the image file name
     * @param img: target image file
     * @param newOriName: new image name
     * @return boolean true if successfully rename image
     */
    public boolean renameImage(ImageFile img, String newOriName) {
        String name = img.toString();
        ImageFile temp = imageList.get(name);
        String oriName = temp.getOriName();
        String path = temp.getDir();
        temp.changeOriName(newOriName);
        String newName = temp.toString(); // get that class's new name
        if (!renameFile(name, newName, path, img)){
            temp.changeOriName(oriName);
            return false;
        }
        return true;
    }

    /**
     * Renames image who is named name.
     * @param newTags: tags going to be include
     * @param imageClass: image's current name. e.g. cat.jpg
     * @param addOrDel: a true or false determination by codes in viewer, true for add, false for delete
     */
    public void tagImages(ArrayList<Tag> newTags, ImageFile imageClass, boolean addOrDel){
        String name = imageClass.toString();
        ImageFile temp = imageList.get(name); // find target's corresponding Image class
        String path = temp.getDir();
        temp.editTags(newTags, addOrDel);
        String newName = temp.toString(); // get that class's new name
        // change actual file name
        if (!renameFile(name, newName, path, temp)){
            temp.editTags(newTags, !addOrDel);
        }
    }

    /**
     * Finds target's corresponding Image class and gets this class's new name
     * @param oldName: list of old names
     * @param image: target image file
     */
    public void changeBack(ArrayList<Tag> oldName, ImageFile image) {
        String name = image.toString();
        ImageFile temp = imageList.get(name); // find target's corresponding Image class
        String path = temp.getDir();
        temp.changeBack(oldName);
        String newName = temp.toString(); // get that class's new name
        // change actual file name
        if (!renameFile(name, newName, path, temp)){
            image.changeBack(oldName);
        }
    }

    /**
     * Renames this image file's name and log the change at the same time
     * @param oriName: the old name
     * @param newName: the new name
     * @param path: the specific path of the image
     * @param img: target image class
     * @return boolean true if the file is renamed by its newName
     */
    private boolean renameFile(String oriName, String newName, String path, ImageFile img) {
        File oriImage = new File(path + File.separator + oriName); // target the actual image
        File newImage = new File(path + File.separator + newName);
        if (oriImage.renameTo(newImage)) {
            imageList.remove(oriName);
            imageList.put(newName, img);
            allSavedImage.remove(oriName);
            if (!newName.equals(oriName)) {
                LOGGER.log(oriName + " -> " + newName);
                img.addHistory(oriName + " -> " + newName);
            }
            return true;
        }
        return false;
    }
}
