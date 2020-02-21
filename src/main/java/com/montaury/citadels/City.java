package com.montaury.citadels;

import com.montaury.citadels.district.Card;
import com.montaury.citadels.district.DestructibleDistrict;
import com.montaury.citadels.district.District;
import com.montaury.citadels.district.DistrictType;
import com.montaury.citadels.player.Player;
import io.vavr.collection.List;

import static com.montaury.citadels.district.District.GREAT_WALL;
import static com.montaury.citadels.district.District.HAUNTED_CITY;

public class City {
    private static final int END_GAME_DISTRICT_NUMBER = 7;
    private final int WIN_All_COLORS = 3;
    private final int IS_COMPLETED_FIRST = 2;
    private final int IS_COMPLETED = 2;
    private final int IS_DRAGONGATE_UNIVERSITY = 2;
    private final Board board;
    private List<Card> districtCards = List.empty();

    public City(Board board) {
        this.board = board;
    }

    public void buildDistrict(Card card) {
        districtCards = districtCards.append(card);
        if (isComplete()) {
            board.mark(this);
        }
    }

    public boolean isComplete() {
        return districtCards.size() >= END_GAME_DISTRICT_NUMBER;
    }

    public int score(Possession possession) {
        return getBuildCost() + getBonuses(possession);
    }

    private int getBuildCost() {
        int buildCost = 0;
        for (int carte = 0; carte < districts().size(); carte++) {
            buildCost += districts().get(carte).cost();
        }
        return buildCost;
    }

    private int getBonuses(Possession possession) {
        return districtsScoreBonus(possession) + winsAllColorBonus() + isFirst(this) + cityCompleteBonus();
    }

    private int cityCompleteBonus() {
        return (isComplete() ? IS_COMPLETED : 0);
    }

    private int isFirst(City city) {
        return (board.isFirst(city) ? IS_COMPLETED_FIRST : 0);
    }

    private int districtsScoreBonus(Possession possession) {
        int bonus = 0;
        for (District d : districts()) {
            if (d == District.DRAGON_GATE || d == District.UNIVERSITY) {
                bonus += IS_DRAGONGATE_UNIVERSITY;
            }
            if (d == District.MAP_ROOM) {
                bonus += possession.hand.size();
            }
            if (d == District.TREASURY) {
                bonus += possession.gold;
            }

        }
        return bonus;
    }

    private int winsAllColorBonus() {
        int nbDistricts[] = new int[DistrictType.values().length];
        for (District d : districts()) {
            nbDistricts[d.districtType().ordinal()]++;
        }
        return (hasAllColors(nbDistricts) ? WIN_All_COLORS : 0);

    }
    private boolean hasAllColors(int [] nbDistricts) {
        return hasEachTypeOfDistrict(nbDistricts) || hasHauntedCityAndMissesOneTypeOfDistrict(nbDistricts);
    }

        private boolean hasHauntedCityAndMissesOneTypeOfDistrict(int[] nbDistricts) {
            int nbTypesManquants = 0;
            if (has(HAUNTED_CITY)) {
                for (int i = 0; i < nbDistricts.length; i++) {
                    if (nbDistricts[i] == 0) {
                        nbTypesManquants++;
                    }
                }
            }
            return nbTypesManquants == 1;
        }

        private boolean hasEachTypeOfDistrict(int[] nbDistricts) {
            return hasMilitaryDistrict(nbDistricts) && hasNobleDistrict(nbDistricts) && hasSpecialDistrict(nbDistricts) && hasTradeDistrict(nbDistricts) && hasReligiousDistrict(nbDistricts);
        }

        private boolean hasMilitaryDistrict(int[] nbDistrictsMain) {
            return nbDistrictsMain[DistrictType.MILITARY.ordinal()] > 0;
        }

        private boolean hasNobleDistrict(int[] nbDistrictsMain) {
            return nbDistrictsMain[DistrictType.NOBLE.ordinal()] > 0;
        }
        private boolean hasSpecialDistrict(int[] nbDistrictsMain) {
            return nbDistrictsMain[DistrictType.SPECIAL.ordinal()] > 0;
        }

        private boolean hasTradeDistrict(int[] nbDistrictsMain) {
            return nbDistrictsMain[DistrictType.TRADE.ordinal()] > 0;
        }

        private boolean hasReligiousDistrict(int[] nbDistrictsMain) {
            return nbDistrictsMain[DistrictType.RELIGIOUS.ordinal()] > 0;
        }



    public boolean has(District district) {
        return districts().contains(district);
    }

    public void destroyDistrict(Card card) {
        districtCards = districtCards.remove(card);
    }

    public List<DestructibleDistrict> districtsDestructibleBy(Player player) {
        return isComplete() ?
                List.empty() :
                districtCards
                        .filter(card -> card.district().isDestructible())
                        .filter(card -> player.canAfford(destructionCost(card)))
                        .map(card -> new DestructibleDistrict(card, destructionCost(card)));
    }

    private int destructionCost(Card card) {
        return card.district().cost() - (has(GREAT_WALL) && card.district() != GREAT_WALL ? 0 : (1));
    }

    public List<District> districts() {
        return districtCards.map(Card::district);
    }
}
