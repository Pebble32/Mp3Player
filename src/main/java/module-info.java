module vidmot.mp3_player {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens vidmot to javafx.fxml;
    exports vidmot;
}