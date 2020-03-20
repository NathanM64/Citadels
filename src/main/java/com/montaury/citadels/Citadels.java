package com.montaury.citadels;

import com.montaury.citadels.character.Character;
import com.montaury.citadels.character.Power;
import com.montaury.citadels.character.RandomCharacterSelector;
import com.montaury.citadels.district.Card;
import com.montaury.citadels.district.District;
import com.montaury.citadels.district.DistrictType;
import com.montaury.citadels.player.ComputerController;
import com.montaury.citadels.player.HumanController;
import com.montaury.citadels.player.Player;
import com.montaury.citadels.round.GameRoundAssociations;
import com.montaury.citadels.round.Group;
import com.montaury.citadels.round.action.DestroyDistrictAction;
import io.vavr.Tuple;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;

import java.util.Collections;
import java.util.Scanner;

public class Citadels {

    private static final String DRAW_2_CARDS_AND_KEEP_1 = "Draw 2 cards and keep 1";
    private static final String RECEIVE_2_COINS = "Receive 2 coins";
    private static final String DRAW_3_CARDS_AND_KEEP_1 = "Draw 3 cards and keep 1";
    private static final String EXCHANGE_CARDS_WITH_PILE = "Exchange cards with pile";
    private static final String RECEIVE_INCOME = "Receive income";
    private static final String PICK_2_CARDS = "Pick 2 cards";
    private static final String BUILD_DISTRICT = "Build district";
    private static final String DESTROY_DISTRICT = "Destroy district";
    private static final String DRAW_3_CARDS_FOR_2_COINS = "Draw 3 cards for 2 coins";
    private static final String DISCARD_CARD_FOR_2_COINS = "Discard card for 2 coins";
    private static final String RECEIVE_1_GOLD = "Receive 1 gold";
    private static final String KILL = "Kill";
    private static final String ROB = "Rob";
    private static final String EXCHANGE_CARDS_WITH_PLAYER = "Exchange cards with other player";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello! Quel est votre nom ? ");
        String playerName = scanner.next();
        System.out.println("Quel est votre age ? ");
        String ageSaisi;
        int playerAge=0;
        do {
            System.out.println("Saisir un âge concret ");
            ageSaisi = scanner.next();
            try {
                playerAge = Integer.parseInt(ageSaisi);
            }catch(NumberFormatException e){ }
        }while(!(playerAge>0 && playerAge<100 ));
        Board board = new Board();
        Player p = new Player(playerName, playerAge, new City(board), new HumanController());
        p.human = true;
        List<Player> players = List.of(p);
        System.out.println("Saisir le nombre de joueurs total (entre 2 et 8): ");
        int nbP;
        do {
            nbP = scanner.nextInt();
        } while (nbP < 2 || nbP > 8);
        for (int joueurs = 0; joueurs < nbP; joueurs += 1) {
            Player player = new Player("Computer " + joueurs, 35, new City(board), new ComputerController());
            player.computer = true;
            players = players.append(player
            );
        }
        CardPile pioche = new CardPile(Card.all().toList().shuffle());
        players.forEach(player -> {
            player.add(2);
            player.add(pioche.draw(2));
        });
        Player crown = players.maxBy(Player::age).get();

