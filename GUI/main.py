import socket
import sys
import json

from PySide2.QtWidgets import QApplication, QMainWindow,QDialog
from mainwindow import Ui_MainWindow
from connection_dialog import Ui_connection_dialog

class ConnectionDialog(QDialog):
    def __init__(self):
        super(ConnectionDialog,self).__init__()
        self.ui = Ui_connection_dialog()
        self.ui.setupUi(self)
        self.ui.pushButton_2.clicked.connect(self.create_connection)

    
    def pushButton_2_conn_clicked(self):
        self.ui.lineEdit.setText("connection was")
        self.ui.lineEdit_2.setText("created")

    def create_connection(self):
        ip = self.ui.lineEdit.text()
        port = self.ui.lineEdit_2.text()
        # TODO change to ip
        self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.client_socket.connect(("127.0.0.1", 6868))


    def send_command(self,command):
        print(command)
        #self.client_socket.send(bytes(command.encode(),"utf-8"))




class MainWindow(QMainWindow):
    def __init__(self):
        super(MainWindow, self).__init__()
        self.ui = Ui_MainWindow()
        self.ui.setupUi(self)
        self.connection_dialog = ConnectionDialog()
        self.ui.action_connect.triggered.connect(self.connection_dialog.show)
            


    def pushButton_clicked(self):
        eventNum = self.ui.lineEdit.text()
        command = {
            "command": "check_single_message",
            "number":eventNum
        }
        self.connection_dialog.send_command(json.dumps(command))
    def pushButton_2_clicked(self):
        startEvent = self.ui.lineEdit_3.text()
        endEvent = self.ui.lineEdit_2.text()
        command = {
            "command": "check_message_interval",
            "start" : startEvent,
            "end": endEvent
        }
        self.connection_dialog.send_command(json.dumps(command))


    def pushButton_3_clicked(self):
        command = {
            "command": "get_statistics"
        }
        self.connection_dialog.send_command(json.dumps(command))


if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = MainWindow()
    window.show()
    sys.exit(app.exec_())

