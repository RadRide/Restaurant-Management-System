package kitchen;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;

import java.sql.*;
import java.util.*;
import java.util.Date;

import java.text.SimpleDateFormat;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;


public class DatabaseConnection {

    public Connection databaseLink;
    String databaseUser = "root";
    String databasePassword = "root";
    String url = "jdbc:mysql://localhost:3306/restaurantdb";
    private Statement statement;
    private ResultSet resultSet;
    private Alert alert;


    public KitchenPageController kitchencontroller;



//    public DatabaseConnection(KitchenPageController kitchencontroller){
//        this.kitchencontroller = kitchencontroller;
//    }

    // Add a media player for playing sound
    private MediaPlayer mediaPlayer;

    // Constructor
    public DatabaseConnection(KitchenPageController kitchencontroller){
        this.kitchencontroller = kitchencontroller;

        // Initialize media player
        String soundFile = "src/main/resources/sound-file/beep2.mp3"; // Provide the path to your audio file
        Media sound = new Media(getClass().getResource("/sound-file/beep2.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(sound);
    }

    // Method to play the sound
    public void playSound() {
        mediaPlayer.stop(); // Stop any previous playback
        mediaPlayer.play(); // Play the sound
    }




    public DatabaseConnection(){    }
    public Connection openConnection(){
        try{
            //Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser, databasePassword);
//            System.out.println("connection succeeded");

        } catch (Exception e){
            e.printStackTrace();
        }
        return databaseLink;
    }


    public void closeConnection(){
        try{
            databaseLink.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ArrayList<OrderItem> getOrderItems(int orderId){
        ArrayList<OrderItem> list = new ArrayList<>();

        try{
            if(databaseLink.isClosed()){
                openConnection();
            }
            Statement stmt = databaseLink.createStatement();

            String query = "SELECT dish.dish_name as name, order_content.quantity as quantity, " +
                    "order_content.comment as comment, order_content.date , order_content.status, order_content.ordered_dish " +
                    "FROM dish, order_content WHERE order_content.ordered_dish = dish.dish_id " +
                    "AND restaurantdb.order_content.status=0 " +
                    "AND order_content.order_id = " + orderId;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){

                list.add(new OrderItem(rs.getString("name"), rs.getString("comment"),
                        rs.getInt("quantity"), rs.getTimestamp("date").toLocalDateTime(),rs.getInt("status")));

//                orderContentComplete(orderId, rs.getTimestamp("date"), rs.getInt("ordered_dish"));

            }
            stmt.close();
            rs.close();
            return list;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Order Items");
        }
        return list;
    }


    int oId;
    public ArrayList<Order> getOrders(String date){

        ArrayList<Order> list = new ArrayList<>();


        try{

            openConnection();
            statement = databaseLink.createStatement();
            //resultSet = statement.executeQuery("SELECT * FROM restaurantdb.order");

//            ResultSet rs = statement.executeQuery("SELECT * FROM restaurantdb.order WHERE restaurantdb.order.order_status=0 " );
            ResultSet rs = statement.executeQuery("SELECT * FROM restaurantdb.order WHERE restaurantdb.order.order_status=0 AND" +
                    " restaurantdb.order.order_date>'"+date+"';" );

            playSound();

            while (rs.next()){

                ArrayList<OrderItem> orItArr = getOrderItems(rs.getInt("order_id"));
                if(!orItArr.isEmpty()){

                    OrderPane o = new OrderPane(kitchencontroller, new Order(rs.getInt("order_id"), rs.getInt("order_table"),
                            orItArr));

                    kitchencontroller.getVboxMain().getChildren().add(o);

                }

            }
            statement.close();
            closeConnection();


            return list;
        }catch (SQLException e){

            e.printStackTrace();
            showAlert("Error Retrieving Orders");
        }
        return list;
    }


    private void showAlert(String error){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(error);
        alert.showAndWait();

    }


    public void orderComplete(int orderId){

        try{
            openConnection();
            statement = databaseLink.createStatement();

//            int rs = statement.executeUpdate("UPDATE restaurantdb.order\n" +
//                    "SET restaurantdb.order.order_status=1\n" +
//                    "WHERE restaurantdb.order.order_id="+orderId);

            int rs = statement.executeUpdate("UPDATE restaurantdb.order AS o\n" +
                    "JOIN restaurantdb.order_content AS oc ON o.order_id = oc.order_id\n" +
                    "SET o.order_status = 1,\n" +
                    "    oc.status = 1\n" +
                    "WHERE o.order_id ="+orderId);


            statement.close();
            closeConnection();
        }catch (SQLException e){

            e.printStackTrace();
            showAlert("Error Retrieving Orders");
        }
    }
//    public void orderContentComplete(int orderId, Timestamp date, int dish_id){
//
//        try{
//            openConnection();
//            statement = databaseLink.createStatement();
//            // 1
//            int rs = statement.executeUpdate("UPDATE restaurantdb.order_content " +
//                    "SET restaurantdb.order_content.status=1 " +
//                    "WHERE restaurantdb.order_content.order_id="+orderId +
//                    " AND order_content.date='"+date+"'"+
//                    " AND order_content.ordered_dish="+dish_id);
//
//            statement.close();
//            closeConnection();
//        }catch (SQLException e){
//
//            e.printStackTrace();
//            showAlert("Error Retrieving Orders");
//        }
//    }


// this method gets the minimum date of the order having order_status=0.
// i am getting all the orders having order_status=0 and having a date greater than the return of this method
    public String minDate(){
        String MinDate = null;
        try {
            openConnection();
            statement = databaseLink.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT Min(order_date) AS last_order_date FROM restaurantdb.order " +
                    "WHERE restaurantdb.order.order_status=0");

            if (resultSet.next()) {
                java.sql.Timestamp timestamp = resultSet.getTimestamp("last_order_date");
                if (timestamp != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    MinDate = sdf.format(timestamp);
                    System.out.println(MinDate);
                }
            }

            resultSet.close();
            statement.close();
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to retrieve the maximum date.");
        }
        return MinDate;
    }


//    do a method to get the minimum date from the order where the status is 0 w 7otta lal datedate
//    String datedate = "2024-04-1 11:58:15";

    String datedate = minDate();
    public String getMaxDate() {
        String formattedDateTime = null;
        try {
            openConnection();
            statement = databaseLink.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT MAX(order_date) AS last_order_date FROM restaurantdb.order");

            if (resultSet.next()) {
                java.sql.Timestamp timestamp = resultSet.getTimestamp("last_order_date");
                if (timestamp != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    formattedDateTime = sdf.format(timestamp);
                    System.out.println(formattedDateTime);
                }
            }

            resultSet.close();
            statement.close();
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to retrieve the maximum date.");
        }
        return formattedDateTime;
    }






    public ArrayList<Order> timelineTest(){

        ArrayList<Order> list = new ArrayList<>();




        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),e ->{

            if(getMaxDate().compareTo(datedate) > 0){

                getOrders(datedate);
                datedate = getMaxDate();


            }
            getMaxDate();


        }));

