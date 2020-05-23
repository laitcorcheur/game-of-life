import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Scanner;    

public class Main extends JPanel{
    private final JFrame frame;

    private final int width, height;

    private boolean[][] cells;

    private int tour, fps;

    public Main(int width, int height, int n){
        this.width = width;
        this.height = height;

        cells = new boolean[width][height];

        Scanner sc  = new Scanner(System.in);
        generateRandomCells(n);

        frame = new JFrame("Game Of Life");
        frame.setSize(720, 480);
        // barre reduire agrandir fermer
        frame.setUndecorated(true);
        frame.setResizable(true);
        // null = fenetre au centre 
        frame.setLocationRelativeTo(null);

        // redefini le JFrame pour afficher JPanel
        frame.setContentPane(this);
        frame.setBackground(Color.LIGHT_GRAY);

        frame.setVisible(true);

        run();
    }

    private void generateRandomCells(int n){
        Random random = new Random();

        while (n > 0){
            int rx = random.nextInt(width);
            int ry = random.nextInt(height);

            if(cells[rx][ry]) continue;

            cells[rx][ry] = true;
            n--;
        }
    }

    private void run(){
        long nanoSecond = System.nanoTime();
        double tick = 1000000000.0 / 5.0;


        int tps = 0;

        long lastTime = System.currentTimeMillis();

        while(true){
            if(System.nanoTime() - nanoSecond > tick){
                nanoSecond+=tick;
                tps++;
                update();
            }
            else{
                frame.repaint();
            }

            // raz toutes les sec
            if(System.currentTimeMillis() - lastTime >= 1000){
                lastTime = System.currentTimeMillis();
                System.out.println(tps+" TPS - "+fps+" FPS");
                tps = 0;
                fps = 0;

                // force l execution et libere la mémoire
                // System.gc();
            }
        }
    }

    private void update(){
        boolean[][] newCells = new boolean[width][height];

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                int count = 0;

                for(int xo = -1; xo < 2; xo++){
                    for(int yo = -1; yo < 2; yo++){
                        // eviter sa propre case
                        if(xo == 0 && yo == 0) continue;
                        int nx = x +xo;
                        int ny = y + yo;
                        count += (nx >= 0 && ny > 0 && nx < width && ny < height && cells[nx][ny]) ? 1 : 0;
                                                            // si est dans le tableau vraie alors 1 sinon 0
                    }
                }

                newCells[x][y] = cells[x][y] ? (count == 2 || count == 3) : count == 3;
            }
        }

        cells = newCells;
        tour++;
    }

    protected void paintComponent(Graphics g){
        fps++;

        // grille par rapport à la taille de la fenetre
        int xOffset = 1100 / width;
        int yOffset = 800 / height;

        g.setColor(Color.white);

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                if(cells[x][y])
                    g.fillRect(x * xOffset, y * yOffset, xOffset, yOffset);
            }
        }

        g.setColor(Color.YELLOW);
        g.setFont(new Font("arial", Font.PLAIN, 15));
        g.drawString(""+tour, 20, 20);

    }
    //  tableau de parametres string
    public static void main(String... args){
        // new Main(100, 50, 50);
        new Main(300, 250, 5000);
    }

    public boolean getCells(int x, int y){
        return cells[x][y];
    }

    /*public double getTick(){
    	return this.tick;
    }
    public void setTick(double tick){
    	this.tick=tick;
    }*/
}