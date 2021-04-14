package resources;

import java.util.ListResourceBundle;

public class resources_fi extends ListResourceBundle {
    private static final Object[][] resources ={
            {"ErrorTitle","virhe"},
            {"WrongPortError","Anna numero."},
            {"WrongPortNumberError","Portin on oltava suurempi kuin 0 ja alle 65535"},
            {"Russian","Русский"},
            {"Finnish","suomalainen"},
            {"Shqiptar","Shqiptar"},
            {"Español(Ec)","Español"},
            {"Host","isäntä"},
            {"Port","Satama"},
            {"Launch","käynnistää"},
            {"PortSet","Portin asennus"},
            {"Login","Kirjaudu sisään"},
            {"Register","Rekisteröityminen"},
            {"SignUp","rekisteröinti"},
            {"nickname","käyttäjätunnus"},
            {"password","salasana"},
            {"WrongPortAndHost","Emme voineet muodostaa yhteyttä määrättyihin tietoihin. Yritä uudelleen"},
            {"WriteSomething","Kentät ovat tyhjiä, kirjoita jotain"},
            {"ErrorWhileEmail","Sähköpostin rekisteröinnissä tapahtui virhe."},
            {"ThisPersonAlreadyExist","Tämä sähköposti tai kirjautumistunnus on jo rekisteröity."},
            {"SomeErrorLogin","On tapahtunut virhe ja emme pystyneet yhdistämään sinua."},
            {"WrongLoginOrPassword","Väärä käyttäjätunnus tai salasana, yritä uudelleen"},
            {"TableTab","pöytä"},
            {"MapTab","kartta"},
            {"LanguageMenu","kieli"},
            {"CreationDate","Luontipäivämäärä"},
            {"Owner","Omistaja"},
            {"Name","Etunimi"},
            {"Size","koko"},
            {"Add","Lisää"},
            {"Change","muuttaa"},
            {"Delete","poistaa"},
            {"DeleteAll","Poista kaikki"},
            {"AddIfMax","Lisää, jos eniten"},
            {"DeleteLower","Poista pienempi"},
            {"Import","Tuo tiedostosta"},
            {"FileChooseTitle","Tiedoston valinta"},
            {"SomeError","Jotain meni pieleen, yritä myöhemmin uudelleen."},
            {"MessageDelivered","Viesti lähetettiin onnistuneesti"},
            {"AllOk","Kaikki on hyvin"},
            {"DoingCommands","Komentojen suorittaminen"},
            {"InfoTitle","tiedotus"},
            {"ErrorWhileAddInflam","Virhe yrittäessään lisätä syttyvää, täytitkö kaikki kentät?"},
            {"YouCantRemoveIt","Sinulla ei ole oikeutta poistaa tätä kohdetta."},
            {"ItsNotMax","Tämä ei ole suurin elementti."},
            {"InDev","Kehityksessä"},
            {"WrongCommand","Väärä komento. Älä rikkoa sovellusta"},
            {"YouCantChangeIt","Sinulla ei ole lupaa muuttaa tätä kohdetta."},
            {"MessageSended","Viesti lähetetty"},
            {"Redactor","toimittaja"}
    };
    @Override
    protected Object[][] getContents() {
        return resources;
    }

}
