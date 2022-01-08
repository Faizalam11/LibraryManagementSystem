package library_management_system;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Book implements java.io.Serializable{
    final static int FEE_PER_WEEK = 50;

    // To ensure each new book has a new id;
    static int currentId = 0;
    final String name, author, publisher, category, subcategory;
    final int id, edition;
    int copiesAvailable, rentedCopies = 0;
    final ArrayList<String[]> rentedTo;
    final Queue<String> rentingQueue;
    public Book(String name, String author, String publisher, int edition, String category, String subcategory, int copiesAvailable) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.subcategory = subcategory;
        this.edition = edition;
        this.copiesAvailable = copiesAvailable;
        this.id = ++currentId;
        this.rentedTo = new ArrayList<>();
        this.rentingQueue = new LinkedList<>();
    }


    public Book(String name, String author, String publisher, int edition, String category, String subcategory){
        this(name, author, publisher, edition, category, subcategory, 1);
    }

    public Book(String name, String author){
        this(name, author, null, 1, null, null, 1);
    }

    // To add copies, copies parameter should be positive;
    // To remove copies, copies parameter should be negative;

    public void changeCopiesAvailable(int copies) {
        this.copiesAvailable += copies;
    }
    private int rentedToSearch(String userid) {
        for (int i = 0; i < rentedTo.size();i++){
            if(rentedTo.get(i)[0].equals(userid)){
                return i;
            }
        }
        return -1;
    }

    // Userid is "username@id"

    public Boolean rentBook(String userid) {
        int queueSize = rentingQueue.size();
        if (copiesAvailable <= queueSize && !userid.equals(rentingQueue.peek())) {
            rentingQueue.offer(userid);
            System.out.println(userid + " is added to waiting queue for this book");
            return false;
        } else {
            if (queueSize != 0){
                rentingQueue.poll();
            }
            copiesAvailable--;
            rentedCopies++;
            String[] data = {userid, LocalDateTime.now().toString()};
            rentedTo.add(data);
            System.out.println(data[0] + " rented Book " + name + " on " + data[1]);
            return true;
        }
    }
    public int calculateFee(LocalDateTime lendingTime) {
        long days = Duration.between(lendingTime, LocalDateTime.now()).toDays();
        long weeks = days/7;
        if (days % 7 != 0) {
            weeks++;
        }
        return (int) (FEE_PER_WEEK * weeks);
    }

    public Boolean returnBook(String userid) {
        int index = rentedToSearch(userid);
        if (rentedCopies == 0 || index == -1) {
            return false;
        }
        else{
            Scanner in = new Scanner(System.in);
            int fees = calculateFee(LocalDateTime.parse(rentedTo.get(index)[1]));
            System.out.println("Payment for Lending for " + fees/FEE_PER_WEEK + " weeks:\n" + fees);
            System.out.println("Press Enter after Receiving Payment:");
            in.next();
            copiesAvailable++;
            rentedCopies--;
            rentedTo.remove(index);
            return true;
        }
    }

    public String pollRentingQueue(){
        return rentingQueue.poll();
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", category='" + category + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", edition=" + edition +
                ", copiesAvailable=" + copiesAvailable +
                ", rentedCopies=" + rentedCopies +
                ", rentedTo=" + rentedTo +
                ", rentingQueue=" + rentingQueue +
                '}';
    }

}
