module Main {
    requires javafx.controls;
    requires javafx.fxml;

    requires commons.math3;

    opens Main;
    opens GUI;

    exports Main;
}
