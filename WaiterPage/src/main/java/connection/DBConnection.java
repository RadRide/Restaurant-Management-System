package connection;

import category.CategoryButton;
import javafx.scene.control.Alert;
import menu.Dish;
import menu.MenuButton;
import menu.MenuPane;
import order.Discount;
import order.Order;
import order.OrderItem;
import order.OrderItemBox;
import properties.Properties;
import tables.Table;
import waiter.WaiterPageController;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {

    private final String dbURl = "jdbc:mysql://localhost:3306/restaurantdb",
            username = "root", password = "root";
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Alert alert;
    private String waiterName = "";
    private double rate = 0;
    private DecimalFormat formatter;
    private SimpleDateFormat dateFormat;
    private WaiterPageController controller;

    public DBConnection(WaiterPageController controller){
        this.controller = controller;
        rate = getExchangeRate();
        dateFormat = new SimpleDateFormat(Properties.DATE_FORMAT);
    }
    public DBConnection(){}

    public void openConnection(){
        try{
            connection = DriverManager.getConnection(dbURl, username, password);
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Establishing Connection With Database");
        }
    }

    public void closeConnection(){
        try{
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Closing Connection With Database");
        }
    }

    private ArrayList<String> getCategories(){
        ArrayList<String> categories = new ArrayList<>();
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT DISTINCT dish_category AS category FROM dish WHERE dish_status = 1 ORDER BY category");
            while (resultSet.next()){
                categories.add(resultSet.getString("category"));
            }
            statement.close();
            closeConnection();
            return categories;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Categories From Database");
        }
        return categories;
    }

    public ArrayList<CategoryButton> getCategoryButtons(){
        ArrayList<CategoryButton> buttons = new ArrayList<>();
        ArrayList<String> categories = getCategories();
        for (int i = 0; i < categories.size(); i++){
            buttons.add(new CategoryButton(controller, categories.get(i)));
        }
        return buttons;
    }

    public ArrayList<MenuPane<Dish>> getMenu(){
        ArrayList<MenuPane<Dish>> panes = new ArrayList<>();
        ArrayList<String> categories = getCategories();
        for(int i = 0; i < categories.size(); i++){
            panes.add(new MenuPane(controller, categories.get(i), getMenuItems(categories.get(i))));
        }
        return panes;
    }

    public ArrayList<MenuButton<Dish>> getMenuItems(String category){
        ArrayList<MenuButton<Dish>> buttons = new ArrayList<>();
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM dish WHERE dish_category = '" + category +
                    "' AND dish_status = 1 ORDER BY dish_category");
            while (resultSet.next()){
                buttons.add(new MenuButton(controller, new Dish(resultSet.getString("dish_name"),
                        resultSet.getString("dish_category"), resultSet.getDouble("dish_price"),
                        resultSet.getInt("dish_id"))));
            }
            statement.close();
            closeConnection();
            return buttons;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Menu From Database");
        }
        return buttons;
    }

    private ArrayList<String> getSections(){
        ArrayList<String> sections = new ArrayList<>();
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT DISTINCT tables.section FROM tables;");
            while (resultSet.next()){
                sections.add(resultSet.getString("section"));
            }
            statement.close();
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Getting Sections");
        }
        return sections;
    }

    public ArrayList<CategoryButton> getSectionButtons(){
        ArrayList<CategoryButton> buttons = new ArrayList<>();
        ArrayList<String> sections = getSections();
        for (int i = 0; i < sections.size(); i++){
            buttons.add(new CategoryButton(controller, "Section: " + sections.get(i)));
        }
        return buttons;
    }

    public ArrayList<MenuPane<Table>> getTablePane(){
        ArrayList<MenuPane<Table>> panes = new ArrayList<>();
        ArrayList<String> sections = getSections();
        for (int i = 0; i < sections.size(); i++){
            panes.add(new MenuPane<>(controller, "Section: " + sections.get(i), getTableButtons(sections.get(i))));
        }
        return panes;
    }

    private ArrayList<MenuButton<Table>> getTableButtons(String section){
        ArrayList<MenuButton<Table>> buttons = new ArrayList<>();
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM tables WHERE tables.section = '" + section + "';");
            while (resultSet.next()){
                buttons.add(new MenuButton<>(controller, new Table(resultSet.getString("table_number"))));
            }

            statement.close();
            closeConnection();

            return buttons;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Tables");
        }
        return buttons;
    }

    /**
     * Adds an order to the database
     * @param order The order to be added
     * @param type 1 if the order is added normally. 2 if the order is created while transferring an ordered
     *             item to a table which does not have an open order
     * @return The id of the newly created order if the type is 2, else it returns -1
     */
    public int addOrder(Order order, int type){
        try{
            openConnection();
            int  id = 0;
            if(order.isNew()){
                id = getLastId();
                preparedStatement = connection.prepareStatement("INSERT INTO restaurantdb.order " +
                        "(order_id, order_date, order_discount, order_total_usd, order_total_lbp, order_paid_usd, order_paid_lbp, order_status, order_table) " +
                        "values(?,?,?,?,?,?,?,?,?)");
                preparedStatement.setInt(1, id);
//            preparedStatement.setDate(2, Date.valueOf(LocalDateTime.now().toLocalDate()));
                preparedStatement.setString(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern(Properties.DATE_FORMAT)));
                preparedStatement.setDouble(3, order.getDiscount().getDiscount());
                preparedStatement.setDouble(4, order.getOrderTotal()); // Order Total In USD
                preparedStatement.setDouble(5, order.getOrderTotal() * rate); // Order Total In LBP
                preparedStatement.setDouble(6, Properties.DEFAULT_PAID); // Order Total Paid in USD
                preparedStatement.setDouble(7, Properties.DEFAULT_PAID); // Order Total Paid in LBP
                preparedStatement.setInt(8, Properties.IN_KITCHEN);
                preparedStatement.setInt(9, order.getOrderSource().toInt());
                preparedStatement.execute();
            }else{
                id = order.getId();
                preparedStatement = connection.prepareStatement("UPDATE restaurantdb.order SET order_status = ?, " +
                        "order_total_usd = ?, order_total_lbp = ?, order_discount = ? WHERE order_id = ?");
                preparedStatement.setInt(1, Properties.IN_KITCHEN);
                preparedStatement.setDouble(2, order.getOrderTotal());
                preparedStatement.setDouble(3, order.getOrderTotal() * rate);
                preparedStatement.setDouble(4, order.getDiscount().getDiscount());
                preparedStatement.setInt(5, id);
                preparedStatement.execute();
            }
            preparedStatement.close();

            switch (type){
                case 1:
                    addOrderItem(id, order.getOrderItems());
                    closeConnection();

                    showMessage("Order Successfully Added");
                    break;
                case 2:
                    return id;
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Adding Order To Database");
        }
        return -1;
    }

    public void addOrderItem(int id, ArrayList<OrderItem> orderItems){
        try{
            if(connection.isClosed())
                openConnection();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO order_content Values(?,?,?,?,?,?);");
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Properties.DATE_FORMAT));
            for(OrderItem item : orderItems){
                if(item.isNew()){
                    stmt.setInt(1, id);
                    stmt.setInt(2, item.getItem().getId());
                    stmt.setInt(3, item.getItemQuantity());
                    stmt.setString(4, item.getComment());
                    stmt.setString(5, currentTime);
                    stmt.setInt(6, Properties.ITEM_NOT_DISPLAYED);
                    stmt.execute();
                }
            }
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Adding Order Items To Database");
        }
    }

    /**
     * Checks if the table already has an active order and retrieves the already available ordered items
     * @param table Selected Table
     */
    public void loadTable(Table table){
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM restaurantdb.order WHERE order_table = " + table.toInt() +
                    " AND (order_status = " + Properties.IN_KITCHEN + " OR order_status = " + Properties.COMPLETE + ")");
            if(resultSet.next()){
                controller.setOrder(new Order(controller, resultSet.getInt("order_id"),
                        new Table(String.valueOf(resultSet.getInt("order_table"))),
                        new Discount(resultSet.getDouble("order_discount")), resultSet.getDouble("order_total_usd"),
                        getOrderItems(resultSet.getInt("order_id"), 1)));
            }else{
                controller.setOrder(new Order(controller));
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Loading Table");
        }
    }

    /**
     * Retrieves the ordered items of an order
     * @param orderId The id of the order needed
     * @param type If 1 adds each ordered item to the order box, else it does not
     * @return An ArrayList of OrderItem
     */
    public ArrayList<OrderItem> getOrderItems(int orderId, int type){
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        try{
            if(connection.isClosed()){
                openConnection();
            }
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ordered_dish, dish_name, dish_category, " +
                    "dish_price, quantity, comment, order_content.date FROM order_content, dish " +
                    "WHERE ordered_dish = dish_id " + "AND order_id = " + orderId);
            while (rs.next()){
                orderItems.add(new OrderItem(new Dish(rs.getString("dish_name"),
                        rs.getString("dish_category"), rs.getDouble("dish_price"),
                        rs.getInt("ordered_dish")), rs.getString("comment"),
                        rs.getInt("quantity"), rs.getTimestamp("date").toLocalDateTime()));
                if(type == 1){
                    controller.getOrderBox().getChildren().add(new OrderItemBox(controller, orderItems.get(orderItems.size() - 1)));
                }
            }
            stmt.close();
            return orderItems;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Ordered Items");
            closeConnection();
        }
        return orderItems;
    }

    /**
     * Changes the order's table
     * @param table The table which the order will be moved to
     * @param orderId The id of the order to be moved
     */
    public void transferOrder(Table table, int orderId){
        try{
            openConnection();
            preparedStatement = connection.prepareStatement("UPDATE restaurantdb.order SET order_table = ? WHERE order_id = ?");
            preparedStatement.setInt(1, table.toInt());
            preparedStatement.setInt(2, orderId);
            preparedStatement.execute();
            preparedStatement.close();
            closeConnection();
            controller.getOrder().setOrderSource(table);
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Changing Order's Table");
        }
    }

    /**
     * Transfers the ordered item from one order to the selected one and if the table does not have an open order,
     * then it creates a new one
     * @param table Selected table to move the order to
     * @param item The item that needs to be moved
     * @param oldOrderId The order id that the item will be moved from
     */
    public void transferItem(Table table, OrderItem item, int oldOrderId){
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT order_id FROM restaurantdb.order WHERE order_table = " +
                    table.toInt() + " AND (order_status = " + Properties.IN_KITCHEN + " OR order_status = " + Properties.COMPLETE + ")");
            if(resultSet.next()){
                moveItem(item, oldOrderId, resultSet.getInt("order_id"));

                refreshOrder(resultSet.getInt("order_id"));
            }else{
                moveItem(item, oldOrderId, addOrder(new Order(table, item), 2));
            }
            refreshOrder(oldOrderId);
            closeConnection();
            showMessage("Transfer Completed Successfully");
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Transferring Dish");
        }
    }

    /**
     * Moves ordered item from one order to another
     * @param item
     * @param oldOrderId
     * @param newOrderId
     * @throws SQLException
     */
    private void moveItem(OrderItem item, int oldOrderId, int newOrderId) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement("UPDATE order_content SET order_id = ? WHERE order_id = ? " +
                "AND ordered_dish = ? AND order_content.date = ?;");
        stmt.setInt(1, newOrderId);
        stmt.setInt(2, oldOrderId);
        stmt.setInt(3, item.getItem().getId());
        stmt.setTimestamp(4, Timestamp.valueOf(item.getDate()));
        stmt.execute();
        stmt.close();
    }

    private void refreshOrder(int orderId){
        try{
            if(connection.isClosed()){
                openConnection();
            }
            double total = 0;
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("SELECT order_discount FROM restaurantdb.order WHERE order_id = " + orderId);
            if(rs.next()){
                total = getTotal(getOrderItems(orderId, 2), rs.getDouble("order_discount"));
            }

            PreparedStatement stmt = connection.prepareStatement("UPDATE restaurantdb.order SET " +
                    "order_total_usd = ?, order_total_lbp = ? WHERE order_id = ?");
            stmt.setDouble(1, total);
            stmt.setDouble(2, total * rate);
            stmt.setInt(3, orderId);
            stmt.execute();

            stat.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Refreshing Order");
        }
    }

    private int getLastId(){
        int id = 0;
        try{
            if(connection.isClosed()){
                openConnection();
            }
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(order_id) AS id FROM restaurantdb.order;");
            while (rs.next()){
                if(rs.getInt("id") >= id)
                    return rs.getInt("id") + 1;
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Getting Last Order Id");
            closeConnection();
        }
        return id;
    }

    public double getExchangeRate(){
        double rate = 0;
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM exchange_rate;");
            while (resultSet.next()){
                rate = resultSet.getDouble("rate");
            }
            statement.close();
            closeConnection();
            return rate;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Exchange Rate From Database");
            closeConnection();
        }
        return rate;
    }

    public String login(String id){
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM waiter WHERE waiter_id = " + id);
            if(resultSet.next()){
                return resultSet.getString("waiter_name");
            }else{
                resultSet = statement.executeQuery("SELECT * FROM manager WHERE manager_id = " + id);
                if(resultSet.next()){
                    return resultSet.getString("manager_name");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error");
        }
        return null;
    }

    public double getTotal(ArrayList<OrderItem> orderItems, double discount){
        double total = 0;
        for(OrderItem item : orderItems){
            total += item.getTotalPrice();
        }
        return total - (total * discount / 100);
    }

    public double getRate() {
        return rate;
    }

    private void showAlert(String error){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(controller.getOwner());
        alert.setTitle("ERROR");
        alert.setHeaderText(error);
        alert.showAndWait();
        closeConnection();
    }

    public void showMessage(String header){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(controller.getOwner());
        alert.setTitle("Completed Successfully");
        alert.setHeaderText(header);
        alert.showAndWait();
    }

}
