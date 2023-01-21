package com.webCrawler.plagiarismDetection.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import org.springframework.lang.Nullable;



@Entity

public class  CrawledSites {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String postName;

  private String url;
  @Lob
  private String text;

  private Integer matchedWordsCount = 0;
  private Double editCost = 1.0;
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "sessionId", referencedColumnName = "sessionId")
  private Session session;


  public CrawledSites(String postName, String url, String text, Integer matchedWordsCount,
      Double editCost, Session session) {
    this.postName = postName;
    this.url = url;
    this.text = text;
    this.matchedWordsCount = matchedWordsCount;
    this.editCost = editCost;
    this.session = session;
  }

  public String getPostName() {
    return postName;
  }

  public void setPostName(String postName) {
    this.postName = postName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Nullable
  public String getText() {
    return text;
  }

  public void setText(@Nullable String text) {
    this.text = text;
  }

  public Integer getMatchedWordsCount() {
    return matchedWordsCount;
  }

  public void setMatchedWordsCount(Integer matchedWordsCount) {
    this.matchedWordsCount = matchedWordsCount;
  }

  public Double getEditCost() {
    return editCost;
  }

  public void setEditCost(Double editCost) {
    this.editCost = editCost;
  }

  public Session getSession() {
    return session;
  }

  public void setSession(Session session) {
    this.session = session;
  }
}
