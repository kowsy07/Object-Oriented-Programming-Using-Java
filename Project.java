class Database { 
    static final String URL = 
        "jdbc:mysql://localhost:3306/online_shopping"; 
    static final String USER = import java.sql.*; 
import java.util.*; 
  
"root"; 
  
    // Change this to your MySQL password 
    static final String PASSWORD = "your_password"; 
  
    static Connection connect() throws SQLException { 
        return DriverManager.getConnection(URL, USER, PASSWORD); 
    } 
  
    static void initialize() throws SQLException { 
        try (Connection con = connect(); 
             Statement st = con.createStatement()) { 
  
            st.executeUpdate( 
                "CREATE TABLE IF NOT EXISTS users (" + 
                "id INT AUTO_INCREMENT PRIMARY KEY," + 
                "name VARCHAR(100) NOT NULL," + 
                "email VARCHAR(100) UNIQUE NOT NULL," + 
                "password VARCHAR(100) NOT NULL)" 
            ); 
  
            st.executeUpdate( 
                "CREATE TABLE IF NOT EXISTS products (" + 
                "id INT AUTO_INCREMENT PRIMARY KEY," + 
                "name VARCHAR(100) NOT NULL," + 
                "category VARCHAR(50) NOT NULL," + 
                "price DECIMAL(10,2) NOT NULL," + 
                "stock INT NOT NULL)" 
            ); 
  
            st.executeUpdate( 
                "CREATE TABLE IF NOT EXISTS cart (" + 
                "id INT AUTO_INCREMENT PRIMARY KEY," + 
                "user_id INT NOT NULL," + 
                "product_id INT NOT NULL," + 
                "quantity INT NOT NULL," + 
                "FOREIGN KEY (user_id) REFERENCES users(id)," + 
                "FOREIGN KEY (product_id) REFERENCES products(id))" 
            ); 
  
            st.executeUpdate( 
                "CREATE TABLE IF NOT EXISTS orders (" + 
                "id INT AUTO_INCREMENT PRIMARY KEY," + 
                "user_id INT NOT NULL," + 
                "total DECIMAL(10,2) NOT NULL," + 
                "status VARCHAR(30) NOT NULL," + 
                "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," + 
                "FOREIGN KEY (user_id) REFERENCES users(id))" 
            ); 
  
            st.executeUpdate( 
                "CREATE TABLE IF NOT EXISTS order_items (" + 
                "id INT AUTO_INCREMENT PRIMARY KEY," + 
                "order_id INT NOT NULL," + 
                "product_id INT NOT NULL," + 
                "quantity INT NOT NULL," + 
                "price DECIMAL(10,2) NOT NULL," + 
                "FOREIGN KEY (order_id) REFERENCES orders(id)," + 
                "FOREIGN KEY (product_id) REFERENCES products(id))" 
            ); 
  
            st.executeUpdate( 
                "CREATE TABLE IF NOT EXISTS ai_previews (" + 
                "id INT AUTO_INCREMENT PRIMARY KEY," + 
                "user_id INT NOT NULL," + 
                "product_id INT NOT NULL," + 
                "preview_type VARCHAR(50) NOT NULL," + 
                "preview_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," + 
                "FOREIGN KEY (user_id) REFERENCES users(id)," + 
                "FOREIGN KEY (product_id) REFERENCES products(id))" 
            ); 
  
            ResultSet rs = st.executeQuery( 
                "SELECT COUNT(*) FROM products" 
            ); 
            rs.next(); 
  
            if (rs.getInt(1) == 0) { 
                st.executeUpdate( 
                    "INSERT INTO products(name,category,price,stock) VALUES" + 
                    "('Summer Dress','Dress',1200.00,10)," + 
                    "('Lipstick','Cosmetics',350.00,20)," + 
                    "('Face Cream','Cosmetics',500.00,15)," + 
                    "('Sofa','Home Decor',15000.00,5)," + 
                    "('Curtains','Home Decor',1800.00,12)" 
                ); 
            } 
        } 
    } 
} 
  
class Product { 
    int id; 
    String name, category; 
    double price; 
    int stock; 
  
    Product(int id, String name, String category, 
            double price, int stock) { 
        this.id = id; 
        this.name = name; 
        this.category = category; 
        this.price = price; 
        this.stock = stock; 
    } 
  
    void display() { 
        System.out.printf( 
            "%-4d %-18s %-14s Rs.%-10.2f %d%n", 
            id, name, category, price, stock 
        ); 
    } 
} 
  
public class OnlineShoppingSystem { 
    static Scanner sc = new Scanner(System.in); 
    static int userId; 
  
