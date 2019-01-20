package xmltv.model.hdhomerun;

import java.util.Date;
import java.util.List;

/*
[
   {
      "GuideNumber" : "1.1",
      "VideoCodec" : "MPEG2",
      "URL" : "http://192.168.20.137:5004/auto/v1.1",
      "GuideName" : "WhatsOn",
      "AudioCodec" : "AC3"
   },

 */
public class Channel {
   String guideNumber;  // guide.json, lineup.json
   String guideName;    // guide.json, lineup.json

   
   String videoCodec;   // lineup.json
   String url;          // lineup.json
   String audioCodec;   // lineup.json
   
   String affiliate;    // guide.json
   
   List<Program> guide;
   
   public Channel() {}
   
   public String toString() {
      return "Channel("+guideNumber+" "+guideName+", url="+url+", programs: "+
            (guide==null?"null":guide.size())+")";
   }
   
   public String getId() {
      return guideNumber+"-"+guideName;
   }
   
   public Date getStartDate() {
      if (guide == null)
         return null;
      long min=Long.MAX_VALUE;
      for (Program p:guide) {
         if (p.getStartTime() < min)
            min=p.getStartTime();
      }
      return new Date(min*1000);
   }
   
   public Date getEndDate() {
      if (guide == null)
         return null;
      long max=0;
      for (Program p:guide) {
         if (p.getEndTime() > max)
            max=p.getEndTime();
      }
      return new Date(max*1000);
   }
   
   public void merge(Channel c) {
      if (guide == null)
         guide=c.guide;
      else
         guide.addAll(c.guide);
      
      if (affiliate == null && c.affiliate != null)
         affiliate=c.affiliate;
   }
   ////////////////////////////////////////////
   public String getGuideNumber() {
      return guideNumber;
   }

   public void setGuideNumber(String guideNumber) {
      this.guideNumber = guideNumber;
   }

   public String getVideoCodec() {
      return videoCodec;
   }

   public void setVideoCodec(String videoCodec) {
      this.videoCodec = videoCodec;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getGuideName() {
      return guideName;
   }

   public void setGuideName(String guideName) {
      this.guideName = guideName;
   }

   public String getAudioCodec() {
      return audioCodec;
   }

   public void setAudioCodec(String audioCodec) {
      this.audioCodec = audioCodec;
   }

   public String getAffiliate() {
      return affiliate;
   }

   public void setAffiliate(String affiliate) {
      this.affiliate = affiliate;
   }

   public List<Program> getGuide() {
      return guide;
   }

   public void setGuide(List<Program> guide) {
      this.guide = guide;
   }
}
