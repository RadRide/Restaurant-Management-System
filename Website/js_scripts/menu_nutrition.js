


// this file was in the cart.js file
// but l sign out ma3ach meshe for anonymous reason
// 2am 7attayto hon in a seperate file


// haw to get the nutrition facts for the dish
document.addEventListener("DOMContentLoaded", function() {
    // he event listener for form submission
    document.addEventListener("submit", function(event) {
        event.preventDefault();

        // Check 2eza the submitted form is the nutritionForm
        if (event.target.id === "nutritionForm") {
            submitForm(event.target);
        }
    });


    function submitForm(form) {
        var ingredients = form.querySelector("#ingredients").value;

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "nutrition_analysis.php", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    showModal(xhr.responseText);
                } else {
                    console.log('Error:', xhr.status);
                }
            }
        };

        xhr.send("ingredients=" + ingredients);
    }

    // Function to show modal
    function showModal(response) {
        var modal = document.getElementById("myModal");
        var modalContent = document.getElementById("modalContent");
        modalContent.innerHTML = response;
        modal.style.display = "block";

        var span = document.getElementsByClassName("close")[0];
        span.onclick = function() {
            modal.style.display = "none";
        }

        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }
    }
});