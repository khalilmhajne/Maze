package Model;

import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;


public class Display extends Canvas implements Serializable {
    public int [] [] maze ;
    private int player_x = -1; //default value ;
    private int player_y = -1; //default value ;
    public Position goal = null;
    public Position start = null ;
    private boolean arrived = false ; //help us to know if the character find the goal or not ;
    private GraphicsContext graphicsContext ;
    private double cellHeight ; //height of graphicsContext
    private double cellWidth ;  //width of graphicsContext
    public Position Current;
    public Display (){
        //super(250, 250);
        widthProperty().addListener(evt -> {
            try {
                draw();
            } catch (FileNotFoundException e) {

            }
        });
        heightProperty().addListener(evt -> {
            try {
                draw();
            } catch (FileNotFoundException e) {
            }
        });
    }
    private void draw() throws FileNotFoundException { //use it whe
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        if (maze!=null) {
            int row = maze.length;
            int col = maze[0].length;
            this.cellHeight = canvasHeight / col;
            this.cellWidth = canvasWidth / row;
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            graphicsContext.setFill(Color.BLACK);
            double x, y;
            //Draw Maze
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (maze[i][j] == 1) // Wall
                    {
                        graphicsContext.setFill(Color.BLACK);
                        x = i * cellWidth;
                        y = j * cellHeight;
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    } else if (i == goal.getRowIndex() && j == goal.getColumnIndex()) {
                        graphicsContext.setFill(Color.RED);
                        x = i * cellWidth;
                        y = j * cellHeight;
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    }
                }
            }
            Image player = new Image(new FileInputStream("./src/Resources/sonic.png"));
            graphicsContext.drawImage(player, player_x * cellWidth, player_y * cellHeight, cellWidth, cellHeight); //draw the character
            if (player_x == goal.getRowIndex() && player_y == goal.getColumnIndex()) { //the character find the goal
                Main.win.set(true);
                showWinDisplay();
                graphicsContext.clearRect(0,0,canvasWidth, canvasHeight); //clear the canvas when the character find the goal
            } else { //the character not find the goal
                arrived = false; //arrived is an variable to help us to know if the character find the goal or not ;
            }
        }
    }
    private void showWinDisplay() {
        try {
            Random rand = new Random();
            int num = rand.nextInt(3);

            String path =System.getProperty("user.dir") + "\\resources\\Winner" + num + ".gif";

            Image win = new Image(new FileInputStream(path));
            ImageView winGif =new ImageView( );
            winGif.setImage(win);
            winGif.setFitHeight(getHeight());
            winGif.setFitWidth(getWidth());



            Pane pane = new Pane();
            Scene scene = new Scene(pane, getWidth(),getHeight());
            Stage newStage = new Stage();
            newStage.setTitle("You did it!");
            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);

            Button button = new Button();
            button.setText("Let me play again!");
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    newStage.close();
                    event.consume();
                }
            });


            winGif.setImage(win);
            pane.getChildren().addAll(winGif, button);
            newStage.initOwner(Main.mainStage);

            newStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw (int [] [] maze , Position start , Position goal) throws FileNotFoundException {
        if( this.start == null || this.goal == null ||this.maze != maze){
            this.maze = maze ;
            this.start = start ;
            this.goal = goal ;
            this.Current = start ;
        }
        this.player_x = start.getRowIndex();
        this.player_y = start.getColumnIndex();
        this.graphicsContext = getGraphicsContext2D();
        draw();
    }
    public void set_player(int i) throws FileNotFoundException {
         if (i==1 && check(player_x-1,player_y+1)){
            player_x = player_x - 1;
            player_y = player_y + 1;
        }
        else if(i == 2 && check(player_x,player_y+1)){ //down
            player_y = player_y + 1 ;
        }
        if (i==3 && check(player_x+1,player_y+1)){
            player_x = player_x + 1;
            player_y = player_y + 1;
        }
        else if(i == 4 && check(player_x-1,player_y)) { //left
            player_x = player_x - 1;
        }
        else if(i == 6 && check(player_x+1, player_y )) { //right
            player_x = player_x + 1;
        }
        if (i==7 && check(player_x-1,player_y-1)){
            player_x = player_x - 1;
            player_y = player_y - 1;
        }
        else if(i == 8 && check(player_x , player_y-1 )){ //up
            player_y = player_y - 1 ;
        }
        if (i==1 && check(player_x-1,player_y+1)){
            player_x = player_x + 1;
            player_y = player_y - 1;
        }

        this.Current = new Position(player_x, player_y) ;
        draw(maze , Current , goal ); //draw the Maze after moving the character
    }
    public boolean check(int x , int y){ //check if the character can move to (x,y)
        if(x >= 0 && x < maze.length && y >= 0 && y < maze[0].length){
            return maze[x][y] != 1;
        }
        return false ;
    }
    public void Solve_Maze(ArrayList<AState> solutionPath ) throws FileNotFoundException {
        for (int j = 0 ; j < solutionPath.size() ; j++) {
            graphicsContext.setFill(Color.LIGHTPINK);
            graphicsContext.fillRect(solutionPath.get(j).GetRow() * cellHeight, solutionPath.get(j).GetCol() * cellWidth, cellHeight, cellWidth);
        }
        javafx.scene.image.Image player = new javafx.scene.image.Image(new FileInputStream("./src/Resources/sonic.png"));
        graphicsContext.drawImage(player , player_x*cellHeight , player_y * cellWidth , cellHeight , cellWidth);
        graphicsContext.setFill(Color.RED);
        graphicsContext.fillRect(goal.getRowIndex()*cellHeight , goal.getColumnIndex()*cellWidth , cellHeight , cellWidth);
    }
}
