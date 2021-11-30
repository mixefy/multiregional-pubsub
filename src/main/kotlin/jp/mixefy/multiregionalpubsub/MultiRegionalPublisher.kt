package jp.mixefy.multiregionalpubsub

import com.google.cloud.pubsub.v1.Publisher
import com.google.protobuf.ByteString
import com.google.pubsub.v1.PubsubMessage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

interface IMultiRegionalPublisher {

    fun publishToMultipleRegions(message: String)
}

@Service
class MultiRegionalPublisher(private val publisher: List<Publisher>) : IMultiRegionalPublisher {

    override fun publishToMultipleRegions(message: String) {
        log.info("#### Handling message: '{}'", message)
        val pubsubMessage = PubsubMessage.newBuilder().setData(ByteString.copyFromUtf8(message)).build()

        val futures = publisher.map {
            log.info("#### Publishing to '{}'", it.topicName)
            it.publish(pubsubMessage)
        }

        val results = futures.map { apiFuture ->
            apiFuture.runCatching { apiFuture.get() }
                .onSuccess { messageId -> log.info("#### Successfully published message: messageId='{}'", messageId) }
                .onFailure { throwable -> log.info("#### Failed to publish message with error: '{}'", throwable) }
        }

        log.info(
            "#### Success/Fail: {}/{} (total: {})",
            results.count { it.isSuccess },
            results.count { it.isFailure },
            results.count()
        )
    }

    companion object {
        private val log = LoggerFactory.getLogger(MultiRegionalPublisher::class.java)
    }
}
