package app.patuhmobile.model;

import java.io.Serializable;

/**
 * Created by Fahmi Hakim on 17/09/2018.
 * for SERA
 */
public class TukarKupon implements Serializable{

    private String Id, UserID, MsCouponId, Title, Benefit, Usage, Tnc, PointNeeded, ValidUntil;

    public TukarKupon(String id, String userID, String msCouponId, String title, String benefit, String usage, String tnc, String pointNeeded, String validUntil) {
        Id = id;
        UserID = userID;
        MsCouponId = msCouponId;
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

    public String getUserID() {
        return UserID;
    }

    public String getMsCouponId() {
        return MsCouponId;
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
}
