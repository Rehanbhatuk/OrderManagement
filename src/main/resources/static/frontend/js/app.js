// Stores the title of each page.
// The key represents the page name, and the value represents its display title.
const pageTitles = {
  dashboard: "Dashboard",
  customers: "Customers",
  products: "Products",
  categories: "Categories",
  orders: "Orders",
  payments: "Payments"
};


// =====================================================
// 1. NAVIGATION FUNCTION
// =====================================================

function navigate(page) {

  // Check whether the requested page exists.
  // If it doesn't exist, navigate to the dashboard.
  if (!pageTitles[page]) {
    page = "dashboard";
  }

  // Hide all pages by removing the 'active' class.
  $(".page-section").removeClass("active");

  // Display the selected page by adding the 'active' class.
  $("#page-" + page).addClass("active");


  // Remove the active state from all sidebar navigation links.
  $(".side-nav .nav-link").removeClass("active");

  // Highlight the sidebar link corresponding to the selected page.
  $(`.side-nav [data-page="${page}"]`).addClass("active");


  // Update the main page heading.
  $("#pageTitle").text(pageTitles[page]);


  // Update the browser URL hash.
  // Example: http://localhost:8080/#customers
  location.hash = page;


  // Close the sidebar (useful for mobile screens).
  $("#sidebar").removeClass("show");


  // Load data according to the selected page.
  if (page === "dashboard") {
    loadDashboard();
  }

  if (page === "customers") {
    loadCustomers();
  }
}


// =====================================================
// 2. DOCUMENT READY FUNCTION
// =====================================================

// This function executes when the HTML document is ready.
// All event listeners and initial page configurations
// are registered inside this function.

$(function () {


  // -------------------------------------------------
  // A. DISPLAY TODAY'S DATE
  // -------------------------------------------------

  // Get the current date.
  const currentDate = new Date();

  // Format the date according to Indian date conventions.
  // Example: 19 Nov 2026
  const formattedDate = new Intl.DateTimeFormat("en-IN", {
    dateStyle: "medium"
  }).format(currentDate);

  // Display the formatted date inside the element
  // having ID 'todayLabel'.
  $("#todayLabel").text(formattedDate);


  // -------------------------------------------------
  // B. SET DEFAULT DATE VALUES
  // -------------------------------------------------

  // Get today's date in YYYY-MM-DD format.
  const today = new Date().toISOString().slice(0, 10);

  // Set today's date as the default value for these inputs.
  $("#orderDate, #dashTo, #orderTo").val(today);


  // Get the current date and change its day to 1.
  // This gives us the first day of the current month.
  const monthStart = new Date();
  monthStart.setDate(1);

  // Convert the month start date into YYYY-MM-DD format.
  const firstDayOfMonth = monthStart.toISOString().slice(0, 10);

  // Set the first day of the month as the default
  // starting date for dashboard and order filters.
  $("#dashFrom, #orderFrom").val(firstDayOfMonth);


  // -------------------------------------------------
  // C. SIDEBAR NAVIGATION CLICK EVENT
  // -------------------------------------------------

  // Attach a click event to all sidebar links
  // having the 'data-page' attribute.
  $(".side-nav [data-page]").on("click", function (e) {

    // Prevent the default anchor/link behavior.
    e.preventDefault();

    // Get the value of the clicked element's data-page attribute.
    // Example: data-page="customers" returns "customers".
    const selectedPage = $(this).data("page");

    // Navigate to the selected page.
    navigate(selectedPage);

  });


  // -------------------------------------------------
  // D. NAVIGATION USING data-go ATTRIBUTE
  // -------------------------------------------------

  // Attach click events to elements having data-go.
  // These elements can navigate to different pages
  // from buttons or dashboard shortcuts.
  $("[data-go]").on("click", function () {

    // Get the destination page from the data-go attribute.
    const selectedPage = $(this).data("go");

    // Navigate to that page.
    navigate(selectedPage);

  });


  // -------------------------------------------------
  // E. MOBILE SIDEBAR TOGGLE
  // -------------------------------------------------

  // When the menu button is clicked,
  // toggle the 'show' class on the sidebar.
  $("#menuToggle").on("click", () => {

    $("#sidebar").toggleClass("show");

  });


  // -------------------------------------------------
  // F. REFRESH CURRENT PAGE
  // -------------------------------------------------

  // When the refresh button is clicked,
  // reload the currently selected page.
  $("#refreshPage").on("click", () => {

    // Get the current page name from the URL hash.
    // Example: #customers becomes customers.
    const currentPage = location.hash.replace("#", "");

    // If no page is specified, open the dashboard.
    // Otherwise, navigate to the current page again.
    navigate(currentPage || "dashboard");

  });


  // -------------------------------------------------
  // G. CUSTOMER SEARCH TYPE CHANGE EVENT
  // -------------------------------------------------

  // Execute this function whenever the customer
  // search type dropdown value changes.
  $("#customerSearchType").on("change", function () {

    // Get the selected search type.
    const searchType = this.value;

    // If the search type is ID:
    // Change placeholder to 'Enter customer ID'
    // and input type to number.
    //
    // Otherwise:
    // Change placeholder to 'Enter email address'
    // and input type to email.

    $("#customerSearchValue")
        .attr(
            "placeholder",
            searchType === "id"
                ? "Enter customer ID"
                : "Enter email address"
        )
        .attr(
            "type",
            searchType === "id"
                ? "number"
                : "email"
        );

  });


  // -------------------------------------------------
  // H. PRODUCT SEARCH TYPE CHANGE EVENT
  // -------------------------------------------------

  // Execute this function whenever the product
  // search type dropdown value changes.
  $("#productSearchType").on("change", function () {

    // Check whether the selected search type is price.
    const price = this.value === "price";


    // Show the 'To Price' input when searching by price.
    // Hide it for other search types.
    $("#productToWrap").toggleClass("d-none", !price);


    // Update the label according to the selected search type.
    $("#productValueWrap label").text(
        price ? "From price" : "Value"
    );


    // Update the placeholder dynamically.
    // Different search types have different placeholders.
    const placeholders = {
      id: "Product ID",
      name: "Exact product name",
      category: "Category ID",
      price: "From price"
    };

    // Set the appropriate placeholder in the input field.
    $("#productSearchValue").attr(
        "placeholder",
        placeholders[this.value]
    );

  });


  // -------------------------------------------------
  // I. CHECK BACKEND API CONNECTION
  // -------------------------------------------------

  // Call checkApi() to check whether the backend API
  // is accessible.
  checkApi();


  // -------------------------------------------------
  // J. INITIAL PAGE LOAD
  // -------------------------------------------------

  // Read the page name from the browser URL hash.
  //
  // Example:
  // URL: http://localhost:8080/#products
  // Result: products
  //
  // If no hash exists, open the dashboard.

  const initialPage = location.hash.replace("#", "");

  // Display the initial page and load its data.
  navigate(initialPage || "dashboard");

});