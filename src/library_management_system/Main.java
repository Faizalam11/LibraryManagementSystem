package library_management_system;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
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
        Library library;
        File file = new File("library.ser");
        if (file.exists() && file.isFile()) {
            try {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream objIn = new ObjectInputStream(fileIn);
                library = (Library) objIn.readObject();
                objIn.close();
                fileIn.close();
                Book.currentId = library.book_id;
                User.currentId = library.user_id;
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                library = new Library();
            }


        }
        else {
            library = new Library();
        }
        System.out.println("Welcome to Library Management System, " + username + '!');
        while (true) {
            System.out.println("Press 0 to search for a book:");
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
                case 0 -> {
                    Book res = getBook(library);
                    if (res == null) {
                        System.out.println("Book not Found!");
                    } else {
                        System.out.println(res);
                    }
                    in.nextLine();
                }
                case 1 -> {
                    System.out.println("Enter New User NIC:");
                    long NIC = in.nextLong();
                    in.nextLine();
                    System.out.println("Enter New User name:");
                    String userName = in.nextLine();
                    System.out.println("Enter New User email:");
                    String email = in.nextLine();
                    if (library.getUserByNIC(NIC) == null) {
                        library.addUser(NIC, userName, email);
                    } else {
                        System.out.println("User is already registered!");
                    }
                    in.nextLine();
                }
                case 2 -> {
                    System.out.println("Enter User NIC:");
                    long userNIC = in.nextLong();
                    in.nextLine();
                    User user = library.getUserByNIC(userNIC);
                    if (user == null) {
                        System.out.println("Please Register First!");
                    } else {
                        Book book_to_lend = getBook(library);
                        if (book_to_lend == null) {
                            System.out.println("Please add the Book first!");
                        } else {
                            library.rentedBooks.add(new String[]{user.username, book_to_lend.toString(), LocalDateTime.now().toString()});
                            book_to_lend.rentBook(user.username);
                        }
                    }
                    in.nextLine();
                }
                case 3 -> {
                    System.out.println("Enter User NIC:");
                    long user_NIC = in.nextLong();
                    in.nextLine();
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
                            library.rentedBooks.removeIf(entry -> entry[1].equals(temp_user.username) && entry[2].equals(book_lent.toString()));
                        }
                    }
                    in.nextLine();
                }
                case 4 -> {
                    System.out.println(library);
                    in.nextLine();
                }
                case 5 -> {
                    in.nextLine();
                    System.out.println("Enter Book Details: ");
                    System.out.print("\tEnter Book's Name:");
                    String name = in.nextLine();
                    System.out.print("\n\tEnter Book's Author Name:");
                    String author = in.nextLine();
                    System.out.print("\n\tEnter Book's Publisher:");
                    String publisher = in.nextLine();
                    System.out.print("\n\tEnter Book's Edition:");
                    int edition = in.nextInt();
                    in.nextLine();
                    System.out.print("\n\tEnter Book's Category/Subject:");
                    String category = in.nextLine();
                    System.out.print("\n\tEnter Book's Sub-category/field:");
                    String subcategory = in.nextLine();
                    System.out.print("\n\tEnter Book's copies Available in Library:");
                    int copiesAvailable = in.nextInt();
                    in.nextLine();
                    library.insertBook(name, author, publisher, edition, category, subcategory, copiesAvailable);
                }
                case 6 -> {
                    Book rem_book = getBook(library);
                    if (rem_book == null) {
                        System.out.println("Please add the Book first!");
                        in.nextLine();
                        break;
                    }
                    library.removeBook(rem_book.id);
                }
                case 7 -> {
                    Book book = getBook(library);
                    if (book == null) {
                        System.out.println("Please add the Book first!");
                        in.nextLine();
                        break;
                    }
                    System.out.println("Enter the copies to add or remove: ");
                    int copies = in.nextInt();
                    in.nextLine();
                    book.addCopiesAvailable(copies);
                }
                case 8 -> {
                    System.out.println("Enter NIC of User to remove:");
                    long nic = in.nextLong();
                    in.nextLine();
                    library.removeUser(nic);
                }
                case 9 -> {
                    try {
                        FileOutputStream fileOut = new FileOutputStream("library.ser");
                        ObjectOutputStream out = new ObjectOutputStream(fileOut);
                        out.writeObject(library);
                        out.close();
                        fileOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
                default -> {
                    System.out.println("Enter only numbers mentioned above!!!");
                    in.nextLine();
                }
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
        in.nextLine();
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
