package org.icroco.javafx;

import lombok.NoArgsConstructor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

//@Slf4j
@NoArgsConstructor
@SupportedAnnotationTypes("org.icroco.picture.fwk.FxViewBinding")
@SupportedSourceVersion(SourceVersion.RELEASE_19)
public class FxViewProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (final Element element : roundEnv.getElementsAnnotatedWith(FxViewBinding.class)) {
            System.out.printf("Process, found: %s, class: '%s'%n", element, element.getSimpleName());
            if (element instanceof TypeElement typeElement) {
                System.out.printf("typeElement: %s, qualifiedNamed: %s%n", typeElement, typeElement.getQualifiedName());
                try {
                    writeView(typeElement.getQualifiedName(), typeElement.getSimpleName(), typeElement.getAnnotation(FxViewBinding.class));
                }
                catch (IOException e) {
                    System.err.printf("Cannot create view for class: '%s', annotated with: '%s'%n", typeElement, FxViewBinding.class.getName());
                    e.printStackTrace();
                }
                for (final Element eclosedElement : typeElement.getEnclosedElements()) {
//                    if( eclosedElement instanceof VariableElement ) {
//                        final VariableElement variableElement = ( VariableElement )eclosedElement;
//
//                        if( !variableElement.getModifiers().contains( Modifier.FINAL ) ) {
//                            processingEnv.getMessager().printMessage( Diagnostic.Kind.ERROR,
//                                    String.format( "Class '%s' is annotated as @Immutable,
//                                            but field '%s' is not declared as final",
//                            typeElement.getSimpleName(), variableElement.getSimpleName()
//               )
//             );
//                        }
                }
            }
        }

        // Claiming that annotations have been processed by this processor
        return true;
    }

    private void writeView(final Name qualifiedName, final Name simpleName, FxViewBinding annotation) throws IOException {
        System.out.println("simpleName: " + simpleName);
        var packageName = qualifiedName.toString().replace("." + simpleName.toString(), "");
        var viewName    = simpleName.toString().replace("Controller", "View");
        var output = """
                package %1$s;
                        
                import org.springframework.stereotype.Component;
                import org.icroco.picture.fwk.ViewLoader;
                import org.icroco.picture.fwk.SceneInfo;
                import org.icroco.picture.fwk.FxViewDelegate;
                import org.icroco.picture.fwk.FxView;
                                
                @Component       
                public class %2$s extends FxViewDelegate { 
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
