package nl.pczeeuw.animofx8.controllers;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import nl.pczeeuw.animofx8.domain.RectangleDrawEvent;
import nl.pczeeuw.animofx8.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@FXMLController
public class EditorController {
    Scene root;

    @FXML
    Canvas editCanvas;

    @FXML
    ImageView imgView;

    @FXML
    Pane editorPane;

    @FXML
    MenuBar menuBar;

    private Image origImg;

    private String origFilePath;

    private RectangleDrawEvent currentEvent;
    private List<RectangleDrawEvent> drawEvents;

    private List<Canvas> canvasList;

    @Autowired
    ImageService imageService;


//    public EditorController (File file) {
//        log.info("In editor constructor");
//        origImgFile = file;
//    }

    public void initPage(Scene root, Image image, String url) {
        log.info("Set file in controller");
        this.root = root;
        origImg = image;
        imgView.setImage(origImg);
        editorPane.setPrefHeight(origImg.getHeight() + 25);
        editorPane.setPrefWidth(origImg.getWidth());
        editCanvas.setHeight(origImg.getHeight() + 25);
        editCanvas.setWidth(origImg.getWidth());
        menuBar.setPrefWidth(image.getWidth());
        origFilePath = url;

        drawEvents = new ArrayList<>();
        canvasList = new ArrayList<>();

        // Zelf keypresed event toevoegen aan rootpane. Anders doet ie het neit :(
        root.setOnKeyPressed(event -> {
        if (event.isControlDown() && event.getCode() == KeyCode.Z) {
            log.info("Ctrl-z presed");
            removeLastDrawingEvent();
        }
        });

        if (imageService == null) {
            this.imageService = new ImageService();
        }
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

    private void drawDragEventToNewCanvas(RectangleDrawEvent drawEvent) {
        log.info("Drawing to canvas");
        Canvas canvas = createCanvas();
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        g.fillRect(drawEvent.getStartingX(), drawEvent.getStartingY(), drawEvent.getWidthX(), drawEvent.getHeightY());

        drawEvents.add(this.currentEvent);
        editCanvas.toFront();

        this.currentEvent = null;
    }

    private Canvas createCanvas() {
        Canvas canvas = new Canvas();
        canvas.setWidth(editCanvas.getWidth());
        canvas.setHeight(editCanvas.getHeight());
        canvas.setLayoutX(editCanvas.getLayoutX());
        canvas.setLayoutY(editCanvas.getLayoutY());
        editorPane.getChildren().add(canvas);
        canvas.toFront();

        canvasList.add(canvas);
        return canvas;
    }

    private void removeLastDrawingEvent() {
        if (canvasList.size() > 0) {
            Canvas canvas = canvasList.remove(canvasList.size() - 1);
            log.info("Canvas = " + canvas.toString());
            canvas.toBack();
            editorPane.getChildren().remove(canvas);
            drawEvents.remove(drawEvents.size() - 1);
            log.info("DrawEvent remvoed");
        }

    }


    public void mouseClicked(MouseEvent mouseEvent) {
        log.info("Mouse clicked");
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        if (this.currentEvent != null && this.currentEvent.isActive()) {
            this.currentEvent.setXExit((int) mouseEvent.getSceneX());
            this.currentEvent.setYExit((int) mouseEvent.getSceneY() - 25);
            log.info("Drawing: " + this.currentEvent.toString());

            drawDragEventToNewCanvas(this.currentEvent);
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {
        this.currentEvent = new RectangleDrawEvent((int) mouseEvent.getSceneX(), (int) mouseEvent.getSceneY() - 25);
    }

    public void saveImage(ActionEvent actionEvent) {
        Image imgToWrite = writeEventsToImage();
        log.info("URL is: " + this.origFilePath);
        imageService.saveToFile(imgToWrite,this.origFilePath);
    }

    public void saveImageAs(ActionEvent actionEvent) {
    }

    private Image writeEventsToImage () {
        WritableImage wImg = new WritableImage(origImg.getPixelReader(), (int) origImg.getWidth(), (int) origImg.getHeight());
        PixelWriter writer = wImg.getPixelWriter();

        for (RectangleDrawEvent event: drawEvents) {
            writeEventToImage(event, writer);
        }

        return wImg;
    }

    private void writeEventToImage(RectangleDrawEvent event, PixelWriter writer) {
        for (int i = event.getStartingX(); i < event.getStartingX() + event.getWidthX(); i++) {
            for (int j = event.getStartingY(); j < event.getStartingY()+ event.getHeightY(); j++) {
                writer.setColor(i, j, Color.BLACK);
            }
        }
    }

    public void quit(ActionEvent actionEvent) {
        Stage stage = (Stage) this.editorPane.getScene().getWindow();
        stage.close();
    }

    public void revert(ActionEvent actionEvent) {
    }
}
