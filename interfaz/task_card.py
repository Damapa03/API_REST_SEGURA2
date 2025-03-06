import requests
import jwt
from PyQt6.QtWidgets import QWidget, QVBoxLayout, QHBoxLayout, QLabel, QPushButton, QMessageBox, QDialog, QLineEdit

class TaskCard(QWidget):
    def __init__(self, task, token, parent=None):
        super().__init__(parent)
        self.task = task
        self.token = token
        self.parent_window = parent
        self.initUI()

    def initUI(self):
        layout = QVBoxLayout()
        self.setStyleSheet("""
            QWidget {
                border: 1px solid #ddd;
                border-radius: 5px;
                padding: 10px;
                background-color: #f9f9f9;
            }
        """)
        # Título
        title_label = QLabel(f"Título: {self.task.get('titulo', 'Sin Título')}")
        title_label.setStyleSheet("font-weight: bold;")
        layout.addWidget(title_label)

        # Descripción
        desc_label = QLabel(f"Descripción: {self.task.get('descripcion', 'Sin Descripción')}")
        layout.addWidget(desc_label)

        public_key = self.load_public_key("public.pem")
        decoded = jwt.decode(self.token, public_key, algorithms=["RS256"])

        user_label = QLabel(f"Usuario: {self.task.get('usuario', 'Sin usuario')}")

        if "ROLE_ADMIN" == decoded["roles"]:
            layout.addWidget(user_label)

        # Diseño de Botones
        btn_layout = QHBoxLayout()

        # Botón Editar
        edit_btn = QPushButton('Editar')
        edit_btn.clicked.connect(self.editTask)
        btn_layout.addWidget(edit_btn)

        # Botón Eliminar
        delete_btn = QPushButton('Eliminar')
        delete_btn.clicked.connect(self.deleteTask)
        btn_layout.addWidget(delete_btn)

        layout.addLayout(btn_layout)
        self.setLayout(layout)

    def load_public_key(self,file_path):
        """Carga la clave pública desde un archivo."""
        with open(file_path, "r") as file:
            return file.read()
        
    def editTask(self):
        # Crear diálogo de edición
        dialog = QDialog(self)
        dialog.setWindowTitle('Editar Tarea')
        layout = QVBoxLayout()

        # Campo de Título
        titulo_label = QLabel('Título:')
        titulo_input = QLineEdit()
        titulo_input.setText(self.task.get('titulo', ''))
        layout.addWidget(titulo_label)
        layout.addWidget(titulo_input)

        # Campo de Descripción
        desc_label = QLabel('Descripción:')
        desc_input = QLineEdit()
        desc_input.setText(self.task.get('descripcion', ''))
        layout.addWidget(desc_label)
        layout.addWidget(desc_input)

        # Botón de Guardar
        save_btn = QPushButton('Guardar Cambios')
        save_btn.clicked.connect(lambda: self.updateTask(
            titulo_input.text(), 
            desc_input.text(), 
            dialog
        ))
        layout.addWidget(save_btn)

        dialog.setLayout(layout)
        dialog.exec()

    def updateTask(self, nuevo_titulo, nueva_descripcion, dialog):
        # Validar campos
        if not nuevo_titulo or not nueva_descripcion:
            QMessageBox.warning(self, 'Error', 'Todos los campos son obligatorios')
            return

        try:
            # Preparar datos de actualización
            update_data = {
                'titulo': nuevo_titulo,
                'descripcion': nueva_descripcion,
                'usuario': self.task.get('usuario')
            }

            # Incluir token en la cabecera de autorización
            headers = {'Authorization': f'Bearer {self.token}'}
            # Realizar petición de actualización
            response = requests.put(
                f'https://api-rest-segura2-xres.onrender.com/tarea/{self.task["_id"]}', 
                json=update_data, 
                headers=headers
            )
            
            if response.status_code == 201:  # Asumiendo que el backend devuelve 201 para actualización
                QMessageBox.information(self, 'Éxito', 'Tarea actualizada correctamente')
                dialog.accept()
                # Recargar tareas en la ventana principal
                self.parent_window.loadTasks()
            else:
                QMessageBox.warning(self, 'Error', 'No se pudo actualizar la tarea')
        except requests.RequestException as e:
            QMessageBox.critical(self, 'Error de Red', str(e))

    def deleteTask(self):
        confirm = QMessageBox.question(self, 'Confirmar Eliminación', 
                                       '¿Está seguro de que desea eliminar esta tarea?',
                                       QMessageBox.StandardButton.Yes | QMessageBox.StandardButton.No)
        
        if confirm == QMessageBox.StandardButton.Yes:
            try:
                # Incluir token en la cabecera de autorización
                headers = {'Authorization': f'Bearer {self.token}'}
                response = requests.delete(f'https://api-rest-segura2-xres.onrender.com/tarea/{self.task["_id"]}', headers=headers)
                
                if response.status_code == 200:
                    QMessageBox.information(self, 'Éxito', 'Tarea eliminada correctamente')
                    self.parent_window.loadTasks()
                else:
                    QMessageBox.warning(self, 'Error', 'No se pudo eliminar la tarea')
            except requests.RequestException as e:
                QMessageBox.critical(self, 'Error de Red', str(e))