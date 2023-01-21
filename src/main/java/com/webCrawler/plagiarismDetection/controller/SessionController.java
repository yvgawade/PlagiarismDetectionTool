package com.webCrawler.plagiarismDetection.controller;

import com.webCrawler.plagiarismDetection.callable.IngestData;
import com.webCrawler.plagiarismDetection.callable.ProcessData;
import com.webCrawler.plagiarismDetection.entity.IngestDataPOJO;
import com.webCrawler.plagiarismDetection.services.CrawledSitesService;
import com.webCrawler.plagiarismDetection.services.SessionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/session")
public class SessionController {

  @Autowired
  SessionService sessionService;
  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @RequestMapping(method = RequestMethod.POST)
  public String process(@RequestBody String json) {

    final IngestDataPOJO ingestDataPOJO = new IngestDataPOJO();
    ingestDataPOJO.setTitle("fortification");
    ingestDataPOJO.setSearchText("Food fortification or enrichment is the process of adding micronutrients to food. It can be carried out by food manufacturers, or by governments as a public health policy which aims to reduce the number of people with dietary deficiencies within a population.");
    final ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(new Runnable() {
      @Override
      public void run() {
        execute(ingestDataPOJO);
      }
    });
    executor.shutdown();
    return "";
  }

  @Async
  protected IngestDataPOJO execute(final IngestDataPOJO ingestDataPOJO) {
    try {
      ExecutorService executorService = Executors.newFixedThreadPool(1);
      Future<?> futureDataSet = processDataSet(executorService, ingestDataPOJO);
      manageDatasetResults(futureDataSet);
      executorService.shutdown();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ingestDataPOJO;
  }

  protected Future<?> processDataSet(ExecutorService executorService, IngestDataPOJO ingestDataPOJO) {
    Future<?> futureDataSet = null;
    try {
      ProcessData<IngestDataPOJO> processData = new IngestData(ingestDataPOJO, sessionService);
      futureDataSet = executorService.submit(processData);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return futureDataSet;
  }

  protected void manageDatasetResults(Future<?> futureDataSet) {
    if (futureDataSet != null) {
      try {
        IngestDataPOJO pojo = (IngestDataPOJO) futureDataSet.get();
        log.info("session controller done");
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
  }
}
