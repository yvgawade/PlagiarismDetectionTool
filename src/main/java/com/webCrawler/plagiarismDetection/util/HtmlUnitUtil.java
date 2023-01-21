package com.webCrawler.plagiarismDetection.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;


/**
 * Created by Vladi
 * Date: 9/19/2016
 * Time: 12:00 AM
 * Copyright VMSR
 */
public class HtmlUnitUtil {
    public BrowserVersion getBrowserVersion() {
        return BrowserVersion.getDefault();
    }

    /**
     * Creates web client for each crawling URL
     */
    public WebClient createWebClient() {

        final WebClient webClient = new WebClient(getBrowserVersion());

        webClient.setJavaScriptErrorListener(null);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.setJavaScriptTimeout(500);
        webClient.waitForBackgroundJavaScript(490);
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        WebClientOptions options = webClient.getOptions();

        options.setRedirectEnabled(true);
        options.setJavaScriptEnabled(true);
        options.setCssEnabled(true);
        options.setUseInsecureSSL(true);

        options.setThrowExceptionOnScriptError(false);
        options.setThrowExceptionOnFailingStatusCode(false);
        options.setPopupBlockerEnabled(false);
        options.setPrintContentOnFailingStatusCode(false);

        return webClient;
    }
}
