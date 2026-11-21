// =====================================================
// 1. LOAD DASHBOARD CUSTOMER METRIC
// =====================================================

function loadDashboard() {

    // Call the backend API to get all active customers.
    apiRequest({
        url: "/customer/get/active",
        method: "GET"
    })

        // Execute when the API request is successful.
        .done(data => {

            // Check whether the API response is an array.
            // If yes, display the total number of customers.
            // Otherwise, display a dash (—).
            $("#metricCustomers").text(
                Array.isArray(data) ? data.length : "—"
            );

        })

        // Execute when the API request fails.
        .fail(() => {

            // Display a dash if something goes wrong.
            $("#metricCustomers").text("—");

        });

}


// =====================================================
// 2. RENDER ORDER TABLE ROWS
// =====================================================

function renderOrderRows(orders) {

    // Convert the orders array into HTML table rows.
    //
    // orders.map() iterates over every order object.
    // For each order, it generates one <tr> containing
    // the order details.

    return renderTable(

        // Table column headings.
        [
            "Order ID",
            "Customer",
            "Date",
            "Amount",
            "Status"
        ],

        // Generate table rows from the orders array.
        orders.map(o => `

            <tr>

                <!-- Display Order ID -->
                <td class="fw-semibold">
                    #${escapeHtml(o.orderId)}
                </td>

                <!-- Display Customer ID -->
                <td>
                    ${escapeHtml(o.customerId)}
                </td>

                <!-- Display Order Date -->
                <td>
                    ${dateOnly(o.orderDate)}
                </td>

                <!-- Display Total Order Amount -->
                <td>
                    ${money(o.totalAmount)}
                </td>

                <!-- Display Order Status -->
                <td>
                    <span class="status-pill">
                        ${escapeHtml(o.status || "—")}
                    </span>
                </td>

            </tr>

        `)

    );

}


// =====================================================
// 3. DASHBOARD ORDER SEARCH FORM
// =====================================================

// Execute when the HTML document is ready.
$(function () {


    // -------------------------------------------------
    // A. ATTACH SUBMIT EVENT TO ORDER SEARCH FORM
    // -------------------------------------------------

    $("#dashboardOrderSearch").on("submit", function (e) {

        // Prevent the browser's default form submission.
        e.preventDefault();


        // -------------------------------------------------
        // B. GET FORM DATA
        // -------------------------------------------------

        // Get the starting date entered by the user.
        const from = $("#dashFrom").val();

        // Get the ending date entered by the user.
        const to = $("#dashTo").val();


        // -------------------------------------------------
        // C. VALIDATE DATE RANGE
        // -------------------------------------------------

        // Compare dates in YYYY-MM-DD format.
        // If the starting date is greater than the ending date,
        // show a warning and stop further execution.

        if (from > to) {

            showToast(
                "From date must not be after to date.",
                "warning"
            );

            return;
        }


        // -------------------------------------------------
        // D. DISPLAY LOADING MESSAGE
        // -------------------------------------------------

        // Display a spinner while waiting for the API response.
        $("#recentOrders").html(`
            
            <div class="loading-state">

                <span class="spinner-border spinner-border-sm"></span>

                Loading orders…

            </div>

        `);


        // -------------------------------------------------
        // E. CALL BACKEND API
        // -------------------------------------------------

        // Send a GET request to the backend.
        //
        // from and to are inserted into the URL.
        // encodeURIComponent() safely encodes each value.

        apiRequest({

            url: `/order/get-order-between-dates/${encodeURIComponent(from)}/${encodeURIComponent(to)}`,

            method: "GET"

        })


            // -------------------------------------------------
            // F. HANDLE SUCCESS RESPONSE
            // -------------------------------------------------

            .done(data => {

                // API successfully returned the orders.
                //
                // data contains the response from Spring Boot.
                //
                // data || [] ensures that an empty array is used
                // if the response is null or undefined.

                // Pass the orders to renderOrderRows indirectly
                // through the HTML generated by renderOrderRows.
                $("#recentOrders").html(
                    renderOrderRows(data || [])
                );

            })


            // -------------------------------------------------
            // G. HANDLE FAILURE RESPONSE
            // -------------------------------------------------

            .fail(xhr => {

                // Handle API errors.
                // xhr contains the HTTP response information.
                //
                // handleError() displays an appropriate error
                // message inside the recentOrders section.

                handleError(xhr, "#recentOrders");

            });

    });

});