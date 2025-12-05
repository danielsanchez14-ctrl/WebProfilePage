<%-- 
    Document   : profile
    Created on : 4/12/2025, 6:41:33 p. m.
    Author     : Usuario
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
      rel="stylesheet"
      integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
      crossorigin="anonymous"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css" />
    
    <style>
      :root {
          --banner-color: ${profile.bannerColor};
      }

      #hero {
          background: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7));
          background-color: var(--banner-color);
          min-height: 60vh;
          display: flex;
          align-items: center;
          position: relative;
      }
    </style>
    
    <title>Mi Perfil Dev</title>
</head>
<body class="bg-light d-flex flex-column min-vh-100">
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">
      <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">MiPortafolio</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
          <ul class="navbar-nav gap-3">
            <li class="nav-item">
              <a class="nav-link active" href="#inicio">Inicio</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#sobre-mi">Sobre mí</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#habilidades">Habilidades</a>
            </li>
            <li class="nav-item">
              <a class="btn btn-outline-warning btn-sm mt-1" href="${pageContext.request.contextPath}/edit">
                ⚙️ Editar Perfil
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <!-- Hero / Banner -->
    <header id="hero">
      <div class="container text-center text-white h-100 d-flex flex-column justify-content-center">
        <h1 class="display-3 fw-bold">${profile.name}</h1>
        <p class="lead fs-3">Developer</p>
        <p class="mt-3 opacity-75">
          <i class="bi bi-envelope"></i> ${profile.contact}
        </p>
      </div>
    </header>

    <!-- Sobre mí -->
    <section id="sobre-mi" class="container text-center mb-5">
      <div class="profile-container">
      <img
        src="${pageContext.request.contextPath}/uploads/${empty profile.profilePicture ? 'default.jpg' : profile.profilePicture}"
        alt="Foto de Perfil"
        class="rounded-circle profile-img mb-3"
      />
      </div>

      <div class="row justify-content-center">
        <div class="col-md-8">
          <h2 class="mb-3">Sobre mí</h2>
          <p class="text-muted fs-5">${profile.bio}</p>

          <h4 class="mt-4">Experiencia</h4>
          <p class="text-muted">${profile.experience}</p>
        </div>
      </div>
    </section>

    <hr class="container my-5" />

    <!-- Habilidades -->
    <section id="habilidades" class="container mb-5">
      <div class="text-center mb-5">
        <h2 class="fw-bold">Mis Habilidades</h2>
        <p class="text-muted">Tecnologías con las que trabajo</p>
      </div>

      <div class="row g-4 justify-content-center">
        <c:forEach items="${skills}" var="skill">
          <div class="col-6 col-md-3">
            <div class="card h-100 border-0 shadow-sm skill-card text-center py-4">
              <div class="card-body">
                <h4 class="card-title text-primary fw-bold">${skill.name}</h4>
                <c:choose>
                  <c:when test="${skill.level >= 80}">
                    <span class="badge bg-success">Avanzado</span>
                  </c:when>
                  <c:when test="${skill.level >= 50}">
                    <span class="badge bg-warning text-dark">Intermedio</span>
                  </c:when>
                  <c:otherwise>
                    <span class="badge bg-secondary">Básico</span>
                  </c:otherwise>
                </c:choose>
              </div>
            </div>
          </div>
        </c:forEach>

        <c:if test="${empty skills}">
          <div class="col-12 text-center text-muted">
            <p>No hay habilidades registradas.</p>
          </div>
        </c:if>
      </div>
    </section>

    <!-- Footer -->
    <footer class="bg-dark text-white py-4 mt-auto">
      <div class="container text-center">
        <p class="mb-2">
          Contacto:
          <a href="mailto:${profile.contact}" class="text-white text-decoration-underline">
            ${profile.contact}
          </a>
        </p>
        <small class="text-white-50">&copy; 2025 Mi Perfil Profesional. Todos los derechos reservados.</small>
      </div>
    </footer>

    <script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
      integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
      crossorigin="anonymous"
    ></script>
</body>
</html>