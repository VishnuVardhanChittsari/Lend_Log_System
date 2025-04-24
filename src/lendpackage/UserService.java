package lendpackage;

import java.sql.*;
import java.util.Scanner;

public class UserService {
	
	  static Scanner scanner=new Scanner(System.in);
	  public static int registerUser() {
	       
	        System.out.print("Create a username: ");
	        String username = scanner.nextLine();
	        
	        System.out.print("ENTER EMAIL: ");
	        String email = scanner.nextLine();

	        System.out.print("Create a password: ");
	        String password = scanner.nextLine();
	        
	        

	        String checkUserSql = "SELECT * FROM users WHERE username = ?";
	        String insertUserSql = "INSERT INTO users (username, password_hash,email) VALUES (?, ?, ?)";

	        
	        
	        try(Connection conn=DBConnection.getConnection();
	        		PreparedStatement cpstmt=conn.prepareStatement(checkUserSql);
	        		PreparedStatement ipstmt=conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
	        	
	        	
	        	cpstmt.setString(1, username);
	        	ResultSet rs=cpstmt.executeQuery();
	        	while(rs.next()) {
	        		System.out.println("user name already exist");
	        		return 0;
	        	}
	        	
	        	ipstmt.setString(1, username);
	        	ipstmt.setString(2,password);
	        	ipstmt.setString(3, email);
	        	
	        	ipstmt.executeUpdate();
	        	
	        	ResultSet generatedKeys = ipstmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                int userId = generatedKeys.getInt(1);
	                System.out.println("✅ Registration successful! Your user ID is: " + userId);
	                return userId;
				
			} }catch (Exception e) {
				 System.out.println("❌ Database error during registration.");
			        e.printStackTrace();			
			        }
	        return -1;
}
	  
	  
	  public static int loginUser() {
		  System.out.println("enetr user name");
		  String name=scanner.nextLine();
		  System.out.println("enetr password");
		  String password=scanner.nextLine();
		  
		  String passwordCheck="select user_id from users where username=?and password_hash=?";
		  try(Connection conn=DBConnection.getConnection();
				  PreparedStatement pstmt=conn.prepareStatement(passwordCheck)) {
			  
			  
			  pstmt.setString(1, name);
			  pstmt.setString(2, password);
			  ResultSet rs=pstmt.executeQuery();
			  
			  if (rs.next()) {
				  int userId=rs.getInt("user_id");
				  System.out.println("Welcome back, "+ name);
				  return userId;
				
			}
			  else {
				System.out.println("Invalid credentials. Try again.");
				return -1;
			}
			
		} catch (Exception e) {
			System.out.println("Login failed due to a system error");
			e.printStackTrace();
			return -1;
		}
				 

	  }
	 
} 

