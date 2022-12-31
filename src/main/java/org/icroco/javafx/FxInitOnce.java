package org.icroco.javafx;

import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;

/**
 * If into your fxml file you're using fx:include instruction. your controller must extend this one to make sure your initialize is called one end only one.
 */
@Slf4j
public abstract class FxInitOnce implements FxInit {
    private boolean alreadyInitialized = false;

    @FXML
    public final void initialize() {
        if (!alreadyInitialized) {
            alreadyInitialized = true;
            initializedOnce();
        } else {
            log.warn("Looks like 'initialize' method of your controller is called more that one time");
        }
    }

    protected abstract void initializedOnce();
}
