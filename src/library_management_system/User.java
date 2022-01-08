package library_management_system;

import java.util.ArrayList;

public class User implements java.io.Serializable{
    String name, email, username;
    long NIC;
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

    public User(int id, long NIC, String name, String email) {
        this.name = name;
        this.id = id;
        this.username = name + "@" + id;
        this.email = email;
        this.NIC = NIC;
    }

    public User(long NIC, String name, String password) {
        this(++currentId, NIC, name, password);
    }

    // Due to time shortage, can not get it to work. Commented here to help in DBMS project
    /*
    public Boolean login() {
        Random rd = new Random();
        Scanner in = new Scanner(System.in);
        int pin = rd.nextInt(999999);
        sendPin(pin);
        System.out.println("Enter the pin: ");
        int inPin = in.nextInt();
        if (pin == inPin) {
            return true;
        }
        return false;
    }

    private void sendPin(int pin) {
        final String host = "smtp.gmail.com";
        Properties sysProps = System.getProperties();


//    email.properties content
//    email=your email
//    password=email's password

        Properties email = new Properties();
        try (FileReader in = new FileReader("email.properties")){
            p.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String email = p.getProperty("email");
        String password = p.getProperty("password");

        sysProps.setProperty("mail.smtp.host", host);
        sysProps.setProperty("mail.smtp.port", "465");
        sysProps.setProperty("mail.smtp.ssl.enable", "true");
        sysProps.setProperty("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email , password);
            }
        });
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(email));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.email));
            // Set Subject: header field
            message.setSubject("Library Management System Pin");
            // Now set the actual message
            message.setText("This is your pin code:\n" + pin);
            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
*/

    private int searchUserBooks(Book book) {
        for (int i = 0; i < user_books.size(); i++) {
            if (user_books.get(i).book == book)
                return i;
        }
        return -1;
    }



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

}