


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

 interface Commons {
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int WIDTH = (int)screenSize.getWidth();
    public static final int HEIGTH = (int)screenSize.getHeight();
    public static final int BOTTOM_EDGE =(int)screenSize.getHeight()-100;
    public static final int N_OF_BRICKS = 30;
    public static final int INIT_PADDLE_X =(int)screenSize.getWidth()/2-100 ;      // 200 paddle width.
    public static final int INIT_PADDLE_Y = (int)screenSize.getHeight()-150;      // 40 height
    public static final int INIT_BALL_X = INIT_PADDLE_X+70;            
    public static final int INIT_BALL_Y = INIT_PADDLE_Y-50;          // 50 ball diameter(height)
    public static final int DELAY = 2000;
    public static final int PERIOD1 = 5;
    public static final int PERIOD2 = 3;
    public static final int PERIOD3=15;
    public static final int N_OF_SPIDERS=11;
}

 class Basic {
    
    protected int x;
    protected int y;
    protected int i_width;
    protected int i_height;
    protected Image image;
     
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return i_width;
    }
    
    public int getHeight() {
        return i_height;
    }
    
    Image getImage() {
        return image;
    }
    
    //ball
    Rectangle getRect1() {
        return new Rectangle(x, y,50, 50);
    }
    //paddle
    Rectangle getRect2() {
        return new Rectangle(x, y,200, 50);
    }
    //brick
    Rectangle getRect3() {
        return new Rectangle(x, y,100, 50);
    }
     
     Rectangle getRect4() {
         return new Rectangle(x, y,70, 70); // spidy dimensions
     }

}

 class Brick extends Basic {
    
    private boolean destroyed;
    
    public Brick(int x, int y) {
        
        this.x = x;
        this.y = y;
        
        ImageIcon ii = new ImageIcon("/Users/yuktigirdhar/Desktop/project_images/brick2.png");
        image = ii.getImage();
        
        //i_width = image.getWidth(null);
        //i_height = image.getHeight(null);
        
        i_width=100;
        i_height=50;
        destroyed = false;
    }
    
    public boolean isDestroyed() {
        
        return destroyed;
    }
    
    public void setDestroyed(boolean val) {
        
        destroyed = val;
    }
}


 class Ball extends Basic implements Commons {
    
    private int xdir;
    private int ydir;
    
    public Ball() {
        
        xdir = 1;
        ydir = -1;
        
        ImageIcon ii = new ImageIcon("/Users/yuktigirdhar/Desktop/project_images/ping_pong_PNG10364.png");
        image = ii.getImage();
        
       // i_width = image.getWidth(null);
        //i_height = image.getHeight(null);
        i_width=50;
        i_height=50;
        
        resetState();
    }
    
    public void move() {
        
        x += xdir;
        y += ydir;
        
        if (x == 0) {
            setXDir(1);
        }
        
        if (x == WIDTH - i_width) {
            setXDir(-1);
        }
        
        if (y == 0) {
            setYDir(1);
        }
    }
    
    private void resetState() {
        
        x = INIT_BALL_X;
        y = INIT_BALL_Y;
    }
    
    public void setXDir(int x) {
        xdir = x;
    }
    
    public void setYDir(int y) {
        ydir = y;
    }
    
    public int getYDir() {
        return ydir;
    }
}


 class Paddle extends Basic implements Commons {
    
    private int dx;
    
    public Paddle() {
        
        ImageIcon ii = new ImageIcon("/Users/yuktigirdhar/Desktop/project_images/paddle1.png");
        image = ii.getImage();
        
      //  i_width = image.getWidth(null);
       // i_height = image.getHeight(null);
        i_width=200;
        i_height=50;
    
        resetState();
    }
    
    public void move() {
        
        x += dx;
        
        if (x <= 0) {
            x = 0;
        }
        
        if (x >= WIDTH - i_width) {
            x = WIDTH - i_width;
        }
    }
    
    public void keyPressed(KeyEvent e) {
        
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT) {
            dx = -1;
        }
        
        if (key == KeyEvent.VK_RIGHT) {
            dx = 1;
        }
    }
    
    public void keyReleased(KeyEvent e) {
        
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }
        
        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }
    
    private void resetState() {
        
        x = INIT_PADDLE_X;
        y = INIT_PADDLE_Y;
    }
}


