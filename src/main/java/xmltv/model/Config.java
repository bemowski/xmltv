package xmltv.model;

import java.util.List;

public class Config {
  List<String> skip;
  String zip;
  String hdhomerunUrl;

  public List<String> getSkip() {
    return skip;
  }

  public void setSkip(List<String> skip) {
    this.skip = skip;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getHdhomerunUrl() {
    return hdhomerunUrl;
  }

  public void setHdhomerunUrl(String hdhomerunUrl) {
    this.hdhomerunUrl = hdhomerunUrl;
  }
}
