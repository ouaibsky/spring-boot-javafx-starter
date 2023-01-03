package org.icroco.javafx;

import javafx.scene.Scene;

public interface FxView<T> {

    /**
     * @return a scene for this view (From FXML or programmatically)
     */
    Scene scene();

    /**
     * @return the associated controller.
     */
    T controller();

    /**
     * @return the annotation associated to this controller /view.
     */
    FxViewBinding binding();
}
