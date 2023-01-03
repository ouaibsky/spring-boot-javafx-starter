package org.icroco.javafx;

import javafx.scene.Scene;

public record FxViewImpl<T>(Scene scene, T controller, FxViewBinding binding) implements FxView<T> {
}
