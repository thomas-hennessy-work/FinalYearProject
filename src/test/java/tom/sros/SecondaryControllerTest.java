package tom.sros;

import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryControllerTest {

    @FXML
    private void switchToPrimary() throws IOException {
        AppTest.setRoot("logIn");
    }
}