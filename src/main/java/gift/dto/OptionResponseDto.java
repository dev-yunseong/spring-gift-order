package gift.dto;

import gift.entity.OptionEntity;

public record OptionResponseDto(
        Long id,
        String name,
        int quantity
) {
    public OptionResponseDto(OptionEntity optionEntity) {
        this(optionEntity.getId(), optionEntity.getName(), optionEntity.getQuantity());
    }
}

