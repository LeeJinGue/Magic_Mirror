from pickletools import long1
from datetime import datetime
from pprint import pprint
import requests
import json


#구글기반 위치 확인 api
LOCATION_API_KEY = "AIzaSyA7cGdXvE7EQIJfR9aENuV16m_rz_9JSEk" #api 사용키
url = f'https://www.googleapis.com/geolocation/v1/geolocate?key={LOCATION_API_KEY}'
data = {
    'considerIp': True,
}
result = requests.post(url, data) #위치정보 요청
data = json.loads(result.text)    
lon = data["location"]["lng"] #경도/위도 설정
lat = data["location"]["lat"]

# API 키를 지정합니다. 자신의 키로 변경해서 사용해주세요. --- (※1)
apikey = "b4c722b70c2d3e0d0405429119c0f30d"
# 날씨를 확인할 도시 지정하기 --- (※2)
cities = ["Suwon,KR"] ##
# API 지정 --- (※3)
exclude = "minutely,daily,alerts"
api = "http://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&exclude={exclude}&APPID={key}"


# API의 URL 구성하기 --- (※6)
url = api.format(lat=lat, lon=lon, exclude=exclude, key=apikey)
# API에 요청을 보내 데이터 추출하기
r = requests.get(url)
# 결과를 JSON 형식으로 변환하기 --- (※7)
data = json.loads(r.text)    
#print(data)

k2c = lambda k: k - 273.15 #섭씨 화씨 변환
count = 0
hour_weather_set = [] #현재부터 5시간후까지 일기예보 기록 리스트
current_weather = {} #현재 날씨

current_weather = {'time':datetime.fromtimestamp(int(data["current"]['dt'])).hour, 'temp': int(k2c(data['current']['temp'])),'weather':data['current']['weather'][0]['description']}

now = datetime.now().hour
for hour in data['hourly']: #현재부터 1시간 단위로 5시간 후까지 일기예보 추출
    unixtime = hour['dt']
    hourly_time = datetime.fromtimestamp(int(unixtime))
    hourly_temp = hour['temp']
    if hourly_time.hour > now:
        hourly_weather = hour['weather'][0]['description']
        hourly_temp = k2c(hourly_temp)
        tmp = {'time':hourly_time.hour, 'temp':int(hourly_temp), 'weather':hourly_weather}
        hour_weather_set.insert(len(hour_weather_set),tmp)
        #print(tmp)
        #print(hourly_time.hour )
        #print(int(hourly_temp))
        #print(hourly_weather)
        count+=1
    if(count>=5):
        break
print('현재 날씨')
print(current_weather)

print('날씨 예보')
print(hour_weather_set)