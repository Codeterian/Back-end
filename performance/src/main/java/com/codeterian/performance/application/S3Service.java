package com.codeterian.performance.application;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	// 파일 업로드 메서드
	public String uploadFile(MultipartFile file) throws IOException {
		// 고유한 파일 이름을 위해 UUID를 사용 (S3에 저장될 파일명)
		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

		// S3에 업로드할 파일 메타데이터 설정
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());

		// 파일을 S3에 업로드
		amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);

		// S3에 저장된 파일의 URL 반환
		return amazonS3.getUrl(bucketName, fileName).toString();
	}

	// 파일 삭제 메서드
	public void deleteFile(String fileUrl) {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		amazonS3.deleteObject(bucketName, fileName);
	}
}
