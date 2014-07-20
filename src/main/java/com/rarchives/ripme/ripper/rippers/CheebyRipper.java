package com.rarchives.ripme.ripper.rippers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.rarchives.ripme.ripper.AbstractHTMLRipper;
import com.rarchives.ripme.utils.Http;

public class CheebyRipper extends AbstractHTMLRipper {

    public CheebyRipper(URL url) throws IOException {
        super(url);
    }

    @Override
    public String getHost() {
        return "cheeby";
    }
    @Override
    public String getDomain() {
        return "cheeby.com";
    }

    @Override
    public String getGID(URL url) throws MalformedURLException {
        Pattern p = Pattern.compile("^https?://[w.]*cheeby.com/u/([a-zA-Z0-9\\-_]{3,}).*$");
        Matcher m = p.matcher(url.toExternalForm());
        if (m.matches()) {
            return m.group(1);
        }
        throw new MalformedURLException("cheeby user not found in " + url + ", expected http://cheeby.com/u/username");
    }

    @Override
    public URL sanitizeURL(URL url) throws MalformedURLException {
        return new URL("http://cheeby.com/u/" + getGID(url) + "/pics");
    }

    @Override
    public Document getFirstPage() throws IOException {
        return Http.url(this.url)
                   .get();
    }

    @Override
    public List<String> getURLsFromPage(Document page) {
        List<String> imageURLs = new ArrayList<String>();
        for (Element image : page.select("div.i a img")) {
            String imageURL = image.attr("src");
            imageURL = imageURL.replace("s.", ".");
            imageURLs.add(imageURL);
        }
        return imageURLs;
    }
    
    @Override
    public void downloadURL(URL url, int index) {
        addURLToDownload(url, getPrefix(index));
    }
}
