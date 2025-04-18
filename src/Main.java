import java.util.*;

public class Main {
    public static void main(String[] args) {
        Store store = new Store();
        store.loadStock();  
        store.run();        
        store.saveOrders(); 
    }
}