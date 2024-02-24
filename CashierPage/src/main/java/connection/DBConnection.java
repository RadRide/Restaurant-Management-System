package connection;

import cashier.CashierPageController;
import javafx.scene.control.Alert;
import order.*;
import properties.Properties;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DBConnection {

    private final String dbURl = "jdbc:mysql://localhost:3306/restaurantdb",
            username = "root", password = "root";
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private CashierPageController controller;
    private Alert alert;
    private String cashierName = "";

    public DBConnection(CashierPageController controller){
        this.controller = controller;
    }
    public DBConnection(){}

    private void openConnection(){
        try{
            connection = DriverManager.getConnection(dbURl, username, password);
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Creating Connection To Database");
        }
    }
    private void closeConnection(){
        try{
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Closing Connection With Database");
        }
    }

    /**
     * Retrieves both delivery and in house active orders from the database and displays them in the orderViewer page
     */
    public void getOrders(){
        try{
            openConnection();
            controller.setInHouseFlow(getInHouseOrders());
            controller.setDeliveryFlow(getDeliveryOrders());
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Orders From Database");
        }
    }

    /**
     * Retrieves both delivery and in house already checked out orders from the database and displays them in the orderHistory page
     */
    public void getOldOrders(){
        try{
            openConnection();
            controller.setOldInHouseFlow(getOldInHouseOrders());
            controller.setOldDeliveryFlow(getOldDeliveryOrders());
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Orders History From Database");
        }
    }

    /**
     * Retrieves the active in house orders from the database
     * @return An ArrayList of OrderCards that contains the active in house orders
     * @throws SQLException
     */
    private ArrayList<OrderCard> getInHouseOrders() throws SQLException{
        ArrayList<OrderCard> list = new ArrayList<>();
        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT o.order_id, o.order_date, o.order_status, o.order_table, o.order_total_usd, o.order_total_lbp, " +
                "o.order_paid_usd, o.order_paid_lbp, o.order_discount, o.order_date, o.order_table, t.section FROM restaurantdb.order AS o, restaurantdb.tables AS t " +
                "WHERE o.order_status != " + Properties.CHECKED_OUT + " AND o.order_table = t.table_number AND o.order_table != " + Properties.DELIVERY);
        while (resultSet.next()){
            list.add(new OrderCard(new Order(resultSet.getInt("order_id"), resultSet.getInt("order_status"),
                    resultSet.getInt("order_table"), resultSet.getDouble("order_total_usd"), resultSet.getDouble("order_total_lbp"),
                    resultSet.getDouble("order_paid_usd"), resultSet.getDouble("order_paid_lbp"), resultSet.getDouble("order_discount"),
                    resultSet.getTimestamp("order_date").toLocalDateTime().toString(), resultSet.getString("section"),
                    getOrderItems(resultSet.getInt("order_id")), getOrderTip(resultSet.getInt("order_id"))), controller, false));
        }
        statement.close();
        return list;
    }

    /**
     * Retrieves the already checked out in house orders from the database
     * @return An ArrayList of OrderCards that contains the already checked out in house orders
     * @throws SQLException
     */
    private ArrayList<OrderCard> getOldInHouseOrders() throws SQLException{
        ArrayList<OrderCard> list = new ArrayList<>();
        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT o.order_id, o.order_date, o.order_status, o.order_table, o.order_total_usd, o.order_total_lbp, " +
                "o.order_paid_usd, o.order_paid_lbp, o.order_discount, o.order_date, o.order_table, t.section FROM restaurantdb.order AS o, restaurantdb.tables AS t " +
                "WHERE o.order_status = " + Properties.CHECKED_OUT + " AND o.order_table = t.table_number AND o.order_table != " + Properties.DELIVERY + " " +
                "AND o.order_date > DATE_SUB(NOW(), INTERVAL 1 MONTH)");
        while (resultSet.next()){
            list.add(new OrderCard(new Order(resultSet.getInt("order_id"), resultSet.getInt("order_status"),
                    resultSet.getInt("order_table"), resultSet.getDouble("order_total_usd"), resultSet.getDouble("order_total_lbp"),
                    resultSet.getDouble("order_paid_usd"), resultSet.getDouble("order_paid_lbp"), resultSet.getDouble("order_discount"),
                    resultSet.getTimestamp("order_date").toLocalDateTime().toString(), resultSet.getString("section"),
                    getOrderItems(resultSet.getInt("order_id")), getOrderTip(resultSet.getInt("order_id"))), controller, true));
        }
        statement.close();
        return list;
    }

    /**
     * Retrieves the already checked out delivery orders from the database
     * @return An ArrayList of OrderCards that contains the already checked out delivery orders
     * @throws SQLException
     */
    private ArrayList<OrderCard> getDeliveryOrders() throws SQLException{
        ArrayList<OrderCard> list = new ArrayList<>();
        statement = connection.createStatement();;
        resultSet = statement.executeQuery("SELECT o.order_id, o.order_date, o.order_status, o.order_table, o.order_total_usd, o.order_total_lbp, " +
                "o.order_paid_usd, o.order_paid_lbp, o.order_discount, o.order_date, o.order_table, d.address, c.customer_name, c.customer_id " +
                "FROM restaurantdb.order AS o, restaurantdb.delivery_order AS d, restaurantdb.customer AS c WHERE o.order_status != " + Properties.CHECKED_OUT + " " +
                "AND o.order_table = " + Properties.DELIVERY + " AND o.order_id = d.order_id AND c.customer_id = d.customer_id");
        while (resultSet.next()){
            list.add(new OrderCard(new Order(resultSet.getInt("order_id"), resultSet.getInt("order_status"),
                    resultSet.getInt("order_table"), resultSet.getDouble("order_total_usd"), resultSet.getDouble("order_total_lbp"),
                    resultSet.getDouble("order_paid_usd"), resultSet.getDouble("order_paid_lbp"), resultSet.getDouble("order_discount"),
                    resultSet.getTimestamp("order_date").toLocalDateTime().toString(), resultSet.getString("address"),
                    new Customer(resultSet.getInt("customer_id"), resultSet.getString("customer_name")),
                    getOrderItems(resultSet.getInt("order_id")), getOrderTip(resultSet.getInt("order_id"))), controller, false));
        }
        statement.close();
        return list;
    }

    /**
     * Retrieves the active delivery orders from the database
     * @return An ArrayList of OrderCards that contains the active delivery orders
     * @throws SQLException
     */
    private ArrayList<OrderCard> getOldDeliveryOrders() throws SQLException{
        ArrayList<OrderCard> list = new ArrayList<>();
        statement = connection.createStatement();;
        resultSet = statement.executeQuery("SELECT o.order_id, o.order_date, o.order_status, o.order_table, o.order_total_usd, o.order_total_lbp, " +
                "o.order_paid_usd, o.order_paid_lbp, o.order_discount, o.order_date, o.order_table, d.address, c.customer_name, c.customer_id " +
                "FROM restaurantdb.order AS o, restaurantdb.delivery_order AS d, restaurantdb.customer AS c WHERE o.order_status = " + Properties.CHECKED_OUT + " " +
                "AND o.order_table = " + Properties.DELIVERY + " AND o.order_id = d.order_id AND c.customer_id = d.customer_id " +
                "AND o.order_date > DATE_SUB(NOW(), INTERVAL 1 MONTH)");
        while (resultSet.next()){
            list.add(new OrderCard(new Order(resultSet.getInt("order_id"), resultSet.getInt("order_status"),
                    resultSet.getInt("order_table"), resultSet.getDouble("order_total_usd"), resultSet.getDouble("order_total_lbp"),
                    resultSet.getDouble("order_paid_usd"), resultSet.getDouble("order_paid_lbp"), resultSet.getDouble("order_discount"),
                    resultSet.getTimestamp("order_date").toLocalDateTime().toString(), resultSet.getString("address"),
                    new Customer(resultSet.getInt("customer_id"), resultSet.getString("customer_name")),
                    getOrderItems(resultSet.getInt("order_id")), getOrderTip(resultSet.getInt("order_id"))), controller, true));
        }
        statement.close();
        return list;
    }

    /**
     * Retrieves the order content of an order from the database
     * @param orderId The id of the order from which we need the content
     * @return An ArrayList of OrderItems belonging to the order
     */
    private ArrayList<OrderItem> getOrderItems(int orderId) {
        ArrayList<OrderItem> list = new ArrayList<>();
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT order_content.ordered_dish AS id, order_content.quantity, dish.dish_name AS name, " +
                    "dish.dish_category AS category, dish.dish_price AS price FROM order_content, dish WHERE order_content.ordered_dish = dish.dish_id AND " +
                    "order_content.order_id = " + orderId);
            while (rs.next()){
                list.add(new OrderItem(rs.getInt("id"), rs.getInt("quantity"),
                        rs.getDouble("price"), rs.getString("name")));
            }
            rs.close();
            stmt.close();
            return list;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Order Items From Database");
        }
        return list;
    }

    /**
     * Retrieves the info of a specific order and updates it
     * @param order The order to be updated
     * @return The updated order
     */
    public Order getSingleOrder(Order order){
        try{
            openConnection();
            String query = "SELECT o.order_id, o.order_date, o.order_status, o.order_table, o.order_total_usd, o.order_total_lbp, " +
                    "o.order_paid_usd, o.order_paid_lbp, o.order_discount, o.order_date, o.order_table, t.section FROM restaurantdb.order AS o, restaurantdb.tables AS t " +
                    "WHERE o.order_table = t.table_number AND o.order_table != " + Properties.DELIVERY +
                    " AND o.order_id = " + order.getId();
            if(order.getTable() == Properties.DELIVERY){
                query = "SELECT o.order_id, o.order_date, o.order_status, o.order_table, o.order_total_usd, o.order_total_lbp, " +
                        "o.order_paid_usd, o.order_paid_lbp, o.order_discount, o.order_date, o.order_table, d.address, c.customer_name, c.customer_id " +
                        "FROM restaurantdb.order AS o, restaurantdb.delivery_order AS d, restaurantdb.customer AS c WHERE o.order_table = " + Properties.DELIVERY + " " +
                        "AND o.order_id = d.order_id AND c.customer_id = d.customer_id AND o.order_id = " +  order.getId();
            }
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                if(order.getTable() == Properties.DELIVERY){
                    return new Order(resultSet.getInt("order_id"), resultSet.getInt("order_status"),
                            resultSet.getInt("order_table"), resultSet.getDouble("order_total_usd"),
                            resultSet.getDouble("order_total_lbp"),
                            resultSet.getDouble("order_paid_usd"), resultSet.getDouble("order_paid_lbp"),
                            resultSet.getDouble("order_discount"),
                            resultSet.getTimestamp("order_date").toLocalDateTime().toString(), resultSet.getString("address"),
                            new Customer(resultSet.getInt("customer_id"), resultSet.getString("customer_name")),
                            getOrderItems(resultSet.getInt("order_id")), getOrderTip(resultSet.getInt("order_id")));
                }else{
                    return new Order(resultSet.getInt("order_id"), resultSet.getInt("order_status"),
                            resultSet.getInt("order_table"), resultSet.getDouble("order_total_usd"),
                            resultSet.getDouble("order_total_lbp"),
                            resultSet.getDouble("order_paid_usd"), resultSet.getDouble("order_paid_lbp"),
                            resultSet.getDouble("order_discount"),
                            resultSet.getTimestamp("order_date").toLocalDateTime().toString(), resultSet.getString("section"),
                            getOrderItems(resultSet.getInt("order_id")), getOrderTip(resultSet.getInt("order_id")));
                }
            }else{
                showAlert("Error Updating Order:  Order Not Found");
            }
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Updating Order");
        }
        return order;
    }

    /**
     * Retrieves the tip related to a specific order from the database
     * @param orderId The id of the order from which we need the tip
     * @return The tip of the order
     * @throws SQLException
     */
    private Tip getOrderTip(int orderId) throws SQLException{
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM tips WHERE tip_order = " + orderId);
        if(rs.next()){
            Tip tip = new Tip(rs.getDouble("tip_usd"), rs.getInt("tip_lbp"));
            stmt.close();
            return tip;
        }
        stmt.close();
        return new Tip();
    }

    /**
     * Checks out the order by updating the order_status to 2 in the database and adding to paid amount in USD and LBP.
     * Also, it creates a new entry in the tip table if one was available.
     * @param order The order to be checked out
     * @param tip The tip amount in USD and LBP left by the customer
     */
    public void checkoutOrder(Order order, Tip tip){
        try{
            openConnection();

            preparedStatement = connection.prepareStatement("UPDATE restaurantdb.order SET order_paid_usd = ?, " +
                    "order_paid_lbp = ?, order_status = ? WHERE order_id = ?");
            preparedStatement.setDouble(1, order.getPaidUSD());
            preparedStatement.setDouble(2, order.getPaidLBP());
            preparedStatement.setInt(3, Properties.CHECKED_OUT);
            preparedStatement.setInt(4, order.getId());
            preparedStatement.execute();

            if(!tip.isEmpty()){
                preparedStatement = connection.prepareStatement("INSERT INTO tips (tip_order, tip_usd, tip_lbp, tip_date) VALUES (?,?,?,?)");
                preparedStatement.setInt(1, order.getId());
                preparedStatement.setDouble(2, tip.getUsd());
                preparedStatement.setDouble(3, tip.getLbp());
                preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.execute();
            }

            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Checking Out Order");
        }
    }

    /**
     * Retrieves the USD to LBP conversion rate from the exchange_rate table in the database
     * @return The conversion rate as an Integer
     */
    public int getRate(){
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM exchange_rate");
            if(resultSet.next()){
                return resultSet.getInt("rate");
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving LBP Rate From Database");
        }
        return 0;
    }

    /**
     * Checks if the entered id is valid
     * @param id The entered id
     * @return The name of the cashier related to the entered id
     */
    public String login(String id){
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM cashier WHERE cashier_id = " + id);
            if(resultSet.next()){
                String name = resultSet.getString("cashier_name");
                closeConnection();
                return name;
            }
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Logging In");
        }
        return null;
    }

    /**
     * Displays an Alert containing a specific error message
     * @param error The error message to be displayed
     */
    public void showAlert(String error){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }
}