# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'Dialog1.ui'
#
# Created by: PyQt5 UI code generator 5.10.1
#
# WARNING! All changes made in this file will be lost!

from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtCore import QTimer
from PyQt5.QtWidgets import (QApplication, QWidget
, QLineEdit, QTextBrowser, QPushButton, QVBoxLayout)
from PyQt5.QtCore import Qt


class Ui_Dialog1(object):

<<<<<<< HEAD
  loc_start = [[0,0],[400,0],[0,250],[400,250]]
  loc_end = [[400,250],[800,250],[400,500],[800,500]]

  def __init__(self):
    super().__init__()
    
=======

  loc_start = [[0,0],[400,0],[0,250],[400,250]]
  loc_end = [[400,250],[800,250],[400,500],[800,500]]
  def __init__(self):
    super().__init__()
>>>>>>> 9f89017f0b0ba6bbbabf32f7ee06c414bdf946c5


  def timeout_fun(self):
    self.time_cnt += 1
    temp = str(self.time_cnt)
    #print("time cnt is %d" %self.time_cnt)

  def setupUi(self, Dialog1, seed):
    Dialog1.setObjectName("Dialog1")
<<<<<<< HEAD
    Dialog1.resize(800, 500)
=======
    Dialog1.resize(1024, 600)
>>>>>>> 9f89017f0b0ba6bbbabf32f7ee06c414bdf946c5
    Dialog1.setWindowFlags(Qt.FramelessWindowHint)
    self.timer = QTimer(Dialog1)
    self.timer.start(100)
    self.timer.timeout.connect(self.timeout_fun)
    self.time_cnt = 0
    self.seed = seed

    self.dialog=Dialog1
    self.pushButton = QtWidgets.QPushButton(Dialog1)
    self.pushButton.setGeometry(QtCore.QRect(50, 450, 75, 23))
    self.pushButton.setObjectName("pushButton")
    
    self.tb_maker()

    self.retranslateUi(Dialog1)
    QtCore.QMetaObject.connectSlotsByName(Dialog1)

  def retranslateUi(self, Dialog1):
    _translate = QtCore.QCoreApplication.translate
    Dialog1.setWindowTitle(_translate("Dialog1", "Dialog"))
    self.pushButton.setText(_translate("Dialog1", "back"))
    self.pushButton.clicked.connect(self.jump_to_main)

  def tb_maker(self):
    for i in range(0,2):
     
      if self.seed[i][0] == 0:
        tempstr = "hello 날씨"
      else:
        tempstr = "hello 뉴스"
      
<<<<<<< HEAD
      #print(Ui_Dialog1.loc_start[self.seed[i][1]][0])
      #print(Ui_Dialog1.loc_start[self.seed[i][1]][1])
      #print(Ui_Dialog1.loc_end[self.seed[i][2]][0])
      #print(Ui_Dialog1.loc_end[self.seed[i][2]][1])
=======
>>>>>>> 9f89017f0b0ba6bbbabf32f7ee06c414bdf946c5
      self.tb = QtWidgets.QTextBrowser(self.dialog)
      self.tb.setAcceptRichText(True)
      self.tb.setOpenExternalLinks(True)
      self.tb.setGeometry(QtCore.QRect(Ui_Dialog1.loc_start[self.seed[i][1]][0], Ui_Dialog1.loc_start[self.seed[i][1]][1], Ui_Dialog1.loc_end[self.seed[i][2]][0], Ui_Dialog1.loc_end[self.seed[i][2]][1]))
      self.tb.append(tempstr)


  def jump_to_main(self):
    self.dialog.close()