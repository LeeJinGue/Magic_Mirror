from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import QMainWindow, QApplication
from PyQt5.QtCore import QTimer
from PyQt5.QtCore import Qt
import numpy as np
import Layout_generator
from window import Network_init , default_wait
import sys


class Ui_Form(QMainWindow):

  def __init__(self):
    super().__init__()
    
    # self.timer = QTimer(self)
    # self.timer.start(1)
    # self.timer.timeout.connect(self.timeout_fun)
    # self.time_cnt =0



  def setupUi(self, Form):
    Form.setObjectName("Form")
    Form.resize(1024, 600)
    Form.setWindowFlags(Qt.FramelessWindowHint)
    self.form = Form
    self.btn_d1 = QtWidgets.QPushButton(Form)
    self.btn_d1.setGeometry(QtCore.QRect(60, 100, 150, 23))
    self.btn_d1.setObjectName("btn_d1")
    self.btn_d2 = QtWidgets.QPushButton(Form)
    self.btn_d2.setGeometry(QtCore.QRect(60, 140, 75, 23))
    self.btn_d2.setObjectName("btn_d2")
    self.btn_exit = QtWidgets.QPushButton(Form)
    self.btn_exit.setGeometry(QtCore.QRect(310, 140, 75, 23))
    self.btn_exit.setObjectName("btn_exit")

    # self.mylabel = QtWidgets.QLabel(Form)
    # self.mylabel.setGeometry(QtCore.QRect(60, 160, 75, 23))
    # self.mylabel.setObjectName("label1")
    # self.mylabel.setText("hello")

    self.retranslateUi(Form)
    QtCore.QMetaObject.connectSlotsByName(Form)

  def retranslateUi(self, Form):
    _translate = QtCore.QCoreApplication.translate
    Form.setWindowTitle(_translate("Form", "Form"))
    self.btn_d1.setText(_translate("Form", "네트워크 설정"))
    self.btn_d1.clicked.connect(self.jump_to_Network)
    self.btn_d2.setText(_translate("Form", "대기화면"))
    self.btn_d2.clicked.connect(self.jump_to_default_wait)
    self.btn_exit.setText(_translate("Form", "Exit"))
    self.btn_exit.clicked.connect(self.exit)

    
#   def timeout_fun(self):
    # self.time_cnt += 1
    # temp = str(self.time_cnt)
    # self.mylabel.setText(temp)
    # print("time cnt is %d" %self.time_cnt)

  def jump_to_demo1(self): 
    #self.timer.stop()        
    #self.form.hide()    
    form1 = QtWidgets.QDialog()
    ui = Layout_generator.Ui_Dialog1()
    seed = np.array([[0,0,0],[1,1,3]])
    ui.setupUi(form1, seed)
    form1.show()
    form1.exec_()
    self.form.show()
    #self.timer.start(100)

  #네트워크 설정 화면 이동
  def jump_to_Network(self):
   #self.timer.stop()        
   self.form.hide()    
   form1 = QtWidgets.QDialog()
   ui = Network_init.Ui_Form()
   ui.setupUi(form1)
   form1.show()
   form1.exec_()
   self.form.show()
   #self.timer.start(100)

  #기본화면 이동
  def jump_to_default_wait(self): 
   #self.timer.stop()        
   self.form.hide()    
   form1 = QtWidgets.QDialog()
   ui = default_wait.Ui_Form()
   ui.setupUi(form1)
   form1.show()
   form1.exec_()
   self.form.show()
   #self.timer.start(100)


  def exit(self):
    self.form.close()


if __name__ == "__main__":
  app = QApplication(sys.argv)
  form = QtWidgets.QWidget()
  window = Ui_Form()
  window.setupUi(form)
  form.show()
  sys.exit(app.exec_())