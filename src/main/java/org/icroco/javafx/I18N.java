package org.icroco.javafx;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * I18N utility class..
 */
@Slf4j
public class I18N {

    private final FxSupportedLocale supportedLocale;

    /**
     * the current selected Locale.
     */
    private final ObjectProperty<Locale> locale;

    public I18N(FxSupportedLocale supportedLocale) {
        this.supportedLocale = supportedLocale;
        this.locale = new SimpleObjectProperty<>(getDefaultLocale());
        locale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
    }

    public Set<Locale> getSupportedLocale() {
        return supportedLocale.getSupportedLocal();
    }

    /**
     * get the default locale. This is the systems default if contained in the supported locales, english otherwise.
     */
    private Locale getDefaultLocale() {
        Locale sysDefault = Locale.getDefault();
        return supportedLocale.getSupportedLocal().contains(sysDefault) ? sysDefault : Locale.ENGLISH;
    }

    public Locale getLocale() {
        return locale.get();
    }

    public void setLocale(Locale locale) {
        localeProperty().set(locale);
        Locale.setDefault(locale);
    }

    public ObjectProperty<Locale> localeProperty() {
        return locale;
    }

    /**
     * gets the string with the given key from the resource bundle for the current locale and uses it as first argument
     * to MessageFormat.format, passing in the optional args and returning the result.
     *
     * @param key  message key
     * @param args optional arguments for the message
     * @return localized formatted string
     */
    public String get(final String key, boolean isOptional, final Object... args) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", getLocale());
            return MessageFormat.format(bundle.getString(key), args);
        }
        catch (MissingResourceException ex) {
            log.warn("Cannot find resource for key: '{}' nad locale: '{}'", key, getLocale());
        }
        return isOptional ? "" : "NotFound";
    }

    public String get(final String key, final Object... args) {
        return get(key, false, args);
    }

    /**
     * creates a String binding to a localized String for the given message bundle key
     *
     * @param key key
     * @return String binding
     */
    public StringBinding createStringBinding(final String key, Object... args) {
        return Bindings.createStringBinding(() -> get(key, args), locale);
    }

    public void bind(final Label node, final String key, Object... args) {
        node.textProperty().bind(createStringBinding(key, args));
        bind(node.getTooltip(), key + ".tooltip", true, args);
    }

    public void bind(final Button node, final String key, Object... args) {
        node.textProperty().bind(createStringBinding(key, args));
        bind(node.getTooltip(), key + ".tooltip", args);
    }

    public void bind(final ToggleButton node, final String key, Object... args) {
        node.textProperty().bind(createStringBinding(key, args));
    }

    public void bind(final MenuButton node, final String key, Object... args) {
        node.textProperty().bind(createStringBinding(key, args));
    }

    public void bind(final MenuItem node, final String key, Object... args) {
        node.textProperty().bind(createStringBinding(key, args));
    }

    public void bind(final Menu node, final String key, Object... args) {
        node.textProperty().bind(createStringBinding(key, args));
    }

    public void bind(final Hyperlink node, final String key, Object... args) {
        node.textProperty().bind(createStringBinding(key, args));
    }

    public void bind(final TextInputControl node, final String key, Object... args) {
        node.textProperty().bind(createStringBinding(key, args));
        bind(node.getTooltip(), key + ".tooltip", true, args);
        node.promptTextProperty().bind(createStringBinding(key + ".prompt", true, args));
    }

    /**
     * creates a String Binding to a localized String that is computed by calling the given func
     *
     * @param func function called on every change
     * @return StringBinding
     */
    public StringBinding createStringBinding(Callable<String> func) {
        return Bindings.createStringBinding(func, locale);
    }

    private void bind(final Tooltip node, final String key, Object... args) {
        if (node != null) {
            node.textProperty().bind(createStringBinding(key, args));
        }
    }
}