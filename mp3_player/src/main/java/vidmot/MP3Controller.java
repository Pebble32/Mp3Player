package vidmot;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MP3Controller {
    @FXML
    public ProgressBar fxSongProgress;
    @FXML
    public Slider fxVolume;
    @FXML
    public Button fxPlay, fxPause, fxNext, fxRestart, fxPrevious;
    @FXML
    public ComboBox<String> fxSpeed;
    @FXML
    public Label fxLabel;
    @FXML
    public AnchorPane fxPane;
    @FXML
    private Pane pane;
    @FXML
    private File directory;
    @FXML
    private File[] files;
    @FXML
    private ArrayList<File> songs;
    private Media media;
    private MediaPlayer mediaPlayer;

    private int songN;
    private final int[] speeds = {25,50,100,125,150,175,200};
    private Timer timer;
    private TimerTask task;
    private boolean running;


    public void initialize(){
        songs = new ArrayList<File>();
        directory = new File("music");
        files = directory.listFiles();

        if (files != null){
            for (File file : files){
                songs.add(file);
                System.out.println(file);
            }
        }

        media = new Media(songs.get(songN).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        fxLabel.setText(songs.get(songN).getName());

        for (int speed : speeds) {
            fxSpeed.getItems().add((speed)+"%");
        }

        fxSpeed.setOnAction(this::speedChange);

        fxVolume.valueProperty().addListener((observable, oldValue, newValue) ->
                mediaPlayer.setVolume(fxVolume.getValue() * 0.01));

        fxSongProgress.setStyle("-fx-accent: #00FF00;");
    }

    @FXML
    private void speedChange(ActionEvent actionEvent) {
        if (fxSpeed.getValue() == null){
            mediaPlayer.setRate(1);
        } else {
            mediaPlayer.setRate(Integer.parseInt(fxSpeed.getValue().substring(0,fxSpeed.getValue().length()-1))*0.01);
        }
    }

    @FXML
    public void playMedia() {

        beginTimer();
        speedChange(null);
        mediaPlayer.setVolume(fxVolume.getValue() * 0.01);
        mediaPlayer.play();
    }
    @FXML
    public void pauseMedia() {

        stopTimer();
        mediaPlayer.pause();
    }
    @FXML
    public void restartMedia() {

        fxSongProgress.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0.0));
    }
    @FXML
    public void nextMedia() {
        if (songN < songs.size() -1) {
            songN++;

            mediaPlayer.stop();

            if (running){
                stopTimer();
            }

            media = new Media(songs.get(songN).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            fxLabel.setText(songs.get(songN).getName());

            playMedia();
        } else {
            songN = 0;

            mediaPlayer.stop();

            if (running){
                stopTimer();
            }

            media = new Media(songs.get(songN).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            fxLabel.setText(songs.get(songN).getName());

            playMedia();
        }
    }
    @FXML
    public void previousMedia() {
        if (songN > 0) {
            songN--;

            mediaPlayer.stop();

            if (running){
                stopTimer();
            }

            media = new Media(songs.get(songN).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            fxLabel.setText(songs.get(songN).getName());

            playMedia();
        } else {
            songN = songs.size() - 1;

            mediaPlayer.stop();

            if (running){
                stopTimer();
            }

            media = new Media(songs.get(songN).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            fxLabel.setText(songs.get(songN).getName());

            playMedia();
        }
    }

    @FXML
    public void beginTimer(){
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                System.out.println(current/end); // checking % of songs that is finnished
                fxSongProgress.setProgress(current/end);

                if (current/end == 1){
                    stopTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
    @FXML
    public void stopTimer(){
        running = false;
        timer.cancel();
    }
}