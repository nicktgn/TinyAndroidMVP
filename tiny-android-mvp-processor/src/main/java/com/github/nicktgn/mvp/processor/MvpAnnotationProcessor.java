/*
 * Copyright $year  Nick Tsygankov (nicktgn@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.nicktgn.mvp.processor;

/**
 * Created by nick on 22/03/2018.
 */

import com.github.nicktgn.mvp.annotations.MVPActivity;
import com.github.nicktgn.mvp.annotations.MVPFragmentCompat;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class MvpAnnotationProcessor extends AbstractProcessor {

    private final String SUFFIX = "Compat";

    private final static String PREFIX = "Mvp";

    private final static String CLASS_NAME_FRAGMENT_COMPAT = "android.support.v4.app.Fragment";
    private final static String CLASS_NAME_ACTIVITY = "android.app.Activity";

    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Types typeUtils;

    private Map<String, Boolean> createdClasses = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        typeUtils = processingEnvironment.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {

        processMvpFragmentCompat(roundEnvironment);

        processMvpActivity(roundEnvironment);

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new LinkedHashSet<String>() {{
            add(MVPFragmentCompat.class.getCanonicalName());
            add(MVPActivity.class.getCanonicalName());
        }};
    }

    /*
    private void processMyAnnotation(RoundEnvironment roundEnvironment){
        Map<Element, List<Element>> dependants = new HashMap<>();

        Set<? extends Element> fields = roundEnvironment.getElementsAnnotatedWith(MyAnnotation.class);
        for (Element field : fields) {
            try {
                validateField(field);
            } catch (ProcessingException e) {
                e.print(messager);
                continue;
            }

            // Get the class that contains this field
            Element parent = field.getEnclosingElement();

            // Add the field to list of dependencies for this class
            List<Element> dependantFields = dependants.getOrDefault(parent, new ArrayList<Element>());
            dependantFields.add(field);

            dependants.put(parent, dependantFields);
        }

        try {
            generateCode(dependants);
        } catch (ProcessingException e) {
            e.print(messager);
        }
    }*/

    private void processMvpFragmentCompat(RoundEnvironment roundEnvironment){
        List<Element> dependants = new ArrayList<>();

        Set<? extends Element> classes = roundEnvironment.getElementsAnnotatedWith(MVPFragmentCompat.class);

        for (Element classElement : classes) {
            try {
                validateMvpFragmentCompatClass(classElement);
            } catch (ProcessingException e) {
                e.print(messager);
                continue;
            }

            dependants.add(classElement);
        }

        try {
            generateCodeMvpFragmentCompat(dependants);
        } catch (ProcessingException e) {
            e.print(messager);
        }
    }

    private void processMvpActivity(RoundEnvironment roundEnvironment) {
        List<Element> dependants = new ArrayList<>();

        Set<? extends Element> classes = roundEnvironment.getElementsAnnotatedWith(MVPActivity.class);

        for (Element classElement : classes) {
            try {
                validateMvpActivityClass(classElement);
            } catch (ProcessingException e) {
                e.print(messager);
                continue;
            }

            dependants.add(classElement);
        }

        try {
            generateCodeMvpActivity(dependants);
        } catch (ProcessingException e) {
            e.print(messager);
        }
    }

    private void validateMvpActivityClass(Element classElement) throws ProcessingException {
        if (classElement.getKind() != ElementKind.CLASS) {
            throw new ProcessingException(classElement, "Can only be applied to classes");
        }

        TypeMirror fragmentClass = getTypeMirror(classElement.getAnnotation(MVPActivity.class));
        TypeMirror requiredSuperClass = elementUtils.getTypeElement(CLASS_NAME_ACTIVITY).asType();

        if (!typeUtils.isSubtype(fragmentClass, requiredSuperClass)){
            throw new ProcessingException(classElement, "Extending activity class should be a sub-type of " + CLASS_NAME_ACTIVITY);
        }
    }

    private void validateMvpFragmentCompatClass(Element classElement) throws ProcessingException {
        if (classElement.getKind() != ElementKind.CLASS) {
            throw new ProcessingException(classElement, "Can only be applied to classes");
        }

        TypeMirror fragmentClass = getTypeMirror(classElement.getAnnotation(MVPFragmentCompat.class));
        TypeMirror requiredSuperClass = elementUtils.getTypeElement(CLASS_NAME_FRAGMENT_COMPAT).asType();

        if (!typeUtils.isSubtype(fragmentClass, requiredSuperClass)){
            throw new ProcessingException(classElement, "Extending fragment class should be a sub-type of " + CLASS_NAME_FRAGMENT_COMPAT);
        }
    }

    private void validateField(Element field) throws ProcessingException {
        Element enclosing = field.getEnclosingElement();
        if (enclosing == null || enclosing.getKind() != ElementKind.CLASS) {
            throw new ProcessingException(field, "Only classes can have dependencies.");
        }

        if (field.getModifiers().contains(Modifier.PRIVATE)) {
            throw new ProcessingException(field, "protected or higher access is required.");
        }

        if (field.getModifiers().contains(Modifier.FINAL)) {
            throw new ProcessingException(field, "final fields cannot be dependent.");
        }
    }

    private TypeMirror getTypeMirror(MVPActivity annotation) {
        try {
            annotation.value(); // this should throw
        } catch( MirroredTypeException mte ) {
            return mte.getTypeMirror();
        }
        return null; // can this ever happen ??
    }

    private TypeMirror getTypeMirror(MVPFragmentCompat annotation) {
        try {
            annotation.value(); // this should throw
        } catch( MirroredTypeException mte ) {
            return mte.getTypeMirror();
        }
        return null; // can this ever happen ??
    }

    private TypeElement asTypeElement(TypeMirror typeMirror) {
        return (TypeElement) typeUtils.asElement(typeMirror);
    }

    private boolean isClassCreated(String packageName, String className){
        return createdClasses.containsKey(packageName+'.'+className);
    }

    private boolean setClassCreated(String packageName, String className){
        if (isClassCreated(packageName, className)){
            return false;
        }
        createdClasses.put(packageName+'.'+className, true);
        return true;
    }

    private void generateCodeMvpActivity(List<Element> userClasses) throws ProcessingException{
        for (Element userClass: userClasses){

            TypeElement fragmentClass = asTypeElement(getTypeMirror(userClass.getAnnotation(MVPActivity.class)));

            String mvpActivityClassName = PREFIX + fragmentClass.getSimpleName().toString();

            PackageElement pkg = elementUtils.getPackageOf(userClass);
            String packageName = pkg.isUnnamed() ? "" : pkg.getQualifiedName().toString();

            if(isClassCreated(packageName, mvpActivityClassName)){
                continue;
            }

            ClassName baseFragmentClassName = ClassName.get(fragmentClass);
            ClassName mvpViewClassName = ClassName.get("com.github.nicktgn.mvp", "MvpView");
            ClassName mvpInterfaceClassName = ClassName.get("com.github.nicktgn.mvp", "IMvpActivity");
            ClassName mvpPresenterClassName = ClassName.get("com.github.nicktgn.mvp", "MvpPresenter");
            ClassName mvpActivityHelperClassName = ClassName.get("com.github.nicktgn.mvp", "MvpActivityHelper");
            ClassName bundleClassName = ClassName.get("android.os", "Bundle");

            // class body
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(mvpActivityClassName)
                    //.addOriginatingElement(enclosing)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addTypeVariable(TypeVariableName.get("V", mvpViewClassName))
                    .addTypeVariable(TypeVariableName.get("P", mvpPresenterClassName))
                    .superclass(baseFragmentClassName)
                    .addSuperinterface(mvpInterfaceClassName);

            // field - mvp fragment helper
            FieldSpec.Builder mvpHelperField = FieldSpec.builder(mvpActivityHelperClassName, "mvp")
                    .addModifiers(Modifier.PRIVATE)
                    .initializer("new $T()", mvpActivityHelperClassName);
            classBuilder.addField(mvpHelperField.build());

            // field - protected presenter
            FieldSpec.Builder presenterField = FieldSpec.builder(TypeVariableName.get("P"), "presenter")
                    .addModifiers(Modifier.PROTECTED);
            classBuilder.addField(presenterField.build());

            // method - onActivityCreated
            MethodSpec.Builder onActivityCreatedMethod = MethodSpec.methodBuilder("onActivityCreated")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(bundleClassName, "savedInstanceState")
                    .addStatement("super.onActivityCreated(savedInstanceState)")
                    .addStatement("presenter = getPresenter()")
                    .addStatement("mvp.onActivityCreated(this, savedInstanceState, presenter)");
            classBuilder.addMethod(onActivityCreatedMethod.build());

            // method - onSaveInstanceState
            MethodSpec.Builder onSaveInstanceStateMethod = MethodSpec.methodBuilder("onSaveInstanceState")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(bundleClassName, "outState")
                    .addStatement("mvp.onSaveInstanceState(outState)")
                    .addStatement("super.onSaveInstanceState(outState)");
            classBuilder.addMethod(onSaveInstanceStateMethod.build());

            // method - onDestroyView
            MethodSpec.Builder onDestroyViewMethod = MethodSpec.methodBuilder("onDestroyView")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addStatement("mvp.onDestroyView(this)")
                    .addStatement("super.onDestroyView()");
            classBuilder.addMethod(onDestroyViewMethod.build());

            // method - getPresenter
            MethodSpec.Builder getPresenterMethod = MethodSpec.methodBuilder("getPresenter")
                    .addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED)
                    .returns(TypeVariableName.get("P"));
            classBuilder.addMethod(getPresenterMethod.build());


            try {
                JavaFile.builder(packageName, classBuilder.build()).build().writeTo(filer);

                setClassCreated(packageName, mvpActivityClassName);
            } catch (IOException e) {
                throw new ProcessingException(userClass, e.getMessage());
            }
        }
    }

    private void generateCodeMvpFragmentCompat(List<Element> userClasses) throws ProcessingException{
        for (Element userClass: userClasses){

            TypeElement fragmentClass = asTypeElement(getTypeMirror(userClass.getAnnotation(MVPFragmentCompat.class)));

            String mvpFragmentClassName = PREFIX + fragmentClass.getSimpleName().toString() + SUFFIX;

            PackageElement pkg = elementUtils.getPackageOf(userClass);
            String packageName = pkg.isUnnamed() ? "" : pkg.getQualifiedName().toString();

            if(isClassCreated(packageName, mvpFragmentClassName)){
                continue;
            }

            ClassName superFragmentClassName = ClassName.get(fragmentClass);
            ClassName baseFragmentCompatClassName = ClassName.get("android.support.v4.app", "Fragment");
            ClassName baseFragmentClassName = ClassName.get("android.app", "Fragment");
            ClassName mvpViewClassName = ClassName.get("com.github.nicktgn.mvp", "MvpView");
            ClassName mvpInterfaceClassName = ClassName.get("com.github.nicktgn.mvp", "IMvpFragment");
            ClassName mvpPresenterClassName = ClassName.get("com.github.nicktgn.mvp", "MvpPresenter");
            ClassName mvpFragmentHelperClassName = ClassName.get("com.github.nicktgn.mvp", "MvpFragmentCompatHelper");
            ClassName bundleClassName = ClassName.get("android.os", "Bundle");
            ParameterizedTypeName classT = ParameterizedTypeName.get(ClassName.get(Class.class));
            ClassName mvpBundleClassName = ClassName.get("com.github.nicktgn.mvp", "MvpBundle");


            // class body
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(mvpFragmentClassName)
                    //.addOriginatingElement(enclosing)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addTypeVariable(TypeVariableName.get("V", mvpViewClassName))
                    .addTypeVariable(TypeVariableName.get("P", mvpPresenterClassName))
                    .superclass(superFragmentClassName)
                    .addSuperinterface(mvpInterfaceClassName);

            // field - mvp fragment helper
            FieldSpec.Builder mvpHelperField = FieldSpec.builder(mvpFragmentHelperClassName, "mvp")
                    .addModifiers(Modifier.PRIVATE)
                    .initializer("new $T()", mvpFragmentHelperClassName);
            classBuilder.addField(mvpHelperField.build());

            // field - protected presenter
            FieldSpec.Builder presenterField = FieldSpec.builder(TypeVariableName.get("P"), "presenter")
                    .addModifiers(Modifier.PROTECTED);
            classBuilder.addField(presenterField.build());

            // method - onActivityCreated
            MethodSpec.Builder onActivityCreatedMethod = MethodSpec.methodBuilder("onActivityCreated")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(bundleClassName, "savedInstanceState")
                    .addStatement("super.onActivityCreated(savedInstanceState)")
                    .addStatement("presenter = getPresenter()")
                    .addStatement("mvp.onActivityCreated(this, savedInstanceState, presenter)");
            classBuilder.addMethod(onActivityCreatedMethod.build());

            // method - onSaveInstanceState
            MethodSpec.Builder onSaveInstanceStateMethod = MethodSpec.methodBuilder("onSaveInstanceState")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(bundleClassName, "outState")
                    .addStatement("mvp.onSaveInstanceState(outState)")
                    .addStatement("super.onSaveInstanceState(outState)");
            classBuilder.addMethod(onSaveInstanceStateMethod.build());

            // method - onDestroyView
            MethodSpec.Builder onDestroyViewMethod = MethodSpec.methodBuilder("onDestroyView")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addStatement("mvp.onDestroyView(this)")
                    .addStatement("super.onDestroyView()");
            classBuilder.addMethod(onDestroyViewMethod.build());

            // method - getPresenter
            MethodSpec.Builder getPresenterMethod = MethodSpec.methodBuilder("getPresenter")
                    .addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED)
                    .addAnnotation(Override.class)
                    .returns(TypeVariableName.get("P"));
            classBuilder.addMethod(getPresenterMethod.build());

            // method - getMvpFragmentCompat
            MethodSpec.Builder getMvpFragmentCompatMethod = MethodSpec.methodBuilder("getMvpFragmentCompat")
                    .addModifiers(Modifier.PROTECTED)
                    .addAnnotation(Override.class)
                    .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), TypeVariableName.get("T")), "targetView")
                    .addParameter(mvpBundleClassName, "arguments")
                    .returns(TypeVariableName.get("T", baseFragmentCompatClassName, mvpInterfaceClassName))
                    .addStatement("return mvp.getMvpFragmentCompatMethod()");
            classBuilder.addMethod(getMvpFragmentCompatMethod.build());

            // method - getMvpFragment
            MethodSpec.Builder getMvpFragmentMethod = MethodSpec.methodBuilder("getMvpFragmentCompat")
                    .addModifiers(Modifier.PROTECTED)
                    .addAnnotation(Override.class)
                    .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), TypeVariableName.get("T")), "targetView")
                    .addParameter(mvpBundleClassName, "arguments")
                    .returns(TypeVariableName.get("T", baseFragmentClassName, mvpInterfaceClassName))
                    .addStatement("throw new java.lang.UnsupportedOperationException()");
            classBuilder.addMethod(getMvpFragmentMethod.build());

            try {
                JavaFile.builder(packageName, classBuilder.build()).build().writeTo(filer);

                setClassCreated(packageName, mvpFragmentClassName);
            } catch (IOException e) {
                throw new ProcessingException(userClass, e.getMessage());
            }
        }
    }

    /*
    private void generateCode(Map<Element, List<Element>> dependants) throws ProcessingException {
        for (Map.Entry<Element, List<Element>> dependant : dependants.entrySet()) {
            Element enclosing = dependant.getKey();
            List<Element> fields = dependant.getValue();

            // Create a provider name
            String providerName = enclosing.getSimpleName().toString() + SUFFIX;

            // Get the package name
            PackageElement pkg = elementUtils.getPackageOf(enclosing);
            String packageName = pkg.isUnnamed() ? "" : pkg.getQualifiedName().toString();

            // Generate the class
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(providerName)
                    //.addOriginatingElement(enclosing)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            MethodSpec.Builder provideFuncBuilder = MethodSpec.methodBuilder("provide")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(ClassName.get(enclosing.asType()), "dependant");

            // Add fields to the parameters
            for (Element field : fields) {
                String fieldName = field.getSimpleName().toString();

                // Add the parameter to function
                provideFuncBuilder.addParameter(ClassName.get(field.asType()), fieldName);

                // Set the dependant field
                provideFuncBuilder.addStatement("dependant.$L = $L", fieldName, fieldName);
            }

            classBuilder.addMethod(provideFuncBuilder.build());

            try {
                JavaFile.builder(packageName, classBuilder.build()).build().writeTo(filer);
            } catch (IOException e) {
                throw new ProcessingException(enclosing, e.getMessage());
            }
        }
    }*/

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}

