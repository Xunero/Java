import java.util.Random;
import java.util.Scanner;

// Klasa reprezentująca nieruchomość
class Nieruchomosc {
    private String nazwa;
    private int cena;
    private int czynsz;

    public Nieruchomosc(String nazwa, int cena, int czynsz) {
        this.nazwa = nazwa;
        this.cena = cena;
        this.czynsz = czynsz;
    }

    public String getNazwa() {
        return nazwa;
    }

    public int getCena() {
        return cena;
    }

    public int getCzynsz() {
        return czynsz;
    }
}

// Klasa reprezentująca gracza
class Gracz {
    private String nazwa;
    private int pieniadze;
    private Nieruchomosc[] posiadlosci;
    private int pozycja;

    public Gracz(String nazwa, int pieniadze) {
        this.nazwa = nazwa;
        this.pieniadze = pieniadze;
        this.posiadlosci = new Nieruchomosc[0];
        this.pozycja = 0;
    }

    public String getNazwa() {
        return nazwa;
    }

    public int getPieniadze() {
        return pieniadze;
    }

    public Nieruchomosc[] getPosiadlosci() {
        return posiadlosci;
    }

    public int getPozycja() {
        return pozycja;
    }

    public void setPozycja(int pozycja) {
        this.pozycja = pozycja;
    }

    public void dodajPieniadze(int kwota) {
        pieniadze += kwota;
    }

    public void odejmijPieniadze(int kwota) {
        pieniadze -= kwota;
    }

    public void dodajPosiadlosc(Nieruchomosc nieruchomosc) {
        Nieruchomosc[] nowePosiadlosci = new Nieruchomosc[posiadlosci.length + 1];
        for (int i = 0; i < posiadlosci.length; i++) {
            nowePosiadlosci[i] = posiadlosci[i];
        }
        nowePosiadlosci[posiadlosci.length] = nieruchomosc;
        posiadlosci = nowePosiadlosci;
    }

    public boolean czyMaPosiadlosc(Nieruchomosc nieruchomosc) {
        for (Nieruchomosc posiadlosc : posiadlosci) {
            if (posiadlosc == nieruchomosc) {
                return true;
            }
        }
        return false;
    }
}

// Klasa reprezentująca planszę
class Plansza {
    private Nieruchomosc[] nieruchomosci;

    public Plansza() {
        // Inicjalizacja nieruchomości
        nieruchomosci = new Nieruchomosc[14];
        nieruchomosci[0] = new Nieruchomosc("Nieruchomość 1", 100, 10);
        nieruchomosci[1] = new Nieruchomosc("Nieruchomość 2", 150, 15);
        nieruchomosci[2] = new Nieruchomosc("Nieruchomość 3", 100, 10);
        nieruchomosci[3] = new Nieruchomosc("Nieruchomość 4", 150, 15);
        nieruchomosci[4] = new Nieruchomosc("Nieruchomość 5", 100, 10);
        nieruchomosci[5] = new Nieruchomosc("Nieruchomość 6", 150, 15);
        nieruchomosci[6] = new Nieruchomosc("Nieruchomość 7", 100, 10);
        nieruchomosci[7] = new Nieruchomosc("Nieruchomość 8", 150, 15);
        nieruchomosci[8] = new Nieruchomosc("Nieruchomość 9", 100, 10);
        nieruchomosci[9] = new Nieruchomosc("Nieruchomość 10", 150, 15);
        nieruchomosci[10] = new Nieruchomosc("Nieruchomość 11", 100, 10);
        nieruchomosci[11] = new Nieruchomosc("Nieruchomość 12", 150, 15);
        nieruchomosci[12] = new Nieruchomosc("Nieruchomość 13", 100, 10);
        nieruchomosci[13] = new Nieruchomosc("Nieruchomość 14", 150, 15);
    }

    public Nieruchomosc[] getNieruchomosci() {
        return nieruchomosci;
    }

    public Nieruchomosc getNieruchomosc(int pozycja) {
        return nieruchomosci[pozycja % nieruchomosci.length];
    }
}

