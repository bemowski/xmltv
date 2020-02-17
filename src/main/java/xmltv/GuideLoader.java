package xmltv;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jmatrix.utils.ClassLogFactory;
import net.jmatrix.utils.DebugUtils;
import net.jmatrix.utils.StreamUtil;
import org.slf4j.Logger;
import xmltv.model.Config;
import xmltv.model.hdhomerun.Channel;
import xmltv.model.hdhomerun.Device;
import xmltv.model.hdhomerun.Guide;
import xmltv.model.hdhomerun.Program;
import xmltv.model.xmltv.Programme;
import xmltv.model.xmltv.TV;
import xmltv.util.TrackingInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * 
 * lineupUrl=hdhomerunRootUrl+"/lineup.json"
 * deviceUrl=hdhomerunRootUrl+"/discover.json"
 * guideUrl=HD_GUID_URL+device.getAuthCode()
 * 
 * @author bemo
 *
 */
public class GuideLoader {
   static final Logger log=ClassLogFactory.getLog();

   Config config;

   static String HD_GUIDE_URL="http://my.hdhomerun.com/api/guide.php?DeviceAuth=";
   
   // These may be set for testing, or derived for real stuff
   String lineupUrl;
   String deviceUrl;
   String guideUrl;


   long cacheTime=3600*1000; // cache guide 1 hour
   
   Guide guide=null;
   
   public GuideLoader(Config config) {
     this.config=config;
   }
   
   public Guide getGuide() throws IOException {
      if (guide != null && guide.getAge() < cacheTime) {
         log.debug("Returning cached copy of guide.");
         return guide;
      }
      
      guide=loadGuide();
      return guide;
   }
   
   public TV getTV()throws IOException {
      return convert(getGuide());
   }
   
   private Guide loadGuide() throws IOException {
      Guide guide=new Guide();

     log.info("Skipping: "+config.getSkip());
     guide.setSkip(config.getSkip());

      List<Channel> lineup=readGuideChannels(config.getHdhomerunUrl()+"/lineup.json");
      guide.addChannels(lineup);

      Device device=Device.load(getDeviceUrl());
      
      log.debug("Device: "+DebugUtils.jsonDebug(device));
      String url=getGuideUrl(device.getDeviceAuth());
      
      log.debug("Guide URl: "+url);
      //Thread.sleep(2000);
      
//      long now=System.currentTimeMillis();
//      
//      int blocks=ap.getIntArg("-b", 1);
//      log.info("Loading "+blocks+" total guide blocks.");
//      
//      for (int i=0; i<blocks; i++) {
//         log.debug("Loading Guide Block "+i);
//         
//         // Start time of a 4 hour block.
//         long start=(now/1000)+(i*4*3600);
//         
//         log.debug("Guide Block Start time: "+new Date(start*1000));
//         
//         String guideTimeUrl=guideUrl+"&Start="+start;
//         log.debug("GuideBlockURL: "+guideTimeUrl);
//         List<Channel> guideChannels=readGuideChannels(guideTimeUrl);
//         
//         log.debug("Found "+guideChannels.size()+" Channels.");
//         
//         guide.addChannels(guideChannels);
//      }
      List<Channel> guideChannels=readGuideChannels(url);
    
      log.debug("Found "+guideChannels.size()+" Channels.");
    
      guide.addChannels(guideChannels);
      
      
      return guide;
   }
   
   public String getXmlTV() throws JAXBException, IOException {
      JAXBContext jaxbContext = JAXBContext.newInstance(TV.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      ByteArrayOutputStream baos=new ByteArrayOutputStream();
      
      jaxbMarshaller.marshal(getTV(), baos);
      
      return baos.toString();
   }
   
   
   static TV convert(Guide guide) {
      List<Channel> channels=guide.getChannels();
      
      TV tv=new TV();
      String s=new java.lang.String();
      
      for (Channel c:channels) {
         //log.debug("Procesing channel: "+DebugUtils.jsonDebug(c));
         
         log.debug(c.toString());
         log.debug("         time range: "+c.getStartDate()+"  --  "+c.getEndDate());
         
         tv.addChannel(new xmltv.model.xmltv.Channel(c));
         
         List<Program> programs=c.getGuide();
         if (programs == null) {
            log.warn("Null programs: "+c);
         } else {
            for (Program p:programs) {
               if (p.getTitle() == null)
                  log.warn("Title is null: "+p);
               
               tv.addProgramme(new Programme(c, p));
            }
         }
      }
      
      return tv;
   }
   
   static List<Channel> readLineupChannels(String surl) throws IOException {
      ObjectMapper om=new ObjectMapper();
      om.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
      om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      URL url=new URL(surl);
      
      log.debug("Loading lineup: "+surl);
      
      URLConnection con=url.openConnection();
      
      if (con instanceof HttpURLConnection) {
         HttpURLConnection hcon=(HttpURLConnection)con;
         log.debug("Response code: "+hcon.getResponseCode());
         log.debug("Content length: "+hcon.getContentLength());
      }
      
      InputStream is=con.getInputStream();
      TrackingInputStream tis=new TrackingInputStream(is);
      
      List<Channel> lineup=om.readValue(tis, new TypeReference<List<Channel>>() {});
      
      log.info("Lineup loaded "+tis.getByteCount()+" bytes.");
      
      return lineup;
   }
   

   static List<Channel> readGuideChannels(String surl) throws IOException {
      ObjectMapper om=new ObjectMapper();
      om.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
      om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      URL url=new URL(surl);
      
      log.debug("Loading guide: "+surl);
      
      URLConnection con=url.openConnection();
      
      if (con instanceof HttpURLConnection) {
         HttpURLConnection hcon=(HttpURLConnection)con;
         log.debug("Response code: "+hcon.getResponseCode());
         log.debug("Content length: "+hcon.getContentLength());
      }
      
      InputStream is=con.getInputStream();
      TrackingInputStream tis=new TrackingInputStream(is);
      
      
      ByteArrayOutputStream baos=new ByteArrayOutputStream();
      StreamUtil.pump(tis, baos);
      
      log.info("Guide loaded "+tis.getByteCount()+" bytes.");
      
      
      List<Channel> guide=om.readValue(new ByteArrayInputStream(baos.toByteArray()), 
            new TypeReference<List<Channel>>() {});
      
      
      if (guide == null) {
         log.warn("Raw input from URL: \n\n"+new String(baos.toByteArray()));
         
         throw new NullPointerException("Guide is null after reading from "+surl);
      }
      
      return guide;
   }
   
   ///////////////////////////////////////////////////////////////////////////////////////////

   public String getLineupUrl() {
      if (lineupUrl == null)
         return config.getHdhomerunUrl()+"/lineup.json";
      return lineupUrl;
   }

   public void setLineupUrl(String lineupUrl) {
      this.lineupUrl = lineupUrl;
   }

   public String getDeviceUrl() {
      if (deviceUrl == null)
         return config.getHdhomerunUrl()+"/discover.json";
      return deviceUrl;
   }

   public void setDeviceUrl(String deviceUrl) {
      this.deviceUrl = deviceUrl;
   }

   public String getGuideUrl(String authCode) {
      if (guideUrl == null) 
         return HD_GUIDE_URL+authCode;
      
      return guideUrl;
   }

   public void setGuideUrl(String guideUrl) {
      this.guideUrl = guideUrl;
   }
}
