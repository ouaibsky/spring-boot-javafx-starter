package org.icroco.javafx;

import com.google.auto.service.AutoService;
import lombok.NoArgsConstructor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@NoArgsConstructor
@SupportedAnnotationTypes("org.icroco.javafx.FxViewBinding")
@SupportedSourceVersion(SourceVersion.RELEASE_19)
@AutoService(Processor.class)
public class FxViewProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (final Element element : roundEnv.getElementsAnnotatedWith(FxViewBinding.class)) {
            processingEnv.getMessager().printNote("Process, found: %s, class: '%s'%n".formatted(element, element.getSimpleName()));
            if (element instanceof TypeElement typeElement) {
                processingEnv.getMessager()
                             .printNote("typeElement: %s, qualifiedNamed: %s%n".formatted(typeElement, typeElement.getQualifiedName()));
                try {
                    writeView(typeElement.getQualifiedName(), typeElement.getSimpleName(), typeElement.getAnnotation(FxViewBinding.class));
                }
                catch (IOException e) {
                    processingEnv.getMessager()
                                 .printError("Cannot create view for class: '%s', annotated with: '%s'%n".formatted(typeElement,
                                                                                                                    FxViewBinding.class.getName()));
                    e.printStackTrace();
                }
            }
        }

        // Claiming that annotations have been processed by this processor
        return true;
    }

    private void writeView(final Name qualifiedName, final Name simpleName, FxViewBinding annotation) throws IOException {
        processingEnv.getMessager()
                     .printMessage(Diagnostic.Kind.NOTE, "simpleName: " + simpleName);
        var packageName = qualifiedName.toString().replace("." + simpleName.toString(), "");
        var viewName    = simpleName.toString().replace("Controller", "View");
        var output = """
                package %1$s;
                        
                import org.springframework.stereotype.Component;
                import org.icroco.javafx.ViewLoader;
                import org.icroco.javafx.SceneInfo;
                import org.icroco.javafx.FxViewDelegate;
                import org.icroco.javafx.FxView;
                                
                @Component
                public class %2$s extends FxViewDelegate<%5$s> {
                    public %2$s(ViewLoader loader, %5$s controller) {
                        super(loader.loadView(controller));
                    }
                }
                """.formatted(packageName,
                              viewName,
                              annotation.fxmlLocation(),
                              annotation.isPrimary(),
                              simpleName);

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(packageName + "." + viewName);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println(output);
        }
    }
}
