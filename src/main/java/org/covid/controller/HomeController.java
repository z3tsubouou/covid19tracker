package org.covid.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import okhttp3.*;
import org.covid.App;
import org.jetbrains.annotations.NotNull;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.util.List;

/**
 *Home хуудсын controller
 */
public class HomeController {
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

    /**
     *Home хуудсын controller үүсэхэд эхэлж хийгдэх үйлдэлүүд
     * @throws InterruptedException алдаа гарахад throw хийх төрөл
     */
    @FXML
    private void initialize() throws InterruptedException {
        client = new OkHttpClient();
        this.parentStage = App.returnStage();
        sendRequest();
        lineChart.setTitle("Түүх");
    }

    /**
     *Хүсэлт илгээх функ болон мэдэгдэл хэрэглэгчид харуулах функц
     * @throws InterruptedException алдаа гарахад throw хийх төрөл
     */
    private void sendRequest() throws InterruptedException {
        final Request request = new Request.Builder().url(BASE_URL + "/timeline").build();
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
                } catch (IOException e) {
                    e.printStackTrace();
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
            infected.setText(JsonPath.read(res, "$.data[0].confirmed").toString());
            newInfected.setText(JsonPath.read(res, "$.data[0].new_confirmed").toString());
            recovered.setText(JsonPath.read(res, "$.data[0].recovered").toString());
            newRecovered.setText(JsonPath.read(res, "$.data[0].new_recovered").toString());
            deaths.setText(JsonPath.read(res, "$.data[0].deaths").toString());
            newDeaths.setText(JsonPath.read(res, "$.data[0].new_deaths").toString());
            String date = JsonPath.read(res, "$.data[0].date");
            infectedDate.setText(date);
            recoveredDate.setText(date);
            deathsDate.setText(date);

            List < Integer > infectedArray = JsonPath.read(res, "$.data[*].confirmed");
            List < String > infectedDateArray = JsonPath.read(res, "$.data[*].date");

            List < Integer > recoveredArray = JsonPath.read(res, "$.data[*].recovered");
            List < String > recoveredDateArray = JsonPath.read(res, "$.data[*].date");

            List < Integer > deathsArray = JsonPath.read(res, "$.data[*].deaths");
            List < String > deathsDateArray = JsonPath.read(res, "$.data[*].date");

            XYChart.Series series = new XYChart.Series();
            series.setName("Infected");

            XYChart.Series series1 = new XYChart.Series();
            series1.setName("Recovered");

            XYChart.Series series2 = new XYChart.Series();
            series2.setName("Deaths");

            for (int i = infectedArray.size() - 120; i < infectedArray.size(); i++) {
                series.getData().add(new XYChart.Data(infectedDateArray.get(infectedArray.size() - i), infectedArray.get(infectedArray.size() - i)));
                series1.getData().add(new XYChart.Data(recoveredDateArray.get(infectedArray.size() - i), recoveredArray.get(infectedArray.size() - i)));
                series2.getData().add(new XYChart.Data(deathsDateArray.get(infectedArray.size() - i), deathsArray.get(infectedArray.size() - i)));
            }

            lineChart.getData().add(series);
            lineChart.getData().add(series1);
            lineChart.getData().add(series2);
        }

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

}