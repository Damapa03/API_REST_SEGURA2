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