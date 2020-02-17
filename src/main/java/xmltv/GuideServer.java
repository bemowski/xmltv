package xmltv;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.rmi.activation.ActivationGroup_Stub;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import net.jmatrix.console.log.ColorConsoleConfig;
import net.jmatrix.jproperties.util.ArgParser;
import net.jmatrix.utils.ClassLogFactory;
import xmltv.model.Config;


public class GuideServer {
  static final Logger log = ClassLogFactory.getLog();

  static GuideLoader loader = null;

  public static void main(String[] args) throws Exception {
    ArgParser ap = new ArgParser(args);

    ColorConsoleConfig.setGlobalLevel(org.slf4j.event.Level.DEBUG);
    ColorConsoleConfig.setAsJavaUtilLoggingHandler();

    if (ap.getStringArg("-c") == null) {
      System.out.println("Usage: main -c <config file>\n"+
              "  config file (json format) is required.");
      System.exit(1);
    }

    // load config.
    Config config=loadConfig(ap.getStringArg("-c"));

    loader = new GuideLoader(config);
    loader.setGuideUrl(ap.getStringArg("-i"));
    loader.setLineupUrl(ap.getStringArg("-l"));

    // this fails at bootstrap time... because it comes up before the network.
    //loader.getXmlTV(); // this will fail-fast if something doesn't work.

    int port = 8000;
    log.debug("Starting guide server on port " + port);

    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/guide", new GuideHandler());
    server.start();

    log.debug("Guide available on http://localhost:" + port + "/guide");
  }

  private static Config loadConfig(String c) throws Exception {
    JsonFactory factory = new JsonFactory();
    factory.enable(JsonParser.Feature.ALLOW_COMMENTS);

    ObjectMapper om = new ObjectMapper(factory);
    om.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    File f = new File(c);

    Config config=om.readValue(f, Config.class);
    return config;
  }

  static class GuideHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
      URI uri = t.getRequestURI();

      log.debug("Request: " + t.getRequestMethod() + " " + uri);

      try {
        String xmltvXml = loader.getXmlTV();

        byte b[] = xmltvXml.getBytes(Charset.forName("utf-8"));

        t.getResponseHeaders().add("Content-Type", "application/xml; charset=UTF-8");


        log.debug("xmltv length: " + xmltvXml.length() + ", byte array " +
                xmltvXml.getBytes().length);

        if (t.getRequestMethod().equals("HEAD")) {
          t.sendResponseHeaders(200, b.length);

          t.close();
        } else {

          t.getResponseHeaders().add("Content-Length", "" + b.length);
          t.sendResponseHeaders(200, b.length);
          OutputStream os = t.getResponseBody();
          os.write(b);
          os.flush();
          os.close();
          t.close();
        }

      } catch (Throwable ex) {
        log.error("Error loading guide", ex);
        throw new IOException("Exception loading guide", ex);
      }
    }
  }
}
