
'''mermaid
classDiagram
    class Usuario {
        +String nombre
        +String correo_e
        +String constraseña
        +void crearUsuario()
        +void eliminarUsuario()
    }

    class Login {
	    +String nombreUsuario
	    +String contraseña
	    +void login(nombreUsuario:String, contraseña:String)
	    +void logout()
	    +void resetContraseña(nombreUsuario:String)
    }

    class Preferencias {
	    +String tema
	    +String lenguje
	    +String userId
	    +Boolean notificaciones
	    +void cambiarTema()
	    +void habilitarNotificaciones()
	    +void deshabilitarNotificaciones()
	    +void cambiarLenguaje()
    }

    class Viaje {
	    +Date diaInicio
	    +Date diaFinal
        +Int numeroParadas
	    +void empezarViaje(diaIncio:Date, diaFinal:Date)
    }

    class Mapa {
	    +void mostrarLocalizacion()
	    +void sitiosCercanos()
        +void mostarPunto()
    }
    class PuntoIntinerario{
        +Mapa puntoIntinerario
        +void mostratPunto()
        +void establecerPunto()
    }
    class Recomendaciones{
        +Mapa recomendacion
        +void tenerRecomendacion()
    }

    Usuario "1" -- "1" Preferencias : has
    Usuario "1" -- "1" Login : manages
    Usuario "1" -- "*" Viaje : owns
    Viaje "*" -- "1" Mapa : shows locations
    Viaje "1" -- "*" PuntoIntinerario : contains
    Viaje "*" -- "*" Recomendaciones : gets
'''