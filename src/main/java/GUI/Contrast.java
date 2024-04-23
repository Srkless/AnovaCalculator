package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.commons.math3.distribution.TDistribution;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.Node;
import static java.lang.Math.sqrt;

public class Contrast implements Initializable {

    @FXML
    private Button bCalculateContrast;

    @FXML
    private Text calculatorText;

    @FXML
    private ComboBox<String> cbSystem1;

    @FXML
    private ComboBox<String> cbSystem2;

    @FXML
    private Button bClose;

    @FXML
    void calculate(ActionEvent event) {
        HashMap<Integer, Double> effects = Table.effects;
        String system1 = cbSystem1.getValue();
        String system2 = cbSystem2.getValue();
        int i = Table.names.indexOf(system1);
        int j = Table.names.indexOf(system2);

        double studentDistribution = getStudentDistribution(Table.matrixValues);
        double sc = getSc(Table.matrixValues);
        System.out.println(sc);

        double c = effects.get(i + 1) - effects.get(j + 1);
        double c1 = c - studentDistribution * sc;
        double c2 = c + studentDistribution * sc;

        calculatorText.setText("(" + Table.formatedDecial(c1) + "," + Table.formatedDecial(c2) + ")");

    }

    @FXML
    void close(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbSystem1.getItems().addAll(Table.names);
        cbSystem2.getItems().addAll(Table.names);
    }

    public static double getStudentDistribution(ArrayList<ArrayList<Double>> matrix) {
        double k = matrix.size();
        double n = matrix.get(0).size();
        double alpha = 0.95;
        return new TDistribution(k * (n - 1)).inverseCumulativeProbability(alpha);
    }

    public static double getSc(ArrayList<ArrayList<Double>> matrix) {
        double SSE = 0;
        for (int i = 1; i <= homepageController.nAlternatives; i++) {
            for (int j = 0; j < Table.colValues.get(i).size(); j++) {
                SSE += Math.pow(Table.colValues.get(i).get(j) - Table.averages.get(i), 2);
            }

        }

        double Se2 = SSE / (homepageController.nAlternatives * (homepageController.nMeasurements - 2));

        return sqrt(2 * Se2 / (homepageController.nAlternatives * (homepageController.nMeasurements - 1)));
    }
}
