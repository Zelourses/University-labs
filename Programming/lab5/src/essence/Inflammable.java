    package essence;

    import java.util.Date;

    public class Inflammable extends NotAlive implements Comparable<String>{
        private float burningPower;
        private float size;
        private Thing thing;
        private Date date;

        public  Inflammable(String name, float burningPower, float size, Thing thing){
            super(name);
            this.burningPower = burningPower;
            this.size = size;
            this.thing = thing;

        }
        public  Inflammable(String name, float burningPower, float size, Thing thing, Date date){
            this(name,burningPower,size,thing);

            this.date = date;

        }

        public Date getDate(){return date;}

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
        public String getThing(){return this.thing.getName();}


        @Override
        public int compareTo(String in ) {

            return this.getThing().compareTo(in);
        }


    }
