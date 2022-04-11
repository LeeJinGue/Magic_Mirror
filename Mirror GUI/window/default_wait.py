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


class Net_Widget(object):


  def __init__(self):
    super().__init__()
    

  def timeout_fun(self):
    self.time_cnt += 1
    temp = str(self.time_cnt)
    #print("time cnt is %d" %self.time_cnt)

  def setupUi(self, Dialog1):
    Dialog1.setObjectName("Net_init_widget")
    Dialog1.resize(1024, 600)
    Dialog1.setWindowFlags(Qt.FramelessWindowHint)
    self.timer = QTimer(Dialog1)

    self.dialog=Dialog1
    self.pushButton = QtWidgets.QPushButton(Dialog1)
    self.pushButton.setGeometry(QtCore.QRect(50, 450, 75, 23))
    self.pushButton.setObjectName("pushButton")

    self.retranslateUi(Dialog1)
    QtCore.QMetaObject.connectSlotsByName(Dialog1)

  def retranslateUi(self, Dialog1):
    _translate = QtCore.QCoreApplication.translate
    Dialog1.setWindowTitle(_translate("Dialog1", "Dialog"))
    self.pushButton.setText(_translate("Dialog1", "back"))
    self.pushButton.clicked.connect(self.jump_to_main)


  def jump_to_main(self):
    self.dialog.close()