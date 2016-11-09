package com.anthonyfdev.graphQLQueryGen;


import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("com.anthonyfdev.graphQLQueryGen.GraphQLObject")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class Processor extends AbstractProcessor {

    private static final String MODELS_PACKAGE = "com.anthonyfdev.graphQLQueryGen.models";

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<Element> graphQLObjectSet = (Set<Element>) roundEnv.getElementsAnnotatedWith(GraphQLObject.class);
        for (Element modelElement : graphQLObjectSet) {
            //building the getQuery() method
            MethodSpec.Builder mb = MethodSpec.methodBuilder("getQuery")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(String.class)
                    .addStatement("$T sb = new StringBuilder()", StringBuilder.class)
                    .addStatement("sb.append(\"{ \")");

            for (Element element : modelElement.getEnclosedElements()) {
                if (element.getKind().isField()
                        && element.getAnnotation(GraphQLField.class) != null) {
                    String fieldName = element.getSimpleName().toString();

                    mb.addStatement("sb.append(\" " + fieldName + " \")");
                    if (!isScalarType(element)) {
                        mb.addStatement("sb.append(com.anthonyfdev.graphQLQueryGen.models.GraphQL_"
                                        + getFieldType(element)
                                        + ".getQuery())");
                    }
                }
            }
            mb.addStatement("sb.append(\" }\")")
                    .addStatement("return sb.toString()");

            //building class
            String className = "GraphQL_" + getClassName(modelElement);
            TypeSpec typeSpec = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(mb.build())
                    .build();

            //building class file
            JavaFile javaFile = JavaFile.builder(MODELS_PACKAGE, typeSpec)
                    .build();

            //writing the file
            try {
                JavaFileObject source = processingEnv.getFiler().createSourceFile(MODELS_PACKAGE + "." + className);

                Writer writer = source.openWriter();
                writer.write(javaFile.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                // Note: calling e.printStackTrace() will print IO errors
                // that occur from the file already existing after its first run, this is normal
            }

        }
        return true;
    }

    private String getFieldType(Element element) {
        String verboseTypeName = element.asType().toString();
        int idxToSplit = verboseTypeName.lastIndexOf('.') + 1;
        return cleanType(verboseTypeName.substring(idxToSplit, verboseTypeName.length()));
    }

    private boolean isScalarType(Element element) {
        return element.asType().getKind().isPrimitive() || getFieldType(element).equals("String");
    }

    private String getClassName(Element modelElement) {
        return modelElement.getSimpleName().toString().replace(".class", "");
    }

    private String cleanType(String type) {
        return type.replaceAll("[.<>]","").replaceAll("\\[", "").replaceAll("\\]", "");
    }
}
