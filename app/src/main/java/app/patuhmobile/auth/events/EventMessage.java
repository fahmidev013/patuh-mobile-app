package app.patuhmobile.auth.events;

/**
 * Created by Fahmi Hakim on 12/08/2018.
 * for SERA
 */

public class EventMessage {
    private String message;

    public EventMessage(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
