package com.sarahisweird.dogecraft.towns;

import java.util.ArrayList;
import java.util.List;

public class Town {
    private String name;
    private String owner;
    /** A list of UUIDs. */
    private List<String> members = new ArrayList<>();

    public Town(String name, String owner) {
        this.name = name;
        this.owner = owner;
        this.members.add(owner);
    }
}
