import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("view.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        String path = this.getClass().getResource("style.css").toExternalForm();
//        root.getStylesheets().add(path);
        Scene scene = new Scene(root);
        stage.setTitle("Lab 6 - Part A - Distributed Computing - Game of Live");
//        stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
        stage.setScene(scene);
        stage.show();
    }
}