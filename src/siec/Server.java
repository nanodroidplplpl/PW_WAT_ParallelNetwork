package siec;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.*;
import java.util.concurrent.locks.Condition;
import java.util.Random;

/* Klasa servera jest monitorem ktory przydziela dostep do urzadzen peryferyjnych */
public class Server {
    /* Obiekta zamka */
    private final Lock lock = new ReentrantLock();

    /* Tablice i miejsca w nich sa instancjami wirtualnymi urzadzen
       peryferyjnych. Jezeli true to oznacza ze jest dostep do tablicy,
       jezeli false to oznacza ze uzadzenie o intex i+1 jest zajete. */
    private Boolean[] skanery = new Boolean[]{false, false};
    private Boolean[] drukarki = new Boolean[]{false, false};

    /* Tablice zmiennych warunkowych jest tablica odpowiedzialna
       za blokowanie odpowiednich elementow w tablicy urzadzen. */
    Condition skaner1 = lock.newCondition();
    Condition skaner2 = lock.newCondition();
    Condition drukarka1 = lock.newCondition();
    Condition drukarka2 = lock.newCondition();
    Condition[] skaneryLock = new Condition[]{skaner1, skaner2};
    Condition[] drukarkiLock = new Condition[]{drukarka1, drukarka2};

    /* Zmienne warunkowe dla grup */
    Condition g1 = lock.newCondition();
    Condition g2 = lock.newCondition();
    Condition[] g = new Condition[]{g1, g2};

    /* Ponizej zmienne okreslajace z jakiej grupy stacje robocze sa w zasobach */
    private int skan = 5;
    private int druk = 5;

    private int peryferium = 0;

    private int[] w12 = new int[]{0,0};
    private int[] w23 = new int[]{0,0};

    /* Funcka ponizej prosi od dostep do zasobow zmienna gropa pozwala
       na identyfikacje odpowiedniej grupy zasobow, ktora ma prawo do
       danego zasobu. Zmienna rodzaj_urzadzenia okresla o jakim zasobie mowa */
    Random r = new Random();
    public int uzyskaj_dostep( int rodzaj_urzadzenia, int grupa, int urzadzenie, String nazwa, int i, int numer ) throws InterruptedException {
        lock.lock();
        try {
            // Sprawdzenie ktore urzadzenie bedzie urzyte
            if ( rodzaj_urzadzenia == 1) {
                // Poproszono o skaner
                // Sprawdzenie czy jest wolny skaner
                urzadzenie = 0;
                if ( skan == grupa ) {
                    //g[grupa - 1].await();
                    System.out.println(nazwa+" dupa");
                    g[grupa-1].await();
                }
                skan = grupa;
                /* Po zwolnieniu zamka nastepuje ponowne sprawdzenie ktory skaner
                   jest wolny */
                System.out.println(nazwa+" waru1 ");
                if (skanery[urzadzenie]) {
                    skaneryLock[urzadzenie].await();
                    urzadzenie++;
                    urzadzenie = urzadzenie % 2;
                    if (skanery[urzadzenie])
                    {
                        skaneryLock[urzadzenie].await();
                    }
                }
                System.out.println(nazwa+" warun2 ");
                /* Po uzystaniu wszystkich dostepow nastepuje kozystanie z zasobu */
                skanery[urzadzenie] = true;
                System.out.println("[" + nazwa + "," + i + "]" + ">> skaner " + urzadzenie +" zajetosc skanerow ["+skanery[0]+","+skanery[1]+"]");
                //skan = 5;
                //skanery[urzadzenie] = false;
                //sleep(r.nextInt(10));
                // Zostawia po sobie informacje co zmienil 1-skaner 2-drukarke oraz ktora pare
                //ktore_zmienilem[numer];
                //g[grupa - 1].signal();
            } else {
                // Poproszono o drukarke
                // Sprawdzenie ktore urzadzenie bedzie urzyte
                if (rodzaj_urzadzenia != 1) {
                /* Kiedy znaleziono wolny skaner, sprawdzenie czy ktos z tej
                    samej grupy jest w skanerach i oczekiwanie jezeli tak jest */
                    if (druk == grupa) {
                        //g[grupa - 1].await();
                        //wait();
                        System.out.println(nazwa+"dupa");
                        g[grupa-1].await();
                    }
                    druk = grupa;
                /* Po zwolnieniu zamka nastepuje ponowne sprawdzenie ktory skaner
                   jest wolny */
                    System.out.println(nazwa+" warun1 ");
                    if (drukarki[urzadzenie]) {
                        urzadzenie++;
                        urzadzenie = urzadzenie % 2;
                        drukarkiLock[urzadzenie].await();
                        if (drukarki[urzadzenie])
                        {
                            drukarkiLock[urzadzenie].await();
                        }
                       // System.out.println("Sprawdzam"+urzadzenie);
                    }
                    System.out.println(nazwa+" warun2 ");
                    /* Po uzystaniu wszystkich dostepow nastepuje kozystanie z zasobu */
                    drukarki[urzadzenie] = true;
                    System.out.println("[" + nazwa + "," + i + "]" + ">> drukarka " + urzadzenie +" zajetosc drukarek ["+drukarki[0]+","+drukarki[1]+"]");
                    //drukarki[urzadzenie] = false;
                    //sleep(r.nextInt(10));
                    //g[grupa - 1].signal();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return urzadzenie;
    }

    /* Ponizsza funkcja opuszcza zajete urzadzenie przez odpowiedni proces */
    public void zakoncz_prace( int LR, int rodzaj_urzadzenia, int grupa, int urzadzenie, String nazwa, int i, int numer ) throws InterruptedException {
        lock.lock();
        // dla skanerow
        if (rodzaj_urzadzenia == 1)
        {
            skanery[LR] = false;
            skan = 5;
            skaneryLock[urzadzenie].signal();
            g[grupa - 1].signal();
            System.out.println("[" + nazwa + "," + i + "]" + "<<< skaner " + urzadzenie +" zajetosc skanerow ["+skanery[0]+","+skanery[1]+"]");
        }
        else
        {
            drukarki[LR] = false;
            druk = 5;
            drukarkiLock[urzadzenie].signal();
            g[grupa - 1].signal();
            System.out.println("[" + nazwa + "," + i + "]" + "<<< druk " + urzadzenie +" zajetosc drukarek ["+drukarki[0]+","+drukarki[1]+"]");
        }
        lock.unlock();
    }
}
