module com.app.javaexamen {
    // JavaFX dependencies
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    
    // Database dependencies
    requires java.sql;
    requires mysql.connector.j;
    
    // Utility dependencies
    requires java.net.http;
    requires java.desktop;

    // Exports for JavaFX
    exports com.app.javaexamen;
    exports com.app.javaexamen.controllers;
    exports com.app.javaexamen.entities;
    exports com.app.javaexamen.services;
    exports com.app.javaexamen.dao;
    exports com.app.javaexamen.util;
    
    // Opens for JavaFX FXML
    opens com.app.javaexamen to javafx.fxml;
    opens com.app.javaexamen.controllers to javafx.fxml;
    opens com.app.javaexamen.entities to javafx.base, javafx.fxml;
}