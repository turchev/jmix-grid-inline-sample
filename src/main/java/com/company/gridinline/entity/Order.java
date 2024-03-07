package com.company.gridinline.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "ORDER_", indexes = {
        @Index(name = "IDX_ORDER__CUSTOMER", columnList = "CUSTOMER_ID")
})
@Entity(name = "Order_")
public class Order {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "NUMBER_")
    private Long number;

    @Column(name = "COSTS")
    private BigDecimal costs;

    @Column(name = "ORDER_DATE")
    private LocalDate orderDate;

    @Composition
    @OneToMany(mappedBy = "order")
    private List<OrderLine> orderLines;

    @JoinColumn(name = "CUSTOMER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public void setCosts(BigDecimal costs) {
        this.costs = costs;
    }

    public BigDecimal getCosts() {
        return costs;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getNumber() {
        return number;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}