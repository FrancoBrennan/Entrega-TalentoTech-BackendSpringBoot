# GadgetZone - API REST para E-commerce

GadgetZone es una API REST desarrollada con Spring Boot para gestionar un e-commerce de productos tecnológicos. Incluye autenticación con JWT, control de roles (USER y ADMIN), manejo de productos, usuarios, pedidos y más.

## 🚀 Tecnologías utilizadas

- Java 21
- Spring Boot  
- Spring Security  
- JWT (JSON Web Token)  
- MySQL
- Maven  
- Lombok  

## 📦 Instalación y ejecución

1. Cloná el repositorio:
   ```bash
   git clone https://github.com/tuusuario/gadgetzone.git
   cd gadgetzone
   ```

2. Configurá el archivo `application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/gadgetzone
   spring.datasource.username=usuario
   spring.datasource.password=clave
   jwt.secret-key=TU_CLAVE_SECRETA
   ```

3. Ejecutá la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```

## 🔐 Endpoints de Autenticación (`/api/auth`)

| Método | Ruta      | Descripción                   |
|--------|-----------|-------------------------------|
| POST   | /register | Registra un nuevo usuario     |
| POST   | /login    | Inicia sesión y retorna el JWT |

## 👤 Endpoints de Usuario (`/api/usuarios`)

| Método | Ruta              | Descripción                                 | Rol requerido     |
|--------|-------------------|---------------------------------------------|-------------------|
| GET    | /                 | Lista todos los usuarios                    | ADMIN             |
| PUT    | /me               | Actualiza los datos personales del usuario  | USER / ADMIN      |
| PUT    | /me/password      | Cambia la contraseña del usuario autenticado| USER / ADMIN      |
| DELETE | /me               | Elimina la propia cuenta                    | USER / ADMIN      |

## 📦 Endpoints de Producto (`/api/productos`)

| Método | Ruta     | Descripción                        | Rol requerido |
|--------|----------|------------------------------------|---------------|
| GET    | /        | Lista todos los productos          | Público       |
| GET    | /{id}    | Obtiene un producto por ID         | Público       |
| POST   | /        | Crea un nuevo producto             | ADMIN         |
| PUT    | /{id}    | Actualiza un producto existente    | ADMIN         |
| DELETE | /{id}    | Elimina un producto                | ADMIN         |

## 🛒 Endpoints de Pedido (`/api/pedidos`)

| Método | Ruta                        | Descripción                                      | Rol requerido |
|--------|-----------------------------|--------------------------------------------------|---------------|
| POST   | /                           | Crea un nuevo pedido con los productos del carrito| USER         |
| GET    | /mis-pedidos                | Lista los pedidos del usuario autenticado       | USER          |
| GET    | /                           | Lista todos los pedidos del sistema             | ADMIN         |
| GET    | /usuario/{usuarioId}        | Lista pedidos de un usuario específico          | ADMIN         |
| PUT    | /{id}/estado                | Actualiza el estado del pedido                  | ADMIN         |
| PUT    | /{id}/lineas                | Modifica líneas del pedido                      | ADMIN         |
| DELETE | /{id}                       | Elimina un pedido                               | ADMIN         |

## 🔐 Roles disponibles

- **USER**: puede registrarse, autenticarse, ver productos y realizar pedidos.  
- **ADMIN**: tiene permisos para gestionar productos, usuarios y pedidos.

## 🧪 Autenticación JWT

Cada endpoint protegido requiere el siguiente header:

```
Authorization: Bearer {token}
```

El token se obtiene luego de loguearse exitosamente.

## 📫 Autor

Desarrollado por **Franco Ezequiel Brennan** [@FrancoBrennan]  
💼 Estudiante de Ingeniería Informática (5to año) | Backend Developer
