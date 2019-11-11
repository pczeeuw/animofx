package nl.pczeeuw.animofx8.controllers;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

    Image origImg;

    String origUrl;

    File origImgFile;

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
            menuBar.setPrefWidth(image.getWidth());

//            imgView.set

    }

}
