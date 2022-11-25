var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
  return new bootstrap.Tooltip(tooltipTriggerEl)
})

//var let cont
var bodyLoad=document.querySelector("#loader-wrapper")
setTimeout(()=>{
    bodyLoad.style.display="none"
},1500)