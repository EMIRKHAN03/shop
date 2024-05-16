package org.example;

import java.sql.*;
import java.util.Scanner;

public class OnlineStore {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/bayman";
    static final String USER = "emirkhanismailov";
    static final String PASS = "0771714629";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("Connected to the database");

            // Подключение к базе данных
            Statement stmt = conn.createStatement();
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("1. Войти как администратор");
                System.out.println("2. Войти как клиент");
                System.out.println("3. Зарегистрироваться как администратор");
                System.out.println("4. Зарегистрироваться как клиент");
                System.out.println("5. Выйти из приложения");
                System.out.print("Выберите опцию: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        // Логин администратора
                        adminLogin(conn, scanner);
                        break;
                    case 2:
                        // Логин клиента
                        clientLogin(conn, scanner);
                        break;
                    case 3:
                        // Регистрация администратора
                        adminRegistration(conn, scanner);
                        break;
                    case 4:
                        // Регистрация клиента
                        clientRegistration(conn, scanner);
                        break;
                    case 5:
                        // Выход из приложения
                        System.out.println("До свидания!");
                        return;
                    default:
                        System.out.println("Неверный ввод, попробуйте еще раз.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void clientRegistration(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        String sql = "INSERT INTO clients (username, password) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        int rowsInserted = pstmt.executeUpdate();

        if (rowsInserted > 0) {
            System.out.println("Вы успешно зарегистрировались как клиент");
        } else {
            System.out.println("Ошибка регистрации");
        }
    }
    public static void adminRegistration(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        String sql = "INSERT INTO admins (username, password) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        int rowsInserted = pstmt.executeUpdate();

        if (rowsInserted > 0) {
            System.out.println("Вы успешно зарегистрировались как администратор");
        } else {
            System.out.println("Ошибка регистрации");
        }
    }



    public static void adminLogin(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("Вы успешно вошли как администратор");
            adminMenu(conn, scanner);
        } else {
            System.out.println("Неверный логин или пароль");
        }
    }

    public static void adminMenu(Connection conn, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("\nМеню администратора:");
            System.out.println("1. Добавить товар");
            System.out.println("2. Удалить товар");
            System.out.println("3. Просмотреть список товаров");
            System.out.println("4. Просмотреть список заказов");
            System.out.println("5. Просмотреть список клиентов");
            System.out.println("6. Редактировать товар");
            System.out.println("7. Редактировать клиента");
            System.out.println("8. Изменить статус выполнения заказа");
            System.out.println("9. Выход из аккаунта");
            System.out.print("Выберите опцию: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Считываем лишний перевод строки

            switch (choice) {
                case 1:
                    System.out.println("\n--- Добавление нового товара ---\n");
                    addProduct(conn, scanner);
                    break;
                case 2:
                    System.out.println("\n--- Удаление товара ---\n");
                    System.out.println();
                    viewProducts(conn);
                    deleteProduct(conn, scanner);
                    break;
                case 3:
                    System.out.println("\n--- Просмотр списка товаров ---\n");
                    viewProducts(conn);
                    break;
                case 4:
                    System.out.println("\n--- Просмотр списка заказов ---\n");
                    viewOrders(conn);
                    break;
                case 5:
                    System.out.println("\n--- Просмотр списка клиентов ---\n");
                    viewClients(conn);
                    break;
                case 6:
                    System.out.println("\n--- Редактирование товара ---\n");
                    System.out.println();
                    viewProducts(conn);
                    System.out.println();
                    editProduct(conn, scanner);
                    break;
                case 7:
                    System.out.println("\n--- Редактирование клиента ---\n");
                    System.out.println();
                    viewClients(conn);
                    System.out.println();
                    editClient(conn, scanner);
                    break;
                case 8:
                    System.out.println("\n--- Изменение статуса заказа ---\n");
                    System.out.println();
                    viewOrders(conn);
                    System.out.println();
                    changeOrderStatus(conn, scanner);
                    break;
                case 9:
                    System.out.println("Выход из аккаунта администратора");
                    return;
                default:
                    System.out.println("Неверный ввод, попробуйте еще раз.");
            }
        }
    }


