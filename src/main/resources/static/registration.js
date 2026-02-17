$(document).ready(function() {

    // Detectamos clic en el botón Registrar
    $('#btn-registrar').on('click', function(event) {
        event.preventDefault(); // Evitar recarga de página

        // 1. CAPTURA DE DATOS
        var nombre      = $('#nombre').val();
        var apellidos   = $('#apellidos').val();
        var email       = $('#email').val();
        var pass1       = $('#contraseña').val();
        var pass2       = $('#contraseña_repetida').val();
        var terminos    = $('#recordarme').is(':checked');

        // 2. VALIDACIONES
        if (nombre === "" || apellidos === "" || email === "" || pass1 === "") {
            alert("Por favor, completa todos los campos.");
            return;
        }

        if (pass1 !== pass2) {
            alert("Las contraseñas no coinciden.");
            return;
        }

        if (!terminos) {
            alert("Debes aceptar los términos y condiciones.");
            return;
        }

        // 3. GENERAR USERNAME AUTOMÁTICO (Todo lo antes del @)
        // Ejemplo: juan.perez@email.com -> juan.perez
        var usuarioGenerado = email.split('@')[0]; 

        // 4. CREAR OBJETO JSON
        var objetoParaEnviar = {
            username: usuarioGenerado,
            name: nombre + " " + apellidos,
            email: email
            // password: pass1 // Descomenta si tu Backend ya guarda contraseñas reales
        };

        // 5. ENVIAR AL BACKEND (AJAX)
        $.ajax({
            url: '/api/users/register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(objetoParaEnviar),
            
            success: function(respuesta) {
                // EXITO: No usamos alert(). Usamos el Modal bonito.
                
                // A. Ponemos el usuario en el texto del modal
                $('#modal-username-display').text(usuarioGenerado);
                
                // B. Mostramos el modal
                var myModal = new bootstrap.Modal(document.getElementById('successModal'));
                myModal.show();
            },
            
            error: function(xhr) {
                // ERROR: Si el usuario ya existe o falla el servidor
                var mensajeError = "Ocurrió un error desconocido";
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    mensajeError = xhr.responseJSON.message;
                }
                alert("Error: " + mensajeError);
            }
        });
    });

    // 6. Botón dentro del Modal para ir al Login
    $('#btn-go-login').click(function() {
        window.location.href = "/login.html";
    });

});