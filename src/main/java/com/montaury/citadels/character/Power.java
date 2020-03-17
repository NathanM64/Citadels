package com.montaury.citadels.character;

import com.montaury.citadels.round.Group;
import io.vavr.collection.List;

public enum Power {
    ASSASSIN("Kill"),
    THIEF("Rob"),
    MAGICIAN("Exchange cards with other player","Exchange card with pile"),
    KING("Receive income"),
    BISHOP("Receive income"),
    MERCHANT("Receive income", "Receive 1 gold"),
    ARCHITECT("Build district", "Pick 2 cards"),
    WARLORD("Destroy district", "Receive income");

    private String action;
    private String action2;
    private String action3;
    Power(String action) {
        this.action = action;
    }
    Power(String action, String action2) {
        this.action = action;
        this.action = action2;
    }
    Power(String action, String action2, String action3) {
        this.action = action;
        this.action = action2;
        this.action = action3;
    }

    public String getAction() {
        return this.action;
    }
    public String getAction2() {
        return this.action2;
    }
    public String getAction3() {
        return this.action3;
    }


    public static List<Power> assignment(Group group, List<Power> powers){


            if (group.character == Character.ASSASSIN) {
                powers = List.of(Power.ASSASSIN);
            } else if (group.character == Character.THIEF) {
                powers = List.of(Power.THIEF);
            } else if (group.character == Character.MAGICIAN) {
                powers = List.of(Power.MAGICIAN);
            } else if (group.character == Character.KING) {
                powers = List.of(Power.KING);
            } else if (group.character == Character.BISHOP) {
                powers = List.of(Power.BISHOP);
            } else if (group.character == Character.MERCHANT) {
                powers = List.of(Power.MERCHANT);
            } else if (group.character == Character.ARCHITECT) {
                powers = List.of(Power.ARCHITECT);
            } else if (group.character == Character.WARLORD) {
                powers = List.of(Power.WARLORD);
            } else {
                System.out.println("Uh oh");
            }

        return powers;
    }




}
