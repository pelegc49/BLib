package server;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class ReportGenerator extends Application {
	private static LocalDate date;
	private DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM");
	
	
	public void GenerateReport(LocalDate date) {
		ReportGenerator.date = date;
		launch();
	}
	
	
	
	@Override
	@SuppressWarnings("unchecked")
	
	public void start(Stage primaryStage) throws Exception {

		
		
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		yAxis.setLabel("Number of Members");
		BarChart<String, Number> stackedBarChart = new BarChart<>(xAxis, yAxis);
		stackedBarChart.setTitle("Frozen and Active Members Over Time");

		XYChart.Series<String, Number> frozenSeries = new XYChart.Series<>();
		frozenSeries.setName("Frozen");
		XYChart.Series<String, Number> activeSeries = new XYChart.Series<>();
		activeSeries.setName("Active");
		date = LocalDate.of(date.getYear(), date.getMonth(), 1);
		
		Map<LocalDate, Integer[]> map = BLibServer.getInstance().getSubscribersStatusOnMonth(date);
		
		TreeSet<LocalDate> dates = new TreeSet<>();
		dates.addAll(map.keySet());
		
		Integer[] lastKnownStatus = {0,0}; // To track the last known status
		Month curMonth = date.getMonth();
		while (date.getMonth().equals(curMonth)) {
		    LocalDate min = dates.isEmpty() ? null : dates.first(); // Get the earliest date from the TreeSet
		    if (min != null && min.compareTo(date) <= 0) {
		        // Found data for this date
		        Integer[] status = map.get(min); // Get the status for the current date

		        if (status != null) {
		            // Update last known status
		            lastKnownStatus = status;

		            // Add the new data to the chart
		            frozenSeries.getData().add(new XYChart.Data<>(date.format(f), status[1])); // Frozen members
		            activeSeries.getData().add(new XYChart.Data<>(date.format(f), status[0])); // Active members
		        }

		        // Remove the processed date from the set
		        dates.remove(min);
		    } else {
		        // If no data for this date, use the last known status
		       
	            frozenSeries.getData().add(new XYChart.Data<>(date.format(f), lastKnownStatus[1]));
	            activeSeries.getData().add(new XYChart.Data<>(date.format(f), lastKnownStatus[0]));
		       
		    }
		    
		    // Move to the next day
		    date = date.plusDays(1);
		}

		stackedBarChart.getData().addAll(activeSeries, frozenSeries);
		VBox vbox = new VBox(stackedBarChart);
		vbox.setPrefSize(800, 600);
		VBox.setVgrow(stackedBarChart, Priority.ALWAYS);
		Scene scene = new Scene(vbox, 800, 600);
		
        scene.getStylesheets().add(getClass().getResource("../gui/server/Server.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((E) -> System.exit(0));
        primaryStage.setFullScreen(true);
        primaryStage.show();
        primaryStage.setOnShown(event -> {
            Platform.runLater(() -> {
                WritableImage image = vbox.snapshot(null, null);

                File file = new File("chart.png");

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                    System.out.println("Chart saved as image: " + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
//        Platform.runLater(()->{
//        	vbox.applyCss();
//            vbox.layout();
//            WritableImage image = vbox.snapshot(null, null);
//            File file = new File("chart.png");
//            try {
//            	ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//            	System.out.println("Chart saved as image: " + file.getAbsolutePath());
//            } catch (IOException e) {
//            	// TODO Auto-generated catch block
//            	e.printStackTrace();
//            }
////        	Thread capturer = new Thread(()->{
////        		try {
////					Thread.sleep(500);
////				} catch (Exception e) {
////					// TODO: handle exception
////				}
////        	});
////        	capturer.start();
//        });
	}
	
}
