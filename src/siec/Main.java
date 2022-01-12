package Siec;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.init();

        Thread w1 = new StacjaRobocza(1, 0, server, 100);
        Thread w2 = new StacjaRobocza(2, 0, server, 100);
        Thread w3 = new StacjaRobocza(3, 1, server, 100);
        Thread w4 = new StacjaRobocza(4, 1, server, 100);

        w1.start();
        w2.start();
        w3.start();
        w4.start();

        try {
            w1.join();
            w2.join();
            w3.join();
            w4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Koniec\n");
    }
}
