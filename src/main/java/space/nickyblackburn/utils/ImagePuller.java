/***
 * this class is for pulling images for the background of my minecraft client 
 */

package space.nickyblackburn.utils;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImagePuller {
    
    // grabs elements from jsoup
    public void getElementOfImage(String url) throws IOException{
        
        Document doc = Jsoup.connect(url).get();
        
        Consts.dbg(doc.title());

        // grabs all the elements names
        Elements newsHeadlines = doc.select("image-placeholder");
        for (Element headline : newsHeadlines) {
            Consts.dbg(headline.data());
        }
    }
}
