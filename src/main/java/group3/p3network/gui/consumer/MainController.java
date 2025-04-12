package group3.p3network.gui.consumer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.File;

public class MainController {
    @FXML
    private GridPane videoGrid;

    public void initialize() {
        Label testLabel = new Label("(0, 0)");
        Label testLabel2 = new Label("(1, 2)");

        videoGrid.add(testLabel, 0, 0);
        videoGrid.add(testLabel2, 1, 2);
    }

    @FXML
    public void closeFromMenu() {
        System.out.println("Im closing");
    }
}
