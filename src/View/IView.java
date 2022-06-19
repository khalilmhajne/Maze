package View;

import Model.Display;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import sample.Main;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Observable;
import java.util.Optional;

public interface IView {
    public void Create_Maze() throws FileNotFoundException;
    public void Solve_Maze() ;
    public void setView(MyViewModel myViewModel);
    //public void update(Display mazeDisplayer);
    public void update(Observable o, Object arg) ;
    public void mouse_clicked(javafx.scene.input.MouseEvent mouseEvent) ;
    public void key_pressed(KeyEvent keyEvent) throws FileNotFoundException ;
    public void AboutClick(ActionEvent actionEvent);
    public void OptionsClick(ActionEvent actionEvent);
    public void HelpClick(ActionEvent actionEvent) ;
    public void ExitButton (ActionEvent actionEvent);
    public void upload() throws IOException, ClassNotFoundException ;
    public void saveMaze() throws IOException ;
    public void setPlayPauseEvent(ActionEvent actionEvent) ;
    public void addMouseScrolling(ScrollEvent scrollEvent) ;



}
