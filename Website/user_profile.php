<?php
session_start();


if (!isset($_SESSION["customer_id"])) {
    // redirect user to login page if not logged in
    header("Location: login_page.php");
    exit();
}


include 'db_connection.php';
global $conn;


$customer_id = $_SESSION["customer_id"];

// to fetch favorite dishes of the logged-in user
$sql = "SELECT dish.dish_id, dish_name, dish_category, dish_price FROM dish
        INNER JOIN customer_favorites ON dish.dish_id = customer_favorites.dish_id
        WHERE customer_favorites.customer_id = $customer_id";

$result = $conn->query($sql);

// get customer name and phone number
$sql_user_info = "SELECT customer_name, customer_phone FROM customer WHERE customer_id = $customer_id";
$result_user_info = $conn->query($sql_user_info);
$row_user_info = $result_user_info->fetch_assoc();
$customer_name = $row_user_info["customer_name"];
$customer_phone = $row_user_info["customer_phone"];






// check 2eza the user sign out
if (isset($_POST["sign_out"])) {
    // bta3mol unset for all session variables
    $_SESSION = array();

    // clear cart data from local storage
    echo "<script>localStorage.removeItem('cart');</script>";

    // to refresh the page
    echo "<script>window.location.reload();</script>";

    session_destroy();
}


$conn->close();
?>



<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>test</title>

    <!-- Sweet Alert   -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <!-- Bootstrap   -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>

    <!-- css styles  -->
    <link rel="stylesheet" href="css_styles/user_profile.css" />
</head>
<body>




<!--=======================================================================================================================-->
<!-- Navbar -->
<!--=======================================================================================================================-->

<div class="navbar-container">
    <div class="navbar-icon">
        <!--		<img style="height: 50px; width: 50px" src="navbar-images/AlphaBeta_Icon.png" alt="">-->
    </div>
    <div class="menu-open">
        <img style="height: 70px; width: 80px" src="navbar-images/icons8-menu1-64.png" alt="">
    </div>
</div>

<div class="nav-container">
    <div class="menu-close">
        <img style="height: 50px; width: 50px" src="navbar-images/icons8-close-128.png" alt="">
    </div>
    <nav class="menu">
        <div class="menu__item">
            <a href="index.php" class="menu__item-link">Home</a>
            <img class="menu__item-img" src="navbar-images/icons8-home-128.png" />
            <div class="marquee">
                <div class="marquee__inner">
                    <span>Home - Home - Home - Home - Home - Home - Home</span>
                </div>
            </div>
        </div>
        <div class="menu__item">
            <a href="menu.php" class="menu__item-link">Menu</a>
            <img class="menu__item-img" src="navbar-images/icons8-restaurant-menu-100.png" />
            <div class="marquee">
                <div class="marquee__inner">
					<span>Menu - Menu - Menu - Menu - Menu - Menu - Menu</span>
                </div>
            </div>
        </div>
        <div class="menu__item">
            <a href="user_profile.php" class="menu__item-link">Profile</a>
            <img class="menu__item-img" src="navbar-images/icons8-contact-us-100.png" />
            <div class="marquee">
                <div class="marquee__inner">
                    <span>Profile - Profile - Profile - Profile - Profile - Profile - Profile</span>
                </div>
            </div>
        </div>
        <div class="menu__item">
            <a href="login_page.php" class="menu__item-link">Login</a>
            <img class="menu__item-img" src="navbar-images/icons8-customer-100.png" />
            <div class="marquee">
                <div class="marquee__inner">
					<span>Login - Login - Login - Login - Login - Login - Login</span>
                </div>
            </div>
        </div>
    </nav>


</div>









