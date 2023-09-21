package com.examly.springapp;

import com.examly.springapp.controller.ShoppingCartController;
import com.examly.springapp.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpringappApplicationTests {

	@InjectMocks
	private ShoppingCartController cartController;

	@Mock
	private List<Product> cart;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testAddToCart_Success() {
		// Arrange
		Product product = new Product("Product1", 10.99, 2);

		// Act
		ResponseEntity<String> response = cartController.addToCart(product);

		// Assert
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Successfully added the product to the cart", response.getBody());
	}

	@Test
	public void testAddToCart_InvalidRequest() {
		// Arrange
		Product product = null;

		// Act
		ResponseEntity<String> response = cartController.addToCart(product);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Invalid request payload or missing required fields.", response.getBody());
	}

	// Test case for success scenario in getTotalPrice method
	// Test case for success scenario in getTotalPrice method
	@Test
	public void testGetTotalPrice_Success() {
		// Arrange
		ShoppingCartController cartController = new ShoppingCartController();

		List<Product> products = new ArrayList<>();
		products.add(new Product("Product1", 10.99, 2));
		products.add(new Product("Product2", 5.49, 3));

		List<Product> cartMock = mock(ArrayList.class);
		when(cartMock.isEmpty()).thenReturn(false);
		when(cartMock.stream()).thenReturn(products.stream());

		// Set the mocked cart in the controller using ReflectionTestUtils
		ReflectionTestUtils.setField(cartController, "cart", cartMock);

		// Manually calculate the total price
		double expectedTotalPrice = products.stream().mapToDouble(product -> product.getPrice() * product.getQuantity())
				.sum();

		// Act
		ResponseEntity<?> response = cartController.getTotalPrice();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody() instanceof String);
		String responseBody = (String) response.getBody();
		assertEquals("Successfully retrieved the total price of the cart: " + expectedTotalPrice, responseBody);
	}

	@Test
    public void testGetTotalPrice_CartEmpty() {
        // Arrange
        when(cart.isEmpty()).thenReturn(true);

        // Act
        ResponseEntity<?> response = cartController.getTotalPrice();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	  assertEquals("The cart is empty.", response.getBody());
    }

	@Test
	public void testCheckout_CartIsEmpty() {
		// Arrange
		ShoppingCartController cartController = new ShoppingCartController();
		List<Product> cartMock = mock(ArrayList.class);
		when(cartMock.isEmpty()).thenReturn(true);
		ReflectionTestUtils.setField(cartController, "cart", cartMock);

		// Act
		ResponseEntity<String> response = cartController.checkout();

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("The cart is empty.", response.getBody());
	}

	@Test
	public void testCheckout_CartIsNotEmpty() {
		// Arrange
		ShoppingCartController cartController = new ShoppingCartController();
		List<Product> cartMock = mock(ArrayList.class);
		when(cartMock.isEmpty()).thenReturn(false);
		ReflectionTestUtils.setField(cartController, "cart", cartMock);

		// Act
		ResponseEntity<String> response = cartController.checkout();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Successfully checked out the cart and processed the payment.", response.getBody());
	}

	@Test
	public void testCheckout_PaymentProcessingError() {
		// Arrange
		ShoppingCartController cartController = new ShoppingCartController();
		List<Product> cartMock = mock(ArrayList.class);
		when(cartMock.isEmpty()).thenReturn(false);
		ReflectionTestUtils.setField(cartController, "cart", cartMock);

		// Simulate an exception during payment processing
		doThrow(new RuntimeException("Payment processing error")).when(cartMock).clear();

		// Act
		ResponseEntity<String> response = cartController.checkout();

		// Assert
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("An error occurred while processing the payment.", response.getBody());
	}

	@Test
    public void testCheckout_CartEmpty() {
        // Arrange
        when(cart.isEmpty()).thenReturn(true);

        // Act
        ResponseEntity<String> response = cartController.checkout();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("The cart is empty.", response.getBody());
    }

	@Test

	public void controllerfolder() {
		String directoryPath = "src/main/java/com/examly/springapp/controller"; // Replace with the path to your
																				// directory
		File directory = new File(directoryPath);
		assertTrue(directory.exists() && directory.isDirectory());
	}

	@Test
	public void controllerfile() {
		String filePath = "src/main/java/com/examly/springapp/controller/ShoppingCartController.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}

	@Test
	public void testModelFolder() {
		String directoryPath = "src/main/java/com/examly/springapp/model"; // Replace with the path to your directory
		File directory = new File(directoryPath);
		assertTrue(directory.exists() && directory.isDirectory());
	}

	@Test
	public void testModelFile() {
		String filePath = "src/main/java/com/examly/springapp/model/Product.java";
		File file = new File(filePath);
		assertTrue(file.exists() && file.isFile());
	}

}
