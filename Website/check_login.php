<?php

session_start();

$response = array();

// check 2za l customer is logged in
if(isset($_SESSION["customer_id"]) && isset($_SESSION["customer_email"])) {
    // include customer ID in the response
    $response['customer_id'] = $_SESSION["customer_id"];
} else {
    // 2za not logged in, set customer ID to null
    $response['customer_id'] = null;
}

// bet ragge3 l  JSON response
header('Content-Type: application/json');
echo json_encode($response);

?>
