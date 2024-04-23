package GUI;

import Main.*;
import Main.Calculator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.ColumnConstraints;
import java.util.ArrayList;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Label;
import java.io.File;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.List;
import javafx.scene.Node;
import java.util.HashMap;
import java.text.DecimalFormat;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Table implements Initializable {

    @FXML
    private TextField alternativesField;

    @FXML
    private Button bCalculateAnova;

    @FXML
    private Button bCreateMatrix;

    @FXML
    public GridPane matrix;

    @FXML
    private Button bCreateContrast;

    @FXML
    private TextField measurementsField;

    @FXML
    private Button bClose;

    static int columnIndex = 1;

    public static double totalAverage = 0;
    private static Label totalAverageLabel = new Label(totalAverage + "");
    public static HashMap<Integer, Double> averages = new HashMap<>();
    public static HashMap<Integer, Double> effects = new HashMap<>();
    public static HashMap<Integer, List<Double>> colValues = new HashMap<>();
    public static ArrayList<ArrayList<Double>> matrixValues = new ArrayList<>();
    public static ArrayList<String> names = new ArrayList<>();

    @FXML
    void close(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void add(ActionEvent event) {
        String name = alternativesField.getText();
        names.add(name);
        int nAlternatives = homepageController.nAlternatives - 1;
        int nMeasurements = homepageController.nMeasurements + 1;

        double columnWidth = 450.0 / (nAlternatives + 1);
        double rowHeight = 350.0 / (nMeasurements + 1);

        List<Double> values = Arrays.asList(measurementsField.getText().split(",")).stream()
                .map(Double::parseDouble).collect(Collectors.toList());
        if (columnIndex > homepageController.nAlternatives || values.size() != homepageController.nMeasurements - 1) {
            return;
        }
        colValues.put(columnIndex, values);
        matrixValues.add(new ArrayList<>(values));
        System.out.println(colValues);
        ArrayList<String> column = new ArrayList<>();
        column.add(name);

        for (int i = 0; i < nMeasurements - 2; i++) {
            column.add(formatedDecial(values.get(i)));
        }
        averages.put(columnIndex, Calculator.average(values));
        column.add(formatedDecial(Calculator.average(values)));
        column.add("NaN");

        totalAverage = Calculator.average(new ArrayList<>(averages.values()));
        totalAverageLabel.setText(formatedDecial(totalAverage));

        for (int i = 0; i < column.size(); i++) {
            Label textField = new Label(column.get(i));
            textField.setStyle(
                    "-fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold;");
            textField.setPrefWidth(columnWidth);
            textField.setMinWidth(columnWidth);
            textField.setMaxWidth(columnWidth);

            textField.setPrefHeight(rowHeight);
            textField.setMinHeight(rowHeight);
            textField.setMaxHeight(rowHeight);

            textField.setAlignment(Pos.CENTER);
            textField.wrapTextProperty();
            matrix.add(textField, columnIndex, i);
        }
        alternativesField.clear();
        measurementsField.clear();

        columnIndex++;
        System.out.println(columnIndex + " " + homepageController.nAlternatives);
        if (columnIndex > homepageController.nAlternatives) {
            calculateEfect();
            alternativesField.setDisable(true);
            measurementsField.setDisable(true);
            bCreateContrast.setVisible(true);
            bCalculateAnova.setVisible(true);
            bCreateMatrix.setVisible(false);
        }
    }

    @FXML
    void calculationsTable(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File(App.calculations).toURI().toURL());
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Izracunavanja");
        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    void calculateContrast(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File(App.contrast).toURI().toURL());
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Kontrast");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void calculateEfect() {
        for (Node node : matrix.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                if ("NaN".equals(label.getText())) {
                    int columnIndex = GridPane.getColumnIndex(label);
                    double value = averages.get(columnIndex);
                    System.out.println(value);

                    double effect = value - totalAverage;
                    effects.put(columnIndex, effect);
                    label.setText(formatedDecial(effect));
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        int nAlternatives = homepageController.nAlternatives + 1;
        int nMeasurements = homepageController.nMeasurements + 1;

        double columnWidth = 450.0 / (nAlternatives + 1);
        double rowHeight = 350.0 / (nMeasurements + 1);

        bCreateContrast.setVisible(false);
        bCalculateAnova.setVisible(false);
        System.out.println(nMeasurements + 1);
        ArrayList<ColumnConstraints> columns = new ArrayList<>();
        for (int i = 0; i < nAlternatives; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setMinWidth(columnWidth);
            column.setPrefWidth(columnWidth);
            column.setHalignment(HPos.CENTER);
            column.setMaxWidth(columnWidth);
            columns.add(column);
        }

        ArrayList<RowConstraints> rows = new ArrayList<>();
        for (int i = 0; i < nMeasurements; i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(rowHeight);
            row.setPrefHeight(rowHeight);
            row.setMaxHeight(rowHeight);
            rows.add(row);
        }

        matrix.getColumnConstraints().addAll(columns);
        matrix.getRowConstraints().addAll(rows);

        ArrayList<String> firstColumn = new ArrayList<>();
        firstColumn.add(" ");
        for (int i = 0; i < nMeasurements - 2; i++) {
            firstColumn.add(i + 1 + "");
        }

        firstColumn.add("Sr. vr. kolona");
        firstColumn.add("Efekat");

        // double fieldWidth = 450.0;
        System.out.println(nAlternatives);
        for (int i = 0; i < firstColumn.size(); i++) {
            Label textField = createLabel(firstColumn.get(i), columnWidth, rowHeight);
            matrix.add(textField, 0, i);
        }

        ArrayList<String> lastColumn = new ArrayList<>();
        lastColumn.add("Ukupna sr.vr.");
        for (int i = 0; i < nMeasurements - 2; i++) {
            lastColumn.add(" ");
        }

        totalAverageLabel.setStyle(
                "-fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold;");
        totalAverageLabel.setPrefWidth(columnWidth);
        totalAverageLabel.setMinWidth(columnWidth);
        totalAverageLabel.setMaxWidth(columnWidth);

        totalAverageLabel.setPrefHeight(rowHeight);
        totalAverageLabel.setMinHeight(rowHeight);
        totalAverageLabel.setMaxHeight(rowHeight);

        totalAverageLabel.setAlignment(Pos.CENTER);
        totalAverageLabel.wrapTextProperty();

        // double fieldWidth = 450.0;
        // System.out.println(nAlternatives);
        for (int i = 0; i < lastColumn.size() - 1; i++) {
            Label textField = createLabel(lastColumn.get(i), columnWidth, rowHeight);
            matrix.add(textField, nAlternatives, i);
        }

        matrix.add(totalAverageLabel, nAlternatives, lastColumn.size());

    }

    private Label createLabel(String text, double columnWidth, double rowHeight) {
        Label textField = new Label(text);
        textField.setStyle(
                "-fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold;");
        textField.setPrefWidth(columnWidth);
        textField.setMinWidth(columnWidth);
        textField.setMaxWidth(columnWidth);

        textField.setPrefHeight(rowHeight);
        textField.setMinHeight(rowHeight);
        textField.setMaxHeight(rowHeight);

        textField.setAlignment(Pos.CENTER);
        textField.wrapTextProperty();
        return textField;

    }

    public static String formatedDecial(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        return decimalFormat.format(value);
    }
}