// Klasa zarządzająca grą
public class DZ21 {
    private Gracz[] gracze;
    private Plansza plansza;
    private Scanner scanner;
    private Random random;

    public DZ21(int liczbaGraczy) {
        gracze = new Gracz[liczbaGraczy];
        plansza = new Plansza();
        scanner = new Scanner(System.in);
        random = new Random();
        // Inicjalizacja graczy
        for (int i = 0; i < liczbaGraczy; i++) {
            System.out.print("Podaj nazwę gracza " + (i + 1) + ": ");
            String nazwaGracza = scanner.nextLine();
            gracze[i] = new Gracz(nazwaGracza, 1000); // Początkowa kwota pieniędzy gracza
        }
    }

    public void rozgrywka() {
        while (true) {
            for (Gracz gracz : gracze) {
                System.out.println("\nTura gracza: " + gracz.getNazwa());
                wykonajRuch(gracz);
            }
        }
    }

    private void wykonajRuch(Gracz gracz) {
        int rzut = rzutKoscia();
        System.out.println("Wyrzucono: " + rzut);
        gracz.setPozycja((gracz.getPozycja() + rzut) % plansza.getNieruchomosci().length);
        Nieruchomosc aktualnaNieruchomosc = plansza.getNieruchomosc(gracz.getPozycja());
        System.out.println("Jesteś na nieruchomości: " + aktualnaNieruchomosc.getNazwa());
        
        if (gracz.getPozycja() % 15 == 0) {
            System.out.println("Dostałeś 500 pieniędzy za przekroczenie pola 0.");
            gracz.dodajPieniadze(500);
        }
        
        System.out.print("Czy chcesz zobaczyć swoje posiadłości? (T/N): ");
        String odpowiedz = scanner.nextLine();
        if (odpowiedz.equalsIgnoreCase("T")) {
            System.out.println("Twoje posiadłości:");
            for (Nieruchomosc posiadlosc : gracz.getPosiadlosci()) {
                System.out.println("- " + posiadlosc.getNazwa());
            }
        }
        
        System.out.print("Czy chcesz zobaczyć stan konta? (T/N): ");
        odpowiedz = scanner.nextLine();
        if (odpowiedz.equalsIgnoreCase("T")) {
            System.out.println("Twój stan konta wynosi: " + gracz.getPieniadze() + " pieniędzy.");
        }
        
        if (!gracz.czyMaPosiadlosc(aktualnaNieruchomosc)) {
            if (!czyNieruchomoscPosiadaWlasciciela(aktualnaNieruchomosc)) {
                kupNieruchomosc(gracz, aktualnaNieruchomosc);
            } else {
                System.out.println("Ta nieruchomość jest już własnością innego gracza.");
            }
        } else {
            System.out.println("Już posiadasz tę nieruchomość.");
        }
    }
    private boolean czyNieruchomoscPosiadaWlasciciela(Nieruchomosc nieruchomosc) {
        for (Gracz gracz : gracze) {
            if (gracz.czyMaPosiadlosc(nieruchomosc)) {
                return true;
            }
        }
        return false;
    }

    private int rzutKoscia() {
        return random.nextInt(6) + 1;
    }

    private void kupNieruchomosc(Gracz gracz, Nieruchomosc nieruchomosc) {
        System.out.print("Czy chcesz kupić nieruchomość? (T/N): ");
        String odpowiedz = scanner.nextLine();
        if (odpowiedz.equalsIgnoreCase("T")) {
            if (gracz.getPieniadze() >= nieruchomosc.getCena()) {
                gracz.odejmijPieniadze(nieruchomosc.getCena());
                gracz.dodajPosiadlosc(nieruchomosc);
                System.out.println("Gracz " + gracz.getNazwa() + " kupił nieruchomość: " + nieruchomosc.getNazwa());
            } else {
                System.out.println("Nie masz wystarczająco pieniędzy, aby kupić tę nieruchomość.");
            }
        }
    }

    public static void main(String[] args) {
        int liczbaGraczy = 2; // Przykładowa liczba graczy
        DZ21 gra = new DZ21(liczbaGraczy);
        gra.rozgrywka();
    }
}
