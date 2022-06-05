from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import QMainWindow, QApplication
from PyQt5.QtCore import QTimer ,Qt


from window import Network_init , Default_Touch_wait, Default_Rg_wait, resist_face
import sys
#from facerecognize import face_rg

#rg = face_rg.FaceRecog()


class Ui_Form(QMainWindow):

  def __init__(self):

    super().__init__()
    
  def setupUi(self, Form):
    Form.setObjectName("Form")
    Form.resize(1024, 600)
    Form.setWindowFlags(Qt.FramelessWindowHint)

    self.form = Form
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



    #네트워크 설정 버튼
    self.btn_d1 = QtWidgets.QPushButton(Form)
    self.btn_d1.setGeometry(QtCore.QRect(60, 100, 150, 40))
    self.btn_d1.setObjectName("btn_d1")
    self.btn_d1.setStyleSheet("""color: #FFFFFF; 
                                        background-color: #000000;
                                        border-style: solid; 
                                        border-width: 1px; 
                                        border-color: #FFFFFF; 
                                        border-radius: 0px;
                                        font: 15pt """)

    #얼굴 인식 모드                                   
    self.btn_d2 = QtWidgets.QPushButton(Form)
    self.btn_d2.setGeometry(QtCore.QRect(60, 260, 100, 40))
    self.btn_d2.setObjectName("btn_d2")
    self.btn_d2.setStyleSheet("""color: #FFFFFF; 
                                        background-color: #000000;
                                        border-style: solid; 
                                        border-width: 1px; 
                                        border-color: #FFFFFF; 
                                        border-radius: 0px;
                                        font: 15pt """)

    #터치모드
    self.btn_d3 = QtWidgets.QPushButton(Form)
    self.btn_d3.setGeometry(QtCore.QRect(60, 180, 100, 40))
    self.btn_d3.setObjectName("btn_d3")
    self.btn_d3.setStyleSheet("""color: #FFFFFF; 
                                        background-color: #000000;
                                        border-style: solid; 
                                        border-width: 1px; 
                                        border-color: #FFFFFF; 
                                        border-radius: 0px;
                                        font: 15pt """)

    #종료버튼
    self.btn_exit = QtWidgets.QPushButton(Form)
    self.btn_exit.setGeometry(QtCore.QRect(60,440, 75, 23))
    self.btn_exit.setObjectName("btn_exit")
    self.btn_exit.setStyleSheet("""color: #FFFFFF; 
                                        background-color: #000000;
                                        border-style: solid; 
                                        border-width: 1px; 
                                        border-color: #FFFFFF; 
                                        border-radius: 0px;
                                        font: 15pt """)
    #사용자 등록
    self.btn_regist = QtWidgets.QPushButton(Form)
    self.btn_regist.setGeometry(QtCore.QRect(240, 100, 150, 40))
    self.btn_regist.setObjectName("btn_exit")
    self.btn_regist.setStyleSheet("""color: #FFFFFF; 
                                        background-color: #000000;
                                        border-style: solid; 
                                        border-width: 1px; 
                                        border-color: #FFFFFF; 
                                        border-radius: 0px;
                                        font: 15pt """)

    self.retranslateUi(Form)
    QtCore.QMetaObject.connectSlotsByName(Form)

  def retranslateUi(self, Form):
    _translate = QtCore.QCoreApplication.translate
    Form.setWindowTitle(_translate("Form", "Form"))
    self.btn_d1.setText(_translate("Form", "네트워크 설정"))
    self.btn_d1.clicked.connect(self.jump_to_Network)

    self.btn_d2.setText(_translate("Form", "얼굴인식"))
    self.btn_d2.clicked.connect(self.jump_to_user_UI)

    self.btn_d3.setText(_translate("Form", "터치모드"))
    self.btn_d3.clicked.connect(self.jump_to_default_touch)

    self.btn_exit.setText(_translate("Form", "Exit"))
    self.btn_exit.clicked.connect(self.exit)

    self.btn_regist.setText(_translate("Form", "얼굴등록"))
    self.btn_regist.clicked.connect(self.jump_to_regist)

  #얼굴 등록 화면 이동
  def jump_to_regist(self):
    form1 = QtWidgets.QDialog()
    ui = resist_face.Ui_Form()
    ui.setupUi(form1)
    form1.show()
    form1.exec_()
    self.form.show()


  #네트워크 설정 화면 이동
  def jump_to_Network(self):        
   #self.form.hide()    
   form1 = QtWidgets.QDialog()
   ui = Network_init.Ui_Form()
   ui.setupUi(form1)
   form1.show()
   form1.exec_()
   self.form.show()

  #기본화면 이동
  def jump_to_default(self):         
   #self.form.hide()    
   form1 = QtWidgets.QDialog()
   ui = Default_Touch_wait.Ui_Form()
   ui.setupUi(form1)
   form1.show()
   form1.exec_()
   self.form.show()

  #터치모드로 작동 시작
  def jump_to_default_touch(self):         
   #self.form.hide()    
   form1 = QtWidgets.QDialog()
   #ui = default_wait.Ui_Form()
   ui = Default_Touch_wait.Ui_Form()
   ui.setupUi(form1)
   form1.show()
   form1.exec_()
   self.form.show()

  #안면인식 모드로 작동 시작
  def jump_to_user_UI(self):        
   #self.form.hide()    
   form1 = QtWidgets.QDialog()
   ui = Default_Rg_wait.Ui_Form()
   ui.setupUi(form1)
   form1.show()
   form1.exec_()
   self.form.show()

  def exit(self):
    self.form.close()


if __name__ == "__main__":
  app = QApplication(sys.argv)
  form = QtWidgets.QWidget()
  window = Ui_Form()
  window.setupUi(form)
  form.show()
  sys.exit(app.exec_())