import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

class Utwor {
    String autor;
    String wykonawca;
    double czasTrwania;

    // Konstruktor
    public Utwor(String autor, String wykonawca, double czasTrwania) {
        this.autor = autor;
        this.wykonawca = wykonawca;
        this.czasTrwania = czasTrwania;
    }

    // Metoda wyświetlająca informacje o utworze
    public void wyswietlUtwor() {
        System.out.println("Autor: " + autor);
        System.out.println("Wykonawca: " + wykonawca);
        System.out.println("Czas trwania: " + czasTrwania + " minut");
    }
}

class CD {
    String tytulAlbumu;
    String nazwiskoWykonawcy;
    String nazwaWydawcy;
    int rokWydania;
    double cenaPlyty;
    Utwor[] utwory = new Utwor[10];
    int liczbaUtworow = 0;

    // Konstruktor
    public CD(String tytulAlbumu, String nazwiskoWykonawcy, String nazwaWydawcy, int rokWydania, double cenaPlyty) {
        this.tytulAlbumu = tytulAlbumu;
        this.nazwiskoWykonawcy = nazwiskoWykonawcy;
        this.nazwaWydawcy = nazwaWydawcy;
        this.rokWydania = rokWydania;
        this.cenaPlyty = cenaPlyty;
    }

    // Metoda dodająca utwór do tablicy utworów
    public void dodajUtwor(Utwor utwor) {
        if (liczbaUtworow < 10) {
            utwory[liczbaUtworow] = utwor;
            liczbaUtworow++;
        } else {
            System.out.println("Nie można dodać więcej utworów, limit został osiągnięty.");
        }
    }

    // Metoda wyświetlająca informacje o płycie CD
    public void wyswietlCD() {
        System.out.println("Tytuł albumu: " + tytulAlbumu);
        System.out.println("Nazwisko wykonawcy: " + nazwiskoWykonawcy);
        System.out.println("Nazwa wydawcy: " + nazwaWydawcy);
        System.out.println("Rok wydania: " + rokWydania);
        System.out.println("Cena płyty: " + cenaPlyty);
        System.out.println("Lista utworów:");

        for (int i = 0; i < liczbaUtworow; i++) {
            System.out.println("Utwór " + (i + 1) + ":");
            utwory[i].wyswietlUtwor();
        }
    }
}

class KolekcjaPlyt {
    CD[] kolekcja = new CD[10];
    int liczbaPlyt = 0;

    // Metoda dodająca płytę do kolekcji
    public void dodajPlyte(CD cd) {
        if (liczbaPlyt < 10) {
            kolekcja[liczbaPlyt] = cd;
            liczbaPlyt++;
        } else {
            System.out.println("Nie można dodać więcej płyt, limit został osiągnięty.");
        }
    }

     // Metoda wyświetlająca zawartość kolekcji płyt
     public void wyswietlKolekcje() {
        System.out.println("Kolekcja płyt:");

        for (int i = 0; i < liczbaPlyt; i++) {
            System.out.println("Płyta " + (i + 1) + ":");
            kolekcja[i].wyswietlCD();
            System.out.println();
        }
        wyswietlStatystykiCenowe();
    }

    // Metoda wyświetlająca statystyki cenowe
    private void wyswietlStatystykiCenowe() {
        Map<Double, Integer> cenaPlytyMapa = new HashMap<>();

        // Liczenie ilości płyt w poszczególnych cenach
        for (int i = 0; i < liczbaPlyt; i++) {
            double cena = kolekcja[i].cenaPlyty;
            if (cenaPlytyMapa.containsKey(cena)) {
                cenaPlytyMapa.put(cena, cenaPlytyMapa.get(cena) + 1);
            } else {
                cenaPlytyMapa.put(cena, 1);
            }
        }

        // Wyświetlanie statystyk cenowych
        System.out.println("Statystyki cenowe:");
        for (Map.Entry<Double, Integer> entry : cenaPlytyMapa.entrySet()) {
            System.out.println("Cena: " + entry.getKey() + " - Ilość płyt: " + entry.getValue());
        }
    }
}
// raport
public class DZ13 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Tworzenie kolekcji płyt
        KolekcjaPlyt kolekcjaPlyt = new KolekcjaPlyt();

        // Pętla dodająca płyty do kolekcji
        char kontynuuj;
        do {
            System.out.println("Dodawanie nowej płyty:");
            System.out.println("Podaj tytuł albumu:");
            String tytul = scanner.nextLine();
            System.out.println("Podaj nazwisko wykonawcy:");
            String nazwisko = scanner.nextLine();
            System.out.println("Podaj nazwę wydawcy:");
            String wydawca = scanner.nextLine();
            System.out.println("Podaj rok wydania:");
            int rok = scanner.nextInt();
            System.out.println("Podaj cenę płyty:");
            double cena = scanner.nextDouble();

            CD cd = new CD(tytul, nazwisko, wydawca, rok, cena);

            // Pętla dodająca utwory do płyty
            scanner.nextLine(); // konsumuje znak nowej linii
            char kontynuujUtwory;
            do {
                System.out.println("Dodawanie nowego utworu:");
                System.out.println("Podaj autora utworu:");
                String autor = scanner.nextLine();
                System.out.println("Podaj wykonawcę utworu:");
                String wykonawca = scanner.nextLine();
                System.out.println("Podaj czas trwania utworu (w minutach):");
                double czas = scanner.nextDouble();

                Utwor utwor = new Utwor(autor, wykonawca, czas);
                cd.dodajUtwor(utwor);

                System.out.println("Czy chcesz dodać kolejny utwór? (T/N)");
                kontynuujUtwory = scanner.next().charAt(0);
                scanner.nextLine(); // konsumuje znak nowej linii

            } while (kontynuujUtwory == 'T' || kontynuujUtwory == 't');

            kolekcjaPlyt.dodajPlyte(cd);

            System.out.println("Czy chcesz dodać kolejną płytę? (T/N)");
            kontynuuj = scanner.next().charAt(0);
            scanner.nextLine(); // konsumuje znak nowej linii

        } while (kontynuuj == 'T' || kontynuuj == 't');

        // Wyświetlanie zawartości kolekcji płyt
        kolekcjaPlyt.wyswietlKolekcje();

        scanner.close();
    }
}