    public static void addProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Введите название товара: ");
        String name = scanner.nextLine();
        System.out.print("Введите описание товара: ");
        String description = scanner.nextLine();
        System.out.print("Введите цену товара: ");
        double price = scanner.nextDouble();
        System.out.print("Введите количество товара на складе: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "INSERT INTO products (name, description, price, stock_quantity) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setString(2, description);
        pstmt.setDouble(3, price);
        pstmt.setInt(4, stockQuantity);
        pstmt.executeUpdate();

        System.out.println("Товар успешно добавлен");
    }

    public static void deleteProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Введите ID товара для удаления: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Проверяем, есть ли заказы, связанные с этим товаром
        String checkOrdersSql = "SELECT COUNT(*) FROM orders WHERE product_id = ?";
        PreparedStatement checkOrdersStmt = conn.prepareStatement(checkOrdersSql);
        checkOrdersStmt.setInt(1, productId);
        ResultSet rs = checkOrdersStmt.executeQuery();
        rs.next();
        int ordersCount = rs.getInt(1);

        if (ordersCount > 0) {
            System.out.println("Невозможно удалить товар, так как у него есть связанные заказы.");
            return;
        }

        // Удаляем сам товар
        String deleteProductSql = "DELETE FROM products WHERE id = ?";
        PreparedStatement deleteProductStmt = conn.prepareStatement(deleteProductSql);
        deleteProductStmt.setInt(1, productId);
        int deletedRows = deleteProductStmt.executeUpdate();

