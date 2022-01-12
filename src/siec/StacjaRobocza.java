package Siec;
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
        for ( int i = 1; i <= N; i++ ) {
            try {
                rr = r.nextInt(2)+1;
                LR = server.uzyskaj_dostep(getName(), rr, grupa, i); // String nazwa, int co_bierzesz, int gr, int nr_powt
                // LR jezeli 0 to lewy jezeli 1 to prawy
                sleep(r.nextInt(5));
                //System.out.println("Moje urzo to "+LR+" jestem "+getName());
                server.zwolnij_zasob(getName(), rr, grupa, i, LR); // String nazwa, int co_bierzesz, int gr, int nr_powt, int ktore
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

