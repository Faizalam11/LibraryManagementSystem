package library_management_system;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
    String name, type, passwordHash, username;
    int id;
    static int currentId = 0;

    enum BOOK_STATUS {
        LENDING, WAITING_LIST;
    }

    static class BookStatus {
        Book book;
        BOOK_STATUS status;

        public BookStatus(Book book, BOOK_STATUS status) {
            this.book = book;
            this.status = status;
        }
    }

    ArrayList<BookStatus> user_books;

    public User(int id, String name, String type, String password) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.username = name + "@" + id;
        try {
            this.passwordHash = getPasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public User(String name, String type, String password) {
        this(++currentId, name, type, password);
    }

    private int searchUserBooks(Book book) {
        for (int i = 0; i < user_books.size(); i++) {
            if (user_books.get(i).book == book)
                return i;
        }
        return -1;
    }

    /*
    email.properties content
    email=your email
    password=email's password
     */
//    public void getpass() {
//        Properties p = new Properties();
//        try (FileReader in = new FileReader("email.properties")){
//            p.load(in);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String email = p.getProperty("email");
//        String password = p.getProperty("password");
//    }

    public void addBook(Book book, Boolean isLent) {
        int index = searchUserBooks(book);
        if (index != -1) {
            user_books.remove(index);
        } else if (isLent) {
            user_books.add(new BookStatus(book, BOOK_STATUS.LENDING));
        } else {
            user_books.add(new BookStatus(book, BOOK_STATUS.WAITING_LIST));
        }
    }

    public Boolean removeBook(Book book) {
        int index = searchUserBooks(book);
        if (index != -1) {
            user_books.remove(index);
            return true;
        }
        return false;
    }

    private String getPasswordHash(String password) throws NoSuchAlgorithmException {
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