        //timeline.setDelay(new Duration(0));
        timeline.setCycleCount(timeline.INDEFINITE);
        timeline.play();

        return list;
    }

    public void increaseIngredients(int id){

        try{

            openConnection();
            statement = databaseLink.createStatement();

            int rs = statement.executeUpdate("UPDATE restaurantdb.inventory, restaurantdb.dish, restaurantdb.ingredient, restaurantdb.order_content, restaurantdb.order\n" +
                    "SET inventory.item_quantity = inventory.item_quantity-(ingredient.ingredient_quantity*order_content.quantity)\n" +
                    "WHERE dish.dish_id=ingredient.dish_id\n" +
                    "AND inventory.item_id=ingredient.ingredient_id\n" +
                    "AND restaurantdb.order.order_id = order_content.order_id\n" +
                    "AND restaurantdb.order.order_id="+id);
            statement.close();
            closeConnection();

        }catch (SQLException e){

            e.printStackTrace();
            showAlert("chou hal ingredient ya ayre");
        }
    }




    public String login(String id){
        String name = null;
        try{

            openConnection();
            statement = databaseLink.createStatement();
            ResultSet rs = statement.executeQuery("SELECT restaurantdb.kitchen.kitchen_name FROM restaurantdb.kitchen WHERE restaurantdb.kitchen.kitchen_id='"+id+"'" );

            if(rs.next()){
                name = rs.getString(1);
            }
            else{
                name = null;
            }
            statement.close();
            closeConnection();

            return name;
        }catch (SQLException e){

            e.printStackTrace();
            showAlert("Error Retrieving Orders");
        }
        return name;
    }









}

