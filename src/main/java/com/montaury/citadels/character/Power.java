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
    WARLORD("Receive income", "Destroy district");

    private String action;
    private String action2;
    private String action3;
    Power(String action) {
        this.action = action;
    }
    Power(String action, String action2) {
        this.action = action;
        this.action2 = action2;
    }
    Power(String action, String action2, String action3) {
        this.action = action;
        this.action2 = action2;
        this.action3 = action3;
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


    public static List<String> assignment(Group group, List<String> powers){


        if (group.character == Character.ASSASSIN) {
            powers = List.of(Power.ASSASSIN.getAction());
        } else if (group.character == Character.THIEF) {
            powers = List.of(Power.THIEF.getAction());
        } else if (group.character == Character.MAGICIAN) {
            powers = List.of(Power.MAGICIAN.getAction(), Power.MAGICIAN.getAction2());
        } else if (group.character == Character.KING) {
            powers = List.of(Power.KING.getAction());
        } else if (group.character == Character.BISHOP) {
            powers = List.of(Power.BISHOP.getAction());
        } else if (group.character == Character.MERCHANT) {
            powers = List.of(Power.MERCHANT.getAction(), Power.MERCHANT.getAction2());
        } else if (group.character == Character.ARCHITECT) {
            powers = List.of(Power.ARCHITECT.getAction(), Power.ARCHITECT.getAction2());
        } else if (group.character == Character.WARLORD) {
            powers = List.of(Power.WARLORD.getAction(), Power.WARLORD.getAction2());
        } else if (group.character == Character.ALCHEMIST) {
            powers = List.empty();
        }else if (group.character == Character.BAILLI) {
            powers = List.empty();
        }
        else {
            System.out.println("Uh oh");
        }

        return powers;
    }




}