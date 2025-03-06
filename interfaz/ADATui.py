import sys
from PyQt6.QtWidgets import QApplication, QMainWindow, QWidget, QVBoxLayout, QPushButton

# Importaciones de ventanas
from login_window import LoginWindow
from register_window import RegisterWindow

class MainApplication(QMainWindow):
    def __init__(self):
        super().__init__()
        self.initUI()

    def initUI(self):
        self.setWindowTitle('Sistema de Gestión de Tareas')
        self.setGeometry(100, 100, 400, 300)

        # Widget central
        central_widget = QWidget()
        self.setCentralWidget(central_widget)
        layout = QVBoxLayout()
        central_widget.setLayout(layout)

        # Botón Iniciar Sesión
        login_btn = QPushButton('Iniciar Sesión')
        login_btn.clicked.connect(self.showLoginWindow)
        layout.addWidget(login_btn)

        # Botón Registrarse
        register_btn = QPushButton('Registrarse')
        register_btn.clicked.connect(self.showRegisterWindow)
        layout.addWidget(register_btn)

    def showLoginWindow(self):
        self.login_window = LoginWindow(self)
        self.login_window.show()

    def showRegisterWindow(self):
        self.register_window = RegisterWindow()
        self.register_window.show()

def main():
    app = QApplication(sys.argv)
    main_window = MainApplication()
    main_window.show()
    sys.exit(app.exec())

if __name__ == '__main__':
    main()