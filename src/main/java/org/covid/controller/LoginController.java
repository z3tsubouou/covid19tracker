package org.covid.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
public class LoginController extends EncryptDecrypt {
    private Stage parentStage;
    @FXML
    TextField mail;
    @FXML
    PasswordField password;

    /**
     *Login хуудсын controller үүсэхэд эхэлж хийгдэх үйлдэлүүд
     */
    @FXML
    private void initialize() {
        this.parentStage = App.returnStage();
        this.mail.requestFocus();
    }

    /**
     *Хуудас register хооронд шилжих
     * @throws IOException алдаа гарахад throw хийх төрөл
     */
    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("register");
    }

    /**
     *Хэрэглэгч нэвтрэх үеийн функц
     */
    @FXML
    private void loginHandler() {
        App.user = new User(mail.getText(), password.getText());

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.initOwner(parentStage);
        if (App.user.getMail().isEmpty()) {
            alert.setTitle("Өгөгдөл буруу");
            alert.setContentText("Хоосон майл оруулж болохгүй!!");
            alert.showAndWait();
            App.logger.info("Mail hooson oruulsan!!");
        } else if (!EmailValidator.getInstance().isValid(App.user.getMail())) {
            alert.setTitle("Өгөгдөл буруу");
            alert.setContentText("Майл оруулна уу!!");
            alert.showAndWait();
            App.logger.info("Mail buruu oruulsan !!");
        } else if (App.user.getPassword().isEmpty()) {
            alert.setTitle("Өгөгдөл буруу");
            alert.setContentText("Хоосон нууц үг оруулж болохгүй!!");
            alert.showAndWait();
            App.logger.info("Password hooson oruuslan!!");
        } else {
            ConnectionClass connect = new ConnectionClass();
            Connection connectDB = connect.getConnection();

            try {
                PreparedStatement pst = connectDB.prepareStatement("SELECT `mail`, `password` FROM `covidUser` WHERE `mail` = '" + App.user.getMail() + "'");
                ResultSet status = pst.executeQuery();

                if (status != null) {
                    while (status.next()) {
                        System.out.println("query ajilsan");
                        if (!status.getString("mail").isEmpty()) {
                            if (status.getString("mail").equals(App.user.getMail())) {

                                String secretKey = "secrete";
                                String decryptedString = decrypt(status.getString("password"), secretKey);

                                if (decryptedString.equals(App.user.getPassword())) {
                                    App.setRoot("home");
                                } else {
                                    alert.setTitle("Нэвтрэх");
                                    alert.setContentText("Майл эсвэл нууц үг буруу!!");
                                    alert.showAndWait();
                                }
                            } else {
                                alert.setTitle("Нэвтрэх");
                                alert.setContentText("Майл эсвэл нууц үг буруу!!");
                                alert.showAndWait();
                            }
                        }
                    }
                } else {
                    alert.setTitle("Нэвтрэх");
                    alert.setContentText("Хэрэглэгч олдсонгүй!!");
                    alert.showAndWait();
                }

                connectDB.close();
            } catch (Exception e) {
                e.printStackTrace();
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
            this.loginHandler();
        }
    }

}