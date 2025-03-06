# API REST SEGURA 2

## Descripcion de los documentos 
### collUser
El documento de los usuarios contendra informacion basica de estos para poder identificarlos con un nombre de usuario y contraseña, ademas de una diferenzacion por roles, por otro lado tambien tendremos que saber sus direcciones
    - username
    - password
    - roles
    - Direccion
        - calle
        - num
        - cp
        - ciudad
        - municipio
### collTareas
El documento de las tareas estara compuesto por su titulo, una descripcion, el usuario al que le pertenezca, la fecha de creacion y si ha sido realizada o no
    - titulo
    - descripcion
    - usuario
    - fechaCreacion
    - estado

## Endpoints

#### - GET /tareas
Se obtienen todas las tareas

#### - GET /tareasUsuario/{usuario}
Se obtienen todas las tareas de un usuario

#### - POST /login
Realiza el inicio de sesion del usuario

#### - POST /register
Realiza el registro del usuario

#### - POST /tarea
Realiza la creacion de una tarea

#### - POST /tareaUsuario
Realiza la creacion de una tarea para un usuario especifico

#### - PUT /tarea/{id}
Realiza la modificicacion de una tarea

#### - DELETE /tarea/{id}
Realiza la eliminacion de una tarea

## Logica de negocio 
En esta aplicacion la logica de negocio que se va realizar es que los usurios deben tener usuarios unicos, ni la contraseña de los usuario ni su direccion pueden estar en blanco.
Por otro lado las tareas no pueden ser creadas sin un titulo pero si pueden carecer de una descripcion, ademas no sera posible modificar la fecha de creacion de estas
## Excepciones
#### · NotFoundException:
Excepcion para cuando no sea posible encontrar una entidad Codigo: 404

#### · ValidationException: 
Excepcion para cuando no sea posible realizar una validacion Codigo: 400

## Restricciones de seguridad
Las restricciones de seguridad para los usuarios seran que estos no podran ver las tareas de el resto de ususarios de la aplicacion y por lo tanto tampoco podran borrar las tareas a los otros usuarios.
Por otro lado todos los tipos de usuarios podran realizar tanto el login como el registro

