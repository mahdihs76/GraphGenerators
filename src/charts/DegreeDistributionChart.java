package charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class DegreeDistributionChart extends ApplicationFrame {

    DefaultCategoryDataset dataset =
            new DefaultCategoryDataset();

    public DegreeDistributionChart(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Degree",
                "Number Of Nodes",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setContentPane(chartPanel);
    }

    void addData(double value, String columnKey) {
        dataset.addValue(value, "degree", columnKey);
    }

    public static void main(String[] args) {
        DegreeDistributionChart chart = new DegreeDistributionChart("Car Usage Statistics",
                "Which car do you like?");
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }

    public void setData(int[] ints) {
        for (int i = 0; i < ints.length; i++) {
            addData(ints[i], String.valueOf(i));
        }
    }
}