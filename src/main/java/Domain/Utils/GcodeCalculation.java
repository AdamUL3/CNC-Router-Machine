package Domain.Utils;


import Domain.Coupe;
import Domain.Outil;

import java.util.List;

public class GcodeCalculation{
    public static int calculateSpindleSpeed(Outil outil) {
        return Math.min(Math.max((int)(outil.getDiametre() * 1000), 1000), 10000);
    }

    public static int calculateFeedRate(Outil outil) {
        return Math.min(Math.max((int)(outil.getDiametre() * 100), 500), 2000);
    }

    public static int calculatePassDepth(Outil outil) {
        return Math.max((int)(outil.getDiametre() / 2), 1);
    }

    public static Coordinates findFirstCutPoint(List<Coupe> coupeListe) {
        if (coupeListe != null && !coupeListe.isEmpty() &&
                !coupeListe.get(0).getLignes().isEmpty()) {
            return coupeListe.get(0).getLignes().get(0).getPoint1();
        }
        return null;
    }
}