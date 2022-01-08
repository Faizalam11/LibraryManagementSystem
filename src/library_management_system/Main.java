package library_management_system;

import java.io.Console;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Admin Admin, for login
        Console con = System.console(); // Does not work in IDE
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Enter Login ID:");
            String username = in.nextLine();
            System.out.println("Enter Password:");
            String password;
            if (con == null) {
                password = in.nextLine();
            }
            else {
                password = String.valueOf(con.readPassword()); // Does not work in IDE
            }
            String passHash = "c1c224b03cd9bc7b6a86d77f5dace40191766c485cd55dc48caf9ac873335d6f";
            try {
                if (!username.equals("Admin") || !getPasswordHash(password).equals(passHash)) {
                    System.out.println("Wrong Username or Password!");
                } else {
                    break;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }


        Library library = new Library();
        library.insertBook("AB","123");
        library.insertBook("AB","123");
        library.insertBook("ABA","123");
        library.insertBook("BBA","123");
        Book book = library.searchBooks("AB").get(0);
        System.out.println(book.toString());
        book = library.searchBooks("AB").get(1);
        System.out.println(book.toString());

    }
    public static String getPasswordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("Sha-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        BigInteger num = new BigInteger(1, hash);
        StringBuilder sb = new StringBuilder(num.toString(16));
        while (sb.length() < 32) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }
}
