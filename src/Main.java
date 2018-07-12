import generators.MediationDrivenAttachmentGenerator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.graphstream.algorithm.APSP;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.WattsStrogatzGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.jfree.ui.RefineryUtilities;

/**
 * Created by MahdiHS on 7/11/2018.
 */
public class Main extends Application {

    private Generator barabasiGenerator;
    private Generator wattsGenerator;
    private Generator drivenGenerator;
    private Graph barabasiGraph;
    private Graph wattsGraph;
    private Graph drivenGraph;
    private LineChart_AWT barabasiAverageChart;
    private LineChart_AWT wattsAverageChart;
    private LineChart_AWT drivenAverageChart;
    private BarChart_AWT barabasiDegreeChart;
    private BarChart_AWT wattsDegreeChart;
    private BarChart_AWT drivenDegreeChart;
    private VBox barabasiDataVBox;
    private VBox wattsDataVBox;
    private VBox drivenDataVBox;

    @Override
    public void start(Stage primaryStage) throws Exception {
        UiUtils.setupStage(primaryStage);

        VBox barabasiGroup = initBarabasiGroup();
        VBox wattsGroup = initWattsGroup();
        VBox drivenGroup = initMediationGroup();

        VBox mainGroup = new VBox(barabasiGroup, wattsGroup, drivenGroup);
        mainGroup.setStyle("-fx-background-color: black");

        Scene scene = new Scene(mainGroup);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private VBox initWattsGroup() {
        VBox vBox = new VBox(UiUtils.initHeader(GraphModel.WATTS_STROGATZ, event -> {
            wattsDataVBox.setVisible(true);
            wattsDataVBox.setManaged(true);
            barabasiDataVBox.setVisible(false);
            barabasiDataVBox.setManaged(false);
            drivenDataVBox.setVisible(false);
            drivenDataVBox.setManaged(false);
        }));

        Label description = UiUtils.initDescription("Enter The number of nodes (n),\n" +
                " Base degree of each node(k),\n" +
                " Probability to \"rewire\" an edge (beta)\n" +
                " to generate:");

        TextField nodesNumberTextField = UiUtils.initInputText("n");
        TextField baseDegreeTextField = UiUtils.initInputText("k");
        TextField betaTextField = UiUtils.initInputText("beta");

        HBox hBox = new HBox();
        Button button = UiUtils.initButton("Draw Graph");

        hBox.getChildren().addAll(betaTextField, button);
        VBox vBox1 = new VBox(description, nodesNumberTextField, baseDegreeTextField, hBox);
        vBox1.setPadding(new Insets(20));

        button.setOnMouseClicked(event -> {
            drawWattsGraph(Integer.valueOf(nodesNumberTextField.getText()),
                    Integer.parseInt(baseDegreeTextField.getText()),
                    Double.parseDouble(betaTextField.getText()));
            setDataToWattsCharts();
        });

        wattsDataVBox = UiUtils.initDataVBox(vBox1, null);

        vBox.getChildren().addAll(wattsDataVBox);
        return vBox;
    }

    private VBox initBarabasiGroup() {
        VBox vBox = new VBox(UiUtils.initHeader(GraphModel.BARABASI_ALBERT, event -> {
            barabasiDataVBox.setVisible(true);
            barabasiDataVBox.setManaged(true);
            wattsDataVBox.setVisible(false);
            wattsDataVBox.setManaged(false);
            drivenDataVBox.setVisible(false);
            drivenDataVBox.setManaged(false);
        }));

        Label barabasiDes = UiUtils.initDescription("Enter the number of links per step (m):");

        HBox hBox = new HBox();
        Button button = UiUtils.initButton("Draw Graph");

        TextField textField = UiUtils.initInputText("m");
        hBox.getChildren().addAll(textField, button);
        VBox vBox1 = new VBox(barabasiDes, hBox);
        vBox1.setPadding(new Insets(20));

        Button addButton = new Button("Next Step");
        addButton.setTranslateX(20);
        addButton.setMinWidth(360);
        addButton.setMinHeight(50);
        addButton.setStyle(
                "-fx-background-color: white;" +
                        " -fx-font-size: 20;" +
                        " -fx-text-inner-color: black;" +
                        " -fx-border-color: white;" +
                        " -fx-font-style: bold;");

        addButton.setDisable(true);

        button.setOnMouseClicked(event -> {
            addButton.setDisable(false);
            String text = textField.getText();
            drawBarabasiGraph(Integer.valueOf(text));
            setDataToBarabasiCharts();
        });

        addButton.setOnMouseClicked(event -> {
            nextBarabasiGraphStep();
            setDataToBarabasiCharts();
        });

        barabasiDataVBox = UiUtils.initDataVBox(vBox1, addButton);

        vBox.getChildren().addAll(barabasiDataVBox);
        return vBox;
    }

    private VBox initMediationGroup() {
        VBox vBox = new VBox(UiUtils.initHeader(GraphModel.MEDIATION_DRIVEN, event -> {
            drivenDataVBox.setVisible(true);
            drivenDataVBox.setManaged(true);
            barabasiDataVBox.setVisible(false);
            barabasiDataVBox.setManaged(false);
            wattsDataVBox.setVisible(false);
            wattsDataVBox.setManaged(false);
        }));

        Label drivenDes = UiUtils.initDescription("Enter the number of links per step (m):");

        HBox hBox = new HBox();
        Button button = UiUtils.initButton("Draw Graph");

        TextField textField = UiUtils.initInputText("m");
        hBox.getChildren().addAll(textField, button);
        VBox vBox1 = new VBox(drivenDes, hBox);
        vBox1.setPadding(new Insets(20));

        Button addButton = new Button("Next Step");
        addButton.setTranslateX(20);
        addButton.setMinWidth(360);
        addButton.setMinHeight(50);
        addButton.setStyle(
                "-fx-background-color: white;" +
                        " -fx-font-size: 20;" +
                        " -fx-text-inner-color: black;" +
                        " -fx-border-color: white;" +
                        " -fx-font-style: bold;");

        addButton.setDisable(true);

        button.setOnMouseClicked(event -> {
            addButton.setDisable(false);
            String text = textField.getText();
            drawDrivenGraph(Integer.valueOf(text));
            setDataToDrivenCharts();
        });

        addButton.setOnMouseClicked(event -> {
            nextDrivenGraphStep();
            setDataToDrivenCharts();
        });

        drivenDataVBox = UiUtils.initDataVBox(vBox1, addButton);

        vBox.getChildren().addAll(drivenDataVBox);
        return vBox;
    }

    private void setDataToBarabasiCharts() {
        setDataToCharts(barabasiGraph, barabasiAverageChart, barabasiDegreeChart);
    }

    private void setDataToWattsCharts() {
        setDataToCharts(wattsGraph, wattsAverageChart, wattsDegreeChart);
    }

    private void setDataToDrivenCharts() {
        setDataToCharts(drivenGraph, drivenAverageChart, drivenDegreeChart);
    }

    private void setDataToCharts(Graph graph, LineChart_AWT avgChart, BarChart_AWT degreeChart) {
        int nodeCount = graph.getNodeCount();
//        numOfNodes.setText("Number Of Nodes: " + nodeCount);
        APSP apsp = new APSP();
        apsp.init(graph); // registering apsp as a sink for the barabasiGraph
        apsp.setDirected(false); // undirected barabasiGraph
        apsp.setWeightAttributeName("weight"); // ensure that the attribute name used is "weight"

        apsp.compute(); // the method that actually computes shortest paths

        int sum = 0;
        int count = 0;
        for (int i = 0; i < nodeCount; i++) {
            for (int j = i + 1; j < nodeCount; j++) {
                APSP.APSPInfo info = graph.getNode(i).getAttribute(APSP.APSPInfo.ATTRIBUTE_NAME);
                Path shortestPath = info.getShortestPathTo(graph.getNode(j).toString());
                sum += shortestPath.getEdgeCount();
                count++;
            }
        }
        String avg = String.format("%.2f", (float) sum / count);
//        averageDistance.setText("Average Distance Between Nodes: " + avg);
        avgChart.addData(Double.parseDouble(avg), "Average", String.valueOf(nodeCount));
        degreeChart.setData(Toolkit.degreeDistribution(graph));
    }

    private void nextBarabasiGraphStep() {
        barabasiGenerator.nextEvents();
    }

    private void nextDrivenGraphStep() {
        drivenGenerator.nextEvents();
    }

    private void drawBarabasiGraph(int m) {
        barabasiGraph = new SingleGraph("Barabàsi-Albert");
        barabasiGenerator = new BarabasiAlbertGenerator(m);

        barabasiGenerator.addSink(barabasiGraph);
        barabasiGenerator.begin();
        Viewer display = barabasiGraph.display();
        display.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
        drawAverageChart(GraphModel.BARABASI_ALBERT);
        drawDegreeChart(GraphModel.BARABASI_ALBERT);
    }

    private void drawDrivenGraph(int m) {
        drivenGraph = new SingleGraph("Mediation-Driven Attachment");
        drivenGenerator = new MediationDrivenAttachmentGenerator(m);

        drivenGenerator.addSink(drivenGraph);
        drivenGenerator.begin();
        Viewer display = drivenGraph.display();
        display.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
        drawAverageChart(GraphModel.MEDIATION_DRIVEN);
        drawDegreeChart(GraphModel.MEDIATION_DRIVEN);
    }

    private void drawWattsGraph(int n, int k, double beta) {
        wattsGraph = new SingleGraph("Watts-Strogatz");
        try {
            wattsGenerator = new WattsStrogatzGenerator(n, k, beta);
        } catch (RuntimeException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }

        wattsGenerator.addSink(wattsGraph);
        wattsGenerator.begin();
        while (wattsGenerator.nextEvents()) {
        }
        wattsGenerator.end();
        Viewer display = wattsGraph.display();
        display.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
        drawAverageChart(GraphModel.WATTS_STROGATZ);
        drawDegreeChart(GraphModel.WATTS_STROGATZ);
        setDataToWattsCharts();
    }

    private void drawAverageChart(GraphModel model) {
        LineChart_AWT avgChart;
        if (model == GraphModel.BARABASI_ALBERT) {
            barabasiAverageChart = new LineChart_AWT(model.getText(), "Average Distance Between Nodes");
            avgChart = barabasiAverageChart;
        } else if (model == GraphModel.WATTS_STROGATZ){
            wattsAverageChart = new LineChart_AWT(model.getText(), "Average Distance Between Nodes");
            avgChart = wattsAverageChart;
        } else {
            drivenAverageChart = new LineChart_AWT(model.getText(), "Average Distance Between Nodes");
            avgChart = drivenAverageChart;
        }
        avgChart.pack();
        RefineryUtilities.centerFrameOnScreen(avgChart);
        avgChart.setVisible(true);
    }

    private void drawDegreeChart(GraphModel model) {
        BarChart_AWT barChart;
        if (model == GraphModel.BARABASI_ALBERT) {
            barabasiDegreeChart = new BarChart_AWT(model.getText(), "Degree Distribution");
            barChart = barabasiDegreeChart;
        } else if (model == GraphModel.WATTS_STROGATZ){
            wattsDegreeChart = new BarChart_AWT(model.getText(), "Degree Distribution");
            barChart = wattsDegreeChart;
        } else {
            drivenDegreeChart = new BarChart_AWT(model.getText(), "Degree Distribution");
            barChart = drivenDegreeChart;
        }
        barChart.pack();
        RefineryUtilities.centerFrameOnScreen(barChart);
        barChart.setVisible(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
