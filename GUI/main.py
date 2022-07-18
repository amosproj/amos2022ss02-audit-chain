from logging import warning
import socket
from sqlite3 import connect
import sys
import json
import time


from PySide2.QtWidgets import QApplication, QMainWindow,QDialog, QMessageBox
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
        self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        #Remove the comment and comment the next line to not use the hardcoded values anymore
        #self.client_socket.connect((ip, port))
        self.client_socket.connect(("127.0.0.1", 6868))


    def send_command(self,command):
        print(command)
        self.client_socket.send(str(command).encode('utf-8'))
    


class MainWindow(QMainWindow):
    def __init__(self):
        super(MainWindow, self).__init__()
        self.ui = Ui_MainWindow()
        self.ui.setupUi(self)       
        self.connection_dialog = ConnectionDialog()
        self.ui.action_connect.triggered.connect(self.connection_dialog.show)
                
        msgBox = QMessageBox()
        msgBox.setWindowTitle("Connection")
        msgBox.setText("Welcome to your GUI!\nPlease, before performing any operation, make sure that you are connected by clicking on the settings button on the top left! \nEnjoy!")
        msgBox.exec()

    def checkMessageButton_clicked(self):
        
        eventNum = self.ui.messageCheckLineEdit.text()
        command = {
            "command": "check_single_message",
            "number": eventNum
        }
        # added new line else java wouldn't recognize the send command
        self.connection_dialog.send_command(str(command) + '\r\n')
        returned_data = self.connection_dialog.client_socket.recv(1024)
        json_string = returned_data.decode("utf-8").replace("'",'"').strip()
        json_object = json.loads(json_string)
        output = "Answer:"
        output += '\n'.join(str(e) for e in json_object["check_single_message"])
        
        self.ui.messageCheck_textB.setText(output)
    
        
    def checkIntervallButton_clicked(self):
        startEvent = self.ui.startEventLineEdit.text()
        endEvent = self.ui.endEventLineEdit.text()
        command = {
            "command": "check_message_interval",
            "start": startEvent,
            "end": endEvent
        }
        # added new line else java wouldn't recognize the send command
        self.connection_dialog.send_command(str(command) + '\r\n')
        returned_data = self.connection_dialog.client_socket.recv(1024)
        json_string = returned_data.decode("utf-8").replace("'",'"').strip()
        json_object = json.loads(json_string)
        output = "Answer:"
        output += '\n'.join(str(e) for e in json_object["check_message_interval"])
        
        self.ui.messageCheckInterval_textB.setText(output)
    

    def getStatsButton_clicked(self):
        command = {
            "command": "get_statistics"
            
        }
        # added new line else java wouldn't recognize the send command
        self.connection_dialog.send_command(str(command) + '\r\n')
        returned_data = self.connection_dialog.client_socket.recv(1024)
        json_string = returned_data.decode("utf-8").replace("'",'"').strip()
        json_object = json.loads(json_string)
        self.ui.dataRecordsLineEdit.setText(''.join(str(e) for e in json_object["amountDataRecords"]))
        self.ui.sizeLineEdit.setText(''.join(str(e) for e in json_object["amountFilesCreated"]))
        self.ui.filesLineEdit.setText(''.join(str(e) for e in json_object["currentSize"]))

if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = MainWindow()
    window.show()
    sys.exit(app.exec_())

