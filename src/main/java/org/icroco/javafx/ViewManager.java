package org.icroco.javafx;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Slf4j
public class ViewManager {
    private final Map<String, FxView<?>> views;

    public ViewManager(final List<FxView<?>> views) {
        this.views = views.stream()
                          .collect(toMap(si -> si.binding().id(), identity()));
    }

    @PostConstruct
    public void postConstruct() {
        if (views.isEmpty()) {
            throw new IllegalStateException("Any views found, define at least one !");
        }

        getPrimary();
    }


    public <C> FxView<C> getPrimary() {
        return views.values()
                    .stream()
                    .filter(si -> si.binding().isPrimary())
                    .findFirst()
                    .or(() -> views.values().stream().findFirst())
                    .map(v -> (FxView<C>) v)
                    .orElseThrow(() -> new IllegalStateException("At least one view must be defined as primary."));
    }

    public Optional<FxView<?>> getView(final String name) {
        return Optional.ofNullable(views.get(name));
    }

    @EventListener(StageReadyEvent.class)
    public void stageReady(StageReadyEvent event) {
        log.debug("Stage ready received");
        event.getStage().setScene(getPrimary().scene());
        event.getStage().show();
    }
}
