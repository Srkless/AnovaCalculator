package Main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.File;

import GUI.homepageController;

public class App extends Application {
    public static String path = "src" + File.separator + "main" + File.separator + "java" + File.separator + "GUI"
            + File.separator;
    public static String homepagePath = path + "homepage.fxml";
    public static String table = path + "table.fxml";
    public static String calculations = path + "calculation.fxml";
    public static String contrast = path + "contrast.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Anova Calculator");
        FXMLLoader loader = new FXMLLoader(new File(homepagePath).toURI().toURL());
        Parent root = loader.load();
        Scene loginScene = new Scene(root);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
