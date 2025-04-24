package lendpackage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class LendLogService {
		public static void addBorrower(int userId,Scanner scanner) {
			System.out.println("enetr borrower name");
			String name=scanner.nextLine();
			
			String sql="insert into borrowers(user_id,name) values(?,?)";
			try (Connection conn = DBConnection.getConnection();
		             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	
				pstmt.setInt(1, userId);
				pstmt.setString(2, name);
				
				pstmt.executeUpdate();
				System.out.println("Borrower added");
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
//===========================================================================================================================================
//===========================================================================================================================================
		
		
		public static void lendItem(int userId,Scanner scanner) {
			
			System.out.println("enter borrower name");
	        String borrowerName = scanner.nextLine();
	        
	        System.out.println("enter item name");
	        String itemName = scanner.nextLine();
	        
	        System.out.println("enter due date (YYYY-MM-DD)");
	        String dueDate=scanner.nextLine();
	        
	        String sql1="select borrower_id from borrowers where name=? and user_id=?";
	        String sql2="insert into borrowed_items(item_name, borrower_id, due_date) values(?,?,?)";
	        
	        try (Connection conn = DBConnection.getConnection();
	                PreparedStatement stmt1 = conn.prepareStatement(sql1);
	                PreparedStatement stmt2 = conn.prepareStatement(sql2)) {

	               stmt1.setInt(2, userId);
	               stmt1.setString(1, borrowerName);
	               ResultSet rs = stmt1.executeQuery();

	               if (rs.next()) {
	                   int borrowerId = rs.getInt("borrower_id");

	                   stmt2.setString(1, itemName);
	                   stmt2.setInt(2, borrowerId);
	                   stmt2.setDate(3, Date.valueOf(dueDate));
	                   stmt2.executeUpdate();

	                   System.out.println("✅ Item lent successfully.");

	               } else {
	                   System.out.println("❌ Borrower not found.");
	               }

	           } catch (SQLException e) {
	               e.printStackTrace();
	           }
            

		}
		
		//===========================================================================================================================================
		//===========================================================================================================================================
		
		public static void alert(int userId) {
			
			 boolean hasAlerts = false;
			 System.out.println("Alert");
			
			String ale="SELECT borrower_id,item_name,DATEDIFF(due_date, date_borrowed) AS days_until_due ,DATEDIFF(due_date, date_returned) AS returnedDate FROM borrowed_items";
			String name="select user_id,name from borrowers where borrower_id=?";
			
			try (Connection conn = DBConnection.getConnection();
	                PreparedStatement stmt1 = conn.prepareStatement(ale);
	                PreparedStatement stmt2 = conn.prepareStatement(name)){
				 
			ResultSet rs=stmt1.executeQuery();
			
			while(rs.next()) {
				int borrowerID=rs.getInt("borrower_id");
				String item_name=rs.getString("item_name");
				int dateDue=rs.getInt("days_until_due");
				int returnedDate=rs.getInt("returnedDate");
				
				stmt2.setInt(1, borrowerID);
				
				ResultSet rs1=stmt2.executeQuery();
				
				String borrower_name="unknown";
				int  borrowerUser=-1;
				
				if(rs1.next()) {
					borrowerUser=rs1.getInt("user_id");
					borrower_name=rs1.getString("name");
				}
				
				if(borrowerUser==userId) {
				if(returnedDate<=0) {
				if(dateDue<=2) {
					
					System.out.println(borrower_name +" has to return " + item_name +"in " + dateDue+ " daya");
				    hasAlerts=true;
				}
				}}
				
				rs1.close();
				
				
				
			}
			rs.close();
				if(!hasAlerts) {
					System.out.println("no alerts! enjoy pandagooo");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		
		//===========================================================================================================================================
		//===========================================================================================================================================
		
		

		public static void viewItems(int userId) {
		    boolean hasAlerts = false;
			
			
			String ale="SELECT borrower_id,item_name,DATEDIFF(due_date, date_borrowed) AS days_until_due ,DATEDIFF(due_date, date_returned) AS returnedDate FROM borrowed_items";
			
			String name="select user_id,name from borrowers where borrower_id=?";
			
			try (Connection conn = DBConnection.getConnection();
	                PreparedStatement stmt1 = conn.prepareStatement(ale);
	                PreparedStatement stmt2 = conn.prepareStatement(name)){
				 
			ResultSet rs=stmt1.executeQuery();
			
			while(rs.next()) {
				int borrowerID=rs.getInt("borrower_id");
				String item_name=rs.getString("item_name");
				int dateDue=rs.getInt("days_until_due");
				int returnedDate=rs.getInt("returnedDate");
				
				stmt2.setInt(1, borrowerID);
				
				ResultSet rs1=stmt2.executeQuery();
				
				String borrower_name="unknown";
				int  borrowerUser=-1;
				
				if(rs1.next()) {
					borrowerUser=rs1.getInt("user_id");
					borrower_name=rs1.getString("name");
				}
				
				if(returnedDate<=0) {
				if(borrowerUser==userId) {
				
					
					System.out.println(borrower_name +" has to return " + item_name +"in " + dateDue+ " daya");
				    hasAlerts=true;
				}
				}
				
				
				rs1.close();
				
				
				
			}
			rs.close();
				if(!hasAlerts) {
					System.out.println("no alerts! enjoy pandagooo");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}

		
//===========================================================================================================================================
//===========================================================================================================================================		


		public static void returnItem(Scanner scanner) {
			 System.out.print("Enter item name to return: ");
		        String itemName = scanner.nextLine();

		        String updateReturn = """
		            UPDATE borrowed_items 
		            SET date_returned = CURDATE() 
		            WHERE item_name = ? AND date_returned IS NULL
		        """;
		        
		        try (Connection conn = DBConnection.getConnection();
		                PreparedStatement stmt = conn.prepareStatement(updateReturn)) {

		               stmt.setString(1, itemName);
		               int updated = stmt.executeUpdate();

		               if (updated > 0) {
		                   System.out.println("✅ Item marked as returned.");
		               } else {
		                   System.out.println("❌ No matching unreturned item found.");
		               }
		               
		        } catch (SQLException e) {
		            e.printStackTrace();
		        } 
			
		}

		
//===========================================================================================================================================
//===========================================================================================================================================

		public static void profile(int userId,Scanner scanner) {
		
			String sql="select username,email from users where user_id=?";
			String sql1="select name from borrowers where user_id=?";
			
			try(Connection conn = DBConnection.getConnection();
	                PreparedStatement stmt = conn.prepareStatement(sql);
					PreparedStatement stmt1 = conn.prepareStatement(sql1)) {
				
				stmt.setInt(1, userId);
				ResultSet rs=stmt.executeQuery();
				String name= "unknown";
				String email = "unknown";
				if(rs.next()) {
					name=rs.getString("username");
					email=rs.getString("email");
					
				}
			    System.out.println("name : " + name);
			    System.out.println("email : "+ email);
				System.out.println("Borrowers:");
				stmt1.setInt(1, userId);;
				ResultSet rs1=stmt1.executeQuery();
				
				while(rs1.next()) {
					String Bname=rs1.getString("name");
					
					System.out.println(Bname);
				}
				
			System.out.println();
			
			
				settings(userId,scanner);
				
				
			} catch (Exception e) {
				System.out.println("SERVER ERROR!");
				System.out.println("TRY AFTER SOME TIME");
				e.printStackTrace();
			}
			
			
		}
		

		private static void settings(int userId,Scanner sc){
			System.out.println("1 : settings");
			int a = sc.nextInt();
			sc.nextLine();
			int  b=0;
			if(a==1) {
				System.out.println("1 : password change" + "\n 2 : email change" + "\n 3 : Delete Account" );
				b=sc.nextInt();
			}
			
			sc.nextLine();
			
			switch(b) {
			case 1: 
				 String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";

				    try (Connection conn = DBConnection.getConnection();
				         PreparedStatement stmt = conn.prepareStatement(sql)) {
				    	
				    	System.out.println("enter new password");
				    	String newPassword=sc.nextLine();
				    	
				        stmt.setString(1, newPassword); 
				        stmt.setInt(2, userId);

				        int rowsUpdated = stmt.executeUpdate();
				        if (rowsUpdated > 0) {
				            System.out.println("Password updated successfully.");
				        } else {
				            System.out.println("User not found.");
				        }

				    } catch (Exception e) {
				        System.out.println("Error updating password.");
				        e.printStackTrace();
				    }
				    break;
				    
				    
				    
			case 2:
				
				String sql1 = "UPDATE users SET email = ? WHERE user_id = ?";

			    try (Connection conn = DBConnection.getConnection();
			         PreparedStatement stmt = conn.prepareStatement(sql1)) {
			    	
			    	System.out.println("enter new email");
			    	String email=sc.nextLine();
			    	
			        stmt.setString(1, email); 
			        stmt.setInt(2, userId);

			        int rowsUpdated = stmt.executeUpdate();
			        if (rowsUpdated > 0) {
			            System.out.println("email updated successfully.");
			        } else {
			            System.out.println("User not found.");
			        }

			    } catch (Exception e) {
			        System.out.println("Error updating password.");
			        e.printStackTrace();
			    }
			    break;
			    
			    
			    
			    
			case 3:
				String borrowerId="select borrower_id from borrowers where user_id=?";
				String deleteborrower="DELETE FROM borrowers WHERE user_id = ?";
				String deleteitems="DELETE FROM borrowed_items WHERE borrower_id = ?";
				String deleteuser = "DELETE FROM users WHERE user_id = ?;";

			    try (Connection conn = DBConnection.getConnection();
			         PreparedStatement stmt = conn.prepareStatement(deleteuser);
			    		PreparedStatement stmt1 = conn.prepareStatement(deleteborrower);
			    				PreparedStatement stmt2 = conn.prepareStatement(borrowerId);
			    					PreparedStatement stmt3 = conn.prepareStatement(deleteitems)) {
			    	
			    	
			    	
			        
			    	stmt2.setInt(1, userId);
			    	ResultSet rs2=stmt2.executeQuery();
			    	while(rs2.next()) {
			    		int borrowerid=rs2.getInt("borrower_id");
			    		
			    		stmt3.setInt(1,borrowerid );
			    		stmt3.executeUpdate();
			    		
			    	}
			    	
			    	
			    	
			        stmt1.setInt(1, userId);
                    stmt1.executeUpdate();
                    
                    stmt.setInt(1, userId);
			        int rowsUpdated = stmt.executeUpdate();
			        if (rowsUpdated > 0) {
			            System.out.println("account deleted successfully.");
			        } else {
			            System.out.println("User not found.");
			        }
			        
			        lendpackage.MainApp.main(null);
			break;
				
			        	

			    } catch (Exception e) {
			        System.out.println("Error updating password.");
			        e.printStackTrace();
			    }
			}
			
			
			
	
	
} 
		
		
		
//===========================================================================================================================================
//===========================================================================================================================================
		
		
}
