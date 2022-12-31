package org.icroco.javafx;

import javafx.fxml.FXML;

/**
 * If into your fxml file you're using fx:include instruction. your controller must extend this one to make sure your initialize is called one end only one.
 */
public interface FxInit {

    @FXML
    default void initialize() {}
}
