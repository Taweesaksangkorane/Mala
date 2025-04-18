public abstract class Item {
    protected String name;
    protected int stock;

    public Item(String name, int stock) {
        this.name = name;
        this.stock = stock;
    }

    public String getName() { return name; }

    public int getStock() { return stock; }

    public void reduceStock(int amount) throws Exception {
        if (amount > stock) {
            throw new Exception("Not enough stock for " + name);
        }
        stock -= amount;
    }

    public abstract int getPrice();
}