package de.teampb.soco.llm.guitester.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity()
@Table(name = "T_ORDER")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private int orderId;

    @Column(name="order_start")
    private LocalDateTime orderStart;

    @Column(name="order_shipping_date")
    private LocalDateTime orderShippingDate;

    @Column(name="customer_id")
    private String customerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderStart() {
        return orderStart;
    }

    public void setOrderStart(LocalDateTime orderStart) {
        this.orderStart = orderStart;
    }

    public LocalDateTime getOrderShippingDate() {
        return orderShippingDate;
    }

    public void setOrderShippingDate(LocalDateTime orderShippingDate) {
        this.orderShippingDate = orderShippingDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getOpenItemQty() {
        return this.getOrderItems().stream().mapToInt((orderItem -> orderItem.getTotalQty()-orderItem.getActQty())).sum();
    }
}