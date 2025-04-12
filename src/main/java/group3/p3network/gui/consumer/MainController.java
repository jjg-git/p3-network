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
        int maxRow = videoGrid.getRowCount();
        int maxCol = videoGrid.getColumnCount();

        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {

                VBox loadingCell = FXMLLoader.load(
                    getClass().getResource("loadingCell.fxml")
                );

                videoGrid.add(loadingCell, i, j);
            }
        }
    }

    @FXML
    public void closeFromMenu() {
        System.out.println("Im closing");
    }
}
