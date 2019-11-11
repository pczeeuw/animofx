package nl.pczeeuw.animofx8.controllers;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import nl.pczeeuw.animofx8.Animofx8Application;
import nl.pczeeuw.animofx8.services.AwesomeActionService;
import nl.pczeeuw.animofx8.services.ImageService;
import nl.pczeeuw.animofx8.views.EditorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import sun.applet.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@FXMLController
@Slf4j
public class MainController {


    @FXML
    Pane dropPane;

    @FXML
    Canvas canvas;

    @FXML
    ImageView imgView;

    @Autowired
    private ImageService imageService;

    public MainController() {
    }


    public void openImage(ActionEvent actionEvent) {
        log.info("In openImage: " + actionEvent.getEventType().getName());
        Image image = getImage();

        if (image != null) {
//            tempLabel.setText("Image: " + image.getRequestedHeight() + " " + image.getRequestedWidth());
            imgView.setImage(image);

            canvas.setLayoutX(imgView.getLayoutX());
            canvas.setLayoutY(imgView.getLayoutY());

            imgView.setFitWidth(image.getWidth());
            imgView.setFitHeight(image.getHeight());

            canvas.setWidth(image.getWidth());
            canvas.setHeight(image.getHeight());


            log.info("Canvas width, height: " + canvas.getWidth() + ":" + canvas.getHeight());


//            imgView.getGraphicsContext2D().drawImage(image, image.getWidth(), image.getHeight());
        } else {

            log.error("Could not load image");
        }

    }

    private Image getImage() {

        Resource imgResource = new ClassPathResource("/img/dune-cat.jpg");
        try {
            return new Image(imgResource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void drawToCanvas(MouseEvent mouseEvent) {
        log.info("Mouse clicked on canvas! Coordinate screen: " + mouseEvent.getScreenX() + ":" + mouseEvent.getScreenY());
        log.info("Mouse clicked on canvas! Coordinate scene: " + mouseEvent.getSceneX() + ":" + mouseEvent.getSceneY());
        log.info("Mouse clicked on canvas! Coordinate: " + mouseEvent.getX() + ":" + mouseEvent.getY());

        int canvasX = (int) mouseEvent.getSceneX() - (int) canvas.getLayoutX();
        int canvasY = (int) mouseEvent.getSceneY() - (int) canvas.getLayoutY();
//        GraphicsContext g = canvas.getGraphicsContext2D();
//        g.setFill(Color.BLACK);
//        g.fillRect(mouseEvent.getX(),mouseEvent.getY(), 10,10);

//        BufferedImage awtImg = SwingFXUtils.fromFXImage(imgView.getImage(),null);
//        awtImg.getWritableTile()
        Image img = imgView.getImage();
        WritableImage wImg = new WritableImage(img.getPixelReader(), (int) img.getWidth(), (int) img.getHeight());
        PixelWriter writer = wImg.getPixelWriter();

        for (int i = canvasX; i < canvasX + 10; i++) {
            for (int j = canvasY; j < canvasY + 10; j++) {
                writer.setColor(i, j, Color.BLACK);
            }
        }
        imgView.setImage(wImg);

    }

//    public void drawDragDetected(MouseEvent mouseEvent) {
//
//    }
//
//    public void dropFileEnter(MouseDragEvent mouseDragEvent) {
//        dropPane.setCursor(Cursor.CLOSED_HAND);
//        dropPane.setStyle("-fx-background-color: #a2ddf5");
//    }
//
//    public void dropFileExit(MouseDragEvent mouseDragEvent) {
//        dropPane.setCursor(Cursor.CLOSED_HAND);
//        dropPane.setStyle("-fx-background-color: #a2ddf5");
//    }
//
//    public void dropFileOver(MouseDragEvent mouseDragEvent) {
//        dropPane.setCursor(Cursor.CLOSED_HAND);
//        dropPane.setStyle("-fx-background-color: #a2ddf5");
//    }
//
//    public void dropFileRelease(MouseDragEvent mouseDragEvent) {
//        dropPane.setCursor(Cursor.CLOSED_HAND);
//        dropPane.setStyle("-fx-background-color: #a2ddf5");
//    }

    public void dropFileDetected(MouseEvent mouseEvent) {
        log.info("detected");
    }

    public void dropFileDone(DragEvent dragEvent) {
        log.info("done");
    }

    public void dropFileDropped(DragEvent dragEvent) {
        log.info("dropped");
    }

    public void dropFileEnter(DragEvent dragEvent) {
        log.info("enter");
    }

    public void dropFileExit(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            log.info("file detected!");
            List<File> files = imageService.checkFiles(dragEvent.getDragboard().getFiles());
            files.forEach(p -> {
                log.info(p.getAbsolutePath());
                showEditorView(p);
            });
        }
        dropPane.setStyle(null);
    }

    public void dropFileOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dropPane.setCursor(Cursor.OPEN_HAND);
            dropPane.setStyle("-fx-background-color: #a2ddf5");
        } else {
            dropPane.setStyle("-fx-background-color: RED");
        }
    }

    private void showEditorView(File file) {
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(new ClassPathResource("fxml/editor.fxml").getURL());

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );

            EditorController controller = loader.getController();

            Image image = imageService.fileToImage(file);
            if (image != null) {
                controller.initPage(image, file.getAbsolutePath());
                stage.setTitle(file.getName());
                stage.show();
            } else {
                log.error("File kon niet geladen worden");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}