package GUI;

import Solver.Solver;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(makeScene(primaryStage));
        primaryStage.show();
    }

    private Scene makeScene(Stage primaryStage) {
        TextField nElements = new javafx.scene.control.TextField("15");
        Button button = new Button("draw graph");
        button.setOnAction(event -> primaryStage.setScene(makeGraphScene(Integer.parseInt(nElements.getText()))));
        VBox vBox = new VBox(nElements, button);
        vBox.setAlignment(Pos.CENTER);
        return new Scene(vBox);
    }

    private Scene makeGraphScene(int n) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        Solver solver = new Solver(n);
        double[] result = solver.solve();
        for (double i = 0; i <= 2; i += 0.01) {
            double value = 0;
            for (int j = 0; j < n; j++) {
                value += result[j] * solver.e(j, i);
            }
            series.getData().add(new XYChart.Data<>(i, value));
        }
        return new Scene(lineChart);
    }
}
