# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'UI_Frame.ui'
#
# Created by: PyQt5 UI code generator 5.15.6
#
# WARNING: Any manual changes made to this file will be lost when pyuic5 is
# run again.  Do not edit this file unless you know what you are doing.


from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtGui import QPixmap,QIcon,QFont
from PyQt5.QtWidgets import *
from PyQt5.QtCore import QSize
from PyQt5.QtCore import *

class belonging(QWidget):
  def __init__(self):
    super().__init__()
  def setupUi(self, Form, x, y):
        self.frame = QtWidgets.QLabel(Form)
        self.frame.setGeometry(QtCore.QRect(x+20, y+60, 480, 250))
        self.frame.setFrameShape(QtWidgets.QFrame.Box)
        self.frame.setText("")
        self.frame.setObjectName("frame")
        self.belonging_label = QtWidgets.QLabel(Form)
        self.belonging_label.setGeometry(QtCore.QRect(x+30, y+65, 121, 31))
        font = QtGui.QFont()
        font.setFamily("Adobe 고딕 Std B")
        font.setPointSize(15)
        self.belonging_label.setFont(font)
        self.belonging_label.setObjectName("belonging_label")
        self.belonging_view = QtWidgets.QListWidget(Form)
        self.belonging_view.setGeometry(QtCore.QRect(x+30, y+100, 460, 201))
        font = QtGui.QFont()
        font.setPointSize(20)
        self.belonging_view.setFont(font)
        self.belonging_view.setObjectName("belonging_view")
        
        self.retranslatesBelonging(Form)
        QtCore.QMetaObject.connectSlotsByName(Form)
        self.addBelonging()


  def retranslatesBelonging(self, Form):
      _translate = QtCore.QCoreApplication.translate
      Form.setWindowTitle(_translate("Form", "Form"))
      self.belonging_label.setText(_translate("Form", "소지품 확인"))

  def addBelonging(self):
   def get_item_wight():

    layout_main = QHBoxLayout()
    map1 = QLabel()
    map1.setStyleSheet( "QLabel{color: white;}")
    map1.setFixedWidth(430)
    map1.setFixedHeight(60)
    map1.setFrameShape(QtWidgets.QFrame.Box)
    map1.setText("BBBB")
    layout_main.addWidget(map1)

    wight = QWidget()
    wight.setLayout(layout_main)
    return wight #   wight

   item = QListWidgetItem() #   QListWidgetItem  
   item.setSizeHint(QSize(200, 80)) #   QListWidgetItem 
   brush = QtGui.QBrush(QtGui.QColor(0, 0, 0))
   item.setBackground(brush)
   
   widget = get_item_wight() #            
   self.belonging_view.addItem(item) #   item
   self.belonging_view.setItemWidget(item, widget) #  item  widget