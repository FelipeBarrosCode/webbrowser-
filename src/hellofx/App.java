/*
 * java -cp ./bin --module-path "C:/Users/Felipe/Documents/LangaraTerm3/CPSC1181/openjfx-21.0.2_windows-x64_bin-sdk/javafx-sdk-21.0.2/lib" --add-modules javafx.controls,javafx.fxml,javafx.web hellofx.App

 * 
 * javac -d ./bin --module-path C:/Users/Felipe/Documents/LangaraTerm3/CPSC1181/openjfx-21.0.2_windows-x64_bin-sdk/javafx-sdk-21.0.2/lib  --add-modules javafx.controls,javafx.fxml,javafx.web ./src/hellofx/App.java
 */

package hellofx;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
// package application;
//--------------------------------------	
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.css.converter.URLConverter;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.MalformedURLException;
import java.net.URL;

//--------------------------------------

/**
 * 
 * @author Felipe Barros Moura
 * @since 3-30-2024
 * @version 0.0.1
 * 
 * 
 */
public class App extends Application {
    private BorderPane root;
    private WebView webView;
    private WebEngine webEngine;
    private HBox addressBar;
    private HBox statusBar;
    private Text domain;
    private final String homePage = "https://www.langara.ca";
    private Rectangle backgroundInfo;
    private ObservableList<Node> children;
    private Button home;
    private Button arrowRight;
    private Button arrowLeft;
    private Button refreshButton;
    private TextField url;
    private Text separator;
    private Text copyright;
    private Timeline timeline;
    private String time;
    private LocalDateTime localTime;
    private Text dateCalendar;
    private String timeCounter;
    private Text dateCounter;
    private URL urlToParse;
    private Scene scene;
    private WebHistory webHistory;
    private String middleString = "";
    private int indexCounter = 0;
    private ArrayList<URL> listOfurls = new ArrayList<URL>();

    public static void main(String[] args) {

        Application.launch(args);
    }

    /**
     * @param stage
     *              start method will start the application and set the basic
     *              operations
     */

    public void start(Stage stage) {

        root = new BorderPane();
        scene = new Scene(root);
        // --------------------------------------
        this.setupWebView();
        this.setupAddressBar();
        this.setupStatusBar();
        // --------------------------------------
        root.setTop(addressBar);
        root.setBottom(statusBar);
        root.setCenter(webView);
        // --------------------------------------

        stage.setScene(scene);
        // stage.setFullScreen(true);
        stage.setWidth(1200);
        stage.setHeight(800);
        stage.setResizable(false);
        stage.getIcons().add(new Image("home.png"));

        stage.setTitle("JavaFX Browser");
        stage.show();
    }
    // --------------------------------------

    // --------------------------------------
    // --------------------------------------

    /**
     * 
     * 
     * Deal with the changing page event when the user press enter
     * 
     * For some reasons sites work with www always so I just kept the World Wide Web
     * domain
     * 
     * I had some issues with www and just https so I just kept https://www.
     * 
     */

