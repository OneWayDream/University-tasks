import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes(value = {"FtlhForm"})
public class FtlhProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(FtlhForm.class);
        Set<? extends Element> subElements = roundEnv.getElementsAnnotatedWith(FtlhInput.class);
        for (Element element : annotatedElements) {

            String path = FtlhProcessor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            path = path.substring(1) + element.getSimpleName().toString() + ".ftlh";
            //processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, path);
            Path out = Paths.get(path);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(out.toFile()));
                FtlhForm annotation = element.getAnnotation(FtlhForm.class);
                writer.write("<form action='" + annotation.action() + "' method='" + annotation.method() + "'>");
                writer.write("\n");
                for (Element subElement : subElements) {
                    if (subElement.getSimpleName().equals(subElement.getSimpleName())){
                        FtlhInput subAnnotation = subElement.getAnnotation(FtlhInput.class);
                        writer.write("<input type='" + subAnnotation.type() + "' name='" + subAnnotation.name()
                                + "' placeholder='" + subAnnotation.placeholder() + "'>");
                        writer.write("\n");
                    }
                }
                writer.write("</form>");
                writer.close();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return true;
    }
}

