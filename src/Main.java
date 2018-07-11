import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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

import java.util.Arrays;

/**
 * Created by MahdiHS on 7/11/2018.
 */
public class Main extends Application {

    private Label label;
    private Label label1;
    private Label label2;
    private Generator barabasiGenerator;
    Generator wattsGenerator;
    private Graph barabasiGraph;
    private Graph wattsGraph;
    private LineChart_AWT chart;
    private VBox aVBox1;
    private VBox aVBox2;
    VBox aVBox3;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Graph Models");
        primaryStage.setWidth(400);
        primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight());
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setX(0);
        primaryStage.setY(0);

        VBox barabasiGroup = initBarabasiGroup();
        VBox wattsGroup = initWattsGroup();

        VBox mainGroup = new VBox(barabasiGroup, wattsGroup);
        mainGroup.setStyle("-fx-background-color: black");

        Scene scene = new Scene(mainGroup);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox initWattsGroup() {
        VBox vBox = new VBox();
        vBox.setMinHeight(300);
        Label label = new Label("Watts-Strogatz Graph Model");
        label.setFont(new Font(24));
        label.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPadding(new Insets(20));
        label.setStyle("-fx-background-color: lawngreen; -fx-cursor: hand");

        vBox.getChildren().add(label);

        Label des = new Label("Enter the number of links per step (m):");
        des.setStyle(
                "-fx-text-fill: white;" +
                        " -fx-font-size: 16");
        des.setPadding(new Insets(10));
        HBox hBox = new HBox();
        Button button = new Button("Draw Graph");
        button.setMinWidth(180);
        button.setMinHeight(50);
        button.setStyle(
                "-fx-background-color: white;" +
                        " -fx-font-size: 20;" +
                        " -fx-border-color: white;" +
                        " -fx-font-style: bold");
        button.setPadding(new Insets(10));

        TextField textField = new TextField();
        textField.setPromptText("m");
        textField.setStyle(
                "-fx-background-color: black;" +
                        " -fx-text-inner-color: white;" +
                        " -fx-border-color: white");
        textField.setFont(new Font(24));
        hBox.getChildren().addAll(textField, button);
        VBox vBox1 = new VBox(des, hBox);
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

        this.label = new Label("Number Of Nodes:");
        label1 = new Label("Degree Distribution:");
        label2 = new Label("Average Distance Between Nodes:");
        this.label.setPadding(new Insets(0,0,10,20));
        label1.setPadding(new Insets(0,0,10,20));
        label2.setPadding(new Insets(0,0,20,20));
        this.label.setStyle(
                "-fx-text-fill: white;" +
                        " -fx-font-size: 16");
        label1.setStyle(
                "-fx-text-fill: white;" +
                        " -fx-font-size: 16");
        label2.setStyle(
                "-fx-text-fill: white;" +
                        " -fx-font-size: 16");

        button.setOnMouseClicked(event -> {
            addButton.setDisable(false);
            drawWattsGraph();
//            setBarabasiDataGraph();
        });

        addButton.setOnMouseClicked(event -> {
            nextStep();
//            setBarabasiDataGraph();
        });

        aVBox2 = new VBox(vBox1, this.label, label1, label2, addButton);
        aVBox2.setPadding(new Insets(0,0,20,0));
        aVBox2.setVisible(false);
        aVBox2.setManaged(false);

        label.setOnMouseClicked(event -> {
            aVBox2.setVisible(true);
            aVBox2.setManaged(true);
            aVBox1.setVisible(false);
            aVBox1.setManaged(false);
        });

        vBox.getChildren().addAll(aVBox2);
        return vBox;
    }

    private VBox initBarabasiGroup() {
        VBox vBox = new VBox();
//        vBox.setMinHeight(300);
        Label barabasiLabel = new Label("Barabasi Albert Graph Model");
        barabasiLabel.setFont(new Font(24));
        barabasiLabel.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(barabasiLabel, 0.0);
        AnchorPane.setRightAnchor(barabasiLabel, 0.0);
        barabasiLabel.setAlignment(Pos.CENTER);
        barabasiLabel.setTextAlignment(TextAlignment.CENTER);
        barabasiLabel.setPadding(new Insets(20));
        barabasiLabel.setStyle("-fx-background-color: lawngreen; -fx-cursor: hand");
        vBox.getChildren().add(barabasiLabel);

        Label barabasiDes = new Label("Enter the number of links per step (m):");
        barabasiDes.setStyle(
                "-fx-text-fill: white;" +
                        " -fx-font-size: 16");
        barabasiDes.setPadding(new Insets(10));
        HBox hBox = new HBox();
        Button button = new Button("Draw Graph");
        button.setMinWidth(180);
        button.setMinHeight(50);
        button.setStyle(
                "-fx-background-color: white;" +
                        " -fx-font-size: 20;" +
                        " -fx-border-color: white;" +
                        " -fx-font-style: bold");
        button.setPadding(new Insets(10));

        TextField textField = new TextField();
        textField.setPromptText("m");
        textField.setStyle(
                "-fx-background-color: black;" +
                        " -fx-text-inner-color: white;" +
                        " -fx-border-color: white");
        textField.setFont(new Font(24));
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

        label = new Label("Number Of Nodes:");
        label1 = new Label("Degree Distribution:");
        label2 = new Label("Average Distance Between Nodes:");
        label.setPadding(new Insets(0,0,10,20));
        label1.setPadding(new Insets(0,0,10,20));
        label2.setPadding(new Insets(0,0,20,20));
        label.setStyle(
                "-fx-text-fill: white;" +
                        " -fx-font-size: 16");
        label1.setStyle(
                "-fx-text-fill: white;" +
                        " -fx-font-size: 16");
        label2.setStyle(
                "-fx-text-fill: white;" +
                        " -fx-font-size: 16");

        button.setOnMouseClicked(event -> {
            addButton.setDisable(false);
            String text = textField.getText();
            drawGraph(Integer.valueOf(text));
            setBarabasiDataGraph();
        });

        addButton.setOnMouseClicked(event -> {
            nextStep();
            setBarabasiDataGraph();
        });

        aVBox1 = new VBox(vBox1, label, label1, label2, addButton);
        aVBox1.setPadding(new Insets(0,0,20,0));
        aVBox1.setVisible(false);
        aVBox1.setManaged(false);
        barabasiLabel.setOnMouseClicked(event -> {
            aVBox1.setVisible(true);
            aVBox1.setManaged(true);
            aVBox2.setVisible(false);
            aVBox2.setManaged(false);
        });
        vBox.getChildren().addAll(aVBox1);
        return vBox;
    }

    private void setBarabasiDataGraph(){
        int nodeCount = barabasiGraph.getNodeCount();
        label.setText("Number Of Nodes: "+nodeCount);
        label1.setText("Degree Distribution: "+ Arrays.toString(Toolkit.degreeDistribution(barabasiGraph)));
        APSP apsp = new APSP();
        apsp.init(barabasiGraph); // registering apsp as a sink for the barabasiGraph
        apsp.setDirected(false); // undirected barabasiGraph
        apsp.setWeightAttributeName("weight"); // ensure that the attribute name used is "weight"

        apsp.compute(); // the method that actually computes shortest paths

        int sum = 0;
        int count = 0;
        for (int i = 0; i < barabasiGraph.getNodeCount(); i++) {
            for (int j = i + 1; j < barabasiGraph.getNodeCount(); j++) {
                APSP.APSPInfo info = barabasiGraph.getNode(i).getAttribute(APSP.APSPInfo.ATTRIBUTE_NAME);
                Path shortestPath = info.getShortestPathTo(barabasiGraph.getNode(j).toString());
                sum += shortestPath.getEdgeCount();
                System.out.println(shortestPath.getEdgeCount() + " + ");
                count++;
            }
        }
        String avg = String.format("%.2f", (float) sum / count);
        label2.setText("Average Distance Between Nodes: " + avg);
        chart.addData(Double.parseDouble(avg), "Average", String.valueOf(barabasiGraph.getNodeCount()));
    }
    private void nextStep() {
        barabasiGenerator.nextEvents();
    }

    private void drawGraph(int links){
        barabasiGraph = new SingleGraph("Barabàsi-Albert");
        barabasiGenerator = new BarabasiAlbertGenerator(links);

        barabasiGenerator.addSink(barabasiGraph);
        barabasiGenerator.begin();
        Viewer display = barabasiGraph.display();
        display.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
        chart = new LineChart_AWT(
                "Barabasi-Albert Model" ,
                "Average Distance Between Nodes");

        chart.pack();
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true);
    }

    private void drawWattsGraph(){
        wattsGraph = new SingleGraph("Watts-Strogatz");
        wattsGenerator = new WattsStrogatzGenerator(200, 4, 0.9999);
        wattsGenerator.addSink(wattsGraph);
        wattsGenerator.begin();
        while(wattsGenerator.nextEvents()) {}
        wattsGenerator.end();
        wattsGraph.display();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
