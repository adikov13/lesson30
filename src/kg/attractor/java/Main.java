package kg.attractor.java;

import kg.attractor.java.homework.RestaurantOrders;
import kg.attractor.java.lesson.MovieCollection;

public class Main {
    public static void main(String[] args) {
        // Для занятия
        var movieCollection = MovieCollection.readFromJson();

        // Для домашки
        var restaurantOrders = RestaurantOrders.read("orders_100.json");

        // Примеры использования методов:
        System.out.println("=== Top 5 orders ===");
        restaurantOrders.getTopNOrdersByTotal(5).forEach(order -> {
            System.out.printf("Customer: %s, Total: $%.2f%n",
                    order.getCustomer().getFullName(),
                    order.getTotal());
        });

        System.out.println("\n=== Total revenue ===");
        System.out.printf("$%.2f%n", restaurantOrders.getTotalRevenue());

        System.out.println("\n=== Customer with max total ===");
        System.out.println(restaurantOrders.getCustomerWithMaxTotal());
    }
}