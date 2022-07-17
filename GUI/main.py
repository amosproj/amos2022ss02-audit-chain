import socket
import sys
import json
import time


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
        self.client_socket.send(str(command).encode('utf-8'))
    


class MainWindow(QMainWindow):
    def __init__(self):
        super(MainWindow, self).__init__()
        self.ui = Ui_MainWindow()
        self.ui.setupUi(self)
        self.connection_dialog = ConnectionDialog()
        self.ui.action_connect.triggered.connect(self.connection_dialog.show)
            


    def checkMessageButton_clicked(self):
        eventNum = self.ui.messageCheckLineEdit.text()
        command = {
            "command": "check_single_message",
            "number": eventNum
        }
        # added new line else java wouldn't recognize the send command
        self.connection_dialog.send_command(str(command) + '\r\n')
<<<<<<< HEAD
        a = self.connection_dialog.client_socket.recv(1024)
        json_string = json.dumps(a.decode("utf-8"))
        self.ui.messageCheck_textB.setText(json_string) 
=======
        returned_data = self.connection_dialog.client_socket.recv(1024)
        json_string = returned_data.decode("utf-8").replace("'",'"').strip()
        json_object = json.loads(json_string)
        for i in json_object.keys():
            print(i)
        print("done")

>>>>>>> 53b7164689b0ee920f6eeefdaf8d3284a06656e9

        
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
        a = self.connection_dialog.client_socket.recv(1024)
        json_string = json.dumps(a.decode("utf-8"))
        self.ui.messageCheckInterval_textB.setText(json_string) 


    def getStatsButton_clicked(self):
        command = {
            "command": "get_statistics"
            
        }
        # added new line else java wouldn't recognize the send command
        self.connection_dialog.send_command(str(command) + '\r\n')
        a = self.connection_dialog.client_socket.recv(1024)
        json_string = json.dumps(a.decode("utf-8"))
        """
        #Initial idea for json parse
        json_acceptable_string = json_string.replace("\"", "'")

        print(json_string)
        print(json_acceptable_string)
        
        data = json.load(json_acceptable_string)
        print(data['amountDataRecords'])
        """

if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = MainWindow()
    window.show()
    sys.exit(app.exec_())

