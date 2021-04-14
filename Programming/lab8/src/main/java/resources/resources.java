package resources;

import java.util.ListResourceBundle;

public class resources extends ListResourceBundle {
    private static final Object[][] resources =
            {
                    {"ErrorTitle","Ошибка"},
                    {"WrongPortError","Пожалуйста, введите число."},
                    {"WrongPortNumberError","Порт должен быть больше 0 и меньше 65535"},
                    {"Russian","Русский"},
                    {"Finnish","suomalainen"},
                    {"Shqiptar","Shqiptar"},
                    {"Español(Ec)","Español"},
                    {"Host","Хост"},
                    {"Port","Порт"},
                    {"Launch","Запуск"},
                    {"PortSet","Установка порта"},
                    {"Login","Войти"},
                    {"Register","Зарегестрироваться"},
                    {"SignUp","Регистрация"},
                    {"nickname","Имя пользователя"},
                    {"password","Пароль"},
                    {"WrongPortAndHost","Мы не смогли подключится по указаным данным. Попробуйте ещё раз"},
                    {"WriteSomething","Поля пусты, напишите что-нибудь"},
                    {"ErrorWhileEmail","Произошла ошибка при регистрации почты"},
                    {"ThisPersonAlreadyExist","Такая почта или логин уже зарегестрированы"},
                    {"SomeErrorLogin","Произошла ошибка, и мы не смогли подключить вас"},
                    {"WrongLoginOrPassword","Неправильный логин или пароль, попробуйте ещё раз"},
                    {"TableTab","Таблица"},
                    {"MapTab","Карта"},
                    {"LanguageMenu","Язык"},
                    {"CreationDate","Дата создания"},
                    {"Owner","Владелец"},
                    {"Name","Имя"},
                    {"Size","Размер"},
                    {"Add","Добавить"},
                    {"Change","Изменить"},
                    {"Delete","Удалить"},
                    {"DeleteAll","Удалить всё"},
                    {"AddIfMax","Добавить если больше всех"},
                    {"DeleteLower","Удалить меньшие"},
                    {"Import","Импорт из файла"},
                    {"FileChooseTitle","Выбор файла"},
                    {"SomeError","Произошла какая-то ошибка, попробуйте позже"},
                    {"MessageDelivered","Сообщение успешно отправлено"},
                    {"AllOk","Всё хорошо"},
                    {"DoingCommands","Выполнение команд"},
                    {"InfoTitle","Информация"},
                    {"ErrorWhileAddInflam","ошибка при попытке добавить Inflammable, вы заполнили все поля?"},
                    {"YouCantRemoveIt","У вас нет прав удалять этот элемент"},
                    {"ItsNotMax","Это не максимальный элемент"},
                    {"InDev","В разработке"},
                    {"WrongCommand","Неправильная команда. Не ломайте приложение"},
                    {"YouCantChangeIt","У вас нет прав изменять этот объект"},
                    {"MessageSended","Сообщение отправлено"},
                    {"Redactor","Редактор"}
            };

    @Override
    protected Object[][] getContents() {
        return resources;
    }
}
