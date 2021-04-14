    package essence;

    import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

    public class Inflammable extends NotAlive implements Comparable<Inflammable>, Serializable {
        private float burningPower;
        private float size;
        private Thing thing;
        private ZonedDateTime zonedtime;
        private OffsetDateTime offsettime;
        private LocalDateTime localtime;
        private String owner = "all";

        public Inflammable(String name, float burningPower, float size){
            super(name);
            this.burningPower = burningPower;
            this.size = size;
        }

        public  Inflammable(String name, float burningPower, float size, Thing thing){
            super(name);
            this.burningPower = burningPower;
            this.size = size;
            this.thing = thing;

        }

        public void setOwner(String owner){
            this.owner = owner;
        }
        public String getOwner(){
            return this.owner;
        }

        public ZonedDateTime getZonedtime(){
            return this.zonedtime;
        }
        public void setZonedtime(ZonedDateTime d){
            this.zonedtime = d;
        }
        public OffsetDateTime getOffsettime(){
            return this.offsettime;
        }
        public void setOffsettime(OffsetDateTime d){
            this.offsettime = d;
        }

        public  LocalDateTime getLocaltime(){
            return this.localtime;
        }

        public void setLocaltime(LocalDateTime d){
            this.localtime = d;
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
