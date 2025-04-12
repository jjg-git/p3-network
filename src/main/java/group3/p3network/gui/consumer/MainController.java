package group3.p3network.gui.consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainController {
    @FXML
    private ListView videoListView;
    @FXML
    private MediaView videoMediaView;

    private ArrayList<File> fileList = new ArrayList<>();

    public void initialize() throws IOException {

    }

    @FXML
    public void closeFromMenu() {
        System.out.println("Im closing");
    }
}
