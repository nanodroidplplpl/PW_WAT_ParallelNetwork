package Siec;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.*;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    /* Obiekt zamka */
    private final Lock lock = new ReentrantLock();
    /* Tablica do zajmowania urzadzen */
    private int ile_urzadzen_w_grupie = 2;
    private int ile_grup_urzadzen = 2;
    private Boolean[][] urzadzenia = new Boolean[ile_grup_urzadzen][ile_urzadzen_w_grupie];
    /* Tablica do blokowania swojej grupy */
    private int ile_grup_stacji_roboczych = 2;
    //private int ile_stacji_roboczych = 2;
    private Condition[][] grupa = new Condition[ile_grup_urzadzen][ile_grup_stacji_roboczych];
    /* Blokady poszczegulnych grup urzadzen */
    private Condition[] urzadzenie = new Condition[ile_grup_urzadzen];
    /* grupy w urzadzeniu */
    private Boolean[][] grupy_w_urzadzeniu = new Boolean[ile_grup_stacji_roboczych][ile_urzadzen_w_grupie];

    /* Zmienne pomocnicze */
    Boolean zajete = false;
    int j;

    /* Funkcja do ustalenia parametrow */
    public void init() {
        /* Init dla zajetosci urzadzen */
        for ( int i = 0; i < ile_grup_urzadzen; i++ ) {
            for ( int j = 0; j < ile_urzadzen_w_grupie; j++ ) {
                urzadzenia[i][j] = false;
            }
        }
        /* Init dla blokowania grup */
        for ( int i = 0; i < ile_grup_urzadzen; i++ ) {
            for ( int j = 0; j < ile_grup_stacji_roboczych; j++ ) {
                grupa[i][j] = lock.newCondition();
            }
        }
        /* Init dla blokowania grup urzadzen */
        for ( int i = 0; i < ile_grup_urzadzen; i++ ) {
            urzadzenie[i] = lock.newCondition();
        }

        for ( int i = 0; i < ile_grup_stacji_roboczych; i++ ) {
            for ( int j = 0; j < ile_urzadzen_w_grupie; j++ ) {
                grupy_w_urzadzeniu[i][j] = false;
            }
        }
    }

    public int uzyskaj_dostep( String nazwa, int co_bierzesz, int gr, int nr_powt ) throws InterruptedException {
        lock.lock();
        try {
            j = 0;
            while ( grupy_w_urzadzeniu[co_bierzesz][gr] ) {
                grupa[co_bierzesz][gr].await();
            }
            zajete = true;
            for ( int i = 0; i < ile_urzadzen_w_grupie; i++ ) {
                if ( !urzadzenia[co_bierzesz][i] ) {
                    zajete = false;
                    j = i;
                    break;
                }
            }
            // Jezeli wszystkie skanery sa zajete to czekanie na odp
            while ( zajete ) {
                urzadzenie[co_bierzesz].await();
                for ( int i = 0; i < ile_urzadzen_w_grupie; i++ ) {
                    if ( !urzadzenia[co_bierzesz][i] ) {
                        j = i;
                        break;
                    }
                }
            }
            // Teraz przydzielone wszystkie dostepy
            grupy_w_urzadzeniu[co_bierzesz][gr] = true;
            urzadzenia[co_bierzesz][j] = true;
            System.out.println("["+nazwa+","+nr_powt+"] >> biore "+j+" stan urzadzenia nr {"+co_bierzesz+"} ["+urzadzenia[co_bierzesz][0]+","+urzadzenia[co_bierzesz][1]+"] moja grupa "+gr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return j;
    }

    public void zwolnij_zasob( String nazwa, int co_bierzesz, int gr, int nr_powt, int ktore ) throws InterruptedException {
        lock.lock();
        try{
            grupy_w_urzadzeniu[co_bierzesz][gr] = false;
            urzadzenia[co_bierzesz][ktore] = false;
            grupa[co_bierzesz][gr].signal();
            urzadzenie[co_bierzesz].signal();
            System.out.println("["+nazwa+","+nr_powt+"] <<< urzadzenie "+ktore+" stan {"+co_bierzesz+"} ["+urzadzenia[co_bierzesz][0]+","+urzadzenia[co_bierzesz][1]+"]");
        } finally {
            lock.unlock();
        }
    }

}
