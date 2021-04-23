module org.covid {
    requires javafx.controls;
    requires javafx.fxml;
    requires annotations;
    requires json.path;
    requires okhttp3;
    requires java.sql;
    requires mysql.connector.java;
    requires commons.validator;

    opens org.covid to javafx.fxml;
    exports org.covid;
    opens org.covid.controller to javafx.fxml;
    exports org.covid.controller;
    opens org.covid.db to javafx.fxml;
    exports org.covid.db;
    opens org.covid.model to javafx.fxml;
    exports org.covid.model;
}
