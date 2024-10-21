import java.sql.*;
import java.util.Objects;

public class DB {

    private final String HOST = "localhost";
    private final String PORT = "8889";
    private final String DB_NAME = "home";
    private final String LOGIN = "root";
    private final String PASS = "root";

    private Connection dbConn = null;

    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
        return dbConn;
    }

    public void isConnected() throws SQLException, ClassNotFoundException {
        dbConn = getDbConnection();
        System.out.println(dbConn.isValid(1000));
    }

    public void setOrders(String name, String thing) throws SQLException, ClassNotFoundException {
        String sql = "SELECT `id`, `login` FROM `users`";
        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);
        String id_us = null;
        String id_it = null;
        while (res.next()) {
            String lg = res.getString("login");
            if (Objects.equals(lg, name)) {
                id_us = res.getString("id");
            }
        }
        String sql_1 = "SELECT `id`, `title`, `category` FROM `items`";
        Statement statement_1 = getDbConnection().createStatement();
        ResultSet res_1 = statement_1.executeQuery(sql_1);
        while (res_1.next()) {
            String cat = res_1.getString("category");
            if (Objects.equals(cat, thing)) {
                id_it = res_1.getString("id");
                String sql_2 = "INSERT INTO `orders` (user_id, item_id) VALUES(?, ?)";
                PreparedStatement prSt = getDbConnection().prepareStatement(sql_2);
                prSt.setString(1, id_us);
                prSt.setString(2, id_it);
                prSt.executeUpdate();
            }
        }
        String name_login = null;
        String name_id_orders = null;
        String name_id_users = null;
        String sql_3 = "SELECT `user_id` FROM `orders`";
        Statement statement_2 = getDbConnection().createStatement();
        ResultSet res_name_id_orders = statement_2.executeQuery(sql_3);
        while (res_name_id_orders.next())
            name_id_orders = res_name_id_orders.getString("user_id");

        String sql_4 = "SELECT `id`, `login` FROM `users`";
        Statement statement_3 = getDbConnection().createStatement();
        ResultSet res_name_id_users = statement_3.executeQuery(sql_4);
        while (res_name_id_users.next()) {
            name_id_users = res_name_id_users.getString("id");
            if (Objects.equals(name_id_orders, name_id_users)) {
                name_login = res_name_id_users.getString("login");
            }
        }

        String thing_id_orders = null;
        String thing_id_items = null;
        String sql_5 = "SELECT `item_id` FROM `orders`";
        Statement statement_4 = getDbConnection().createStatement();
        ResultSet res_thing_id_orders = statement_4.executeQuery(sql_5);
        System.out.println("Все заказы :");
        while (res_thing_id_orders.next()) {
            thing_id_orders = res_thing_id_orders.getString("item_id");
            String sql_6 = "SELECT `id`, `title` FROM `items`";
            Statement statement_5 = getDbConnection().createStatement();
            ResultSet res_thing_id_items = statement_5.executeQuery(sql_6);

            while (res_thing_id_items.next()) {
                thing_id_items = res_thing_id_items.getString("id");

                if (Objects.equals(thing_id_orders, thing_id_items)) {
                    String name_thing = res_thing_id_items.getString("title");
                    System.out.println(name_login + " - " + name_thing);
                }
            }
        }
    }
}

