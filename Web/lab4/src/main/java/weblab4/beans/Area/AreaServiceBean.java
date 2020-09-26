package weblab4.beans.Area;
import weblab4.beans.Mbeans.AverageClicks;
import weblab4.beans.Mbeans.Counter;

import javax.ejb.Stateless;
import javax.management.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;

@Stateless
public class AreaServiceBean implements AreaService{
    Counter counter;
    AverageClicks avg;
    public AreaServiceBean(){
        try {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("weblab4.beans.Mbeans:type=Counter");
        counter = new Counter();
        avg = new AverageClicks();
        ObjectName avgName = new ObjectName("weblab4.beans.Mbeans:type=AverageClicks");
            mbs.registerMBean(counter,name);
            mbs.registerMBean(avg,avgName);

        } catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
            e.printStackTrace();
        }
    }
    private static final BigDecimal MINUS_ONE = BigDecimal.valueOf(-1);
    @Override
    public boolean checkPoint(
            @DecimalMin(value = "-3", inclusive = true)
            @DecimalMax(value = "5", inclusive = true)
            @NotNull BigDecimal x,
            @DecimalMin(value = "-3", inclusive = true)
            @DecimalMax(value = "3", inclusive = true)
            @NotNull BigDecimal y,
            @DecimalMin(value = "-3", inclusive = true)
            @DecimalMax(value = "5", inclusive = true)
            @NotNull BigDecimal r
    ) {

        boolean b;
        if (r.compareTo(BigDecimal.ZERO) < 0){
            b= checkPoints(x.multiply(MINUS_ONE),y.multiply(MINUS_ONE),r.multiply(MINUS_ONE));
        }else {
            b = checkPoints(x,y,r);
        }

        if (b){
            counter.addHits();
        }

        counter.addPoints();
        avg.addClickTime();
        return b;
    }

    private boolean checkPoints(BigDecimal x,BigDecimal y,BigDecimal r){
        final BigDecimal halfR =r.multiply(new BigDecimal("0.5"));

        return ((x.compareTo(BigDecimal.ZERO) <=0 && y.compareTo(BigDecimal.ZERO) >=0
                    && y.compareTo(x.add(halfR)) <=0) ||
                (x.compareTo(BigDecimal.ZERO) >= 0 && y.compareTo(BigDecimal.ZERO) <= 0 &&
                        x.compareTo(r.multiply(MINUS_ONE)) >= 0 &&  y.compareTo(halfR.multiply(MINUS_ONE)) >= 0) ||
                (x.compareTo(BigDecimal.ZERO)>=0 && y.compareTo(BigDecimal.ZERO) <= 0 &&
                        x.multiply(x).add(y.multiply(y)).compareTo(r.multiply(r)) <= 0)
        );
    }
}
