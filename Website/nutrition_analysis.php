<?php
//if ($_SERVER["REQUEST_METHOD"] == "POST") {
//    // collect value of input field
//    $ingredients = $_POST['ingredients'];
//    if (empty($ingredients)) {
//        echo "Ingredients are empty";
//    } else {
//        $api_url = "https://api.edamam.com/api/nutrition-details?app_id=03a5f30c&app_key=61d32a27d35a5ddd460398fffa42f3e5";
//        $options = array(
//            'http' => array(
//                'header'  => "Content-type: application/json\r\n",
//                'method'  => 'POST',
//                'content' => json_encode(array('ingr' => explode(',', $ingredients)))
//            )
//        );
//        $context  = stream_context_create($options);
//        $result = file_get_contents($api_url, false, $context);
//        if ($result === FALSE) { /* Handle error */ }
//        $nutrition_data = json_decode($result, true);
//        echo "<h2>Nutrition Facts</h2>";
//        foreach ($nutrition_data['totalNutrients'] as $nutrient) {
//            echo $nutrient['label'] . ": " . $nutrient['quantity'] . " " . $nutrient['unit'] . "<br>";
//        }
//    }
//}
//?>






<?php

// he l php file for the nutrition facts in the menu

if ($_SERVER["REQUEST_METHOD"] == "POST") {

    $ingredients = $_POST['ingredients'];
    if (empty($ingredients)) {
        echo "Ingredients are empty";
    } else {
        $api_url = "https://api.edamam.com/api/nutrition-details?app_id=03a5f30c&app_key=61d32a27d35a5ddd460398fffa42f3e5";
        $options = array(
            'http' => array(
                'header'  => "Content-type: application/json\r\n",
                'method'  => 'POST',
                'content' => json_encode(array('ingr' => explode(',', $ingredients)))
            )
        );
        $context  = stream_context_create($options);
        $result = file_get_contents($api_url, false, $context);
        if ($result === FALSE) { /* hon eza lal error handling     b3dyn ba3mela */ }
        $nutrition_data = json_decode($result, true);
        echo "<div class='nutrition-facts'>";
        echo "<h1>Nutrition Facts</h1>";
        foreach ($nutrition_data['totalNutrients'] as $nutrient) {
            // Sanitize nutrient label to remove invalid characters
            $sanitized_nutrient_name = preg_replace('/[^a-zA-Z0-9\-_]/', '', $nutrient['label']);
            $nutrient_quantity = intval($nutrient['quantity']);  // Convert to integer
            $nutrient_unit = $nutrient['unit'];
            $daily_value = isset($nutrient['daily']) ? $nutrient['daily'] : '';
            echo "<div class='nutrient nutrient-$sanitized_nutrient_name'>";
            echo "<nav class='nutrient-label'>" . $nutrient['label'] . ": </nav>";
            echo "<span class='nutrient-quantity'>$nutrient_quantity $nutrient_unit</span>";
            echo "</div>";
        }
        echo "<p>Amount Per Serving</p>";

        echo "</div>";
    }
}
?>



