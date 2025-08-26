package alma.apwa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/alma/apwa/config.fxml"));
        Parent root = loader.load();

        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Watermark Generator by ALMA");

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int hres = size.width;
        int vres = size.height;
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

    }
}