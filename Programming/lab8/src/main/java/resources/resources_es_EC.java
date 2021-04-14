package resources;

import java.util.ListResourceBundle;

public class resources_es_EC extends ListResourceBundle {
    private static final Object[][] resources={
            {"ErrorTitle","Error"},
            {"WrongPortError","Por favor, introduzca un número."},
            {"WrongPortNumberError","El puerto debe ser mayor que 0 y menor que 65535"},
            {"Russian","Русский"},
            {"Finnish","suomalainen"},
            {"Shqiptar","Shqiptar"},
            {"Español(Ec)","Español"},
            {"Host","Anfitrión"},
            {"Port","El puerto"},
            {"Launch","Lanzar"},
            {"PortSet","Instalación de puerto"},
            {"Login","Iniciar sesión"},
            {"Register","Para registrarse"},
            {"SignUp","Registro"},
            {"nickname","Nombre de usuario"},
            {"password","Contraseña"},
            {"WrongPortAndHost","No pudimos conectarnos a los datos especificados. Inténtalo de nuevo"},
            {"WriteSomething","Los campos están vacíos, escribe algo."},
            {"ErrorWhileEmail","Se produjo un error al registrar el correo."},
            {"ThisPersonAlreadyExist","Este correo electrónico o inicio de sesión ya está registrado."},
            {"SomeErrorLogin","Ha ocurrido un error y no pudimos conectarte."},
            {"WrongLoginOrPassword","Nombre de usuario o contraseña incorrectos, inténtalo de nuevo"},
            {"TableTab","Mesa"},
            {"MapTab","Mapa"},
            {"LanguageMenu","Idioma"},
            {"CreationDate","Fecha de creación"},
            {"Owner","El dueño"},
            {"Name","Primer nombre"},
            {"Size","Tamaño"},
            {"Add","Añadir"},
            {"Change","Cambio"},
            {"Delete","Eliminar"},
            {"DeleteAll","Borrar todo"},
            {"AddIfMax","Añadir si la mayoría"},
            {"DeleteLower","Quitar más pequeño"},
            {"Import","Importar desde archivo"},
            {"FileChooseTitle","Selección de archivos"},
            {"SomeError","Algo salió mal, inténtalo de nuevo más tarde."},
            {"MessageDelivered","Mensaje enviado exitosamente"},
            {"AllOk","Todo esta bien"},
            {"DoingCommands","Comando de ejecución"},
            {"InfoTitle","Informacion"},
            {"ErrorWhileAddInflam","Error al intentar agregar Inflamable, ¿completaste todos los campos?"},
            {"YouCantRemoveIt","No tienes permiso para borrar este elemento."},
            {"ItsNotMax","Este no es el elemento máximo."},
            {"InDev","En desarrollo"},
            {"WrongCommand","Comando equivocado. No rompas la aplicación"},
            {"YouCantChangeIt","No estás autorizado para modificar este objeto."},
            {"MessageSended","Mensaje enviado"},
            {"Redactor","Editor"}


    };
    @Override
    protected Object[][] getContents() {
        return resources;
    }
}
