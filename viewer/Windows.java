package viewer;

import control.Controller;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.ImageFile;
import model.Tag;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Shows the main window and start the program
 * @author QiZeng, yangfanli, yuyuexin
 * @version IntelliJ IDEA 2017.2.4
 */
public class Windows extends Application {

    Controller control = new Controller();

    private ListView<ImageFile> listImages;

    private ListView<Tag> listTags;

    private File path;

    private ImageView view = new ImageView();

       public static void main(String[] args) {

        launch(args);
    }

    /**
     * Starts the program
     * @param primaryStage:
     * @throws Exception override
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Image Modifier");
        primaryStage.setOnCloseRequest(event -> control.saveFiles());

        Button selectFile = new Button("SELECT DIRECTORY");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select your directory");
        selectFile.setOnAction(
                event -> {
                    File file = directoryChooser.showDialog(primaryStage);
                    if (file != null) {
                        path = file;
                        listImages.getItems().clear();
                        listImages.getItems().addAll(control.showImage(path));
                        listTags.getItems().clear();
                        listTags.getItems().setAll(control.getTagList());
                    }
                }
        );
        Button refreshDirButton = new Button("REFRESH");
        refreshDirButton.setOnAction(event -> {
            if (path != null) {
                listImages.getItems().clear();
                listImages.getItems().addAll(control.showImage(path));
            }
        });

        // Divide the window into two parts, images and tags.
        // Left column - images.
        Label images = new Label("IMAGES");
        images.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        // Add a ListView to show all images names.
        listImages =  new ListView<>();
        listImages.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); //Can only select one Image each time.

        // Click the goImage Button and go to another window that shows the image.
        Button goImage = new Button("GO!");
        goImage.setOnAction(event -> {
            ImageFile selected = listImages.getSelectionModel().getSelectedItem();
            control.showSingleImage(selected);
        });

        listImages.setOnMouseClicked((MouseEvent time) -> {
            if (time.getClickCount() == 2) {
                ImageFile selected = listImages.getSelectionModel().getSelectedItem();
                control.showSingleImage(selected);
            }
            else if (time.getClickCount() == 1) {
                try {
                    FileInputStream fileShow = control.findFile(listImages.getSelectionModel().getSelectedItem());
                    Image img = new Image
                            (fileShow, 360, 280, true, true);
                    fileShow.close();
                    view.setImage(img);
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
        });

        VBox imageColumn = new VBox();
        HBox bottomList = new HBox();
        bottomList.setSpacing(5);
        bottomList.getChildren().addAll(goImage, refreshDirButton);
        imageColumn.setPadding(new Insets(20, 20, 20, 20));
        imageColumn.getChildren().addAll(selectFile, images, listImages, bottomList, view);

        // Right column - tags.
        Label tags = new Label("TAGS");
        tags.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        TextField newTag = new TextField();
        newTag.setPromptText("Type new tag here");
        Button addTag = new Button("ADD");
        addTag.setOnAction(event -> {
            listTags.getItems().setAll(control.startAddTag(newTag));
            newTag.setText("");
        });
        Button deleteTag = new Button("DEL");
        deleteTag.setOnAction(event -> listTags.getItems().setAll(
                control.deleteTag(listTags.getSelectionModel().getSelectedItems())));

        HBox addTagField = new HBox();
        addTagField.setSpacing(10);
        addTagField.getChildren().addAll(newTag, addTag, deleteTag);

        // ListView to show all existing tags.
        listTags = new ListView<>();
        listTags.getItems().setAll(control.getTagList());
        listTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        VBox tagsColumn = new VBox();
        tagsColumn.setPadding(new Insets(20, 20, 20, 20));
        Button selTag = new Button("SHOW IMAGES CONTAINS THE CHOSEN TAG(S)");
        selTag.setOnAction(event -> {
            if (path != null) {
                listImages.getItems().clear();
                listImages.getItems().addAll(control.targetImages(listTags.getSelectionModel().getSelectedItems()));
            }
        });
        Button showAll = new Button(("SHOW ALL IMAGES"));
        showAll.setOnAction(event -> {
            if (path != null) {
                listImages.getItems().clear();
                listImages.getItems().addAll(control.showImage(path));
            }
        });
        tagsColumn.setPadding(new Insets(20, 20, 20, 20));
        tagsColumn.getChildren().addAll(tags, addTagField, listTags, selTag, showAll);
        tagsColumn.setSpacing(2);

        // Set the window layout.
        BorderPane allItems = new BorderPane();
        allItems.setLeft(imageColumn);
        allItems.setRight(tagsColumn);
        allItems.setCenter(view);

        Scene pageDir = new Scene(allItems, 1000, 580);
        primaryStage.setScene(pageDir);
        primaryStage.show();
    }
}
