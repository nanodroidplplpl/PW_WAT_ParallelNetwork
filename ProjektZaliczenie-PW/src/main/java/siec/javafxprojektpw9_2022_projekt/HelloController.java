package siec.javafxprojektpw9_2022_projekt;

import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
//import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.Duration;


public class HelloController {

    //in the Controller class
    @FXML
    private Slider ile_stacji_roboczych_w_grupie;
    @FXML
    private Slider ile_grup_stacji_roboczych;
    @FXML
    private Slider ile_urzadzen_w_grupie;
    @FXML
    private Slider ile_grup_urzadzen;
    @FXML
    private Text textpierw;
    @FXML
    private Text textdwa;
    @FXML
    private Text texttrzy;
    @FXML
    private Text textcztery;
    @FXML
    private AnchorPane animacja = new AnchorPane();

    int fasterr = 4;
    int N = 9;


    int ile_stacji_roboczych_w_gr;
    int ile_gr_stacji_roboczych;
    int ile_urzadzen_w_gr;
    int ile_gr_urzadzen;

    private int[][][] kolaX = new int[3][3][2]; //0-x 1-y

    @FXML
    private Rectangle[][] Urzadzenia = new Rectangle[3][3];
    @FXML
    private Rectangle[][] StacjeRobocze = new Rectangle[3][3];

    Thread[][] sta = new StacjaRobocza[3][3];

    public void initialize() {
        ile_stacji_roboczych_w_gr = 2;
        ile_gr_urzadzen = 2;
        ile_urzadzen_w_gr = 2;
        ile_gr_stacji_roboczych = 2;
    }

    @FXML
    public void drawing() {
        int xStacjeDzielenie = 500/(ile_gr_stacji_roboczych+1);
        int xUrzadzeniaDzielenie = 500/(ile_gr_urzadzen+1);
        int yStacje = 100;
        int yUrzadzenia = 400;
        int xlayoutStacje = -40;
        int xlayoutUrzadzenia = -50;
        int stala = xStacjeDzielenie;
        for (int i = 0; i < ile_gr_stacji_roboczych; i++) {
            for (int j = 0; j < ile_stacji_roboczych_w_gr; j++) {
                StacjeRobocze[i][j] = new Rectangle();
                StacjeRobocze[i][j].setHeight(40);
                StacjeRobocze[i][j].setWidth(40);
                StacjeRobocze[i][j].setFill(Color.LIGHTGRAY);
                StacjeRobocze[i][j].setStroke(Color.BLACK);
                StacjeRobocze[i][j].setLayoutX(stala+xlayoutStacje);
                StacjeRobocze[i][j].setLayoutY(yStacje);
                animacja.getChildren().add(StacjeRobocze[i][j]);
                xlayoutStacje+=40;
            }
            //xStacjeDzielenie*=2;
            stala+=xStacjeDzielenie;
        }
        stala=xUrzadzeniaDzielenie;

        for (int i = 0; i < ile_gr_urzadzen; i++) {
            for (int j = 0; j < ile_urzadzen_w_gr; j++) {
                Urzadzenia[i][j] = new Rectangle();
                Urzadzenia[i][j].setWidth(50);
                Urzadzenia[i][j].setHeight(50);
                Urzadzenia[i][j].setStroke(Color.BLACK);
                Urzadzenia[i][j].setFill(Color.GRAY);
                Urzadzenia[i][j].setLayoutX(stala+xlayoutUrzadzenia);
                Urzadzenia[i][j].setLayoutY(yUrzadzenia);
                animacja.getChildren().add(Urzadzenia[i][j]);
                xlayoutUrzadzenia+=50;
            }
            stala+=xUrzadzeniaDzielenie;
        }
    }

    @FXML
    public void onSliderStacjeRoboczeChanged() {
        int sliderValue = (int) ile_stacji_roboczych_w_grupie.getValue();
        textpierw.setText("Ile stacji roboczych: "+sliderValue);
        ile_stacji_roboczych_w_gr = sliderValue;
        System.out.println(sliderValue + " "); // zbieranie informacji ze slajdera 1
        //txtOut.setText(escritoresQuant.getValue()+" ");
    }

