import requests
from PyQt6.QtWidgets import QDialog, QVBoxLayout, QLabel, QLineEdit, QPushButton, QMessageBox


class RegisterWindow(QDialog):
    def __init__(self, parent=None):
        super().__init__(parent)
        self.initUI()

    def initUI(self):
        self.setWindowTitle('Registro')
        self.setGeometry(200, 200, 400, 500)

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

        # Repeat Password
        repeat_password_label = QLabel('Repetir Contraseña:')
        self.repeat_password_input = QLineEdit()
        self.repeat_password_input.setEchoMode(QLineEdit.EchoMode.Password)
        layout.addWidget(repeat_password_label)
        layout.addWidget(self.repeat_password_input)

        # Dirección - Calle
        calle_label = QLabel('Calle:')
        self.calle_input = QLineEdit()
        layout.addWidget(calle_label)
        layout.addWidget(self.calle_input)

        # Dirección - Número
        num_label = QLabel('Número:')
        self.num_input = QLineEdit()
        layout.addWidget(num_label)
        layout.addWidget(self.num_input)

        # Dirección - Código Postal
        cp_label = QLabel('Código Postal:')
        self.cp_input = QLineEdit()
        layout.addWidget(cp_label)
        layout.addWidget(self.cp_input)

        # Dirección - Provincia
        provincia_label = QLabel('Provincia:')
        self.provincia_input = QLineEdit()
        layout.addWidget(provincia_label)
        layout.addWidget(self.provincia_input)

        # Dirección - Municipio
        municipio_label = QLabel('Municipio:')
        self.municipio_input = QLineEdit()
        layout.addWidget(municipio_label)
        layout.addWidget(self.municipio_input)

        # Register Button
        register_btn = QPushButton('Registrarse')
        register_btn.clicked.connect(self.register)
        layout.addWidget(register_btn)

        self.setLayout(layout)

    def register(self):
        # Validaciones
        username = self.username_input.text()
        password = self.password_input.text()
        repeat_password = self.repeat_password_input.text()
        
        # Campos de dirección
        calle = self.calle_input.text()
        num = self.num_input.text()
        cp = self.cp_input.text()
        provincia = self.provincia_input.text()
        municipio = self.municipio_input.text()

        # Validaciones de campos
        if not username or not password or not repeat_password:
            QMessageBox.warning(self, 'Error', 'Por favor, rellene los campos obligatorios')
            return

        if password != repeat_password:
            QMessageBox.warning(self, 'Error', 'Las contraseñas no coinciden')
            return

        try:
            # Estructura de datos de registro
            register_data = {
                'username': username,
                'password': password,
                'passwordRepeat': repeat_password,
                'direccion': {
                    'calle': calle,
                    'num': num,
                    'cp': cp,
                    'provincia': provincia,
                    'municipio': municipio
                }
            }

            # Petición de registro
            response = requests.post('https://api-rest-segura2-xres.onrender.com/usuario/register', json=register_data)

            if response.status_code == 201:
                QMessageBox.information(self, 'Éxito', 'Registro completado con éxito')
                self.close()
            else:
                QMessageBox.warning(self, 'Error de Registro', 'No se pudo completar el registro')
        except requests.RequestException as e:
            QMessageBox.critical(self, 'Error de Red', str(e))