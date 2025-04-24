package lendpackage;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static final String URL = "jdbc:mysql://localhost:3306/lendlog";
    private static final String USER = "root";
    private static final String PASS = "Vishnu@2025";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    
    public static void initializeDatabase() {
    	 try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()){
    		 
    		  String createUsers = """
    	                CREATE TABLE IF NOT EXISTS users (
    	                    user_id INT AUTO_INCREMENT PRIMARY KEY,
    	                    username VARCHAR(50) NOT NULL UNIQUE,
    	                    password_hash VARCHAR(255) NOT NULL,
    	                    email VARCHAR(100),
    	                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    	                );
    	            """;
    		  
    		  
    		  String createBorrowers = """
    	                CREATE TABLE IF NOT EXISTS borrowers (
    	                    borrower_id INT AUTO_INCREMENT PRIMARY KEY,
    	                    user_id INT NOT NULL,
    	                    name VARCHAR(100) NOT NULL,
    	                    total_borrowed INT DEFAULT 0,
    	                    total_returned INT DEFAULT 0,
    	                    FOREIGN KEY (user_id) REFERENCES users(user_id)
    	                );
    	            """;

    	            String createItems = """
    	                CREATE TABLE IF NOT EXISTS borrowed_items (
    	                    item_id INT AUTO_INCREMENT PRIMARY KEY,
    	                    item_name VARCHAR(255) NOT NULL,
    	                    borrower_id INT NOT NULL,
    	                    date_borrowed DATETIME DEFAULT CURRENT_TIMESTAMP,
    	                    due_date DATE,
    	                    date_returned DATE DEFAULT NULL,
    	                    FOREIGN KEY (borrower_id) REFERENCES borrowers(borrower_id)
    	                );
    	            """;
    	            
    	            
    	            stmt.execute(createUsers);
    	            stmt.execute(createBorrowers);
    	            stmt.execute(createItems);   
//    	            System.out.println("✅ Tables created or already exist.");

    	            
    		 
    	 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("❌ Error creating tables.");

		}
    }
	
}
