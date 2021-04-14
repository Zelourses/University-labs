package essence;

public class Inflammable extends NotAlive {
    private float burningPower;
    private float size;

    public  Inflammable(String name, float burningPower, float size){
        super(name);
        this.burningPower = burningPower;
        this.size = size;
        
    }

    public float getBurningPower(){return this.burningPower;}

    public String getSize(){
        class Size{
            float size;
            Size(float size){this.size = size;}

            String getSize(){
                if (size > 0.66)
                    return "большой";
                else if (size <= 0.66 & size >= 0.33)
                    return "средний";
                else
                    return "маленький";
            }
        }

        Size size = new Size(this.size);

        return size.getSize();
    }
}
