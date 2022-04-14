package com.sellertool.server.domain.margin_record.dto;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarginRecordDto {
    private Integer cid;
    private UUID id;
    private UUID openKey;
    private String name;
    private float salePrice;
    private float purchaseCost;
    private float consumerDeliveryCharge;
    private float sellerDeliveryCharge;
    private float purchaseDeliveryCharge;
    private float extraCost;
    private float commission;
    private float totalIncome;
    private float totalIncomeInterestExpense;
    private float totalExpense;
    private float margin;
    private float marginRate;
    private float incomeTax;
    private float expenseTax;
    private float totalTax;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID workspaceId;
    private UUID createdBy;
}
