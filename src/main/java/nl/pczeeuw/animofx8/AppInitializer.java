package nl.pczeeuw.animofx8;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AppInitializer implements ApplicationListener<FxApplication.StageReadyEvent> {
    @Value(" classpath:fxml/main.fxml")
    private Resource mainResource;
    private ApplicationContext applicationContext;

    public AppInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(FxApplication.StageReadyEvent stageReadyEvent) {
        Stage stage = stageReadyEvent.getStage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(mainResource.getURL());
            fxmlLoader.setControllerFactory(aClass -> applicationContext.getBean(aClass));

            Parent parent = fxmlLoader.load();
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

