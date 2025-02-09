package site.haruhana.www.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class S3Util implements FileStorage {

    @Value("${aws.s3.bucket}")
    private String bucket;

    private final S3Client s3;

    public String upload(MultipartFile file) {
        return upload(file, null);
    }

    public String upload(MultipartFile file, String directory) {
        if (file.isEmpty() || Objects.isNull(file.getOriginalFilename())) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        String newFilename = Objects.isNull(directory)
            ? System.currentTimeMillis() + "_" + originalFilename
            : directory + "/" + System.currentTimeMillis() + "_" + originalFilename;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(newFilename)
                    .contentType(file.getContentType())
                    .build();

            s3.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return String.format("https://%s.s3.amazonaws.com/%s", bucket, newFilename);

        } catch (S3Exception e) {
            throw new IllegalStateException("AWS S3 error: " + e.getMessage());

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to upload file: " + e.getMessage());
        }
    }
}