package essence;


public class FlameAbleNotAlive extends NotAlive implements FlameAble, toActionAble{

    private float fuel;
    private float power;

    public FlameAbleNotAlive(String name, float power){
        super(name);

        if (power < 0 | power > 1)
            throw new WrongPower("непрвильное значение мощности", power);

        this.power = power;
    }

    public FlameAbleNotAlive(String name, float power, float fuel){
        this(name, power);
        this.fuel = fuel;
    }

    public void addFuel(Inflammable fuel){
        System.out.print("подбросил в " + this.getName() + " " + fuel.getName() + " ");
        this.fuel += fuel.getBurningPower();
    }

    @Override
    public void flame() throws NoFuelException{

        if (this.fuel - this.power < 0)
            throw new NoFuelException("Недостаточно топлива", this.fuel, this.getName() + " потухает");

        System.out.print(this.getName() + " горит и " + getBrightness() + " ");
        lightUp((Essence)this.getHaveAble());

        this.fuel -= this.power;
    }

    @Override
    public void lightUp(Essence essence) {
            System.out.println(this.getName() + " освещает " + essence.getName());
    }

    @Override
    public void _do(ActionAble actionAble) {
        actionAble.invoke();
    }

    public String getBrightness(){
        if (power > 0.66)
            return "Ярко";
        else if (power <= 0.66 & power >= 0.33)
            return "теплым, живым светом";
        else
            return "тускло";
    }
}
