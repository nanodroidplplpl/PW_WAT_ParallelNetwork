module siec.javafxprojektpw9_2022_projekt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;

    opens siec.javafxprojektpw9_2022_projekt to javafx.fxml;
    exports siec.javafxprojektpw9_2022_projekt;
}