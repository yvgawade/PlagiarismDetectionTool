package com.webCrawler.plagiarismDetection.services;

import static java.util.stream.Collectors.toList;

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
public class CrawledSitesService {

}
