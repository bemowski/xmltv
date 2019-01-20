package xmltv;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;

import org.slf4j.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import net.jmatrix.console.log.ColorConsoleConfig;
import net.jmatrix.jproperties.util.ArgParser;
import net.jmatrix.utils.ClassLogFactory;


public class GuideServer {
   static final Logger log = ClassLogFactory.getLog();
   
   static GuideLoader loader=null;
   
   public static void main(String[] args) throws Exception {
      ArgParser ap=new ArgParser(args);
      
      ColorConsoleConfig.setGlobalLevel(org.slf4j.event.Level.DEBUG);
      ColorConsoleConfig.setAsJavaUtilLoggingHandler();
      
      loader=new GuideLoader();
      loader.setGuideUrl(ap.getStringArg("-i"));
      loader.setLineupUrl(ap.getStringArg("-l"));
      loader.setSkipFile(ap.getStringArg("--skip"));
      
      loader.setHdhomerunRootUrl("http://hdhomerun");
      
      int port=8000;
      log.debug("Starting guide server on port "+port);
      
      HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
      server.createContext("/guide", new GuideHandler());
      server.start();
   }

   static class GuideHandler implements HttpHandler {
      @Override
      public void handle(HttpExchange t) throws IOException {
         URI uri = t.getRequestURI();
         
         log.debug("Request: "+t.getRequestMethod()+" "+uri);
         
         try {
            String xmltvXml=loader.getXmlTV();
            
            byte b[]=xmltvXml.getBytes(Charset.forName("utf-8"));
            
            t.getResponseHeaders().add("Content-Type", "application/xml; charset=UTF-8");
            
            
            log.debug("xmltv length: "+xmltvXml.length()+", byte array "+
                  xmltvXml.getBytes().length);
            
            if (t.getRequestMethod().equals("HEAD")) {
               t.sendResponseHeaders(200, b.length);

               t.close();
            } else {
               
               t.getResponseHeaders().add("Content-Length", ""+b.length);
               t.sendResponseHeaders(200, b.length);
               OutputStream os = t.getResponseBody();
               os.write(b);
               os.flush();
               os.close();
            }

         } catch (Exception ex) {
            log.error("Error loading guide", ex);
            throw new IOException("Exception loading guide", ex);
         }
      }
   }
}
