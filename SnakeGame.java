
import javax.swing.*;

public class SnakeGame {
    public static void main(String[] args) throws Exception{
        int boardWidth=600;
        int boardHeight=boardWidth;

        JFrame frame=new JFrame("Snake");
        
        frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGamePanel Panel= new SnakeGamePanel(boardWidth,boardHeight);
        frame.add(Panel);
        frame.pack();
        Panel.requestFocus();
        
        
    }
}