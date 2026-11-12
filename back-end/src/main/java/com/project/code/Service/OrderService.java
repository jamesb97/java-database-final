package com.project.code.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.code.Model.Customer;
import com.project.code.Model.Inventory;
import com.project.code.Model.OrderDetails;
import com.project.code.Model.OrderItem;
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Product;
import com.project.code.Model.PurchaseProductDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.OrderDetailsRepository;
import com.project.code.Repo.OrderItemRepository;
import com.project.code.Repo.StoreRepository;

@Service
public class OrderService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {
        // 1. Retrieve or create the Customer
        Customer existingCustomer = customerRepository.findByEmail(placeOrderRequest.getCustomerEmail());
        Customer customer;
        if (existingCustomer == null) {
            customer = new Customer(
                    placeOrderRequest.getCustomerName(),
                    placeOrderRequest.getCustomerEmail(),
                    placeOrderRequest.getCustomerPhone());
            customer = customerRepository.save(customer);
        } else {
            customer = existingCustomer;
        }

        // 2. Retrieve the Store
        Store store = storeRepository.findById(placeOrderRequest.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // 3. Create OrderDetails
        OrderDetails orderDetails = new OrderDetails(
                customer,
                store,
                placeOrderRequest.getTotalPrice(),
                java.time.LocalDateTime.now());
        orderDetails = orderDetailsRepository.save(orderDetails);

        // 4. Create and save OrderItems (products purchased)
        List<PurchaseProductDTO> purchaseProducts = placeOrderRequest.getPurchaseProduct();
        List<Long> productIds = purchaseProducts.stream()
                .map(PurchaseProductDTO::getId)
                .distinct()
                .toList();
        List<Inventory> inventories = inventoryRepository.findByStore_IdAndProduct_IdIn(
                placeOrderRequest.getStoreId(), productIds);
        Map<Long, Inventory> inventoriesByProductId = new HashMap<>();
        for (Inventory inventory : inventories) {
            inventoriesByProductId.put(inventory.getProduct().getId(), inventory);
        }

        List<OrderItem> orderItems = new ArrayList<>(purchaseProducts.size());
        for (PurchaseProductDTO productDTO : purchaseProducts) {
            Inventory inventory = inventoriesByProductId.get(productDTO.getId());
            if (inventory == null) {
                throw new IllegalStateException("Inventory not found for product " + productDTO.getId()
                        + " in store " + placeOrderRequest.getStoreId());
            }

            inventory.setStockLevel(inventory.getStockLevel() - productDTO.getQuantity());

            Product product = inventory.getProduct();
            OrderItem orderItem = new OrderItem(
                    orderDetails,
                    product,
                    productDTO.getQuantity(),
                    productDTO.getPrice() * productDTO.getQuantity());
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);
    }
}

// package com.project.code.Service;


// public class OrderService {
// 1. **saveOrder Method**:
//    - Processes a customer's order, including saving the order details and associated items.
//    - Parameters: `PlaceOrderRequestDTO placeOrderRequest` (Request data for placing an order)
//    - Return Type: `void` (This method doesn't return anything, it just processes the order)

// 2. **Retrieve or Create the Customer**:
//    - Check if the customer exists by their email using `findByEmail`.
//    - If the customer exists, use the existing customer; otherwise, create and save a new customer using `customerRepository.save()`.

// 3. **Retrieve the Store**:
//    - Fetch the store by ID from `storeRepository`.
//    - If the store doesn't exist, throw an exception. Use `storeRepository.findById()`.

// 4. **Create OrderDetails**:
//    - Create a new `OrderDetails` object and set customer, store, total price, and the current timestamp.
//    - Set the order date using `java.time.LocalDateTime.now()` and save the order with `orderDetailsRepository.save()`.

// 5. **Create and Save OrderItems**:
//    - For each product purchased, find the corresponding inventory, update stock levels, and save the changes using `inventoryRepository.save()`.
//    - Create and save `OrderItem` for each product and associate it with the `OrderDetails` using `orderItemRepository.save()`.

//
// }
