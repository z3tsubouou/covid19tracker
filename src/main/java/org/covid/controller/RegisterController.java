package org.covid.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;
import org.covid.App;
import org.covid.db.ConnectionClass;
import org.covid.model.User;

/**
 *Register хуудсын controller
 */
public class RegisterController extends EncryptDecrypt {
    private Stage parentStage;
    @FXML
    TextField mail;
    @FXML
    PasswordField password;
    @FXML
    PasswordField confirmPassword;

    /**
     *Register хуудсын controller үүсэхэд эхэлж хийгдэх үйлдэлүүд
     */
    @FXML
    private void initialize() {
        this.parentStage = App.returnStage();
    }

    /**
     *Хуудас login хооронд шилжих
     * @throws IOException алдаа гарахад throw хийх төрөл
     */
    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }

    /**
     *Хэрэглэгч бүртгүүлэх үеийн функц
     */
    @FXML
    private void registerHandler() {
        User user = new User(mail.getText(), password.getText());
        String confirmPasswordText = confirmPassword.getText();

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.initOwner(parentStage);

        if (user.getMail().isEmpty()) {
            alert.setTitle("Өгөгдөл буруу");
            alert.setContentText("Хоосон майл оруулж болохгүй!!");
            alert.showAndWait();
        } else if (!EmailValidator.getInstance().isValid(user.getMail())) {
            alert.setTitle("Өгөгдөл буруу");
            alert.setContentText("Майл оруулна уу!!");
            alert.showAndWait();
        } else if (user.getPassword().isEmpty()) {
            alert.setTitle("Өгөгдөл буруу");
            alert.setContentText("оосон нууц үг оруулж болохгүй!!");
            alert.showAndWait();
        } else if (!user.getPassword().equals(confirmPasswordText)) {
            alert.setTitle("Өгөгдөл буруу");
            alert.setContentText("Нууц үгээ дахиж зөв оруулна уу!!");
            alert.showAndWait();
        } else {
            ConnectionClass connect = new ConnectionClass();
            Connection connectDB = connect.getConnection();

            String secretKey = "secrete";
            String encryptedString = encrypt(user.getPassword(), secretKey);

            try {
                PreparedStatement pst = connectDB.prepareStatement("INSERT INTO `covidUser`\n" +
                        "            (`mail`,\n" +
                        "             `password`)\n" +
                        "VALUES      (?,\n" +
                        "             ?) ");
                pst.setString(1, user.getMail());
                pst.setString(2, encryptedString);
                int status = pst.executeUpdate();
                if (status == 1) {
                    System.out.println("success");
                    App.setRoot("login");
                } else {
                    System.out.println("false");
                    alert.setTitle("Бүртгүүлэх");
                    alert.setContentText("Майл эсвэл нууц үг буруу!!");
                    alert.showAndWait();
                }
                connectDB.close();
            } catch (Exception e) {
                alert.setTitle("Бүртгүүлэх");
                alert.setContentText("Хэрэглэгч аль хэдийн бүртгэлтэй байна!!");
                alert.showAndWait();
            }

        }

    }

    /**
     *Enter дахарад registerHandler дуудагдана
     * @param event Keyevent ямар үсэг дарж байгааг мэднэ
     */
    @FXML
    private void keyHandler(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            this.registerHandler();
        }
    }

}