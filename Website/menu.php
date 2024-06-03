<?php
// to get the customer ID
//include 'get_customer_id.php';
?>




<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menu</title>

    <!-- css   -->
    <link rel="stylesheet" href="css_styles/menu.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA==" crossorigin="anonymous" referrerpolicy="no-referrer" />

    <!-- Bootstrap   -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">

    <!-- SweetAlert Script   -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

</head>
<body>
<!-- 7attayta hon la2an ma 3am temche bel head -->
<link rel="stylesheet" href="css_styles/menu-nutritions.css">








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
					<span
                    >Menu - Menu - Menu - Menu - Menu - Menu
						- Menu</span>
                </div>
            </div>
        </div>
        <div class="menu__item">
            <a href="user_profile.php" class="menu__item-link">Profile</a>
            <img class="menu__item-img" src="navbar-images/icons8-customer-100.png" />
            <div class="marquee">
                <div class="marquee__inner">
                    <span>Profile - Profile - Profile - Profile - Profile - Profile -Profile</span>
                </div>
            </div>
        </div>
        <div class="menu__item">
            <a href="user_profile.php" class="menu__item-link">Login</a>
            <img class="menu__item-img" src="navbar-images/icons8-customer-100.png" />
            <div class="marquee">
                <div class="marquee__inner">
					<span>Login - Login - Login - Login - Login - Login -Login</span>
                </div>
            </div>
        </div>
    </nav>


</div>













<!-- ============================================================================ -->
<!-- Welcome -->
<!-- ============================================================================ -->




<div>
    <div class="welcome-background-color">
        <div class="welcome-colored-part">

        </div>
    </div>
</div>


<div class="welcome-page-elements-container">

    <div class="welcome-text-container">
        <h1 class="welcome-text-title">Meet Our Delicious <span class="last-word">Meals!</span></h1>
        <p class="welcome-text-paragraph">From farm to table, we source the finest ingredients.</p>
        <div class="welcome-buttons-container">
            <button class="welcome-menu-btn">Discover Menu!</button>
            <button class="welcome-order-btn">Order Online!</button>
        </div>
    </div>

</div>


<div class="all-images-container" >
    <div class="images-inner-container" id="images-inner-container">
        <div class="block b-1"></div>
        <div class="block b-2"></div>
        <div class="block b-3"></div>
        <div class="block b-4"></div>
        <div class="block b-5"></div>
        <div class="block b-6"></div>
        <div class="block b-7"></div>
    </div>
</div>







<!-- ============================================================================ -->
<!-- Menu and Cart -->
<!-- ============================================================================ -->


<div id="menu-title-container" class="menu-title-container">
    <img class="menu-title-img" src="images/menu-title.png" alt="">
    <h1>Our Menu</h1>
</div>

<!--container-fluid to make it full screen-->
<div class="container-fluid" >
    <div class="row" >

        <!-- Menu Items -->
        <div id="menu" class="col-md-7 col-sm-12">
            <div id="navigation" >
                <!-- Navigation category buttons will show here -->
            </div>
            <ul id="menu-list">
                <!-- Menu items will be dynamically added here -->
            </ul>
        </div>



        <!-- Cart Items -->
        <div id="all-all-cart-container" class="col-md-4 col-s-5 ">

            <div id="cart" >
                <h2 id="cart-title">Your Order</h2>

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
                    <div>
                        <button class="cart-checkout-btn" onclick="checkout()">Checkout</button>
                    </div>
                </div>


            </div>
        </div>





    </div>
</div>
















<!-- GSAP & ScrollTrigger -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.5/gsap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.5/ScrollTrigger.min.js"></script>

<!-- Bootstrap -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>

<script src="js_scripts/menu_nutrition.js"></script>
<script src="js_scripts/cart.js"></script>






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
