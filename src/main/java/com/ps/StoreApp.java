package com.ps;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class StoreApp {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Product> inventory = new ArrayList<>();
    static ArrayList<Product> cart = new ArrayList<>();

    public static void main(String[] args) {
        loadProducts();

        int mainMenuCommand;

        do {
            System.out.println("\nWelcome to the Online Store!");
            System.out.println("1) Display Products");
            System.out.println("2) Display Cart");
            System.out.println("0) Exit");
            System.out.print("What would you like to do? ");
            mainMenuCommand = scanner.nextInt();

            switch (mainMenuCommand) {
                case 1:
                    displayProducts();
                    break;
                case 2:
                    displayCart();
                    break;
                case 0:
                    System.out.println("Exiting. Thanks for visiting!");
                    break;
                default:
                    System.out.println("Command not found! Try again.");
            }

        } while (mainMenuCommand != 0);
    }

    private static void loadProducts() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("products.csv"));
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split("\\|");

                String sku = parts[0];
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                String department = parts[3];

                Product product = new Product(sku, name, price, department);
                inventory.add(product);
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void displayProducts() {
        System.out.println("\n--- Product Menu ---");
        System.out.println("1) View all products");
        System.out.println("2) Search by name");
        System.out.println("3) Filter by department");
        System.out.println("4) Add product to cart");
        System.out.println("0) Back");
        System.out.print("What would you like to do? ");
        int productMenuCommand = scanner.nextInt();

        switch (productMenuCommand) {
            case 1:
                displayAllProducts();
                break;
            case 2:
                findProductByName();
                break;
            case 3:
                filterByDepartment();
                break;
            case 4:
                addProductToCart();
                break;
            case 0:
                System.out.println("Going back...");
                break;
            default:
                System.out.println("Incorrect command, going back...");
        }
    }

    private static void displayAllProducts() {
        System.out.println("\n--- All Products ---");
        for (Product product : inventory) {
            System.out.println(product);
        }
    }

    private static void findProductByName() {
        System.out.println("Please provide a product name: ");
        scanner.nextLine(); // clear the leftover \n
        String name = scanner.nextLine();

        boolean found = false;
        for (Product product : inventory) {
            if (product.getName().equalsIgnoreCase(name)) {
                System.out.println(product);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Product not found.");
        }
    }

    private static void filterByDepartment() {
        System.out.println("Please provide a department: ");
        scanner.nextLine();
        String department = scanner.nextLine();

        boolean found = false;
        for (Product product : inventory) {
            if (product.getDepartment().equalsIgnoreCase(department)) {
                System.out.println(product);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No products found in this department.");
        }
    }

    private static void addProductToCart() {
        System.out.println("\n--- Add Product to Cart ---");
        displayAllProducts();
        System.out.print("Enter the number of the product to add: ");
        int productNumber = scanner.nextInt();
        int index = productNumber - 1;

        if (index >= 0 && index < inventory.size()) {
            Product selected = inventory.get(index);
            cart.add(selected);
            System.out.println(selected.getName() + " added to cart.");
        } else {
            System.out.println("Invalid product selection.");
        }
    }

    private static void displayCart() {
        System.out.println("\n--- Your Cart ---");
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        double total = 0;
        for (Product product : cart) {
            System.out.println(product);
            total += product.getPrice();
        }
        System.out.println("Total: $" + total);

        System.out.println("\n1) Checkout");
        System.out.println("2) Remove product from cart");
        System.out.println("0) Back");
        System.out.print("What would you like to do? ");
        int cartMenuCommand = scanner.nextInt();

        switch (cartMenuCommand) {
            case 1:
                checkout(total);
                break;
            case 2:
                removeProductFromCart();
                break;
            case 0:
                System.out.println("Going back...");
                break;
            default:
                System.out.println("Invalid option, going back...");
        }
    }

    private static void checkout(double total) {
        System.out.print("Enter cash amount: ");
        double payment = scanner.nextDouble();

        if (payment < total) {
            System.out.println("Insufficient payment. Try again.");
            return;
        }

        double change = payment - total;
        System.out.println("Change: $" + change);

        printReceipt(total, payment, change);
        cart.clear();
        System.out.println("Checkout complete. Thank you!");
    }

    private static void printReceipt(double total, double paid, double change) {
        StringBuilder receipt = new StringBuilder();
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        receipt.append("Receipt - ").append(timeStamp).append("\n");
        receipt.append("-------------------------\n");
        for (Product product : cart) {
            receipt.append(product.getName()).append(" - $").append(product.getPrice()).append("\n");
        }
        receipt.append("Total: $").append(total).append("\n");
        receipt.append("Paid: $").append(paid).append("\n");
        receipt.append("Change: $").append(change).append("\n");

        try {
            Files.createDirectories(Paths.get("Receipts"));
            Files.write(Paths.get("Receipts/" + timeStamp + ".txt"), receipt.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Error writing receipt: " + e.getMessage());
        }

        System.out.println("\n" + receipt);
    }

    private static void removeProductFromCart() {
        System.out.println("Please provide a product name to remove:");
        scanner.nextLine();
        String name = scanner.nextLine();

        for (Product product : cart) {
            if (product.getName().equalsIgnoreCase(name)) {
                cart.remove(product);
                System.out.println(name + " removed from cart.");
                return;
            }
        }
        System.out.println("Product not found in cart.");
    }
}









