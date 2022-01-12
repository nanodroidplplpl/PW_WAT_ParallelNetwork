package Siec;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.*;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    /* Obiekt zamka */
    private final Lock lock = new ReentrantLock();
    private Boolean[] skanery = new Boolean[]{false, false};
    private Boolean[] drukarki = new Boolean[]{false, false};
    Condition gr_1 = lock.newCondition();
    Condition gr_2 = lock.newCondition();
    private Condition[] grupa = new Condition[]{gr_1, gr_2};
    private Boolean[] grupy_w_skan = new Boolean[]{false, false};
    private Boolean[] grupy_w_druk = new Boolean[]{false, false};
    private Condition skaner = lock.newCondition();
    private Condition drukarka = lock.newCondition();

    Boolean zajete = false;
    int j;

    public int uzyskaj_dostep(String nazwa, int co_bierzesz, int gr, int nr_powt) throws InterruptedException { // grupa jest 0 i 1
        lock.lock();
        j = 0;
        if ( co_bierzesz == 1) {
            try {
                while ( grupy_w_skan[gr] ) {
                    grupa[gr].await();
                }
                zajete = true;
                for ( int i = 0; i < 2; i++ )
                {
                    if ( !skanery[i] )
                    {
                        zajete = false;
                        j = i;
                        break;
                    }
                }
                // Jezeli wszystkie skanery sa zajete to czekanie na odp
                while ( zajete )
                {
                    skaner.await();
                    for ( int i = 0; i < 2; i++ )
                    {
                        if ( !skanery[i] )
                        {
                            j = i;
                            break;
                        }
                    }
                }
                // Teraz sa przydzielone wszystkie niezbedne dostepy wchodze do obszaru krytycznego
                grupy_w_skan[gr] = true;
                skanery[j] = true;
                System.out.println("["+nazwa+","+nr_powt+"] >> skaner "+j+" stan skanerow ["+skanery[0]+","+skanery[1]+"] moja grupa "+gr);//+", grupy to ["+grupy_w_skan[0]+","+grupy_w_skan[1]+"]");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        } else {
            try {
                while ( grupy_w_druk[gr] ) {
                    grupa[gr].await();
                }
                zajete = true;
                for ( int i = 0; i < 2; i++ )
                {
                    if ( !drukarki[i] )
                    {
                        zajete = false;
                        j = i;
                        break;
                    }
                }
                // Jezeli wszystkie skanery sa zajete to czekanie na odp
                while ( zajete )
                {
                    drukarka.await();
                    for ( int i = 0; i < 2; i++ )
                    {
                        if ( !drukarki[i] )
                        {
                            j = i;
                            break;
                        }
                    }
                }
                // Teraz sa przydzielone wszystkie niezbedne dostepy wchodze do obszaru krytycznego
                grupy_w_druk[gr] = true;
                drukarki[j] = true;
                System.out.println("["+nazwa+","+nr_powt+"] >> drukarka "+j+" stan drukarek ["+drukarki[0]+","+drukarki[1]+"] moja grupa "+gr);//+", grupy to ["+grupy_w_druk[0]+","+grupy_w_druk[1]+"]");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        return j;
    }

    public void zwolnij_zasob(String nazwa, int co_bierzesz, int gr, int nr_powt, int ktore) throws InterruptedException {
        lock.lock();
        if ( co_bierzesz == 1 ) {
            try{
                //System.out.println("Zaczynam zwalnianie skanera ja"+nazwa+" jej nr to "+ ktore);
                grupy_w_skan[gr] = false;
                skanery[ktore] = false;
                grupa[gr].signal();
                skaner.signal();
                System.out.println("["+nazwa+","+nr_powt+"] <<< skaner "+ktore+" stan skanerow ["+skanery[0]+","+skanery[1]+"]");
            } finally {
                lock.unlock();
            }
        } else {
            try{
                //System.out.println("Zaczynam zwalnianie drukarki ja"+nazwa+" jej nr to "+ ktore);
                grupy_w_druk[gr] = false;
                drukarki[ktore] = false;
                grupa[gr].signal();
                drukarka.signal();
                System.out.println("["+nazwa+","+nr_powt+"] <<< drukarka "+ktore+" stan drukarek ["+drukarki[0]+","+drukarki[1]+"]");
            } finally {
                lock.unlock();
            }
        }
    }


}