<div class="container">



    <div class="row gutters-sm">
        <div class="col-md-4  d-block">
            <div class="card">
                <div class="card-body">
                    <nav class="nav flex-column nav-pills nav-gap-y-1">
                        <a href="#profile" data-bs-toggle="tab" class="nav-item nav-link has-icon nav-link-faded active">
                            Favorites
                        </a>
                        <a href="#account" data-bs-toggle="tab" class="nav-item nav-link has-icon nav-link-faded">
                            Addresses
                        </a>
                        <a href="#security" data-bs-toggle="tab" class="nav-item nav-link has-icon nav-link-faded">
                            Order History
                        </a>
                        <a href="#notification" data-bs-toggle="tab" class="nav-item nav-link has-icon nav-link-faded">
                            Account Settings
                        </a>
                    </nav>
                </div>
            </div>
        </div>
        <div class="col-md-8">
            <div class="card">


                <div class="card-body tab-content">


                    <div class="tab-pane active" id="profile">

                        <!-- Display favorite dishes -->
                        <div class="user-profile-favorites-section">

                            <?php if ($result->num_rows > 0) : ?>
                                <h2>Your Favorite Dishes</h2>
                                <ul class="row pe-4">
                                    <?php while ($row = $result->fetch_assoc()) : ?>
                                        <li class="col-md-6 col-sm-12">

                                            <!-- echo $row["dish_id"];-->

                                            <div class="favorite-dish-container">

                                                <div class="favorites-first-row">
                                                    <?php echo $row["dish_name"]; ?>
                                                </div>

                                                <div class="favorites-second-row">
                                                    Category: <?php echo $row["dish_category"]; ?>
                                                </div>

                                                <div class="favorites-third-row">
                                                    Price: $<?php echo $row["dish_price"]; ?>
                                                </div>

                                                <div class="favorites-last-row">
                                                    <button onclick="removeFromFavorites(<?php echo $row["dish_id"]; ?>)">
                                                        <img src="icons/trash2.png" alt="">
                                                    </button>

                                                    <button onclick="addToCartWithAlert(<?php echo $row["dish_id"]; ?>, '<?php echo $row["dish_name"]; ?>', <?php echo $row["dish_price"]; ?>);">

                                                        <img src="icons/cart.png" alt="">
                                                    </button>
                                                </div>

                                            </div>

                                        </li>
                                    <?php endwhile; ?>
                                </ul>
                            <?php else : ?>
                                <p>No favorite dishes found.</p>
                            <?php endif; ?>
                        </div>
                    </div>




                    <div class="tab-pane" id="account">

                        <!-- Address Section -->
                        <div class="user-profile-address-section">
                            <h2>Addresss Section</h2>
                            <button class="show-map-button" onclick="showTheMap()">
                                <img class="show-map-image" src="images/addAddress2.png" alt="">
                            </button>
                            <h2>Your Saved Addresses</h2>
                            <nav id="savedAddresses" >
                                <!-- Addresses 7a ybayno hon   -->
                            </nav>
                        </div>


                    </div>




                    <div class="tab-pane" id="security">


                        <!-- Display Order History- -->
                        <div class="user-profile-order-history-section">
                            <h2>Order History</h2>
                            <?php

                            include 'db_connection.php';
                            global $conn;


                            $customer_id = $_SESSION["customer_id"];


                            $order_history_sql = "SELECT * FROM `order` WHERE order_id IN (SELECT order_id FROM delivery_order WHERE customer_id = $customer_id) ORDER BY order_date DESC";
                            $order_history_result = $conn->query($order_history_sql);

                            if ($order_history_result->num_rows > 0) :
                                while ($order_row = $order_history_result->fetch_assoc()) :
                                    $order_id = $order_row['order_id'];
                                    ?>
                                    <span class="order">
                                        <div class="single-order-container">

<!--                                        <h3>Order ID: --><?php //echo $order_id; ?><!--</h3>-->
                                        <h4>Ordered Items:</h4>
                                        <ul>
                                            <?php

                                            $order_items_sql = "SELECT oc.quantity, d.dish_name, d.dish_price FROM order_content oc JOIN dish d ON oc.ordered_dish = d.dish_id WHERE oc.order_id = $order_id";
                                            $order_items_result = $conn->query($order_items_sql);
                                            if ($order_items_result->num_rows > 0) :
                                                while ($order_item_row = $order_items_result->fetch_assoc()) :
                                                    ?>
                                                    <li><?php echo $order_item_row['quantity']; ?> x <?php echo $order_item_row['dish_name']; ?> ($<?php echo $order_item_row['dish_price']; ?> each)</li>
                                                <?php
                                                endwhile;
                                            endif;
                                            ?>
                                        </ul>
                                        <p>Date: <?php echo $order_row['order_date']; ?></p>
                                        <div class="order-total-amount">
                                            <p>Total Amount: $<?php echo $order_row['order_total_usd']; ?></p>
                                        </div>
                                        </div>
                                    </span>
                                <?php
                                endwhile;
                            else :
                                echo "<p>No order history found.</p>";
                            endif;


                            $conn->close();
                            ?>
                        </div>

                    </div>



                    <div class="tab-pane" id="notification">

                        <!-- Account Settings -->
                        <div class="user-profile-settings-section">
                            <h2>Edit Your Settings</h2>

                            <form id="profileForm">
                                <div class="settings-first-row">
                                    <label for="name">Name : </label>
                                    <input type="text" id="name" name="customer_name" value="<?php echo $customer_name; ?>" required>
                                </div>
                                <div class="settings-second-row">
                                    <label for="phone">Phone: </label>
                                    <input type="text" id="phone" name="customer_phone" value="<?php echo $customer_phone; ?>" required>
                                </div>
                                <div class="settings-save-btn-container">
                                    <button type="submit">Save Changes</button>
                                </div>
                            </form>
                            <div id="profileMessage"></div>

                            <div class="settings-signout-btn-container">
                                <form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>">
                                    <input type="hidden" name="sign_out">
                                    <input type="submit" value="Sign Out">
                                </form>
                            </div>

                        </div>


                    </div>







                </div>
            </div>
        </div>
    </div>

</div>





