package org.covid.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.covid.App;
import org.covid.db.ConnectionClass;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.covid.App.user;

/**
 *Review хуудсын controller
 */
public class ReviewController {
    private Stage parentStage;
    @FXML
    private TextField name;
    @FXML
    private TextField mail;
    @FXML
    private TextArea comment;

    /**
     *Review хуудсын controller үүсэхэд эхэлж хийгдэх үйлдэлүүд
     */
    @FXML
    private void initialize() {
        parentStage = App.returnStage();
        mail.setText(user.getMail());
        mail.setEditable(false);
        mail.setDisable(true);
    }

    /**
     *Хүсэлтийг илгээх функц
     *Өгөгдлийн сантай холбоо тогоож өгөгдлийг оруулсан байна уу гэж шалгаж хүсэлт илгээж байгаа
     *Хүсэлтэнд алдаа гарвал мэдэгдэл харуулах
     */
    @FXML
    private void sendHandler() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.initOwner(parentStage);

        ConnectionClass connect = new ConnectionClass();
        Connection connectDB = connect.getConnection();

        try {
            PreparedStatement pst = connectDB.prepareStatement("INSERT INTO `covidReview`\n" +
                    "            (`mail`,\n" +
                    "             `name`,\n" +
                    "             `comment`)\n" +
                    "VALUES      (?,\n" +
                    "             ?,\n" +
                    "             ?) ");
            if (!name.getText().isEmpty() && !mail.getText().isEmpty() && !comment.getText().isEmpty()) {
                pst.setString(1, mail.getText());
                pst.setString(2, name.getText());
                pst.setString(3, comment.getText());
                int status = pst.executeUpdate();
                if (status == 1) {
                    App.setRoot("home");
                } else {
                    alert.setTitle("Нэвтрэх");
                    alert.setContentText("Хүсэлт илгээхэд алдаа гарлаа!!");
                    alert.showAndWait();
                }
            } else {
                alert.setTitle("Нэвтрэх");
                alert.setContentText("Хоосон сэтгэгдэл илгээж болохгүй!!");
                alert.showAndWait();
            }

            connectDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *Хуудас home хооронд шилжих
     * @throws IOException алдаа гарахад throw хийх төрөл
     */
    @FXML
    private void globeHandler() throws IOException {
        App.setRoot("home");
    }

    /**
     *Хуудас country хооронд шилжих
     * @throws IOException алдаа гарахад throw хийх төрөл
     */
    @FXML
    private void countryHandler() throws IOException {
        App.setRoot("country");
    }

    /**
     *Хуудас login хооронд шилжих
     * @throws IOException алдаа гарахад throw хийх төрөл
     */
    @FXML
    private void logoutHandler() throws IOException {
        App.setRoot("login");
    }
}