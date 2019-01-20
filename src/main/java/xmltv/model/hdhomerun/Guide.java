package xmltv.model.hdhomerun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import net.jmatrix.utils.ClassLogFactory;

public class Guide {
   static final Logger log=ClassLogFactory.getLog();

   List<Channel> channels;
   Map<String, Channel> channelMap;
   
   long startTime;
   long endTime;
   
   long loadTime;
   
   List<String> skip=null;
   
   
   public Guide() {
      loadTime=System.currentTimeMillis();
   }
   
   public long getAge() {
      return System.currentTimeMillis()-loadTime;
   }
   
   public void addChannels(List<Channel> clist) {
      if (channels == null) {
         channels=new ArrayList<>();
         channelMap=new HashMap<>();
      }
      
      for (Channel c:clist) {
         addChannel(c);
      }
   }
   
   private void addChannel(Channel c) {
      String cnum=c.getGuideNumber();
      
      if (skip(cnum)) {
         log.debug("Skipping "+c);
         return;
      }
      
      Channel existing=channelMap.get(cnum);
      if (existing == null){
         log.debug("Adding "+c);
         channelMap.put(cnum, c);
         channels.add(c);
      } else {
         log.debug("Merging "+c+" => "+existing);
         existing.merge(c);
      }
   }
   
   boolean skip(String cnum) {
      if (skip == null) return false;
      
      for (String s:skip) {
         if (cnum.matches(s)) {
            return true;
         }
      }
      return false;
   }

   public List<Channel> getChannels() {
      return channels;
   }

   public void setChannels(List<Channel> channels) {
      this.channels = channels;
   }

   public Map<String, Channel> getChannelMap() {
      return channelMap;
   }

   public void setChannelMap(Map<String, Channel> channelMap) {
      this.channelMap = channelMap;
   }

   public long getStartTime() {
      return startTime;
   }

   public void setStartTime(long startTime) {
      this.startTime = startTime;
   }

   public long getEndTime() {
      return endTime;
   }

   public void setEndTime(long endTime) {
      this.endTime = endTime;
   }

   public long getLoadTime() {
      return loadTime;
   }

   public void setLoadTime(long loadTime) {
      this.loadTime = loadTime;
   }

   public List<String> getSkip() {
      return skip;
   }

   public void setSkip(List<String> skip) {
      this.skip = skip;
   }
}
