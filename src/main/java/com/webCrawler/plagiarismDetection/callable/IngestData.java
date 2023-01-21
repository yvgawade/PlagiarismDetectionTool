package com.webCrawler.plagiarismDetection.callable;

import static com.webCrawler.plagiarismDetection.util.ProfileConfiguration.getProperties;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.webCrawler.plagiarismDetection.entity.IngestDataPOJO;
import com.webCrawler.plagiarismDetection.model.Session;
import com.webCrawler.plagiarismDetection.repository.SessionRepository;
import com.webCrawler.plagiarismDetection.services.SessionService;
import com.webCrawler.plagiarismDetection.util.DomainNameUtil;
import com.webCrawler.plagiarismDetection.util.HtmlUnitUtil;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class IngestData extends
    ProcessData<IngestDataPOJO> {

  private final Logger log = LoggerFactory.getLogger(this.getClass());
  WebClient webClient;
  DomainNameUtil domainNameUtil;
  IngestDataPOJO ingestDataPOJO;

  Queue<String>[] urlsToCrawl;

  String[] stopWords;
  Set<String> searchWords;

  Session session;
  int crawledUrls;


  private SessionService sessionService;

  Map<String, Integer> visitCountByDomain;
  final private String commonWords = getProperties().getProperty("common-words");
  final private String[] blacklistedDomains = getProperties().getProperty("domain-names").split(",");


  public IngestData() {

  }

  public IngestData(IngestDataPOJO ingestDataPOJO, SessionService sessionService) {
    this.sessionService = sessionService;
    this.ingestDataPOJO = ingestDataPOJO;
    urlsToCrawl = new LinkedList[2];
    urlsToCrawl[0] = new LinkedList<>();
    urlsToCrawl[1] = new LinkedList<>();
    webClient = new HtmlUnitUtil().createWebClient();
    domainNameUtil = new DomainNameUtil();
    visitCountByDomain = new HashMap<>();
    crawledUrls = 0;

  }

  @Override
  public IngestDataPOJO call() throws Exception {

    execute();
    return ingestDataPOJO;
  }

  private void execute() {
    try {
      session = new Session(ingestDataPOJO.getTitle() , ingestDataPOJO.getSearchText(), java.time.Instant.now());
      sessionService.saveSession(session);
      stopWords = Files.readAllLines(Paths.get("stopwords.txt")).toArray(new String[0]);
    } catch (IOException e) {

    }
    getGoogleSearchResults();
    searchWords = removeStopWords(ingestDataPOJO.getSearchText());
    crawl();
    log.info("Session executed");
  }


  public Set<String> removeStopWords(String data) {

    data = data.replaceAll("\\p{Punct}","").toLowerCase();
    ArrayList<String> allWords =
        Stream.of(data.split(" "))
            .collect(Collectors.toCollection(ArrayList<String>::new));
    allWords.removeAll(Arrays.asList(stopWords));
    return new HashSet<>(allWords);
  }

  public void getGoogleSearchResults() {
    try {
      HtmlPage page = webClient.getPage("https://www.google.com/search?q=" + ingestDataPOJO.getTitle().replaceAll(" ", "+"));
      List<HtmlAnchor> anchors = page.getByXPath("//a");
      for (HtmlAnchor anchor : anchors) {
        String link = anchor.getHrefAttribute();
        if (link.startsWith("https"))
          urlsToCrawl[0].offer(link);
      }
    } catch (IOException e) {

    }
  }

  public void crawl() {

    try {
      while (!urlsToCrawl[0].isEmpty() && crawledUrls < 10) {
        crawledUrls++;
        String newUrl = urlsToCrawl[0].poll();
        String domainName = domainNameUtil.getDomainNameNoException(newUrl);
        Boolean isBlackListed = false;
        for (String domain : blacklistedDomains)
          if (domainName.contains(domain))
            isBlackListed = true;
        if (!isBlackListed && visitCountByDomain.getOrDefault(domainName, 0) < 5) {
          visitCountByDomain.put(domainName, visitCountByDomain.getOrDefault(domainName, 0) + 1);
          if (evaluateEditCost(newUrl)) {
            HtmlPage page = webClient.getPage(newUrl);
            List<HtmlAnchor> anchors = page.getByXPath("//a");
            for (HtmlAnchor anchor : anchors) {
              String link = anchor.getHrefAttribute();
              if (link.startsWith("https"))
                urlsToCrawl[1].offer(domainNameUtil.fixUrl(domainName, link));
            }
          }
        }
      }
    } catch (IOException e) {
    }
    if (urlsToCrawl[1].size() > 0) {
      urlsToCrawl[0] = urlsToCrawl[1];
      urlsToCrawl[1] = new LinkedList<>();
      crawl();
    }
  }

  public boolean evaluateEditCost(String url) {
    double editCost = 1;
    int matchedWordsCount = 0;
    try {
      HtmlPage page = webClient.getPage(url);
      log.info(page.getTitleText());
      String pageText = page.asNormalizedText();
      Set<String> words = removeStopWords(pageText);
      for (String str: words)
        if (searchWords.contains(str))
          matchedWordsCount++;
      editCost = editDistance(String.join(" ", words),String.join(" ", searchWords));
    } catch (IOException e) {
      e.printStackTrace();
    }
    log.info("edit cost - " + editCost + ", matched words - " + matchedWordsCount);
    return (matchedWordsCount / (1.0 * searchWords.size())) > 0.25;
  }

  private double editDistance(String str1, String str2) {
    int len1 = str1.length();
    int len2 = str2.length();
    double[][] DP = new double[2][len1 + 1];
    for (int i = 0; i <= len1; i++)
      DP[0][i] = i;
    for (int i = 1; i <= len2; i++) {
      for (int j = 0; j <= len1; j++)
        if (j == 0)
          DP[i % 2][j] = i;
        else if (str1.charAt(j - 1) == str2.charAt(i - 1))
          DP[i % 2][j] = DP[(i - 1) % 2][j - 1];
        else
          DP[i % 2][j] = Math.min(1 + DP[(i - 1) % 2][j], 1 + Math.min(DP[i % 2][j - 1],1 + DP[(i - 1) % 2][j - 1]));
    }
    return (DP[len2 % 2][len1]) / (1.0 * Math.max(len2, len1));
  }
}
