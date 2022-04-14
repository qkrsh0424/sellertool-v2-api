package com.sellertool.server.domain.margin_record.entity;

import com.sellertool.server.domain.margin_record.dto.MarginRecordDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "margin_record")
@DynamicInsert
public class MarginRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;
    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;
    @Column(name = "open_key")
    @Type(type = "uuid-char")
    private UUID openKey;
    @Column(name = "name")
    private String name;
    @Column(name = "sale_price", columnDefinition = "0")
    private float salePrice;
    @Column(name = "purchase_cost", columnDefinition = "0")
    private float purchaseCost;
    @Column(name = "consumer_delivery_charge", columnDefinition = "0")
    private float consumerDeliveryCharge;
    @Column(name = "seller_delivery_charge", columnDefinition = "0")
    private float sellerDeliveryCharge;
    @Column(name = "purchase_delivery_charge", columnDefinition = "0")
    private float purchaseDeliveryCharge;
    @Column(name = "extra_cost", columnDefinition = "0")
    private float extraCost;
    @Column(name = "commission", columnDefinition = "0")
    private float commission;
    @Column(name = "total_income", columnDefinition = "0")
    private float totalIncome;
    @Column(name = "total_income_interest_expense", columnDefinition = "0")
    private float totalIncomeInterestExpense;
    @Column(name = "total_expense", columnDefinition = "0")
    private float totalExpense;
    @Column(name = "margin", columnDefinition = "0")
    private float margin;
    @Column(name = "margin_rate", columnDefinition = "0")
    private float marginRate;
    @Column(name = "income_tax", columnDefinition = "0")
    private float incomeTax;
    @Column(name = "expense_tax", columnDefinition = "0")
    private float expenseTax;
    @Column(name = "total_tax", columnDefinition = "0")
    private float totalTax;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "workspace_id")
    @Type(type = "uuid-char")
    private UUID workspaceId;
    @Column(name = "created_by")
    @Type(type = "uuid-char")
    private UUID createdBy;

    public static MarginRecordEntity toEntity(MarginRecordDto dto) {
        MarginRecordEntity entity = MarginRecordEntity.builder()
                .cid(dto.getCid())
                .id(dto.getId())
                .openKey(dto.getOpenKey())
                .name(dto.getName())
                .salePrice(dto.getSalePrice())
                .purchaseCost(dto.getPurchaseCost())
                .consumerDeliveryCharge(dto.getConsumerDeliveryCharge())
                .sellerDeliveryCharge(dto.getSellerDeliveryCharge())
                .purchaseDeliveryCharge(dto.getPurchaseDeliveryCharge())
                .extraCost(dto.getExtraCost())
                .commission(dto.getCommission())
                .totalIncome(dto.getTotalIncome())
                .totalIncomeInterestExpense(dto.getTotalIncomeInterestExpense())
                .totalExpense(dto.getTotalExpense())
                .margin(dto.getMargin())
                .marginRate(dto.getMarginRate())
                .incomeTax(dto.getIncomeTax())
                .expenseTax(dto.getExpenseTax())
                .totalTax(dto.getTotalTax())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .workspaceId(dto.getWorkspaceId())
                .createdBy(dto.getCreatedBy())
                .build();
        return entity;
    }
}
