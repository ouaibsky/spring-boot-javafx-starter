package org.icroco.javafx;

import javafx.scene.Scene;

public interface FxView<T> {

    Scene scene();

    T controller();

    FxViewBinding binding();
}
