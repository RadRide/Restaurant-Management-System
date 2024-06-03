

// bta3mil Initialize lal cart from local storage or create a new empty cart
let cart = JSON.parse(localStorage.getItem('cart')) || [];

function fetchMenuItems() {

    fetch('get_menu_items.php')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Menu items:', data);
            displayMenu(data);
        })
        .catch(error => console.error('Error fetching menu items:', error));
}

// hy to display menu items
function displayMenu(menuItems) {

    const menuList = document.getElementById('menu-list');
    menuList.innerHTML = '';

    let counter = 1;

    for (const category in menuItems) {
        if (menuItems.hasOwnProperty(category)) {

            // category header (category name before showing the dishes)
            const categoryHeader = document.createElement('h1');
            categoryHeader.setAttribute('id', `div${counter}`);
            categoryHeader.style.textAlign = 'center';
            categoryHeader.style.paddingTop = '85px';
            categoryHeader.style.paddingBottom = '20px';
            categoryHeader.style.color = 'black';
            categoryHeader.style.fontWeight = 'bold';
            categoryHeader.style.fontFamily = "'Staatliches', cursive";
            categoryHeader.textContent = category;
            menuList.appendChild(categoryHeader);

            const categoryDishes = menuItems[category];

            // create the buttons and add them to the navigation top bar
            var button = document.createElement("button");
            button.textContent = category+counter;
            // gave it a class to style
            button.classList.add('category-navigation-button');
            button.setAttribute("onclick", "scrollToDiv('div"+counter+"')");
            const navigation = document.getElementById('navigation');
            navigation.appendChild(button);

            // create a categories div to add the dishes to it
            const categoriesContainer = document.createElement('div');
            menuList.appendChild(categoriesContainer);

            categoryDishes.forEach(item => {

                const menuItem = document.createElement('li');
                // add a class so i can style it in CSS
                menuItem.classList.add('dish-li-tag');
                menuItem.innerHTML = `
                    <div class="single-dish-container" >
                    
                        <div class="dish-row1">
                            <span class="dish-name">${item.name} </span>
                            <span class="dish-price">$${item.price} </span>
                        </div>
                        
                        <div class="dish-row2" >
                            <span class="dish-ingredient-name">Ingredients:</span>
                            <span class="content">
                                            ${Object.keys(item.ingredients).map(ingredient => `<li>${ingredient}, </li>`).join('')}
                            </span>
                            <button class="read-more-btn">Read More</button>
                        </div>
                        
                        <div class="dish-row3">
                        
                            
                            <!-- show nutritions buttons -->                            
                            <!-- also fi label w input field, l ingredients with their quantity are shown in the input field -->                            
                            <!-- so when the nutrtion button is clicked the form is submitted -->                            
                            <form style="display: flex;" id="nutritionForm" method="post">
                                <label style="display: none;" for="ingredients">Ingredients (quantity and ingredient name, separated by commas):</label><br>
                                <input style="display: none;" type="text" id="ingredients" name="ingredients" value="${Object.keys(item.ingredients).map(ingredient => `${item.ingredients[ingredient]} ${ingredient}, `).join('')}"><br>
                                <button class="dish-favorite-btn-container" id="submitBtn">
                                    <img  class="dish-favorite-btn" src="/icons/nutrition-btn.png" alt="">
                                </button>
                            </form>
                                                       
                            <!-- modal that will appear showing the nutritions -->
                            <div id="myModal" class="modal">
                                <div class="modal-content">
                                    <span class="close">&times;</span>
                                    <div id="modalContent"></div>
                                </div>
                            </div>
                        
                            
                            <button class="dish-favorite-btn-container" onclick="addToFavorites(${item.id})">
                                <img class="dish-favorite-btn" src="icons/heart.png" alt="">
                            </button>
                            <button class="dish-add-to-cart-btn-container"  onclick="addToCart(${item.id}, '${item.name}', ${item.price})">
                                <img class="dish-add-to-cart-btn" src="icons/cart.png" alt="">
                            </button>
                        </div>
                        
                    </div>
                    
                    
                    
                    
                    
                    
                    
                    
                    
                `;



                // // create button to display nutrition facts
                // const nutritionBtn = document.createElement('button');
                // nutritionBtn.textContent = 'Nutrition Facts';
                // nutritionBtn.classList.add('nutrition-btn');
                // menuItem.appendChild(nutritionBtn);
                //
                // nutritionBtn.addEventListener('click', function() {
                //     // gather ingredients for nutrition analysis
                //     const ingredientsList = Object.keys(item.ingredients).map(ingredient => `${item.ingredients[ingredient]} ${ingredient}`).join(',');
                //     console.log(ingredientsList);
                //     // make API call to get nutrition facts
                //
                // });






                categoriesContainer.appendChild(menuItem);

                // Toggle functionality for read more/less (la each ingredient)
                const content = menuItem.querySelector('.content');
                const readMoreBtn = menuItem.querySelector('.read-more-btn');
                const maxWords = 3;
                let originalText = content.textContent.trim().split(' ');
                let truncatedText = originalText.slice(0, maxWords).join(' ');
                let isExpanded = false;

                // bas show  first 4 words
                content.textContent = truncatedText + '...';

                readMoreBtn.addEventListener('click', function() {
                    if (isExpanded) {
                        content.textContent = truncatedText + '...';
                        readMoreBtn.innerText = 'Read More';
                    } else {
                        content.textContent = originalText.join(' ');
                        readMoreBtn.innerText = 'Read Less';
                    }
                    isExpanded = !isExpanded;
                });

            });
        }
        counter++;
    }
    // calling the animation function that makes the navigation bar pin
    pinCategoryNavigationBar();
    // check if it is on mobile or not, if it is do not pin the cart on scroll (because the cart is fixed to bottom)
    if (window.innerWidth > 768) {
        pinCart();
    }

}


















