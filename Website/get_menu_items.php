<?php

$servername = "localhost";
$username = "root";
$password = "root";
$dbname = "restaurantdb";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}


$sql = "SELECT DISTINCT category_name FROM category";
$result = $conn->query($sql);

$menuItems = array();
if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $category = $row['category_name'];
        $menuItems[$category] = array();


        $sql_dishes = "SELECT d.dish_id, d.dish_name, d.dish_price, 
                            GROUP_CONCAT(i.item_name) AS ingredients, 
                            GROUP_CONCAT(di.ingredient_quantity) AS quantities,
                            GROUP_CONCAT(i.item_unit) AS units
                        FROM dish d
                        LEFT JOIN ingredient di ON d.dish_id = di.dish_id
                        LEFT JOIN inventory i ON di.ingredient_id = i.item_id
                        WHERE d.dish_category = '$category'
                        AND d.dish_status = 1
                        GROUP BY d.dish_id";
        $result_dishes = $conn->query($sql_dishes);

        if ($result_dishes->num_rows > 0) {
            while ($row_dish = $result_dishes->fetch_assoc()) {
                $ingredients = explode(',', $row_dish['ingredients']);
                $quantities = explode(',', $row_dish['quantities']);
                $units = explode(',', $row_dish['units']);

                // Combine ingredients with quantities and units
                $ingredientDetails = array_combine($ingredients, array_map(function($quantity, $unit) {
                    return $quantity . ' ' . $unit;
                }, $quantities, $units));

                $menuItems[$category][] = array(
                    'id' => $row_dish['dish_id'],
                    'name' => $row_dish['dish_name'],
                    'price' => $row_dish['dish_price'],
                    'ingredients' => $ingredientDetails
                );
            }
        }
    }
}


$conn->close();


echo json_encode($menuItems);
?>
