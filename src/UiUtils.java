import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by MahdiHS on 7/12/2018.
 */
public class UiUtils {
    static Button initButton(String text) {
        Button button = new Button(text);
        button.setMinWidth(180);
        button.setMinHeight(50);
        button.setStyle(
                "-fx-background-color: white;" +
                        " -fx-font-size: 20;" +
                        " -fx-border-color: white;" +
                        " -fx-font-style: bold");
        button.setPadding(new Insets(10));
        return button;
    }

    static TextField initInputText(String hint) {
        TextField textField = new TextField();
        textField.setPromptText(hint);
        textField.setStyle(
                "-fx-background-color: black;" +
                        " -fx-text-inner-color: white;" +
                        " -fx-border-color: white");
        textField.setFont(new Font(24));
        return textField;
    }


    static VBox initDataVBox(VBox vBox1, Button addButton) {
        VBox vBox = new VBox();
        vBox.getChildren().addAll(vBox1);
        if (addButton != null) {
            vBox.getChildren().addAll(addButton);
        }
        vBox.setStyle("-fx-background-color: black");
        vBox.setPadding(new Insets(0, 0, 20, 0));
        vBox.setVisible(false);
        vBox.setManaged(false);
        return vBox;
    }


    static Label initHeader(GraphModel model, EventHandler<MouseEvent> event) {
        Label label = new Label(model.getText());
        label.setFont(new Font(24));
        label.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPadding(new Insets(20));
        label.setStyle("-fx-background-color: lawngreen; -fx-cursor: hand");
        label.setOnMouseClicked(event);
        return label;
    }

    static Label initDescription(String text) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-text-fill: white;" +
                        " -fx-font-size: 16");
        label.setPadding(new Insets(10));
        return label;
    }

    static void setupStage(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Graph Models");
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setX(0);
        primaryStage.setY(0);
    }
}
