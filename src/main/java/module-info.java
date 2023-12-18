module SE2StartupProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;

    opens mainpackage;
    opens Applikation;

    exports Applikation;
    exports Threads;
    opens Threads;
}
