package ViewModel;
import Model.Display;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
public class MyViewModel extends Observable implements Observer {
    private int R , C ;
    private Display display ;
    public MyModel myModel ;
    private ArrayList<Observer> myViewControllers = new ArrayList<>();
    public MyViewModel(MyModel m ){
        myModel = m ;
    }
    public void Create_Maze( int Rows , int Cols ) throws FileNotFoundException {
        myModel.Generate_Maze(Rows , Cols);
    }
    public void Move(int i) throws FileNotFoundException {
        myModel.set_player(i);
    }
    public void set(Maze maze){
        this.myModel.set(maze);
    }
    public void draw() throws FileNotFoundException {
        myModel.draw();
    }
    public void Solve_Maze() {
        myModel.Solve_Maze();
    }
    public void SetDisplay(Display mazeDisplayer) {
        this.display = mazeDisplayer ;
        this.myModel.setDisplay(mazeDisplayer);
    }
    public void notifyObservers(Display display) {
        for (int i = 0 ; i < this.myViewControllers.size() ; i++){
            this.myViewControllers.get(i).update(this, display);
        }
    }
    public void update(Display display) {
        this.display = display ;
        notifyObservers( display);
    }
    public void exit(){
        myModel.exit();
        System.exit(0);
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
        this.display = (Display) arg;
        notifyObservers( display);
    }
}
