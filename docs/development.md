# Java LiMS Development guides and documents

Before start developing or contributing to this project, you should follow some rules in this project

## Design Pattern 

We have looked through several design patterns, we have decided to use the MVC (Model-View-Controller) design
pattern.This pattern is widely used in the industry, it is easy to understand and implement. You can find more
information about the MVC design pattern [here](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller).

Pros:
- Easy to understand
- Coding reusability, readability and maintainability
- Separation of concerns

Cons:
- Not suitable for small projects
- Can be complex for beginners
- Package structure can be complex

Currently MVC pattern is implemented in this project as follows:

- Model: com.lims.model
- View: com.resources.view
- Controller: com.lims.controller (this is view controller, logic code is implemented directly in the view controller)

This is not the final implementation of the MVC pattern, it may change down the road because we detected some issues in
development and manatenance of the project.

## Coding Standards

We have decided to use the Google Java Style Guide for this project. This is because it is one of the requirements in
project grading. You can find the style guide [here](https://google.github.io/styleguide/javaguide.html).

## Technical stack

- JavaFX    (for the GUI)
- SQLite    (for the database)
- JDBC      (for the database connection)

## Database

We have decided to use SQLite for this project. SQLite is a lightweight database and no hosting is required. You can
find more information about SQLite [here](https://www.sqlite.org/index.html).

Database entity relationship diagram (ERD) can be found [here](/docs/Database%20ERD.jpg).
Entities we included in the database are:

- User
- Librarian (Inherits from User)
- Book
- BorrowDetail (Many-to-Many relationship between User and Book)

Note: This is not the final ERD, it may change down the road.

## Database Manager Development Manual

We have created a database manager class to handle all the database operations. This class is a singleton class, which
means only one instance of this class can be created. You can find the
class [here](/com/lims/model/DatabaseManager.java). DAO (Data Access Object) pattern is used in this class. You can find
more information about the DAO pattern [here](https://en.wikipedia.org/wiki/Data_access_object).

Development manual:

0. Import static methods

```java
import static com.lims.dao.DatabaseManager.*;
```

1. Get all books from the database

```java
List<Book> books = getAllBook();
```

- Parameters: None (May implement pagination in the future)
- Return type: Array list of Book objects

2. Get book by ISBN

```java
Book book = getBookByISBN(isbn;
```

- Parameters:
    - ISBN: String (ISBN of the book)
- Return type: Book object

3. Add new book to the database

```java
addBookToDatabase(book);
```

- Parameters:
    - Book: Book object
- Return type: None

4. Delete book from database

```java
deleteBookFromDatabase(isbn)
```

- Parameters:
  - ISBN: String (ISBN of the book)
- Return type: None

5. Update book in the database

```java
updateBookInDatabase(book);
```

- Parameters:
    - Book: Book object
- Return type: None
- Note: This method take book object and get the ISBN from the object and update the book in the database (the ISBN in the object should be the same as the ISBN in the database)