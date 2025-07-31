package gift.domain;

import java.time.LocalDateTime;

public class OptionBuying {

    private static final String MESSAGE_TEMPLATE =
            """
            주문번호: %d
            상품명: %s
            옵션명: %s
            주문 수량: %d
            주문 일자: %s
            메시지: %s
            """;

    private Long id;
    private Product product;
    private Option option;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;

    public String createMessage() {
        return String.format(
                MESSAGE_TEMPLATE,
                id,
                product.getName(),
                option.getName(),
                quantity,
                orderDateTime.toString(),
                message);
    }

    public OptionBuying(Long id, Product product, Option option, int quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.product = product;
        this.option = option;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }
}
