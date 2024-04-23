package GUI;

import Main.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.File;

public class homepageController {

    public static int nAlternatives;
    public static int nMeasurements;

    @FXML
    private TextField alternativesField;

    @FXML
    private Button bCreateMatrix;

    @FXML
    private Text calculatorText;

    @FXML
    private TextField measurementsField;

    @FXML
    private Button bClose;

    @FXML
    void create(ActionEvent event) throws Exception {

        nAlternatives = Integer.parseInt(alternativesField.getText());
        nMeasurements = Integer.parseInt(measurementsField.getText()) + 1;

        FXMLLoader loader = new FXMLLoader(new File(App.table).toURI().toURL());
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene registerScene = new Scene(root);
        stage.setScene(registerScene);
        stage.show();

    }

    @FXML
    void close(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