class spider extends Basic implements Commons {
    
    private int flag=-1;
    private int dy=1;
    //i_width=10;
    //i_height=10;
    
    spider(){
        flag=-1;
        dy=1;
    }
    public void initial(int a,int b)
    {
        flag=1;
        ImageIcon ii = new ImageIcon("/Users/yuktigirdhar/Desktop/project_images/SKELETON.png");
        image = ii.getImage();
        
        //i_width = image.getWidth(null);
        //i_height = image.getHeight(null);
        
        i_width=70;
        i_height=70;
        y=a;
        x=b;
    }
    public int get_flag(){
        
       return flag;
    }
    public void set_flag()
    {
        flag=0; // to kill whatever
    }
    public void spider_move(){
        
        y += dy;
       
    }
}


 class Board extends JPanel implements Commons {
    
    private Timer timer1;
    private Timer timer2;
    private Timer t[];
    private String message = "Game Over";
    private Ball ball;
    private Paddle paddle;
    private Brick bricks[];
    private spider spidy[];
    private boolean ingame= true;
    private int no;
     
    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {
        
        no=0;
        
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        
        spidy=new spider[N_OF_SPIDERS];
        t=new Timer[N_OF_SPIDERS];
        
        bricks = new Brick[N_OF_BRICKS];
        
        setDoubleBuffered(true);
        
        timer1 = new Timer();
        timer1.scheduleAtFixedRate(new ScheduleTask1(), DELAY, PERIOD1);
        timer2=new Timer(true);
        timer2.scheduleAtFixedRate(new ScheduleTask2(), DELAY, PERIOD2);
        
    }

    public void addNotify() {
        
        super.addNotify();
        gameInit();
    }
    
    private void gameInit() {
        
        ball = new Ball();
        paddle = new Paddle();
        
        int k = 0;
        
      for (int i = 0; i < 3; i++){
            for (int j = 0; j < 10; j++) {
                bricks[k] = new Brick(j * 120 + 100, i * 40 + 50);
                k++;
            }
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                             RenderingHints.VALUE_RENDER_QUALITY);
        
        if (ingame) {
            
            drawObjects(g2d);
        } else {
            
            gameFinished(g2d);
        }
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    private void drawObjects(Graphics2D g2d) {
        
        g2d.drawImage(ball.getImage(), ball.getX(), ball.getY(),
                      ball.getWidth(), ball.getHeight(), this);
        g2d.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),
                      paddle.getWidth(), paddle.getHeight(), this);
        
        for(int k=1;k<=no;k++)
        {
          if(spidy[k].get_flag()==1)
          {
            g2d.drawImage(spidy[k].getImage(),spidy[k].getX(),
                          spidy[k].getY(), spidy[k].getWidth(),
                          spidy[k].getHeight(), this);

          }
        }
        for (int i = 0; i < N_OF_BRICKS; i++) {
            if (!bricks[i].isDestroyed()) {
                
                g2d.drawImage(bricks[i].getImage(), bricks[i].getX(),
                              bricks[i].getY(), bricks[i].getWidth(),
                              bricks[i].getHeight(), this);
            }
        }
    }
     
     
    
    private void gameFinished(Graphics2D g2d) {
        
        Font font = new Font("Verdana", Font.BOLD, 18);
        FontMetrics metr = this.getFontMetrics(font);
        
        g2d.setColor(Color.RED);
        g2d.setFont(font);
        g2d.drawString(message,
                       (Commons.WIDTH - metr.stringWidth(message)) / 2,
                       Commons.WIDTH / 2);
    }
    
    private class TAdapter extends KeyAdapter {
        
    
        public void keyReleased(KeyEvent e) {
            paddle.keyReleased(e);
        }
        
      
        public void keyPressed(KeyEvent e) {
            paddle.keyPressed(e);
        }
    }
    
    private class ScheduleTask1 extends TimerTask {
        
      
        public void run() {
            
            ball.move();
            checkCollision();
            repaint();
        }
    }

     private class ScheduleTask2 extends TimerTask {
         
         public void run() {
             paddle.move();
             repaint();
         }
     }
     
     private class ScheduleTask3 extends TimerTask {
         
      
         public void run() {
             for(int i=1;i<=no;i++)
             {
                 if(spidy[i].get_flag()==1){
                   checkCollision2(i);
             
                   spidy[i].spider_move();
                 }
             }
             repaint();
         }
     }

    private void stopGame() {
        
        ingame = false;
        timer1.cancel();
        timer2.cancel();
    }
     
     private void checkCollision2(int k) {
         
         if (paddle.getRect2().intersects(spidy[k].getRect4())) {
             spidy[k].set_flag();
             t[k].cancel();
         }
         
         if (spidy[k].getRect4().getMaxY()> Commons.BOTTOM_EDGE+50) {
             stopGame();
        }
         
     }
     private void checkCollision() {
        
        if (ball.getRect1().getMaxY() > Commons.BOTTOM_EDGE) {
            stopGame();
        }
        
        for (int i = 0, j = 0; i < N_OF_BRICKS; i++) {
            
            if (bricks[i].isDestroyed()) {
                j++;
            }
            
            if (j == N_OF_BRICKS) {
                message = "Victory";
                stopGame();
            }
        }
        
        if ((ball.getRect1()).intersects(paddle.getRect2())) {
            
            int paddleLPos = (int) paddle.getRect2().getMinX();
            int ballLPos = (int) ball.getRect1().getMinX();
            
            int first = paddleLPos + 40;
            int second = paddleLPos + 80;
            int third = paddleLPos + 120;
            int fourth = paddleLPos + 160;
            
            if (ballLPos < first) {
                ball.setXDir(-1);
                ball.setYDir(-1);
            }
            
            if (ballLPos >= first && ballLPos < second) {
                ball.setXDir(-1);
                ball.setYDir(-1 );//* ball.getYDir());
            }
        
            if (ballLPos >= second && ballLPos < third) {
                ball.setXDir(0);
                ball.setYDir(-1);
            }
            
            if (ballLPos >= third && ballLPos < fourth) {
                ball.setXDir(1);
                ball.setYDir(-1 ); //* ball.getYDir());
            }
            
            if (ballLPos > fourth) {
                ball.setXDir(1);
                ball.setYDir(-1);
            }
        }
        
        for (int i = 0; i < N_OF_BRICKS; i++) {
            
            if ((ball.getRect1()).intersects(bricks[i].getRect3())) {
                
                int ballLeft = (int) ball.getRect1().getMinX();
                int ballHeight = (int) ball.getRect1().getHeight();
                int ballWidth = (int) ball.getRect1().getWidth();
                int ballTop = (int) ball.getRect1().getMinY();
                
                Point pointRight = new Point(ballLeft + ballWidth + 1, ballTop);
                Point pointLeft = new Point(ballLeft - 1, ballTop);
                Point pointTop = new Point(ballLeft, ballTop - 1);
                Point pointBottom = new Point(ballLeft, ballTop + ballHeight + 1);
                
                if (!bricks[i].isDestroyed()) {
                    if (bricks[i].getRect3().contains(pointRight)) {
                        ball.setXDir(-1);
                    } else if (bricks[i].getRect3().contains(pointLeft)) {
                        ball.setXDir(1);
                    }
                    
                    if (bricks[i].getRect3().contains(pointTop)) {
                        ball.setYDir(1);
                    } else if (bricks[i].getRect3().contains(pointBottom)) {
                        ball.setYDir(-1);
                    }
                    
                    bricks[i].setDestroyed(true);
                    
                    if(i==0||i==4||i==9||i==10||i==15||i==16||i==17||i==29)
                    {
                         no+=1;
                         spidy[no]=new spider();
                         spidy[no].initial(bricks[i].getY(),bricks[i].getX());
                         t[no]=new Timer(true);
                         t[no].scheduleAtFixedRate(new ScheduleTask3(), 100, PERIOD3);
                    }
                }
            }
        }
    }
}

public class breakout extends JFrame {
    
    public breakout() {
        initUI();
    }
    private void initUI() {
        
        add(new Board());
        setTitle("Breakout");
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(Commons.WIDTH, Commons.HEIGTH);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                breakout game = new breakout();
                game.setVisible(true);
            }
        });
    }
}






