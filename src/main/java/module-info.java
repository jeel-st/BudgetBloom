module SE2StartupProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;

    opens mainpackage;
    opens Controller;

    exports Controller;
    exports Threads;
    opens Threads;
    exports Logic;
    opens Logic;
    exports Interfaces;
    opens Interfaces;
}
