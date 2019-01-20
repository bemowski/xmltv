package xmltv.model.xmltv;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import xmltv.model.hdhomerun.Program;

public class Programme {
   long start;
   long stop;
   String channelId;
   LangString title;
   String subtitle;
   LangString description;
   
   static DateFormat XMLTV_DATE_FORMAT=new SimpleDateFormat("yyyyMMddHHmmss ZZZZZ");
   
   static DateFormat DISPLAY_FORMAT=new SimpleDateFormat("dd MMM yyyy");
   
   public Programme() {}
   
   public Programme(xmltv.model.hdhomerun.Channel c, Program p) {
      this.start=p.getStartTime()*1000;
      this.stop=p.getEndTime()*1000;
      this.channelId=c.getId();
      

      this.title=new LangString(p.getTitle());
      
      if (p.getSynopsis() != null)
         this.description=new LangString(p.getSynopsis());
      else 
         this.description=new LangString("none");
      
      if (p.getOriginalAirDate() > 0) {
         description=new LangString(description.getValue()+"\n"+
               "Original Air: "+
               DISPLAY_FORMAT.format(new Date(p.getOriginalAirDate()*1000)));
      }
   }
   
   //start="20170424100000 -0700" stop="20170424133000 -0700"
   // 2017 04 24 10.00.00
   // 2017 04 24 13.30.00
   // yyyy MM dd HH mm ss
   
   @XmlAttribute(name="start")
   public String getFormattedStart() {
      return XMLTV_DATE_FORMAT.format(new Date(start));
   }
   
   @XmlAttribute(name="stop")
   public String getFormattedStop() {
      return XMLTV_DATE_FORMAT.format(new Date(stop));
   }
   
   /////////////////////////////////////////////////////
   @XmlTransient
   public long getStart() {
      return start;
   }

   public void setStart(long start) {
      this.start = start;
   }
   
   @XmlTransient
   public long getStop() {
      return stop;
   }

   public void setStop(long stop) {
      this.stop = stop;
   }
   
   @XmlAttribute(name="channel")
   public String getChannelId() {
      return channelId;
   }

   public void setChannelId(String channelId) {
      this.channelId = channelId;
   }
   
   @XmlElement(name="title")
   public LangString getTitle() {
      return title;
   }

   public void setTitle(LangString title) {
      this.title = title;
   }

   public String getSubtitle() {
      return subtitle;
   }

   public void setSubtitle(String subtitle) {
      this.subtitle = subtitle;
   }
   
   @XmlElement(name="desc")
   public LangString getDescription() {
      return description;
   }

   public void setDescription(LangString description) {
      this.description = description;
   }
}
