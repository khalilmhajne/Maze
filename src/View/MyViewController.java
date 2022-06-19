package View;
import Model.Display;
import ViewModel.MyViewModel;
import java.io.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import sample.Main;
import javax.swing.*;
public class MyViewController implements IView , Observer {
    public TextField Rows ;
    public TextField Cols ;
    public Display mazeDisplayer ;
    public Pane pane ;
    public ComboBox combo ;
    public GridPane prog ;
    public BorderPane border ;
    public MenuBar MenuBar ;
    public MyViewModel myViewModel ;
    public Button btn_exit;
    public Button btn_music;
    public StackPane stackPane ;
    Media startMusic = new Media(new File("resources/game_music.mp3").toURI().toString());
    Media winMusic = new Media(new File("resources/end_music.mp3").toURI().toString());
    MediaPlayer mediaPlayerStart = new MediaPlayer(startMusic);
    MediaPlayer mediaPlayerWinner = new MediaPlayer(winMusic);


    public void initialize(StackPane stackPane){ //use when we try to resize the component(window)
        this.stackPane = stackPane ;
        prog.prefWidthProperty().bind(stackPane.widthProperty());
        prog.prefHeightProperty().bind(stackPane.heightProperty());
        border.prefWidthProperty().bind(prog.widthProperty());
        border.prefHeightProperty().bind(prog.heightProperty());
        pane.prefWidthProperty().bind(border.widthProperty());
        pane.prefHeightProperty().bind(border.heightProperty());
        mazeDisplayer.heightProperty().bind(pane.heightProperty());
        mazeDisplayer.widthProperty().bind(pane.widthProperty());
    }
    @Override
    public void Create_Maze() throws FileNotFoundException {
        myViewModel.SetDisplay(mazeDisplayer);
        if(!Rows.getText().equals("") && !Cols.getText().equals("")){
            int R = Integer.parseInt(Rows.getText());
            int C = Integer.parseInt(Cols.getText());
            if (R == 0 || C == 0 || R==1 || C==1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("You must insert number ( between 2 - 500) !");
                alert.show();
            }
            else {
                myViewModel.Create_Maze(R, C);
                Main.win.set(false);
                if (mediaPlayerStart.getStatus().equals(MediaPlayer.Status.PLAYING)){
                    return;
                }
                setPlayPauseEvent(new ActionEvent());
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You must insert number ( between 2 - 500) !");
            alert.show();
        }
    }

    public void Solve_Maze(){
            myViewModel.Solve_Maze();
    }

    public void setView(MyViewModel myViewModel) {
        this.myViewModel = myViewModel ;
    }

    public void update(Display mazeDisplayer) {
        this.mazeDisplayer = mazeDisplayer ;
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        this.mazeDisplayer = (Display) arg;
    }

    //events
    @Override
    public void mouse_clicked(javafx.scene.input.MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
    public void key_pressed(KeyEvent keyEvent) throws FileNotFoundException {
        switch (keyEvent.getText()){
            case "1":
                myViewModel.Move(1);
                break;
            case "2":
                myViewModel.Move(2);
                break;
            case "3":
                myViewModel.Move(3);
                break;
            case "4":
                myViewModel.Move(4);
                break;
            case "5":
                myViewModel.Move(5);
                break;
            case "6":
                myViewModel.Move(6);
                break;
            case "7":
                myViewModel.Move(7);
                break;
            case "8":
                myViewModel.Move(8);
                break;
        }
        keyEvent.consume();
    }
    // menu bar
    public void AboutClick(ActionEvent actionEvent) {
        String str = "This game was developed within the framework of advanced topic in programming course, we are still new to this so we apologize for any technical issues you might encounter while running it, but we promise that we'll fix it if you contacted us and let us know. \n" +
                "Finally we want to thank Google for helping us develop this game.";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Programmers Info");
        alert.setContentText(str);
        alert.initStyle(StageStyle.DECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();
    }
    public void OptionsClick(ActionEvent actionEvent) {
        String str = "ThreadPoolSize=8\n" +
                "MazeGenerator=MyMazeGenerator\n" +
                "SearchingAlgorithm=BestFirstSearch";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Options");
        alert.setHeaderText("properties");
        alert.setContentText(str);
        alert.initStyle(StageStyle.DECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();
    }

    public void HelpClick(ActionEvent actionEvent) {
        String str = "This is a maze game.\nFirst you need to define the maze size and press" +
                " on the GenerateMaze button.\nUse the arrows keys and help Sonic" +
                " to get out of the maze!\nYou can use the SolveMaze button\nIf you are unable to solve the maze!\n";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("User's Guide:");
        alert.setContentText(str);
        alert.initStyle(StageStyle.DECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();
    }
    public void ExitButton (ActionEvent actionEvent){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Are you sure you want to exit?");
            alert.setTitle("Exit");
            alert.setHeaderText("Exit");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("myDialog");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // player press OK
                // Close the program
                myViewModel.exit();
            }
        actionEvent.consume();
    }
    public void upload() throws IOException, ClassNotFoundException { //uploading maze
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showOpenDialog(parentFrame) ;
        if(userSelection == JFileChooser.APPROVE_OPTION){
            File selected_file = fileChooser.getSelectedFile();
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(selected_file.getAbsolutePath()));
            int [][]arr = (int[][]) objectInputStream.readObject() ;
            Position start = (Position) objectInputStream.readObject() ;
            Position goal = (Position) objectInputStream.readObject() ;
            Main.win.set(false);
            myViewModel.set(new Maze( arr,start ,goal));
            myViewModel.draw();
            objectInputStream.close();
        }
    }
    public void saveMaze() throws IOException { //saving maze
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("save as ...");
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(String.format("%s" , fileToSave.getAbsolutePath())));
            objectOutputStream.writeObject(mazeDisplayer.maze);
            objectOutputStream.writeObject(mazeDisplayer.start);
            objectOutputStream.writeObject(mazeDisplayer.goal);
            objectOutputStream.close();
        }
    }
    public void setPlayPauseEvent(ActionEvent actionEvent) {
        actionEvent.consume();
        MediaPlayer.Status statusStart = mediaPlayerStart.getStatus();
        MediaPlayer.Status statusWinner = mediaPlayerWinner.getStatus();
        if (statusStart.equals(MediaPlayer.Status.READY)){
            mediaPlayerWinner.stop();
            btn_music.setText("Pause");
            mediaPlayerStart.play();
            return;
        }
        if (Main.win.getValue()){
            if (statusWinner.equals(MediaPlayer.Status.PLAYING)){
                btn_music.setText("Play");
                mediaPlayerWinner.pause();
                return;
            }
            mediaPlayerStart.stop();
            mediaPlayerWinner.play();
            btn_music.setText("Pause");
            return;
        }
        mediaPlayerWinner.stop();
        if (statusStart.equals(MediaPlayer.Status.PLAYING)){
            mediaPlayerStart.pause();
            btn_music.setText("Play");
        }
        else {
            mediaPlayerStart.play();
            btn_music.setText("Pause");
        }
    }
    public void addMouseScrolling(ScrollEvent scrollEvent) {
        if(scrollEvent.isControlDown()) {
            double zoomFactor = 1.05;
            double deltaY = scrollEvent.getDeltaY();
            if (deltaY < 0) {
                zoomFactor = 2.0 - zoomFactor;
            }
            mazeDisplayer.setScaleX(mazeDisplayer.getScaleX() * zoomFactor);
            mazeDisplayer.setScaleY(mazeDisplayer.getScaleY() * zoomFactor);
        }
    }

}
