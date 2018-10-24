package ui;

import application.Constants;
import application.Main;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;


public class InitUi extends Application {
    private static Stage mainStage;
    private static MainController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader
                .load(getClass().getClassLoader().getResource("ui/main.fxml"));
        root.getStylesheets().add(
                getClass().getClassLoader().getResource("ui/style.css").toExternalForm());
        primaryStage.setTitle("Einfach Genial");
        Scene scene = new Scene(root);
        primaryStage.setWidth(Constants.SCREEN_WIDTH);
        primaryStage.setHeight(Constants.SCREEN_HEIGHT);
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        setListeners(scene);
        mainStage = primaryStage;
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void injectMaincontroller(MainController main) {
        InitUi.mainController = main;
    }

    private static void setListeners(Scene scene) {
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                InitUi.mainController.handleEvent(mouseEvent);
            }
        });
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                InitUi.mainController.handleEvent(mouseEvent);
            }
        });
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                InitUi.mainController.handleEvent(mouseEvent);
            }
        });
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                InitUi.mainController.handleEvent(mouseEvent);
            }
        });
        scene.addEventFilter(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                InitUi.mainController.handleEvent(mouseEvent);
            }
        });
        scene.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                InitUi.mainController.handleScrollEvent(scrollEvent);
            }
        });
    }

    protected static Stage getMainStage() {
        return mainStage;
    }

    protected static void closeProgram() {
        Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION, "");
        closeAlert.setTitle("Beenden");
        Button exitBtn = (Button) closeAlert.getDialogPane().lookupButton(ButtonType.OK);
        exitBtn.setText("Beenden");
        Button cancelBtn = (Button) closeAlert.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelBtn.setText("Abbrechen");
        closeAlert.setHeaderText("Einfach Genial beenden?");
        closeAlert.initModality(Modality.APPLICATION_MODAL);
        closeAlert.initOwner(InitUi.getMainStage());
        Optional<ButtonType> closeResponse = closeAlert.showAndWait();
        if (ButtonType.OK.equals(closeResponse.get())) {
            InitUi.getMainStage().close();
            System.exit(0);
        }
    }

}