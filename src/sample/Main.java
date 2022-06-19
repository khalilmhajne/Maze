package sample;
import Model.MyModel;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class Main extends Application {
    public static Stage mainStage;
    public static SimpleBooleanProperty win;
    @Override
    public void start(Stage primaryStage) throws Exception{
        MyModel myModel = new MyModel();
        MyViewModel myViewModel = new MyViewModel(myModel);
        myModel.addObserver(myViewModel);
        FXMLLoader FXML = new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        Parent fxml = FXML.load();
        MyViewController myViewController = FXML.getController();
        myViewController.setView(myViewModel);
        myViewModel.addObserver(myViewController);
        primaryStage.setTitle("Maze App");
        StackPane stackPane = new StackPane(fxml);
        myViewController.initialize(stackPane);
        int width = 716;
        int height = 502;
        Scene scene = new Scene(stackPane ,width , height ) ;
        scene.getStylesheets().add(getClass().getResource("../View/ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        // setting event to close the program after pressed X at top right
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            myViewController.ExitButton(new ActionEvent());
        });
        mainStage = primaryStage;
        win = new SimpleBooleanProperty(false);
        win.addListener((observable, oldValue, newValue) -> myViewController.setPlayPauseEvent(new ActionEvent()));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}