// hy for when i reach a category section in the menu
// its corresponding category navigation button will change opacity
// also if the navigation button is not in the view, it will automatically scroll to show it
window.addEventListener('scroll', function() {
    const menuSections = document.querySelectorAll('#menu-list > h1');
    const navigationButtons = document.querySelectorAll('.category-navigation-button');
    const navigationContainer = document.getElementById('navigation');

    // bta3mol loop through each menu section
    menuSections.forEach((section, index) => {
        // check 2eza l section is in view
        const rect = section.getBoundingClientRect();
        if (rect.top >= 0 && rect.bottom <= window.innerHeight) {
            // 2eza l section is in view, reduce opacity of corresponding navigation button
            navigationButtons[index].style.opacity = 0.5;

            // check 2eza l navigation button is in the view
            const navRect = navigationContainer.getBoundingClientRect();
            const buttonRect = navigationButtons[index].getBoundingClientRect();
            if (buttonRect.left < navRect.left || buttonRect.right > navRect.right) {
                // bta3mel scroll lal navigation container to bring the button into view
                navigationContainer.scrollTo({
                    left: buttonRect.left + navigationContainer.scrollLeft - navRect.left,
                    behavior: 'smooth'
                });
            }
        } else {
            // 2eza section out of view, 5alle button opacity normal
            navigationButtons[index].style.opacity = 1;
        }
    });
});





// hy l function to: when i click on the category in the navigation bar,
// it takes me to it in the menu
function scrollToDiv(divId) {
    const targetDiv = document.getElementById(divId);
    if (targetDiv) {
        targetDiv.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
}




function addToFavorites(dishId) {

    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'add_to_favorites.php');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        if(xhr.status === 200) {
            // alert(xhr.responseText);
            if(xhr.responseText === "Item is already in the favorites"){
                Swal.fire({
                    position: "center",
                    title: xhr.responseText,
                    text: "You can find all your favorites in your settings",
                    showConfirmButton: false,
                    timer: 2500,
                    footer: '<a href="user_profile.php">See Your Favorites!</a>'
                });
            }else if(xhr.responseText === "Item added to favorites successfully!") {
                Swal.fire({
                    position: "center",
                    icon: "success",
                    title: xhr.responseText,
                    text: "You can find all your favorites in your settings",
                    showConfirmButton: false,
                    timer: 2500,
                    footer: '<a href="user_profile.php">See Your Favorites!</a>'
                });
            }else if(xhr.responseText === "You are not logged in") {
                Swal.fire({
                    position: "center",
                    icon: "error",
                    title: xhr.responseText,
                    text: "Login to Benefit from our Features!",
                    showConfirmButton: false,
                    timer: 2500,
                    footer: '<a href="login_page.php">Create an Account!</a>'
                });
            }
        } else {
            alert('Error adding item to favorites.');

        }
    };
    xhr.send('dish_id=' + dishId);
}




// thif function to add items to the cart
function addToCart(id, name, price) {
    const existingItem = cart.find(item => item.id === id);
    if (existingItem) {
        existingItem.quantity++;
    } else {
        cart.push({ id, name, price, quantity: 1, comment: "" });
    }

    console.log("Adding item to cart:", id, name, price);

    updateCartDisplay();
    saveCartToLocalStorage();
}


