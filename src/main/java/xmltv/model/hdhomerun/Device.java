package xmltv.model.hdhomerun;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
{
   "LineupURL" : "http://192.168.20.137:80/lineup.json",
   "TunerCount" : 2,
   "DeviceID" : "1047BA79",
   "FriendlyName" : "HDHomeRun CONNECT",
   "FirmwareName" : "hdhomerun4_atsc",
   "ModelNumber" : "HDHR4-2US",
   "BaseURL" : "http://192.168.20.137:80",
   "FirmwareVersion" : "20161117",
   "DeviceAuth" : "D7Q+e+QXlhSWiUE8Et06PcUU"
}

 */
public class Device {
   String lineupUrl;
   String tunerCount;
   String deviceId;
   String deviceAuth;
   String friendlyName;
   String firmwareName;
   String modelNumber;
   String baseURL;
   String firmwareVersion;
   
   public Device() {}
   
   public static Device load(String surl) throws IOException {
      ObjectMapper om=new ObjectMapper();
      om.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
      om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      URL url=new URL(surl);
      
      Device d=om.readValue(url.openStream(), Device.class); 
      
      return d;
   }
   
   ////////////////////////////////////////////////////////////////////////////
   public String getLineupUrl() {
      return lineupUrl;
   }

   public void setLineupUrl(String lineupUrl) {
      this.lineupUrl = lineupUrl;
   }

   public String getTunerCount() {
      return tunerCount;
   }

   public void setTunerCount(String tunerCount) {
      this.tunerCount = tunerCount;
   }

   public String getDeviceId() {
      return deviceId;
   }

   public void setDeviceId(String deviceId) {
      this.deviceId = deviceId;
   }

   public String getFriendlyName() {
      return friendlyName;
   }

   public void setFriendlyName(String friendlyName) {
      this.friendlyName = friendlyName;
   }

   public String getFirmwareName() {
      return firmwareName;
   }

   public void setFirmwareName(String firmwareName) {
      this.firmwareName = firmwareName;
   }

   public String getModelNumber() {
      return modelNumber;
   }

   public void setModelNumber(String modelNumber) {
      this.modelNumber = modelNumber;
   }

   public String getBaseURL() {
      return baseURL;
   }

   public void setBaseURL(String baseURL) {
      this.baseURL = baseURL;
   }

   public String getFirmwareVersion() {
      return firmwareVersion;
   }

   public void setFirmwareVersion(String firmwareVersion) {
      this.firmwareVersion = firmwareVersion;
   }

   public String getDeviceAuth() {
      return deviceAuth;
   }

   public void setDeviceAuth(String deviceAuth) {
      this.deviceAuth = deviceAuth;
   }
}
