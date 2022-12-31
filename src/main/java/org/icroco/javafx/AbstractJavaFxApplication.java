package org.icroco.javafx;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Abstract Spring Boot extension of {@link Application}.
 * Comes from: <a href="https://github.com/yoep/spring-boot-starter-javafx/tree/49bfde00b5a910a6e7ce14489630ff0910ae5beb/src/main/java/com/github/spring/boot/javafx">...</a>
 */
@Slf4j
public abstract class AbstractJavaFxApplication extends Application {
    /**
     * The application context created by the JavaFX starter.
     * This context will only be available once the {@link #init()} has been invoked by JavaFX.
     */
    protected ConfigurableApplicationContext applicationContext;

    /**
     * Launch a JavaFX application with for the given class and program arguments.
     *
     * @param appClass The class to launch the JavaFX application for.
     * @param args     The program arguments.
     */
    @SuppressWarnings("unused")
    public static void launch(Class<? extends Application> appClass, String... args) {
        Application.launch(appClass, args);
    }

    /**
     * Launch a JavaFX application with for the given class and program arguments.
     *
     * @param appClass       The class to launch the JavaFX application for.
     * @param preloaderClass The class to use as the preloader of the JavaFX application.
     * @param args           The program arguments.
     */
    @SuppressWarnings("unused")
    public static void launch(Class<? extends Application> appClass, Class<? extends Preloader> preloaderClass, String... args) {
        System.setProperty("javafx.preloader", preloaderClass.getName());
        Application.launch(appClass, args);
    }

    /**
     * Launch a JavaFX application with the given program arguments.
     *
     * @param args The program arguments.
     */
    @SuppressWarnings("unused")
    public static void launch(String... args) {
        Application.launch(args);
    }

    @Override
    public final void init() {
        try {
        log.debug("Init: {}", getClass().getSimpleName());
        ApplicationContextInitializer<GenericApplicationContext> initializer = genericApplicationContext -> {
            genericApplicationContext.registerBean(Application.class, () -> AbstractJavaFxApplication.this);
            genericApplicationContext.registerBean(Parameters.class, this::getParameters);
            genericApplicationContext.registerBean(HostServices.class, this::getHostServices);
        };

        applicationContext = new SpringApplicationBuilder()
                .sources(getClass())
                .bannerMode(Banner.Mode.OFF)
                .headless(false)
                .initializers(initializer)
                .build()
                .run(getParameters().getRaw().toArray(new String[0]));

        postInit();
        } catch (Exception ex) {
            log.error("Unexpected error while init", ex);
        }
    }


    @Override
    public final void start(Stage primaryStage) {
        try {
            log.debug("Start: {}", getClass().getSimpleName());
            preStart(primaryStage);
            Thread.setDefaultUncaughtExceptionHandler(this::showError);
            postStart(primaryStage);
            primaryStage.setOnCloseRequest(this::closeRequest);
            applicationContext.publishEvent(new StageReadyEvent(primaryStage));
        } catch (Exception ex) {
            log.error("Unexpected error while starting", ex);
        }
    }

    @Override
    public final void stop() throws Exception {
        preStop();
        applicationContext.close();
        System.exit(0);
    }

    private void showError(Thread thread, Throwable throwable) {
        log.error("An unexpected error occurred in thread: {}, ", thread, throwable);
        if (Platform.isFxApplicationThread()) {
            showErrorToUser(throwable);
        }
    }

    /**
     * To be overwritten if you want to present any un-expected exception to end user.
     *
     */
    protected void showErrorToUser(Throwable throwable) {
        log.debug("Make sure to override showErrorToUser to present error to user: {}", throwable.getMessage());
    }

    protected void postInit() {
        log.debug("postInit not implemented");
    }

    @SuppressWarnings("unused")
    protected void preStart(Stage primaryStage) {
        log.debug("preStart not implemented: {}", primaryStage);
    }

    @SuppressWarnings("unused")
    protected void postStart(Stage primaryStage) {
        log.debug("postStart not implemented: {}", primaryStage);
    }

    protected void preStop() {
        log.debug("preStop not implemented");
    }

    protected void closeRequest(WindowEvent windowEvent) {
        log.debug("closeRequest not implemented: {}", windowEvent);
    }

}