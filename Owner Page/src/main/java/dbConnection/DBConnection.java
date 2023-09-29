package dbConnection;


import javafx.scene.control.Alert;
import manager.Staff;
import menu.Dish;
import menu.Ingredient;
import menu.InventoryItem;
import order.Order;
import order.OrderItem;
import section.Section;
import settings.Category;
import settings.Unit;
import statistics.Tip;

import java.sql.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DBConnection {

    private final String dbURl = "jdbc:mysql://localhost:3306/restaurantdb",
            username = "root", password = "root";
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Alert alert;
    public String ownerName = "";
    private int rate = 0;
    private final String pattern = "#,###.00";
    private DecimalFormat formatter;

    public DBConnection(){
        formatter = new DecimalFormat(pattern);
    }

    public void openConnection(){
        try{
            connection = DriverManager.getConnection(dbURl, username, password);
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Connecting To Database");
        }
    }
    public void closeConnection(){
        try{
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Disconnecting To Database");
        }
    }

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
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Data From Database");
        }
        return employees;
    }

    private void getManagerOrWaiter(ArrayList<Staff> list, String tableName) throws SQLException{
        // Retrieve Staff from manager or waiter table
        String query = "SELECT * FROM " + tableName + ";";
        resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            list.add(new Staff(resultSet.getString(tableName + "_name"), resultSet.getString(tableName + "_phone"),
                    resultSet.getDate(tableName + "_date_joined").toString(), resultSet.getString(tableName + "_shift"),
                    resultSet.getString(tableName + "_section"), resultSet.getInt(tableName + "_age"),
                    resultSet.getDouble(tableName + "_salary_usd"),resultSet.getDouble(tableName + "_salary_usd"),
                    resultSet.getInt(tableName + "_id")));
        }
    }
    private void getOtherEmployees(ArrayList<Staff> list, String tableName) throws SQLException{
        // Retrieves Other Staff from DB
        String query = "SELECT * FROM " + tableName + ";";
        resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            list.add(new Staff(resultSet.getString(tableName + "_name"), resultSet.getInt(tableName + "_age"),
                    resultSet.getDouble(tableName + "_salary_usd"), resultSet.getDouble(tableName + "_salary_lbp"),
                    resultSet.getString(tableName + "_phone"), resultSet.getDate(tableName + "_date_joined").toString(),
                    resultSet.getString(tableName + "_shift"), resultSet.getInt(tableName + "_id")));
        }
    }

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
            while (resultSet.next()){
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

    public Tip getTotalTips(){
        try{
            openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT SUM(tip_usd) AS total_usd, SUM(tip_lbp) AS total_lbp " +
                    "FROM tips WHERE YEAR(tip_date) = YEAR(CURDATE()) AND MONTH(tip_date) = MONTH(CURDATE());");
            while (resultSet.next()){
                return new Tip(resultSet.getDouble("total_usd"), resultSet.getDouble("total_lbp"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Retrieving Tips From Database");
        }
        return new Tip(0, 0);
    }

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

    private void insertWaiterOrManager(String tableName, Staff staff) throws SQLException {
        // Query for waiters and managers
        preparedStatement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES (?,?,?,?,?,?,?,?,?);");
        preparedStatement.setInt(1, staff.getId());
        preparedStatement.setString(2, staff.getName());
        preparedStatement.setInt(3, staff.getAge());
        preparedStatement.setString(4, staff.getPhoneNumber());
        preparedStatement.setDouble(5, staff.getSalaryUSD());
        preparedStatement.setDouble(6, staff.getSalaryLBP());
        preparedStatement.setString(7, staff.getShift());
        preparedStatement.setDate(8, Date.valueOf(staff.getDateJoined()));
        preparedStatement.setString(9, staff.getSection());
    }
    private void insertOtherStaff(String tableName, Staff staff) throws SQLException{
        // Query for cashiers and kitchen staff
        preparedStatement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES(?,?,?,?,?,?,?,?);");
        preparedStatement.setInt(1, staff.getId());
        preparedStatement.setString(2, staff.getName());
        preparedStatement.setInt(3, staff.getAge());
        preparedStatement.setString(4, staff.getPhoneNumber());
        preparedStatement.setDouble(5, staff.getSalaryUSD());
        preparedStatement.setDouble(6, staff.getSalaryLBP());
        preparedStatement.setString(7, staff.getShift());
        preparedStatement.setDate(8, Date.valueOf(staff.getDateJoined()));
    }

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

    private void insertIngredients(int dishId, ArrayList<Ingredient> ingredients){
        try{
            if(connection.isClosed())
                openConnection();
            PreparedStatement stmt = null;
            for(int i = 0; i < ingredients.size(); i++){
                System.out.println(ingredients);
                stmt = connection.prepareStatement("INSERT INTO ingredient VALUES (?,?,?)");
                stmt.setInt(1, dishId);
                stmt.setInt(2, ingredients.get(i).getId());
                System.out.println(ingredients.get(i).getId() + "Inserted");
                stmt.setDouble(3, ingredients.get(i).getQuantity());
                stmt.execute();
            }
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
            showAlert("Error Inserting Ingredients In Database");
        }
    }

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

    public void delete(String tableName, int id){
        // Deletes entries from table via the id
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
    private void editWithSection(String tableName, Staff staff) throws SQLException{
        preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET " + tableName + "_name = ?, " +
                tableName + "_age = ?, " + tableName + "_phone = ?, " + tableName + "_salary_usd = ?, " + tableName + "_salary_lbp = ?, " +
                tableName + "_shift = ?, " + tableName + "_date_joined = ?, " + tableName + "_section = ? WHERE " + tableName + "_id = ?");
        preparedStatement.setString(1, staff.getName());
        preparedStatement.setInt(2, staff.getAge());
        preparedStatement.setString(3, staff.getPhoneNumber());
        preparedStatement.setDouble(4, staff.getSalaryUSD());
        preparedStatement.setDouble(5, staff.getSalaryLBP());
        preparedStatement.setString(6, staff.getShift());
        preparedStatement.setDate(7, Date.valueOf(staff.getDateJoined()));
        preparedStatement.setString(8, staff.getSection());
        preparedStatement.setInt(9, staff.getId());
    }
    private void editWithoutSection(String tableName, Staff staff) throws SQLException{
        preparedStatement = connection.prepareStatement("UPDATE " + tableName + "SET " + tableName + "_name = ?, "
                + tableName + "_age = ?, " + tableName + "_phone = ?, " + tableName + "_salary_usd = ?, " +
                tableName + "_salary_lbp = ?, " + tableName + "_shift = ?, " + tableName +
                "_date_joined = ? WHERE " + tableName + "_id = ?");
        preparedStatement.setString(1, staff.getName());
        preparedStatement.setInt(2, staff.getAge());
        preparedStatement.setString(3, staff.getPhoneNumber());
        preparedStatement.setDouble(4, staff.getSalaryUSD());
        preparedStatement.setDouble(5, staff.getSalaryLBP());
        preparedStatement.setString(6, staff.getShift());
        preparedStatement.setDate(7, Date.valueOf(staff.getDateJoined()));
        preparedStatement.setInt(8, staff.getId());
    }

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

    private void showAlert(String error){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(error);
        alert.showAndWait();
    }

}
