# -*- coding: utf-8 -*-

################################################################################
## Form generated from reading UI file 'mainwindow.ui'
##
## Created by: Qt User Interface Compiler version 5.15.2
##
## WARNING! All changes made in this file will be lost when recompiling UI file!
################################################################################

from PySide2.QtCore import *
from PySide2.QtGui import *
from PySide2.QtWidgets import *


class Ui_MainWindow(object):
    def setupUi(self, MainWindow):
        if not MainWindow.objectName():
            MainWindow.setObjectName(u"MainWindow")
        MainWindow.resize(686, 602)
        self.action_connect = QAction(MainWindow)
        self.action_connect.setObjectName(u"action_connect")
        self.actiontest = QAction(MainWindow)
        self.actiontest.setObjectName(u"actiontest")
        self.centralwidget = QWidget(MainWindow)
        self.centralwidget.setObjectName(u"centralwidget")
        self.horizontalLayout = QHBoxLayout(self.centralwidget)
        self.horizontalLayout.setObjectName(u"horizontalLayout")
        self.tabWidget = QTabWidget(self.centralwidget)
        self.tabWidget.setObjectName(u"tabWidget")
        self.tabWidget.setEnabled(True)
        self.tab = QWidget()
        self.tab.setObjectName(u"tab")
        self.horizontalLayout_3 = QHBoxLayout(self.tab)
        self.horizontalLayout_3.setObjectName(u"horizontalLayout_3")
        self.verticalLayout_2 = QVBoxLayout()
        self.verticalLayout_2.setObjectName(u"verticalLayout_2")
        self.horizontalLayout_2 = QHBoxLayout()
        self.horizontalLayout_2.setObjectName(u"horizontalLayout_2")
        self.messageCheckLabel = QLabel(self.tab)
        self.messageCheckLabel.setObjectName(u"messageCheckLabel")
        font = QFont()
        font.setPointSize(16)
        self.messageCheckLabel.setFont(font)

        self.horizontalLayout_2.addWidget(self.messageCheckLabel)

        self.messageCheckLineEdit = QLineEdit(self.tab)
        self.messageCheckLineEdit.setObjectName(u"messageCheckLineEdit")

        self.horizontalLayout_2.addWidget(self.messageCheckLineEdit)


        self.verticalLayout_2.addLayout(self.horizontalLayout_2)

        self.checkMessageButton = QPushButton(self.tab)
        self.checkMessageButton.setObjectName(u"checkMessageButton")

        self.verticalLayout_2.addWidget(self.checkMessageButton)

        self.verticalLayout = QVBoxLayout()
        self.verticalLayout.setObjectName(u"verticalLayout")
        self.messageCheckResLabel = QLabel(self.tab)
        self.messageCheckResLabel.setObjectName(u"messageCheckResLabel")
        self.messageCheckResLabel.setFont(font)
        self.messageCheckResLabel.setScaledContents(True)
        self.messageCheckResLabel.setAlignment(Qt.AlignCenter)

        self.verticalLayout.addWidget(self.messageCheckResLabel)

        self.messageCheck_textB = QTextBrowser(self.tab)
        self.messageCheck_textB.setObjectName(u"messageCheck_textB")

        self.verticalLayout.addWidget(self.messageCheck_textB)


        self.verticalLayout_2.addLayout(self.verticalLayout)


        self.horizontalLayout_3.addLayout(self.verticalLayout_2)

        self.tabWidget.addTab(self.tab, "")
        self.tab3 = QWidget()
        self.tab3.setObjectName(u"tab3")
        self.horizontalLayout_6 = QHBoxLayout(self.tab3)
        self.horizontalLayout_6.setObjectName(u"horizontalLayout_6")
        self.verticalLayout_3 = QVBoxLayout()
        self.verticalLayout_3.setObjectName(u"verticalLayout_3")
        self.horizontalLayout_5 = QHBoxLayout()
        self.horizontalLayout_5.setObjectName(u"horizontalLayout_5")
        self.startEventButton = QLabel(self.tab3)
        self.startEventButton.setObjectName(u"startEventButton")
        self.startEventButton.setFont(font)

        self.horizontalLayout_5.addWidget(self.startEventButton)

        self.startEventLineEdit = QLineEdit(self.tab3)
        self.startEventLineEdit.setObjectName(u"startEventLineEdit")

        self.horizontalLayout_5.addWidget(self.startEventLineEdit)


        self.verticalLayout_3.addLayout(self.horizontalLayout_5)

        self.horizontalLayout_4 = QHBoxLayout()
        self.horizontalLayout_4.setObjectName(u"horizontalLayout_4")
        self.endEventButton = QLabel(self.tab3)
        self.endEventButton.setObjectName(u"endEventButton")
        self.endEventButton.setFont(font)

        self.horizontalLayout_4.addWidget(self.endEventButton)

        self.endEventLineEdit = QLineEdit(self.tab3)
        self.endEventLineEdit.setObjectName(u"endEventLineEdit")

        self.horizontalLayout_4.addWidget(self.endEventLineEdit)


        self.verticalLayout_3.addLayout(self.horizontalLayout_4)

        self.checkIntervallButton = QPushButton(self.tab3)
        self.checkIntervallButton.setObjectName(u"checkIntervallButton")

        self.verticalLayout_3.addWidget(self.checkIntervallButton)

        self.verticalLayout_4 = QVBoxLayout()
        self.verticalLayout_4.setObjectName(u"verticalLayout_4")
        self.checkIntervalResLabel = QLabel(self.tab3)
        self.checkIntervalResLabel.setObjectName(u"checkIntervalResLabel")
        self.checkIntervalResLabel.setFont(font)
        self.checkIntervalResLabel.setScaledContents(True)
        self.checkIntervalResLabel.setAlignment(Qt.AlignCenter)

        self.verticalLayout_4.addWidget(self.checkIntervalResLabel)

        self.messageCheckInterval_textB = QTextBrowser(self.tab3)
        self.messageCheckInterval_textB.setObjectName(u"messageCheckInterval_textB")

        self.verticalLayout_4.addWidget(self.messageCheckInterval_textB)


        self.verticalLayout_3.addLayout(self.verticalLayout_4)


        self.horizontalLayout_6.addLayout(self.verticalLayout_3)

        self.tabWidget.addTab(self.tab3, "")
        self.tab_2 = QWidget()
        self.tab_2.setObjectName(u"tab_2")
        self.verticalLayout_6 = QVBoxLayout(self.tab_2)
        self.verticalLayout_6.setObjectName(u"verticalLayout_6")
        self.horizontalLayout_7 = QHBoxLayout()
        self.horizontalLayout_7.setObjectName(u"horizontalLayout_7")
        self.dataRecordsLabel = QLabel(self.tab_2)
        self.dataRecordsLabel.setObjectName(u"dataRecordsLabel")
        font1 = QFont()
        font1.setPointSize(14)
        self.dataRecordsLabel.setFont(font1)

        self.horizontalLayout_7.addWidget(self.dataRecordsLabel)

        self.dataRecordsLineEdit = QLineEdit(self.tab_2)
        self.dataRecordsLineEdit.setObjectName(u"dataRecordsLineEdit")

        self.horizontalLayout_7.addWidget(self.dataRecordsLineEdit)


        self.verticalLayout_6.addLayout(self.horizontalLayout_7)

        self.horizontalLayout_8 = QHBoxLayout()
        self.horizontalLayout_8.setObjectName(u"horizontalLayout_8")
        self.filesLabel = QLabel(self.tab_2)
        self.filesLabel.setObjectName(u"filesLabel")
        self.filesLabel.setFont(font1)

        self.horizontalLayout_8.addWidget(self.filesLabel)

        self.filesLineEdit = QLineEdit(self.tab_2)
        self.filesLineEdit.setObjectName(u"filesLineEdit")

        self.horizontalLayout_8.addWidget(self.filesLineEdit)


        self.verticalLayout_6.addLayout(self.horizontalLayout_8)

        self.horizontalLayout_9 = QHBoxLayout()
        self.horizontalLayout_9.setObjectName(u"horizontalLayout_9")
        self.sizeLabel = QLabel(self.tab_2)
        self.sizeLabel.setObjectName(u"sizeLabel")
        self.sizeLabel.setFont(font1)

        self.horizontalLayout_9.addWidget(self.sizeLabel)

        self.sizeLineEdit = QLineEdit(self.tab_2)
        self.sizeLineEdit.setObjectName(u"sizeLineEdit")

        self.horizontalLayout_9.addWidget(self.sizeLineEdit)


        self.verticalLayout_6.addLayout(self.horizontalLayout_9)

        self.verticalLayout_5 = QVBoxLayout()
        self.verticalLayout_5.setObjectName(u"verticalLayout_5")
        self.getStatsButton = QPushButton(self.tab_2)
        self.getStatsButton.setObjectName(u"getStatsButton")
        self.getStatsButton.setMinimumSize(QSize(120, 40))
        self.getStatsButton.setFont(font)

        self.verticalLayout_5.addWidget(self.getStatsButton)


        self.verticalLayout_6.addLayout(self.verticalLayout_5)

        self.tabWidget.addTab(self.tab_2, "")

        self.horizontalLayout.addWidget(self.tabWidget)

        MainWindow.setCentralWidget(self.centralwidget)
        self.statusbar = QStatusBar(MainWindow)
        self.statusbar.setObjectName(u"statusbar")
        MainWindow.setStatusBar(self.statusbar)
        self.menuBar = QMenuBar(MainWindow)
        self.menuBar.setObjectName(u"menuBar")
        self.menuBar.setGeometry(QRect(0, 0, 686, 18))
        self.menuSettings = QMenu(self.menuBar)
        self.menuSettings.setObjectName(u"menuSettings")
        MainWindow.setMenuBar(self.menuBar)

        self.menuBar.addAction(self.menuSettings.menuAction())
        self.menuSettings.addAction(self.action_connect)

        self.retranslateUi(MainWindow)

        self.tabWidget.setCurrentIndex(2)


        QMetaObject.connectSlotsByName(MainWindow)
    # setupUi

    def retranslateUi(self, MainWindow):
        MainWindow.setWindowTitle(QCoreApplication.translate("MainWindow", u"Audit Chain Blockchain App", None))
        self.action_connect.setText(QCoreApplication.translate("MainWindow", u"config Connection", None))
        self.actiontest.setText(QCoreApplication.translate("MainWindow", u"test", None))
        self.messageCheckLabel.setText(QCoreApplication.translate("MainWindow", u"Insert Event Number", None))
        self.checkMessageButton.setText(QCoreApplication.translate("MainWindow", u"CheckMessage", None))
        self.messageCheckResLabel.setText(QCoreApplication.translate("MainWindow", u"Result", None))
        self.checkMessageButton.clicked.connect(MainWindow.checkMessageButton_clicked)     
        
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.tab), QCoreApplication.translate("MainWindow", u"MessageCheck", None))
        self.startEventButton.setText(QCoreApplication.translate("MainWindow", u"Insert Event Number Start", None))
        self.endEventButton.setText(QCoreApplication.translate("MainWindow", u"Insert Event Number End", None))
        self.checkIntervallButton.setText(QCoreApplication.translate("MainWindow", u"CheckMessage", None))
        self.checkIntervalResLabel.setText(QCoreApplication.translate("MainWindow", u"Result", None))
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.tab3), QCoreApplication.translate("MainWindow", u"MessageCheckIntervall", None))
        self.checkIntervallButton.clicked.connect(MainWindow.checkIntervallButton_clicked)

        
        self.dataRecordsLabel.setText(QCoreApplication.translate("MainWindow", u"Amout of Data Records", None))
        self.filesLabel.setText(QCoreApplication.translate("MainWindow", u"Amount of Files already created", None))
        self.sizeLabel.setText(QCoreApplication.translate("MainWindow", u"Blockchain Current Size", None))
        self.getStatsButton.setText(QCoreApplication.translate("MainWindow", u"Get Statistics!", None))
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.tab_2), QCoreApplication.translate("MainWindow", u"Statistics", None))
        self.getStatsButton.clicked.connect(MainWindow.getStatsButton_clicked)
        
        self.menuSettings.setTitle(QCoreApplication.translate("MainWindow", u"Settings", None))
    # retranslateUi

