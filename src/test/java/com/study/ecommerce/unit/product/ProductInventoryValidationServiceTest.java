package com.study.ecommerce.unit.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.study.ecommerce.domain.product.ProductException;
import com.study.ecommerce.domain.product.ProductInventory;
import com.study.ecommerce.domain.product.service.ProductInventoryValidationService;
import com.study.ecommerce.infra.product.ProductInventoryQueryRepository;

@ExtendWith(MockitoExtension.class)
class ProductInventoryValidationServiceTest {

	@InjectMocks
	private ProductInventoryValidationService productInventoryValidationService;

	@Mock
	private ProductInventoryQueryRepository productInventoryQueryRepository;

	@Test
	@DisplayName("재고가 충분하면 예외가 발생하지 않고 검증을 통과해야 한다.")
	void testValidateAmount_Success() {
		// given
		Long productId = 1L;
		long amountToValidate = 10L;

		ProductInventory productInventory = ProductInventory.builder()
			.productId(productId)
			.amount(20)
			.build();

		when(productInventoryQueryRepository.getOne(productId)).thenReturn(Optional.of(productInventory));

		// when & then
		productInventoryValidationService.validateAmount(productId, amountToValidate);

		// 재고가 충분하므로 예외가 발생하지 않음
		verify(productInventoryQueryRepository, times(1)).getOne(productId);
	}

	@Test
	@DisplayName("재고가 부족하면 ProductAmountExceedException 예외가 발생해야 한다.")
	void testValidateAmount_Failure_InsufficientInventory() {
		// given
		Long productId = 1L;
		long amountToValidate = 30L;

		ProductInventory productInventory = ProductInventory.builder()
			.productId(productId)
			.amount(20)
			.build();

		when(productInventoryQueryRepository.getOne(productId)).thenReturn(Optional.of(productInventory));

		// when & then
		assertThatThrownBy(() -> productInventoryValidationService.validateAmount(productId, amountToValidate))
			.isInstanceOf(ProductException.class);

		// 재고가 부족하므로 예외 발생
		verify(productInventoryQueryRepository, times(1)).getOne(productId);
	}

	@Test
	@DisplayName("존재하지 않는 상품일 경우 ProductNotFoundException 예외가 발생해야 한다.")
	void testValidateAmount_Failure_ProductNotFound() {
		// given
		Long productId = 999L;
		long amountToValidate = 10L;

		when(productInventoryQueryRepository.getOne(productId)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> productInventoryValidationService.validateAmount(productId, amountToValidate))
			.isInstanceOf(ProductException.class);

		// 상품이 존재하지 않으므로 예외 발생
		verify(productInventoryQueryRepository, times(1)).getOne(productId);
	}
}