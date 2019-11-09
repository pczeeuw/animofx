package nl.pczeeuw.animofx8.controllers;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nl.pczeeuw.animofx8.services.AwesomeActionService;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class MainController {

    @FXML
    private Label helloLabel;

    @FXML
    private TextField nameField;

    // Be aware: This is a Spring bean. So we can do the following:
    @Autowired
    private AwesomeActionService actionService;

    @FXML
    private void setHelloText(final Event event) {
        final String textToBeShown = actionService.processName(nameField.getText());
        helloLabel.setText(textToBeShown);
    }
}