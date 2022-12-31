package org.icroco.javafx;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
@Documented
@SuppressWarnings("unused")
public @interface FxViewBinding {
    /**
     * Only one Controlle can be annotated with isPrimary value to true. If multiple are primary, an exception will be triggered st startup time.
     *
     * @return true if the controller annotated represents the main Window.
     */
    boolean isPrimary() default false;

    /**
     * @return a URI of a fxml resource.
     * if location start with a '/', resource will be loaded according to root classpath.
     * If not it will be loaded according to Controller package.
     */
    String fxmlLocation() default "";

    /**
     *
     * @return a unique id across all controller. If duplicate an exception will be triggered a startup time.
     */
    String id() default "";
}
