package org.icroco.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
@Slf4j
public class ViewLoader {
    private final ApplicationContext applicationContext;

    public <T> SceneInfo<T> loadView(T controller) {
        return loadView(controller, controller.getClass().getAnnotation(FxViewBinding.class));
    }

    public <T> SceneInfo<T> loadView(T controller, FxViewBinding annotation) {
        if (annotation == null) {
            throw new IllegalStateException("Class: '%s' is not annotated with '@ %s'"
                    .formatted(controller.getClass().getSimpleName(), FxViewBinding.class.getSimpleName()));
        }
        requireNonNull(annotation.fxmlLocation(), "LoadingClass location cannot be empty");
        var resource = controller.getClass().getResource(annotation.fxmlLocation());
        requireNonNull(resource, "resource not found: " + annotation.fxmlLocation());

        var fxmlLoader = new FXMLLoader(resource);
        fxmlLoader.setControllerFactory(applicationContext::getBean);

        try {
            Region root = fxmlLoader.load();
            Scene scene = new Scene(root);

            return new SceneInfo<>(scene, controller, annotation);

        } catch (IOException ex) {
            throw new IllegalArgumentException("Unexpected error while loading view: " + annotation.fxmlLocation(), ex);
        }
    }
}
