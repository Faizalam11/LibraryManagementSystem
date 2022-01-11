package library_management_system;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class Library implements java.io.Serializable{
    @SuppressWarnings("unchecked")
    private final LinkedList<Book>[] books = new LinkedList[27];
    private final List<Book> booksSortedById = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private int earnings = 0;
    AtomicInteger book_id = Book.currentId;
    AtomicInteger user_id = User.currentId;
    ArrayList<String[]> rentedBooks = new ArrayList<>();

    Library(){
        for (int i = 0; i < 27; i++) {
            books[i] = new LinkedList<>();
        }
    }

    public int getEarnings() {
        return earnings;
    }

    public void addEarnings(int earnings) {
        if (earnings < 0) {
            System.out.println("Earnings cannot be less than 0!");
            return;
        }
        this.earnings += earnings;
    }

    public List<Book> getBooks() {
        return new ArrayList<>(booksSortedById);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    // Insert element by order
    private void insert_book(int indexOfLL, Book book){
        LinkedList<Book> LL = books[indexOfLL];
        int size = LL.size();
        if (size == 0) {
            LL.add(book);
        } else if ((LL.get(size - 1)).name.compareToIgnoreCase(book.name) < 0) {
            LL.add(book);
        } else {
            int i;
            for (i = 0; i < size; i++) {
                boolean condition = (LL.get(i)).name.compareToIgnoreCase(book.name) > 0;
                if (condition)
                    break;
            }
            LL.add(i, book);
        }
    }

    public void insertBook(String name, String author, String publisher, int edition, String category, String subcategory, int copiesAvailable) {
        Book book = new Book(name, author, publisher, edition, category, subcategory, copiesAvailable);
        booksSortedById.add(book);
        int start = name.toLowerCase(Locale.ROOT).charAt(0);
        insert_book(start - 'a', book);
    }

    public LinkedList<Book> searchBooks(String name){
        LinkedList<Book> res = new LinkedList<>();
        int index = name.toLowerCase(Locale.ROOT).charAt(0) - 'a';
        for (int i = 0; i < books[index].size() ; i++) {
            if ((books[index].get(i)).name.equals(name)) {
                res.add(books[index].get(i));
            }
        }
        return res;
    }

    public Book searchBookById(int id) {
        if (id <= booksSortedById.size() && id >= 0) {
            return booksSortedById.get(id-1);
        }
        return null;
    }

    public void removeBook(int id) {
        Book book = searchBookById(id);
        if (book != null) {
            // To not cause chaos;
            booksSortedById.set(id-1, null);
            int index = book.name.toLowerCase(Locale.ROOT).charAt(0) - 'a';
            books[index].remove(book);
        }
    }

    public ArrayList<User> getUserByName(String name) {
        ArrayList<User> res = new ArrayList<>();
        for (User user: users) {
            if (name.equals(user.name)) {
                res.add(user);
            }
        }
        if (res.isEmpty()) {
            return null;
        }
        return res;
    }

    public User getUserByNIC(long NIC) {
        for (User user: users) {
            if (user.NIC == NIC) {
                return user;
            }
        }
        return null;
    }

    public void addUser(long NIC, String name, String email){
        if (getUserByNIC(NIC) != null) {
            return;
        }
        users.add(new User(NIC, name, email));
    }

    public void removeUser(long NIC) {
        if (getUserByNIC(NIC) == null) {
            return;
        }
        users.remove(getUserByNIC(NIC));
    }

    @Override
    public String toString() {
        return "Library{" + "\n" +
                "booksSortedById=" + booksSortedById + "\n" +
                ", users=" + users + "\n" +
                ", rentedBooks=" + rentedBooks+ "\n" +
                ", earnings=" + earnings + "\n" +
                '}';
    }
}