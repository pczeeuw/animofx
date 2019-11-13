package nl.pczeeuw.animofx8.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import nl.pczeeuw.animofx8.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
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


    public void dropFileDetected(MouseEvent mouseEvent) {
        log.debug("detected");
    }

    public void dropFileDone(DragEvent dragEvent) {
        log.debug("done");
    }

    public void dropFileDropped(DragEvent dragEvent) {
        log.debug("dropped");
    }

    public void dropFileEnter(DragEvent dragEvent) {
        log.debug("enter");
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
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            EditorController controller = loader.getController();

            Image image = imageService.fileToImage(file);
            if (image != null) {
                controller.initPage(scene, image, file.getAbsolutePath());
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