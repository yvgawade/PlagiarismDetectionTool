package com.webCrawler.plagiarismDetection.model;

import java.time.Instant;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
public class Session {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long sessionId;

  private String title;

  @Lob
  private String text;
  private Instant createdDate;

  public Long getSessionId() {
    return sessionId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Instant getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Instant createdDate) {
    this.createdDate = createdDate;
  }

  public Session(String title, String text, Instant createdDate) {
    this.title = title;
    this.text = text;
    this.createdDate = createdDate;
  }
}
