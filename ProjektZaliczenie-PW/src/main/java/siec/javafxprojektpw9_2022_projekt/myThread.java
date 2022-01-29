package siec.javafxprojektpw9_2022_projekt;

import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class myThread extends Thread {
    AnchorPane animacja;
    Rectangle urzadzenie;
    Rectangle stacjaRobocza;
    int ile_urzadzen;
    public myThread(AnchorPane animacja, Rectangle stacjaRobocza, Rectangle urzadzenie, int ile_urzodzen) {
        this.animacja = animacja;
        this.stacjaRobocza = stacjaRobocza;
        this.urzadzenie = urzadzenie;
        this.ile_urzadzen = ile_urzodzen;
    }

    @Override
    public void run() {
        /*--------------Tworzenie---------------*/
        Circle circle = new Circle();
        circle.setCenterX(20+stacjaRobocza.getLayoutX());
        circle.setCenterY(20+stacjaRobocza.getLayoutY());
        circle.setRadius(0);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.RED);

        Platform.runLater(() -> {
            animacja.getChildren().add(circle);
        });
        /*--------------Powiekszanie---------------*///0->20
        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(1000));
        scaleTransition.setNode(circle);
        scaleTransition.setByX(40);
        scaleTransition.setByY(40);
        Platform.runLater(()->{
          scaleTransition.play();
        });
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*--------------Linia---------------*/
        Line line = new Line();
        line.setStartX(20+stacjaRobocza.getLayoutX());
        line.setStartY(20+stacjaRobocza.getLayoutY());
        line.setEndX(25+urzadzenie.getLayoutX());
        line.setEndY(25+urzadzenie.getLayoutY());
        Platform.runLater(() -> {
            animacja.getChildren().add(line);
        });

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*--------------Przesuwanie---------------*/
        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.setX(circle.getCenterX());
        moveTo.setY(circle.getCenterY());
        LineTo lineTo = new LineTo();
        lineTo.setX(25+urzadzenie.getLayoutX());
        lineTo.setY(25+urzadzenie.getLayoutY());
        path.getElements().addAll(moveTo,lineTo);
        PathTransition pathTransition = new PathTransition(Duration.millis(2000), path, circle);
        System.out.println("Puszczam animacje");
        Platform.runLater(() -> {
            pathTransition.play();
        });

        try {
            sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*--------------Powrot---------------*/
        Path backPath = new Path();
        MoveTo backMoveToo = new MoveTo();
        backMoveToo.setX(25+urzadzenie.getLayoutX());
        backMoveToo.setY(25+urzadzenie.getLayoutY());
        LineTo BacklineToo = new LineTo();
        BacklineToo.setX(20+stacjaRobocza.getLayoutX());
        BacklineToo.setY(20+stacjaRobocza.getLayoutY());
        backPath.getElements().addAll(backMoveToo,BacklineToo);
        PathTransition backPathTransitionn = new PathTransition(Duration.millis(2000), backPath, circle);
        System.out.println("Puszczam animacje");
        Platform.runLater(() -> {
            backPathTransitionn.play();
        });

        try {
            sleep(2000);
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
            animacja.getChildren().remove(line);
        });
        /*--------------Znikanie---------------*/
        ScaleTransition puff = new ScaleTransition();
        puff.setDuration(Duration.millis(1000));
        puff.setNode(circle);
        puff.setByX(-40);
        puff.setByY(-40);
        puff.setAutoReverse(true);
        puff.setCycleCount(1);
        Platform.runLater(() -> {
            puff.play();
        });
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> {
            animacja.getChildren().remove(circle);
            animacja.getChildren().remove(puff);
        });
    }
}
