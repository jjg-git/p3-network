package group3.p3network.gui.consumer;

import group3.p3network.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.io.IOException;

public class MainController {
    @FXML
    public ListView videoListView;
    @FXML
    public MediaView videoMediaView;


    public void initialize() throws IOException {
        String[] fileList =  new File(Consumer.getSetting().output()).list();
        String directory = Consumer.getSetting().output();
        if (fileList != null) {
            videoListView.getItems().addAll(FXCollections.observableArrayList(fileList));
            videoListView.getSelectionModel().selectedItemProperty()
                .addListener(item -> {
                    int index =
                        videoListView.getSelectionModel().getSelectedIndex();

                    Media videoMedia = new Media(
                        new File(directory + fileList[index]).toURI().toString()
                    );

                    MediaPlayer player = new MediaPlayer(videoMedia);
                    player.setAutoPlay(true);

                    videoMediaView.setMediaPlayer(player);
                });
        }
    }

    @FXML
    public void closeFromMenu() {
        System.out.println("Im closing");
    }

}
