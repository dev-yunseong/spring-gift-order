package gift.entity;

import gift.dto.OptionBuyingRequestDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "option_buyings")
public class OptionBuyingEntity {

    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "option_id")
    private OptionEntity optionEntity;

    public OptionEntity getOptionEntity() {
        return optionEntity;
    }

    @Column(nullable = false)
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    @Column(name = "created_at", insertable = false)
    private LocalDateTime orderDateTime;

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    @Column(nullable = false)
    private String message;

    public String getMessage() {
        return message;
    }

    protected OptionBuyingEntity() {}

    public OptionBuyingEntity(Long id, OptionEntity optionEntity, int quantity, String message) {
        this.id = id;
        this.optionEntity = optionEntity;
        this.quantity = quantity;
        this.message = message;
    }

    public OptionBuyingEntity(OptionBuyingRequestDto optionBuyingRequestDto, OptionEntity optionEntity) {
        this(
                null,
                optionEntity,
                optionBuyingRequestDto.quantity(),
                optionBuyingRequestDto.message()
                );
    }

}