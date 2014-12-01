
package examples.stripchart;

import com.messners.ajf.ui.chart.ChartDataProvider;
import com.messners.ajf.ui.chart.StripChart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;
import javax.swing.JFrame;

public class StripChartDemo implements ChartDataProvider {

	protected Random simulator = new Random();
	int count = 0;
	public void provideData (StripChart chart) {

		float offset;
		count++;
		if (count > 50) {

			offset = 3.0f;
			count = 0;

		} else if (count > 25) {

			offset = 10.0f;

		} else {

			offset = 3.0f;
		}

		float f = (.5f - simulator.nextFloat()) * 2.0f + offset;
		chart.addData(0, f);

		f = (.5f - simulator.nextFloat()) + 2.0f;
		chart.addData(1, f);
		
		f = (.5f - simulator.nextFloat()) + 2.0f;
		chart.addData(2, f);
	}

	public static void main (String args[]) {

		StripChartDemo scd = new StripChartDemo();
		StripChart chart = new StripChart(scd);
		chart.setChartColor(Color.black);
		chart.setFixedYTextWidth("000.0");
		chart.setAutoScale(true);
		chart.setUpdateRate(500);

		chart.addDataSet(Color.pink);
		chart.addDataSet(Color.green);
		chart.addDataSet(Color.yellow);

		JFrame frame = new JFrame();
		frame.getContentPane().add(chart, BorderLayout.CENTER);
		Dimension size = new Dimension(480, 256);
		frame.setSize(size);

		frame.setVisible(true);
		chart.start();
	}
}
