package essence;

public class NoFuelException extends Exception{
    private float fuel;
    private String effects;

    public float getFuel() {
        return fuel;
    }

    public String getEffects() {
        return effects;
    }

    public NoFuelException(String msg, float fuel, String effects){
        super(msg);
        this.fuel = fuel;
        this.effects = effects;
    }
}
