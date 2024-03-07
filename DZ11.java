import java.util.Scanner;

public class DZ11 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double sumaOcen = 0;
        double ocena = 1;
        int liczbaOcen = 0;

        while (ocena != 0) {
            System.out.println("Podaj ocenę nr " + (liczbaOcen + 1) + ":");
            ocena = scanner.nextDouble();
            if (ocena != 0) {
                sumaOcen += ocena;
                liczbaOcen++;
            }
        }

        if (liczbaOcen > 0) {
            double srednia = sumaOcen / liczbaOcen;
            System.out.println("Średnia ocen: " + srednia);

            if (srednia > 4.1) {
                System.out.println("Gratulacje! Przysługuje Ci stypendium naukowe.");
            } else {
                System.out.println("Niestety, nie przysługuje Ci stypendium naukowe.");
            }
        } else {
            System.out.println("Nie podano żadnych ocen.");
        }

        scanner.close();
    }
}