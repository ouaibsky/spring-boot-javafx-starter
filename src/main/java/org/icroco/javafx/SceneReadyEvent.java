package org.icroco.javafx;

import javafx.scene.Scene;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("unused")
public class SceneReadyEvent extends ApplicationEvent {
    public Scene getScene() {
        return (Scene) getSource();
    }

    public SceneReadyEvent(Object source) {
        super(source);
    }
}
