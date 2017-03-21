import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class TotalFitnessChart extends Application {

	static ArrayList<Double> fitness;

	@Override public void start(Stage stage) {

		//Set fitness vals in ArrayList based on the toString representation passed in through args[0]
		fitness = parse(this.getParameters().getRaw().get(0));

		stage.setTitle("Total Fitness Chart");
		//defining the axes
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Generation");
		yAxis.setLabel("Fitness");
		//creating the chart
		final LineChart<Number,Number> lineChart = 
				new LineChart<Number,Number>(xAxis,yAxis);

		lineChart.setTitle("Total Fitness vs. Generation");
		//defining a series
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("Total Fitness");
		//populating the series with data
		for(int i = 0; i < fitness.size(); i++)
			series.getData().add(new XYChart.Data<Number, Number>(i+1, fitness.get(i)));

		Scene scene  = new Scene(lineChart,800,600);
		lineChart.getData().add(series);

		//Create Tooltip
		for (XYChart.Data<Number, Number> d : series.getData())
			Tooltip.install(d.getNode(), new Tooltip("Generation: " + d.getXValue().doubleValue() + "\nFitness: " + d.getYValue().doubleValue()));

		stage.setScene(scene);
		stage.show();

	}


	// Converts a toString() of an ArrayList<Double> to an ArrayList<Double>
	public ArrayList<Double> parse(String s) {
		String listString = s.substring(1, s.length() - 1); // chop off brackets
		String[] valStrings = listString.split(",");
		ArrayList<Double> output = new ArrayList<Double>();
		for(String curr : valStrings){
			output.add(Double.parseDouble(curr));
		}
		return output;
	}


	public static void main(String[] args) {
		Population pop = new Population(SimpleAlgorithm.POP_SIZE, SimpleAlgorithm.CHROM_SIZE);

		SimpleAlgorithm.runAlgorithm(pop, 10);
		launch(args);
	}
}
