package com.sellertool.server.domain.erp_download_excel_header.dto;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpDownloadExcelHeaderDetailDto {
    //    @Type(type = "jsonb")
    private List<DetailDto> details;
}
