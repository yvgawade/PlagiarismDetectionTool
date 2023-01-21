package com.webCrawler.plagiarismDetection.util;

import java.io.InputStream;
import java.util.Properties;

public class ProfileConfiguration {
  private static Properties props = null;

  public static Properties getProperties() {

    if (props == null) {
      props = new Properties();
      InputStream is = null;
      try {
        is = ProfileConfiguration.class.getResourceAsStream("/application.properties");
        props.load(is);
        } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return props;
  }
}
