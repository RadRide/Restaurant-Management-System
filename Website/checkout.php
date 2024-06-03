
<?php
session_start();
//
//if(isset($_SESSION["customer_id"])) {
//    echo "Customer ID is set: " . $_SESSION["customer_id"];
//} else {
//    echo "Customer ID is not set";
//}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout</title>

    <link rel="stylesheet" href="css_styles/checkout.css">

    <!-- Bootstrap   -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">


</head>

<body>

<div class="checkout-left-section">
    <h1>Checkout</h1>
    <br>
    <h2>
        <a href="menu.php"><-- Back To Menu</a>
    </h2>
</div>




<div class="wrapper" >



    <form action="" id="wizard">
        <!-- SECTION 1 -->
        <h4></h4>
        <section>
            <!-- Cart Items -->
            <h3>Your Order</h3>
            <div id="cart">
                <div class="clear-btn-container">
                    <button id="cart-clear-btn" onclick="clearCart()">Clear Cart</button>
                </div>
                <ul class="cart-list" id="cart-list">
                    <!-- Cart items will be added here -->
                </ul>
                <div class="cart-last-row">
                    <div>
                        <div id="cart-total-quantity">
                            <!-- Display total Items in the cart -->
                        </div>
                        <div id="cart-total">
                            <!-- Display total Price in the cart -->
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- SECTION 2 -->
        <h4></h4>
        <section>
            <h3>Select Your Address</h3>
            <!-- address section -->
            <div id="all-address-container">
                <div id="fullCheckoutForm" action="">
                    <div id="addresses">
<!--                        <h4 class="checkout-address-title">Your saved addresses:</h4>-->

                        <div class="add-address-btn-container">
                            <button class="add-address-btn">
                                <a href="user_profile.php">Add Address</a>
                            </button>
                        </div>
                        <div id="address-select">
                            <!-- all the Address Options 7a ybayno dynamically hon  -->
                            <div id="address-radio-buttons">
                                <!-- Radio buttons will be dynamically added here -->
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>


        <!-- SECTION 3 -->
        <h4></h4>
        <section>
            <h3>Confirm Order</h3>
            <div id="confirm-order-page">

                <div id="cart-elements-confirm-page">
                    <!-- show the dish name,quantity, and price -->
                </div>

                <!-- show the selected address -->
                <div id="selected-address-label"></div>

                <div id="cart-total-confirm-page">
                    <!-- show the total cost, and the total items -->
                </div>

                <div class="confirm-order-btn-container">
                    <button id="confirm-order-btn" onclick="confirmOrder()">Confirm Order</button>
                </div>

            </div>
        </section>
    </form>
</div>




<div class="background-image-slider">
<!--    <img src="images/1.jpg" alt="">-->
</div>













<!-- jquery minimized cdnf -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>

<!-- JQUERY STEP -->
<script src="js_scripts/checkout_js/jquery.steps.js"></script>

<script src="js_scripts/checkout_js/main.js"></script>



<!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>-->
<script src="js_scripts/checkout_address.js"></script>
<script src="js_scripts/cart.js"></script>



<script>




//    show the cart-list items in the confirm page
    $(document).ready(function() {
        $("#cart-list .cart-list-dish-container").each(function() {
            var dishName = $(this).find('.cart-list-dish-name').text();
            var quantity = $(this).find('.cart-list-quantity').text();
            var dishPrice = $(this).find('.cart-list-dish-price').text();

            var cartItemDiv = $('<div class="cart-item"></div>');
            cartItemDiv.append('<span class="cart-list-dish-name">' + dishName + '</span>');
            cartItemDiv.append('<span class="cart-list-quantity"> x' + quantity + '</span>');
            cartItemDiv.append('<div class="cart-list-dish-price">' + dishPrice + '</div>');

            $("#cart-elements-confirm-page").append(cartItemDiv);
        });

    });

//    show the cart-total and cart-total-quantity in the confirm page
    $(document).ready(function() {
        $.ajax({
            url: 'get_delivery_fee.php',
            type: 'GET',
            dataType: 'json',
            success: function(deliveryFee) {

                var totalQuantity = $("#cart-total-quantity").text();
                var totalPrice = $("#cart-total").text();

                var totalPriceNumber = parseFloat(totalPrice.split("$")[1]);

                /
                var newTotalPrice = totalPriceNumber + parseFloat(deliveryFee);

                var cartTotalDiv = $('<div id="cart-total"></div>');
                cartTotalDiv.append('<div id="cart-total-quantity-confirm-page">' + totalQuantity + '</div>');
                cartTotalDiv.append('<div id="cart-total-price-confirm-page">Total: $' + newTotalPrice + '  (Added Delivery fee: $' + parseFloat(deliveryFee) + ')</div>');

                $("#cart-total-confirm-page").append(cartTotalDiv);
            },
            error: function(xhr, status, error) {
                console.error('Error fetching delivery fee:', error);
            }
        });
    });



//    show the selected address in the confirm page
$(document).ready(function() {
    $(document).on('change', '#address-select input[type="radio"]', function() {
        var selectedAddress = $(this).siblings('label').text();
        // Clear previous selections
        $('#selected-address-label').empty();
        // Append the newly selected address
        $('#selected-address-label').append("<div id='address-selected'> " + selectedAddress + "</div>");
    });
});











    // hy to confirm the order ( "confirm order" button)
    function confirmOrder() {
        const selectedAddressElement = document.querySelector('input[name="address"]:checked');
        if (!selectedAddressElement) {
            alert('Please select an address to deliver to.');
            return;
        }

        const selectedAddress = selectedAddressElement.value;
        // console.log(selectedAddress);
        const customerId = <?php echo isset($_SESSION["customer_id"]) ? $_SESSION["customer_id"] : 0; ?>;
        const cart = JSON.parse(localStorage.getItem('cart')) || [];

        if (customerId === 0) {
            alert('Please log in to confirm the order.');
            return;
        }

        if (cart.length === 0) {
            alert('Your cart is empty. Please add items before confirming the order.');
            return;
        }

        const orderData = {
            customerId: customerId,
            selectedAddress: selectedAddress,
            cart: cart
        };

        // AJAX
        const xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    alert('Order confirmed!');
                    // redirect 3al  index.php w clear all the cart items
                    localStorage.removeItem('cart');
                    window.location.href = 'index.php';
                } else {
                    console.error('Error confirming order:', xhr.status);
                    alert('An error occurred while confirming the order. Please try again later.');
                }
            }
        };
        xhr.open('POST', 'confirm_order.php', true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(JSON.stringify(orderData));
    }




</script>

</body>
</html>
