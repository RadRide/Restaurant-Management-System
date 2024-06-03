<?php
session_start();
include 'db_connection.php';
global $conn;

if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    $data = json_decode(file_get_contents("php://input"));


    $customerId = $data->customerId;
    $selectedAddress = $data->selectedAddress;
    $cart = $data->cart;


    $totalAmount = 0;
    $sql = "INSERT INTO `order` (order_total_usd, order_status, order_table) VALUES (?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("dsi", $totalAmount, $status, $order_table);
    $status = "0";
    $order_table = "999";
    $stmt->execute();
    $orderId = $stmt->insert_id;
    $stmt->close();


    $currentTimestamp = time();




// get the latest delivery_fee (not its id) and add it to the total amount

    $sql = "SELECT fee FROM delivery_fee ORDER BY id DESC LIMIT 1";
    $result = $conn->query($sql);
    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $deliveryFee = $row['fee'];
    } else {
        // if no delivery fee in the table 7otta 4
        $deliveryFee = 4;
    }
    // zid l deliveryFee 3al total amount
    $totalAmount += $deliveryFee;






// format the timestamp
    $formattedDate = date('Y-m-d H:i:s', $currentTimestamp);

    echo $formattedDate; // l output: 2023-10-28 10:00:00


    $sql = "INSERT INTO order_content (order_id, ordered_dish, quantity, comment, date) VALUES (?, ?, ?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("iisss", $orderId, $orderedDish, $quantity, $comment, $formattedDate);
    foreach ($cart as $item) {
        $orderedDish = $item->id;
        $quantity = $item->quantity;
        $comment = $item->comment;
        $stmt->execute();
        $totalAmount += $item->price * $quantity;
    }
    echo $comment;
    $stmt->close();


    $sql = "UPDATE `order` SET order_total_usd = ? WHERE order_id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("di", $totalAmount, $orderId);
    $stmt->execute();
    $stmt->close();















// Get the id of the selected address
    $sql = "SELECT address_id FROM customer_address WHERE customer_id = " . $conn->real_escape_string($customerId) . " AND saved_address = '" . $conn->real_escape_string($selectedAddress) . "'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $selectedAddressId = $row['address_id'];
    } else {
        echo "Error: Selected address not found.";
        exit();
    }








// Get the id of the latest delivery fee
    $sql_fee = "SELECT id FROM delivery_fee ORDER BY id DESC LIMIT 1";
    $result = $conn->query($sql_fee);

    if ($result && $result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $deliveryID = $row['id'];
    } else {
        echo "Error: Unable to retrieve delivery fee.";
        exit();
    }




// Insert data to the delivery_order table
    $sql = "INSERT INTO delivery_order (order_id, customer_id, address_id, delivery_fee) VALUES (?, ?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("iisi", $orderId, $customerId, $selectedAddressId, $deliveryID);
    $stmt->execute();
    $stmt->close();


    
// Insert data to the delivery_order table
//    $sql = "INSERT INTO delivery_order (order_id, customer_id, address) VALUES (?, ?, ?)";
//    $stmt = $conn->prepare($sql);
//    $stmt->bind_param("iis", $orderId, $customerId, $selectedAddress);
//    $stmt->execute();
//    $stmt->close();


    $conn->close();


    http_response_code(200);
} else {

    http_response_code(405);
}
?>
