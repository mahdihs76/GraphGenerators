package charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

/**
 * Created by MahdiHS on 7/12/2018.
 */
public class AverageDistanceChart extends ApplicationFrame {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    public AverageDistanceChart(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Number Of Nodes","Average",
                dataset,
                PlotOrientation.VERTICAL,
                true,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 600 , 400) );
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setContentPane( chartPanel );
    }

    public void addData(double value, String columnKey){
        dataset.addValue(value, "Average", columnKey);
    }
}
