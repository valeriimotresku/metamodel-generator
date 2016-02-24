package com.amberu.metamodelgen.app;

import com.amberu.metamodelgen.entity.EntitySample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MetamodelGeneratorApp");

        initRootLayout();

        // showPersonOverview();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {

            doStuff();

            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/RootLayout.fxml"));
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean doStuff() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        String srcPath = "D:\\avery\\projects\\my\\metamodel-generator\\src\\main\\java\\com\\amberu\\metamodelgen\\entity\\EntitySample.java";
        String srcPackagePath = "D:\\avery\\projects\\my\\metamodel-generator\\src\\main\\java\\com\\amberu\\metamodelgen\\entity\\";

        boolean doIWantCompileToClassFile = true;
        JavaCompiler.CompilationTask compilationTask = null;
        if (doIWantCompileToClassFile) {
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

            try {
                fileManager.setLocation(StandardLocation.SOURCE_OUTPUT, Arrays.asList(new File(srcPackagePath)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Iterable<? extends JavaFileObject> targetCompilationUnits =
                    fileManager.getJavaFileObjects(srcPath);
            compilationTask = compiler.getTask(null, fileManager, null, null, null, targetCompilationUnits);
        } else {
            Iterable<String> targetClasses = Arrays.asList(EntitySample.class.getName());
            compilationTask = compiler.getTask(null, null, null, null, targetClasses, null);
        }
        compilationTask.setProcessors(Arrays.asList(new JPAMetaModelEntityProcessor()));
        return compilationTask.call();
    }

//    /**
//     * Shows the person overview inside the root layout.
//     */
//    public void showPersonOverview() {
//        try {
//            // Load person overview.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
//            AnchorPane personOverview = (AnchorPane) loader.load();
//
//            // Set person overview into the center of root layout.
//            rootLayout.setCenter(personOverview);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
