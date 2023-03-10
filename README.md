# shopping-cart

This project is the solution for a technical problem, presented as part of a selective process for a position as a back-end engineer. It follows an architecture similar to the typical MVC normally seen in Spring applications. It has both Controller and Service layers and also an Entity package, but it lacks a Repository layer because the app doesn't connect to any database and runs completely in the computer memory. There are peripheral packages in the application, such as constant (for Enums), dto (for Requests and Responses) and exception (for Exceptions), to keep the classes organized and where they should be.  

To be able to run and analyze the application, you'll need to have both Java (11+) and Maven installed in your environment. It's also recommended the use of some IDE, but not mandatory. After having your environment all set up, run the following command on your terminal:

```
mvn clean install
mvn spring-boot:run
```

The app runs on port 5353 and the API has the following endpoints:

* `/cart`
  * `POST /add_item` -> use it to add items to the shopping cart
  * `DELETE /remove_item` -> use it to remove items from the shopping cart
  * `DELETE /empty_cart` -> use it to empty the shopping cart (practical means for re-testing)
  * `GET /close_order` -> use it to close the order and get the details and final price

The only endpoints that require a body are POST /cart/add_item and DELETE /remove_item and they share the same body as shown below:
```
{
    "itemId": 1, // item ID as listed in the table below
    "amount": 3  // amount desired for this item
}
```

So, you'll only be able to add or remove one type of product (and its amount) at a time.

When you're done adding / removing items, call the GET /cart/close_order endpoint to get the final price and the list of the products.

If you wish to reset the cart, you may call the DELETE /cart/empty_cart endpoint so you don't have to shut down the app and run it again. :D

## Table of products:

| Product ID | Name    | Price     |
|------------|---------|-----------|
| 1          | T-shirt | USD 12.99 |
| 2          | Jeans   | USD 25.00 |
| 3          | Dress   | USD 20.65 |

The three products listed above are the only ones accepted and the application has their data stored in an Enum. Trying to use different IDs on the requests to the app won't add any product to the cart, but return an error message instead.