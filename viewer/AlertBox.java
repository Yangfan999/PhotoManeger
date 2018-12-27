package viewer;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

/**
 * Is used to show any waring or messages and shown by a window
 * @author QiZeng, yangfanli, yuyuexin
 * @version IntelliJ IDEA 2017.2.4
 */
public class AlertBox {

    /**
     * The design and display of the program
     * @param title: a brief description, usually a phase, of the program
     * @param message: a message shown on the window
     */
    public void display(String title, String message){

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);

        Label appearMessage = new Label(message);

        Button closeButton = new Button("CLOSE");
        closeButton.setOnAction(event -> window.close());

        VBox allItems = new VBox(10);
        allItems.getChildren().addAll(appearMessage, closeButton);
        allItems.setAlignment(Pos.CENTER);

        Scene alertMessage = new Scene(allItems);
        window.setScene(alertMessage);
        window.showAndWait();

    }
}

// Adapted from: https://www.youtube.com/watch?v=SpL3EToqaXA