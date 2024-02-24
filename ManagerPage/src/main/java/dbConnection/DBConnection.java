package dbConnection;


import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import staff.Salary;
import staff.Staff;
import menu.Dish;
import menu.Ingredient;
import menu.InventoryItem;
import order.Order;
import order.OrderItem;
import section.Section;
import settings.Category;
import settings.Unit;
import staff.Tip;

import java.sql.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DBConnection {

    /**
     * The connection link to the database
     */
    private final String dbURl = "jdbc:mysql://localhost:3306/restaurantdb",
            /**
             * The username of the database user
             */
            username = "root",
            /**
             * The password of the database user
             */
            password = "root";
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Alert alert;
    public String ownerName = "";
    private int rate = 0;
    private final String pattern = "#,##0.##";
    private DecimalFormat formatter;

    public DBConnection(){
        formatter = new DecimalFormat(pattern);
    }

    /**
     * @Function Opens the connection to the database
     */
    public void openConnection(){
        try{
            connection = DriverManager.getConnection(dbURl, username, password);
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Connecting To Database");
        }
    }
    /**
     * @Function Closes the connection to the database
     */
    public void closeConnection(){
        try{
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Disconnecting To Database");
        }
    }

    /**
     * Retrieves a specific type of employees depending on the table name from the database
     * @param tableName The name of table of the specific employee type
     * @return A list of the employees
     */
    public ArrayList<Staff> getEmployees(String tableName){
        // Retrieves staff from the DB
        ArrayList<Staff> employees = new ArrayList<>();
        try{
            openConnection();
            statement = connection.createStatement();
            rate = getExchangeRate();
            if(tableName.equals("manager") || tableName.equals("waiter")){
                getManagerOrWaiter(employees, tableName);
            }else{
                getOtherEmployees(employees, tableName);
            }
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Data From Database");
        }
        return employees;
    }

    /**
     * Retrieves either the supervisors or the waiters from the database
     * @param list The list which will store the employees
     * @param tableName The name of the employee type
     * @throws SQLException if the employee type was not found in the database
     */
    private void getManagerOrWaiter(ArrayList<Staff> list, String tableName) throws SQLException{
        // Retrieve Staff from manager or waiter table
        String query = "SELECT * FROM " + tableName + ";";
        resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            list.add(new Staff(resultSet.getString(tableName + "_name"), resultSet.getString(tableName + "_phone"),
                    resultSet.getDate(tableName + "_date_joined").toString(), resultSet.getString(tableName + "_shift"),
                    resultSet.getString(tableName + "_section"), resultSet.getInt(tableName + "_age"),
                    new Salary(resultSet.getDouble(tableName + "_salary_usd"), resultSet.getDouble(tableName + "_salary_lbp")),
                    resultSet.getInt(tableName + "_id")));
        }
    }
    /**
     * Retrieves either the cashiers or the kitchen crew from the database
     * @param list The list which will store the employees
     * @param tableName The name of the employee type
     * @throws SQLException if the employee type was not found in the database
     */
    private void getOtherEmployees(ArrayList<Staff> list, String tableName) throws SQLException{
        // Retrieves Other Staff from DB
        String query = "SELECT * FROM " + tableName + ";";
        resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            list.add(new Staff(resultSet.getString(tableName + "_name"), resultSet.getInt(tableName + "_age"),
                    new Salary(resultSet.getDouble(tableName + "_salary_usd"), resultSet.getDouble(tableName + "_salary_lbp")),
                    resultSet.getString(tableName + "_phone"), resultSet.getDate(tableName + "_date_joined").toString(),
                    resultSet.getString(tableName + "_shift"), resultSet.getInt(tableName + "_id")));
        }
    }

    /**
     * Retrieves all the dishes with their respective ingredients from the database
     * @return A list of ingredients
     */
    public ArrayList<Dish> getDishes(){
        ArrayList<Dish> dishes = new ArrayList<>();
        try{
            openConnection();
            statement = connection.createStatement();
            String query = "SELECT * FROM dish;";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                dishes.add(new Dish(resultSet.getString("dish_name"), resultSet.getString("dish_category"),
                        resultSet.getDouble("dish_price"), resultSet.getInt("dish_id"),
                        resultSet.getInt("dish_status"), getIngredients(resultSet.getInt("dish_id"))));
            }
            closeConnection();
            statement.close();
            return dishes;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Menu Items from Database");
        }
        return dishes;
    }

    /**
     * Retrieves the ingredients of a specific dish
     * @param dishId The id of the dish
     * @return A list of ingredients
     */
    public ArrayList<Ingredient> getIngredients(int dishId){
        ArrayList<Ingredient> inventoryItems = new ArrayList<>();
        try{
            if(connection.isClosed())
                openConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ingredient.ingredient_id AS ingredient_id, inventory.item_name AS name, inventory.item_unit as unit, " +
                    "ingredient.ingredient_quantity as quantity, inventory.item_cost as cost_per_unit FROM " +
                    "inventory, ingredient WHERE inventory.item_id = ingredient.ingredient_id AND ingredient.dish_id = " + dishId + ";");
            while (rs.next()){
                double cost = rs.getDouble("cost_per_unit");
                double quantity = rs.getDouble("quantity");
                inventoryItems.add(new Ingredient(rs.getString("name"), rs.getString("unit"),
                        quantity, cost, rs.getInt("ingredient_id")));
            }
            stmt.close();
            return inventoryItems;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Ingredients From Database");
        }
        return inventoryItems;
    }

    /**
     * Retrieves all the items available in the database with their quantities etc
     * @return A list of inventory items
     */
    public ArrayList<InventoryItem> getInventoryItems(){
        try{
            ArrayList<InventoryItem> items = new ArrayList<>();
            openConnection();
            statement = connection.createStatement();
            String query = "SELECT * FROM inventory";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                items.add(new InventoryItem(resultSet.getString("item_name"), resultSet.getString("item_unit"),
                        resultSet.getString("item_category"), resultSet.getDouble("item_quantity"),
                        resultSet.getDouble("item_cost"), resultSet.getInt("item_id")));
            }
            closeConnection();
            statement.close();
            return items;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Getting Inventory Items From Database");
        }
        return null;
    }

    /**
     * Retrieves all the inventory or menu's category from the database
     * @param tableName The name of the category's database table
     * @return A list containing the category
     */
    public ArrayList<Category> getCategory(String tableName){
        ArrayList<Category> list = new ArrayList<>();
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            while (resultSet.next()){
                list.add(new Category(resultSet.getString("category_name")));
            }
            statement.close();
            closeConnection();
            return list;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Categories From " + tableName);
        }
        return list;
    }

    /**
     * Retrieves all the units from the database
     * @return A list of units
     */
    public ArrayList<Unit> getUnits(){
        ArrayList<Unit> list = new ArrayList<>();
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM units;");
            while (resultSet.next()){
                list.add(new Unit(resultSet.getString("unit")));
            }
            return list;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Units From Database");
        }
        return list;
    }

    /**
     * Retrieves all the orders from the database
     * @return A list of the orders
     */
    public ArrayList<Order> getOrders(){
        ArrayList<Order> list = new ArrayList<>();
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM restaurantdb.order");
            while (resultSet.next()){
                list.add(new Order(resultSet.getInt("order_id"), resultSet.getInt("order_table"),
                        resultSet.getTimestamp("order_date").toLocalDateTime(), resultSet.getDouble("order_discount"),
                        resultSet.getDouble("order_total_usd"), resultSet.getDouble("order_total_lbp"),
                        resultSet.getDouble("order_paid_usd"), resultSet.getDouble("order_paid_lbp"),
                        getOrderItems(resultSet.getInt("order_id"))));
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

    /**
     * Retrieves the ordered items of a specific order from the database
     * @param orderId The id of the order which we need the ordered items from
     * @return A list of the ordered items
     */
    public ArrayList<OrderItem> getOrderItems(int orderId){
        ArrayList<OrderItem> list = new ArrayList<>();
        try{
            if(connection.isClosed()){
                openConnection();
            }
            Statement stmt = connection.createStatement();
            String query = "SELECT dish.dish_name as name, order_content.quantity as quantity, order_content.comment as comment" +
                    " FROM dish, order_content WHERE order_content.ordered_dish = dish.dish_id AND order_content.order_id = " + orderId;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                list.add(new OrderItem(rs.getString("name"), rs.getString("comment"),
                        rs.getInt("quantity")));
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

    /**
     * Retrieves the total USD and LBP tips in a specific time interval from the database
     * @return The total USD and LBP tip
     */
    /**
     * Retrieves the total USD and LBP tips in a specific time interval from the database
     * @param interval The time interval to get the tips
     * @return The total USD and LBP tip
     */
    public Tip getTotalTips(String interval){
        try{
            String timeInterval = "";
            switch (interval){
                case "This Month": timeInterval = "YEAR(tip_date) = YEAR(CURDATE()) AND MONTH(tip_date) = MONTH(CURDATE())"; break;
                case "Last Month": timeInterval = "EXTRACT(YEAR_MONTH FROM tip_date) = EXTRACT(YEAR_MONTH FROM CURDATE() - INTERVAL 1 MONTH)"; break;
                case "Last 30 Days": timeInterval = "tip_date > NOW() - INTERVAL 30 DAY"; break;
                case "This Week": timeInterval = "WEEK(tip_date) = WEEK(NOW())"; break;
                case "Last 7 days": timeInterval = "tip_date > NOW() - INTERVAL 7 DAY"; break;
                case "Today": timeInterval = "tip_date = CURDATE()"; break;
            }
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT SUM(tip_usd) AS total_usd, SUM(tip_lbp) AS total_lbp " +
                    "FROM tips WHERE " + timeInterval + ";");
            while (resultSet.next()){
                return new Tip(resultSet.getDouble("total_usd"), resultSet.getDouble("total_lbp"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Tips From Database");
        }
        return new Tip(0, 0);
    }

    /**
     * Retrieves the USD and LBP total revenues from the orders in the last 6 months from the database
     * @return An XYChart.Series containing the month and the USD and LBP total revenue
     */
    public XYChart.Series<String, Double>[] getRevenue(){
        XYChart.Series<String, Double> revenueUSD = new XYChart.Series<>(),
                            revenueLBP = new XYChart.Series<>();
        revenueUSD.setName("Revenue USD");
        revenueLBP.setName("Revenue LBP");
        try{
            openConnection();
            statement = connection.createStatement();
            String query = "SELECT SUM(order_paid_usd) AS total_usd, SUM(order_paid_lbp) AS total_lbp, " +
                    "CONCAT(MONTH(order_date), ' ', YEAR(order_date)) AS revenue_date FROM restaurantdb.order WHERE " +
                    "order_date BETWEEN CURDATE() - INTERVAL 6 MONTH AND CURDATE() GROUP BY revenue_date";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                revenueUSD.getData().add(new XYChart.Data<>(resultSet.getString("revenue_date"), resultSet.getDouble("total_usd")));
                revenueLBP.getData().add(new XYChart.Data<>(resultSet.getString("revenue_date"), resultSet.getDouble("total_lbp")));
            }
            closeConnection();
            return  new XYChart.Series[] {revenueUSD, revenueLBP};
        }catch (SQLException e){
            e.printStackTrace();
            closeConnection();
            showAlert("Error Retrieving Revenue From Database");
        }
        return  new XYChart.Series[] {revenueUSD, revenueLBP};
    }

    /**
     * Retrieves the 10 most ordered dishes from the database
     * @return A XYChart.Series containing the name and the number of times a dish was ordered.
     */
    public XYChart.Series<String, Integer> getTopDishes(){
        XYChart.Series<String, Integer> topDishes = new XYChart.Series<>();
        topDishes.setName("Top 10 Most Ordered Dishes");
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT d.dish_name, COUNT(*) AS dish_count FROM dish AS d, " +
                    "order_content AS o WHERE d.dish_id = o.ordered_dish GROUP BY o.ordered_dish ORDER BY dish_count " +
                    "DESC LIMIT 10");
            while (resultSet.next()){
                topDishes.getData().add(new XYChart.Data<>(resultSet.getString("dish_name"), resultSet.getInt("dish_count")));
            }
            closeConnection();
            return topDishes;
        }catch (SQLException e){
            e.printStackTrace();
            closeConnection();
            showAlert("Error Retrieving Most Ordered Dishes");
        }
        return topDishes;
    }

    /**
     * Inserts a new Staff member to the database depending on the type of the employee
     * @param tableName The type of the employee
     * @param staff The new employee's information to be added
     */
    public void insertStaff(String tableName, Staff staff){
        // Inserts staff to the DB depending on there type
        try{
            openConnection();
            if(tableName.equals("manager") || tableName.equals("waiter")){
                insertWaiterOrManager(tableName, staff);
            }else{
                insertOtherStaff(tableName, staff);
            }
            preparedStatement.execute();
            closeConnection();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Inserting Data To Database");
        }
    }

    /**
     * Inserts a new supervisor or waiter in the database
     * @param tableName The type of the employee
     * @param staff The new employee's information
     * @throws SQLException if there was an error in the information of the new employee or the employee type
     * was not found
     */
    private void insertWaiterOrManager(String tableName, Staff staff) throws SQLException {
        // Query for waiters and managers
        preparedStatement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES (?,?,?,?,?,?,?,?,?);");
        preparedStatement.setInt(1, staff.getId());
        preparedStatement.setString(2, staff.getName());
        preparedStatement.setInt(3, staff.getAge());
        preparedStatement.setString(4, staff.getPhoneNumber());
        preparedStatement.setDouble(5, staff.getSalary().getUsd());
        preparedStatement.setDouble(6, staff.getSalary().getLbp());
        preparedStatement.setString(7, staff.getShift());
        preparedStatement.setDate(8, Date.valueOf(staff.getDateJoined()));
        preparedStatement.setString(9, staff.getSection());
    }
    /**
     * Inserts a new cashier or kitchen member in the database
     * @param tableName The type of the employee
     * @param staff The new employee's information
     * @throws SQLException if there was an error in the information of the new employee or the employee type
     * was not found
     */
    private void insertOtherStaff(String tableName, Staff staff) throws SQLException{
        // Query for cashiers and kitchen staff
        preparedStatement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES(?,?,?,?,?,?,?,?);");
        preparedStatement.setInt(1, staff.getId());
        preparedStatement.setString(2, staff.getName());
        preparedStatement.setInt(3, staff.getAge());
        preparedStatement.setString(4, staff.getPhoneNumber());
        preparedStatement.setDouble(5, staff.getSalary().getUsd());
        preparedStatement.setDouble(6, staff.getSalary().getLbp());
        preparedStatement.setString(7, staff.getShift());
        preparedStatement.setDate(8, Date.valueOf(staff.getDateJoined()));
    }

    /**
     * Inserts a new item in the inventory in the database
     * @param item The new item's information to be added
     */
    public void insertInventoryItem(InventoryItem item){
        try{
            openConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO inventory VALUES (?,?,?,?,?,?)");
            preparedStatement.setInt(1, item.getId());
            preparedStatement.setString(2, item.getName());
            preparedStatement.setString(3, item.getCategory());
            preparedStatement.setString(4, item.getUnit());
            preparedStatement.setDouble(5, item.getQuantity());
            preparedStatement.setDouble(6, item.getPrice());
            preparedStatement.execute();
            closeConnection();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Inserting Item To Database");
        }
    }

    /**
     * Inserts a new dish with its ingredients in the database
     * @param dish The new dish's information to be added
     */
    public void insertDish(Dish dish){
        try{
            openConnection();
            int id = getLastId("dish");
            preparedStatement = connection.prepareStatement("INSERT INTO dish VALUES (?,?,?,?,?)");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, dish.getName());
            preparedStatement.setString(3, dish.getCategory());
            preparedStatement.setDouble(4, dish.getPrice());
            preparedStatement.setInt(5, 0);
            preparedStatement.execute();
            insertIngredients(id, dish.getIngredients());
            closeConnection();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Inserting Dish In Database");
        }
    }

    /**
     * Inserts the ingredients of a dish in the database
     * @param dishId The id of the dish related to the ingredients
     * @param ingredients A list of the ingredients to be added
     */
    private void insertIngredients(int dishId, ArrayList<Ingredient> ingredients){
        try{
            if(connection.isClosed())
                openConnection();
            PreparedStatement stmt = null;
            for(int i = 0; i < ingredients.size(); i++){
                stmt = connection.prepareStatement("INSERT INTO ingredient VALUES (?,?,?)");
                stmt.setInt(1, dishId);
                stmt.setInt(2, ingredients.get(i).getId());
                stmt.setDouble(3, ingredients.get(i).getQuantity());
                stmt.execute();
            }
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Inserting Ingredients In Database");
        }
    }

    /**
     * Inserts a new category in the database depending on the category type
     * @param tableName The category type either the dish or the inventory item category
     * @param category The new category to be added
     */
    public void insertCategory(String tableName, Category category){
        try{
            if(!checkIfExist(tableName, category.getName())){
                openConnection();
                preparedStatement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES (?);");
                preparedStatement.setString(1, category.getName());
                preparedStatement.execute();
                preparedStatement.close();
                closeConnection();
            }else{
                showAlert("Category Already Exist");
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Inserting Category In " + tableName);
        }
    }

    /**
     * Inserts a new unit in the database to be used for measuring quantities
     * @param unit The new unit to be added
     */
    public void insertUnit(Unit unit){
        try{
            openConnection();
            if(checkIfExist("units", unit.getUnit())){
                showAlert("Unit Already Exist");
            }else{
                preparedStatement = connection.prepareStatement("INSERT INTO units VALUES (?);");
                preparedStatement.setString(1, unit.getUnit());
                preparedStatement.execute();
                preparedStatement.close();
                closeConnection();
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Inserting Unit In Database");
        }
    }

    /**
     * Deletes a record from the database depending on the id
     * @param tableName The name of the database table we need to delete a record from
     * @param id The id of the record to be deleted
     */
    public void delete(String tableName, int id){
        String name = tableName;
        try{
            if(tableName.equals("inventory")){
                name = "item";
            }else if(tableName.equals("dish")){
                deleteIngredients(id);
            }
            openConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + name + "_id = ? ;");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            closeConnection();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Deleting " + name + " From Database");
        }
    }

    /**
     * Deletes the ingredients of a dish from the database (The code might not be correct please check later)
     * @param dishId The id of the dish which the ingredients need to be deleted
     */
    private void deleteIngredients(int dishId){
        // This is Not Correct and should be changed.
        try{
            if(connection.isClosed())
                openConnection();
            PreparedStatement stmt = null;
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ingredient WHERE dish_id = " + dishId + ";");
            while (resultSet.next()){
                stmt = connection.prepareStatement("DELETE FROM ingredient WHERE dish_id = ? ;");
                stmt.setInt(1, dishId);
                stmt.execute();
            }
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Deleting Ingredients From Database");
        }
    }

    /**
     * Deletes a category from the database depending on the category type
     * @param tableName The type of the category either a dish category or the inventory item category
     * @param category the category to be deleted
     */
    public void deleteCategory(String tableName, Category category){
        try{
            openConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE category_name = ?;");
            preparedStatement.setString(1, category.getName());
            preparedStatement.execute();
            preparedStatement.close();
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Deleting Category From Database");
        }
    }

    /**
     * Deletes a unit from the database
     * @param unit The unit to be deleted
     */
    public void deleteUnit(Unit unit){
        try{
            openConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM units WHERE unit = ?;");
            preparedStatement.setString(1, unit.getUnit());
            preparedStatement.execute();
            preparedStatement.close();
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Deleting Unit From Database");
        }
    }

    /**
     * Updates a staff's information in the database depending on the staff's type
     * @param tableName The type of the employee (waiter, manager, cashier, kitchen)
     * @param staff The staff's updated information
     */
    public void editStaff(String tableName, Staff staff){
        try{
            openConnection();
            if(tableName.equals("manager") || tableName.equals("waiter")){
                editWithSection(tableName, staff);
            }else{
                editWithoutSection(tableName, staff);
            }
            preparedStatement.execute();
            preparedStatement.close();
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Editing Staff");
        }
    }

    /**
     * Updates either a waiter's or supervisor's information in the database
     * @param tableName the type of the employee (waiter, manager)
     * @param staff The staff's updated info
     * @throws SQLException If there was an error in the staff's info or in the type or in the database connection
     */
    private void editWithSection(String tableName, Staff staff) throws SQLException{
        preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET " + tableName + "_name = ?, " +
                tableName + "_age = ?, " + tableName + "_phone = ?, " + tableName + "_salary_usd = ?, " + tableName + "_salary_lbp = ?, " +
                tableName + "_shift = ?, " + tableName + "_date_joined = ?, " + tableName + "_section = ? WHERE " + tableName + "_id = ?");
        preparedStatement.setString(1, staff.getName());
        preparedStatement.setInt(2, staff.getAge());
        preparedStatement.setString(3, staff.getPhoneNumber());
        preparedStatement.setDouble(4, staff.getSalary().getUsd());
        preparedStatement.setDouble(5, staff.getSalary().getLbp());
        preparedStatement.setString(6, staff.getShift());
        preparedStatement.setDate(7, Date.valueOf(staff.getDateJoined()));
        preparedStatement.setString(8, staff.getSection());
        preparedStatement.setInt(9, staff.getId());
    }
    /**
     * Updates either a cashier's or kitchen member's information in the database
     * @param tableName the type of the employee (cashier, kitchen)
     * @param staff The staff's updated info
     * @throws SQLException If there was an error in the staff's info or in the type or in the database connection
     */
    private void editWithoutSection(String tableName, Staff staff) throws SQLException{
        preparedStatement = connection.prepareStatement("UPDATE " + tableName + "SET " + tableName + "_name = ?, "
                + tableName + "_age = ?, " + tableName + "_phone = ?, " + tableName + "_salary_usd = ?, " +
                tableName + "_salary_lbp = ?, " + tableName + "_shift = ?, " + tableName +
                "_date_joined = ? WHERE " + tableName + "_id = ?");
        preparedStatement.setString(1, staff.getName());
        preparedStatement.setInt(2, staff.getAge());
        preparedStatement.setString(3, staff.getPhoneNumber());
        preparedStatement.setDouble(4, staff.getSalary().getUsd());
        preparedStatement.setDouble(5, staff.getSalary().getLbp());
        preparedStatement.setString(6, staff.getShift());
        preparedStatement.setDate(7, Date.valueOf(staff.getDateJoined()));
        preparedStatement.setInt(8, staff.getId());
    }

    /**
     * Updates an inventory item in the database
     * @param item The item's updated information
     */
    public void editInventoryItem(InventoryItem item){
        try{
            openConnection();
            preparedStatement = connection.prepareStatement("UPDATE inventory SET item_name = ?, item_category = ?, item_unit = ?," +
                    "item_quantity = ?, item_cost = ? WHERE item_id = ?;");
            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getCategory());
            preparedStatement.setString(3, item.getUnit());
            preparedStatement.setDouble(4, item.getQuantity());
            preparedStatement.setDouble(5, item.getPrice());
            preparedStatement.setInt(6, item.getId());
            preparedStatement.execute();
            closeConnection();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Editing Item In Database");
        }
    }

    /**
     * Updates a dish in the database
     * @param dish The dish's updated information
     */
    public void editDish(Dish dish){
        try{
            openConnection();
            preparedStatement = connection.prepareStatement("UPDATE dish SET dish_name = ?, dish_category = ?, dish_price = ? WHERE dish_id = ?");
            preparedStatement.setString(1, dish.getName());
            preparedStatement.setString(2, dish.getCategory());
            preparedStatement.setDouble(3, dish.getPrice());
            preparedStatement.setInt(4, dish.getId());
            preparedStatement.execute();
            deleteIngredients(dish.getId());
            System.out.println(dish.getIngredients().get(0).getId() + "editDish");
            insertIngredients(dish.getId(), dish.getIngredients());
            preparedStatement.close();
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Updating Dish In Database");
        }
    }

    /**
     * Changes the status of a dish in the database
     * @param id The id of the dish to be changed
     * @param active The new status of the dish (0 for IN_KITCHEN, 1 for COMPLETED, 2 for CHECKED_OUT)
     */
    public void editDishStatus(int id, int active){
        try{
            openConnection();
            preparedStatement = connection.prepareStatement("UPDATE dish SET dish_status = ? WHERE dish_id = ?");
            preparedStatement.setInt(1, active);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
            preparedStatement.close();
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Updating Dish In Database");
        }
    }

    /**
     * Updates a category in the database depending on its type
     * @param tableName The type of the category either for the menu or the inventory items
     * @param category The category's updated info
     * @param oldCategory The old category's name
     */
    public void editCategory(String tableName, Category category, String oldCategory){
        try{
            if(!checkIfExist(tableName, category.getName())){
                openConnection();
                preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET category_name = ? WHERE category_name = ?;");
                preparedStatement.setString(1, category.getName());
                preparedStatement.setString(2, oldCategory);
                preparedStatement.execute();
                preparedStatement.close();
                closeConnection();
            }else{
                showAlert("Category Already Exist");
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Editing Category In Database");
        }
    }

    /**
     * Updates a unit in the database
     * @param unit The old unit to be updated
     * @param newUnit the unit's updated info
     */
    public void editUnit(Unit unit, String newUnit){
        try{
            openConnection();
            if(checkIfExist("units", newUnit)){
                showAlert("Unit Already Exist");
            }else{
                preparedStatement = connection.prepareStatement("UPDATE units SET unit = ? WHERE unit = ?");
                preparedStatement.setString(1, newUnit);
                preparedStatement.setString(2, unit.getUnit());
                preparedStatement.execute();
                preparedStatement.close();
                closeConnection();
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Editing Unit");
        }
    }

    /**
     * Checks if the staff's id already exist in the database
     * @param id The id to be checked
     * @param tableName The type of the staff (waiter, manager, cashier, kitchen)
     * @return True if the id already exist, else False
     */
    public boolean checkId(int id, String tableName){
        // Checks if the id is already available in the Database.
        if(id <= 1000){
            return true;
        }
        try{
            openConnection();
            statement = connection.createStatement();
            String query = "SELECT " + tableName + "_id FROM " + tableName + " WHERE " + tableName + "_id = " + id + ";";
            resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                return true;
            }
            closeConnection();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Checking Id From Database");
        }
        return false;
    }

    /**
     * Retrieves the last id of a specific table from the database
     * @param tableName The database's table name
     * @return The last id +1
     */
    public int getLastId(String tableName){
        int id = 0;
        String name = tableName;
        try{
            if(tableName.equals("inventory")){
                name = "item";
            }
            openConnection();
            statement = connection.createStatement();
            String query = "SELECT MAX(" + name + "_id) AS max_id FROM " + tableName + ";";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                int dbId = resultSet.getInt("max_id");
                if(id <= dbId){
                    id = dbId + 1;
                }
            }
            return id;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Creating New Id");
        }
        return id;
    }

    /**
     *
     * @param tableName
     * @param columnName
     * @return
     */
    public ArrayList<String> getList(String tableName, String columnName){
        ArrayList<String> list = new ArrayList<>();
        try{
            openConnection();
            String query = "SELECT " + columnName + " FROM " + tableName + ";";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                list.add(resultSet.getString(columnName));
            }
            closeConnection();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Getting " + tableName + " List From Database");
        }
        return list;
    }

    /**
     * Retrieves the tables of a specific section from the database
     * @param section The section from which we need the tables
     * @return A list of tables
     */
    public ArrayList<String> getTables(String section){
        ArrayList<String> list = new ArrayList<>();
        try{
            openConnection();
            String query = "SELECT table_number FROM tables WHERE section = '" + section + "';";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                list.add(resultSet.getString("table_number"));
            }
            closeConnection();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Getting Tables List From Database");
        }
        return list;
    }

    /**
     * Retrieves all the sections with their tables from the database
     * @return A list of sections
     */
    public ArrayList<Section> getSections(){
        ArrayList<Section> sections = new ArrayList<>();
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM section");
            while (resultSet.next()){
                sections.add(new Section(resultSet.getString("section_number"),
                        getSectionTables(resultSet.getString("section_number"))));
            }
            closeConnection();
            statement.close();
            return sections;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Sections From Database");
        }
        return sections;
    }

    /**
     * Retrieves the tables inside a section from the database
     * @param section The section from where we need the tables
     * @return A list of table
     */
    private ArrayList<Section> getSectionTables(String section){
        ArrayList<Section> tables = new ArrayList<>();
        try{
            if(connection.isClosed())
                openConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tables WHERE tables.section = '" + section + "';");
            while (rs.next()){
                tables.add(new Section(rs.getInt("table_number"), rs.getInt("number_seat")));
            }
            stmt.close();
            return tables;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Tables From Database");
        }
        return tables;
    }

    /**
     * Adds a new section in the database if it does not already exist
     * @param section The new section to be added
     */
    public void insertSection(String section){
        try{
            if(!checkIfExist("section", section)){
                openConnection();
                preparedStatement = connection.prepareStatement("INSERT INTO section VALUES (?)");
                preparedStatement.setString(1, section);
                preparedStatement.execute();
                preparedStatement.close();
                closeConnection();
            }else {
                showAlert("Section Name Already Exist");
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Adding New Section To Database\nTwo Sections Cannot have the Same Name");
        }
    }

    /**
     * Updates the info of a section in the database
     * @param section The old section
     * @param newName The new section to be edited
     */
    public void editSection(Section section, String newName){
        try{
            if(!checkIfExist("section", newName)){
                openConnection();
                preparedStatement = connection.prepareStatement("UPDATE section SET section_number = ? WHERE section_number = ?");
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, section.getName());
                preparedStatement.execute();
                preparedStatement.close();
                closeConnection();
            }else{
                showAlert("Section Name Already Exist");
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Editing Section");
        }
    }

    /**
     * Deletes a section or a table from the database
     * @param section The table or section to be deleted
     */
    public void deleteSectionOrTable(Section section){
        try{
            openConnection();
            if(section.getName() == null){
                deleteTable(section);
            }else{
                deleteSection(section);
            }
            preparedStatement.close();
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Deleting Section/Table From Database");
        }
    }
    private void deleteSection(Section section) throws SQLException{
        preparedStatement = connection.prepareStatement("DELETE FROM section WHERE section_number = ?");
        preparedStatement.setString(1, section.getName());
        preparedStatement.execute();
    }
    private void deleteTable(Section section) throws SQLException{
        preparedStatement = connection.prepareStatement("DELETE FROM tables WHERE table_number = ?");
        preparedStatement.setInt(1, section.getTableNumber());
        preparedStatement.execute();
    }

    /**
     * Inserts a new table in a section in the database if the table name does not already exist
     * @param table The new table to be added
     * @param sectionNumber The section where the table need to be added in
     */
    public void insertTable(Section table, String sectionNumber){
        try{
            if(checkIfExist("tables", String.valueOf(table.getTableNumber()))){
                showAlert("Table Already Exist");
            }else{
                openConnection();
                preparedStatement = connection.prepareStatement("INSERT INTO tables VALUES (?,?,?);");
                preparedStatement.setInt(1, table.getTableNumber());
                preparedStatement.setInt(2, table.getNumberOfSeats());
                preparedStatement.setString(3, sectionNumber);
                preparedStatement.execute();
                preparedStatement.close();
                closeConnection();
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Inserting Table To Database");
        }
    }

    /**
     * Checks if the entered Table does not already exist in the database and updates it
     * @param table The table to be added
     * @param tableId The old id of the edited table
     * @param sectionNumber The section of the edited table
     */
    public void editTable(Section table, int tableId, String sectionNumber){
        try{
            if(table.getTableNumber() != tableId){
                if(checkIfExist("tables", String.valueOf(table.getTableNumber()))){
                    showAlert("Table Number Already Taken");
                }
            }else{
                openConnection();
                preparedStatement = connection.prepareStatement("UPDATE tables SET table_number = ?, " +
                        "number_seat = ?, section = ? WHERE table_number = ?;");
                preparedStatement.setInt(1, table.getTableNumber());
                preparedStatement.setInt(2, table.getNumberOfSeats());
                preparedStatement.setString(3, sectionNumber);
                preparedStatement.setInt(4, tableId);
                preparedStatement.execute();
                preparedStatement.close();
                closeConnection();
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Editing Table In Database");
        }
    }

    /**
     * Checks if the login credentials are correct
     * @param username The entered username
     * @param password The entered password
     * @return True if the credentials are correct, else returns False
     */
    public boolean checkOwner(String username, String password){
        try{
            openConnection();
            statement = connection.createStatement();
            String query = "SELECT * FROM owner WHERE owner_username = '" + username +"';";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                if (resultSet.getString("owner_password").equals(password)){
                    ownerName = resultSet.getString("owner_name");
                    return true;
                }
            }
            statement.close();
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Logging In");
        }
        return false;
    }

    /**
     * Checks of a specific item already exists in the database
     * @param tableName The name of the table in the database to be checked
     * @param item The item to be checked
     * @return True if it exists, else returns False
     */
    public boolean checkIfExist(String tableName, String item){
        try{
            openConnection();
            statement = connection.createStatement();
            String query = "SELECT * FROM " + tableName + " WHERE " + tableName + "_number= '" + item + "';";
            if(tableName.equals("tables")){
                query = "SELECT * FROM " + tableName + " WHERE  table_number= " + item + ";";
            }else if(tableName.equals("category") || tableName.equals("item_category")){
                query = "SELECT * FROM " + tableName + " WHERE  category_name = '" + item + "';";
            }else if(tableName.equals("units")) {
                query = "SELECT * FROM units WHERE units.unit = '" + item + "';";
            }
            resultSet = statement.executeQuery(query);
            if(resultSet.next())
                return true;
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Checking If " + tableName + " Already Exist In Database");
        }
        return false;
    }

    /**
     * Retrieves the USD to LBP exchange rate from the database
     * @return The exchange rate as an Integer
     */
    public int getExchangeRate(){
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT rate FROM exchange_rate");
            if(resultSet.next()){
                return resultSet.getInt("rate");
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Getting Exchange Rate From Database");
        }
        return 0;
    }

    /**
     * Changes the USD to LBP exchange rate in the database
     * @param rate The rate to be updated
     */
    public void changeRate(int rate){
        try{
            openConnection();
            preparedStatement = connection.prepareStatement("UPDATE exchange_rate SET rate = ?;");
            preparedStatement.setInt(1, rate);
            preparedStatement.execute();
            preparedStatement.close();
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Updating Exchange Rate");
        }
    }

    /**
     * Displays and error window containing the error's details
     * @param error The error message to be displayed
     */
    private void showAlert(String error){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(error);
        alert.showAndWait();
    }

}
