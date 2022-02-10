package com.sellertool.server.domain.margin_record.vo;

import com.sellertool.server.domain.margin_record.entity.MarginRecordEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarginRecordVo {
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

    public static MarginRecordVo toVo(MarginRecordEntity entity) {
        MarginRecordVo vo = MarginRecordVo.builder()
                .id(entity.getId())
                .openKey(entity.getOpenKey())
                .name(entity.getName())
                .salePrice(entity.getSalePrice())
                .purchaseCost(entity.getPurchaseCost())
                .consumerDeliveryCharge(entity.getConsumerDeliveryCharge())
                .sellerDeliveryCharge(entity.getSellerDeliveryCharge())
                .purchaseDeliveryCharge(entity.getPurchaseDeliveryCharge())
                .extraCost(entity.getExtraCost())
                .commission(entity.getCommission())
                .totalIncome(entity.getTotalIncome())
                .totalIncomeInterestExpense(entity.getTotalIncomeInterestExpense())
                .totalExpense(entity.getTotalExpense())
                .margin(entity.getMargin())
                .marginRate(entity.getMarginRate())
                .incomeTax(entity.getIncomeTax())
                .expenseTax(entity.getExpenseTax())
                .totalTax(entity.getTotalTax())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
        return vo;
    }
}
