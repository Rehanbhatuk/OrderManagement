# OrderFlow – Order & Inventory Management Frontend

A responsive admin interface built with HTML5, CSS3, JavaScript, jQuery and Bootstrap 5. It calls the supplied Spring Boot REST endpoints directly; it does not use a frontend framework or mock database records.

## Run the application

1. Start MySQL and ensure the `order_management` database and required tables exist.
2. Start the Spring Boot application from IntelliJ or run `mvn spring-boot:run` from the backend project directory.
3. Confirm the backend is available at `http://localhost:8080`.
4. Open this `frontend` folder in VS Code and launch `index.html` with Live Server (or serve it using any static HTTP server). Do not open the HTML with `file://`.
5. If the frontend is served from a different origin/port, configure Spring Boot CORS for that origin. Alternatively, copy the frontend files into the backend's `src/main/resources/static/` directory and open `http://localhost:8080/index.html` to use same-origin requests.
6. If the backend host/port changes, update `API_BASE_URL` in `js/config.js`.

The HTML loads Bootstrap and jQuery from CDNs, so internet access is required unless you download and serve those libraries locally.

## Features implemented

- Dashboard with live active-customer count and date-filtered order lookup.
- Customer create, search by ID/email, active-customer list and update (where the backend returns an ID).
- Product create, lookup by ID/name/category/price range, and stock update.
- Category creation.
- Order creation with multiple products, live product-price lookup, client-side total calculation, quantity checks against returned stock, and date-range search.
- Payment lookup by status, order payment history and unpaid-order list.
- Shared AJAX/error/toast helpers, loading states, basic form validation and responsive sidebar.

## Known backend-dependent gaps

See [API documentation and backend limitations](docs/API-DOCUMENTATION.md). In short: category list, all-products list, all-orders list, dashboard aggregates, low-stock inventory scan and payment creation are not supported by the current backend. No fake data is displayed for these features.

## Notes

- The API base URL is centralized in `js/config.js`.
- The frontend does not change backend source code.
- The frontend shows backend validation payloads even where the current exception handler returns them with HTTP 500.
- Product low-stock highlighting uses a visible UI threshold of 5 units; adjust this in `js/products.js` if your project defines a different rule.
