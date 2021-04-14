    package essence;

    import java.io.Serializable;
import java.time.OffsetDateTime;

    public class Inflammable extends NotAlive implements Comparable<Inflammable>, Serializable {
        private float burningPower;
        private float size;
        private Thing thing;
        private OffsetDateTime offsettime;
        private String owner = "all";
        private int sizeUI=1;
        private int id;

        public Inflammable(String name, float burningPower, float size,int id){
            super(name);
            this.burningPower = burningPower;
            this.size = size;
            this.id=id;
        }
        public void setId(int id){
            this.id=id;
        }
        public int getId(){
            return this.id;
        }

        public  Inflammable(String name, float burningPower, float size, Thing thing){
            super(name);
            this.burningPower = burningPower;
            this.size = size;
            this.thing = thing;

        }
        public void setSizeUI(int sizeUI){
            this.sizeUI=sizeUI;
        }
        public int getSizeUI(){
            return this.sizeUI;
        }

        public void setOwner(String owner){
            this.owner = owner;
        }
        public String getOwner(){
            return this.owner;
        }

        public OffsetDateTime getOffsettime(){
            return this.offsettime;
        }
        public void setOffsettime(OffsetDateTime d){
            this.offsettime = d;
        }

        public float getBurningPower(){
            return this.burningPower;
        }
        public void setBurningPower(float f){
            this.burningPower = f;
        }

        public void setSize(float s){
            this.size = s;
        }

        public float getSize(){
            return this.size;
        }

        public String getThing(){
            return this.thing.getName();
        }

        public void setThing(Thing th){
            this.thing = th;
        }


        @Override
        public int compareTo(Inflammable in ) {

            return this.getThing().compareTo(in.getThing());
        }


    }
