package com.azercell.marketplace.catalog.infrastructure.persistence.mapper;

import com.azercell.marketplace.catalog.domain.Color;
import com.azercell.marketplace.catalog.domain.vo.HexCode;
import com.azercell.marketplace.catalog.infrastructure.persistence.entity.ColorJpaEntity;

public class ColorMapper {
    public static ColorJpaEntity toJpaEntity(Color color) {
        var colorJpaEntity = new ColorJpaEntity();

        colorJpaEntity.setId(color.getId());
        colorJpaEntity.setName(color.getName());
        colorJpaEntity.setHexCode(color.getHexCode().value());
        colorJpaEntity.setStatus(color.getStatus());

        return colorJpaEntity;
    }

    public static Color toDomain(ColorJpaEntity colorJpaEntity) {
        if (colorJpaEntity.getId() == null)
            return Color.create(colorJpaEntity.getName(), new HexCode(colorJpaEntity.getHexCode()));
        else
            return Color.rehydrate(colorJpaEntity.getId(),
                    colorJpaEntity.getName(),
                    new HexCode(colorJpaEntity.getHexCode()),
                    colorJpaEntity.getStatus());
    }
}
