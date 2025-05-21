package com.kutalev.auction.service

import com.kutalev.auction.model.AuctionStatus
import com.kutalev.auction.repository.AuctionRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@Service
class AuctionStatusUpdateService(
    private val auctionRepository: AuctionRepository
) {

    // Запускать каждую минуту
    @Scheduled(fixedRate = 60000)
    @Transactional
    fun updateAuctionStatuses() {
        logger.info("Начало обновления статусов аукционов")
        val now = LocalDateTime.now()

        // Обновляем аукционы из PENDING в ACTIVE
        val pendingAuctions = auctionRepository.findByStatus(AuctionStatus.PENDING)
        pendingAuctions.forEach {
            if (it.startTime.isBefore(now) || it.startTime.isEqual(now)) {
                it.status = AuctionStatus.ACTIVE
                auctionRepository.save(it)
                logger.info("Аукцион ${it.auctionId} переведен в статус ACTIVE")
            }
        }

        // Обновляем аукционы из ACTIVE в ENDED
        val activeAuctions = auctionRepository.findByStatus(AuctionStatus.ACTIVE)
        activeAuctions.forEach {
            if (it.endTime.isBefore(now) || it.endTime.isEqual(now)) {
                it.status = AuctionStatus.ENDED
                auctionRepository.save(it)
                logger.info("Аукцион ${it.auctionId} переведен в статус ENDED")
            }
        }
        logger.info("Завершение обновления статусов аукционов")
    }
} 