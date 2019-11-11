package nl.pczeeuw.animofx8.controllers;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import nl.pczeeuw.animofx8.domain.RectangleDrawEvent;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@FXMLController
public class EditorController {

    @FXML
    Canvas editCanvas;

    @FXML
    ImageView imgView;

    @FXML
    Pane editorPane;

    @FXML
    MenuBar menuBar;

    @FXML
    MenuItem menuItemOpen;

    Image origImg;

    String origUrl;

    RectangleDrawEvent currentEvent;
    List<RectangleDrawEvent> drawEvents;


//    public EditorController (File file) {
//        log.info("In editor constructor");
//        origImgFile = file;
//    }

    public void initPage(Image image, String url) {
        log.info("Set file in controller");
        origImg = image;
        imgView.setImage(origImg);
        editorPane.setPrefHeight(origImg.getHeight() + 25);
        editorPane.setPrefWidth(origImg.getWidth());
        editCanvas.setHeight(origImg.getHeight() + 25);
        editCanvas.setWidth(origImg.getWidth());
        menuBar.setPrefWidth(image.getWidth());
        origUrl = url;

        drawEvents = new ArrayList<>();
//            imgView.set
    }


    public void dragDetected(MouseEvent mouseEvent) {



//        log.info("Drag detected: " + mouseEvent.getSceneX() + ":" + mouseEvent.getSceneY());
    }

    public void dragEntered(MouseEvent mouseEvent) {
        log.info("Drag entered: " + mouseEvent.getSceneX() + ":" + mouseEvent.getSceneY());
        log.info("Mouse down? " + mouseEvent.isPrimaryButtonDown() + " " + mouseEvent.isSecondaryButtonDown());
//        log.info(mouseEvent.toString());

    }

    public void dragExited(MouseEvent mouseEvent) {
        log.info("Drag exited: " + mouseEvent.getSceneX() + ":" + mouseEvent.getSceneY());
//        log.info(mouseEvent.toString());

    }

    public void action(ActionEvent actionEvent) {
        log.info("TEst");
    }

    private void drawDragEventToCanvas(RectangleDrawEvent drawEvent) {
        log.info("Draw to canvas");
        GraphicsContext g = editCanvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        g.fillRect(drawEvent.getStartingX(), drawEvent.getStartingY(), drawEvent.getWidthX(), drawEvent.getHeightY());
        log.info("Drawed to: " + drawEvent.getStartingX() + ":" + drawEvent.getStartingY() + " WH: " + drawEvent.getWidthX() + ":" + drawEvent.getHeightY());
        editCanvas.toFront();

        drawEvents.add(this.currentEvent);
        this.currentEvent = null;


    }


    public void mouseClicked(MouseEvent mouseEvent) {
            this.currentEvent = new RectangleDrawEvent((int) mouseEvent.getSceneX() + 25, (int) mouseEvent.getSceneY());
            log.info("Setting draw event");


    }

    public void mouseReleased(MouseEvent mouseEvent) {
        if (this.currentEvent != null && this.currentEvent.isActive()) {
            this.currentEvent.setXExit((int) mouseEvent.getSceneX() + 25);
            this.currentEvent.setYExit((int) mouseEvent.getSceneY());
            drawDragEventToCanvas(this.currentEvent);
        }
    }
}
