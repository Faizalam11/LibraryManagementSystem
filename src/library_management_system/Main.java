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
        String username;
        while (true) {
            System.out.println("Enter Login ID:");
            username = in.nextLine();
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
        int choice;
        Library library = new Library();
        System.out.println("Welcome to Library Management System, " + username + '!');
        while (true) {
            System.out.println("Press 1 to register a user:");
            System.out.println("Press 2 to lend a book:");
            System.out.println("Press 3 to return a book:");
            System.out.println("Press 4 to view all library statistic:");
            System.out.println("Press 5 to add a book:");
            System.out.println("Press 6 to remove a book:");
            System.out.println("Press 7 to add/remove copies of a book:");
            System.out.println("Press 8 to delete a user:");
            System.out.println("Press 9 to exit:");
            choice = in.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.println("Enter New User NIC:");
                    long NIC = in.nextLong();
                    System.out.println("Enter New User name:");
                    String userName = in.nextLine();
                    System.out.println("Enter New User email:");
                    String email = in.nextLine();
                    library.addUser(NIC, userName, email);
                }
                case 2 -> {
                    System.out.println("Enter User NIC:");
                    long userNIC = in.nextLong();
                    User user = library.getUserByNIC(userNIC);
                    if (user == null) {
                        System.out.println("Please Register First!");
                    } else {
                        Book book_to_lend = getBook(library);
                        if (book_to_lend == null) {
                            System.out.println("Please add the Book first!");
                        } else {
                            book_to_lend.rentBook(user.username);
                        }
                    }
                }
                case 3 -> {
                    System.out.println("Enter User NIC:");
                    long user_NIC = in.nextLong();
                    User temp_user = library.getUserByNIC(user_NIC);
                    if (temp_user == null) {
                        System.out.println("Please Register First!");
                    } else {
                        Book book_lent = getBook(library);
                        if (book_lent == null) {
                            System.out.println("The book is not even in Library.");
                        } else {
                            int fees = book_lent.returnBook(temp_user.username);
                            library.addEarnings(fees);
                        }
                    }
                }
                case 4 -> System.out.println(library);
                case 5 -> {
                    System.out.println("Enter Book Details: ");
                    System.out.println("\tEnter Book's Name:");
                    String name = in.nextLine();
                    System.out.println("\tEnter Book's Author Name:");
                    String author = in.nextLine();
                    System.out.println("\tEnter Book's Publisher:");
                    String publisher = in.nextLine();
                    System.out.println("\tEnter Book's Edition:");
                    int edition = in.nextInt();
                    System.out.println("\tEnter Book's Category/Subject:");
                    String category = in.nextLine();
                    System.out.println("\tEnter Book's Sub-category/field:");
                    String subcategory = in.nextLine();
                    System.out.println("\tEnter Book's copies Available in Library:");
                    int copiesAvailable = in.nextInt();
                    library.insertBook(name, author, publisher, edition, category, subcategory, copiesAvailable);
                }
                case 6 -> {
                    Book rem_book = getBook(library);
                    if (rem_book == null) {
                        System.out.println("Please add the Book first!");
                        break;
                    }
                    library.removeBook(rem_book.id);
                }
                case 7 -> {
                    Book book = getBook(library);
                    if (book == null) {
                        System.out.println("Please add the Book first!");
                        break;
                    }
                    System.out.println("Enter the copies to add or remove: ");
                    int copies = in.nextInt();
                    book.addCopiesAvailable(copies);
                }
                case 8 -> {
                    System.out.println("Enter NIC of User to remove:");
                    long nic = in.nextLong();
                    library.removeUser(nic);
                }
                case 9 -> System.exit(0);
                default -> System.out.println("Enter only numbers mentioned above!!!");
            }
        }


    }

    private static Book getBook(Library library) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the Book Name: ");
        String bookName = in.nextLine();
        System.out.println(library.searchBooks(bookName));
        System.out.println("Enter the Book id: ");
        int bookId = in.nextInt();
        return library.searchBookById(bookId);
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
