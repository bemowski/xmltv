package xmltv.model.xmltv;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="tv")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TV {
   List<Channel> channels;
   List<Programme> programmes;
   
   
   public TV() {}
   
   public void addChannel(Channel c) {
      if (channels == null)
         channels=new ArrayList<Channel>();
      channels.add(c);
   }
   
   public void addProgramme(Programme p) {
      if (programmes == null)
         programmes=new ArrayList<Programme>();
      programmes.add(p);
   }
   ////////////////////////////////////////////////////////////////////////////////
   @XmlElement(name="channel")
   public List<Channel> getChannels() {
      return channels;
   }

   public void setChannels(List<Channel> channels) {
      this.channels = channels;
   }

   @XmlElement(name="programme")
   public List<Programme> getProgrammes() {
      return programmes;
   }

   public void setProgrammes(List<Programme> programmes) {
      this.programmes = programmes;
   }
}
