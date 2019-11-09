package nl.pczeeuw.animofx8;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import nl.pczeeuw.animofx8.views.MainView;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Animofx8Application extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
//        SpringApplication.run(Animofx8Application.class, args);
        launch(Animofx8Application.class, MainView.class, args);
    }

}
