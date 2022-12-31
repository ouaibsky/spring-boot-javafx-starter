package org.icroco.javafx;

import javafx.scene.Scene;

public record SceneInfo<T>(Scene scene, T controller, FxViewBinding binding) implements FxView<T> {
}
