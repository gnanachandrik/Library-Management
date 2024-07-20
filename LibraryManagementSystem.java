import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryManagementSystem {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        while (true) {
            System.out.println();
            System.out.println("Library Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Search Book");
            System.out.println("4. Check Out Book");
            System.out.println("5. Return Book");
            System.out.println("6. List Available Books");
            System.out.println("7. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    searchBook();
                    break;
                case 4:
                    checkOutBook();
                    break;
                case 5:
                    returnBook();
                    break;
                case 6:
                    listAvailableBooks();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addBook() {
        System.out.println("Enter book title:");
        String title = scanner.nextLine();
        System.out.println("Enter book author:");
        String author = scanner.nextLine();
        System.out.println("Enter book ISBN:");
        String ISBN = scanner.nextLine();
        Book book = new Book(title, author, ISBN);
        library.addBook(book);
        System.out.println("Book added.");
    }

    private static void removeBook() {
        System.out.println("Enter book ISBN to remove:");
        String ISBN = scanner.nextLine();
        library.removeBook(ISBN);
        System.out.println("Book removed.");
    }

    private static void searchBook() {
        System.out.println("Enter book title to search:");
        String title = scanner.nextLine();
        Book book = library.searchBook(title);
        if (book != null) {
            System.out.println("Book found: " + book.getTitle() + " by " + book.getAuthor());
        } else {
            System.out.println("Book not available.");
        }
    }

    private static void checkOutBook() {
        System.out.println("Enter book ISBN to check out:");
        String ISBN = scanner.nextLine();
        System.out.println("Enter user ID:");
        String userId = scanner.nextLine();
        User user = library.findUser(userId);
        if (user != null) {
            library.checkOutBook(ISBN, user);
            System.out.println("Book checked out.");
        } else {
            System.out.println("User not found.");
        }
    }

    private static void returnBook() {
        System.out.println("Enter book ISBN to return:");
        String ISBN = scanner.nextLine();
        System.out.println("Enter user ID:");
        String userId = scanner.nextLine();
        User user = library.findUser(userId);
        if (user != null) {
            library.returnBook(ISBN, user);
            System.out.println("Book returned.");
        } else {
            System.out.println("User not found.");
        }
    }

    private static void listAvailableBooks() {
        System.out.println("Available books:");
        for (Book book : library.listAvailableBooks()) {
            System.out.println(book.getTitle() + " by " + book.getAuthor());
        }
    }

    // Inner classes

    static class Book {
        private String title;
        private String author;
        private String ISBN;
        private boolean available;

        public Book(String title, String author, String ISBN) {
            this.title = title;
            this.author = author;
            this.ISBN = ISBN;
            this.available = true;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getISBN() {
            return ISBN;
        }

        public boolean isAvailable() {
            return available;
        }

        public void borrow() {
            if (available) {
                available = false;
            } else {
                System.out.println("Book is already borrowed.");
            }
        }

        public void returnBook() {
            available = true;
        }
    }

    static class User {
        private String name;
        private String userId;
        private List<Book> borrowedBooks;

        public User(String name, String userId) {
            this.name = name;
            this.userId = userId;
            this.borrowedBooks = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public String getUserId() {
            return userId;
        }

        public void borrowBook(Book book) {
            borrowedBooks.add(book);
            book.borrow();
        }

        public void returnBook(Book book) {
            borrowedBooks.remove(book);
            book.returnBook();
        }

        public List<Book> getBorrowedBooks() {
            return borrowedBooks;
        }
    }

    static class Library {
        private List<Book> books;
        private List<User> users;

        public Library() {
            books = new ArrayList<>();
            users = new ArrayList<>();
        }

        public void addBook(Book book) {
            books.add(book);
        }

        public void removeBook(String ISBN) {
            books.removeIf(book -> book.getISBN().equals(ISBN));
        }

        public Book searchBook(String title) {
            for (Book book : books) {
                if (book.getTitle().equalsIgnoreCase(title) && book.isAvailable()) {
                    return book;
                }
            }
            return null;
        }

        public void checkOutBook(String ISBN, User user) {
            Book book = books.stream().filter(b -> b.getISBN().equals(ISBN)).findFirst().orElse(null);
            if (book != null && book.isAvailable()) {
                user.borrowBook(book);
            } else {
                System.out.println("Book not available.");
            }
        }

        public void returnBook(String ISBN, User user) {
            Book book = books.stream().filter(b -> b.getISBN().equals(ISBN)).findFirst().orElse(null);
            if (book != null && !book.isAvailable()) {
                user.returnBook(book);
            } else {
                System.out.println("Book was not borrowed.");
            }
        }

        public List<Book> listAvailableBooks() {
            List<Book> availableBooks = new ArrayList<>();
            for (Book book : books) {
                if (book.isAvailable()) {
                    availableBooks.add(book);
                }
            }
            return availableBooks;
        }

        public void addUser(User user) {
            users.add(user);
        }

        public User findUser(String userId) {
            return users.stream().filter(user -> user.getUserId().equals(userId)).findFirst().orElse(null);
        }
    }
}
