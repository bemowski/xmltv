package xmltv.model.xmltv;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class LangString {
   String value=null;
   String lang="en";
   
   public LangString(String s) {
      this.value=s;
   }
   
   @XmlValue
   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }
   
   @XmlAttribute(name="lang")
   public String getLang() {
      if (value == null)
         return null;
      return lang;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }
}
