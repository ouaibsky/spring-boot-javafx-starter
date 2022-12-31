package org.icroco.javafx;

import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;

/**
 * If into your fxml file you're using fx:include instruction.
 * Your controller must extend this one to make sure your 'initialize' method is called one and only once.
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
