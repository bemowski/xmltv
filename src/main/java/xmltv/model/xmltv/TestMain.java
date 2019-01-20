package xmltv.model.xmltv;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;

import net.jmatrix.jproperties.util.ArgParser;
import net.jmatrix.utils.ClassLogFactory;

public class TestMain {
   static final Logger log=ClassLogFactory.getLog();

   
   public static void main(String args[]) {
      ArgParser ap=new ArgParser(args);
      
      TV tv=new TV();
      
      tv.addChannel(new Channel("2.1-ktvu", "KTVU"));
      tv.addChannel(new Channel("2.2-ktvu", "KTVU - two"));
      tv.addChannel(new Channel("2.2-ktvu", "KTVU - three"));
      
      
      
      String output=ap.getStringArg("-o");
      
      
      try {
         JAXBContext jaxbContext = JAXBContext.newInstance(TV.class);
         Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

         // output pretty printed
         jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

         jaxbMarshaller.marshal(tv, System.out);
      } catch (Exception ex) {
         log.error("Error: ", ex);
      }
   }
}
