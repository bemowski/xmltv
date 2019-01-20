package xmltv.model.hdhomerun;

/*
            {
                "StartTime": 1493065800,
                "EndTime": 1493067600,
                "Title": "100 Huntley Street",
                "OriginalAirdate": 1397347200,
                "ImageURL": "http://my.hdhomerun.com/tmsimg/assets/p10727969_b_h3_ab.jpg",
                "SeriesID": "C10727969EN29VO"
                "Synopsis": "Shop jewelry designs featuring the finest diamond & gem simulants! Buy More Save More, save 15 percent on purchases of $49, 20 percent on purchases of $99 & more.",
            },
            
            
            {
                "StartTime": 1493173800,
                "EndTime": 1493175600,
                "Title": "The Big Bang Theory",
                "EpisodeNumber": "S06E02",
                "EpisodeTitle": "The Decoupling Fluctuation",
                "Synopsis": "Sheldon tries to intervene when he learns that Penny is thinking about breaking up with Leonard; Howard is picked on.",
                "OriginalAirdate": 1349308800,
                "ImageURL": "http://my.hdhomerun.com/tmsimg/assets/p10039305_b_h3_aa.jpg",
                "SeriesID": "C185554EN6JG4"
            },

 */
public class Program {
   long startTime;
   long endTime;
   String title;
   String synopsis;
   long originalAirDate;
   String seriesID;
   String imageURL;
   
   String episodeNumber;
   String episodeTitle;
   
   public Program(){}

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

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getSeriesID() {
      return seriesID;
   }

   public void setSeriesID(String seriesID) {
      this.seriesID = seriesID;
   }

   public String getImageURL() {
      return imageURL;
   }

   public void setImageURL(String imageURL) {
      this.imageURL = imageURL;
   }

   public String getSynopsis() {
      return synopsis;
   }

   public void setSynopsis(String synopsis) {
      this.synopsis = synopsis;
   }

   public String getEpisodeNumber() {
      return episodeNumber;
   }

   public void setEpisodeNumber(String episodeNumber) {
      this.episodeNumber = episodeNumber;
   }

   public String getEpisodeTitle() {
      return episodeTitle;
   }

   public void setEpisodeTitle(String episodeTitle) {
      this.episodeTitle = episodeTitle;
   }

   public long getOriginalAirDate() {
      return originalAirDate;
   }

   public void setOriginalAirDate(long originalAirDate) {
      this.originalAirDate = originalAirDate;
   }
}
