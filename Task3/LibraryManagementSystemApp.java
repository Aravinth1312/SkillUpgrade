import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// Main class
public class LibraryManagementSystemApp {
    public static void main(String[] args) {
        LibraryManagementSystem lms = new LibraryManagementSystem();

        // Add some books
        lms.addBook(new Book("1", "1984", "George Orwell"));
        lms.addBook(new Book("2", "To Kill a Mockingbird", "Harper Lee"));

        // Add some patrons
        lms.addPatron(new Patron("1", "Alice Smith"));
        lms.addPatron(new Patron("2", "Bob Johnson"));

        // Check out a book
        lms.checkoutBook("1", "1");

        // Return the book
        lms.returnBook("1");
    }
}

// Book class
class Book implements Serializable {
    private String id;
    private String title;
    private String author;
    private boolean isCheckedOut;

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isCheckedOut = false;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        isCheckedOut = checkedOut;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isCheckedOut=" + isCheckedOut +
                '}';
    }
}

// Patron class
class Patron implements Serializable {
    private String id;
    private String name;

    public Patron(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Patron{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

// LibraryTransaction class
class LibraryTransaction implements Serializable {
    private Book book;
    private Patron patron;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public LibraryTransaction(Book book, Patron patron, LocalDate checkoutDate, LocalDate dueDate) {
        this.book = book;
        this.patron = patron;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    public Book getBook() {
        return book;
    }

    public Patron getPatron() {
        return patron;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }

    public double calculateFine() {
        if (!isOverdue()) {
            return 0.0;
        }
        long overdueDays = LocalDate.now().toEpochDay() - dueDate.toEpochDay();
        return overdueDays * 0.50; // Fine is $0.50 per day
    }

    @Override
    public String toString() {
        return "LibraryTransaction{" +
                "book=" + book +
                ", patron=" + patron +
                ", checkoutDate=" + checkoutDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                '}';
    }
}

// LibraryManagementSystem class
class LibraryManagementSystem {

    private Map<String, Book> books;
    private Map<String, Patron> patrons;
    private Map<String, LibraryTransaction> transactions;
    private static final String DATA_FILE = "library_data.ser";

    public LibraryManagementSystem() {
        books = new HashMap<>();
        patrons = new HashMap<>();
        transactions = new HashMap<>();
        loadData();
        System.out.println("LibraryManagementSystem initialized.");
    }

    public void addBook(Book book) {
        books.put(book.getId(), book);
        System.out.println("Added book: " + book);
    }

    public void addPatron(Patron patron) {
        patrons.put(patron.getId(), patron);
        System.out.println("Added patron: " + patron);
    }

    public void checkoutBook(String bookId, String patronId) {
        Book book = books.get(bookId);
        Patron patron = patrons.get(patronId);

        if (book == null || patron == null || book.isCheckedOut()) {
            System.out.println("Cannot checkout book. Book or patron not found, or book is already checked out.");
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusWeeks(2); // Due in 2 weeks
        LibraryTransaction transaction = new LibraryTransaction(book, patron, today, dueDate);
        transactions.put(bookId, transaction);
        book.setCheckedOut(true);
        System.out.println("Checked out book: " + book);
        saveData();
    }

    public void returnBook(String bookId) {
        LibraryTransaction transaction = transactions.get(bookId);

        if (transaction == null) {
            System.out.println("No record of this book being checked out.");
            return;
        }

        Book book = transaction.getBook();
        book.setCheckedOut(false);
        transaction.setReturnDate(LocalDate.now());

        if (transaction.isOverdue()) {
            double fine = transaction.calculateFine();
            System.out.println("The book is overdue. Fine: $" + fine);
        } else {
            System.out.println("The book has been returned on time.");
        }

        transactions.remove(bookId);
        saveData();
    }

    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(books);
            out.writeObject(patrons);
            out.writeObject(transactions);
            System.out.println("Data saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File dataFile = new File(DATA_FILE);
        if (dataFile.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                books = (Map<String, Book>) in.readObject();
                patrons = (Map<String, Patron>) in.readObject();
                transactions = (Map<String, LibraryTransaction>) in.readObject();
                System.out.println("Data loaded from file.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data: " + e.getMessage());
            }
        } else {
            // Initialize empty data structures if the file does not exist
            books = new HashMap<>();
            patrons = new HashMap<>();
            transactions = new HashMap<>();
            System.out.println("No data file found. Initialized empty data structures.");
        }
    }
}
