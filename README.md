# Mi Perfil Dev: Portafolio de Habilidades de Programación

## Objetivo
Aplicación web que muestra un perfil profesional editable de un programador, con gestión de habilidades (CRUD) y personalización de banner y foto de perfil.

## Requisitos
- Java 21
- Apache Tomcat 10.1+
- MongoDB 6.0+ ejecutándose en `localhost:27017`
- NetBeans (u otro IDE compatible con Maven)

## Cómo ejecutar
1. Clonar el repositorio.
2. Asegurarse de que **MongoDB esté corriendo** (ejecutar `mongod --dbpath=/ruta/data`).
3. Abrir el proyecto en NetBeans y ejecutar.
4. Acceder a: `http://localhost:8080/WebProfileApp/`

> **Nota**: La primera vez se creará un perfil por defecto con imágenes `default.jpg` y `banner.jpg`.

## Reiniciar datos (si hay problemas)
Accede a:  
`http://localhost:8080/WebProfileApp/?resetdb=true`  
Esto borrará el perfil y habilidades actuales y creará uno nuevo con imágenes por defecto.

## Estructura
- **Modelo**: `Profile`, `Skill`
- **Vistas**: `profile.jsp` (solo lectura), `edit.jsp` (edición + CRUD)
- **Controladores**: `ProfileController`, `SkillController`
- **Persistencia**: MongoDB (colecciones `profile` y `skills`)

## Capturas de pantalla
(Adjunta 2–3 capturas en la carpeta `docs/`)
