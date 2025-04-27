package com.ps;


import java.util.Objects;

public class Product {
    private String sku;
    private String name;
    private double price;
    private String department;

    public Product(String sku, String name, double price, String department) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.department = department;
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDepartment() { return department; }

    @Override
    public String toString() {
        return name + " | $" + price + " | " + department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(sku, product.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }
}
