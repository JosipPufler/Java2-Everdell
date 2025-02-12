module hr.algebra.everdell {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jshell;
    requires static lombok;
    requires java.naming;
    requires java.rmi;
    requires jdk.unsupported.desktop;
    requires java.sql;
    requires java.xml.bind;


    opens hr.algebra.everdell to javafx.fxml;
    opens hr.algebra.everdell.models to java.xml.bind;

    exports hr.algebra.everdell.utils;
    exports hr.algebra.everdell;
    exports hr.algebra.everdell.models;
    exports hr.algebra.everdell.interfaces;
    exports hr.algebra.everdell.controllers;
    opens hr.algebra.everdell.controllers to javafx.fxml;
    exports hr.algebra.everdell.rmi to java.rmi;
}