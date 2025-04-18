public class Meat extends Item {
    public Meat(String name, int stock) {
        super(name, stock);
    }

    @Override
    public int getPrice() {
        return 20;
    }
}