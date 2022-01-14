package library_management_system;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Book implements java.io.Serializable{
    final static int FEE_PER_WEEK = 50;

    // To ensure each new book has a new id;
    static AtomicInteger currentId = new AtomicInteger(0);
    final String name, author, publisher, category, subcategory;
    final int id, edition;
    private int copiesAvailable, rentedCopies = 0;
    private final ArrayList<String[]> rentedTo;
    private final Queue<String> rentingQueue;
    public Book(String name, String author, String publisher, int edition, String category, String subcategory, int copiesAvailable) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.subcategory = subcategory;
        this.edition = edition;
        this.copiesAvailable = copiesAvailable;
        this.id = currentId.incrementAndGet();
        this.rentedTo = new ArrayList<>();
        this.rentingQueue = new LinkedList<>();
    }


    // To add copies, copies parameter should be positive;
    // To remove copies, copies parameter should be negative;

    public void addCopiesAvailable(int copies){
        if (this.copiesAvailable + copies >= 0) {
            this.copiesAvailable += copies;
        } else {
            try {
                throw new IllegalArgumentException("Copies Available can not be less than zero!");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
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
        if (days % 7 != 0 || weeks == 0) {
            weeks++;
        }

        return (int) (FEE_PER_WEEK * weeks);
    }

    public int returnBook(String userid) {
        int index = rentedToSearch(userid);
        if (rentedCopies == 0 || index == -1) {
            return 0;
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
            return fees;
        }
    }
    @Override
    public String toString() {
        return "Book{\n" +
                "id=" + id + '\n' +
                ", name='" + name + "'\n" +
                ", author='" + author + "'\n" +
                ", publisher='" + publisher + "'\n" +
                ", category='" + category + "'\n" +
                ", subcategory='" + subcategory + "'\n" +
                ", edition=" + edition +  '\n' +
                ", copiesAvailable=" + copiesAvailable +  '\n' +
                ", rentedCopies=" + rentedCopies +  '\n' +
                ", rentedTo=" + rentedTo +  '\n' +
                ", rentingQueue=" + rentingQueue +  '\n' +
                '}';
    }

}
