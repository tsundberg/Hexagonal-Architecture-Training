# Hexagonal Architecture Training

Training material for a Hexagonal Architecture Example.
It will guide you through a step-by-step example where you will
implement a Library. The library as such is not important. The important part is the structure and the way different
classes communicate with each other.

If you follow the instructions, you will en up using Test-Driven Development, TDD, when you implement the library.

## Background

There are no strict guidelines one can follow when implementing a Hexagonal Architecture. This is by design from the
creator Alistair Cockburn. This gives the developers a lot of freedom to do whatever they feel is right. However, this
also introduces some potential problems. Such as what names should be used for the different parts working together?

Finding the correct names is not easy. In this example, there are a few naming conventions that can be argued
reasonable.

## Glossary

* Domain objects: they represent the current nouns in the problem we are trying to solve.
  Be liberal when you create domain objects. They can always be removed if they are not needed. Removing is easier than
  adding.

* Controller: something receiving a web request. It lives in a package indicating its version, `v1` is a common
  example.

* XXXRequest: a payload with whatever an external sends to a controller. XXX represents a reasonable domain name for the
  payload. It lives in the same package as the controller. It is probably implemented as a Java record, and it is most
  likely not public as it is only the controller that will use it.

* XXXResponse: a payload with whatever an external system gets from a controller. XXX represents a reasonable domain
  name for
  the payload. It lives in the same package as the controller. It is probably implemented as a Java record, and it is
  most likely not public as it is only the controller that will use it.

* Service: a class with methods that solves the current problem. It is only aware of domain objects. It may use other
  services. It is common that a service uses one repository. It is uncommon that a service uses two repositories.

* Repository: a port to an external storage, probably a database. It is implemented as a Java interface and used
  everywhere anything needs to store something. Typically, a service uses a repository.

* XXXRepository: a concrete implementation of the repository. It can be an in memory implementation or a SQL
  implementation. Or anything else that is able to store data such as a file-based storage.

## Getting started

This is a Java example. You must have Java 17 installed in order for it to work.

Clone this repository with the command

    git clone git@github.com:tsundberg/Hexagonal-Architecture-Training.git

Build the example with Maven

    ./mvnw install

Run the application

    java -jar ./target/Hexagonal-Architecture-Training-1.0.0-SNAPSHOT-jar-with-dependencies.jar

## Instructions

We will build a book Library. This library contains books and borrowers borrowing books.

### Add books

Create a controller test that creates and verifies that a book is available in the package `se.thinkcode.book.v1` in the
test source.

```
class BookControllerTest 
```

Create a controller that can create a book in the same package but in the production code.

```
public class BookController
```

Here is an example of a test:

```
package se.thinkcode.book.v1;

import org.junit.jupiter.api.Test;
import se.thinkcode.book.Book;
import se.thinkcode.book.BookService;
import se.thinkcode.book.ISBN;
import se.thinkcode.book.Title;
import spark.Request;
import spark.RequestStub;
import spark.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BookControllerTest {
    private final BookService service = new BookService();
    private final BookController controller = new BookController(service);

    @Test
    void should_create_a_book() {
        BookRequest bookRequest = new BookRequest("1984", "9788119214341");
        Request request = new RequestStub.RequestBuilder()
                .withPayload(bookRequest)
                .build();
        Response response = mock(Response.class);


        controller.handle(request, response);


        ISBN isbn = new ISBN("isbn");
        Book book = service.getBook(isbn);
        assertThat(book.title()).isEqualTo(new Title("1984"));
        assertThat(book.isbn()).isEqualTo(new ISBN("9788119214341"));
        verify(response).type("application/json");
        verify(response).status(200);
    }
}
```        

It should get a request object called `BookRequest` with the fields needed that is implemented in the same package as
the controller,

A book request can start its life as

```
record BookRequest(String title,
                          String isbn) {
}
```

It should not be a public record. The only user is the controller in the same package.

The controller should transform the request object to a domain object using the method `toModel` on the request object.

Don't add database support yet. Add it later when the problem is better understood.

### A book should be able to have an author

Add author when creating a book.

The request object needs another field:

```
record BookRequest(String title,
                          String author,
                          String isbn) {
}
```

### Store the books in a database

Create an interface `BookRepository` in the package `se.thinkcode.book` with the methods needed.
The repository should only use domain objects. The database is blissfully unaware of the external world of web
applications, so it should never be exposed for the vehicle used to send information from a web application. That is, it
should never see the `BookRequest`.

Create an in memory implementation of the database.
We probably don't understand the domain good enough yet to implement something more complicated.

### Support for borrowers

Create a controller test that creates and verifies that a borrower is available in the
package `se.thinkcode.borrower.v1` in the
test source.

```
class BottowerControllerTest 
```

Create a controller that can create a borrower in the same package but in the production code.

```
public class BottowerController
```

Here is an example test:

