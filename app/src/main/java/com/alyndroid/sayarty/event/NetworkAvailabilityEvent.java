package com.alyndroid.sayarty.event;

/**
 * Created by George on 1/12/2019 at 3:19 AM
 */
public class NetworkAvailabilityEvent {
    private boolean available;

    public NetworkAvailabilityEvent(boolean available) {
        this.setAvailable(available);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
