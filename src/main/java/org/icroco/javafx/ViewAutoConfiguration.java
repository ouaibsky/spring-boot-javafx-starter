package org.icroco.javafx;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    ViewLoader viewLoader(ApplicationContext context) {
        return new ViewLoader(context);
    }

    @Bean
    ViewManager viewManager(final ConfigurableApplicationContext applicationContext, List<FxView<?>> views) {
        return new ViewManager(applicationContext, views);
    }
}
