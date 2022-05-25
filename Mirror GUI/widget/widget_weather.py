# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'UI_Frame.ui'
#
# Created by: PyQt5 UI code generator 5.15.6
#
# WARNING: Any manual changes made to this file will be lost when pyuic5 is
# run again.  Do not edit this file unless you know what you are doing.


from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtGui import QPixmap,QIcon,QFont
from PyQt5.QtCore import Qt
from PyQt5.QtCore import *
from PyQt5.QtCore import QSize
from widget import weather_module
import os


class wThread(QThread):
    #parent = MainWidget을 상속 받음.
    def __init__(self, parent):
        super().__init__(parent)
        self.parent = parent
    def run(self):
      self.parent.set_weather()
      print("날씨 로딩 작동종료")

class weather(object):
    def setupUi(self, Form, x , y):

        self.widget_border = QtWidgets.QLabel(Form)
        self.widget_border.setGeometry(QtCore.QRect(x+20, y+60, 480, 520))
        self.widget_border.setFrameShape(QtWidgets.QFrame.Box)
        self.widget_border.setText("")
        self.widget_border.setObjectName("widget_border")
        self.weather_icon = QtWidgets.QLabel(Form)
        self.weather_icon.setGeometry(QtCore.QRect(x+40, y+80, 161, 151))
        #self.weather_icon.setFrameShape(QtWidgets.QFrame.Box)
        self.weather_icon.setAlignment(QtCore.Qt.AlignCenter)
        self.weather_icon.setObjectName("weather_icon")
        self.main_temperature = QtWidgets.QLabel(Form)
        self.main_temperature.setGeometry(QtCore.QRect(x+290, y+130, 111, 71))
        font = QtGui.QFont()
        font.setPointSize(40)
        font.setBold(True)
        font.setWeight(75)
        self.main_temperature.setFont(font)
        self.main_temperature.setAlignment(QtCore.Qt.AlignCenter)
        self.main_temperature.setObjectName("main_temperature")
        self.weather_text = QtWidgets.QLabel(Form)
        self.weather_text.setGeometry(QtCore.QRect(x+60, y+250, 151, 51))
        font = QtGui.QFont()
        font.setPointSize(15)
        self.weather_text.setFont(font)
        self.weather_text.setAlignment(QtCore.Qt.AlignCenter)
        self.weather_text.setObjectName("weather_text")
        self.dust_text = QtWidgets.QLabel(Form)
        self.dust_text.setGeometry(QtCore.QRect(x+260, y+230, 171, 51))
        font = QtGui.QFont()
        font.setPointSize(15)
        self.dust_text.setFont(font)
        self.dust_text.setAlignment(QtCore.Qt.AlignCenter)
        self.dust_text.setObjectName("dust_text")
        self.location = QtWidgets.QLabel(Form)
        self.location.setGeometry(QtCore.QRect(x+180, y+540, 151, 21))
        self.location.setAlignment(QtCore.Qt.AlignCenter)
        self.location.setObjectName("location")
        self.icon1 = QtWidgets.QLabel(Form)
        self.icon1.setGeometry(QtCore.QRect(x+50, y+380, 60, 60))
        #self.icon1.setFrameShape(QtWidgets.QFrame.Box)
        self.icon1.setObjectName("icon1")
        self.icon2 = QtWidgets.QLabel(Form)
        self.icon2.setGeometry(QtCore.QRect(x+140, y+380, 60, 60))
        #self.icon2.setFrameShape(QtWidgets.QFrame.Box)
        self.icon2.setObjectName("icon2")
        self.icon3 = QtWidgets.QLabel(Form)
        self.icon3.setGeometry(QtCore.QRect(x+230, y+380, 60, 60))
        #self.icon3.setFrameShape(QtWidgets.QFrame.Box)
        self.icon3.setObjectName("icon3")
        self.icon4 = QtWidgets.QLabel(Form)
        self.icon4.setGeometry(QtCore.QRect(x+320, y+380, 60, 60))
        #self.icon4.setFrameShape(QtWidgets.QFrame.Box)
        self.icon4.setObjectName("icon4")
        self.icon5 = QtWidgets.QLabel(Form)
        self.icon5.setGeometry(QtCore.QRect(x+410, y+380, 60, 60))
        #self.icon5.setFrameShape(QtWidgets.QFrame.Box)
        self.icon5.setObjectName("icon5")
        self.time1 = QtWidgets.QLabel(Form)
        self.time1.setGeometry(QtCore.QRect(x+50, y+370, 60, 12))
        self.time1.setAlignment(Qt.AlignCenter)
        self.time1.setObjectName("time1")
        self.time2 = QtWidgets.QLabel(Form)
        self.time2.setGeometry(QtCore.QRect(x+140, y+370, 60, 12))
        self.time2.setAlignment(Qt.AlignCenter)
        self.time2.setObjectName("time2")
        self.time3 = QtWidgets.QLabel(Form)
        self.time3.setGeometry(QtCore.QRect(x+230, y+370, 60, 12))
        self.time3.setAlignment(Qt.AlignCenter)
        self.time3.setObjectName("time3")
        self.time4 = QtWidgets.QLabel(Form)
        self.time4.setGeometry(QtCore.QRect(x+320, y+370, 60, 12))
        self.time4.setAlignment(Qt.AlignCenter)
        self.time4.setObjectName("time4")
        self.time5 = QtWidgets.QLabel(Form)
        self.time5.setGeometry(QtCore.QRect(x+410, y+370, 60, 12))
        self.time5.setAlignment(Qt.AlignCenter)
        self.time5.setObjectName("time5")
        self.bar = QtWidgets.QLabel(Form)
        self.bar.setGeometry(QtCore.QRect(x+50, y+446, 421, 20))
        font = QtGui.QFont()
        font.setPointSize(1)
        self.bar.setFont(font)
        self.bar.setFrameShape(QtWidgets.QFrame.Box)
        self.bar.setText("")
        self.bar.setObjectName("bar")


        icon_path= os.path.dirname(os.path.abspath(__file__)) #리소스 폴더 경로 추적
        icon_path +="/resource/weather_icon/01d.png"
        #print(icon_path)
        #icon_path+="\\resource\weather_icon\weather_icon_001.png"
        pixmap = QPixmap(icon_path)
        pixmap = pixmap.scaled(161, 151, Qt.IgnoreAspectRatio) #아이콘 크기 설정
        self.weather_icon.setPixmap(pixmap)

        pixmap = pixmap.scaled(60, 60, Qt.IgnoreAspectRatio)
        self.icon1.setPixmap(pixmap)
        self.icon2.setPixmap(pixmap)
        self.icon3.setPixmap(pixmap)
        self.icon4.setPixmap(pixmap)
        self.icon5.setPixmap(pixmap)
        self.retranslateweather(Form)
        QtCore.QMetaObject.connectSlotsByName(Form)
        w = wThread(self)
        w.start()
        #self.set_weather()

    def set_weather(self):

        _translate = QtCore.QCoreApplication.translate
        current , hour = weather_module.get_weather()

        icon_path= os.path.dirname(os.path.abspath(__file__)) #리소스 폴더 경로 추적
        icon_path +="/resource/weather_icon/"
        #print(icon_path)
        #icon_path+="\\resource\weather_icon\weather_icon_001.png"
        pixmap = QPixmap(icon_path+ str(current['icon']) + ".png") #매일 날씨 아이콘
        #print(icon_path)
        pixmap = pixmap.scaled(161, 151, Qt.IgnoreAspectRatio) #아이콘 크기 설정
        self.weather_icon.setPixmap(pixmap)
        #예보 날씨 아이콘 설정
        pixmap = QPixmap(icon_path+ str(hour[1]['icon']) + ".png") #1시간뒤
        pixmap = pixmap.scaled(60, 60, Qt.IgnoreAspectRatio)
        self.icon1.setPixmap(pixmap)

        pixmap = QPixmap(icon_path+ str(hour[2]['icon']) + ".png") #2시간뒤
        pixmap = pixmap.scaled(60, 60, Qt.IgnoreAspectRatio)
        self.icon2.setPixmap(pixmap)

        pixmap = QPixmap(icon_path+ str(hour[3]['icon']) + ".png") #3시간뒤
        pixmap = pixmap.scaled(60, 60, Qt.IgnoreAspectRatio)
        self.icon3.setPixmap(pixmap)

        pixmap = QPixmap(icon_path+ str(hour[4]['icon']) + ".png") #4시간뒤
        pixmap = pixmap.scaled(60, 60, Qt.IgnoreAspectRatio)
        self.icon4.setPixmap(pixmap)

        pixmap = QPixmap(icon_path+ str(hour[5]['icon']) + ".png") #5시간뒤
        pixmap = pixmap.scaled(60, 60, Qt.IgnoreAspectRatio)
        self.icon5.setPixmap(pixmap)

        #온도 텍스트 설정
        self.main_temperature.setText(_translate("Form", str(current['temp'])+'도'))
        self.weather_text.setText(_translate("Form", current['weather'])) # 온도
        
        #예보 온도 설정
        self.time1.setText(_translate("Form", str(hour[0]['temp'])+"도"))
        self.time2.setText(_translate("Form", str(hour[1]['temp'])+"도"))
        self.time3.setText(_translate("Form", str(hour[2]['temp'])+"도"))
        self.time4.setText(_translate("Form", str(hour[3]['temp'])+"도"))
        self.time5.setText(_translate("Form", str(hour[4]['temp'])+"도"))
    

    def retranslateweather(self, Form):
        _translate = QtCore.QCoreApplication.translate
        #self.weather_icon.setText(_translate("Form", "image box"))
        self.main_temperature.setText(_translate("Form", "X도")) # 온도
        self.weather_text.setText(_translate("Form", "로딩중")) #현재 날씨
        self.dust_text.setText(_translate("Form", ""))
        self.location.setText(_translate("Form", ""))
        #self.icon1.setText(_translate("Form", "TextLabel"))
        #self.icon2.setText(_translate("Form", "TextLabel"))
        #self.icon3.setText(_translate("Form", "TextLabel"))
        #self.icon4.setText(_translate("Form", "TextLabel"))
        #self.icon5.setText(_translate("Form", "TextLabel"))
        self.time1.setText(_translate("Form", "도"))
        self.time2.setText(_translate("Form", "도"))
        self.time3.setText(_translate("Form", "도"))
        self.time4.setText(_translate("Form", "도"))
        self.time5.setText(_translate("Form", "도"))
