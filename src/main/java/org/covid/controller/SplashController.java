package org.covid.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import org.covid.App;

import java.io.IOException;

/**
 * Splash fxml-ийн controller
 * Прогарм ажиллахад анх гарч ирнэ.
 */
public class SplashController {

    /**
     *Splash fxml үүсэхэд анх хийгдэх үйлдэл
     */
    @FXML
    private void initialize() {
        new SplashScreen().start();
    }

    /**
     * Thread ашиглаж хуудас хооронд шилжиж байгаа
     * Яагаад гэвэл javafx-ын ui нь нэг Thread дээр ажилладаг
     */
    static class SplashScreen extends Thread {
        /**
         *Үүсгэсэн thread дээр хийх үйлдэл
         *4 секундын дараа login хуудасруу шилжинэ
         */
        @Override
        public void run() {
            try {
                Thread.sleep(4000);
                Platform.runLater(() -> {
                    try {
                        App.setRoot("login");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}