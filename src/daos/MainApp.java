package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class MainApp {
    private static Connection conn = null;

    public static void main(String[] args) throws SQLException {

        try {
            String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=AarhusBryghus";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url, "sa", "asdf");
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Hvilken funktion vil du vælge?");
        System.out.println("1: Opret Produkt");
        System.out.println("2: Dagligt salg");
        System.out.println("3: Dagligt salg opdelt på kategori");

        try (Scanner scanner = new Scanner(System.in)) {
            int operation = scanner.nextInt();
            if (operation == 1) {
                createProduct();
            }
            if (operation == 2) {
                dailySales();
            }
            if (operation == 3) {
                dailySalesByCategory();
            }
            else {
                System.out.println("Ikke gyldig funktion, kør program igen");
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Ikke gyldig funktion, kør program igen");
        }
    }

    private static void dailySalesByCategory() throws SQLException {
        String dateString = "";
        LocalDate date = null;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Indtast dato for salg (yy-mm-dd)");
            dateString = scanner.next();
            while (date == null) {
                try {
                    date = LocalDate.parse(dateString);
                }
                catch (DateTimeParseException e) {
                    System.out.println("Ikke gyldig dato");
                }
            }
        }
        PreparedStatement s;
        s = conn.prepareStatement("exec dailySalesByCategory @date=?");
        s.setString(1, dateString);
        ResultSet rs = s.executeQuery();
        while (rs.next()) {
            System.out.printf(Locale.GERMAN, "%s %.2f kr\n", rs.getString("category_name"),
                rs.getFloat("price"));
        }

    }

    public static void createProduct() throws SQLException {
        String name = "";
        Integer clips = null;
        String category = null;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Skriv navnet på produktet");
            name = scanner.next();
            System.out.println("Skriv hvor mange antal klip produktet koster");
            while (clips == null) {
                String c = scanner.next();
                try {
                    clips = Integer.parseInt(c);
                }
                catch (NumberFormatException | InputMismatchException e) {
                    System.out.println("Indtast et tal, por favor");
                }
            }
            System.out.println("Skriv categorien på produktet - skal allerede være oprettet");
            while (category == null) {
                category = scanner.next();
                PreparedStatement s;
                s = conn.prepareStatement("exec testCategory @name=?");
                s.setString(1, category);
                ResultSet rs = s.executeQuery();
                rs.next();
                if (!rs.getBoolean("exists")) {
                    category = null;
                    System.out.println("Kategorien findes ikke, prøv igen");
                }
            }
        }
        PreparedStatement s;
        s = conn.prepareStatement("insert into product values(?,?,?)");
        s.setString(1, name);
        s.setInt(2, clips);
        s.setString(3, category);
        s.executeUpdate();

    }

    public static void dailySales() throws SQLException {
        String dateString = "";
        LocalDate date = null;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Indtast dato for salg (yy-mm-dd)");
            dateString = scanner.next();
            while (date == null) {
                try {
                    date = LocalDate.parse(dateString);
                }
                catch (DateTimeParseException e) {
                    System.out.println("Ikke gyldig dato");
                }
            }
        }
        PreparedStatement s;
        s = conn.prepareStatement("exec dailySales @date=?");
        s.setString(1, dateString);
        ResultSet rs = s.executeQuery();
        rs.next();
        System.out.println(rs.getFloat(1));

    }
}
