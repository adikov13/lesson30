package kg.attractor.java.homework;

import com.google.gson.Gson;
import kg.attractor.java.homework.domain.Order;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import kg.attractor.java.homework.domain.Item;

public class RestaurantOrders {
    // Этот блок кода менять нельзя! НАЧАЛО!
    private List<Order> orders;

    private RestaurantOrders(String fileName) {
        var filePath = Path.of("data", fileName);
        Gson gson = new Gson();
        try {
            orders = List.of(gson.fromJson(Files.readString(filePath), Order[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RestaurantOrders read(String fileName) {
        var ro = new RestaurantOrders(fileName);
        ro.getOrders().forEach(Order::calculateTotal);
        return ro;
    }

    public List<Order> getOrders() {
        return orders;
    }
    // Этот блок кода менять нельзя! КОНЕЦ!

    //----------------------------------------------------------------------
    //------   Реализация ваших методов должна быть ниже этой линии   ------
    //----------------------------------------------------------------------

    // 1. Печать списка заказов
    public void printOrders() {
        orders.forEach(order -> {
            System.out.println("Customer: " + order.getCustomer().getFullName());
            System.out.println("Email: " + order.getCustomer().getEmail());
            System.out.println("Home delivery: " + order.isHomeDelivery());
            System.out.println("Items:");
            order.getItems().forEach(item ->
                    System.out.println("  " + item.getName() + " x" + item.getAmount() + " - $" + item.getPrice()));
            System.out.println("Total: $" + order.getTotal());
            System.out.println("----------------------");
        });
    }

    // 2. Топ N заказов по стоимости
    public List<Order> getTopNOrdersByTotal(int n) {
        return orders.stream()
                .sorted((o1, o2) -> Double.compare(o2.getTotal(), o1.getTotal()))
                .limit(n)
                .collect(Collectors.toList());
    }

    // 3. N заказов с наименьшей стоимостью
    public List<Order> getBottomNOrdersByTotal(int n) {
        return orders.stream()
                .sorted(Comparator.comparingDouble(Order::getTotal))
                .limit(n)
                .collect(Collectors.toList());
    }

    // 4. Заказы с доставкой на дом
    public List<Order> getHomeDeliveryOrders() {
        return orders.stream()
                .filter(Order::isHomeDelivery)
                .collect(Collectors.toList());
    }

    // 5. Самый и наименее прибыльный заказ на дом (без сортировки)
    public Map<String, Order> getMostAndLeastProfitableHomeDeliveryOrders() {
        List<Order> homeOrders = getHomeDeliveryOrders();
        if (homeOrders.isEmpty()) return Map.of();

        Order max = homeOrders.get(0);
        Order min = homeOrders.get(0);

        for (Order order : homeOrders) {
            if (order.getTotal() > max.getTotal()) max = order;
            if (order.getTotal() < min.getTotal()) min = order;
        }

        return Map.of("mostProfitable", max, "leastProfitable", min);
    }

    // 6. Заказы в диапазоне суммы
    public List<Order> getOrdersInTotalRange(double minOrderTotal, double maxOrderTotal) {
        return orders.stream()
                .filter(order -> order.getTotal() > minOrderTotal && order.getTotal() < maxOrderTotal)
                .collect(Collectors.toList());
    }

    // 7. Общая стоимость всех заказов
    public double getTotalRevenue() {
        return orders.stream()
                .mapToDouble(Order::getTotal)
                .sum();
    }

    // 8. Уникальные email (отсортированные без sorted())
    public List<String> getUniqueEmailsSorted() {
        return orders.stream()
                .map(order -> order.getCustomer().getEmail())
                .collect(Collectors.toCollection(TreeSet::new))
                .stream().collect(Collectors.toList());
    }

    // 9. Заказы по клиентам
    public Map<String, List<Order>> getOrdersGroupedByCustomer() {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getFullName(),
                        Collectors.toList()));
    }

    // 10. Сумма заказов по клиентам
    public Map<String, Double> getTotalPerCustomer() {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getFullName(),
                        Collectors.summingDouble(Order::getTotal)));
    }

    // 11. Клиент с максимальной суммой заказов
    public String getCustomerWithMaxTotal() {
        Map<String, Double> totals = getTotalPerCustomer();
        return Collections.max(totals.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    // 12. Клиент с минимальной суммой заказов
    public String getCustomerWithMinTotal() {
        Map<String, Double> totals = getTotalPerCustomer();
        return Collections.min(totals.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    // 13. Товары по количеству продаж
    public Map<String, Long> getItemsByTotalQuantity() {
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        Item::getName,
                        Collectors.summingLong(Item::getAmount)));
    }

    // Бонус: Email клиентов, заказывавших определенный товар
    public List<String> getEmailsForCustomersWhoOrderedItem(String itemName) {
        return orders.stream()
                .filter(order -> order.getItems().stream()
                        .anyMatch(item -> item.getName().equals(itemName)))
                .map(order -> order.getCustomer().getEmail())
                .distinct()
                .collect(Collectors.toList());
    }
}