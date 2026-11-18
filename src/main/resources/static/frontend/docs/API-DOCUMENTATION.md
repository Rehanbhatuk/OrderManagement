# Backend API map (verified against the supplied Spring Boot source)

Base URL: `http://localhost:8080`

| Module | Endpoint | Method | Required parameters / body | Request JSON / response |
|---|---|---:|---|---|
| Customers | `/customer/add` | POST | JSON body | `Customer`: `firstName`, `lastName`, `email`, `phoneNumber`, `registrationDate`, `status`, `id`. Returns text. |
| Customers | `/customer/get-by-email/{email}` | GET | Path variable `email` | Returns `Customer`; 404 text if missing. The service mapper does not populate `id`. |
| Customers | `/customer/get-by-id/{id}` | GET | Path variable `id` | Returns `Customer`; 404 text if missing. The service mapper does not populate `id`. |
| Customers | `/customer/update` | POST | JSON body | Same `Customer` DTO; `id` identifies row. Returns text. SQL updates name, phone, status, email; registration date is not updated. |
| Customers | `/customer/get/active` | GET | None | `List<Customer>`; service mapper sets `id`, but reads `phone_number` while other customer SQL uses `phone_no`; status is not set in the mapper. |
| Products | `/product/add` | POST | JSON body | `Product`: `productName`, `description`, `categoryId`, `price`, `stockQuantity`. Returns empty body. |
| Products | `/product/update/stock?productId={id}&newStock={n}` | POST | Query parameters `productId`, `newStock` | No JSON body; returns empty body. |
| Products | `/product/by-name/{productName}` | GET | Path variable `productName` | `Product` or null. |
| Products | `/product/by-id/{productId}` | GET | Path variable `productId` | `Product` or null. |
| Products | `/product/by-category/{categoryId}` | GET | Path variable `categoryId` | `List<Product>`. |
| Products | `/product/between-price?fromPrice={a}&toPrice={b}` | GET | Query parameters `fromPrice`, `toPrice` | `List<Product>`. |
| Products | `/product/price-by-id/{productId}` | GET | Path variable `productId` | Numeric price. |
| Categories | `/add/category` | POST | JSON body | `Category`: `categoryName`, `description`, `activeStatus`. Returns empty body. |
| Orders | `/order/add` | POST | JSON body | `OrderDto`: `customerId`, `orderDate`, `totalAmount`, `status`, `productsList`. Each list item uses `Product`: `productId`, `quantity`, `price`. Returns text. |
| Orders | `/order/get-order-between-dates/{fromDate}/{toDate}` | GET | Path dates in `yyyy-MM-dd` format | `List<OrderDto>`; order headers only, no products list populated. |
| Payments | `/payments/make` | POST | No parameters and no request body accepted | Returns empty body. Controller constructs an empty `PaymentDto`; this cannot accept a frontend payment. |
| Payments | `/payments/get-by-status?status={status}` | GET | Query parameter `status` | `List<PaymentDto>`; service matches `payment_status LIKE status%`. DTO mapper does not return status or `productId`. |
| Payments | `/payments/get-payment-history?orderId={id}` | GET | Query parameter `orderId` | `List<PaymentDto>`; includes payment ID, order ID, amount, method, date. |
| Payments | `/payments/get-unpaid-orders` | GET | None | `List<OrderDto>`; maps pending amount into `totalAmount`. Current SQL uses an INNER JOIN, so orders with no payment row are not included. |

## JSON examples derived from DTOs

Customer create/update:

```json
{
  "firstName": "Aarav",
  "lastName": "Shah",
  "email": "aarav@example.com",
  "phoneNumber": "9876543210",
  "registrationDate": "2026-11-17",
  "status": "ACTIVE",
  "id": 0
}
```

Product create:

```json
{
  "productName": "Keyboard",
  "description": "USB keyboard",
  "categoryId": 1,
  "price": 799.00,
  "stockQuantity": 20
}
```

Order create (the UI first fetches each product to obtain its current price):

```json
{
  "customerId": 1,
  "orderDate": "2026-11-17",
  "totalAmount": 1598.00,
  "status": "PENDING",
  "productsList": [
    { "productId": 1, "quantity": 2, "price": 799.00 }
  ]
}
```

Category create:

```json
{
  "categoryName": "Accessories",
  "description": "Computer accessories",
  "activeStatus": "ACTIVE"
}
```

## Backend limitations / defects observed

1. No dashboard aggregate endpoints; no all-products, all-categories, or all-orders endpoint. Therefore complete totals, a true recent-orders feed, low-stock scan, and category/product master dropdown cannot be populated. The dashboard counts active customers only and lets the user query orders by date.
2. Category retrieval is not implemented. Only category creation exists.
3. Payment creation cannot work from a UI because `makePayment()` accepts no body and creates a blank DTO. Its validator requires order ID, amount, payment method and product ID. The payment service insert itself stores order, amount, method and status but not product ID.
4. `RestParameterInvalidException` is returned with HTTP 500 by `GlobalExceptionHandler`, not 400. The frontend displays the returned `FieldError` array, but the HTTP status is semantically incorrect.
5. Customer lookup mappers do not set the customer ID. The frontend can restore the ID for an ID-based lookup, but an email-based lookup cannot safely launch update. The active-customer mapper also reads `phone_number`, while insert/update and other mappers use `phone_no`; confirm the actual database column name.
6. `CategoryValidator` currently rejects alphanumeric category names because its condition uses `StringUtils.isAlphanumeric(categoryName)` without negation. This is backend validation behavior and has not been changed here.
7. Product validation requires an alphanumeric product name (spaces and punctuation are rejected) and stock quantity greater than zero. Customer validation requires alphabetic first/last names and numeric phone numbers.
8. Order service trusts the frontend-supplied product price and total amount. The UI fetches current prices before building an order, but backend-side recalculation is recommended to prevent tampered totals. The order service also decrements stock after inserting order items.
9. `getUnpaidOrders()` uses an INNER JOIN and returns orders with an outstanding payment row, not necessarily orders that have never been paid or have no payment record.
10. Product and Order controller exception handlers can convert errors to a generic HTTP 500 response, obscuring the original validation message.
11. The order creation service is not visibly transactional in the supplied code. A failure partway through order/item/stock updates could leave partial data; add a transaction boundary in the backend before production use.
12. The frontend uses a low-stock visual marker at 5 units or fewer only for products returned by a search. This is a presentation threshold, not a backend-defined business rule.
