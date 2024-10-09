
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

// inherits methods from JPanel and implements methods from the ActionListener and KeyListener interfaces
public class SnakeGamePanel extends JPanel implements ActionListener,KeyListener{
    
    //private tile class 
    private class Tile{
        int x;
        int y;

        Tile(int x,int y){
            this.x=x;
            this.y=y;

        }
    }
    
    

    int boardWidth;
    int boardHeight;
    private final int tileSize=25;
    //snake
    private Tile snakeHead;
    private Tile snakeHead2;
    //for the body which is a list of tiles 
    private ArrayList<Tile> snakeBody=new ArrayList<Tile>();
    private ArrayList<Tile> snakeBody2=new ArrayList<Tile>();

    //food
    private Tile food;
    private Random randy;
    //game logic
    private Timer gameLoop;
    private final int fps=110;
    private int velocityX;
    private int velocityY;
    private int velocityX2;
    private int velocityY2;

    
    private String winner="";
    private int prevScore;
    private int speedCnt; 


    //game states
    private int gameState;
    private int prevState=0; 
    private final int playState1=1;
    private final int playState2=2;
    private final int pauseState=3;
    private final int titleState=4;
    private final int gameOverState=6;

    //font
    private Font font;


    SnakeGamePanel(int boardWidth,int boardHeight){
        this.boardWidth=boardWidth;
        this.boardHeight=boardHeight;
        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.black);
        // adding the keyListener
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
        startGame();
       
          
    }

    public void startGame(){
        //the snakes head and body
        snakeHead=new Tile(5,5);
        snakeBody=new ArrayList<Tile>();
       
            snakeHead2= new Tile(18,18);
            snakeBody2=new ArrayList<Tile>();
            velocityX2=0;
            velocityY2=0;
        
        //the food and random placement 
        food=new Tile(10,10);
        randy=new Random();
        WheresFood();
        //timer that sets the fps and starts it
        gameLoop=new Timer(fps,this);
        gameLoop.start();
        // initialising movement
        velocityX=0;
        velocityY=0;
        //initialising gamestate
        gameState=titleState;
        // for speed function
        prevScore=0;
        speedCnt=0;
          
    }
    public void speedUp(){
        //speeds up the Timer thus increasing the speed of the snake every 5 segments added to the body of 1 player and 2 player combined
        if (gameState==playState1){
            if (snakeBody.size()%5==0&&snakeBody.size()!=prevScore&&speedCnt<7){
                gameLoop.stop();
                gameLoop=new Timer(fps-(snakeBody.size()/5)*10,this);
                gameLoop.start();
                speedCnt++;
                prevScore=snakeBody.size();
            }
        }
        else{
            int num=snakeBody2.size()+snakeBody.size();
            if (num%5==0&& num!=prevScore&&speedCnt<7){
                gameLoop.stop();
                gameLoop=new Timer(fps-(num/5)*10,this);
                gameLoop.start();
                speedCnt++;
                prevScore=num;
            }
        }
        
        
    }

    public void pauseGame(int prevState){
        //pauses the game and captures the previous game state 
        gameState=pauseState;
        this.prevState=prevState;
        gameLoop.stop();
    }

    public void unpauseGame(int prevState){
        //unpauses the game using the previously captured state
        gameState=prevState;
        gameLoop.start();
    }
    

    public void drawTitle(Graphics2D g2){
        // draws title screen
        font=new Font("Snap ITC",Font.PLAIN,40);
        g2.setFont(font);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,67f));
        String text="Snake Library";
        int textx= 36;
        int texty=tileSize*10;

        g2.setColor(Color.red);
        //g.fillRect(0,0,boardWidth,boardHeight);
        g2.drawString(text,textx,texty);
        g2.setColor(Color.yellow);
        g2.drawString(text,textx-4,texty-4);

        textx=50;
        texty=tileSize*12;
        text="Press (1) for Single-Player (2) for Two-Player mode.";
        font=new Font("Small Fonts",Font.PLAIN,22);
        g2.setColor(Color.white);
        g2.setFont(font);
        g2.drawString(text,textx,texty);

        textx=210;
        texty=tileSize*14;
        text="Press (p) pause.";
        font=new Font("Small Fonts",Font.PLAIN,22);
        g2.setColor(Color.white);
        g2.setFont(font);
        g2.drawString(text,textx,texty);
        
    }

    public void drawPlaystates(Graphics2D g2){ // draws playstate 1 and playstsate 2
        
        // score p1
        int x=1*tileSize;
        int y=1*tileSize;
        String text="Player 1 Score: "+snakeBody.size();
        font=new Font("Small Fonts",Font.PLAIN,22);
        g2.setColor(Color.white);
        g2.setFont(font);
        g2.drawString(text,x,y);
        
        //food
        g2.setColor(Color.magenta);
        g2.fillOval(food.x*tileSize,food.y*tileSize,tileSize,tileSize);

        //snake
        g2.setColor(Color.cyan);
        g2.fill3DRect(snakeHead.x*tileSize,snakeHead.y*tileSize,tileSize,tileSize,true);

        //body
        for (int i=0;i<snakeBody.size();i++){
            Tile bodyPart=snakeBody.get(i);
            g2.setColor(Color.green);
            g2.fill3DRect(bodyPart.x*tileSize,bodyPart.y*tileSize,tileSize,tileSize,true);
        }
        if (playState2==gameState){

            //score p2
            x=17*tileSize;
            y=23*tileSize;
            text="Player 2 Score: "+snakeBody2.size();
            font=new Font("Small Fonts",Font.PLAIN,22);
            g2.setColor(Color.white);
            g2.setFont(font);
            g2.drawString(text,x,y);

             //snake
            g2.setColor(Color.red);
            g2.fill3DRect(snakeHead2.x*tileSize,snakeHead2.y*tileSize,tileSize,tileSize,true);

            //body
            for (int i=0;i<snakeBody2.size();i++){
                Tile bodyPart2=snakeBody2.get(i);
                g2.setColor(Color.orange);
                g2.fill3DRect(bodyPart2.x*tileSize,bodyPart2.y*tileSize,tileSize,tileSize,true);
            }
        }
    }

    public void drawGameOver(Graphics2D g2){
            //draws game over screen
            String text="Game Over";
            int textx= 150;
            int texty=boardHeight/2;

           
            //shadow
            font=new Font("Snap ITC",Font.PLAIN,40);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,60f));
            g2.setColor(Color.gray);
            g2.drawString(text,textx,texty);
            //main
            g2.setColor(Color.white);
            g2.drawString(text,textx-4,texty-4);
            // instructions
            textx=135;
            texty=tileSize*14;
            text="press (space) to return to title-screen";
            font=new Font("Small Fonts",Font.PLAIN,22);
            g2.setColor(Color.white);
            g2.setFont(font);
            g2.drawString(text,textx,texty);
            //
            textx=(boardWidth/2)-60;
            texty=20*tileSize;
            text=winner;
            font=new Font("Small Fonts",Font.PLAIN,22);
            g2.setColor(Color.white);
            g2.setFont(font);
            g2.drawString(text,textx,texty);

            
    }
    
    public void paintComponent(Graphics g){
        //paint component handles all the drawing in this class inheriting the same method of the Jpanel class
        super.paintComponent(g);
        Graphics2D g2= (Graphics2D)g;
        if (gameState==titleState){
            drawTitle(g2);
        }  
        else if (gameState==playState1||gameState==playState2){
            drawPlaystates(g2);
        }
        else if (gameOverState==gameState){
            drawGameOver(g2);
        }
       
    }

    public void WheresFood(){
        //this class randomly places the food 
        food.x=randy.nextInt(boardWidth/tileSize);
        food.y=randy.nextInt(boardHeight/tileSize);
    }

    // redefining move() function
    public void move(){
        //this function handles everything related to movent such as collisions etc
        boolean ans=(gameState==playState2);
        // when snake collides with wall
        if (snakeHead.x*tileSize<0||snakeHead.y*tileSize<0||snakeHead.x*tileSize>=boardWidth||snakeHead.y*tileSize>=boardHeight){
            gameState=gameOverState;
            if (ans){
                winner="Winner player 2";
            }
            
        }
        if (ans){
            if (snakeHead2.x*tileSize<0||snakeHead2.y*tileSize<0||snakeHead2.x*tileSize>=boardWidth||snakeHead2.y*tileSize>=boardHeight){
                gameState=gameOverState;
                winner="Winner player 1";
            }
        
        }
        //when the snake eats food
        if (collision(snakeHead,food)){
            snakeBody.add(new Tile(food.x,food.y));
            WheresFood(); 
        }
        //when snake 2 eats the food
        if (ans){
            if (collision(snakeHead2,food)){
                snakeBody2.add(new Tile(food.x,food.y));
                WheresFood();    
            }
        }
        //if the snake heads collide
        if (ans){
            if (collision(snakeHead,snakeHead2)){
                gameState=gameOverState;
                winner="Draw";
            }
        }

        //adding a tile to snake body
        for (int i=snakeBody.size()-1;i>=0;i--){
            Tile bodyPart=snakeBody.get(i);
            if (i == 0){
                bodyPart.x=snakeHead.x;
                bodyPart.y=snakeHead.y;
            }
            else{
                Tile prevPart=snakeBody.get(i-1);
                bodyPart.x=prevPart.x;
                bodyPart.y=prevPart.y;

            }
        }

        if (ans){
            for (int i=snakeBody2.size()-1;i>=0;i--){
                Tile bodyPart2=snakeBody2.get(i);
                if (i == 0){
                    bodyPart2.x=snakeHead2.x;
                    bodyPart2.y=snakeHead2.y;
                }
                else{
                    Tile prevPart2=snakeBody2.get(i-1);
                    bodyPart2.x=prevPart2.x;
                    bodyPart2.y=prevPart2.y;
                }
            }
        }
       
        //what it means for the snakehead
        snakeHead.x +=velocityX;
        snakeHead.y +=velocityY;

        if (ans){
            snakeHead2.x +=velocityX2;
            snakeHead2.y +=velocityY2;
        }
           

    
         // when snake collides with its own body or the other snakes head colllides with its body
        for (int i=0;i<snakeBody.size();i++){
            Tile bodyPart=snakeBody.get(i);
            if (collision(snakeHead,bodyPart)){
                gameState=gameOverState;
                System.out.println("collided with own body");
                if (ans){
                    winner="Winner player 2";
                }
            }
            if (ans){
                if (collision(snakeHead2,bodyPart)){
                    gameState=gameOverState;
                    winner="Winner Player 1";
                }
            }
        }
        if (ans){
            for (int i=0;i<snakeBody2.size();i++){
                Tile bodyPart2=snakeBody2.get(i);
                if (collision(snakeHead2,bodyPart2)){
                    gameState=gameOverState;
                    winner="Winner player 1";
                }
                if (collision(snakeHead,bodyPart2)){
                    gameState=gameOverState;
                    winner="Winner player 2";
                }
            }
        }
    }


    public boolean collision(Tile tile1, Tile tile2){
        //this functions checks if two tiles occupy the same position therefore colliding
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    //actionListener methods
    @Override
    //only one method to override
    public void actionPerformed(ActionEvent e){
        //a method of the actionPerformed interface that performs an action in relation to the Timer
        move();
        repaint();
        speedUp();
        if (gameOverState==gameState){
            gameLoop.stop();
        }
    }

    //KeyListener methods
    @Override
    public void keyPressed(KeyEvent e){
        // a method of the keylistener interface waits for the specified keys pressed to signal an action
        if (gameState==titleState){
            if (e.getKeyCode()==KeyEvent.VK_1){
                gameState=playState1;
            }
            if (e.getKeyCode()==KeyEvent.VK_2){
                gameState=playState2;
            }
        }
       
       if (gameState==playState1||gameState==playState2){
           
            if (e.getKeyCode()==KeyEvent.VK_UP && velocityY!=1){
                velocityX=0;
                velocityY=-1;
            }
            else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityY!=-1){
                velocityX=0;
                velocityY=1;
            }
            else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityX!=-1){
                velocityX=1;
                velocityY=0;
            }
            else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityX!=1){
                velocityX=-1;
                velocityY=0;
            }
            if (gameState==playState2){
                if (e.getKeyCode()==KeyEvent.VK_W && velocityY2!=1){
                    velocityX2=0;
                    velocityY2=-1;
                }
                else if(e.getKeyCode()==KeyEvent.VK_S && velocityY2!=-1){
                    velocityX2=0;
                    velocityY2=1;
                }
                else if(e.getKeyCode()==KeyEvent.VK_D && velocityX2!=-1){
                    velocityX2=1;
                    velocityY2=0;
                }
                else if(e.getKeyCode()==KeyEvent.VK_A && velocityX2!=1){
                    velocityX2=-1;
                    velocityY2=0;
                }
            }
        }   
       
        
        if(e.getKeyCode()==KeyEvent.VK_P){
            if(gameState==playState1||gameState==playState2){
                pauseGame(gameState);
                
            }
            else if(gameState==pauseState){
               unpauseGame(prevState);
        }
        }
        if (gameState==gameOverState){
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                if (gameState==titleState||gameState==gameOverState){
                    startGame();
                    winner="";
                }
            }
        }
        
          
    }
    // dont need these methods however are necessarry in order to not have an error
    public void keyTyped(KeyEvent e){

    }
    public void keyReleased(KeyEvent e){
        
    }
}