    static int readInt(String message) { 
        while (true) { 
            System.out.print(message); 
            try { 
                return Integer.parseInt(sc.nextLine().trim()); 
            } catch (NumberFormatException e) { 
                System.out.println("Enter a valid number."); 
            } 
        } 
    } 
  
    static String read(String message) { 
        System.out.print(message); 
        return sc.nextLine().trim(); 
    } 
  
    static boolean register() throws SQLException { 
        String name = read("Enter name: "); 
        String email = read("Enter email: "); 
        String password = read("Enter password: "); 
  
        String sql = 
            "INSERT INTO users(name,email,password) VALUES(?,?,?)"; 
  
        try (Connection con = Database.connect(); 
             PreparedStatement ps = con.prepareStatement( 
                 sql, Statement.RETURN_GENERATED_KEYS)) { 
  
            ps.setString(1, name); 
            ps.setString(2, email); 
            ps.setString(3, password); 
            ps.executeUpdate(); 
  
            try (ResultSet keys = ps.getGeneratedKeys()) { 
                if (keys.next()) { 
                    userId = keys.getInt(1); 
                } 
            } 
  
            System.out.println("Registration successful!"); 
            return true; 
        } catch (SQLIntegrityConstraintViolationException e) { 
            System.out.println("Email already exists."); 
            return false; 
        } 
    } 
  
    static boolean login() throws SQLException { 
        String email = read("Enter email: "); 
        String password = read("Enter password: "); 
  
        String sql = 
            "SELECT id FROM users WHERE email=? AND password=?"; 
  
        try (Connection con = Database.connect(); 
             PreparedStatement ps = con.prepareStatement(sql)) { 
  
            ps.setString(1, email); 
            ps.setString(2, password); 
  
            try (ResultSet rs = ps.executeQuery()) { 
                if (rs.next()) { 
                    userId = rs.getInt("id"); 
                    System.out.println("Login successful!"); 
                    return true; 
                } 
            } 
        } 
  
        System.out.println("Invalid email or password."); 
        return false; 
    } 
  
