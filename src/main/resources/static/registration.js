// 1. Esperamos a que el HTML esté completamente cargado
$(document).ready(function() {

    // 2. Detectamos cuando alguien hace clic en el botón con ID "btn-registrar"
    $('#btn-registrar').on('click', function(event) {
        
        // Evitamos que el botón intente enviar un formulario tradicional y recargue la página
        event.preventDefault();

        // 3. CAPTURA DE DATOS (El Robot agarra lo que escribió el usuario)
        var nombre      = $('#nombre').val();
        var apellidos   = $('#apellidos').val();
        var email       = $('#email').val();
        var pass1       = $('#contraseña').val();
        var pass2       = $('#contraseña_repetida').val();

        // --- VALIDACIONES FRONTEND (Antes de molestar al servidor) ---
        
        // A. Validar que no estén vacíos
        if (nombre === "" || apellidos === "" || email === "" || pass1 === "") {
            alert("Por favor, completa todos los campos.");
            return; // Detiene la ejecución, no sigue bajando
        }

        // B. Validar que las contraseñas coincidan
        if (pass1 !== pass2) {
            alert("Las contraseñas no coinciden.");
            return;
        }

        // 4. PREPARAR EL PAQUETE (JSON)
        // Tu Backend espera: username, name, email.
        // Como tu formulario tiene Nombre y Apellido separados, los unimos.
        
        // Truco: Usaremos la parte antes del @ del email como "username" (UID)
        var usuarioGenerado = email.split('@')[0]; 

        var objetoParaEnviar = {
            username: usuarioGenerado,       // ej: juan.perez
            name: nombre + " " + apellidos,  // ej: Juan Perez
            email: email,                    // ej: juan.perez@test.com
            // Nota: No enviamos la password porque estamos en modo "Simulación LDAP"
            // y las contraseñas reales no se guardan en esta tabla por ahora.
        };

        console.log("Enviando datos:", objetoParaEnviar); // Para que lo veas en la consola F12

        // 5. EL MENSAJERO (AJAX)
        // Ajax permite hablar con Java sin recargar la página
        $.ajax({
            url: '/api/users/register',      // A dónde voy
            type: 'POST',                    // Qué voy a hacer (Enviar/Crear)
            contentType: 'application/json', // En qué idioma hablo (JSON)
            data: JSON.stringify(objetoParaEnviar), // Convertimos el objeto JS a Texto JSON
            
            // Si todo sale BIEN (Código 200 OK)
            success: function(respuesta) {
                console.log("Servidor respondió:", respuesta);
                alert("¡Registro Exitoso! Tu usuario es: " + usuarioGenerado + "\nLa contraseña por defecto es '123'");
                window.location.href = "/login.html"; // Redirigir al login
            },
            
            // Si algo sale MAL (Error 400, 500, etc)
            error: function(xhr) {
                console.error("Error:", xhr);
                var mensajeError = "Ocurrió un error desconocido";
                
                // Intentamos leer el mensaje que mandó Java
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    mensajeError = xhr.responseJSON.message;
                }
                
                alert("Error: " + mensajeError);
            }
        });

    });
});