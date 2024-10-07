package com.hari.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerReport {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Seller seller;

    private Long totalEarnings=0L;

    private Long totalSales=0L;

    private Long totalRefunds=0L;

    private Long totalTax=0L;

    private Long netEarnings=0L;

    private Integer totalOrders=0;

    private Integer cancelledOrders=0;

    private Integer totalTransactions=0;

}
