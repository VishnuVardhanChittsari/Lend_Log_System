package lendpackage;


import java.util.Scanner;

public class MainApp {
	public static void main(String[] args) {
        DBConnection.initializeDatabase();
        
        Scanner scanner = new Scanner(System.in);
        int userId=0;
        
        

        while (userId <= 0) {
            System.out.println("1. Login\n2. Register\n3. Exit");
            switch (scanner.nextLine()) {
                case "1" : userId = UserService.loginUser();
                			break;
                
                case "2" : userId = UserService.registerUser();
                			
                			break;
                case "3" : System.exit(0);
                
                default : System.out.println("Invalid choice.");
                userId=0;
            }
        }
        
        
        
        
        LendLogService.alert(userId);
        
        
        
        while (true) {
        	
            System.out.println("\n=== LendLog Menu ===");
            System.out.println("1. profile");
            System.out.println("2. Add Borrower");
            System.out.println("3. Lend Item");
            System.out.println("4. Return Item");
            System.out.println("5. View All Items");
            System.out.println("6. Logout");
            switch (scanner.nextLine()) {
                case "1" -> LendLogService.profile(userId,scanner);
                case "2" -> LendLogService.addBorrower(userId, scanner);
                case "3" -> LendLogService.lendItem(userId, scanner);
                case "4" -> LendLogService.returnItem(scanner);
                case "5" -> LendLogService.viewItems(userId);
                case "6" -> {
                    System.out.println("Logged out.");
                    
                    main(null);
                }
                default -> System.out.println("Invalid choice.");
            }
            
        }

	}


	
}
        


