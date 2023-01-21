package com.webCrawler.plagiarismDetection.repository;

import com.webCrawler.plagiarismDetection.model.CrawledSites;
import com.webCrawler.plagiarismDetection.model.Session;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CrawledSitesRepository extends JpaRepository<CrawledSites, Long> {
  List<CrawledSites> findBySession(Session session);
}
