public class Drink extends Item {
    public Drink(String name, int stock) {
        super(name, stock);
    }

    @Override
    public int getPrice() {
        return 15;
    }
}