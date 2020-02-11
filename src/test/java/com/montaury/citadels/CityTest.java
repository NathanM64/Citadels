package com.montaury.citadels;

import com.montaury.citadels.district.Card;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class CityTest {
/*
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
*/
    @Test
    public void plusTroisSiLaCiteComprendLesCinqTypes() {
        Board board = new Board();
        City city = new City(board);
        city.buildDistrict(Card.CHURCH_1);
        city.buildDistrict(Card.TAVERN_1);
        city.buildDistrict(Card.PRISON_2);
        city.buildDistrict(Card.MANOR_1);
        city.buildDistrict(Card.GREAT_WALL);
        Possession possession = new Possession(0,null);
        int score = city.score(possession);
        assertThat(score).isEqualTo(19);
    }

    @Test
    public void plusQuatrePourLePremierJoueurQuiACompleteCite() {
        Board board = new Board();
        City city = new City(board);
        city.buildDistrict(Card.HARBOR_2);
        city.buildDistrict(Card.MONASTERY_3);
        city.buildDistrict(Card.MARKET_1);
        city.buildDistrict(Card.PALACE_2);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.OBSERVATORY);
        Possession possession = new Possession(0,null);
        int score = city.score(possession);
        if (board.isFirst(city) && city.isComplete())
        {
            assertThat(score).isEqualTo(22);
        }
    }

    @Test
    public void plusDeuxPourAutresJoueursQuiOntCompleteCite() {
        Board board = new Board();
        City city = new City(board);
        city.buildDistrict(Card.HARBOR_2);
        city.buildDistrict(Card.MONASTERY_3);
        city.buildDistrict(Card.MARKET_1);
        city.buildDistrict(Card.PALACE_2);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.OBSERVATORY);
        Possession possession = new Possession(0,null);
        int score = city.score(possession);
        if (!board.isFirst(city) && city.isComplete())
        {
            assertThat(score).isEqualTo(22);
        }
    }

}