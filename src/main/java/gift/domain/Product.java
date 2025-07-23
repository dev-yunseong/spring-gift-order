package gift.domain;

public class Product {

    public enum Status {
        READY, // 사용 가능 상태 [Option도 등록 완료]
        APPROVED, // 내부 승인 됨
        PENDING, // MD에 의해 검토 필요
        REJECTED // MD에 의해 거절 당함
    }

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private Status status;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Status getStatus() {
        return status;
    }

    public Product(String name, int price, String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Product(Long id, String name, int price, String imageUrl, Status status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        status = inferStatusWithName(name);
    }

    private Status inferStatusWithName(String name) {
        if (name.contains("카카오")) {
            return Product.Status.PENDING;
        } else {
            return Product.Status.APPROVED;
        }
    }
}
