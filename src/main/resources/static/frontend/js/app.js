const pageTitles = {dashboard:"Dashboard",customers:"Customers",products:"Products",categories:"Categories",orders:"Orders",payments:"Payments"};
function navigate(page) { if (!pageTitles[page]) page="dashboard"; $(".page-section").removeClass("active"); $("#page-"+page).addClass("active"); $(".side-nav .nav-link").removeClass("active"); $(`.side-nav [data-page="${page}"]`).addClass("active"); $("#pageTitle").text(pageTitles[page]); location.hash=page; $("#sidebar").removeClass("show"); if(page==="dashboard") loadDashboard(); if(page==="customers") loadCustomers(); }
$(function(){
  $("#todayLabel").text(new Intl.DateTimeFormat("en-IN",{dateStyle:"medium"}).format(new Date()));
  const today = new Date().toISOString().slice(0,10); $("#orderDate,#dashTo,#orderTo").val(today); const monthStart = new Date(); monthStart.setDate(1); $("#dashFrom,#orderFrom").val(monthStart.toISOString().slice(0,10));
  $(".side-nav [data-page]").on("click",function(e){e.preventDefault();navigate($(this).data("page"));}); $("[data-go]").on("click",function(){navigate($(this).data("go"));}); $("#menuToggle").on("click",()=>$("#sidebar").toggleClass("show"));
  $("#refreshPage").on("click",()=>navigate(location.hash.replace("#","")||"dashboard"));
  $("#customerSearchType").on("change",function(){$("#customerSearchValue").attr("placeholder",this.value==="id"?"Enter customer ID":"Enter email address").attr("type",this.value==="id"?"number":"email");});
  $("#productSearchType").on("change",function(){const price=this.value==="price";$("#productToWrap").toggleClass("d-none",!price);$("#productValueWrap label").text(price?"From price":"Value");$("#productSearchValue").attr("placeholder",({id:"Product ID",name:"Exact product name",category:"Category ID",price:"From price"})[this.value]);});
  checkApi(); navigate(location.hash.replace("#","")||"dashboard");
});
