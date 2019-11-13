package nl.pczeeuw.animofx8.services;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.Screen;
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
import java.nio.file.Paths;
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
        return filesToProces.stream().map(this::fileToImage).collect(Collectors.toList());
    }

    public Image fileToImage(File file) {
        try {
            return new Image(new FileInputStream(file), Screen.getPrimary().getBounds().getWidth() * 0.75, Screen.getPrimary().getBounds().getHeight() * 0.75, true, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<File> checkFiles(List<File> files) {
        List<File> result = new ArrayList<>();

        for (File file : files) {
            String extension = getFileExtension(file.getName()).toLowerCase();
            if (Arrays.asList(allowedImgExtensions).contains(extension)) {
                result.add(file);
            } else if (file.getName().toLowerCase().endsWith("pdf")) {
                log.info("Pdf detected");
                result.addAll(pdfToImageFile(file));
            } else {
                log.error(extension + " is geen valide extensie");
            }
        }
        return result;
    }

//    public Image scaleImage(Image imgToScale, Rectangle2D maxScreenSize) {
//
//
//    }
//
//    private Image scaleImage (Image image, double scale) {
//        image.
//    }

    private List<File> pdfToImageFile(File pdfFile) {
        List<File> result = new ArrayList<>();
        PDDocument doc = null;
        try {
            doc = PDDocument.load(pdfFile);
            PDFRenderer renderer = new PDFRenderer(doc);

            for (int page = 0; page < doc.getNumberOfPages(); ++page) {
                BufferedImage bim = renderer.renderImageWithDPI(page, 600, ImageType.RGB);

                File tmpFile = new File(getFilePath(pdfFile.getAbsolutePath()) + File.separator + "_" + page + "_" + pdfFile.getName() + ".jpeg");
                if (!tmpFile.exists()) {
                    tmpFile.createNewFile();
                }

                ImageIO.write(bim, "jpeg", tmpFile);
                result.add(tmpFile);
            }

            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getFileExtension(String fileName) {
        String result ="";
        if (fileName.contains(".")) {
            result = fileName.substring(fileName.lastIndexOf(".") + 1);
//            if (result.contains("jpg")) {
//                return result.replaceAll("jpg","png");
//            } else {
//                return result;
//            }
        } else {
            log.info("No extension found!");
        }
        return result;
    }

    public boolean saveToFile(Image image, String fullPath) {
        File outputFile = new File(getFilePath(fullPath) + File.separator +  "_" + getFileName(fullPath));
        log.info("New file path: " + outputFile.getAbsolutePath());
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        bImage = ensureOpaque(bImage);
        try {
            ImageIO.write(bImage, getFileExtension(outputFile.getName()) , outputFile);
            return true;
        } catch (IOException e) {
            log.error("Er is iets misgegaan bij het wegschrijven van file " + outputFile.getAbsolutePath());
            throw new RuntimeException(e);
        }
    }

    private String getFileName (String fullFilePath) {
        String filename = Paths.get(fullFilePath).getFileName().toString();
        if (filename.startsWith("_")) {
            filename = filename.replaceFirst("_","");
        }

//        return filename.replaceAll(getFileExtension(filename),"png");
        return filename;
    }

    private String getFilePath ( String fullFilePath) {
        return Paths.get(fullFilePath).getParent().toString();
    }

    private BufferedImage ensureOpaque(BufferedImage bi) {
        if (bi.getTransparency() == BufferedImage.OPAQUE)
            return bi;
        int w = bi.getWidth();
        int h = bi.getHeight();
        int[] pixels = new int[w * h];
        bi.getRGB(0, 0, w, h, pixels, 0, w);
        BufferedImage bi2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        bi2.setRGB(0, 0, w, h, pixels, 0, w);
        return bi2;
    }
}
