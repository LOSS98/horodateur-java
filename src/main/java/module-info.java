module fr.insa.horodateurjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires jakarta.persistence;
    requires java.naming;
    requires org.slf4j;
    requires com.fasterxml.jackson.core;
    requires jBCrypt;
    requires itextpdf;

    opens fr.insa.horodateurjava.models to javafx.base;
    opens fr.insa.horodateurjava to javafx.fxml;

    exports fr.insa.horodateurjava;
    exports fr.insa.horodateurjava.adminControllers;
    opens fr.insa.horodateurjava.adminControllers to javafx.fxml;
}