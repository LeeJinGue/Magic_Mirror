# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'Dialog1.ui'
#
# Created by: PyQt5 UI code generator 5.10.1
#
# WARNING! All changes made in this file will be lost!

from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import *
from PyQt5.QtCore import *
from PyQt5.QtCore import QTimer
from PyQt5.QtCore import Qt
from widget import widget_weather , widget_time, widget_camera , widget_schedule , widget_stock, widget_message, widget_belonging
from db import db_access
import time


class T_wait(QThread):
    #parent = MainWidget을 상속 받음.
    def __init__(self, parent):
        super().__init__(parent)
        self.parent = parent
    def run(self):
      while True:
        time.sleep(1)
        self.parent.wait-=1
        if(self.parent.wait < 0):
          self.parent.exit()
          break
        else:
          self.parent.wait_b.setText(str(self.parent.wait)+" 초")
      
      print("사용자 레이아웃 종료")


class Ui_Form(widget_weather.weather, widget_time.clock,widget_camera.camera, 
widget_schedule.schedule, widget_stock.stock, widget_message.message, widget_belonging.belonging):

  loc_xy = [[0,0],[504,0],[504,270],[0,270]] # 1좌상, 2우상, 3우하, 4좌하
  def __init__(self):
    self.wait = 20
    super().__init__()

  def setupUi(self, Form,user_num):
        self.Form = Form
        self.user_id = user_num
        Form.setObjectName("Form")
        Form.resize(1024, 600)
        Form.setWindowFlags(Qt.FramelessWindowHint)
        palette = QtGui.QPalette()
        brush = QtGui.QBrush(QtGui.QColor(255, 255, 255))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.WindowText, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.Button, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.Light, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.Midlight, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.Dark, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.Mid, brush)
        brush = QtGui.QBrush(QtGui.QColor(255, 255, 255))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.Text, brush)
        brush = QtGui.QBrush(QtGui.QColor(255, 255, 255))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.BrightText, brush)
        brush = QtGui.QBrush(QtGui.QColor(255, 255, 255))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.ButtonText, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.Base, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.Window, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.Shadow, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.AlternateBase, brush)
        brush = QtGui.QBrush(QtGui.QColor(255, 255, 220))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.ToolTipBase, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.ToolTipText, brush)
        brush = QtGui.QBrush(QtGui.QColor(255, 255, 255))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.WindowText, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.Button, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.Light, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.Midlight, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.Dark, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.Mid, brush)
        brush = QtGui.QBrush(QtGui.QColor(255, 255, 255))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.Text, brush)
        brush = QtGui.QBrush(QtGui.QColor(255, 255, 255))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.BrightText, brush)
        brush = QtGui.QBrush(QtGui.QColor(255, 255, 255))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.ButtonText, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.Base, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.Window, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.Shadow, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.AlternateBase, brush)
        brush = QtGui.QBrush(QtGui.QColor(255, 255, 220))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.ToolTipBase, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.ToolTipText, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.WindowText, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.Button, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.Light, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.Midlight, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.Dark, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.Mid, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.Text, brush)
        brush = QtGui.QBrush(QtGui.QColor(255, 255, 255))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.BrightText, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.ButtonText, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.Base, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.Window, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.Shadow, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.AlternateBase, brush)
        brush = QtGui.QBrush(QtGui.QColor(255, 255, 220))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.ToolTipBase, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.ToolTipText, brush)
        Form.setPalette(palette)

        self.topBar = QtWidgets.QLabel(Form)
        self.topBar.setGeometry(QtCore.QRect(5, 5, 1014, 45))
        self.topBar.setFrameShape(QtWidgets.QFrame.Box)
        self.topBar.setText("")
        self.topBar.setObjectName("frame")

        self.exit1 = QtWidgets.QPushButton(Form)
        self.exit1.setGeometry(QtCore.QRect(912, 13, 100, 30))
        self.exit1.setObjectName("btn_d1")
        self.exit1.setStyleSheet("""color: #FFFFFF; 
                                        background-color: #000000;
                                        border-style: solid; 
                                        border-width: 1px; 
                                        border-color: #FFFFFF; 
                                        border-radius: 0px;
                                        font: 15pt """)

        self.wait_b = QtWidgets.QPushButton(Form)
        self.wait_b.setGeometry(QtCore.QRect(800, 13, 100, 30))
        self.wait_b.setObjectName("wait_b")
        self.wait_b.setStyleSheet("""color: #FFFFFF; 
                                        background-color: #000000;
                                        border-style: solid; 
                                        border-width: 1px; 
                                        border-color: #FFFFFF; 
                                        border-radius: 0px;
                                        font: 15pt """)

        self.exit1.setText("나가기")
        self.exit1.clicked.connect(self.exit)

        self.wait_b.setText("20 초")
        self.wait_b.clicked.connect(self.reset_wait)

        #위젯 작동 테스트
        #
        
        ##위치 1
        #widget_schedule.schedule.setupUi(self,Form,self.loc_xy[1][0],self.loc_xy[1][1])
        ##widget_stock.stock.setupUi(self,Form,0,0)
        ##위치 0
        #widget_message.message.setupUi(self,Form,self.loc_xy[0][0],self.loc_xy[0][1])
        ##위치 3
        #widget_belonging.belonging.setupUi(self,Form,self.loc_xy[3][0],self.loc_xy[3][1])
        self.drawWidget()

        QtCore.QMetaObject.connectSlotsByName(Form)
        t1 = T_wait(self)
        t1.start()
  
  def exit(self):        
       self.Form.close()

  def reset_wait(self):
    self.wait = 21
  

  def drawWidget(self):
    print("위젯 그림")
    setting = db_access.get_user_layoutsetting(self.user_id)
    #print(setting)
    #0:날씨, 1:시계, 2: 소지품, 3: 스케쥴, 4: 주식, 5: 메시지, 6: 카베라
    functions = [self.draw_weather, self.draw_clock, self.draw_belongings, self.draw_schedule, self.draw_stock, self.draw_message,self.draw_camera ]

    for set in setting:
      print(set[2])
      functions[set[2]](set[3])

  #스케쥴 위젯 그리기 함수
  def draw_schedule(self,loc):
    widget_schedule.schedule.setupUi(self,self.Form,self.loc_xy[loc][0],self.loc_xy[loc][1],self.user_id)
 
  #메시지 위젯 그리기 함수
  def draw_message(self,loc):
    widget_message.message.setupUi(self,self.Form,self.loc_xy[loc][0],self.loc_xy[loc][1],self.user_id)
   
  #소지품 위젯 그리기 함수
  def draw_belongings(self,loc):
    widget_belonging.belonging.setupUi(self,self.Form,self.loc_xy[loc][0],self.loc_xy[loc][1],self.user_id)

  #날씨 위젯 그리기 함수
  def draw_weather(self,loc):
    widget_weather.weather.setupUi(self,self.Form,self.loc_xy[loc][0],self.loc_xy[loc][1])

  #시계 위젯 그리기 함수
  def draw_clock(self,loc):
    widget_time.clock.setupUi(self,self.Form,self.loc_xy[loc][0],self.loc_xy[loc][1])


  def draw_stock(self,loc):
    widget_stock.stock.setupUi(self,self.Form,self.loc_xy[loc][0],self.loc_xy[loc][1],self.user_id)

  def draw_camera(self,loc):
    widget_camera.camera.setupUi(self,self.Form,self.loc_xy[loc][0],self.loc_xy[loc][1])

  
  def retranslateUi(self, Form):
        _translate = QtCore.QCoreApplication.translate
