import requests
from PyQt6.QtWidgets import QDialog, QVBoxLayout, QLabel, QLineEdit, QPushButton, QMessageBox

from task_manager import TaskManagerApp

class LoginWindow(QDialog):
    def __init__(self,main, parent=None):
        super().__init__(parent)
        self.main = main
        self.initUI()

    def initUI(self):
        self.setWindowTitle('Iniciar Sesión')
        self.setGeometry(200, 200, 300, 200)

        layout = QVBoxLayout()

        # Username
        username_label = QLabel('Nombre de usuario:')
        self.username_input = QLineEdit()
        layout.addWidget(username_label)
        layout.addWidget(self.username_input)

        # Password
        password_label = QLabel('Contraseña:')
        self.password_input = QLineEdit()
        self.password_input.setEchoMode(QLineEdit.EchoMode.Password)
        layout.addWidget(password_label)
        layout.addWidget(self.password_input)

        # Login Button
        login_btn = QPushButton('Iniciar Sesión')
        login_btn.clicked.connect(self.login)
        layout.addWidget(login_btn)

        self.setLayout(layout)

    def login(self):
        username = self.username_input.text()
        password = self.password_input.text()

        if not username or not password:
            QMessageBox.warning(self, 'Error', 'Por favor, rellene todos los campos')
            return

        try:
            # Datos de inicio de sesión
            login_data = {
                'username': username,
                'password': password
            }

            # Petición de login
            response = requests.post('https://api-rest-segura2-xres.onrender.com/usuario/login', json=login_data)

            if response.status_code == 201:
                # Obtener el token de la respuesta
                token = response.json()['token']
                
                # Abrir ventana de gestor de tareas con el token
                self.task_manager = TaskManagerApp(token, username)
                self.task_manager.show()
                self.main.close()
                self.close()
            else:
                QMessageBox.warning(self, 'Error de Inicio de Sesión', 'Credenciales inválidas')
        except requests.RequestException as e:
            QMessageBox.critical(self, 'Error de Red', str(e))