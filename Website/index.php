<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Home</title>

    <link rel="stylesheet" href="css-home-styles/home-style.css"/>
    <link rel="stylesheet" href="css-home-styles/image-scroller-styles.css"/>

    <script src="https://code.jquery.com/jquery-3.3.1.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/2.1.3/TweenMax.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/animejs/2.0.2/anime.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.9.1/gsap.min.js"></script>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

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
					<span>Menu - Menu - Menu - Menu - Menu - Menu
						- Menu</span>
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
					<span>Login - Login - Login - Login - Login - Login -Login</span>
                </div>
            </div>
        </div>
    </nav>


</div>




<!--=======================================================================================================================-->
<!-- Home Page -->
<!--=======================================================================================================================-->

<section class="homePage-section">

    <div class="wrapper">
        <div class="left"></div>
        <div class="right"></div>


        <div class="content">
            <div class="img-wrapper">
                <div class="home-image"></div>
            </div>


            <div class="text">
                <h1>Feast&nbsp;&nbsp; Planner</h1>
                <p>Restaurant</p>
            </div>

            <div class="name">Feast Planner</div>

            <div class="bottomnav">
                <h1>A Taste You'll Never Forget!</h1>

            </div>
        </div>
    </div>

</section>






<!--=======================================================================================================================-->
<!-- About Us -->
<!--=======================================================================================================================-->


<div class="about-us-section-container">


    <div class="left-part-about-us">
        <div class="about-us-image-container">
            <img class="about-us-image" src="images/checkout1.jpg" alt="" >
        </div>
        <div class="about-us-location-container">

            <div class="location-container-header">
                <h1>Visit Us</h1>
            </div>

            <div class="location-container-details">
                <div class="location-details-row">
                    <img src="location-icons/location.png" alt="">
                    <p>837 W. Marshall Lane Marshalltown, IA 50158, Los Angeles</p>
                </div>
                <div class="location-details-row">
                    <img src="location-icons/phone.png" alt="">
                    <p>(+961) 76 126 125</p>
                </div>
                <div class="location-details-row">
                    <img src="location-icons/mail.png" alt="">
                    <p>professionalEmail@gmail.com</p>
                </div>
            </div>


        </div>
    </div>

    <div class="right-part-about-us">
        <div class="right-part-all-items">
            <div class="header-right-part">
                <h1>About Us</h1>
            </div>
            <div class="content-about-us">
                <p>Welcome to our culinary haven, where every dish tells a story and every bite is a journey of flavors. At our restaurant, we are passionate about crafting unforgettable dining experiences that transcend the ordinary. <br><br>Our dedication to culinary excellence is reflected in every aspect of our menu, from the carefully curated ingredients to the artful presentation of each dish. Our team of skilled chefs and attentive staff are here to ensure that every visit is a memorable one, whether you're joining us for a casual meal or a special celebration.</p>
            </div>
            <div class="buttons-about-us">
                <button class="button1-about-us">Join Our Team</button>
                <button class="button2-about-us">More About Us</button>
            </div>
        </div>
    </div>

</div>







<!--=======================================================================================================================-->
<!-- image scroller -->
<!--=======================================================================================================================-->

<div class="all-container">

    <div class="left-part">

        <div class="left-part-row1">
            <h1>Taste that keeps you going ;)</h1>
        </div>
        <div class="left-part-row2">
            <p>We love to manage your food hunger with fun !</p>
        </div>
        <div class="left-part-row3">
            <button class="left-part-row3-button1">
                <a href="menu.php">Order Now</a>
            </button>
            <button class="left-part-row3-button2">
                <a href="menu.php">Explore Menu</a>
            </button>
        </div>

    </div>


    <div class="right-part">

        <div class="gallery">
            <div class="col">
                <div class="image">
                    <img src="food-images/food16.jpg" alt="">
                </div>
                <div class="image">
                    <img src="food-images/food17.jpg" alt="">
                </div>
                <div class="image">
                    <img src="food-images/food18.jpg" alt="">
                </div>
            </div>
            <div class="col col2">
                <div class="image">
                    <img src="food-images/food12.jpg" alt="">
                </div>
                <div class="image">
                    <img src="food-images/food14.jpg" alt="">
                </div>
                <div class="image">
                    <img src="food-images/food15.jpg" alt="">
                </div>
            </div>
        </div>

    </div>

</div>









<footer>

    <div class="footer-container">

        <div class="column-1">
            <h1>Restaurant</h1>
            <br>
            <p>Our dedication to culinary excellence is reflected in every aspect of our menu,
                from the carefully curated ingredients to the artful presentation of each dish.
                Our team of skilled chefs and attentive staff are here to ensure that every visit is a memorable one,
                whether you're joining us for a casual meal or a special celebration.
            </p>
        </div>

        <div class="column-2">
            <h1>Pages</h1>
            <br><br>
            <h4><a href="">Home</a></h4><br>
            <h4><a href="">Menu</a></h4><br>
            <h4><a href="">Contact</a></h4><br>
            <h4><a href="">Profile</a></h4><br>
            <h4><a href="">Order</a></h4><br>
            <h4><a href="">Delivery</a></h4><br>
        </div>

        <div class="column-3">
            <h1>Follow Us </h1>
            <br><br>
            <div class="footer-image-container">
                <div class="footer-img1">
                    <img src="img/small1.jpg" alt="">
                </div>
                <div class="footer-img2">
                    <img src="img/small2.jpg" alt="">
                </div>
                <div class="footer-img3">
                    <img src="img/small3.jpg" alt="">
                </div>
                <div class="footer-img4">
                    <img src="img/small4.jpg" alt="">
                </div>
            </div>
        </div>

    </div>


