cd out/production/NAI_07/
rm ~/pjatk/temp/logo.txt && java PathAnalyser ../../../tsp_data/tsp_data2.txt >> ~/pjatk/temp/logo.txt
cat ~/pjatk/temp/logo.txt | grep 'initial\|optimal'