    @FXML
    public void onSliderGrupyStacjiRoboczychChanged() {
        int sliderValue = (int) ile_grup_stacji_roboczych.getValue();
        ile_gr_stacji_roboczych = sliderValue;
        textdwa.setText("Ile grup stacji roboczych: "+sliderValue);
        System.out.println(sliderValue + " ");//zbieranie informacji ze slajdera 2
    }

    @FXML
    public void onSliderUrzadzeniaChanged() {
        int sliderValue = (int) ile_urzadzen_w_grupie.getValue();
        ile_urzadzen_w_gr = sliderValue;
        texttrzy.setText("Ile urzadzen: "+sliderValue);
        System.out.println(sliderValue + " ");
    }

    @FXML
    public void onSliderGrupyUrzadzenChanged() {
        int sliderValue = (int) ile_grup_urzadzen.getValue();
        ile_gr_urzadzen = sliderValue;
        textcztery.setText("Ile grop urzadzen: "+sliderValue);
        System.out.println(sliderValue + " ");
    }

    @FXML
    public void onHelloButtonClick() throws InterruptedException {
        System.out.println("Hello");// zaslepka na start animacji
        //myThread thr = new myThread(animacja, StacjeRobocze[0][0], Urzadzenia[0][0]);
        //myThread thrr = new myThread(animacja, StacjeRobocze[0][1], Urzadzenia[0][1]);
        //thr.start();
        //thrr.start();
        Server server = new Server(ile_urzadzen_w_gr, ile_gr_urzadzen, ile_gr_stacji_roboczych);//int ile_urzadzen_w_grupie,int ile_grup_urzadzen,int ile_grup_stacji_roboczych
        server.init();
        /*Thread w1 = new StacjaRobocza(1,0,server,3, animacja, StacjeRobocze[0][0], Urzadzenia);//int numer, int grupa, Server server, int N, AnchorPane animacja, Rectangle mojaStacja, Rectangle[][] urzadzenia
        Thread w2 = new StacjaRobocza(2,0,server,3, animacja, StacjeRobocze[0][1], Urzadzenia);
        Thread w3 = new StacjaRobocza(3,1,server,3, animacja, StacjeRobocze[1][0], Urzadzenia);
        Thread w4 = new StacjaRobocza(4,1,server,3, animacja, StacjeRobocze[1][1], Urzadzenia);*/
        int numer = 1;
        for (int i = 0; i < ile_gr_stacji_roboczych; i++) {
            for (int j = 0; j < ile_stacji_roboczych_w_gr; j++) {
                sta[i][j] = new StacjaRobocza(numer, i, server, N, animacja, StacjeRobocze[i][j], Urzadzenia, ile_gr_urzadzen, fasterr);
                numer++;
            }
        }

        /*w1.start();
        w2.start();
        w3.start();
        w4.start();*/

        for (int i = 0; i < ile_gr_stacji_roboczych; i++) {
            for (int j = 0; j < ile_stacji_roboczych_w_gr; j++) {
                sta[i][j].start();
            }
        }

        /*try {
            w1.join();
            w2.join();
            w3.join();
            w4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println("Koniec\n");
    }

    int predkosc = 1;

    @FXML
    public void faster() {
        if (predkosc%2 == 1) {
            fasterr = 10;
            N = 9;
            predkosc++;
        }
        else
        {
            fasterr = 4;
            N = 3;
            predkosc++;
        }
    }

    @FXML
    public void szybciej() {
        System.out.println("Gotowe");
        ile_stacji_roboczych_w_grupie.setDisable(true);
        ile_grup_stacji_roboczych.setDisable(true);
        ile_urzadzen_w_grupie.setDisable(true);
        ile_grup_urzadzen.setDisable(true);
        drawing();
    }

    @FXML
    public void kasowanie() {
        for (int i = 0; i < ile_gr_stacji_roboczych; i++) {
            for (int j = 0; j < ile_stacji_roboczych_w_gr; j++) {
                animacja.getChildren().remove(StacjeRobocze[i][j]);
            }
        }

        for (int i = 0; i < ile_gr_stacji_roboczych; i++) {
            for (int j = 0; j < ile_stacji_roboczych_w_gr; j++) {
                animacja.getChildren().remove(Urzadzenia[i][j]);
            }
        }

        ile_stacji_roboczych_w_grupie.setDisable(false);
        ile_grup_stacji_roboczych.setDisable(false);
        ile_urzadzen_w_grupie.setDisable(false);
        ile_grup_urzadzen.setDisable(false);
    }
}