package com.hari.service.Impl;

import com.hari.domain.OrderStatus;
import com.hari.domain.PaymentStatus;
import com.hari.model.*;
import com.hari.repository.AddressRepository;
import com.hari.repository.OrderItemRepository;
import com.hari.repository.OrderRepository;
import com.hari.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if (!user.getAddresses().contains(shippingAddress)){
            user.getAddresses().add(shippingAddress);
        }
        Address address=addressRepository.save(shippingAddress);
        Map<Long,List<CartItem>> itemsBySeller=cart.getCartItems().stream().collect(Collectors.groupingBy(item->item.getProduct().getSeller().getId()));
        Set<Order> orders=new HashSet<>();
        for (Map.Entry<Long,List<CartItem>> entry:itemsBySeller.entrySet()){
            Long sellerId=entry.getKey();
            List<CartItem> items=entry.getValue();

            int totalOrderPrice=items.stream().mapToInt(
                    CartItem::getSellingPrice
            ).sum();
            int totalItem=items.stream().mapToInt(CartItem::getQuantity).sum();
            Order createdOrder=new Order();

            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice( totalOrderPrice);
            createdOrder.setTotalSellingPrice(totalOrderPrice);
            createdOrder.setShippingAddress(address);
            createdOrder.setTotalItem(totalItem);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
            Order saveOrder=orderRepository.save(createdOrder);
            orders.add(saveOrder);

            List<OrderItem> orderItems=new ArrayList<>();

            for (CartItem item:items){
                OrderItem orderItem=new OrderItem();
                orderItem.setOrder(saveOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setSellingPrice(item.getSellingPrice());

                saveOrder.getOrderItems().add(orderItem);

                OrderItem savedOrderItem=orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
            }

        }

        return orders;
    }

    @Override
    public Order findOrderById(Long id) throws Exception {
       return orderRepository.findById(id).orElseThrow(()->new Exception("order not found with id :"+id));
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order=findOrderById(orderId);
        order.setOrderStatus(orderStatus);

        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order=findOrderById(orderId);
        if (!user.getId().equals(order.getUser().getId())){
            throw new Exception("you don't have access to this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);

        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepository.findById(id).orElseThrow(()->new Exception("order item not found with id :"+id));
    }
}
