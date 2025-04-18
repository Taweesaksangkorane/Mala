public class Vegetable extends Item {
    public Vegetable(String name, int stock) {
        super(name, stock);
    }

    @Override
    public int getPrice() {
        return 10;
    }
}