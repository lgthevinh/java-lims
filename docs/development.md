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

### Development manual:

#### DAO Methods

0. Import static methods/class

```java
// import static com.lims.dao.DatabaseManager.*; (All methods are structurize in DAO Objects) (version 2)

import static com.lims.dao.*;
```

1. Get all books from the database

```java
List<Book> books = getAllBook();
```

- Parameters: None (May implement pagination in the future)
- Return type: Array list of Book objects

2. Get book by ISBN

```java
Book book = getBookByISBN(isbn);
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

6. Get all user from database

```java
List<User> userList = getAllUser();
```

- Parameters: None (May implement pagination in the future)
- Return type: Array list of User objects

7. Get user by user id (id)

```java
User user = getUserById(id);
```

- Parameters:
    - id: int (User id) This parameter should match the id in the database
- Return type: User object

8. Add new user to the database

```java
addUserToDatabase(user);
```

- Parameters:
    - User: User object
- Return type: None
- Note: If the user is already in the database, it will throw an exception (not implemented yet)

9. Delete user from the database

```java
deleteUserFromDatabase(id);
```

- Parameters:
    - id: int (User id) This parameter should match the id in the database
- Return type: None

10. Update user in the database

```java
updateUserInDatabase(user);
```

- Parameters:
    - User: User object
- Return type: None
- Note: This method take user object and get the id from the object and update the user in the database (the id in the object should be the same as the id in the database)

11. Get all librarian from the database

```java
List<Librarian> librarianList = getAllLibrarian();
```

- Parameters: None
- Return type: Array list of Librarian objects

12. Get librarian by librarian id (emp_id)

```java
Librarian librarian = getLibrarianById(emp_id);
```

- Parameters:
    - emp_id: int (Librarian id) This parameter should match the emp_id in the database
- Return type: Librarian object

13. Add new librarian to the database

```java
addLibrarianToDatabase(librarian);

// OR
addLirarianToDatabase(user); // Recommended
```

- Parameters:
  - Method 1: Librarian object
  - Method 2: User object (this is recommended due to complex logic when construct Librarian object)
- Return type: None
- Note: If the librarian is already in the database, it will throw an exception

14. Delete librarian from the database

```java
deleteLibrarianFromDatabase(librarian);
```

- Parameters:
    - Librarian: Librarian object
- Return type: None

16. Get all student from the database

```java
List<Student> studentList = getAllStudent();
```

- Parameters: None
- Return type: Array list of Student objects

17. Get student by student id (id)

```java
Student student = getStudentById(id);
```

- Parameters:
    - id: int (Student id) This parameter should match the id in the database
- Return type: Student object

18. Add new student to the database

```java
addStudentToDatabase(student);

// OR
addStudentToDatabase(user, studentId, school, major);
```

- Parameters:
  - Method 1: Student object
  - Method 2: User object, String, String, String
- Return type: None
- Note: If the student is already in the database, it will throw an exception

19. Delete student from the database

```java
deleteStudentFromDatabase(student);
```

- Parameters:
    - Student: Student object
- Return type: None
- Note: This method will delete the student from the database, 

20. Update student in the database

```java
updateStudentInDatabase(student);
```

- Parameters:
    - Student: Student object
- Return type: None
- Note: This method take student object and get the id from the object and update the student in the database (the id in the object should be the same as the id in the database)

21. Get all borrow details from the database

```java
List<BorrowDetail> borrowDetails = getAllBorrowDetail();
```

- Parameters: None
- Return type: Array list of BorrowDetail objects

22. Get borrow detail by borrow id

```java
BorrowDetail borrowDetail = getBorrowDetailById(borrow_id);
```

- Parameters:
    - borrow_id: int (Borrow id) This parameter should match the borrow_id in the database
- Return type: BorrowDetail object

23. Add new borrow detail to the database

```java
addBorrowDetailToDatabase(borrowDetail);
```

- Parameters:
    - BorrowDetail: BorrowDetail object
- Return type: None

24. Delete borrow detail from the database

```java
deleteBorrowDetailFromDatabase(borrowDetail);
```

- Parameters:
    - BorrowDetail: BorrowDetail object
- Return type: None
- Note: This method will delete the borrow detail from the database, be sure the id in the borrow detail object is the same as the id of the data in the database

25. Update borrow detail in the database

```java
updateBorrowDetailInDatabase(borrowDetail);
```

- Parameters:
    - BorrowDetail: BorrowDetail object
- Return type: None
- Note: This method take borrow detail object and get the id from the object and update the borrow detail in the database (the id in the object should be the same as the id in the database)

#### Authentication & Authorization methods/annotations

0. To be updated

#### Utils & APIs

0. To be updated

### Versioning

Some of the methods or features may not be implemented yet, we will keep note of the changes in the versioning in this document.

Current version: 1.1.0

- Version 1.0.0: Initial release
    - Basic CRUD operations for Book, User, Librarian, Student and BorrowDetail (all base on Object class, no primitive data type parameters implemented yet)
    - Singleton class
    - No exception handling
    - No pagination
    - Manual testing (no unit test created yet)

- Version 1.1.0: Additional changes:
  - Additional query function (getUserByEmail)