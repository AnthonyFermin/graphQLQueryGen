package com.anthonyfdev.graphQLQueryGen;


import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("com.anthonyfdev.graphQLQueryGen.GraphQLObject")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class Processor extends AbstractProcessor {

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<Element> graphQLObjectSet = (Set<Element>) roundEnv.getElementsAnnotatedWith(GraphQLObject.class);
        for (Element modelElement : graphQLObjectSet) {
            StringBuilder sbClass = new StringBuilder();
            String name = "GraphQL_" + getClassName(modelElement);
            sbClass.append("package com.anthonyfdev.graphQLQueryGen.generated;\n\n")
                    .append("public class ")
                    .append(name)
                    .append(" {\n")
                    .append("\tpublic String getQuery() {\n")
                    .append("\t\tStringBuilder sb = new StringBuilder();\n")
                    .append("\t\tsb.append(\"{ \");\n");
            for (Element element : modelElement.getEnclosedElements()) {
                if (graphQLObjectSet.contains(element)) {
                    sbClass.append("\t\tsb.append(GraphQL_" + getClassName(element) + ".getQuery())");
                } else if (isScalarType(element)) {
                    if (element.getKind().isField()) {
                    }
                    sbClass.append("\t\tsb.append()");
                }
            }
            sbClass.append("\t\t\t.append(\"}\"")
                    .append("\t\treturn sb.toString();\n\t}\n}");

            try { // write the file
                JavaFileObject source = processingEnv.getFiler().createSourceFile("com.anthonyfermin.graphQLQueryGen.generated." + name);


                Writer writer = source.openWriter();
                writer.write(sbClass.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                // Note: calling e.printStackTrace() will print IO errors
                // that occur from the file already existing after its first run, this is normal
            }

        }
        return true;
    }

    private boolean isScalarType(Element element) {
        return element.asType().getKind().isPrimitive() || getClassName(element).equals("String");
    }

    private String getClassName(Element modelElement) {
        return modelElement.getSimpleName().toString().replace(".class", "");
    }

    private boolean isGraphQLAnnotated(Element element) {
        element.asType().toString();
        return false;
    }
}