</footer>










<!--==================================================================================================================-->
<!-- home page -->
<!--==================================================================================================================-->

<script>

    TweenMax.to('.left', 2, {
        delay: .8,
        width: '50%',
        ease: Power2.easeInOut
    })

    TweenMax.to('.right', 2, {
        delay: .6,
        width: '50%',
        ease: Power3.easeInOut
    })

    TweenMax.from('.text h1', 2, {
        delay: .6,
        x: 1000,
        ease: Circ.easeInOut
    })

    TweenMax.from('.text p', 2, {
        delay: .7,
        x: 1000,
        ease: Circ.easeInOut
    })

    TweenMax.to('.home-image', 2, {
        delay: 1.5,
        width: '1000px',
        ease: Power2.easeInOut
    })

    TweenMax.staggerFrom('.bottomnav h1', 2, {
        delay: 1,
        x: 1000,
        ease: Circ.easeInOut
    }, 0.08)

    TweenMax.from('.name', 2, {
        delay: 1.5,
        x: -600,
        ease: Circ.easeInOut
    })

</script>


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





<!--==================================================================================================================-->
<!-- image Scroller -->
<!--==================================================================================================================-->
<script src="https://unpkg.co/gsap@3/dist/gsap.min.js"></script>
<script src="https://unpkg.com/gsap@3/dist/ScrollTrigger.min.js"></script>

<script>

    console.clear();

    gsap.registerPlugin(ScrollTrigger);

    const additionalY = { val: 0 };
    let additionalYAnim;
    let offset = 0;
    const cols = gsap.utils.toArray(".col");

    cols.forEach((col, i) => {
        const images = col.childNodes;

        // DUPLICATE IMAGES FOR LOOP
        images.forEach((image) => {
            var clone = image.cloneNode(true);
            col.appendChild(clone);
        });

        // BALLESH L ANIMATION
        images.forEach((item) => {
            let columnHeight = item.parentElement.clientHeight;
            let direction = i % 2 !== 0 ? "+=" : "-=";

            gsap.to(item, {
                y: direction + Number(columnHeight / 2),
                duration: 20,
                repeat: -1,
                ease: "none",
                modifiers: {
                    y: gsap.utils.unitize((y) => {
                        if (direction == "+=") {
                            offset += additionalY.val;
                            y = (parseFloat(y) - offset) % (columnHeight * 0.5);
                        } else {
                            offset += additionalY.val;
                            y = (parseFloat(y) + offset) % -Number(columnHeight * 0.5);
                        }

                        return y;
                    })
                }
            });
        });
    });

    const imagesScrollerTrigger = ScrollTrigger.create({
        trigger: "section",
        start: "top 50%",
        end: "bottom 50%",
        onUpdate: function (self) {
            const velocity = self.getVelocity();
            if (velocity > 0) {
                if (additionalYAnim) additionalYAnim.kill();
                additionalY.val = -velocity / 2000;
                additionalYAnim = gsap.to(additionalY, { val: 0 });
            }
            if (velocity < 0) {
                if (additionalYAnim) additionalYAnim.kill();
                additionalY.val = -velocity / 3000;
                additionalYAnim = gsap.to(additionalY, { val: 0 });
            }
        }
    });



</script>








<!--==================================================================================================================-->
<!-- About Us section -->
<!--==================================================================================================================-->


<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.5/gsap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.5/ScrollTrigger.min.js"></script>

<script>

    gsap.registerPlugin(ScrollTrigger);


    gsap.from(".about-us-image-container", {
        opacity: 0,
        x: -100,
        duration: 1,
        scrollTrigger: {
            trigger: ".about-us-image-container",
            start: "top 70%",
            // end: "top 10%",
            scrub: false,
            toggleActions: "play none none none", //7ot last reset 2eza badde yeha t3id
            markers: false,
        }
    })

    gsap.from(".about-us-location-container", {
        opacity: 0,
        y: 100,
        duration: 1,
        scrollTrigger: {
            trigger: ".about-us-location-container",
            start: "top 80%",
            // end: "top 10%",
            scrub: false,
            toggleActions: "play none none none",
            markers: false,
        }
    })


    gsap.from(".header-right-part", {
        opacity: 0,
        x: 100,
        duration: 1,
        scrollTrigger: {
            trigger: ".header-right-part",
            start: "top 80%",
            // end: "top 10%",
            scrub: false,
            toggleActions: "play none none none",
            markers: false,
        }
    })

    gsap.from(".content-about-us", {
        opacity: 0,
        x: 100,
        duration: 1,
        scrollTrigger: {
            trigger: ".content-about-us",
            start: "top 80%",
            // end: "top 10%",
            scrub: false,
            toggleActions: "play none none none",
            markers: false,
        }
    })

    gsap.from(".buttons-about-us", {
        opacity: 0,
        y: 100,
        duration: 1,
        scrollTrigger: {
            trigger: ".buttons-about-us",
            start: "top 80%",
            // end: "top 10%",
            scrub: false,
            toggleActions: "play none none none",
            markers: false,
        }
    })




</script>







</body>
</html>
