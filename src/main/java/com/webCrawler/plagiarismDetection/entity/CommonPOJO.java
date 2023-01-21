package com.webCrawler.plagiarismDetection.entity;

public class CommonPOJO<T> {
  public String getSearchText() {
    return SearchText;
  }

  public void setSearchText(String searchText) {
    SearchText = searchText;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  String title;
  String SearchText;
}
