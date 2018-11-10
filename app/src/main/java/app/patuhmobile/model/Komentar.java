package app.patuhmobile.model;

import java.io.Serializable;

/**
 * Created by Fahmi Hakim on 05/08/2018.
 * for SERA
 */

public class Komentar implements Serializable{

    private int Id;
    private String Comment, cCreated;

    public Komentar(int id, String comment, String cCreated) {
        Id = id;
        Comment = comment;
        this.cCreated = cCreated;
    }

    public String getUserId() {
        return cCreated;
    }

    public int getId() {
        return Id;
    }

    public String getComment() {
        return Comment;
    }
}
