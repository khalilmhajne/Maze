package Model;
import Client.*;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
public class MyModel extends Observable implements IModel {
    private Maze playerMaze ;
    public Display display ;
    private Server mazeGeneratingServer;
    private Server mazeSolvingSerever ;
    private ArrayList<Observer> vms = new ArrayList<>();
    public MyModel (){
        display = new Display();
        mazeGeneratingServer = new Server(5404, 1000, new ServerStrategyGenerateMaze());
        mazeSolvingSerever = new Server(5405 , 1000 , new ServerStrategySolveSearchProblem());
        mazeGeneratingServer.start();
        mazeSolvingSerever.start();
    }
    @Override
    public void Generate_Maze(int Rows , int Cols) throws FileNotFoundException {
        // ------------- generate the maze by the server
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5404, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{Rows, Cols};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = ((byte[])fromServer.readObject());
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[Rows*Cols+12]; //need to check
                        is.read(decompressedMaze);
                        playerMaze = new Maze(decompressedMaze);
                        display.draw(playerMaze.GetMaze(), playerMaze.getStartPosition(), playerMaze.getGoalPosition()); //draw the maze on the display
                        notifyObservers(display); //update all the observers
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }
    public void set(Maze maze){
        playerMaze = maze ;
    }
    @Override
    public void Solve_Maze() {
            try {
                Client client = new Client(InetAddress.getLocalHost(), 5405, new IClientStrategy() {
                    @Override
                    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            Maze m = new Maze(playerMaze.GetMaze() , display.Current , display.goal );
                            toServer.writeObject(m); //send maze to server
                            toServer.flush();
                            Solution mazeSolution = (Solution) fromServer.readObject(); //read the solution from the server
                            ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                            display.Solve_Maze(mazeSolutionSteps) ; // draw the path from the start place to the goal

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                client.communicateWithServer();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    public void set_player (int i) throws FileNotFoundException { //move the player
        display.set_player(i);
        notifyObservers(display);//update all the observers
    }
    public void setDisplay(Display d) {
        display = d ;
    }
    @Override
    public void addObserver(Observer obserever) {
        this.vms.add(obserever);
    }
    @Override
    public void notifyObservers(Display display) {
        for (int i = 0 ; i < this.vms.size() ; i++){
            this.vms.get(i).update(this, display);
        }
    }
    public void exit(){
        mazeGeneratingServer.stop();
        mazeSolvingSerever.stop();
    }
    public void draw() throws FileNotFoundException {//draw after upload the maze
        display.draw(playerMaze.GetMaze() , playerMaze.getStartPosition() , playerMaze.getGoalPosition());
    }
}
