package com.orbit.models;

import lombok.*;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "account_mst")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private Long id;
    @Column(name = "account_no")
    private String accountNo;
    @Column(name = "description")
    private String accountName;
    @Column(name = "date_opened")
    private Date dateOpened;
    @Column(name = "current_balance")
    private BigDecimal totalBalance;
    @Column(name = "lasttran_date")
    private LocalDate lastTranDate;
    @Column(name = "opening_balance")
    private BigDecimal openingBalance;
    @Column(name = "account_type")
    private String account_type;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "account",
        joinColumns = @JoinColumn(name = "account_id"),
        inverseJoinColumns = @JoinColumn(name = "trans_id"))
    private List<Transactions> transactions = new ArrayList<>();



}