// hy to update cart display
function updateCartDisplay() {
    const cartList = document.getElementById('cart-list');
    const totalDisplay = document.getElementById('cart-total');
    const totalQuantityDisplay = document.getElementById('cart-total-quantity');
    cartList.innerHTML = '';

    let totalPrice = 0;
    let totalQuantity = 0;

    cart.forEach(item => {
        const cartItem = document.createElement('li');
        const itemPrice = item.price * item.quantity;
        totalPrice += itemPrice;
        totalQuantity += item.quantity;

        const commentNote = item.comment ? `<div>Note: ${item.comment}</div>` : '';
        // $${item.price} (2eza badde zid dish price (without calculation)
        cartItem.innerHTML = `
            <div class="cart-list-dish-container">
                <div class="cart-list-row1">
                    <div class="cart-list-dish-name">${item.name} </div>
                    <div class="cart-list-quantity-container">
                        <button class="cart-list-decrease-btn" onclick="decreaseQuantity(${item.id})">&minus;</button>
                        <span class="cart-list-quantity">${item.quantity}</span>
                        <button class="cart-list-increase-btn" onclick="increaseQuantity(${item.id})">&plus;</button>
                    </div>
                </div>
                
               <div class="cart-list-row2" style="margin-left: 1px">
                    <span style="font-weight: bold">${commentNote}</span>
                    <span id="comment-${item.id}" style="display: none;">
                        <input class="cart-list-comment-input-field" type="text" id="comment-input-${item.id}" placeholder="Add a comment" onkeypress="handleKeyPress(event, ${item.id})">
                        <button class="cart-list-comment-input-add-btn" onclick="addComment(${item.id})">Add</button>
                    </span>                
                </div>
    
                
                
                <div class="cart-list-row3">
                    <div>
                        <button class="cart-list-delete-btn" onclick="removeFromCart(${item.id})">
                            <img src="icons/trash-can.png" alt="">
                        </button>
                        <button class="cart-list-edit-btn" onclick="toggleComment(${item.id})">
                            <img src="icons/edit.png" alt="">
                        </button>
                    </div>
                    <div>
                        <span class="cart-list-dish-price">  $${itemPrice.toFixed(2)}</span>
                    </div>
                </div>
                
            </div>
        `;
        cartList.appendChild(cartItem);
    });

    totalDisplay.textContent = `Total: $${totalPrice.toFixed(2)}`;
    totalQuantityDisplay.textContent = `Total Items: ${totalQuantity}`;

    checkCartIfEmpty();

}

//  to toggle comment input
function toggleComment(id) {
    const commentDiv = document.getElementById(`comment-${id}`);
    commentDiv.style.display = commentDiv.style.display === 'none' ? 'block' : 'none';
}

// // this fct to add comment to the cart item
// function addComment(id) {
//     const commentInput = document.getElementById(`comment-input-${id}`);
//     const comment = commentInput.value;
//     const itemIndex = cart.findIndex(item => item.id === id);
//     if (itemIndex !== -1) {
//         cart[itemIndex].comment = comment;
//         updateCartDisplay();
//         saveCartToLocalStorage();
//     }
// }


// when the user press enter when adding a comment, it adds it
function handleKeyPress(event, id) {
    if (event.key === 'Enter') {
        addComment(id);
    }
}
// this fct to add comment to the cart item
function addComment(id) {
    const commentInput = document.getElementById(`comment-input-${id}`);
    const comment = commentInput.value;
    const itemIndex = cart.findIndex(item => item.id === id);
    if (itemIndex !== -1) {
        cart[itemIndex].comment = comment;
        updateCartDisplay();
        saveCartToLocalStorage();
    }
}



// function to increase the quantity of a specific dish bel cart
function increaseQuantity(id) {
    const itemIndex = cart.findIndex(item => item.id === id);
    if (itemIndex !== -1) {
        cart[itemIndex].quantity++;
        updateCartDisplay();
        saveCartToLocalStorage();
    }
}

// function to decrease the quantity of a specific dish bel cart
function decreaseQuantity(id) {
    const itemIndex = cart.findIndex(item => item.id === id);
    if (itemIndex !== -1) {
        if (cart[itemIndex].quantity > 1) {
            cart[itemIndex].quantity--;
        } else {
            cart.splice(itemIndex, 1);
        }
        updateCartDisplay();
        saveCartToLocalStorage();
    }
}

// hyde l function to remove a dish completely from the cart
function removeFromCart(id) {
    const itemIndex = cart.findIndex(item => item.id === id);
    if (itemIndex !== -1) {
        cart.splice(itemIndex, 1);
        updateCartDisplay();
        saveCartToLocalStorage();
    }
}

