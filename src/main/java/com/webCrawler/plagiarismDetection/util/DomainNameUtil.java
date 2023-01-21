package com.webCrawler.plagiarismDetection.util;

import java.net.URI;
import java.net.URISyntaxException;

public class DomainNameUtil {

  public  long currentTimeMillis() {
    return System.currentTimeMillis();
  }

  public  String getDomainNameNoException(String url) {
    try {
      return getDomainName(url);
    } catch (URISyntaxException ignored) { }

    return null;
  }

  public  String getDomainName(String url) throws URISyntaxException {
    return getDomainName(new URI(url));
  }

  public  String getDomainName(URI uri) throws URISyntaxException {
    String domain = uri.getHost();
    return domain != null && domain.startsWith("www.") ? domain.substring(4) : domain;
  }

  public  String fixUrl(String domain, String url) {
    if(url.startsWith("//")) {
      //relative protocol
      url = "http:" + url;
    } else if(url.startsWith("/")) {
      //relative url from root
      url = "http://" + domain + (url.startsWith("/") ? url : ("/" + url));
    } else if(!url.contains(".")) {
      //relative url
      url = "http://" + domain + "/" + url;
    } else if (!url.startsWith("http")) {
      //url without protocol
      url = "http://" + url;
    }

    return url;
  }
}
