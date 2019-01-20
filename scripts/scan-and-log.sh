hdhomerun_config 1047BA79 scan 1 > ./scan.log



cat scan.log |sed '/^SCANNING/{N;s/\n//;}'|sed 's/(/\ /g'|sed 's/)/\ /g'|grep -v PROGRAM|grep -v TSID|grep -v none|sed 's/\ /,/g'>scan-`date '+%Y-%m-%d_%H%M%S'`.csv

