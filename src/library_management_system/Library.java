package library_management_system;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Library implements java.io.Serializable{
    @SuppressWarnings("unchecked")
    final LinkedList<Book>[] books = new LinkedList[27];
    final List<Book> booksSortedById = new ArrayList<>();
    final List<User> users = new ArrayList<>();

    Library(){
        for (int i = 0; i < 27; i++) {
            books[i] = new LinkedList<>();
        }
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

    public void insertBook(String name, String author, String publisher, int edition, String category, String subcategory) {
        insertBook(name, author, publisher, edition, category, subcategory, 1);
    }

    public void insertBook(String name, String author) {
        Book book = new Book(name, author);
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

    public User getUserByName(String name) {
        for (User user: users) {
            if (user.name == name) {
                return user;
            }
        }
        return null;
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

    public Book searchBookById(int id) {
        return booksSortedById.get(id-1);
    }
}