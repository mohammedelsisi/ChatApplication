module ServerSide {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.materialdesign2;
    requires java.sql; /*  JDBC*/
    requires mysql.connector.java;   /* not needed after JDBC 3*/
    requires java.naming;
    requires mail;
    opens JETS.ui.controllers;
    exports JETS;
    requires Common;
    requires java.rmi;
}