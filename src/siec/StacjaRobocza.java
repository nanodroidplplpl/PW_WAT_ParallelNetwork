package siec;
import javax.swing.plaf.InternalFrameUI;
import java.util.Random;

public class StacjaRobocza extends Thread {
    private int grupa;
    private int rodzaj;
    //String nazwa;
    private int N;
    private Server server;
    private int numer;
    int LR;
    int rr;

    StacjaRobocza( int numer, int grupa, Server server, int N ) {
        super("W"+numer);
        this.grupa = grupa;
        this.server = server;
        this.N = N;
    }

    Random r = new Random();
    public void run() {
        for ( int i = 0; i < N; i++ ) {
            try {
                rr = r.nextInt(2);
                LR = server.uzyskaj_dostep(rr, grupa, 0, getName(), i, numer);
                // LR jezeli 0 to lewy jezeli 1 to prawy
                sleep(5);
                server.zakoncz_prace(LR,rr, grupa, 0, getName(), i, numer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
