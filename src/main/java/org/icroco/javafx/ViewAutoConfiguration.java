package org.icroco.javafx;

import javafx.application.Platform;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Configuration
//@ConditionalOnClass(AbstractJavaFxApplication.class)
public class ViewAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    FxSupportedLocale getSupportedLocale() {
        return () -> Set.of(Locale.US);
    }


    @Bean
    I18N getI18n(FxSupportedLocale supportedLocale) {
        return new I18N(supportedLocale);
    }

    @Bean
    ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        var eventBus = new SimpleApplicationEventMulticaster();

        eventBus.setTaskExecutor(Platform::runLater);

        return eventBus;
    }

    @Bean
    ViewLoader viewLoader(ApplicationContext context) {
        return new ViewLoader(context);
    }

    @Bean
    ViewManager viewManager(List<FxView<?>> views) {
        return new ViewManager(views);
    }
}
