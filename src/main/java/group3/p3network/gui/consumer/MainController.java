package group3.p3network.gui.consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class MainController {
    @FXML
    private GridPane videoGrid;

    public void initialize() throws IOException {

    }

    @FXML
    public void closeFromMenu() {
        System.out.println("Im closing");
    }
}
