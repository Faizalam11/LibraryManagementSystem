package edu.library_management_system;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Library library = new Library();
        library.insertBook("AB","123");
        library.insertBook("ABA","123");
        library.insertBook("BBA","123");
        Book book = (Book) library.searchBooks("AB").get(0);
        System.out.println(book.toString());
    }
}
