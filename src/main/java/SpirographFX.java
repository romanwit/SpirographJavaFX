import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.Taskbar.Feature;

public class SpirographFX extends Application {

    private Canvas canvas;
    private final double canvasSize = 800;

    @Override
    public void start(Stage primaryStage) {

        Image icon = new Image(getClass().getResourceAsStream("/bulb32.png"));
        primaryStage.getIcons().add(icon);

           if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            try {
                if (Taskbar.isTaskbarSupported()) {
                var taskbar = Taskbar.getTaskbar();

                    if (taskbar.isSupported(Feature.ICON_IMAGE)) {
                        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                        var dockIcon = defaultToolkit.getImage(getClass().getResource("/bulb128.png"));
                        taskbar.setIconImage(dockIcon);
                        
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        primaryStage.setTitle("Spirograph in JavaFX");

        canvas = new Canvas(canvasSize, canvasSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Spinner<Double> spinnerR = createSpinner(50, 300, 100, 1); 
        Spinner<Double> spinnerr = createSpinner(10, 100, 30, 1);   
        Spinner<Double> spinnerO = createSpinner(10, 150, 50, 1);   

        Button drawButton = new Button("Draw Spirograph");
        drawButton.setOnAction(e -> {
            double R = spinnerR.getValue();
            double r = spinnerr.getValue();
            double O = spinnerO.getValue();
            drawSpirograph(gc, canvasSize / 2, canvasSize / 2, R, r, O);
        });

        VBox controls = new VBox(10, spinnerR, spinnerr, spinnerO, drawButton);
        controls.setPadding(new Insets(10));
        controls.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 1px;");

        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setRight(controls);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private Spinner<Double> createSpinner(double min, double max, double initial, double step) {
        Spinner<Double> spinner = new Spinner<>();
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initial, step);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);
        return spinner;
    }

    private void drawSpirograph(GraphicsContext gc, double centerX, double centerY, double R, double r, double O) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);

        double t = 0;
        double x0 = (R - r) * Math.cos(t) + O * Math.cos((R - r) / r * t);
        double y0 = (R - r) * Math.sin(t) - O * Math.sin((R - r) / r * t);

        while (t < 2 * Math.PI * r / gcd((int) R, (int) r)) {
            t += 0.01;

            double x = (R - r) * Math.cos(t) + O * Math.cos((R - r) / r * t);
            double y = (R - r) * Math.sin(t) - O * Math.sin((R - r) / r * t);

            gc.strokeLine(centerX + x0, centerY + y0, centerX + x, centerY + y);
            x0 = x;
            y0 = y;
        }
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
