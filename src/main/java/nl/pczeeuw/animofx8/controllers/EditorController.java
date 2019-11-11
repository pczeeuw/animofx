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
        log.info("Drag detected");
    }

    public void dragDone(DragEvent dragEvent) {
        log.info("Drag done");
    }

    public void dragDropped(DragEvent dragEvent) {
        log.info("Drag dropped");
    }

    public void dragEntered(MouseEvent dragEvent) {
        this.currentEvent = new RectangleDrawEvent((int) dragEvent.getSceneX() + 25, (int) dragEvent.getScreenY());
        log.info("Drag entered");
    }

    public void dragExited(MouseEvent dragEvent) {
        this.currentEvent.setXExit((int) dragEvent.getSceneX() + 25);
        this.currentEvent.setYExit((int) dragEvent.getScreenY());
        drawEvents.add(this.currentEvent);

        drawDragEventToCanvas(this.currentEvent);

        log.info("Drag exited");
    }

    public void action(ActionEvent actionEvent) {
        log.info("TEst");
    }

    private void drawDragEventToCanvas(RectangleDrawEvent drawEvent) {
        log.info("Draw to canvas");
        GraphicsContext g = editCanvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        g.fillRect(drawEvent.getXStart(), drawEvent.getYStart(),drawEvent.getXExit(),drawEvent.getYExit());
        g.fill();
        editCanvas.toFront();

    }


}
