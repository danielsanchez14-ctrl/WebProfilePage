# Mi Perfil Dev: Portafolio de Habilidades de Programación

## Descripción del Proyecto

Esta aplicación web funciona como un **perfil profesional interactivo**, donde un usuario puede mostrar su información personal y sus habilidades de programación.
Permite **editar perfil, subir foto, gestionar habilidades y almacenar todo en MongoDB**.
El proyecto sigue estrictamente el patrón **MVC (Modelo–Vista–Controlador)** y utiliza:

* Java Servlets
* JSP + JSTL
* HTML + CSS + Bootstrap
* MongoDB como base de datos NoSQL
* Repositorios para manejar la persistencia

Es un proyecto educativo orientado a la construcción de aplicaciones web sin frameworks (sin Spring, sin Hibernate).

---

## Arquitectura del Proyecto

La aplicación está organizada en tres capas siguiendo el patrón MVC:

- **Modelo:** Clases Java (Profile, Skill)
- **Vista:** JSP (profile.jsp, edit.jsp)
- **Controlador:** Servlets (ProfileController, SkillController)
- **Persistencia:** MongoDB con repositorios dedicados

---

## Funcionalidades Principales

### Gestión del Perfil

* Editar nombre, biografía, experiencia y contacto.
* Subir una foto de perfil.
* Seleccionar color del banner.
* El perfil se guarda automáticamente en MongoDB.

### Gestión de Habilidades (CRUD completo)

* Registrar nuevas habilidades (con validación de duplicados).
* Editar habilidades existentes.
* Eliminar habilidades.
* Vista con tarjetas (Bootstrap) mostrando nivel 1–100.

### Persistencia

El sistema almacena:

* Un único documento de perfil.
* Colección completa de habilidades.

Colecciones:

```
profile
skills
```

---

## Cómo Ejecutar el Proyecto

### 1. Requisitos previos

Instalar:

* Java 21
* Apache Tomcat 10.1+
* MongoDB 6+
* IDE recomendado: NetBeans

### 2. Clonar el repositorio

```
git clone <url-del-repositorio>
```

### 3. Configurar MongoDB

Asegúrate de tener MongoDB ejecutándose:

```
mongod
```

La app se conecta automáticamente a:

```
mongodb://localhost:27017
Base: webprofiledb
```

### Configurar el servidor

En NetBeans:

1. Añadir Tomcat 10.
2. Cargar el proyecto.
3. Hacer "Clean & Build".
4. Ejecutar.

### Abrir la aplicación

```
http://localhost:8080/WebProfilePage/
```

Si quieres reiniciar toda la base de datos (si hay problemas):

```
http://localhost:8080/WebProfilePage/?resetdb=true
```
---

## Patrón MVC en Este Proyecto

### Modelo

Define los datos usados en la app:

* `Profile`
* `Skill`

Y cómo se guardan en la BD mediante repositorios:

* `ProfileRepositoryMongo`
* `SkillRepositoryMongo`

### Vista

JSP + JSTL renderizan:

* `profile.jsp` → Vista principal del perfil
* `edit.jsp` → Vista para editar habilidades

Ambas reciben datos desde los controladores via `request.setAttribute()`.

### Controlador

Contiene la lógica de negocio:

* `ProfileController` → Maneja edición del perfil y fotos
* `SkillController` → CRUD de habilidades

## Nota: acceda a la carpeta Technical Documentations para visualizar el diagrama de clases, explicación del flujo MVC y las pruebas manuales.

## Integrantes:
- María Camila Torres Chica
- Jesica Alejandra Estor Soto
- Mateo Daniel Galeano Quiñones
- David Sánchez Arango
- Daniel Sánchez Escobar
