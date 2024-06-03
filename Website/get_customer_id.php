<?php

// hyde l page to return the id of the customer after he login
// so i can use in after

session_start();


if(isset($_SESSION["customer_id"])) {
    // by3mol return lal customer ID
    $customer_id = $_SESSION["customer_id"];
    echo "Customer id:" . $customer_id;
} else {
    echo "You are not logged in";
}
?>