<!-- i added haw men l cart page (index.php halla2)  -->
<!-- because i am using the cart.js script that is common for both pages -->
<!-- and it showed me errors in the console because these divs did not exist -->
<!-- so i added them and put a display of none  -->
<div style="display: none;">
    <!-- Display Menu Items -->
    <div id="menu">
        <h2>Menu</h2>
        <ul id="menu-list">
            <!-- Menu items will be dynamically added here -->
        </ul>
    </div>
    <!-- Display Cart Items -->
    <div id="cart">
        <h2>Cart</h2>
        <ul id="cart-list">
            <!-- Cart items will be dynamically added here -->
        </ul>
        <button onclick="clearCart()">Clear All</button>
        <button onclick="checkout()">Checkout</button>
        <div id="cart-total"></div>
        <div id="cart-total-quantity"></div>
    </div>
</div>





<!-- this map has a display of none by default -->
<!-- when i click on the add address button (plus image) -->
<!-- it set the display to block (using js) and show the map fullscreen -->
<!-- and there's an exit button that set the display to none (close the map) -->
<div id="mapWrapper">
    <nav id="myMap" ></nav>


    <nav id="searchBoxContainer" >
        <button class="close-map-btn" onclick="closeTheMap()">
            <img src="images/close.png" alt="">
        </button>
        <input type="text" id="searchBox" placeholder="Enter a location" onkeydown="if (event.keyCode === 13) { searchLocation(); }">

        <!-- search button bs 7ataytello display none and i used enter to call it -->
        <button style="display: none" onclick="searchLocation()">Search</button>
    </nav>

    <div class="map-save-address-section">
        <!-- hy to show the longtitude and latitude -->
        <!-- it is converted from numbers to readable address and shown in the geocodeResult -->
        <nav style="display: none" id="locationInfo"></nav>

        <input id="address-name-input" type="text"  placeholder="Address Name">
        <div id="geocodeResult"></div>
        <button id="saveAddressBtn" onclick="closeTheMap()" >Save this address</button>
    </div>
</div>







<!--Script-->
<!--<script src="js_scripts/user_profile.js"></script>-->
<script src="js_scripts/cart.js"></script>


<!-- for the address section -->
<!-- ajax -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<script src="js_scripts/user_profile_address.js"></script>

<!-- Bing Maps Api with the api key (li jebto men l website)  -->
<script type='text/javascript' src='https://www.bing.com/api/maps/mapcontrol?key=AlQAz5kZ66WhGJMdE-5a59YlBkOJprBTuLITHbETeBPKI8uid1ehw-M4ECrtuUJP'></script>

<!-- Bing Maps Api -->
<script type='text/javascript' src='https://www.bing.com/api/maps/mapcontrol?callback=loadMapScenario' async defer></script>




<script>

//    to add the dish to the cart from the favorites page with an alert
    function addToCartWithAlert(dishId, dishName, dishPrice) {
        addToCart(dishId, dishName, dishPrice);
        Swal.fire({
            position: "center",
            icon: "success",
            title: "Dish added!",
            text: "You can find your ordered dishes in the menu page!",
            showConfirmButton: false,
            timer: 2500,
        });
    }

    function removeFromFavorites(dishId) {
        $.ajax({
            type: 'POST',
            url: 'remove_favorite.php',
            data: { dish_id: dishId },
            success: function (response) {

                if (response == "success") {

                    var dishElement = $("li[data-dish-id='" + dishId + "']");
                    dishElement.remove();
                } else {

                    console.log("Error removing dish from favorites.");
                }
            },
            error: function (xhr, status, error) {

                console.log("AJAX Error:", error);
            }
        });

        // the page reload (refresh)
        // i used this method because ajax did not work for the favorites part
        // for anonymous reasons :/
        location.reload();
    }

</script>






<!-- for Account Settings -->
<!-- i used Ajax to handle the changes  -->
<script>
    $(document).ready(function () {

        $('#profileForm').submit(function (e) {
            e.preventDefault();
            var formData = $(this).serialize();

            $.ajax({
                type: 'POST',
                url: 'update_profile.php',
                data: formData,
                success: function (response) {
                    $('#profileMessage').html(response);
                }
            });
        });
    });
</script>





<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/2.1.3/TweenMax.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/animejs/2.0.2/anime.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.9.1/gsap.min.js"></script>

<!--==================================================================================================================-->
<!-- navbar -->
<!--==================================================================================================================-->
<script>
    var t1 = new TimelineMax({ paused: true });

    t1.to(".nav-container", 1, {
        left: 0,
        ease: Expo.easeInOut,
    });

    t1.staggerFrom(
        ".menu > div",
        0.8,
        { y: 100, opacity: 0, ease: Expo.easeOut },
        "0.1",
        "-=0.4"
    );

    t1.reverse();
    $(document).on("click", ".menu-open", function () {
        t1.reversed(!t1.reversed());
    });
    $(document).on("click", ".menu-close", function () {
        t1.reversed(!t1.reversed());
    });
</script>




</body>
</html>