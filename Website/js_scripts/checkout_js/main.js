
$(function(){
    $("#wizard").steps({
        headerTag: "h4",
        bodyTag: "section",
        transitionEffect: "fade",
        enableAllSteps: true,
        transitionEffectSpeed: 300,
        labels: {
            next: "Continue",
            previous: "Back"
        },
        // to remove the finish button bel 2e5er
        enableFinishButton: false,

        onStepChanging: function (event, currentIndex, newIndex) {

            if ( newIndex >= 1 ) {
                $('.steps ul li:first-child a img').attr('src','image/order-unfilled.png');
            } else {
                $('.steps ul li:first-child a img').attr('src','image/order-filled.png');
            }

            if ( newIndex === 1 ) {
                $('.steps ul li:nth-child(2) a img').attr('src','image/address-filled.png');
            } else {
                $('.steps ul li:nth-child(2) a img').attr('src','image/address-unfilled.png');
            }

            if ( newIndex === 2 ) {
                $('.steps ul li:nth-child(3) a img').attr('src','image/confirm-filled.png');
                $('.actions ul').addClass('step-3');
            } else {
                $('.steps ul li:nth-child(3) a img').attr('src','image/confirm-unfilled.png');
                $('.actions ul').removeClass('step-3');
            }
            return true;
            },
        onStepChanged: function(event, currentIndex, priorIndex) {
            if (currentIndex === 0) {
                $('.actions [href="#previous"]').hide();
            } else {
                $('.actions [href="#previous"]').show();
            }
        }
    });

    // Initially hide the back button
    $('.actions [href="#previous"]').hide();

    // Custom Button Jquery Steps
    $('.forward').click(function(){
        $("#wizard").steps('next');
    })


    // Create Steps Image
    $('.steps ul li:first-child').append('<img src="image/arrow.png" alt="" class="step-arrow">').find('a').append('<img src="image/order-filled.png" alt=""> ').append('<span class="step-order">Step 1</span>');
    $('.steps ul li:nth-child(2)').append('<img src="image/arrow.png" alt="" class="step-arrow">').find('a').append('<img src="image/address-unfilled.png" alt="">').append('<span class="step-order">Step 2</span>');
    $('.steps ul li:last-child a').append('<img src="image/confirm-unfilled.png" alt="">').append('<span class="step-order">Step 3</span>');

    // Count input
    $(".quantity span").on("click", function() {
        var $button = $(this);
        var oldValue = $button.parent().find("input").val();

        if ($button.hasClass('plus')) {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            // Don't allow decrementing below zero
            if (oldValue > 0) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 0;
            }
        }
        $button.parent().find("input").val(newVal);
    });
})
