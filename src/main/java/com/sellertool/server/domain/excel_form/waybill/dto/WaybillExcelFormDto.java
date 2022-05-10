package com.sellertool.server.domain.excel_form.waybill.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaybillExcelFormDto {
    private String receiver;
    private String freightCode;
    private String waybillNumber;
    private String transportType;
    private String courier;
}
