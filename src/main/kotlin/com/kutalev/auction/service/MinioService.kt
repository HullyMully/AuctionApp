package com.kutalev.auction.service

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.GetObjectArgs
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@Service
class MinioService(
    @Value("\${minio.url}") private val minioUrl: String,
    @Value("\${minio.access-key}") private val accessKey: String,
    @Value("\${minio.secret-key}") private val secretKey: String,
    @Value("\${minio.bucket}") private val bucket: String
) {
    private val client: MinioClient = MinioClient.builder()
        .endpoint(minioUrl)
        .credentials(accessKey, secretKey)
        .build()

    init {
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build())
        }
    }

    fun upload(file: MultipartFile): String {
        client.putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .`object`(file.originalFilename ?: file.name)
                .stream(file.inputStream, file.size, -1)
                .contentType(file.contentType)
                .build()
        )
        return file.originalFilename ?: file.name
    }

    fun download(filename: String): InputStream {
        return client.getObject(
            GetObjectArgs.builder()
                .bucket(bucket)
                .`object`(filename)
                .build()
        )
    }
} 