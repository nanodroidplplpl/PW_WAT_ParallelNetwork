package siec.javafxprojektpw9_2022_projekt;

import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

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
    AnchorPane animacja;
    Rectangle mojaStacja;
    Rectangle[][] urzadzenia = new Rectangle[3][3];
    int ile_urzodzen;
    int szybciej;

    StacjaRobocza(int numer, int grupa, Server server, int N, AnchorPane animacja, Rectangle mojaStacja, Rectangle[][] urzadzenia, int ile_urzodzen, int szybciej) {
        super("W"+numer);
        this.grupa = grupa;
        this.server = server;
        this.N = N;
        this.animacja = animacja;
        this.mojaStacja = mojaStacja;
        this.urzadzenia = urzadzenia;
        this.ile_urzodzen = ile_urzodzen;
        this.szybciej = szybciej;
    }

    Random r = new Random();
    Random rrr = new Random();
    Random R = new Random();
    public void run() {
        /*--------------Done---------------*/
        N = rrr.nextInt(N)+1;
        for ( int i = 1; i <= N; i++ ) {
            try {
                int RR = R.nextInt(5)+1;
                /*--------------Tworzenie---------------*/
                Circle circle = new Circle();
                circle.setCenterX(20+mojaStacja.getLayoutX());
                circle.setCenterY(20+mojaStacja.getLayoutY());
                circle.setRadius(0);
                circle.setStroke(Color.BLACK);
                circle.setFill(Color.RED);

                Platform.runLater(() -> {
                    animacja.getChildren().add(circle);
                });
                /*--------------Sprawy wlasne---------------*/
                /*--------------Powiekszanie---------------*///0->20
                ScaleTransition scaleTransition = new ScaleTransition();
                scaleTransition.setDuration(Duration.millis(1000/szybciej));
                scaleTransition.setNode(circle);
                scaleTransition.setByX(40);
                scaleTransition.setByY(40);
                Platform.runLater(()->{
                    scaleTransition.play();
                });

                try {
                    sleep(1000/szybciej);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*--------------Done---------------*/

                rr = r.nextInt(ile_urzodzen);//bylo 2
                LR = server.uzyskaj_dostep(getName(), rr, grupa, i); // String nazwa, int co_bierzesz, int gr, int nr_powt
                /*--------------Linia---------------*/
                System.out.println(rr+" "+LR);
                Line line = new Line();
                line.setStartX(20+mojaStacja.getLayoutX());
                line.setStartY(20+mojaStacja.getLayoutY());
                line.setEndX(25+urzadzenia[rr][LR].getLayoutX());
                line.setEndY(25+urzadzenia[rr][LR].getLayoutY());
                Platform.runLater(() -> {
                    animacja.getChildren().add(line);
                });
                // LR jezeli 0 to lewy jezeli 1 to prawy
                /*--------------Przesuwanie---------------*/
                Path path = new Path();
                MoveTo moveTo = new MoveTo();
                moveTo.setX(circle.getCenterX());
                moveTo.setY(circle.getCenterY());
                LineTo lineTo = new LineTo();
                lineTo.setX(25+urzadzenia[rr][LR].getLayoutX());
                lineTo.setY(25+urzadzenia[rr][LR].getLayoutY());
                path.getElements().addAll(moveTo,lineTo);
                PathTransition pathTransition = new PathTransition(Duration.millis((2000*RR)/szybciej), path, circle);
                System.out.println("Puszczam animacje");
                Platform.runLater(() -> {
                    pathTransition.play();
                });

                try {
                    sleep((4000*RR)/szybciej);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //sleep(r.nextInt(5));
                //System.out.println("Moje urzo to "+LR+" jestem "+getName());

                server.zwolnij_zasob(getName(), rr, grupa, i, LR); // String nazwa, int co_bierzesz, int gr, int nr_powt, int ktore
                /*--------------Powrot---------------*/
                Path backPath = new Path();
                MoveTo backMoveToo = new MoveTo();
                backMoveToo.setX(25+urzadzenia[rr][LR].getLayoutX());
                backMoveToo.setY(25+urzadzenia[rr][LR].getLayoutY());
                LineTo BacklineToo = new LineTo();
                BacklineToo.setX(20+mojaStacja.getLayoutX());
                BacklineToo.setY(20+mojaStacja.getLayoutY());
                backPath.getElements().addAll(backMoveToo,BacklineToo);
                PathTransition backPathTransitionn = new PathTransition(Duration.millis((2000*RR)/szybciej), backPath, circle);
                System.out.println("Puszczam animacje");
                Platform.runLater(() -> {
                    backPathTransitionn.play();
                });

                try {
                    sleep((2000*RR)/szybciej);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    animacja.getChildren().remove(scaleTransition);
                    animacja.getChildren().remove(backPathTransitionn);
                    animacja.getChildren().remove(moveTo);
                    animacja.getChildren().remove(path);
                    animacja.getChildren().remove(lineTo);
                    animacja.getChildren().remove(backPath);
                    animacja.getChildren().remove(backPathTransitionn);
                    animacja.getChildren().remove(backMoveToo);
                    animacja.getChildren().remove(BacklineToo);
                    animacja.getChildren().remove(line);
                });

                /*--------------Znikanie---------------*/
                ScaleTransition puff = new ScaleTransition();
                puff.setDuration(Duration.millis((1000*RR)/szybciej));
                puff.setNode(circle);
                puff.setByX(-40);
                puff.setByY(-40);
                puff.setAutoReverse(true);
                puff.setCycleCount(1);
                Platform.runLater(() -> {
                    puff.play();
                });
                try {
                    sleep((1000*RR)/szybciej);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    animacja.getChildren().remove(circle);
                    animacja.getChildren().remove(puff);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}