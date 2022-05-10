package com.sellertool.server.domain.erp_download_excel_header.entity;

import com.sellertool.server.domain.erp_download_excel_header.dto.ErpDownloadExcelHeaderDetailDto;
import com.sellertool.server.domain.erp_download_excel_header.dto.ErpDownloadExcelHeaderDto;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Table(name = "erp_second_merge_header")
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class ErpDownloadExcelHeaderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Setter
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Setter
    @Column(name = "title")
    private String title;

    @Type(type = "json")
    @Column(name = "header_detail", columnDefinition = "json")
    private ErpDownloadExcelHeaderDetailDto headerDetail;

    @Setter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Setter
    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Setter
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static ErpDownloadExcelHeaderEntity toEntity(ErpDownloadExcelHeaderDto dto) {
        if(dto == null) return null;

        ErpDownloadExcelHeaderEntity entity = ErpDownloadExcelHeaderEntity.builder()
                .cid(dto.getCid())
                .id(dto.getId())
                .title(dto.getTitle())
                .headerDetail(dto.getHeaderDetail())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .updatedAt(dto.getUpdatedAt())
                .build();

        return entity;
    }
}
