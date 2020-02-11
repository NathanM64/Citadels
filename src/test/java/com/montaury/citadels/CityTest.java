package com.montaury.citadels;

import com.montaury.citadels.district.Card;
import org.assertj.core.api.Assertions;
import org.junit.Test;


public class CityTest {

    @Test
    public void scoreConstruction() {
        Board board = new Board();
        City city = new City(board);
        city.buildDistrict(Card.CHURCH_2);
        city.buildDistrict(Card.TEMPLE_1);
        city.buildDistrict(Card.TAVERN_4);
        city.buildDistrict(Card.CATHEDRAL_2);
        city.buildDistrict(Card.CHURCH_1);
        Possession possession = new Possession(6, null);
        int score = city.score(possession);
        Assertions.assertThat(score).isEqualTo(11);
    }

}