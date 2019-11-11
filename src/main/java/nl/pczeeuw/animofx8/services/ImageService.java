package nl.pczeeuw.animofx8.services;

import javafx.scene.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImageService {

    private String[] allowedImgExtensions = {"jpg", "jpeg", "bmp", "gif", "png"};

    public List<Image> filesToImages(List<File> files) {
        List<File> filesToProces = checkFiles(files);
        return files.stream().map(this::fileToImage).collect(Collectors.toList());
    }

    public Image fileToImage(File file) {
        try {
            return new Image(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<File> checkFiles(List<File> files) {
        List<File> result = new ArrayList<>();

        for (File file : files) {
            if (file.getName().contains(".")) {
                String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                if (Arrays.asList(allowedImgExtensions).contains(extension)) {
                    result.add(file);
                }else if (file.getName().toLowerCase().endsWith("pdf")) {
                    log.info("Pdf detected");
                    result.addAll(pdfToImageFile(file));
                } else {
                    log.error(extension + " is geen valide extensie");
                }
            } else {
                log.error("File heeft geen extensie");
            }
        }
        return result;
    }

    private List<File> pdfToImageFile(File pdfFile) {
        List<File> result = new ArrayList<>();
        PDDocument doc = null;
        try {
            doc = PDDocument.load(pdfFile);

            PDFRenderer renderer = new PDFRenderer(doc);
            for (int page = 0; page < doc.getNumberOfPages(); ++page) {
                BufferedImage bim = renderer.renderImageWithDPI(page, 300, ImageType.RGB);

                // suffix in filename will be used as the file format
                File tmpFile = new File(pdfFile.getAbsolutePath() + page + ".png");
                ImageIO.write(bim, pdfFile.getName() + ".png", tmpFile);
                result.add(tmpFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
