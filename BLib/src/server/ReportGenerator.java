package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.management.InstanceNotFoundException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ReportGenerator {
	private static LocalDate date;
	private static byte[] data;
	private DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM");

	public byte[] generateSubscriberStatusReport(LocalDate date) {
		ReportGenerator.date = date;
		data = null;
		subscriberStatus(ServerGUI.primaryStage);
		while(data==null)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return data;
	}
	
	public byte[] generateBorrowTimeReport(LocalDate date) {
		ReportGenerator.date = date;
		data = null;
		borrowTime(ServerGUI.primaryStage);
		while(data==null)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return data;
	}

	@SuppressWarnings("unchecked")
	public void subscriberStatus(Stage primaryStage) {
		try {

			CategoryAxis xAxis = new CategoryAxis();
			NumberAxis yAxis = new NumberAxis();

			xAxis.setLabel("Date");
			yAxis.setLabel("Number of Members");

			BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
			barChart.setTitle("Frozen and Active Members Over Time");

			XYChart.Series<String, Number> frozenSeries = new XYChart.Series<>();
			frozenSeries.setName("Frozen");
			XYChart.Series<String, Number> activeSeries = new XYChart.Series<>();
			activeSeries.setName("Active");
			date = LocalDate.of(date.getYear(), date.getMonth(), 1);

			Map<LocalDate, Integer[]> map = null;
			map = BLibServer.getInstance().getSubscribersStatusOnMonth(date);

			TreeSet<LocalDate> dates = new TreeSet<>();
			dates.addAll(map.keySet());

			Integer[] lastKnownStatus = { 0, 0 }; // To track the last known status
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

			barChart.getData().addAll(activeSeries, frozenSeries);

			int sum = BLibServer.getInstance().SumNewSubscriber(LocalDate.now());

			Label label = new Label("The total new subscribers this month is :%d".formatted(sum));
			label.setFont(new Font("Arial", 24)); // Set font and size
			label.setAlignment(Pos.CENTER);

			VBox.setVgrow(barChart, Priority.ALWAYS);

			VBox vbox = new VBox(barChart, label);
			vbox.setPrefSize(800, 600);
			vbox.setAlignment(Pos.BOTTOM_CENTER); // Center the label at the bottom
			vbox.setSpacing(20);
			vbox.setPadding(new Insets(10, 20, 50, 20));
			Scene scene = new Scene(vbox, 1400, 800);

			barChart.setCategoryGap(2);
			barChart.setBarGap(5);

//			primaryStage.setScene(scene);
//			primaryStage.setFullScreen(true);

			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), e -> {
				
				WritableImage image = scene.snapshot(null);
				ByteArrayOutputStream out = new ByteArrayOutputStream();

				try {
					System.out.println(ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", out));
					data = out.toByteArray();

					System.out.println("subscriber status graph generated");
					//Platform.exit();

				} catch (IOException ex) {
					ex.printStackTrace();
				} 
			}));
			timeline.setCycleCount(1);
			timeline.play();

		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void borrowTime(Stage primaryStage) {
		try {
			CategoryAxis xAxis = new CategoryAxis();
			NumberAxis yAxis = new NumberAxis();

			xAxis.setLabel("Genre");
			yAxis.setLabel("Value");

			BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
			barChart.setTitle("Borrow Statistics per Genre");

			XYChart.Series<String, Number> borrowDuration = new XYChart.Series<>();
			borrowDuration.setName("Average Borrow Duration (Days)");
			XYChart.Series<String, Number> lateReturns = new XYChart.Series<>();
			lateReturns.setName("Late Returns (%)");
			date = LocalDate.of(date.getYear(), date.getMonth(), 1);
			Map<String,Double[]> map = BLibServer.getInstance().getBorrowTimeOnMonth(date);
			
			for(String genre : map.keySet()){
				Double[] status = map.get(genre);
				borrowDuration.getData().add(new XYChart.Data<>(genre, status[0])); 
				lateReturns.getData().add(new XYChart.Data<>(genre, status[1]));
				
			}

			barChart.getData().addAll(lateReturns, borrowDuration);
			
			

			double avg = BLibServer.getInstance().getAvgBorrowTimeOnMonth(LocalDate.now());

			Label label = new Label("The average borrow duration this month is :%.2f".formatted(avg));
			label.setFont(new Font("Arial", 24)); // Set font and size
			label.setAlignment(Pos.CENTER);

			VBox.setVgrow(barChart, Priority.ALWAYS);

			VBox vbox = new VBox(barChart, label);
			vbox.setPrefSize(800, 600);
			vbox.setAlignment(Pos.BOTTOM_CENTER); // Center the label at the bottom
			vbox.setSpacing(20);
			vbox.setPadding(new Insets(10, 20, 50, 20));
			Scene scene = new Scene(vbox, 1400, 800);

			barChart.setCategoryGap(20);
			barChart.setBarGap(5);

//			primaryStage.setScene(scene);
//			primaryStage.setFullScreen(true);

			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), e -> {
				
				WritableImage image = scene.snapshot(null);
				ByteArrayOutputStream out = new ByteArrayOutputStream();

				try {
					System.out.println(ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", out));
					data = out.toByteArray();

					System.out.println("borrow time graph generated");
					//Platform.exit();

				} catch (IOException ex) {
					ex.printStackTrace();
				} 
			}));
			timeline.setCycleCount(1);
			timeline.play();

		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
