module com.example.farminggame {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.example.farminggame.models.environment.crops;
    exports com.example.farminggame.models.environment;
    exports com.example.farminggame.models.farmer;
    exports com.example.farminggame.models.tools;
    exports com.example.farminggame.controllers;

    opens com.example.farminggame.models.environment.crops to javafx.fxml;
    opens com.example.farminggame.models.environment to javafx.fxml;
    opens com.example.farminggame.models.farmer to javafx.fxml;
    opens com.example.farminggame.models.tools to javafx.fxml;
    opens com.example.farminggame.controllers to javafx.fxml;
}