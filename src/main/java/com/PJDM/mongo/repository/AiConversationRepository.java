package com.PJDM.mongo.repository;

import com.PJDM.mongo.document.AiConversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * AI 对话 MongoDB Repository
 *
 * @author Alanine
 */
@Repository
public interface AiConversationRepository extends MongoRepository<AiConversation, String> {

    Optional<AiConversation> findBySessionId(String sessionId);

    Optional<AiConversation> findByTriageRecordId(Long triageRecordId);
}
