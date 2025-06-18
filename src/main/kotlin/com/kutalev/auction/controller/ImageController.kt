package com.kutalev.auction.controller

import com.kutalev.auction.service.MinioService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/images")
class ImageController(private val minioService: MinioService) {

    @PostMapping("/upload")
    fun upload(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        val filename = minioService.upload(file)
        return ResponseEntity.ok(filename)
    }

    @GetMapping("/{filename}")
    fun download(@PathVariable filename: String): ResponseEntity<ByteArray> {
        val inputStream = minioService.download(filename)
        val bytes = inputStream.readAllBytes()
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(bytes)
    }
} 