    static void showProducts() throws SQLException { 
        String sql = "SELECT * FROM products"; 
  
        try (Connection con = Database.connect(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) { 
  
            System.out.println("\nID   Product            Category       Price       Stock"); 
            System.out.println("----------------------------------------------------------"); 
  
            while (rs.next()) { 
                new Product( 
                    rs.getInt("id"), 
                    rs.getString("name"), 
                    rs.getString("category"), 
                    rs.getDouble("price"), 
                    rs.getInt("stock") 
                ).display(); 
            } 
        } 
    } 
  
    static void searchProduct() throws SQLException { 
        String keyword = read("Enter product name/category: "); 
  
        String sql = 
            "SELECT * FROM products " + 
            "WHERE name LIKE ? OR category LIKE ?"; 
  
        try (Connection con = Database.connect(); 
             PreparedStatement ps = con.prepareStatement(sql)) { 
  
            ps.setString(1, "%" + keyword + "%"); 
            ps.setString(2, "%" + keyword + "%"); 
  
            try (ResultSet rs = ps.executeQuery()) { 
                boolean found = false; 
  
                while (rs.next()) { 
                    found = true; 
                    new Product( 
                        rs.getInt("id"), 
                        rs.getString("name"), 
                        rs.getString("category"), 
                        rs.getDouble("price"), 
                        rs.getInt("stock") 
                    ).display(); 
                } 
  
                if (!found) { 
                    System.out.println("No products found."); 
                } 
            } 
        } 
    } 
  
    static void previewProduct() throws SQLException { 
        showProducts(); 
        int productId = readInt("Enter product ID to preview: "); 
  
        try (Connection con = Database.connect()) { 
            String sql = "SELECT * FROM products WHERE id=?"; 
  
            try (PreparedStatement ps = con.prepareStatement(sql)) { 
                ps.setInt(1, productId); 
  
                try (ResultSet rs = ps.executeQuery()) { 
                    if (!rs.next()) { 
                        System.out.println("Product not found."); 
                        return; 
                    } 
  
                    String category = rs.getString("category"); 
                    String type; 
  
                    if (category.equalsIgnoreCase("Dress")) { 
                        type = "Virtual dress try-on"; 
                    } else if (category.equalsIgnoreCase("Cosmetics")) { 
                        type = "Cosmetic face preview"; 
                    } else { 
                        type = "Home decor visualization"; 
                    } 
  
                    String sql2 = 
                        "INSERT INTO ai_previews" + 
                        "(user_id,product_id,preview_type) VALUES(?,?,?)"; 
  
                    try (PreparedStatement ps2 = 
                             con.prepareStatement(sql2)) { 
                        ps2.setInt(1, userId); 
                        ps2.setInt(2, productId); 
                        ps2.setString(3, type); 
                        ps2.executeUpdate(); 
                    } 
  
                    System.out.println("\nAI Preview Module"); 
                    System.out.println("Product: " + rs.getString("name")); 
                    System.out.println("Feature: " + type); 
                    System.out.println( 
                        "Preview request saved successfully." 
                    ); 
                    System.out.println( 
                        "Note: Actual photo-based AI rendering " + 
                        "requires an external AI model or service." 
                    ); 
                } 
            } 
        } 
    } 
  
    static void addToCart() throws SQLException { 
        showProducts(); 
        int productId = readInt("Enter product ID: "); 
        int quantity = readInt("Enter quantity: "); 
  
        if (quantity <= 0) { 
            System.out.println("Quantity must be greater than zero."); 
            return; 
        } 
  
        try (Connection con = Database.connect()) { 
            con.setAutoCommit(false); 
  
            try { 
                int stock; 
                double price; 
  
                String sql = 
                    "SELECT price,stock FROM products WHERE id=? FOR UPDATE"; 
  
                try (PreparedStatement ps = con.prepareStatement(sql)) { 
                    ps.setInt(1, productId); 
  
                    try (ResultSet rs = ps.executeQuery()) { 
                        if (!rs.next()) { 
                            System.out.println("Product not found."); 
                            con.rollback(); 
                            return; 
                        } 
  
                        price = rs.getDouble("price"); 
                        stock = rs.getInt("stock"); 
                    } 
                } 
  
                if (quantity > stock) { 
                    System.out.println("Insufficient stock."); 
                    con.rollback(); 
                    return; 
                } 
  
                String insert = 
                    "INSERT INTO cart(user_id,product_id,quantity) " + 
                    "VALUES(?,?,?)"; 
  
                try (PreparedStatement ps = con.prepareStatement(insert)) { 
                    ps.setInt(1, userId); 
                    ps.setInt(2, productId); 
                    ps.setInt(3, quantity); 
                    ps.executeUpdate(); 
                } 
  
                con.commit(); 
                System.out.printf( 
                    "Added to cart. Subtotal: Rs.%.2f%n", 
                    price * quantity 
                ); 
            } catch (SQLException e) { 
                con.rollback(); 
                throw e; 
            } finally { 
                con.setAutoCommit(true); 
            } 
        } 
    } 
  
    static void viewCart() throws SQLException { 
        String sql = 
            "SELECT c.product_id,p.name,p.price,c.quantity " + 
            "FROM cart c JOIN products p ON c.product_id=p.id " + 
            "WHERE c.user_id=?"; 
  
        double total = 0; 
  
        try (Connection con = Database.connect(); 
             PreparedStatement ps = con.prepareStatement(sql)) { 
  
            ps.setInt(1, userId); 
  
            try (ResultSet rs = ps.executeQuery()) { 
                System.out.println("\nYour Shopping Cart"); 
  
                while (rs.next()) { 
                    double subtotal = 
                        rs.getDouble("price") * rs.getInt("quantity"); 
                    total += subtotal; 
  
                    System.out.printf( 
                        "%s x %d = Rs.%.2f%n", 
                        rs.getString("name"), 
                        rs.getInt("quantity"), 
                        subtotal 
                    ); 
                } 
            } 
        } 
  
        System.out.printf("Total: Rs.%.2f%n", total); 
    } 
  
    static void placeOrder() throws SQLException { 
        try (Connection con = Database.connect()) { 
            con.setAutoCommit(false); 
  
            try { 
                String cartSql = 
                    "SELECT c.product_id,c.quantity,p.price,p.stock,p.name " + 
                    "FROM cart c JOIN products p ON c.product_id=p.id " + 
                    "WHERE c.user_id=? FOR UPDATE"; 
  
                double total = 0; 
  
                try (PreparedStatement ps = 
                         con.prepareStatement(cartSql)) { 
                    ps.setInt(1, userId); 
  
                    try (ResultSet rs = ps.executeQuery()) { 
                        while (rs.next()) { 
                            int qty = rs.getInt("quantity"); 
                            int stock = rs.getInt("stock"); 
  
                            if (qty > stock) { 
                                throw new SQLException( 
                                    "Insufficient stock for " + 
                                    rs.getString("name") 
                                ); 
                            } 
  
                            total += rs.getDouble("price") * qty; 
                        } 
                    } 
                } 
  
                if (total == 0) { 
                    System.out.println("Your cart is empty."); 
                    con.rollback(); 
                    return; 
                } 
  
                int orderId; 
  
                String orderSql = 
                    "INSERT INTO orders(user_id,total,status) " + 
                    "VALUES(?,?,?)"; 
  
                try (PreparedStatement ps = con.prepareStatement( 
                         orderSql, Statement.RETURN_GENERATED_KEYS)) { 
                    ps.setInt(1, userId); 
                    ps.setDouble(2, total); 
                    ps.setString(3, "Placed"); 
                    ps.executeUpdate(); 
  
                    try (ResultSet keys = ps.getGeneratedKeys()) { 
                        keys.next(); 
                        orderId = keys.getInt(1); 
                    } 
                } 
  
                String itemSql = 
                    "INSERT INTO order_items" + 
                    "(order_id,product_id,quantity,price) VALUES(?,?,?,?)"; 
  
                String stockSql = 
                    "UPDATE products SET stock=stock-? WHERE id=?"; 
  
                try (PreparedStatement psItem = 
                         con.prepareStatement(itemSql); 
                     PreparedStatement psStock = 
                         con.prepareStatement(stockSql); 
                     PreparedStatement psCart = 
                         con.prepareStatement( 
                             "SELECT product_id,quantity FROM cart " + 
                             "WHERE user_id=?")) { 
  
                    psCart.setInt(1, userId); 
  
                    try (ResultSet rs = psCart.executeQuery()) { 
                        while (rs.next()) { 
                            int pid = rs.getInt("product_id"); 
                            int qty = rs.getInt("quantity"); 
  
                            double price; 
                            try (PreparedStatement p = 
                                     con.prepareStatement( 
                                         "SELECT price FROM products WHERE id=?")) { 
                                p.setInt(1, pid); 
                                try (ResultSet r = p.executeQuery()) { 
                                    r.next(); 
                                    price = r.getDouble("price"); 
                                } 
                            } 
  
                            psItem.setInt(1, orderId); 
                            psItem.setInt(2, pid); 
                            psItem.setInt(3, qty); 
                            psItem.setDouble(4, price); 
                            psItem.executeUpdate(); 
  
                            psStock.setInt(1, qty); 
                            psStock.setInt(2, pid); 
                            psStock.executeUpdate(); 
                        } 
                    } 
                } 
  
                try (PreparedStatement ps = 
                         con.prepareStatement( 
                             "DELETE FROM cart WHERE user_id=?")) { 
                    ps.setInt(1, userId); 
                    ps.executeUpdate(); 
                } 
  
                con.commit(); 
                System.out.println("Order placed successfully!"); 
                System.out.println("Order ID: " + orderId); 
                System.out.printf("Total bill: Rs.%.2f%n", total); 
                System.out.println("Order status: Placed"); 
  
            } catch (SQLException e) { 
                con.rollback(); 
                System.out.println("Order failed: " + e.getMessage()); 
            } finally { 
                con.setAutoCommit(true); 
            } 
        } 
    } 
  
    public static void main(String[] args) { 
        try { 
            Database.initialize(); 
            System.out.println("=== AI ONLINE SHOPPING SYSTEM ==="); 
  
            boolean authenticated = false; 
  
            while (!authenticated) { 
                System.out.println("\n1. Register"); 
                System.out.println("2. Login"); 
                System.out.println("0. Exit"); 
  
                int choice = readInt("Choose: "); 
  
                try { 
                    if (choice == 1) { 
                        authenticated = register(); 
                    } else if (choice == 2) { 
                        authenticated = login(); 
                    } else if (choice == 0) { 
                        System.out.println("Thank you!"); 
                        return; 
                    } else { 
                        System.out.println("Invalid choice."); 
                    } 
                } catch (SQLException e) { 
                    System.out.println("Database operation failed: " + 
                                       e.getMessage()); 
                } 
            } 
  
            while (true) { 
                System.out.println("\n=== SHOPPING MENU ==="); 
                System.out.println("1. View products"); 
                System.out.println("2. Search products"); 
                System.out.println("3. AI product preview"); 
                System.out.println("4. Add to cart"); 
                System.out.println("5. View cart"); 
                System.out.println("6. Place order"); 
                System.out.println("0. Exit"); 
  
                int choice = readInt("Choose: "); 
  
                if (choice == 0) { 
                    System.out.println("Thank you for shopping!"); 
                    break; 
                } 
  
                try { 
                    switch (choice) { 
                        case 1: showProducts(); break; 
                        case 2: searchProduct(); break; 
                        case 3: previewProduct(); break; 
                        case 4: addToCart(); break; 
                        case 5: viewCart(); break; 
                        case 6: placeOrder(); break; 
                        default: System.out.println("Invalid choice."); 
                    } 
                } catch (SQLException e) { 
                    System.out.println("Operation failed: " + 
                                       e.getMessage()); 
                } 
            } 
  
        } catch (SQLException e) { 
            System.out.println("Cannot connect to MySQL: " + 
                               e.getMessage()); 
            System.out.println( 
                "Check MySQL is running, the database exists, " + 
                "and your username/password are correct." 
            ); 
        } 
    } 
} 
