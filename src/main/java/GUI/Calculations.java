package GUI;

import Main.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import java.util.List;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.File;

public class Calculations implements Initializable {

    @FXML
    private Label AdfLabel;

    @FXML
    private Label CFLabel;

    @FXML
    private Label FLabel;

    @FXML
    private Label SSALabel;

    @FXML
    private Label SSELabel;

    @FXML
    private Label SSTLabel;

    @FXML
    private Label Sa2Label;

    @FXML
    private Label Se2Label;

    @FXML
    private GridPane calculatedMatrix;

    @FXML
    private Label edfLabel;

    @FXML
    private Label tdfLabel;

    @FXML
    private Button bClose;

    @FXML
    void close(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    public void calculateValues() {
        double SSE = 0;
        double SSA = 0;
        double SST = 0;
        double Sa2 = 0;
        double Se2 = 0;
        double F = 0;
        double CF = 0;
        int adf = 0;
        int edf = 0;
        System.out.println(homepageController.nAlternatives);
        System.out.println(homepageController.nMeasurements);
        for (int i = 1; i <= homepageController.nAlternatives; i++) {
            for (int j = 0; j < Table.colValues.get(i).size(); j++) {
                SSE += Math.pow(Table.colValues.get(i).get(j) - Table.averages.get(i), 2);
                SSA += Math.pow(Table.averages.get(i) - Table.totalAverage, 2);
            }

        }

        SST = SSE + SSA;
        Sa2 = SSA / (homepageController.nAlternatives - 1);
        Se2 = SSE / (homepageController.nAlternatives * (homepageController.nMeasurements - 2));
        CF = Sa2 / Se2;
        adf = homepageController.nAlternatives - 1;
        edf = homepageController.nAlternatives * (homepageController.nMeasurements - 2);
        double confidenceLevel = 0.95;
        F = new FDistribution(adf, edf).inverseCumulativeProbability(confidenceLevel);

        AdfLabel.setText(String.valueOf(adf));
        edfLabel.setText(String.valueOf(edf));
        tdfLabel.setText(String.valueOf(homepageController.nAlternatives * (homepageController.nMeasurements - 1) - 1));
        Sa2Label.setText(Table.formatedDecial(Sa2));
        Se2Label.setText(Table.formatedDecial(Se2));
        FLabel.setText(Table.formatedDecial(F));
        CFLabel.setText(Table.formatedDecial(CF));
        SSALabel.setText(Table.formatedDecial(SSA));
        SSELabel.setText(Table.formatedDecial(SSE));
        SSTLabel.setText(Table.formatedDecial(SST));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calculateValues();
    }
}
