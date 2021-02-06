module ClientSide {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
requires libphonenumber;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
requires commons.validator;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.materialdesign2;
    requires java.sql; /*  JDBC*/
//    requires ojdbc10; /*  JDBC*/
    requires mysql.connector.java;   /* not needed after JDBC 3*/
    requires java.naming;
    requires mail;
    opens mypkg.ui.controllers;
//    opens mypkg to javafx.fxml;
    exports mypkg;
}