open module picture.fwk {
    requires lombok;
    requires transitive java.compiler;
    requires transitive org.slf4j;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires java.management;
    requires transitive jakarta.annotation;
    requires spring.context;
    requires spring.core;
    requires spring.boot.autoconfigure;
    requires spring.beans;
    requires spring.aop;
    requires spring.boot;
    requires com.google.auto.service;

    exports org.icroco.javafx;

    provides javax.annotation.processing.Processor with
            org.icroco.javafx.FxViewProcessor;

}