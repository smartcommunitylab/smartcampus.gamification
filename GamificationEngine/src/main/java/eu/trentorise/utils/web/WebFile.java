package eu.trentorise.utils.web;

import java.net.URL;
import java.util.StringTokenizer;

/**
 *
 * @author Luca Piras
 */
public class WebFile {
    
    public static final String WEB_PATH_DELIMITER = "/";
    
    protected String fileName;
    
    protected String fileContent;

    protected URL url;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        if (null != fileName) {
            StringTokenizer st = new StringTokenizer(fileName, 
                                                     WEB_PATH_DELIMITER);
            
            //rendiamo solo il nome del file, privo di un suo eventuale path
            while (st.hasMoreTokens()) {
                fileName = st.nextToken();
            }
        }
        
        this.fileName = fileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }
    
    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WebFile{" + "fileName=" + fileName + ", url=" + url + '}';
    }
}