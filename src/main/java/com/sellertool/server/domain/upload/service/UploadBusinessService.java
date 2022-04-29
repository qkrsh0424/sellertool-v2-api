package com.sellertool.server.domain.upload.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.upload.dto.ImageFileDto;
import com.sellertool.server.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
//TODO : 이미지 파일 업로드 리팩터링 해야됨.
public class UploadBusinessService {
    private final String IMAGE_DIR = "/images";

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String ACCESS_KEY;

    @Value("${cloud.aws.credentials.secretKey}")
    private String SECRET_KEY;

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET;

    @Value("${cloud.aws.region.static}")
    private String REGION;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.ACCESS_KEY, this.SECRET_KEY);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.REGION)
                .build();
    }

    public ImageFileDto uploadImageToS3(MultipartFile file) {
        ImageFileDto fileS3GetDto = null;

        String bucketPath = this.BUCKET + IMAGE_DIR;
        String fileOriginName = file.getOriginalFilename();
        String fileExtension = getFileExt(fileOriginName);
        Long fileSize = file.getSize();

        if (!isPermissionImageFileExt(fileOriginName)) {
            throw new NotMatchedFormatException("Not supported file extension.");
        }

        String fileName = getFileName(fileOriginName);
        String fileUrl = "https://" + bucketPath + "/" + fileName;

        if (this.postFile2S3(bucketPath, fileName, file)) {
            fileS3GetDto = ImageFileDto.builder()
                    .id(UUID.randomUUID())
                    .fileExtension(fileExtension)
                    .fileOriginName(fileOriginName)
                    .fileStorageUri(fileUrl)
                    .filePath(IMAGE_DIR)
                    .fileName(fileName)
                    .madeAt(DateTimeUtils.getCurrentDateTime())
                    .size(fileSize)
                    .build()
            ;
        }

        return fileS3GetDto;
    }

    public List<ImageFileDto> uploadImagesToS3(MultipartFile[] files) {
        List<ImageFileDto> fileS3GetDtos = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            fileS3GetDtos.add(this.uploadImageToS3(files[i]));
        }
        return fileS3GetDtos;
    }

    private String getFileName(String fileFullName) {
        String fileRootName = "ViewDay_";
        int pos = fileFullName.lastIndexOf("."); // 파일의 마지막 . 인덱스를 기준으로 자른다.
        String ext = fileFullName.substring(pos + 1); // 마지막 . 인덱스를 기준으로 뒷쪽 텍스트를 가져온다.


        String fileName = String.valueOf(fileRootName + DateTimeUtils.getCurrentDate2yyyyMMddHHmmss_SSS()) + "_"
                + ((int) (Math.random() * 99999) + 10000) + "." + ext; // 최종 파일 네임 : {현재 시간}-{랜덤값}.{확장자명}
        return fileName;
    }

    private Boolean postFile2S3(String bucketPath, String fileName, MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata(); // 에러코드 피하기 위함 : No content length specified for stream data.
        // Stream contents will be buffered in memory and could result
        // in out of memory errors.
        metadata.setContentLength(file.getSize()); // 위의 에러코드를 피하기 위해서는 contentLength를 지정해줘야 한다.
        metadata.setContentType(file.getContentType());

        try {
            s3Client.putObject(new PutObjectRequest(bucketPath, fileName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.error("==ERROR UploadFileService : postFile2S3 => {}.==", "maybe cause by file.getInputStream, S3 upload failed");
            return false;
        }
        return true;
    }

    private String getFileExt(String fileName) {
        String[] fileNameArr = fileName.split("\\.");

        if (fileNameArr.length <= 0) {
            throw new IllegalArgumentException("Not defined file extension.");
        }

        String ext = fileNameArr[fileNameArr.length - 1].toUpperCase();
        return ext;
    }

    private boolean isPermissionFileExt(String fileName) {

        final String[] PERMISSION_FILE_EXT_ARR = {"GIF", "JPEG", "JPG", "PNG", "APNG", "AVIF", "SVG", "WebP"};

        if (!StringUtils.hasText(fileName)) {
            return false;
        }

        String[] fileNameArr = fileName.split("\\.");

        if (fileNameArr.length == 0) {
            return false;
        }

        String ext = fileNameArr[fileNameArr.length - 1].toUpperCase();

        boolean isPermissionFileExt = false;

        for (int i = 0; i < PERMISSION_FILE_EXT_ARR.length; i++) {
            if (PERMISSION_FILE_EXT_ARR[i].equals(ext)) {
                isPermissionFileExt = true;
                break;
            }
        }

        return isPermissionFileExt;

    }

    private boolean isPermissionImageFileExt(String fileName) {

        final String[] PERMISSION_FILE_EXT_ARR = {"GIF", "JPEG", "JPG", "PNG", "APNG", "AVIF", "SVG", "WebP"};

        if (!StringUtils.hasText(fileName)) {
            return false;
        }

        String[] fileNameArr = fileName.split("\\.");

        if (fileNameArr.length == 0) {
            return false;
        }

        String ext = fileNameArr[fileNameArr.length - 1].toUpperCase();

        boolean isPermissionFileExt = false;

        for (int i = 0; i < PERMISSION_FILE_EXT_ARR.length; i++) {
            if (PERMISSION_FILE_EXT_ARR[i].equals(ext)) {
                isPermissionFileExt = true;
                break;
            }
        }

        return isPermissionFileExt;

    }
}
