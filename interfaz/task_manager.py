import requests
import jwt
from PyQt6.QtWidgets import QMainWindow, QWidget, QVBoxLayout, QLabel, QLineEdit, QPushButton, QMessageBox, QGridLayout, QScrollArea, QDialog

from task_card import TaskCard

class TaskManagerApp(QMainWindow):
    def __init__(self, token, username):
        super().__init__()
        self.token = token
        self.username = username
        self.initUI()
        self.loadTasks()

    def initUI(self):
        self.setWindowTitle('Gestor de Tareas')
        self.setGeometry(100, 100, 800, 600)

        # Widget central y diseño
        central_widget = QWidget()
        self.setCentralWidget(central_widget)
        main_layout = QVBoxLayout()
        central_widget.setLayout(main_layout)

        # Etiqueta de bienvenida
        welcome_label = QLabel(f'Bienvenido, {self.username}!')
        main_layout.addWidget(welcome_label)

        # Botón Añadir Tarea
        add_task_btn = QPushButton('Añadir Nueva Tarea')
        add_task_btn.clicked.connect(self.showAddTaskDialog)
        main_layout.addWidget(add_task_btn)

        # Área de desplazamiento para tareas
        scroll_area = QScrollArea()
        main_layout.addWidget(scroll_area)

        # Grid para Tareas
        self.tasks_grid = QWidget()
        self.tasks_grid_layout = QGridLayout(self.tasks_grid)
        scroll_area.setWidget(self.tasks_grid)
        scroll_area.setWidgetResizable(True)

    def load_public_key(self,file_path):
        """Carga la clave pública desde un archivo."""
        with open(file_path, "r") as file:
            return file.read()
    
    def loadTasks(self):
        public_key = self.load_public_key("public.pem")

        decoded = jwt.decode(self.token, public_key, algorithms=["RS256"])
     
        if "ROLE_USER" == decoded["roles"]:
            try:
                # Incluir token en la cabecera de autorización
                headers = {'Authorization': f'Bearer {self.token}'}
                response = requests.get(f'https://api-rest-segura2-xres.onrender.com/tarea/{self.username}', headers=headers)
                
                if response.status_code == 200:
                    tasks = response.json()
                    self.displayTasks(tasks)
                else:
                    QMessageBox.warning(self, 'Error', 'No se pudieron cargar las tareas')
            except requests.RequestException as e:
                QMessageBox.critical(self, 'Error de Red', str(e))
        else:
            try:
                # Incluir token en la cabecera de autorización
                headers = {'Authorization': f'Bearer {self.token}'}
                response = requests.get(f'https://api-rest-segura2-xres.onrender.com/tarea', headers=headers)
                
                if response.status_code == 200:
                    tasks = response.json()
                    self.displayTasks(tasks)
                else:
                    QMessageBox.warning(self, 'Error', 'No se pudieron cargar las tareas')
            except requests.RequestException as e:
                QMessageBox.critical(self, 'Error de Red', str(e))

    def displayTasks(self, tasks):
        # Limpiar tareas existentes
        for i in reversed(range(self.tasks_grid_layout.count())): 
            widget = self.tasks_grid_layout.itemAt(i).widget()
            if widget:
                widget.deleteLater()

        # Mostrar tareas en la cuadrícula
        row, col = 0, 0
        for task in tasks:
            task_card = TaskCard(task, self.token, self)
            self.tasks_grid_layout.addWidget(task_card, row, col)
            
            col += 1
            if col > 2:  # 3 tarjetas por fila
                col = 0
                row += 1

    def showAddTaskDialog(self):
        dialog = QDialog(self)
        dialog.setWindowTitle('Añadir Nueva Tarea')
        layout = QVBoxLayout()

        # Título de la Tarea
        title_label = QLabel('Título de la Tarea:')
        title_input = QLineEdit()
        layout.addWidget(title_label)
        layout.addWidget(title_input)

        # Descripción de la Tarea
        desc_label = QLabel('Descripción:')
        desc_input = QLineEdit()
        layout.addWidget(desc_label)
        layout.addWidget(desc_input)

        public_key = self.load_public_key("public.pem")
        decoded = jwt.decode(self.token, public_key, algorithms=["RS256"])
     
        user_label = QLabel('Usuario:')
        user_input = QLineEdit()
        if "ROLE_ADMIN" == decoded["roles"]:
            layout.addWidget(user_label)
            layout.addWidget(user_input)

        # Botón Guardar
        save_btn = QPushButton('Guardar Tarea')
        save_btn.clicked.connect(lambda: self.saveTask(title_input.text(), desc_input.text(), dialog, user_input.text()))
        layout.addWidget(save_btn)

        dialog.setLayout(layout)
        dialog.exec()

    def saveTask(self, title, description, dialog, username = None):
        if not title or not description:
            QMessageBox.warning(self, 'Entrada Inválida', 'Por favor, rellene todos los campos')
            return

        if username == "":
            username = self.username

        try:
            # Incluir token en la cabecera de autorización
            headers = {'Authorization': f'Bearer {self.token}'}
            task_data = {
                'titulo': title,
                'descripcion': description,
                'usuario': username
            }
            response = requests.post('https://api-rest-segura2-xres.onrender.com/tarea', json=task_data, headers=headers)
            
            if response.status_code == 201:
                QMessageBox.information(self, 'Éxito', 'Tarea añadida correctamente')
                dialog.accept()
                self.loadTasks()
            else:
                QMessageBox.warning(self, 'Error', 'No se pudo añadir la tarea')
        except requests.RequestException as e:
            QMessageBox.critical(self, 'Error de Red', str(e))
