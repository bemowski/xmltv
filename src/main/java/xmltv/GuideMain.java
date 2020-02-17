package xmltv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.jmatrix.console.log.ColorConsoleConfig;
import net.jmatrix.jproperties.util.ArgParser;
import net.jmatrix.jproperties.util.StreamUtil;
import net.jmatrix.utils.ClassLogFactory;
import net.jmatrix.utils.DebugUtils;
import xmltv.model.hdhomerun.Channel;
import xmltv.model.hdhomerun.Device;
import xmltv.model.hdhomerun.Guide;
import xmltv.model.hdhomerun.Program;
import xmltv.model.xmltv.Programme;
import xmltv.model.xmltv.TV;
import xmltv.util.TrackingInputStream;


/*
 * http://my.hdhomerun.com/api/guide.php?DeviceAuth=fwDHNiBRhOXC2Gzp88gz0TB7&Start=1493162402
 * 
 * Known Parameters:
 *    DeviceAuth - comes from hdhoimerun, rotates daily, REQUIRED
 *    Channel - channel number, guide for single channel
 *    Start - unixtime (seconds since epoch)
 * 
 * Guide returns exactly 4  hours, but can iterate and get multiple 4 hour
 * blocks.  3x4=12   long loadTime=-1;

 *          4*4=16
 *          5*4=20
 * 
 * Run at 6:30 am, and get 5 4 hour blocks, for 20 hour guide.
 * 
 */
public class GuideMain {
   static final Logger log=ClassLogFactory.getLog();
   
   static String HD_GUIDE_URL="http://my.hdhomerun.com/api/guide.php?DeviceAuth=";
   
   public static void main(String args[]) {
      ArgParser ap = new ArgParser(args);
      
      if (ap.getBooleanArg("-v")){
         ColorConsoleConfig.setGlobalLevel(org.slf4j.event.Level.DEBUG);
         log.debug("Debug logging enabled.");
      }
      
      try {
         // -i <url> - the URL to the hdhomerun guide
         // -o <file> - the xml tv file
         // -l <url> the lineup url
         
//         Guide guide=new Guide();
//         
//         String skipfile=ap.getStringArg("--skip");
//         if (skipfile != null) {
//            List<String> skip=readSkip(skipfile);
//            log.warn("Skipping: "+skip);
//            guide.setSkip(skip);
//         }
//         
//         List<Channel> lineup=readGuideChannels(ap.getStringArg("-l"));
//         guide.addChannels(lineup);
//         
//         String guideUrl=ap.getStringArg("-i");
//         if (guideUrl == null) {
//            Device device=Device.load("http://hdhomerun/discover.json");
//            log.debug("Device: "+DebugUtils.jsonDebug(device));
//            guideUrl=HD_GUIDE_URL+URLEncoder.encode(device.getDeviceAuth());
//            
//            log.debug("Guide URl: "+guideUrl);
//            Thread.sleep(2000);
//         }
//         
//         long now=System.currentTimeMillis();
//         
//         int blocks=ap.getIntArg("-b", 1);
//         log.info("Loading "+blocks+" total guide blocks.");
//         
//         for (int i=0; i<blocks; i++) {
//            log.debug("Loading Guide Block "+i);
//            
//            // Start time of a 4 hour block.
//            long start=(now/1000)+(i*4*3600);
//            
//            log.debug("Guide Block Start time: "+new Date(start*1000));
//            
//            String guideTimeUrl=guideUrl+"&Start="+start;
//            log.debug("GuideBlockURL: "+guideTimeUrl);
//            List<Channel> guideChannels=readGuideChannels(guideTimeUrl);
//            
//            log.debug("Found "+guideChannels.size()+" Channels.");
//            
//            guide.addChannels(guideChannels);
//         }
//         
//         TV tv=convert(guide);

        // NOTE: THIS CLASS LEFT FOR REFERENCE, IT DOES NOT WORK
         GuideLoader loader=new GuideLoader(null);
         loader.setGuideUrl(ap.getStringArg("-i"));
         loader.setLineupUrl(ap.getStringArg("-l"));

         TV tv=loader.getTV();
         write(tv, ap.getStringArg("-o", "xmltv.xml"));
         
         
      } catch (Exception ex) {
         log.error("Main Error: ", ex);
         System.exit(2);
      }
   }
   
   static void write(TV tv, String file) throws IOException, JAXBException {
      JAXBContext jaxbContext = JAXBContext.newInstance(TV.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      log.info("Writing "+file);
      
      jaxbMarshaller.marshal(tv, new File(file));
   }
}
