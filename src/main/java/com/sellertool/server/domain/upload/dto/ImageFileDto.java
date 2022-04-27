package com.sellertool.server.domain.upload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageFileDto {
    private UUID id;
    private String fileOriginName;
    private String fileName;
    private String fileStorageUri;
    private String filePath;
    private String fileExtension;
    private LocalDateTime madeAt;
    private Long size;
}
