package com.azercell.marketplace.catalog.application.service.impl;

import com.azercell.marketplace.catalog.application.port.BrandRepository;
import com.azercell.marketplace.catalog.application.port.CategoryRepository;
import com.azercell.marketplace.catalog.application.port.ProductRepository;
import com.azercell.marketplace.catalog.application.service.ProductService;
import com.azercell.marketplace.catalog.domain.Color;
import com.azercell.marketplace.catalog.domain.ProductImage;
import com.azercell.marketplace.catalog.domain.ProductVariant;
import com.azercell.marketplace.catalog.domain.aggregate.Product;
import com.azercell.marketplace.catalog.domain.vo.*;
import com.azercell.marketplace.catalog.web.dto.request.AddProductRequest;
import com.azercell.marketplace.catalog.web.dto.request.UpdateProductRequest;
import com.azercell.marketplace.catalog.web.dto.response.ProductResponse;
import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.common.util.CommonUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    private final CommonUtil commonUtil;

    private final static Currency DEFAULT_CURRENCY = Currency.AZN;

    @Override
    @Transactional
    public UUID addProduct(AddProductRequest request) {
        var basePrice = Money.of(request.price());
        var promoPrice = request.promoPrice() != null ? Money.of(request.promoPrice()) : null;
        var specs = new Specs(commonUtil.toJson(request.specifications()));
        var productVariants = prepareProductVariants(request);
        var brandFromDB = brandRepository.getBrandById(request.brandId())
                .orElseThrow(() -> new DomainException(ErrorCode.BRAND_NOT_FOUND));
        var categoryFromDB = categoryRepository
                .getCategoryById(request.categoryId())
                .orElseThrow(() -> new DomainException(ErrorCode.CATEGORY_NOT_FOUND));

        var newProduct = new Product(
                null,
                null,
                request.name(),
                brandFromDB.getId(),
                request.description(),
                basePrice,
                promoPrice,
                DEFAULT_CURRENCY,
                specs,
                categoryFromDB.getId(),
                productVariants,
                new HashSet<>(List.of(UUID.randomUUID(), UUID.randomUUID())), // TODO => When ready the finance context then finish
                request.availability(),
                Status.ACTIVE
        );

        // TODO => (!!!) UPDATE STOCK (!!!) using EventPublisher


        return productRepository.insert(newProduct, brandFromDB);
    }

    @Override
    @Transactional
    public void updateProduct(UUID productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(ErrorCode.PRODUCT_NOT_FOUND));

        var brand = brandRepository.getBrandById(request.brandId())
                .orElseThrow(() -> new DomainException(ErrorCode.BRAND_NOT_FOUND));
        var category = categoryRepository.getCategoryById(request.categoryId())
                .orElseThrow(() -> new DomainException(ErrorCode.CATEGORY_NOT_FOUND));

        product.changeName(request.name());
        product.changeBrand(brand.getId());
        product.changeDescription(request.description());
        product.changeBasePrice(Money.of(request.price()));
        product.changeSpecs(new Specs(commonUtil.toJson(request.specifications())));
        product.changeCategory(category.getId());
        product.changeAvailability(request.availability());

        product.syncVariants(toVariantDomain(request.variants()));

        productRepository.update(product, brand);
    }

    @Override
    @Transactional
    public ProductResponse getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(ErrorCode.PRODUCT_NOT_FOUND));
        return toResponse(product);
    }

    private ProductResponse toResponse(Product product) {
        var variants = product.getProductVariants().stream()
                .map(this::toVariantResponse)
                .toList();

        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getBrandId(),
                product.getDescription(),
                product.getBasePrice().amount(),
                product.getPromoPrice().map(Money::amount).orElse(null),
                product.getSellingPrice().amount(),
                product.getPriceCurrency() != null ? product.getPriceCurrency().name() : null,
                commonUtil.readJson(product.getSpecs() != null ? product.getSpecs().json() : null),
                product.getCategoryId(),
                product.getAvailability() != null ? product.getAvailability().name() : null,
                product.getStatus() != null ? product.getStatus().name() : null,
                product.getCreditPlans(),
                variants
        );
    }

    private ProductResponse.VariantResponse toVariantResponse(ProductVariant variant) {
        var images = variant.getProductImages().stream()
                .map(this::toImageResponse)
                .toList();

        return new ProductResponse.VariantResponse(
                variant.getId(),
                variant.getSku(),
                toColorResponse(variant.getColor()),
                variant.getPriceOverride() != null ? variant.getPriceOverride().amount() : null,
                variant.getStatus() != null ? variant.getStatus().name() : null,
                commonUtil.readJson(variant.getSpecs() != null ? variant.getSpecs().json() : null),
                images
        );
    }

    private ProductResponse.ColorResponse toColorResponse(Color color) {
        if (color == null) return null;
        return new ProductResponse.ColorResponse(
                color.getId(),
                color.getName(),
                color.getHexCode() != null ? color.getHexCode().value() : null,
                color.getStatus() != null ? color.getStatus().name() : null
        );
    }

    private ProductResponse.ImageResponse toImageResponse(ProductImage image) {
        return new ProductResponse.ImageResponse(
                image.getId(),
                image.getName(),
                image.getUrl(),
                image.getAltText(),
                image.isPrimary(),
                image.getSortOrder(),
                image.getStatus() != null ? image.getStatus().name() : null
        );
    }

    private List<ProductVariant> toVariantDomain(List<UpdateProductRequest.VariantPart> parts) {
        List<ProductVariant> result = new ArrayList<>();

        for (var vp : parts) {
            var color = Color.create(vp.colorName(), new HexCode(vp.colorHexCode()));
            var specs = new Specs(commonUtil.toJson(vp.specifications()));
            var images = toImageDomain(vp.images());

            ProductVariant variant = (vp.id() == null)
                    ? ProductVariant.create(color, specs, images)                       // new
                    : ProductVariant.update(vp.id(), vp.sku(), color, specs, Status.ACTIVE, images); // existing (id carried)

            result.add(variant);
        }

        return result;
    }

    private List<ProductImage> toImageDomain(List<UpdateProductRequest.ImagePart> imageParts) {
        List<ProductImage> images = new ArrayList<>();
        if (imageParts == null) return images;

        for (var ip : imageParts) {
            ProductImage image = (ip.id() == null)
                    ? ProductImage.create(ip.url(), ip.name(), ip.altText(), ip.isPrimary(), ip.sort())
                    : ProductImage.update(ip.id(), ip.url(), ip.name(), ip.altText(), ip.isPrimary(), ip.sort());
            images.add(image);
        }
        return images;
    }


    // <editor-fold desc="privateHelperMethods" >
    private List<ProductVariant> prepareProductVariants(AddProductRequest request) {
        List<ProductVariant> productVariants = new ArrayList<>();
        request.productVariants()
                .forEach(productVReq -> {
                    var colorHexCode = new HexCode(productVReq.colorHexCode());
                    var color = Color.create(productVReq.colorName(), colorHexCode);
                    var productVariantImages = new ArrayList<ProductImage>();
                    productVReq.images().forEach(imageReq -> {
                        var productImageD = ProductImage.create(
                                imageReq.url(),
                                imageReq.name(),
                                imageReq.altText(),
                                imageReq.isPrimary(),
                                imageReq.sort());
                        productVariantImages.add(productImageD);
                    });
                    var productVariantDomain = ProductVariant.create(color, null, productVariantImages);
                    productVariants.add(productVariantDomain);
                });
        return productVariants;
    }
    // </editor-fold>
}
