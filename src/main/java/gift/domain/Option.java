package gift.domain;

public class Option {
    private Long id;

    public Long getId() {
        return id;
    }

    private String name;

    public String getName() {
        return name;
    }

    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    private Product product;

    public Product getProduct() {
        return product;
    }

    public int subtractQuantity(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("Not Enough Option Quantity");
        }

        this.quantity -= quantity;

        return this.quantity;
    }

    public Option(Long id, String name, int quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }
}
