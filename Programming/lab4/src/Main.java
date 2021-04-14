import essence.*;

public class Main {
    public static void main(String[] args) {
        Place komnata = new Place("Комната");

        Human karlson = new Human("Карлсон");

        karlson.enter(komnata);

        Action vstalIpodbral = new Action("встал", "");

        karlson._do(vstalIpodbral);

        try {
            FlameAbleNotAlive kamin = new FlameAbleNotAlive("Камин", -0.75f, 0f);
        }
        catch (WrongPower ex){
            System.out.println(ex.getMessage() + " " + ex.getPower());
        }

        FlameAbleNotAlive kamin = new FlameAbleNotAlive("Камин", 0.75f, 0f);

        kamin.enter(komnata);

        Inflammable[] poleno = new Inflammable[2];
        poleno[0] = new Inflammable("березовое полено 1", 0.7f, 0.75f);
        poleno[1] = new Inflammable("березовое полено 2", 0.7f, 0.75f);

        karlson._do(new ActionAble() {
            @Override
            public void invoke() {
                for (int i = 0; i < 2; i++) {
                    kamin.addFuel(poleno[i]);
                }
                System.out.println();
            }
        });

        try {
            kamin.flame();
            kamin.flame();
        }
        catch (NoFuelException ex){
            System.out.println(ex.getMessage() + " " + ex.getFuel() + " " + ex.getEffects());
        }

        Thing temno = new Thing("темно");

        temno.enter(komnata);

        temno._do(new ActionAble() {
            @Override
            public void invoke() {
                System.out.println("в " + temno.getHaveAble().getName() + " стало " + temno.getName());
            }
        });

        FlameAbleNotAlive lampa = new FlameAbleNotAlive("керосиновая лампа", 0.35f, 2f);

        lampa.enter(komnata);

        karlson._do(new ActionAble() {
            @Override
            public void invoke() {
                System.out.println("поджигает " + lampa.getName());
            }
        });


        Place verstak = new Place("Верстак");

        Thing veshi = new Thing("Вещи");

        veshi.enter(verstak);

        try {
            lampa.flame();
        }
        catch (NoFuelException ex){
            System.out.println(ex.getMessage() + " " + ex.getFuel() + " " + ex.getEffects());
        }

        lampa.lightUp(veshi);
        lampa.lightUp(verstak);

    }

    private static class Action implements ActionAble{
        private String name;
        private String addition;

        public Action(String name, String addition){
            this.name = name;
            this.addition = addition;
        }

        @Override
        public void invoke() {
            System.out.println(this.name + " " + this.addition);
        }
    }
}
