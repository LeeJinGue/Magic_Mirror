from bs4 import BeautifulSoup
from pprint import pprint
import sys
import requests
import re

html = requests.get('https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=%EC%9D%BC%EA%B8%B0+%EC%98%88%EB%B3%B4')
#pprint(html.text)

soup = BeautifulSoup(html.text, 'html.parser')


#f = open("날씨파싱.txt",'w',-1,'utf-8')
#print(soup, file=f)
#
#f.close()


mydata = soup.find('div',{'class':'weather_main'})

find_main = mydata.find('span',{'class':'blind'}).text
print(find_main)

mydata2 = soup.find('div',{'class':'weather_info'})
mydata3 = mydata2.find('div',{'class':'temperature_text'})
find_temp = mydata3.find('span',{'class':'blind'})
temp = mydata2.find('div',{'class':'temperature_text'}).text
ans =  re.sub(r'[^0-9]', '', temp)
print(ans)


#data1 = soup.find('div', {'class': 'weather_box'})
#print(data1)
#
#find_address = data1.find('span', {'class':'btn_select'}).text
#print('현재 위치: '+find_address)
#
#find_currenttemp = data1.find('span',{'class': 'todaytemp'}).text
#print('현재 온도: '+find_currenttemp+'℃')
#
#data2 = data1.findAll('dd')
#find_dust = data2[0].find('span', {'class':'num'}).text
#find_ultra_dust = data2[1].find('span', {'class':'num'}).text
#find_ozone = data2[2].find('span', {'class':'num'}).text
#print('현재 미세먼지: '+find_dust)
#print('현재 초미세먼지: '+find_ultra_dust)
#print('현재 오존지수: '+find_ozone)