        List<Group> roundAssociations;
        do {
            java.util.List<Player> list = players.asJavaMutable();
            Collections.rotate(list, -players.indexOf(crown));
            List<Player> playersInOrder = List.ofAll(list);
            RandomCharacterSelector randomCharacterSelector = new RandomCharacterSelector();
            List<Character> availableCharacters = List.of(Character.ASSASSIN, Character.THIEF, Character.MAGICIAN, Character.KING, Character.BISHOP, Character.MERCHANT, Character.ARCHITECT, Character.WARLORD, Character.ALCHEMIST, Character.BAILLI);

            List<Character> availableCharacters1 = availableCharacters;
            List<Character> discardedCharacters = List.empty();
            for (int i = 0; i < 1; i++) {
                Character discardedCharacter = randomCharacterSelector.among(availableCharacters1);
                discardedCharacters = discardedCharacters.append(discardedCharacter);
                availableCharacters1 = availableCharacters1.remove(discardedCharacter);
            }
            Character faceDownDiscardedCharacter = discardedCharacters.head();
            availableCharacters = availableCharacters.remove(faceDownDiscardedCharacter);

            List<Character> availableCharacters11 = availableCharacters.remove(Character.KING);
            List<Character> discardedCharacters1 = List.empty();
            for (int i = 0; i < 7 - playersInOrder.size() - 1; i++) {
                Character discardedCharacter = randomCharacterSelector.among(availableCharacters11);
                discardedCharacters1 = discardedCharacters1.append(discardedCharacter);
                availableCharacters11 = availableCharacters11.remove(discardedCharacter);
            }
            List<Character> faceUpDiscardedCharacters = discardedCharacters1;
            availableCharacters = availableCharacters.removeAll(faceUpDiscardedCharacters);

            List<Group> associations1 = List.empty();
            for (Player player : playersInOrder) {
                System.out.println(player.name() + " doit choisir un personnage");
                availableCharacters = availableCharacters.size() == 1 && playersInOrder.size() == 7 ? availableCharacters.append(faceDownDiscardedCharacter) : availableCharacters;
                Character selectedCharacter = player.controller.selectOwnCharacter(availableCharacters, faceUpDiscardedCharacters);
                availableCharacters = availableCharacters.remove(selectedCharacter);
                associations1 = associations1.append(new Group(player, selectedCharacter));
            }
            List<Group> associations = associations1;
            GameRoundAssociations groups = new GameRoundAssociations(associations);

            for (int iii = 0; iii < 8; iii++) {
                for (int ii = 0; ii < associations.size(); ii++) {
                    if (iii + 1 == associations.get(ii).character.number() && !associations.get(ii).isMurdered()) {
                        Group group = associations.get(ii);
                        associations.get(ii).thief().peek(thief -> thief.steal(group.player()));
                        Set<String> baseActions = HashSet.of(DRAW_2_CARDS_AND_KEEP_1, RECEIVE_2_COINS);
                        List<District> districts = group.player().city().districts();
                        Set<String> availableActions = baseActions;
                        for (District d : districts) {
                            if (d == District.OBSERVATORY) {
                                availableActions = availableActions.replace(DRAW_2_CARDS_AND_KEEP_1, DRAW_3_CARDS_AND_KEEP_1);
                            }
                        }
                        // keep only actions that player can realize
                        List<String> possibleActions = List.empty();
                        for (String action : availableActions) {
                            if ((action.equals(DRAW_2_CARDS_AND_KEEP_1) && (pioche.canDraw(2))) || (action.equals(DRAW_3_CARDS_AND_KEEP_1) && (pioche.canDraw(3)))) {
                                possibleActions = possibleActions.append(DRAW_2_CARDS_AND_KEEP_1);
                            } else {
                                possibleActions = possibleActions.append(action);
                            }
                        }
                        String actionType = group.player().controller.selectActionAmong(possibleActions.toList());
                        // execute selected action
                        switch (actionType) {
                            case DRAW_2_CARDS_AND_KEEP_1: {
                                Set<Card> cardsDrawn = pioche.draw(2);
                                isDistrictLibrary(pioche, group, cardsDrawn);
                                break;
                            }
                            case RECEIVE_2_COINS:
                                group.player().add(2);
                                break;
                            case DRAW_3_CARDS_AND_KEEP_1:
                                Set<Card> cardsDrawn = pioche.draw(3);
                                isDistrictLibrary(pioche, group, cardsDrawn);
                                break;
                            default:
                                break;
                        }

                        actionExecuted(group, actionType, associations);

                        // receive powers from the character
                        List<String> powers = null;
                        powers = Power.assignment(group, powers);

                        List<String> extraActions = List.empty();
                        for (District d : group.player().city().districts()) {
                            if (d == District.SMITHY) {
                                extraActions = extraActions.append(DRAW_3_CARDS_FOR_2_COINS);
                            } else if (d == District.LABORATORY) {
                                extraActions = extraActions.append(DISCARD_CARD_FOR_2_COINS);
                            }
                        }
                        Set<String> availableActions11 = Group.OPTIONAL_ACTIONS
                                .addAll(powers)
                                .addAll(extraActions);
                        String actionType11;
                        do {
                            Set<String> availableActions1 = availableActions11;
                            // keep only actions that player can realize
                            List<String> possibleActions2 = List.empty();
                            for (String action : availableActions1) {
                                switch (action) {
                                    case BUILD_DISTRICT:
                                        if (!group.player().buildableDistrictsInHand().isEmpty())
                                            possibleActions2 = possibleActions2.append(BUILD_DISTRICT);
                                        break;
                                    case DESTROY_DISTRICT:
                                        if (DestroyDistrictAction.districtsDestructibleBy(groups, group.player()).exists(districtsByPlayer -> !districtsByPlayer._2().isEmpty())) {
                                            possibleActions2 = possibleActions2.append(DESTROY_DISTRICT);
                                        }
                                        break;
                                    case DISCARD_CARD_FOR_2_COINS:
                                        if (!group.player().cards().isEmpty()) {
                                            possibleActions2 = possibleActions2.append(DISCARD_CARD_FOR_2_COINS);
                                        }
                                        break;
                                    case DRAW_3_CARDS_FOR_2_COINS:
                                        if (pioche.canDraw(3) && group.player().canAfford(2)) {
                                            possibleActions2 = possibleActions2.append(DRAW_3_CARDS_FOR_2_COINS);
                                        }
                                        break;
                                    case EXCHANGE_CARDS_WITH_PILE:
                                        if (!group.player().cards().isEmpty() && pioche.canDraw(1)) {
                                            possibleActions2 = possibleActions2.append(EXCHANGE_CARDS_WITH_PILE);
                                        }
                                        break;
                                    case PICK_2_CARDS:
                                        if (pioche.canDraw(2))
                                            possibleActions2 = possibleActions2.append(PICK_2_CARDS);
                                        break;
                                    default:
                                        possibleActions2 = possibleActions2.append(action);
                                        break;
                                }
                            }
                            String actionType1 = group.player().controller.selectActionAmong(possibleActions2.toList());
                            // execute selected action
                            switch (actionType1) {
                                case "End round": {
                                    if (group.player().gold() > 0) {
                                        for (Group gr : groups.associations) {
                                            if (gr.character().equals(Character.BAILLI)) {
                                                gr.player().add(1);
                                                group.player().pay(1);
                                            }
                                        }
                                    }
                                    break;
                                }
                                case BUILD_DISTRICT: {
                                    Card card = group.player().controller.selectAmong(group.player().buildableDistrictsInHand());
                                    group.player().buildDistrict(card);
                                    if (group.character().equals(Character.ALCHEMIST)) {
                                        group.player().add(card.district().cost());
                                    }
                                    break;
                                }


                                case DISCARD_CARD_FOR_2_COINS: {
                                    Player player = group.player();
                                    Card card = player.controller.selectAmong(player.cards());
                                    player.cards = player.cards().remove(card);
                                    pioche.discard(card);
                                    player.add(2);
                                    break;
                                }
                                case DRAW_3_CARDS_FOR_2_COINS:
                                    group.player().add(pioche.draw(3));
                                    group.player().pay(2);
                                    break;
                                case EXCHANGE_CARDS_WITH_PILE:
                                    Set<Card> cardsToSwap = group.player().controller.selectManyAmong(group.player().cards());
                                    group.player().cards = group.player().cards().removeAll(cardsToSwap);
                                    group.player().add(pioche.swapWith(cardsToSwap.toList()));
                                    break;
                                case EXCHANGE_CARDS_WITH_PLAYER:
                                    Player playerToSwapWith = group.player().controller.selectPlayerAmong(groups.associations.map(Group::player).remove(group.player()));
                                    group.player().exchangeHandWith(playerToSwapWith);
                                    break;
                                case KILL:
                                    Character characterToMurder = group.player().controller.selectAmong(List.of(Character.THIEF, Character.MAGICIAN, Character.KING, Character.BISHOP, Character.MERCHANT, Character.ARCHITECT, Character.WARLORD, Character.ALCHEMIST, Character.BAILLI));
                                    groups.associationToCharacter(characterToMurder).peek(Group::murder);
                                    break;
                                case PICK_2_CARDS:
                                    group.player().add(pioche.draw(2));
                                    break;
                                case RECEIVE_2_COINS:
                                    group.player().add(2);
                                    break;
                                case RECEIVE_1_GOLD:
                                    group.player().add(1);
                                    break;
                                case RECEIVE_INCOME:
                                    DistrictType type = null;
                                    switch (group.character) {
                                        case BISHOP:
                                            type = DistrictType.RELIGIOUS;
                                            break;
                                        case WARLORD:
                                            type = DistrictType.MILITARY;
                                            break;
                                        case KING:
                                            type = DistrictType.NOBLE;
                                            break;
                                        case MERCHANT:
                                            type = DistrictType.TRADE;
                                            break;
                                        default:
                                            break;
                                    }

                                    if (type != null) {
                                        for (District d : group.player().city().districts()) {
                                            if (d.districtType() == type) {
                                                group.player().add(1);
                                            }
                                            if (d == District.MAGIC_SCHOOL) {
                                                group.player().add(1);
                                            }
                                        }
                                    }
                                    break;
                                case DESTROY_DISTRICT:
                                    destroyDistrict();
                                    break;
                                case ROB:
                                    Character character = group.player().controller.selectAmong(List.of(Character.MAGICIAN, Character.KING, Character.BISHOP, Character.MERCHANT, Character.ARCHITECT, Character.WARLORD,Character.ALCHEMIST, Character.BAILLI)
                                            .removeAll(groups.associations.find(Group::isMurdered).map(Group::character)));
                                    groups.associationToCharacter(character).peek(association -> association.stolenBy(group.player()));
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + actionType1);
                            }
                            actionExecuted(group, actionType1, associations);
                            actionType11 = actionType1;
                            availableActions11 = availableActions11.remove(actionType11);
                        }
                        while (!availableActions11.isEmpty() && !actionType11.equals("End round"));
                    }
                }
            }
            roundAssociations = associations;
            crown = roundAssociations.find(a -> a.character == Character.KING).map(Group::player).getOrElse(crown);
        } while (!players.map(Player::city).exists(City::isComplete));

        // classe les joueurs par leur score
        // si ex-aequo, le premier est celui qui n'est pas assassiné
        // si pas d'assassiné, le gagnant est le joueur ayant eu le personnage avec le numéro d'ordre le plus petit au dernier tour
        System.out.println("Classement: " + roundAssociations.sortBy(a -> Tuple.of(a.player().score(), !a.isMurdered(), a.character))
                .reverse()
                .map(Group::player));
    }

    private static void isDistrictLibrary(CardPile pioche, Group group, Set<Card> cardsDrawn) {
        if (!group.player().city().has(District.LIBRARY)) {
            Card keptCard = group.player().controller.selectAmong(cardsDrawn);
            pioche.discard(cardsDrawn.remove(keptCard).toList());
            cardsDrawn = HashSet.of(keptCard);
        }
        group.player().add(cardsDrawn);
    }

    public static void actionExecuted(Group association, String actionType, List<Group> associations) {
        System.out.println("Player " + association.player().name() + " executed action " + actionType);
        associations.map(Group::player)
                .forEach(Citadels::displayStatus);
    }

    private static void displayStatus(Player player) {
        System.out.println("  Player " + player.name() + ":");
        System.out.println("    Gold coins: " + player.gold());
        System.out.println("    City: " + textCity(player));
        System.out.println("    Hand size: " + player.cards().size());
        if (player.controller instanceof HumanController) {
            System.out.println("    Hand: " + textHand(player));
        }
        System.out.println();
    }

    private static String textCity(Player player) {
        List<District> districts = player.city().districts();
        return districts.isEmpty() ? "Empty" : districts.map(Citadels::textDistrict).mkString(", ");
    }

    private static String textDistrict(District district) {
        return district.name() + "(" + district.districtType().name() + ", " + district.cost() + ")";
    }

    private static String textHand(Player player) {
        Set<Card> cards = player.cards();
        return cards.isEmpty() ? "Empty" : cards.map(Citadels::textCard).mkString(", ");
    }

    private static String textCard(Card card) {
        return textDistrict(card.district());
    }

    public static void destroyDistrict() {
        /*//Retourne tous les roles dont leurs district peuvent être détruit
        List<Character> roles = getAvailableRolesForDestruction(groups.associations);

        Character character = group.player().controller.selectAmong(roles);


        groups.associationToCharacter(character).peek(association -> association.destroyedBy(group.player()));

        //Retourne uniquement les districts des joueurs pouvant être détruit
        List<District> destructableDistricts = getPlayerDestructableDistrict(character);
*/

    }

    public static List<Character> getAvailableRolesForDestruction(List<Group> groups) {

        List<Character> roles = List.of();
        for (Group group : groups) {
            if (group.player().city().districts().size() == 0) {
                continue;
            }
            if (group.character.equals(Character.BISHOP) && !group.isMurdered()) {
                continue;
            }
            roles = roles.append(group.character);
        }

        return roles;
    }

    public static List<District> getPlayerDestructableDistrict(Character c) {

        List<District> districts = List.of();


        return null;
    }

}
