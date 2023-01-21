package com.webCrawler.plagiarismDetection.repository;

import com.webCrawler.plagiarismDetection.model.Session;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

  Optional<Session> findBySessionId(Long sessionId);
}
