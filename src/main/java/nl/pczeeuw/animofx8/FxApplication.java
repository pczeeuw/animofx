package nl.pczeeuw.animofx8;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FxApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage stage) throws Exception {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void init() throws Exception {
        applicationContext = new SpringApplicationBuilder(FxApplication.class).run();
    }

    @Override
    public void stop() throws Exception {
        applicationContext.stop();
    }

    public class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage () {
            return (Stage) getSource();
        }
    }
}