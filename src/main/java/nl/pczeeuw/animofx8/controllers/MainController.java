package nl.pczeeuw.animofx8.controllers;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import nl.pczeeuw.animofx8.services.AwesomeActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@FXMLController
@Slf4j
public class MainController {



    @FXML
    Canvas canvas;

    @FXML
    Label tempLabel;

    @FXML
    ImageView imgView;

    @Autowired
    private AwesomeActionService actionService;


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



            log.info("Canvas width, height: " + canvas.getWidth() + ":" + canvas.getHeight() );


//            imgView.getGraphicsContext2D().drawImage(image, image.getWidth(), image.getHeight());
        } else {

            log.error("Could not load image");
        }

    }

    private Image getImage () {

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

        int canvasX = (int)mouseEvent.getSceneX() - (int)canvas.getLayoutX();
        int canvasY = (int)mouseEvent.getSceneY() - (int)canvas.getLayoutY();
//        GraphicsContext g = canvas.getGraphicsContext2D();
//        g.setFill(Color.BLACK);
//        g.fillRect(mouseEvent.getX(),mouseEvent.getY(), 10,10);

//        BufferedImage awtImg = SwingFXUtils.fromFXImage(imgView.getImage(),null);
//        awtImg.getWritableTile()
        Image img = imgView.getImage();
        WritableImage wImg = new WritableImage(img.getPixelReader(),(int)img.getWidth(), (int)img.getHeight());
        PixelWriter writer = wImg.getPixelWriter();

        for (int i = canvasX ;i <canvasX + 10; i++) {
            for (int j = canvasY; j< canvasY + 10; j++) {
                writer.setColor(i,j,Color.BLACK);
            }
        }
        imgView.setImage(wImg);

    }

    public void drawDragDetected(MouseEvent mouseEvent) {
    }
}