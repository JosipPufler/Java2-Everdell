module hr.algebra.everdell {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.jshell;
    requires static lombok;
    requires java.naming;
    requires java.rmi;


    opens hr.algebra.everdell to javafx.fxml;
    exports hr.algebra.everdell;
    exports hr.algebra.everdell.controllers;
    opens hr.algebra.everdell.controllers to javafx.fxml;
}