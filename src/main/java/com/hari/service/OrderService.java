package com.hari.service;

import com.hari.domain.OrderStatus;
import com.hari.model.*;

import java.util.List;
import java.util.Set;

public interface OrderService {
      Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
      Order findOrderById(Long id) throws Exception;
      List<Order> userOrderHistory(Long userId);
      List<Order> sellersOrder(Long sellerId);
      Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;
      Order cancelOrder(Long orderId,User user) throws Exception;
      OrderItem getOrderItemById(Long id) throws Exception;
}
