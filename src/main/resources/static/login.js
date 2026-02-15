  $(document).ready(function () {
    // CAMBIO 1: Detectamos el click en el BOTÓN, no el submit del form
    $("#btn-login").on("click", function (event) {
      event.preventDefault();

      console.log("Botón presionado...");

      // CAMBIO 2: Usamos los IDs correctos de tu HTML
      var usuario = $("#login-user").val();
      var clave = $("#login-pass").val();

      console.log("Intentando login con:", usuario);

      var datos = {
        username: usuario, // Esto debe coincidir con LoginRequest en Java
        password: clave,
      };

      $.ajax({
        url: "/api/auth/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(datos),
        success: function (response) {
          console.log("Login Exitoso:", response);

          // 1. GUARDAR EN EL NAVEGADOR (Para usarlo en el dashboard)
          // 'response' es el objeto User que viene de Java (id, name, email, role...)
          localStorage.setItem("usuarioLogueado", JSON.stringify(response));

          // 2. REDIRIGIR
          alert("¡Bienvenido " + response.name + "!");
          window.location.href = "/dashboard.html"; // <-- Redirección real
        },
        error: function (xhr) {
          console.error("Error completo:", xhr);
          var mensaje = "Error desconocido";

          if (xhr.status === 0) {
            mensaje = "No se pudo conectar con el servidor Backend";
          } else if (xhr.responseJSON && xhr.responseJSON.message) {
            mensaje = xhr.responseJSON.message;
          } else if (xhr.status === 401) {
            mensaje = "Contraseña incorrecta";
          } else if (xhr.status === 403) {
            mensaje = "Usuario no encontrado en Base de Datos";
          }

          alert("Error: " + mensaje);
        },
      });
    });
  });