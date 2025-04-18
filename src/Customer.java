import java.util.*;

public class Customer {
    private String name;
    private Map<String, Integer> orderMap = new LinkedHashMap<>();
    private Map<String, Integer> priceMap = new HashMap<>();

    public Customer(String name) {
        this.name = name;
    }

    public void addItem(Item item) {
        String itemName = item.getName();
        orderMap.put(itemName, orderMap.getOrDefault(itemName, 0) + 1);
        priceMap.put(itemName, item.getPrice());
    }

    public int calculateTotal() {
        int total = 0;
        for (String item : orderMap.keySet()) {
            total += orderMap.get(item) * priceMap.get(item);
        }
        return total;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getOrderMap() {
        return orderMap;
    }

    public Map<String, Integer> getPriceMap() {
        return priceMap;
    }
}