```
package se.thinkcode.borrower.v1;

import org.junit.jupiter.api.Test;
import se.thinkcode.borrower.Borrower;
import se.thinkcode.borrower.BorrowerService;
import se.thinkcode.borrower.Name;
import spark.Request;
import spark.RequestStub;
import spark.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BottowerControllerTest {

    private final BorrowerService service = new BorrowerService();
    private final BorrowerController controller = new BorrowerController(service);

    @Test
    void should_create_a_borrower() {
        BorrowerRequest borrowerRequest = new BorrowerRequest("Peter");
        Request request = new RequestStub.RequestBuilder()
                .withPayload(borrowerRequest)
                .build();
        Response response = mock(Response.class);


        controller.handle(request, response);


        Borrower borrower = service.getBorrower(new Name("Peter"));
        Name expected = new Name("Peter");
        assertThat(borrower.name()).isEqualTo(expected);
        verify(response).type("application/json");
        verify(response).status(200);
    }
}
```

It should get a request object called `BorrowerRequest` with the fields needed.
Implemented in the same package as the controller.

A borrower request can start its life as

```
record BorrowerRequest(String name) {
}
```

### Store the borrowers in a database

Create an interface `BorrowerRepository` in the package `se.thinkcode.borrower` with the methods needed. Again, the
repository should only use domain objects.

We probably don't understand the domain good enough yet. Create an in memory implementation of the database and postpone
a more complicated implementation.

### Borrow a book

The main purpose of a library is to make books available to borrowers. Implement that a person can borrow a book.

Start with a test for checking out a book in the package `se.thinkcode.checkout.v1`. It may look like this:

```
package se.thinkcode.checkout.v1;

import org.junit.jupiter.api.Test;
import se.thinkcode.checkout.CheckoutService;
import se.thinkcode.infrastructure.Deserializer;
import spark.Request;
import spark.RequestStub;
import spark.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CheckoutControllerTest {
    private final Deserializer deserializer = new Deserializer();
    private final CheckoutService service = new CheckoutService();
    private final CheckoutController controller = new CheckoutController(service);

    @Test
    void a_borrower_should_checkout_a_book() {
        CheckoutRequest checkoutRequest = new CheckoutRequest("Peter", "9788119214341");
        Request request = new RequestStub.RequestBuilder()
                .withPayload(checkoutRequest)
                .build();
        Response response = mock(Response.class);


        String json = controller.handle(request, response);
        CheckoutResponse actual = deserializer.fromJson(json, CheckoutResponse.class);


        assertThat(actual.endDate()).isNotEmpty(); // we think that there should be a result, but we don't know what is a reasonable result at the moment
        verify(response).type("application/json");
        verify(response).status(200);
    }
}
```

As you can see in the test, we will use a `CheckoutRequest` and a `CheckoutResponse` for the communication.

The request may start its life as

```
record CheckoutRequest(String borrowerName,
                       String isbn) {
}
```

The response may start as

```
record CheckoutResponse(String endDate) {
}
```

It is very likely that the response should be something different that can be shown in a user interface.

How should a loan be represented in the system? Implement a reasonable way of keeping track of borrowed books in a
checkout service.

### Add proper database support

With a better understood problem, it is time to add a proper database support.

Start with a test that can create and retrieve a book from the in memory database.

```
package se.thinkcode.book;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class BookRepositoryTest {
    BookRepository repository = new InMemoryBookRepository();

    @Test
    void should_create_a_book() {
        Title title = new Title("1984");
        ISBN isbn = new ISBN("946358409315789");
        Book expected = new Book(title, isbn);

        repository.create(expected);
        Book actual = repository.getBook(isbn);

        assertThat(actual).isEqualTo(expected);
    }
}
```

This test should pass immediately because the controller test above drove it to be implemented correctly.

#### Add SQL support

With a passing test for the in memory database, it is time to add proper SQL support.

The first step is to refactor the test above and turn it into an abstract class that must be subclassed with a driving
test.

Make the test class abstract like this:

```
public abstract class BookRepositoryTest {
    BookRepository repository;
        
        ...
}
```

The field repository now must be supplied from a subclass.
This test can't be executed, you need to add a driving class like this:

```
public class InMemoryBookRepositoryTest extends BookRepositoryTest {

    public InMemoryBookRepositoryTest() {
        repository = new InMemoryBookRepository();
    }
}
```

This should still pass.

The next step is to add a proper SQL driver. It can be implemented as

```
public class SQLBookRepositoryIT extends BookRepositoryTest {
    public SQLBookRepositoryIT() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        repository = new SQLBookRepository(databaseConnection);
    }
}
```

Note that the class ends with `IT` and not `Test`. This is a Maven feature where tests can be separated in fast and slow
tests and are executed in different build phases. Classes ending with `Test` will be executed before classes ending
with `IT`. If you are wondering, `IT` stands for `integration tests` that are expected to connect to some slow external
source. Such as a database or a file system.

This will drive the actual SQL implementation for books.

### Add database support for Borrowers and Checkout

Continue with borrowers and create

* `BorrowerRepositoryTest`
* `InMemroyBorrowerRepositoryTest`
* `SqlBorrowerRepositoryIT`

and make them pass.

Finally, do the same for checkout implementing these tests:

* `CheckoutRepositoryTest`
* `InMemroyCheckoutRepositoryTest`
* `SqlCheckoutRepositoryIT`

and make them pass.