## PRUEBAS GESTIÓN USUARIOS
[Documentacion pruebas api.pdf](https://github.com/user-attachments/files/18913810/Documentacion.pruebas.api.pdf)
# Insomnia

## Registro

En la primera prueba hacemos un registro como usuario y nos devuelven el nombre de usuario y su rol
![image](https://github.com/user-attachments/assets/85b92db9-6536-40e3-bbb8-32298a662a9c)

Si el usuario ya ha sido registrado nos lo indicará
![image](https://github.com/user-attachments/assets/f649be38-ee55-4f2c-ae72-9cd0b6606919)

Si hubiese algún dato vacío nos saldrá un 400 Bad Request
![image](https://github.com/user-attachments/assets/5fd455e4-ffcd-495f-a744-266d5f87f24f)

Si las contraseñas no coinciden también saltaba un error
![image](https://github.com/user-attachments/assets/d34b3268-8d61-461d-a878-20b9fc220427)


Si el rol introducido no fuese ni USER ni ADMIN también saltará un error
![image](https://github.com/user-attachments/assets/77c68110-fa7b-41a3-b668-72f9fefae8fc)

Al igual que si se introdujese una provincia o municipio que no existen
![image](https://github.com/user-attachments/assets/6c7a1379-76d6-4ed7-ad30-3180f300c1c3)
![image](https://github.com/user-attachments/assets/a18a9bbd-a033-46b9-86b3-e635dc384942)


En esta prueba podemos ver cómo el usuario registrado es ADMIN
![image](https://github.com/user-attachments/assets/fab754fc-5dca-4851-af04-e3e396ec9331)


Y aquí podemos comprobar como han sido introducidos en la base de datos
![image](https://github.com/user-attachments/assets/a98bd751-9312-4684-aa65-09a5e29fda8b)

## Login

Si al realizar el login introducimos una campo mal nos dará un error de credenciales incorrectas
![image](https://github.com/user-attachments/assets/e4326824-7e8d-450a-accb-d08e81ac3e1f)


Y en el caso de que el login sea correcto nos devolverán el token

![image](https://github.com/user-attachments/assets/c0a2741e-5e5a-4268-8643-a2036feb1066)

# Interfaz gráfica

## Registro

En esta prueba si las contraseñas no coinciden saltara un diálogo indicándonos que no coinciden
![image](https://github.com/user-attachments/assets/042ab21d-c434-408d-8d25-f1b755d8b669)



En el caso que algún campo esté en blanco o la provincia y municipio no sean correctos saltara un dialog genérico
![image](https://github.com/user-attachments/assets/585409cd-76c2-467b-ae6a-ffa8cb55dce6)


En el caso que pongamos todo correctamente saldrá un diálogo indicandonoslo
![image](https://github.com/user-attachments/assets/d6e455c2-a010-40ee-80a5-2f91896a0c85)


## Login

En el caso de realizar un login incorrecto saldrá un dialogo que nos lo indique
![image](https://github.com/user-attachments/assets/fe73f07d-0c8f-440a-9385-bf04167fb551)

Y si a sido exitoso saltará la ventana de las tareas

![image](https://github.com/user-attachments/assets/6df54f05-88aa-4c49-8d81-7b09e503596e)

## PRUEBAS GÉSTION DE TAREAS

### Usuario con rol USER

● Ver todas SUS tareas
En esta prueba podemos ver como el usuario no tiene acceso a todas las tareas
![image](https://github.com/user-attachments/assets/8f82b138-e59a-4bad-8f98-8a344987e389)

Como a su vez no puede ver las tareas de otros usuarios
![image](https://github.com/user-attachments/assets/753c14e7-e29e-4745-88c4-b8275c6285bf)

Pero si es posible que vea las suyas propias
![image](https://github.com/user-attachments/assets/e9d4fe87-6479-41f4-92fe-31d98dde82e3)

● Marcar como hecha una tarea propia
Podemos comprobar como no puede modificar tareas que no le pertenezcan
![image](https://github.com/user-attachments/assets/f1711a76-f279-40a1-828e-66ea36a30b79)

Pero si las suyas

![image](https://github.com/user-attachments/assets/3c7cf7aa-b02f-4cf2-9a70-de45c5dc65bd)

● Eliminar una tarea propia
El usuario no puede eliminar una tarea que no se a suya
![image](https://github.com/user-attachments/assets/5735e78e-cbce-4e39-a94d-52138ab1cd56)

Pero si las suyas

![image](https://github.com/user-attachments/assets/3e71d549-846e-4e60-b580-f0e9260e7dee)

● Darse de alta A SÍ MISMO una tarea
El usuario no puede crearle tareas a otros usuarios
![image](https://github.com/user-attachments/assets/f8bafb5f-b1a2-49c4-8da5-5e19a49644b2)

Pero si puede crearselas a si mismo

![image](https://github.com/user-attachments/assets/a5fa3ae4-eaa2-4573-9624-d509dee873ce)

### Usuario con rol ADMIN

● Ver todas las tareas
El admin puede ver todas las tareas
![image](https://github.com/user-attachments/assets/85ad9ae1-fa76-48c1-b20e-8b4c4a23b80a)

Y todas las tareas de un usuario especifico

![image](https://github.com/user-attachments/assets/8712af21-a6d9-46a8-804c-acb06a3e2e06)

● Eliminar cualquier tarea de cualquier usuario
El admin puede borrar cualquier tarea
![image](https://github.com/user-attachments/assets/a9add4f0-d33e-405e-9d4f-1627a26f77e4)

● Dar de alta tareas a cualquier usuario
El admin puede crear taraeas para cualquier usuario
![image](https://github.com/user-attachments/assets/b498f333-d0eb-4e0d-9062-0f554a221174)

## PRUEBAS INTERFAZ CON RENDER
https://drive.google.com/file/d/1ZbVizx_xXRYPdoayJpst-zAHkWNA46w2/view?usp=sharing