    public void enterAction() {

        try {

            if (url.getText().contains("https://www.")) {

                url.setText(url.getText().substring(12));

            }

            url.setText("https://www." + url.getText());
            urlToParse = new URL(url.getText());
            indexCounter = listOfurls.size();

            if (!(urlToParse.equals(listOfurls.get(indexCounter - 1)))) {
                listOfurls.add(urlToParse);
            }

            domain.setText(urlToParse.getHost());
            webEngine.load(urlToParse.toString());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Reloads the page when the user press F5 or the restart button
     */

    public void reloadAction() {

        webEngine.reload();

    }

    /**
     * Go back to the previous page
     */

    public void goBack() {
        webHistory.go(-1);

        indexCounter = indexCounter - 1;
        domain.setText(listOfurls.get(indexCounter).getHost());
        url.setText(listOfurls.get(indexCounter).toExternalForm());

    }

    /**
     * go to the next page if the user already serched for something
     * 
     */
    public void goFoward() {
        webHistory.go(1);

        indexCounter = indexCounter + 1;
        domain.setText(listOfurls.get(indexCounter).getHost());
        url.setText(listOfurls.get(indexCounter).toExternalForm());
    }

    /**
     * Handle all the click and button events
     */

    public void handleEvents() {

        scene.setOnKeyPressed(event -> {
            webHistory = webEngine.getHistory();

            switch (event.getCode()) {

                case ENTER:
                    enterAction();
                    break;
                case F5:
                    reloadAction();
                    break;
                case LEFT:

                    goBack();

                    break;
                case RIGHT:

                    goFoward();

                    break;
                default:
                    break;
            }

        });

        home.setOnAction(event -> {

            url.setText(homePage);
            enterAction();

        });
        arrowLeft.setOnAction(event -> {
            goBack();
        });
        arrowRight.setOnAction(event -> {
            goFoward();
        });
        refreshButton.setOnAction(event -> {
            reloadAction();
        });
    }

    /**
     * 
     * Set up the Hbox on the top
     */

    private void setupAddressBar() {
        addressBar = new HBox();

        addressBar.setMinHeight(50);
        addressBar.setMaxHeight(80);
        // I can create class to create button and other to deal with events

        home = setButton(new ImageView("home.png"));
        arrowLeft = setButton(new ImageView("arrow.png"));
        arrowRight = setButton(new ImageView("right-arrow.png"));
        refreshButton = setButton(new ImageView("refresh.png"));

        // This will be a TextField that we are going to deal with the vent later
        url = new TextField();
        url.setPrefWidth(800);

        addressBar.getChildren().addAll(home, arrowLeft, arrowRight, refreshButton, url);
        setStyles(addressBar);
        handleEvents();
    }

    /**
     * 
     * @param contentInsideButton
     * @return Button
     * 
     *         Create buttons
     */

    public Button setButton(ImageView contentInsideButton) {

        return new Button("", contentInsideButton);

    }

    /**
     * 
     * @param hboxDecide
     * 
     *                   Set the style of both HBoxes
     */
    public void setStyles(HBox hboxDecide) {

        hboxDecide.setSpacing(20);
        hboxDecide.setStyle("-fx-background-color: black;");
        hboxDecide.setAlignment(Pos.CENTER);
        children = hboxDecide.getChildren();
        for (Node control : children) {
            if (control instanceof Button) {

                ((Button) control).setTextFill(Color.WHITE);

            } else if (control instanceof Text) {
                ((Text) control).setFill(Color.YELLOW);

                ((Text) control).setTextAlignment(TextAlignment.CENTER);

            }
        }

    }

    /**
     * 
     * deals with the animation from the timer
     * 
     * 
     */

    public void animate() {
        localTime = LocalDateTime.now();
        timeCounter = localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        dateCounter.setText(timeCounter);

    }

    /**
     * 
     * Set up the Hbox bar on the bottom of the page
     */
    private void setupStatusBar() {

        statusBar = new HBox();

        domain = new Text("langara.ca");
        separator = new Text("|");
        copyright = new Text("JavaFX -- All Rights Reserved.");
        localTime = LocalDateTime.now();

        time = localTime.format(DateTimeFormatter.ofPattern("MM/d/u"));
        dateCalendar = new Text(time);

        dateCounter = new Text("1:1");
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), events -> animate()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        statusBar.getChildren().addAll(domain, separator, new Text('\u00A9' + "2024 --Felipe"), new Text("|"),
                copyright, new Text("|"), dateCalendar, new Text("|"), dateCounter);
        setStyles(statusBar);
    }

    /**
     * 
     * Creates the entire webview
     */
    // --------------------------------------
    private void setupWebView() {
        try {
            urlToParse = new URL(homePage);
            listOfurls.add(urlToParse);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            webView = new WebView();
            webEngine = webView.getEngine();
            webEngine.load(homePage);
        }

    }

}
