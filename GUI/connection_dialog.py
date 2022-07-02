# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'connection_dialog.ui',
# licensing of 'connection_dialog.ui' applies.
#
# Created: Sat Jul  2 02:52:17 2022
#      by: pyside2-uic  running on PySide2 5.13.2
#
# WARNING! All changes made in this file will be lost!

from PySide2 import QtCore, QtGui, QtWidgets

class Ui_connection_dialog(object):
    def setupUi(self, connection_dialog):
        connection_dialog.setObjectName("connection_dialog")
        connection_dialog.resize(400, 300)
        self.verticalLayout = QtWidgets.QVBoxLayout(connection_dialog)
        self.verticalLayout.setObjectName("verticalLayout")
        self.horizontalLayout_3 = QtWidgets.QHBoxLayout()
        self.horizontalLayout_3.setObjectName("horizontalLayout_3")
        self.label = QtWidgets.QLabel(connection_dialog)
        self.label.setObjectName("label")
        self.horizontalLayout_3.addWidget(self.label)
        self.lineEdit = QtWidgets.QLineEdit(connection_dialog)
        self.lineEdit.setObjectName("lineEdit")
        self.horizontalLayout_3.addWidget(self.lineEdit)
        self.verticalLayout.addLayout(self.horizontalLayout_3)
        self.horizontalLayout_2 = QtWidgets.QHBoxLayout()
        self.horizontalLayout_2.setObjectName("horizontalLayout_2")
        self.label_2 = QtWidgets.QLabel(connection_dialog)
        self.label_2.setObjectName("label_2")
        self.horizontalLayout_2.addWidget(self.label_2)
        self.lineEdit_2 = QtWidgets.QLineEdit(connection_dialog)
        self.lineEdit_2.setObjectName("lineEdit_2")
        self.horizontalLayout_2.addWidget(self.lineEdit_2)
        self.verticalLayout.addLayout(self.horizontalLayout_2)
        self.horizontalLayout = QtWidgets.QHBoxLayout()
        self.horizontalLayout.setObjectName("horizontalLayout")
        self.pushButton = QtWidgets.QPushButton(connection_dialog)
        self.pushButton.setObjectName("pushButton")
        self.horizontalLayout.addWidget(self.pushButton)
        self.pushButton_2 = QtWidgets.QPushButton(connection_dialog)
        self.pushButton_2.setObjectName("pushButton_2")
        self.horizontalLayout.addWidget(self.pushButton_2)
        self.verticalLayout.addLayout(self.horizontalLayout)

        self.retranslateUi(connection_dialog)
        QtCore.QMetaObject.connectSlotsByName(connection_dialog)

    def retranslateUi(self, connection_dialog):
        connection_dialog.setWindowTitle(QtWidgets.QApplication.translate("connection_dialog", "Connection Configuration", None, -1))
        self.label.setText(QtWidgets.QApplication.translate("connection_dialog", "Consumer IP", None, -1))
        self.label_2.setText(QtWidgets.QApplication.translate("connection_dialog", "Consumer Port", None, -1))
        self.pushButton.setText(QtWidgets.QApplication.translate("connection_dialog", "confirm", None, -1))
        self.pushButton_2.setText(QtWidgets.QApplication.translate("connection_dialog", "connect", None, -1))

