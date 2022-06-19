package Model;
import algorithms.mazeGenerators.Maze;
import java.io.*;
import java.util.Observer;
public interface IModel {
    public void Generate_Maze (int Rows , int Cols) throws FileNotFoundException;
    public void Solve_Maze () ;
    public void set(Maze maze);
    public void set_player (int i) throws FileNotFoundException ;
    public void setDisplay(Display d);
    public void addObserver(Observer obserever);
    public void notifyObservers(Display display) ;
    public void exit();
    public void draw() throws FileNotFoundException ;
}
