package com.webCrawler.plagiarismDetection.services;

import com.webCrawler.plagiarismDetection.model.CrawledSites;
import com.webCrawler.plagiarismDetection.model.Session;
import com.webCrawler.plagiarismDetection.repository.CrawledSitesRepository;
import com.webCrawler.plagiarismDetection.repository.SessionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class SessionService {

  @Autowired
  private SessionRepository sessionRepository;

  @Autowired
  private CrawledSitesRepository crawledSitesRepository;

  public void saveCrawledSite(CrawledSites crawledSites) {
    crawledSitesRepository.save(crawledSites);
  }

  @Transactional(readOnly = true)
  public CrawledSites getCrawledSite(Long id) throws Exception {
    CrawledSites crawledSites = crawledSitesRepository.findById(id)
        .orElseThrow(() -> new Exception("crawled url not found for id" + id));
    return crawledSites;
  }



  @Transactional(readOnly = true)
  public List<CrawledSites> getCrawledSitesBySession(Long sessionId) throws Exception {
    Session session = sessionRepository.findBySessionId(sessionId)
        .orElseThrow(() -> new Exception("session not found for id" + sessionId));
    return crawledSitesRepository.findBySession(session);
  }


  public void saveSession(Session session) {
    sessionRepository.save(session);
  }
}
