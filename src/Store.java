import java.io.*;
import java.util.*;

public class Store {
    // รายการสินค้าในสต็อก
    // List of items in stock
    private ArrayList<Item> stockList = new ArrayList<>();

    // รายชื่อลูกค้า
    // List of customers
    private ArrayList<Customer> customerList = new ArrayList<>();

    // ใช้สำหรับรับข้อมูลจากผู้ใช้
    // Scanner for user input
    private Scanner scanner = new Scanner(System.in);

    // ฟังก์ชันสำหรับรับค่าตัวเลขที่ถูกต้องในช่วงที่กำหนด
    // Function to get a validated integer input within a specified range
    private int getValidatedIntegerInput(int min, int max, String context) {
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            try {
                // ตรวจสอบว่าค่าที่ป้อนอยู่ในช่วงของ Integer หรือไม่
                // Check if the input is within the range of Integer
                java.math.BigInteger bigValue = new java.math.BigInteger(input);
                java.math.BigInteger intMax = java.math.BigInteger.valueOf(Integer.MAX_VALUE);
                java.math.BigInteger intMin = java.math.BigInteger.valueOf(Integer.MIN_VALUE);

                if (bigValue.compareTo(intMax) > 0 || bigValue.compareTo(intMin) < 0) {
                    System.out.println("Invalid input! Please enter a number between " + min + " and " + max + ".");
                    continue;
                }

                int value = Integer.parseInt(input);

                // ตรวจสอบว่าค่าที่ป้อนอยู่ในช่วงที่กำหนดหรือไม่
                // Check if the input is within the specified range
                if (value < min || value > max) {
                    System.out.println("Invalid input! Please enter a number between " + min + " and " + max + ".");
                    continue;
                }

                return value;

            } catch (NumberFormatException e) {
                // แจ้งเตือนเมื่อป้อนค่าที่ไม่ใช่ตัวเลข
                // Notify when the input is not a valid number
                System.out.println("Invalid input! Please enter a number between " + min + " and " + max + ".");
            }
        }
    }

    // ฟังก์ชันสำหรับโหลดข้อมูลสินค้าในสต็อกจากไฟล์
    // Function to load stock data from a file
    public void loadStock() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/stock.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // แยกข้อมูลสินค้าแต่ละรายการ
                // Split each line into item details
                String[] parts = line.split(",");
                String type = parts[0];
                String name = parts[1];
                int quantity = Integer.parseInt(parts[2]);

                // เพิ่มสินค้าใน stockList ตามประเภท
                // Add items to stockList based on their type
                if (type.equals("Meat")) {
                    stockList.add(new Meat(name, quantity));
                } else if (type.equals("Drink")) {
                    stockList.add(new Drink(name, quantity));
                } else {
                    stockList.add(new Vegetable(name, quantity));
                }
            }
        } catch (IOException e) {
            // แจ้งเตือนเมื่อเกิดข้อผิดพลาดในการอ่านไฟล์
            // Notify when there is an error reading the file
            System.out.println("Error reading stock file.");
        }
    }

    // ฟังก์ชันหลักสำหรับรันโปรแกรม
    // Main function to run the program
    public void run() {
        System.out.println("Welcome to Ma La Skewer Store!");
        while (true) {
            // แสดงเมนูหลัก
            // Display the main menu
            System.out.println("\nMain Menu:");
            System.out.println("1. New Customer");
            System.out.println("2. Show Customer Count");
            System.out.println("3. Check Stock");
            System.out.println("0. Exit");
            System.out.print("Choose option (0–3): ");
            int menu = getValidatedIntegerInput(0, 3, "menu");

            switch (menu) {
                case 1:
                    // เพิ่มลูกค้าใหม่
                    // Add a new customer
                    System.out.print("Enter customer name (or type 'exit'): ");
                    String name = scanner.nextLine().trim();
                    if (name.equalsIgnoreCase("exit")) break;
                    if (name.isEmpty()) {
                        System.out.println("Name cannot be empty.");
                        break;
                    }

                    Customer customer = new Customer(name);
                    orderLoop(customer);
                    customerList.add(customer);
                    System.out.println("Total: " + customer.calculateTotal() + " Baht");
                    break;
                case 2:
                    // แสดงจำนวนลูกค้าทั้งหมด
                    // Show the total number of customers
                    showCustomerCount();
                    break;
                case 3:
                    // แสดงสรุปสต็อกสินค้า
                    // Show stock summary
                    showStockSummary();
                    break;
                case 0:
                    // บันทึกคำสั่งซื้อและออกจากโปรแกรม
                    // Save orders and exit the program
                    saveOrders();
                    return;
                default:
                    // แจ้งเตือนเมื่อเลือกเมนูไม่ถูกต้อง
                    // Notify when an invalid menu option is chosen
                    System.out.println("Invalid menu option! Please choose between 0 and 3.");
                    break;
            }
        }
    }

    // ฟังก์ชันสำหรับจัดการคำสั่งซื้อของลูกค้า
    // Function to handle customer orders
    private void orderLoop(Customer customer) {
        while (true) {
            // แสดงเมนูหมวดหมู่สินค้า
            // Display category menu
            System.out.println("\nSelect a category:");
            System.out.println("1. Meat");
            System.out.println("2. Vegetable");
            System.out.println("3. Drink");
            System.out.println("0. Check Order");

            System.out.print("Select a category (0–3): ");
            int categoryChoice = getValidatedIntegerInput(0, 3, "menu");

            if (categoryChoice == 0) {
                // แสดงคำสั่งซื้อปัจจุบัน
                // Show the current order
                System.out.println("\n=== Current Order ===");
                Map<String, Integer> order = customer.getOrderMap();
                Map<String, Integer> prices = customer.getPriceMap();
                int total = 0;

                if (order.isEmpty()) {
                    System.out.println("No items ordered yet.");
                } else {
                    for (String itemName : order.keySet()) {
                        int qty = order.get(itemName);
                        int price = prices.get(itemName);
                        System.out.println(itemName + " x" + qty + " = " + (qty * price) + " Baht");
                        total += qty * price;
                    }
                    System.out.println("Total: " + total + " Baht");
                }

                System.out.println("\n1. Add More Menu");
                System.out.println("0. Finish Order");
                String subChoice = scanner.nextLine();
                if (subChoice.equals("0")) {
                    if (customer.getOrderMap().isEmpty()) {
                        System.out.println("You haven't ordered anything yet!");
                        System.out.println("Please select at least one item before finishing.");
                        continue;
                    }
                    break;
                }
            }

            // เลือกหมวดหมู่สินค้า
            // Select item category
            String category = "";
            if (categoryChoice == 1) category = "Meat";
            else if (categoryChoice == 2) category = "Vegetable";
            else if (categoryChoice == 3) category = "Drink";
            else {
                System.out.println("Invalid category.");
                continue;
            }

            // กรองสินค้าในหมวดหมู่ที่เลือก
            // Filter items in the selected category
            ArrayList<Item> filtered = new ArrayList<>();
            Set<String> names = new HashSet<>();
            for (Item i : stockList) {
                if (i.getClass().getSimpleName().equals(category) && !names.contains(i.getName())) {
                    filtered.add(i);
                    names.add(i.getName());
                }
            }

            while (true) {
                // แสดงเมนูสินค้าในหมวดหมู่ที่เลือก
                // Display items in the selected category
                System.out.println("\n--- " + category + " Menu ---");
                for (int i = 0; i < filtered.size(); i++) {
                    Item item = filtered.get(i);
                    System.out.println((i + 1) + ". " + item.getName() + " - " + item.getPrice() + " Baht (" + item.getStock() + " left)");
                }
                System.out.println("0. Back to category selection");
                System.out.print("Select item number (0–" + filtered.size() + "): ");
                int choice = getValidatedIntegerInput(0, filtered.size(), "item");

                if (choice == 0) break;

                try {
                    // เพิ่มสินค้าลงในคำสั่งซื้อ
                    // Add items to the order
                    Item item = filtered.get(choice - 1);
                    if (item.getStock() == 0) {
                        System.out.println("Sorry, " + item.getName() + " is out of stock!");
                        continue;
                    }
                    System.out.print("How many Units of " + item.getName() + "?: ");
                    int qty = getValidatedIntegerInput(1, item.getStock(), "stock");

                    item.reduceStock(qty);
                    for (int j = 0; j < qty; j++) {
                        customer.addItem(item);
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    // ฟังก์ชันสำหรับบันทึกคำสั่งซื้อทั้งหมดลงไฟล์
    // Function to save all orders to a file
    public void saveOrders() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("data/orders.txt"))) {
            for (Customer c : customerList) {
                pw.println("Customer: " + c.getName());
                pw.println("Order:");
                for (String itemName : c.getOrderMap().keySet()) {
                    int qty = c.getOrderMap().get(itemName);
                    int price = c.getPriceMap().get(itemName);
                    pw.println(" - " + itemName + " x" + qty + " = " + (qty * price) + " Baht");
                }
                pw.println("Total: " + c.calculateTotal() + " Baht\n");
            }
        } catch (IOException e) {
            // แจ้งเตือนเมื่อเกิดข้อผิดพลาดในการเขียนไฟล์
            // Notify when there is an error writing the file
            System.out.println("Error writing orders file.");
        }
    }

    // ฟังก์ชันสำหรับแสดงจำนวนลูกค้าทั้งหมด
    // Function to display the total number of customers
    public void showCustomerCount() {
        System.out.println("Total customers: " + customerList.size());
    }

    // ฟังก์ชันสำหรับแสดงสรุปสต็อกสินค้า
    // Function to display stock summary
    public void showStockSummary() {
        System.out.println("\n=== Stock Summary ===");
        for (Item item : stockList) {
            System.out.println(item.getName() + " (" + item.getClass().getSimpleName() + "): " + item.getStock() + " left");
        }
    }
}