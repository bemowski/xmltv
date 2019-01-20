package xmltv.model.xmltv;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.slf4j.Logger;

import net.jmatrix.utils.ClassLogFactory;

public class Channel {
   static final Logger log=ClassLogFactory.getLog();

   String id;
   String displayName;
   
   String stream;
   
   public Channel() {}
   
   public Channel(String id, String displayName) {
      this.id=id;
      this.displayName=displayName;
   }
   
   public Channel(xmltv.model.hdhomerun.Channel c) {
      this.id=c.getId();
      
      // GuideNames all have "DT, as in KTVUDT - but the DT
      // addls little.  remove it here.
      String guideName=c.getGuideName();
      guideName=guideName.replace("DT", "");
      
      String affiliate=c.getAffiliate();
      if (affiliate == null)
         affiliate="";
      else {
         if (affiliate.length() < 3 || affiliate.length() > 4) {
            log.debug("Removing affiliate for "+c.getGuideNumber()+" "+
                  guideName+" - odd size: '"+affiliate);
            affiliate="";
         }
      }
      if (affiliate.length() > 0)
         affiliate=" "+affiliate;
      
      this.displayName=c.getGuideNumber()+" "+guideName+affiliate;
      
      this.stream=c.getUrl();
   }
   
   @XmlAttribute(name="id")
   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }
   
   @XmlElement(name="display-name")
   public String getDisplayName() {
      return displayName;
   }

   public void setDisplayName(String displayName) {
      this.displayName = displayName;
   }

   @XmlElement(name="stream")
   public String getStream() {
      return stream;
   }

   public void setStream(String stream) {
      this.stream = stream;
   }
}
