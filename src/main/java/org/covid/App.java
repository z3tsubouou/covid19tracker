package org.covid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.covid.model.User;

import java.io.IOException;
import java.util.Optional;

import org.apache.log4j.Logger;

/**
 * Програм эхлүүлэх класс
 * @author Бүжинлхам
 * @author Давгацэрэн
 */
public class App extends Application {
    private static Scene scene;
    private static Stage mainStage;
    public static User user;
    public static Logger logger = Logger.getLogger(App.class);

    /**
     *програм эхэлхэд ажиллах функцийг дахин бичиж эхэлж харуулах fxml файл болон stage-ийг бэлдэж байна.
     * @param stage Метод ажиллахад үүсэх stage
     * @throws IOException алдаа гарахад throw хийх төрөл
     */
    @Override
    public void start(Stage stage) throws IOException {
        logger.debug("Program ajillasan !!");
        mainStage = stage;
        scene = new Scene(loadFXML("splash"));
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.setTitle("Covid-19 tracker");
        mainStage.show();
        mainStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    /**
     * Хуудас хооронд fxml сольж шилжихэд ашиглана
     * @param fxml солих fxml-ын нэрийг parameter-ээр авна
     * @throws IOException алдаа гарахад throw хийх төрөл
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     *fxml бэлдэх
     * @param fxml Харуулах fxml файлын нэр
     * @return
     * @throws IOException алдаа гарахад throw хийх төрөл
     */
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     *Stage дамжуулах
     * @return Stage буцаана
     */
    public static Stage returnStage() {
        return App.mainStage;
    }

    /**
     *Прогрмыг эхлүүлэх
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     *Програмаас гарахад гарахуу үгүй юу гэдгийг шалгах
     * @param event Window event сонсох
     */
    private void closeWindowEvent(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        ButtonType yes = new ButtonType("Тэгье", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Болих", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(yes);
        alert.getButtonTypes().add(no);
        alert.setTitle("Програмаас гарах");
        alert.setHeaderText(null);
        alert.setContentText("Та програмаас гарах уу?");
        alert.initOwner(mainStage);
        Optional < ButtonType > res = alert.showAndWait();
        if (res.isPresent()) {
            if (res.get().getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
                logger.info("Programmaas garahaa bolison !!");
                event.consume();
            } else {
                logger.info("Programmaas garsan !!");
                System.exit(0);
            }
        }
    }
}