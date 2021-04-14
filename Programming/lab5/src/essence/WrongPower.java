package essence;

public class WrongPower extends RuntimeException {
    private float power;

    public float getPower(){return this.power;}

    public WrongPower(String msg, float power){
        super(msg);
        this.power = power;
    }
}
