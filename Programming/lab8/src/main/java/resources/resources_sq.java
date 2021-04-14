package resources;

import java.util.ListResourceBundle;

public class resources_sq extends ListResourceBundle {
    private  static final Object[][] resources = {
            {"ErrorTitle","gabim"},
            {"WrongPortError","Fut një numër."},
            {"WrongPortNumberError","Porti duhet të jetë më i madh se 0 dhe më pak se 65535"},
            {"Russian","Русский"},
            {"Finnish","suomalainen"},
            {"Shqiptar","Shqiptar"},
            {"Español(Ec)","Español"},
            {"Host","mikpritës"},
            {"Port","Porti"},
            {"Launch","lëshim"},
            {"PortSet","Instalimi i portit"},
            {"Login","Identifikohu"},
            {"Register","Për t'u regjistruar"},
            {"SignUp","regjistrim"},
            {"nickname","Emri i përdoruesit"},
            {"password","fjalëkalim"},
            {"WrongPortAndHost","Ne nuk mund të lidhemi me të dhënat e specifikuara. Provo sërish"},
            {"WriteSomething","Fushat janë bosh, shkruaj diçka"},
            {"ErrorWhileEmail","Ndodhi një gabim gjatë regjistrimit të postës"},
            {"ThisPersonAlreadyExist","Kjo email ose hyrje është regjistruar"},
            {"SomeErrorLogin","Ka ndodhur një gabim dhe nuk ishim në gjendje t'ju lidhim."},
            {"WrongLoginOrPassword","Identifikimi ose fjalëkalimi i gabuar, provo përsëri"},
            {"TableTab","Tabelë"},
            {"MapTab","Hartë"},
            {"LanguageMenu","Gjuhë"},
            {"CreationDate","Data e krijimit"},
            {"Owner","Pronari"},
            {"Name","Emri"},
            {"Size","Madhësi"},
            {"Add","Për të shtuar"},
            {"Change","Ndryshoj"},
            {"Delete","Fshij"},
            {"DeleteAll","Fshini të gjitha"},
            {"AddIfMax","Shto nëse shumica"},
            {"DeleteLower","Hiq më të vogla"},
            {"Import","Importo nga skedari"},
            {"FileChooseTitle","Zgjedhja e skedarit"},
            {"SomeError","Diçka shkoi keq, provo përsëri më vonë."},
            {"MessageDelivered","Mesazhi u dërgua me sukses"},
            {"AllOk","Gjithçka është mirë"},
            {"DoingCommands","Ekzekutimi i komandës"},
            {"InfoTitle","Informacion"},
            {"ErrorWhileAddInflam","Gabim në përpjekjen për të shtuar ndezës, a keni plotësuar të gjitha fushat?"},
            {"YouCantRemoveIt","Nuk ke leje ta fshish këtë artikull"},
            {"ItsNotMax","Ky nuk është elementi maksimal."},
            {"InDev","Në zhvillim"},
            {"WrongCommand","Komandë e gabuar. Mos e prishni aplikacionin"},
            {"YouCantChangeIt","Ju nuk keni leje për të modifikuar këtë objekt."},
            {"MessageSended","Mesazhi u dërgua"},
            {"Redactor","redaktor"}

    };
    @Override
    protected Object[][] getContents() {
      return resources;
    }
}
