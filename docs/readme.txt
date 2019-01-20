
Running the guide server:

nohup ./run xmltv.GuideServer -v --skip ../config/skip.json &


REMOTE REBOOT:

hdhomerun_config discover 
hdhomerun_config 1047BA79 set /sys/restart self

Found here:
http://dougshartzer.blogspot.com/2013/05/resetting-your-hdhomerun-tv-tuner.html





FTV Script: 
  https://github.com/bluezed/FTV-Guide-Repo/tree/master/script.ftvguide


New HD Homerun, has json:

curl -vk http://hdhomerun/lineup.json
curl -vk http://hdhomerun/discover.json


From hdhomerun app.py

discovery.py:LINEUP_URL_BASE = 'http://{ip}/lineup.json'
discovery.pyo:http://{ip}/lineup.jsoni
guide.py:GUIDE_URL = 'http://my.hdhomerun.com/api/guide.php?DeviceAuth={0}'
guide.py:SLICE_URL = 'http://my.hdhomerun.com/api/guide.php?DeviceAuth={deviceAuth}&Channel={channel}{start}'
guide.py:SEARCH_URL = 'http://my.hdhomerun.com/api/search?DeviceAuth={deviceAuth}&Search={search}'
guide.py:EPISODES_URL = 'http://my.hdhomerun.com/api/episodes?DeviceAuth={deviceAuth}&SeriesID={seriesID}'
guide.py:SUGGEST_URL = 'http://my.hdhomerun.com/api/suggest?DeviceAuth={deviceAuth}&Category={category}'
guide.py:NOW_SHOWING_URL = 'http://my.hdhomerun.com/api/up_next?DeviceAuth={deviceAuth}{start}'
guide.py:        "ImageURL": "http://usca-my.hdhomerun.com/fyimediaservices/v_3_3_6_1/Program.svc/96/1942433/Primary",
guide.py:        "ChannelImageURL": "http://usca-my.hdhomerun.com/fyimediaservices/v_3_3_6_1/Station.svc/2/765/Logo/120x120"




Zap2it updates

1) Getting zap2xml running on Ubuntu: 

Error: 
---------
bemo@yoga:/home/bemo/gitroot/xbmc/contrib/zap2xml>./zap2xml.pl 
Can't locate JSON.pm in @INC (you may need to install the JSON module) (@INC contains: /etc/perl /usr/local/lib/x86_64-linux-gnu/perl/5.22.1 /usr/local/share/perl/5.22.1 /usr/lib/x86_64-linux-gnu/perl5/5.22 /usr/share/perl5 /usr/lib/x86_64-linux-gnu/perl/5.22 /usr/share/perl/5.22 /usr/local/lib/site_perl /usr/lib/x86_64-linux-gnu/perl-base .) at ./zap2xml.pl line 38.

See homepage for tips on installing missing modules (example: "perl -MCPAN -e shell")
---------

Resolution: 
   sudo perl -MCPAN -e 'install JSON'

2) Running: 

./zap2xml.pl -u bemowski@yahoo.com -p k9doggy -d 3

"-d 3" - 3 days

Raw output does pretty good.  Could look into better descriptions, but overall
not too bad.


Blank lines, no data, junk channels:

Remove this file:
/home/bemo/.xbmc/userdata/addon_data/script.tvguide/source.db

Can do this with xbmc running, but probably not with the tv guide running.  


This is a small collection of utilities with the following goal:

 1) produce an xmltv.xml file with relevant channels
    a) refresh that xmltv file on a regular basis using mc2xml
 2) generate .strm files for the hdhomerun associated with the channels


Source data: 

config/streams.txt - this file has all of the channel names, and hdhomerun URLs.


Can look at channels: 

http://tvlistings.zap2it.com/tvlistings/ZCGrid.do?method=decideFwdForLineup&zipcode=94607&setMyPreference=false&lineupId=PC:94607&aid=zap2it

To get all channels, do this

mc2xml -c us -g 94607

Then head -1000 the resulting file.

=========== Steps to build streams.txt


=========== Steps to generate everything: 

1) process streams.txt to output .strm files and the mc2xml input files.

java xmltv.xmltv <path to streams.txt> 


2) Run xc2xml 

We are using the standard microsoft service.

mc2xml -c US -g 94607 -o <path to output>


3) run xmltv.FixDisplayNames <path to xmltv.xml> 


Output is in 
strm - stream files
xmltv.out - xmltv.xml EPG data






