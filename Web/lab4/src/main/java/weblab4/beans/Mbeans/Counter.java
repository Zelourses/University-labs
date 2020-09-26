package weblab4.beans.Mbeans;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class Counter extends NotificationBroadcasterSupport implements CounterMBean {
    private int points, hits =0;
    private int sequenceNumber=1;
    @Override
    public void addPoints() {
        points++;
        if (points % 10 ==0){
            Notification n = new AttributeChangeNotification(this,
                    sequenceNumber++,
                    System.currentTimeMillis(),
                    "User clicked % 10 times!",
                    "points",
                    "int",
                    points-1,
                    points);
            sendNotification(n);
        }
    }

    @Override
    public void addHits() {
        hits++;
    }

    @Override
    public int getHits() {
        return hits;
    }


    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[]{
                AttributeChangeNotification.ATTRIBUTE_CHANGE
        };

        String name = AttributeChangeNotification.class.getName();
        String description = "MBean that counts number of users hits and clicks on canvas";
        MBeanNotificationInfo info =
                new MBeanNotificationInfo(types, name, description);
        return new MBeanNotificationInfo[]{info};
    }

}
