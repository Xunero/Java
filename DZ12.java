import java.util.Scanner;


public class DZ12 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj wartość n:");
        int n = scanner.nextInt();

        // Obliczanie wartości n!
        long silnia = 1;
        for (int i = 1; i <= n; i++) {
            silnia *= i;
        }
        System.out.println(n + "! = " + silnia);

        // Obliczanie sumy ciągu
        double suma = 0;
        for (int i = 1; i <= n; i++) {
            suma += (i % 2 == 0) ? -1.0 / (i + n) : 1.0 / (i + n);
        }
        System.out.println("Suma ciągu = " + suma);

        scanner.close();
    }
}