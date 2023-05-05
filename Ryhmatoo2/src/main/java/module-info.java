module com.example.ryhmatoo2 {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.ryhmatoo2 to javafx.fxml;
    exports com.example.ryhmatoo2;
}