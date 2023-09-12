import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.*;


public class Snake extends JFrame {

    int width = 640;        //Screen
    int height = 480;

    int widthPoint = 10;    //snake's size
    int heightPoint = 10;

    Point snake;
    Point apple;

    List<Point> list = new ArrayList<>();

    Design imageSnake;

    int direction = VK_PAUSE;

    long frequency = 70;        //speed

    boolean gameover = false;

    public Snake() {
        setTitle("Snake");
        setSize(width+20, height+30);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-width/2, dim.height/2-height/2);   //Locate the screen in the middle
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //Close the program when you close the Snake window

        Keyboard kb = new Keyboard();
        this.addKeyListener(kb);

        startGame();

        imageSnake = new Design();
        this.getContentPane().add(imageSnake);

        Movement mv = new Movement();
        Thread trid = new Thread(mv);
        trid.start();

        setVisible(true);
    }

    public void startGame() {
        apple = new Point(200, 200);
        snake = new Point(width/2, height/2);
        generateApples();

        list = new ArrayList<>();
        list.add(snake);
    }

    public void generateApples() {
        double randomX = Math.random()*width;
        if((randomX - randomX%10) > 0) {
            apple.x = (int) (randomX - randomX%10);
        } else {
            apple.x = (int) (randomX + (10-randomX%10));
        }


        double randomY = Math.random()*height;
        if((randomY - randomY%10) > 0) {
            apple.y = (int) (randomY - randomY%10);
        } else {
            apple.y = (int) (randomY + (10-randomY%10));
        }

    }

    public void update() {
        imageSnake.repaint();

        list.add(0, new Point(snake.x, snake.y));   //upload "snake body"
        list.remove(list.size()-1);

        for(int i=1; i<list.size(); i++) {
            Point p = list.get(i);
            if(snake.x == p.x && snake.y == p.y) {
                gameover = true;
            }
        }

        if(snake.x == apple.x && snake.y == apple.y) {
            list.add(0, new Point(snake.x, snake.y));   //upload snake
            generateApples();
        }
    }


    public static void main(String[] args) {
        Snake s = new Snake();
    }


    public class Design extends JPanel {

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if(gameover==false) {
                g.setColor(new Color(0,0,255));             //snake
                for(Point point :list) {
                    g.fillRect(point.x, point.y, widthPoint, heightPoint);
                }

                g.setColor(new Color(255,0,0));             //apple
                g.fillOval(apple.x, apple.y, widthPoint, heightPoint);
            }


            if(gameover==true) {
                g.setColor(new Color(255,0,0));
                g.setFont(new Font("TimesRoman", Font.BOLD, 40));
                g.drawString("GAME OVER", 300, 200);
                g.drawString("SCORE: "+(list.size()-1), 300, 240);

                g.setFont(new Font("TimesRoman", Font.BOLD, 20));
                g.drawString("Press ENTER to Start New Game", 100, 320);
                g.drawString("ESC to Exit", 100, 340);
            }

        }
    }

    public class Keyboard extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {


            if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
                System.exit(0);
            } else if(e.getKeyCode() == KeyEvent.VK_UP) {
                if(direction!=KeyEvent.VK_DOWN) {
                    direction = KeyEvent.VK_UP;
                }

            } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                if(direction!=KeyEvent.VK_UP) {
                    direction = KeyEvent.VK_DOWN;
                }

            } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                if(direction!=KeyEvent.VK_RIGHT) {
                    direction = KeyEvent.VK_LEFT;
                }

            } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if(direction!=KeyEvent.VK_LEFT) {
                    direction = KeyEvent.VK_RIGHT;
                }
            } else if (e.getKeyCode() == VK_ENTER) {
                gameover = false;
                startGame();
            }
        }
    }

    public class Movement extends Thread {
        long last = 0;
        public void run() {
            while(true) {
                if((java.lang.System.currentTimeMillis() - last) > frequency) {

                    if(gameover==false) {
                        if(direction== KeyEvent.VK_UP) {
                            snake.y = snake.y - heightPoint;

                            if(snake.y < 0 || snake.y > height) {
                                gameover = true;
                            }
                        } else if(direction == KeyEvent.VK_DOWN) {
                            snake.y = snake.y + heightPoint;

                            if(snake.y < 0 || snake.y > height) {
                                gameover = true;
                            }

                        } else if(direction == KeyEvent.VK_LEFT) {
                            snake.x = snake.x - widthPoint;

                            if(snake.x < 0 || snake.x > width) {
                                gameover = true;
                            }
                        } else if(direction == KeyEvent.VK_RIGHT) {
                            snake.x = snake.x + widthPoint;

                            if(snake.x < 0 || snake.x > width) {
                                gameover = true;
                            }
                        }

                    }

                    update();
                    last = java.lang.System.currentTimeMillis();

                }

            }
        }

    }

}
