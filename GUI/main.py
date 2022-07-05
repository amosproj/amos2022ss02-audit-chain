import socket
import sys

import PySide2.QtUiTools
from PySide2 import QtCore, QtGui, QtWidgets
from PySide2.QtCore import QFile
from PySide2.QtWidgets import QApplication, QMainWindow,QDialog
from mainwindow import Ui_MainWindow
from connection_dialog import Ui_connection_dialog

class ConnectionDialog(QDialog):
    def __init__(self):
        super(ConnectionDialog,self).__init__()
        self.ui = Ui_connection_dialog()
        self.ui.setupUi(self)
    
    def pushButton_2_conn_clicked(self):
        self.ui.lineEdit.setText("connection was")
        self.ui.lineEdit_2.setText("created")


class MainWindow(QMainWindow):
    def __init__(self):
        super(MainWindow, self).__init__()
        self.ui = Ui_MainWindow()
        self.ui.setupUi(self)
        self.connection_dialog = ConnectionDialog()
        self.ui.action_connect.triggered.connect(self.connection_dialog.show)
            
        """for testing purposes
        client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        client_socket.connect(("127.0.0.1", 6868))
        client_socket.send(bytes("hello", 'utf-8'))
        """

    def pushButton_clicked(self):
        eventNum = self.ui.lineEdit.text() 
        self.ui.textBrowser.setText(eventNum)

    def pushButton_2_clicked(self):
        startEvent = self.ui.lineEdit_3.text() 
        endEvent = self.ui.lineEdit_2.text()
        self.ui.textBrowser_2.setText("Start event is " + startEvent + " and end event is: " + endEvent)
        
    def pushButton_3_clicked(self):
        self.ui.lineEdit_4.setText("data records")
        self.ui.lineEdit_5.setText("files")
        self.ui.lineEdit_6.setText("size")


if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = MainWindow()
    window.show()
    sys.exit(app.exec_())