        if (deletedRows > 0) {
            System.out.println("Товар успешно удален");
        } else {
            System.out.println("Товар с указанным ID не найден");
        }
    }

    public static void viewProducts(Connection conn) throws SQLException {
        String sql = "SELECT * FROM products";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("ID | Название | Описание | Цена | Количество на складе");
        System.out.println("-----------------------------------------------");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            double price = rs.getDouble("price");
            int stockQuantity = rs.getInt("stock_quantity");
            System.out.println(id + " | " + name + " | " + description + " | " + price + " | " + stockQuantity);
        }
    }

    public static void viewOrders(Connection conn) throws SQLException {
        String sql = "SELECT * FROM orders";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("ID | ID клиента | ID товара | Количество | Дата заказа | Подтвержден | Выполнен");
        System.out.println("---------------------------------------------------------------------------");
        while (rs.next()) {
            int id = rs.getInt("id");
            int clientId = rs.getInt("client_id");
            int productId = rs.getInt("product_id");
            int quantity = rs.getInt("quantity");
            Timestamp orderDate = rs.getTimestamp("order_date");
            boolean isConfirmed = rs.getBoolean("is_confirmed");
            boolean isCompleted = rs.getBoolean("is_completed");
            System.out.println(id + " | " + clientId + " | " + productId + " | " + quantity + " | " + orderDate + " | " + isConfirmed + " | " + isCompleted);
        }
    }

    public static void clientLogin(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        String sql = "SELECT * FROM clients WHERE username = ? AND password = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("Вы успешно вошли как клиент");
            clientMenu(conn, scanner);
        } else {
            System.out.println("Неверный логин или пароль");
        }
    }

    public static void clientMenu(Connection conn, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("\nМеню клиента:");
            System.out.println("1. Просмотреть список товаров");
            System.out.println("2. Добавить товар в корзину");
            System.out.println("3. Посмотреть корзину");
            System.out.println("4. Оформить заказ");
            System.out.println("5. Просмотреть список заказов");
            System.out.println("6. Выход из аккаунта");
            System.out.print("Выберите опцию: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Считываем лишний перевод строки

            switch (choice) {
                case 1:
                    System.out.println("\n--- Просмотр списка товаров ---\n");
                    viewProducts(conn);
                    break;
                case 2:
                    System.out.println("\n--- Добавление товара в корзину ---\n");
                    viewProducts(conn);
                    addToCart(conn, scanner);
                    break;
                case 3:
                    System.out.println("\n--- Просмотр корзины ---\n");
                    viewCart(conn);
                    break;
                case 4:
                    System.out.println("\n--- Оформление заказа ---\n");
                    placeOrder(conn);
                    break;
                case 5:
                    System.out.println("\n--- Просмотр списка заказов ---\n");
                    viewClientOrders(conn);
                    break;
                case 6:
                    System.out.println("Выход из аккаунта клиента");
                    return;
                default:
                    System.out.println("Неверный ввод, попробуйте еще раз.");
            }
        }
    }

    public static void addToCart(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Введите ID товара для добавления в корзину: ");
        int productId = scanner.nextInt();
        System.out.print("Введите количество: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "INSERT INTO orders (client_id, product_id, quantity) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, 1); // Предполагаем, что у нас только один клиент для этого примера
        pstmt.setInt(2, productId);
        pstmt.setInt(3, quantity);
        pstmt.executeUpdate();

        System.out.println("Товар успешно добавлен в корзину");
    }

    public static void viewCart(Connection conn) throws SQLException {
        String sql = "SELECT p.id, p.name, o.quantity FROM products p JOIN orders o ON p.id = o.product_id WHERE o.client_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, 1); // Предполагаем, что у нас только один клиент для этого примера
        ResultSet rs = pstmt.executeQuery();

        System.out.println("ID | Название | Количество");
        System.out.println("-----------------------------");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int quantity = rs.getInt("quantity");
            System.out.println(id + " | " + name + " | " + quantity);
        }
    }

    public static void placeOrder(Connection conn) throws SQLException {
        String sql = "UPDATE orders SET is_confirmed = TRUE WHERE client_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, 1); // Предполагаем, что у нас только один клиент для этого примера
        pstmt.executeUpdate();

        System.out.println("Заказ успешно оформлен");
    }

    public static void viewClientOrders(Connection conn) throws SQLException {
        String sql = "SELECT * FROM orders WHERE client_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, 1); // Предполагаем, что у нас только один клиент для этого примера
        ResultSet rs = pstmt.executeQuery();

        System.out.println("ID | ID товара | Количество | Дата заказа | Подтвержден | Выполнен");
        System.out.println("----------------------------------------------------------------");
        while (rs.next()) {
            int id = rs.getInt("id");
            int productId = rs.getInt("product_id");
            int quantity = rs.getInt("quantity");
            Timestamp orderDate = rs.getTimestamp("order_date");
            boolean isConfirmed = rs.getBoolean("is_confirmed");
            boolean isCompleted = rs.getBoolean("is_completed");
            System.out.println(id + " | " + productId + " | " + quantity + " | " + orderDate + " | " + isConfirmed + " | " + isCompleted);
        }
    }

    public static void viewClients(Connection conn) throws SQLException {
        String sql = "SELECT * FROM clients";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("ID | Логин");
        System.out.println("------------");
        while (rs.next()) {
            int id = rs.getInt("id");
            String username = rs.getString("username");
            System.out.println(id + " | " + username);
        }
    }

    public static void editProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Введите ID товара для редактирования: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Введите новое название товара: ");
        String name = scanner.nextLine();
        System.out.print("Введите новое описание товара: ");
        String description = scanner.nextLine();
        System.out.print("Введите новую цену товара: ");
        double price = scanner.nextDouble();
        System.out.print("Введите новое количество товара на складе: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock_quantity = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setString(2, description);
        pstmt.setDouble(3, price);
        pstmt.setInt(4, stockQuantity);
        pstmt.setInt(5, productId);
        int updatedRows = pstmt.executeUpdate();

        if (updatedRows > 0) {
            System.out.println("Товар успешно отредактирован");
        } else {
            System.out.println("Товар с указанным ID не найден");
        }
    }

    public static void editClient(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Введите ID клиента для редактирования: ");
        int clientId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Введите новый логин клиента: ");
        String username = scanner.nextLine();
        System.out.print("Введите новый пароль клиента: ");
        String password = scanner.nextLine();

        String sql = "UPDATE clients SET username = ?, password = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setInt(3, clientId);
        int updatedRows = pstmt.executeUpdate();

        if (updatedRows > 0) {
            System.out.println("Клиент успешно отредактирован");
        } else {
            System.out.println("Клиент с указанным ID не найден");
        }
    }

    public static void changeOrderStatus(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Введите ID заказа для изменения статуса: ");
        int orderId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Введите новый статус заказа (1 - выполнен, 0 - не выполнен): ");
        boolean isCompleted = scanner.nextInt() == 1;

        String sql = "UPDATE orders SET is_completed = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setBoolean(1, isCompleted);
        pstmt.setInt(2, orderId);
        int updatedRows = pstmt.executeUpdate();

        if (updatedRows > 0) {
            System.out.println("Статус заказа успешно изменен");
        } else {
            System.out.println("Заказ с указанным ID не найден");
        }
    }
}