// function to clear all items from the cart
function clearCart() {
    // bta3mel reset lal cart array
    cart = [];
    updateCartDisplay();
    saveCartToLocalStorage();
    checkCartIfEmpty();
}



//  this fct check if the cart is empty,
//  if it is, it displays a "cart empty" message in the cart-list
function checkCartIfEmpty(){
    let cartList = document.getElementById("cart-list");
    if(cartList.innerHTML == ""){
        cartList.innerHTML = `
        <div style="padding-top: 30px; width: 100%;display: flex; flex-direction: column;align-items: center; justify-content: center">
            <img src="icons/online-shopping.png" alt="">
            <div style="padding-top: 20px">Your Cart is Empty. Add Dishes.</div>
        </div>
        `;
    }
}


// this function is to save cart to local storage
function saveCartToLocalStorage() {
    localStorage.setItem('cart', JSON.stringify(cart));
}






function checkout() {

    fetch('check_login.php')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            if (data.customer_id !== null) {

                window.location.href = 'checkout.php';
            } else {
                // redirect 3al login page
                // window.location.href = 'login_page.php';
                Swal.fire({
                    icon: "error",
                    title: "You are not Logged In",
                    text: "you cannot checkout unless you're logged in.",
                    footer: '<a href="login_page.php">Create Account</a>'
                });
            }
        })
        .catch(error => console.error('Error checking login status:', error));
}


console.log(cart);

// display menu items
fetchMenuItems();
updateCartDisplay();
















//=========================================================================================
// Animation (using GASP & ScrollTrigger)
//=========================================================================================


// welcome page section //////////////////////////////////////////////////////////

// images
TweenMax.staggerFrom(
    ".images-inner-container > .block",
    2,
    {
        y: "110%",
        ease: Expo.easeInOut,
        delay: 1,
    },
    0.4
);
TweenMax.to(".overlay", 0.5, {
    y: "100%",
    ease: Expo.easeInOut,
    delay: 5.2,
});

TweenMax.to(".images-inner-container", 2, {
    scale: "1.5",
    x: "20vw",
    rotate: "-4",
    boxShadow: "0px 0px 22px 0px rgba(0,0,0,0.75)",
    ease: Expo.easeInOut,
    delay: 4.3,
});


// welcome-colored-part
TweenMax.from(".welcome-colored-part", {
    x:"35vw",
    opacity:0,
    duration: 0.5,
    stagger: 0.7,
    delay: 5.6,
});

TweenMax.to(".welcome-colored-part", 2, {
    x: "0vw",
    ease: Expo.easeOut,
});


// welcome-text-title
TweenMax.from(".welcome-text-title", {
    x:"-100",
    opacity:0,
    duration: 0.5,
    stagger: 0.7,
    delay: 6.2
});

TweenMax.to(".welcome-text-title", {
    opacity: 1,
    duration: 0.5,
    stagger: 0.7,
    delay: 6.2
});

// welcome-text-paragraph
TweenMax.from(".welcome-text-paragraph", {
    y:"150",
    opacity:0,
    delay: 6.7
});

TweenMax.to(".welcome-text-paragraph", {
    opacity: 1,
    duration: 0.5,
    stagger: 0.7,
    delay: 6.7
});

// welcome-buttons-container
TweenMax.from(".welcome-buttons-container", {
    y:"150",
    opacity:0,
    delay: 7
});

TweenMax.to(".welcome-buttons-container", {
    opacity: 1,
    duration: 0.5,
    stagger: 0.7,
    delay: 7
});







// Menu Section ///////////////////////////////////////////////////////////////////
// Using GSAP to create a ScrollTrigger
gsap.registerPlugin(ScrollTrigger);

gsap.from("#menu-title-container", {
    scrollTrigger: {
        trigger: "#menu-title-container",
        start: "top bottom",
        end: "center center",
        toggleActions: "play none none reverse"
    },
    opacity: 0,
    y: 100,
    duration: 1.3,
    delay:0.7,
});

function pinCategoryNavigationBar() {
    ScrollTrigger.create({
        trigger: "#navigation",
        start: "top top",
        endTrigger: "#menu",
        end: "bottom top",
        pin: "#navigation",
        pinSpacing: false,
    });
}


function pinCart() {
    ScrollTrigger.create({
        trigger: "#navigation",
        start: "top top",
        endTrigger: "#menu",
        end: "bottom center",
        pin: "#cart",
        pinSpacing: false,
        // markers:true,
    });
}















