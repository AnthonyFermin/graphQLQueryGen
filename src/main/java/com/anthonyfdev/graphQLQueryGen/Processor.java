package com.anthonyfdev.graphQLQueryGen;


import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("com.anthonyfdev.graphQLQueryGen.GraphQLObject")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class Processor extends AbstractProcessor {

    private static final String PACKAGE_MODELS = "com.anthonyfdev.graphQLQueryGen.models";
    private static final String METHOD_GET_QUERY = "getQuery";
    private static final String MODEL_CLASS_PREFIX = "GraphQL_";
    static final String UNASSIGNED_VALUE = "[unassigned]";

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> graphQLObjectSet = roundEnv.getElementsAnnotatedWith(GraphQLObject.class);
        for (Element modelElement : graphQLObjectSet) {
            //building the getQuery() method
            MethodSpec.Builder mb = MethodSpec.methodBuilder(METHOD_GET_QUERY)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(String.class)
                    .addStatement("$1T sb = new $1T()", StringBuilder.class)
                    .addStatement("sb.append(\"{ \")");

            for (Element element : modelElement.getEnclosedElements()) {
                if (element.getKind().isField()
                        && element.getAnnotation(GraphQLField.class) != null) {

                    // adds alias, field name and arguments
                    addFieldLine(element, mb);
                    // adds field body
                    addFieldBody(element, mb);
                }
            }
            mb.addStatement("sb.append(\" }\")")
                    .addStatement("return sb.toString()");
            writeClassFile(modelElement, mb);
        }
        return true;
    }

    private void writeClassFile(Element modelElement, MethodSpec.Builder mb) {
        //building class
        String className = MODEL_CLASS_PREFIX + modelElement.getSimpleName().toString();
        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(mb.build())
                .build();

        //building class file
        JavaFile javaFile = JavaFile.builder(PACKAGE_MODELS, typeSpec)
                .build();

        //writing the file
        try {
            JavaFileObject source = processingEnv.getFiler().createSourceFile(PACKAGE_MODELS + "." + className);

            Writer writer = source.openWriter();
            writer.write(javaFile.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }
    }

    private void addFieldBody(Element element, MethodSpec.Builder mb) {
        if (!isScalarType(element)) {
            mb.addStatement("sb.append($1L.$2L$3L.$4L())",
                    PACKAGE_MODELS, MODEL_CLASS_PREFIX, getFieldType(element), METHOD_GET_QUERY);
        }
    }

    private void addFieldLine(Element element, MethodSpec.Builder mb) {
        String fieldName = element.getSimpleName().toString();
        mb.addStatement("sb.append(\" $L \")", fieldName);
        GraphQLField graphQLField = element.getAnnotation(GraphQLField.class);
        if (!graphQLField.aliasType().isEmpty() && !graphQLField.aliasType().equals(UNASSIGNED_VALUE)) {
            // use field name as alias
            mb.addStatement("sb.append(\": $L \")", graphQLField.aliasType());
        }
        //add arguments to field
        ArgMapping[] argMappings = graphQLField.arguments();
        if (argMappings.length > 0) {
            mb.addStatement("sb.append(\"(\")");
            for (int idx = 0; idx < argMappings.length; idx++) {
                ArgMapping mapping = argMappings[idx];

                mb.addStatement("sb.append(\"$L : \")", mapping.param());
                if (mapping.isString()) {
                    mb.addStatement("sb.append(\"\\\"\")");
                }
                mb.addStatement("sb.append(\"$L\")", mapping.value());
                if (mapping.isString()) {
                    mb.addStatement("sb.append(\"\\\"\")");
                }

                if (idx < argMappings.length - 1) {
                    mb.addStatement("sb.append(\", \")");
                }
            }
            mb.addStatement("sb.append(\")\")");
        }
    }

    private String getFieldType(Element element) {
        String verboseTypeName = element.asType().toString();
        int idxToSplit = verboseTypeName.lastIndexOf('.') + 1;
        return cleanType(verboseTypeName.substring(idxToSplit, verboseTypeName.length()));
    }

    private boolean isScalarType(Element element) {
        return element.asType().getKind().isPrimitive() || getFieldType(element).equals("String");
    }

    private String cleanType(String type) {
        return type.replaceAll("[.<>]", "").replaceAll("\\[", "").replaceAll("\\]", "");
    }
}
