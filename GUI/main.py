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




class MainWindow(QMainWindow):
    def __init__(self):
        super(MainWindow, self).__init__()
        self.ui = Ui_MainWindow()
        self.ui.setupUi(self)
        self.connection_dialog = ConnectionDialog()
        self.ui.action_connect.triggered.connect(self.connection_dialog.show)





if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = MainWindow()
    window.show()
    sys.exit(app.exec_())





