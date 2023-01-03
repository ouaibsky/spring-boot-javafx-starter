package org.icroco.javafx;


import javafx.scene.Scene;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;

@AllArgsConstructor
public class FxViewDelegate<T> implements FxView<T> {
    @NonNull
    private final FxViewImpl<T> delegate;

    @Override
    public Scene scene() {
        return delegate.scene();
    }

    @Override
    public T controller() {
        return delegate.controller();
    }

    @Override
    public FxViewBinding binding() {
        return delegate.binding();
    }
}
