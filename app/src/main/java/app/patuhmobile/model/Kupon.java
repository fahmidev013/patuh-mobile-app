package app.patuhmobile.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fahmi Hakim on 17/09/2018.
 * for SERA
 */
public class Kupon implements Serializable {

    private String Id, Title, Benefit, Usage, Tnc, PointNeeded, ValidUntil;

    public Kupon(String id, String title, String benefit, String usage, String tnc, String pointNeeded, String validUntil) {
        Id = id;
        Title = title;
        Benefit = benefit;
        Usage = usage;
        Tnc = tnc;
        PointNeeded = pointNeeded;
        ValidUntil = validUntil;
    }

    public String getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public String getBenefit() {
        return Benefit;
    }

    public String getUsage() {
        return Usage;
    }

    public String getTnc() {
        return Tnc;
    }

    public String getPointNeeded() {
        return PointNeeded;
    }

    public String getValidUntil() {
        return ValidUntil;
    }


    /*public static List<Kupon> prepareDesserts(String[] names, String[] descriptions) {
        List<Kupon> desserts = new ArrayList<>(names.length);

        for (int i = 0; i < names.length; i++) {
            Kupon dessert = new Kupon(names[i], descriptions[i]);
            desserts.add(dessert);
        }

        return desserts;
    }*/

}
