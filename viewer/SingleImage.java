package viewer;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import model.ImageFile;
import model.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Shows single image window
 * @author QiZeng, yangfanli, yuyuexin
 * @version IntelliJ IDEA 2017.2.4
 */
public class SingleImage extends Windows{

    private ListView<Tag> tagSelect;

    private ListView<ArrayList<Tag>> listOldNames;

    /**
     * Gets a new ImageFile
     * @param inputImage: this image's name
     * @throws IOException cannot found image file
     */
    public void display(ImageFile inputImage) throws IOException {

        Stage window = new Stage();
        window.setTitle("Image Modifier - image");
        window.setMinWidth(250);

        window.initModality(Modality.APPLICATION_MODAL);

        // Left column - image.
        Label image = new Label("IMAGE");
        image.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        Text path = new Text(inputImage.getDir() + File.separator + inputImage.toString());
        path.setFill(Color.GRAY);
        // Show image
        FileInputStream file = control.findFile(inputImage);
        Image img = new Image(file,
                480,360,true,true);
        file.close();
        ImageView view = new ImageView();
        view.setImage(img);

        // Show the current image name and the name contains tags.
        Label curNames = new Label("Original Name: " + inputImage.getOriName() + "\n" +
                "Name Includes Tag(s) : " +  inputImage.toString());

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select your directory");
        Button changeDir = new Button("CHANGE DIRECTORY");
        changeDir.setOnAction(event -> {
            File newDir = directoryChooser.showDialog(window);
            control.changeDir(inputImage,newDir);
        });

        TextField changeOriName = new TextField();
        changeOriName.setPromptText("Type new name here");
        Button changeName = new Button("CHANGE ORIGINAL NAME");
        changeName.setOnAction(event -> {
            if (control.changeOriName(changeOriName, inputImage)) {
                window.close();
            }
        });

        Text historyLabel = new Text("HISTORY LOG");
        historyLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        HBox changeNameField = new HBox();
        changeNameField.getChildren().addAll(changeOriName, changeName);

        Button openFolder = new Button("OPEN PARENT DIRECTORY");
        openFolder.setOnAction(event -> control.openParentFolder(inputImage));

        HBox openFileField = new HBox();
        openFileField.getChildren().addAll(changeDir, openFolder);

        TextArea history = new TextArea();
        history.setEditable(false);
        history.setText(control.history(inputImage));
        history.setScrollLeft(20);

        VBox imageColumn = new VBox();
        imageColumn.getChildren().addAll
                (image, path, view, curNames, openFileField, changeNameField, historyLabel, history);
        imageColumn.setSpacing(3);

        // Right column - add tag(s) to image name & change to the past image name.
        Label tags = new Label("TAGS");
        tags.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        tagSelect = new ListView<>();
        tagSelect.getItems().addAll(control.getTagList());
        tagSelect.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // can add multiple tags each time
        Text tagRemark = new Text("Press Control/Command to choose multiple tags.");
        tagRemark.setFill(Color.GRAY);

        Button add = new Button("ADD TAG(S)");
        add.setOnAction(event -> {
            ObservableList<Tag> select = tagSelect.getSelectionModel().getSelectedItems();
            if (control.changeTagsName(select, inputImage, true)) {
                window.close();
            }
        });

        Button del = new Button("DEL TAG(S)");
        del.setOnAction(event -> {
            ObservableList<Tag> select = tagSelect.getSelectionModel().getSelectedItems();
            if (control.changeTagsName(select, inputImage, false)) {
                window.close();
            }
        });

        HBox addDeleteButtons = new HBox();
        addDeleteButtons.getChildren().addAll(add, del);
        addDeleteButtons.setSpacing(10);

        Label oldNames = new Label("OLDER IMAGE TAGS");
        oldNames.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        listOldNames = new ListView<>();
        listOldNames.getItems().addAll(inputImage.getTags());
        listOldNames.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Button change = new Button("CHANGE NAME");
        change.setOnAction(event -> {
            ArrayList<Tag> select = listOldNames.getSelectionModel().getSelectedItem();
            if (control.changeToOldName(select, inputImage)) {
                window.close();
            }
        });

        VBox tagNameColumn = new VBox();
        tagNameColumn.setPadding(new Insets(20, 20, 20, 20));
        tagNameColumn.getChildren().addAll(tags, tagSelect,tagRemark, addDeleteButtons, oldNames, listOldNames, change);

        // Set the window layout.
        BorderPane allItems = new BorderPane();
        allItems.setLeft(imageColumn);
        allItems.setRight(tagNameColumn);

        Scene oneImageWindow = new Scene(allItems, 900, 750);
        window.setScene(oneImageWindow);
        window.show();
    }
}
