package com.alwaysallthetime.adnlib.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrivateMessage extends Message {
    private List<String> destinations;

    public PrivateMessage() {}

    public PrivateMessage(String text) {
        super(text);
    }

    public PrivateMessage(boolean machineOnly) {
        super(machineOnly);
    }

    public PrivateMessage(String text, List<String> destinations) {
        super(text);
        this.destinations = destinations;
    }

    public PrivateMessage(String text, String... destinations) {
        this(text, Arrays.asList(destinations));
    }

    public PrivateMessage(boolean machineOnly, List<String> destinations) {
        super(machineOnly);
        this.destinations = destinations;
    }

    public PrivateMessage(boolean machineOnly, String... destinations) {
        this(machineOnly, Arrays.asList(destinations));
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public void addDestination(String destination) {
        if (destinations == null)
            destinations = new ArrayList<String>();

        destinations.add(destination);
    }
}