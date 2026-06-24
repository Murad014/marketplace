package com.azercell.marketplace.catalog.web.dto.response;

import java.util.UUID;

/**
 * One selectable colour on a product card. The frontend renders a swatch from
 * {@code colorHex}/{@code colorName} and, when clicked, shows {@code imageUrl}.
 */
public record ColorOptionResponse(
        UUID variantId,
        UUID colorId,
        String colorName,
        String colorHex,
        String imageUrl
) {
}