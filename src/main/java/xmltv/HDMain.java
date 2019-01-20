package xmltv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.jmatrix.console.log.ColorConsoleConfig;
import net.jmatrix.jproperties.util.ArgParser;
import net.jmatrix.utils.ClassLogFactory;
import net.jmatrix.utils.DebugUtils;
import xmltv.model.hdhomerun.Channel;
import xmltv.model.hdhomerun.Device;

public class HDMain {
   static final Logger log=ClassLogFactory.getLog();
   
   
   public static void main(String args[]) {
      ArgParser ap = new ArgParser(args);
      
      if (ap.getBooleanArg("-v")){
         ColorConsoleConfig.setGlobalLevel(org.slf4j.event.Level.DEBUG);
         log.debug("Debug logging enabled.");
      }
      log.info("Main.");
      
      String url=ap.getStringArg("-u");
      
      try {
         Device d=Device.load(url+"/discover.json");
         
         log.debug("Device: "+DebugUtils.jsonDebug(d));
         
         List<Channel> lineup=loadLineup(url+"/lineup.json");
         
         log.debug("Found "+lineup.size()+" Channels in the lineup.");
         
         
         String sdir=ap.getStringArg("--strm-out");
         
         File dir=new File(sdir);
         if (!dir.exists()) {
            dir.mkdirs();
         }
         
         for (Channel c:lineup) {
            writeStreamFile(dir, c);
         }
         
         
      } catch (Exception ex) {
         log.error("Main Error: ", ex);
         System.exit(2);
      }
      
   }
   
   

   
   static List<Channel> loadLineup(String surl) throws IOException {
      ObjectMapper om=new ObjectMapper();
      om.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
      om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      URL url=new URL(surl);
      
      List<Channel> lineup=om.readValue(url.openStream(), new TypeReference<List<Channel>>() {});
      
      return lineup;
   }
   
   static void writeStreamFile(File dir, Channel c) throws IOException {
      String filename=c.getGuideNumber()+"-"+c.getGuideName()+".strm";
      File file=new File(dir, filename);
      
      log.debug("Writing: "+file);
      FileWriter fw=new FileWriter(file);
      
      fw.write(c.getUrl()+"\n");
      fw.flush();
      fw.close();
   }
}
