import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

/**
 * Created by MahdiHS on 7/12/2018.
 */
public class LineChart_AWT extends ApplicationFrame {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

    LineChart_AWT(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Number Of Nodes","Average",
                dataset,
                PlotOrientation.VERTICAL,
                true,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 600 , 400) );
        setContentPane( chartPanel );
    }

    void addData(double value, String rowKey, String columnKey){
        dataset.addValue(value, rowKey, columnKey);
    }
}
