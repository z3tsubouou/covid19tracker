package org.covid.controller;

import com.jayway.jsonpath.JsonPath;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.*;
import org.covid.App;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 *Country хуудсын controller
 */
public class CountryController {
    OkHttpClient client;
    private static final String BASE_URL = " https://corona-api.com";
    private Stage parentStage;
    private String res;
    @FXML
    public Label infected;
    @FXML
    public Label newInfected;
    @FXML
    public Label infectedDate;
    @FXML
    public Label recovered;
    @FXML
    public Label newRecovered;
    @FXML
    public Label recoveredDate;
    @FXML
    public Label deaths;
    @FXML
    public Label newDeaths;
    @FXML
    public Label deathsDate;
    @FXML
    public LineChart < String, Number > lineChart;
    @FXML
    public TextField countryCode;

    /**
     *Country хуудсын controller үүсэхэд эхэлж хийгдэх үйлдэлүүд
     * @throws InterruptedException алдаа гарахад throw хийх төрөл
     */
    @FXML
    private void initialize() throws InterruptedException {
        client = new OkHttpClient();
        this.parentStage = App.returnStage();
        sendRequestAndSetLineChartData("mn");
        lineChart.setTitle("Түүх");
    }

    /**
     *Хүсэлт илгээх функ болон мэдэгдэл хэрэглэгчид харуулах функц
     * @param code улсын ISO код
     * @throws InterruptedException алдаа гарахад throw хийх төрөл
     */
    private void sendRequestAndSetLineChartData(String code) throws InterruptedException {
        final Request request = new Request.Builder().url(BASE_URL + "/countries/" + code + "?include=timeline").build();
        final Call call = client.newCall(request);

        call.enqueue(new Callback() {
            /**
             * Хүсэлт илгээгээд өгөгдлийг хадгалах
             * @param call функцээс ирэх өгөгдөл
             * @param response  функцээс ирэх өгөгдөл
             */
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    res = responseBody.string();
                    App.logger.info("Huseltiig amjilttai unshsan !!");
                } catch (IOException e) {
                    e.printStackTrace();
                    App.logger.warn("Huseltiig awch chaagui!!");

                }
            }

            /**
             *Алдаа гарахад мэдэгдэл үзүүлэх
             * @param call функцээс ирэх өгөгдөл
             * @param e функцээс ирэх өгөгдөл
             */
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.initOwner(parentStage);
                alert.setTitle("Хүсэлт");
                alert.setContentText("Хүсэлт илгээхэд алдаа гарлаа");
                alert.showAndWait();
            }
        });

        Thread.sleep(3000);

        try {
            JsonPath.read(res, "message");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.initOwner(parentStage);
            alert.setTitle("Хүсэлт");
            alert.setContentText("Хүсэлт илгээхэд алдаа гарлаа!!");
            alert.showAndWait();
        } catch (Exception e) {
            lineChart.getData().removeAll();
            infected.setText(JsonPath.read(res, "$.data.latest_data.confirmed").toString());
            newInfected.setText(JsonPath.read(res, "$.data.timeline[0].new_confirmed").toString());
            recovered.setText(JsonPath.read(res, "$.data.latest_data.recovered").toString());
            newRecovered.setText(JsonPath.read(res, "$.data.timeline[0].new_recovered").toString());
            deaths.setText(JsonPath.read(res, "$.data.latest_data.deaths").toString());
            newDeaths.setText(JsonPath.read(res, "$.data.timeline[0].new_deaths").toString());
            String date = JsonPath.read(res, "$.data.timeline[0].date");
            infectedDate.setText(date);
            recoveredDate.setText(date);
            deathsDate.setText(date);

            List < Integer > infectedArray = JsonPath.read(res, "$.data.timeline[*].confirmed");
            List < String > infectedDateArray = JsonPath.read(res, "$.data.timeline[*].date");

            List < Integer > recoveredArray = JsonPath.read(res, "$.data.timeline[*].recovered");
            List < String > recoveredDateArray = JsonPath.read(res, "$.data.timeline[*].date");

            List < Integer > deathsArray = JsonPath.read(res, "$.data.timeline[*].deaths");
            List < String > deathsDateArray = JsonPath.read(res, "$.data.timeline[*].date");

            XYChart.Series seriesInfected = new XYChart.Series();
            seriesInfected.setName("Infected");

            XYChart.Series seriesRecovered = new XYChart.Series();
            seriesRecovered.setName("Recovered");

            XYChart.Series seriesDeaths = new XYChart.Series();
            seriesDeaths.setName("Deaths");

            for (int i = infectedArray.size() - 90; i < infectedArray.size(); i++) {
                seriesInfected.getData().add(new XYChart.Data(infectedDateArray.get(infectedArray.size() - i), infectedArray.get(infectedArray.size() - i)));
                seriesRecovered.getData().add(new XYChart.Data(recoveredDateArray.get(infectedArray.size() - i), recoveredArray.get(infectedArray.size() - i)));
                seriesDeaths.getData().add(new XYChart.Data(deathsDateArray.get(infectedArray.size() - i), deathsArray.get(infectedArray.size() - i)));
            }

            lineChart.getData().clear();
            lineChart.getData().removeAll();
            lineChart.getData().removeAll(Collections.singleton(lineChart.getData().setAll()));
            lineChart.getData().add(seriesInfected);
            lineChart.getData().add(seriesRecovered);
            lineChart.getData().add(seriesDeaths);
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
     *Хуудас review хооронд шилжих
     * @throws IOException алдаа гарахад throw хийх төрөл
     */
    @FXML
    private void reviewHandler() throws IOException {
        App.setRoot("review");
    }

    /**
     *Хуудас login хооронд шилжих
     * @throws IOException алдаа гарахад throw хийх төрөл
     */
    @FXML
    private void logoutHandler() throws IOException {
        App.setRoot("login");
    }

    /**
     *Өгөгдсөн утгыг хоосон байна уу гэж шалгах
     * @throws InterruptedException алдаа гарахад throw хийх төрөл
     */
    @FXML
    private void searchHandler() throws InterruptedException {
        if (countryCode.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.initOwner(parentStage);
            alert.setTitle("Өгөгдөл буруу");
            alert.setContentText("Хоосон код оруулж болохгүй!!");
            alert.showAndWait();
        } else {
            sendRequestAndSetLineChartData(countryCode.getText());
        }
    }
}