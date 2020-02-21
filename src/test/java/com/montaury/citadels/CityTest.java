package com.montaury.citadels;

import com.montaury.citadels.district.Card;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.montaury.citadels.player.Player;
import io.vavr.collection.HashSet;


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
        assertThat(score).isEqualTo(17);
    }

    @Test
    public void plusQuatrePourLePremierJoueurQuiACompleteCite() {
        Board board = new Board();
        City city = new City(board);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.PRISON_3);
        Possession possession = new Possession(0,null);
        int score = city.score(possession);
        assertThat(board.isFirst(city)).isTrue();
        assertThat(city.isComplete()).isTrue();
        assertThat(score).isEqualTo(18);

    }

    @Test
    public void plusDeuxPourAutresJoueursQuiOntCompleteCite() {
        Board board = new Board();
        City city = new City(board);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.PRISON_3);
        city.buildDistrict(Card.PRISON_3);
        Possession possession = new Possession(0,null);
        int score = city.score(possession);
        Board board2 = new Board();
        City city2 = new City(board);
        city2.buildDistrict(Card.PRISON_3);
        city2.buildDistrict(Card.PRISON_3);
        city2.buildDistrict(Card.PRISON_3);
        city2.buildDistrict(Card.PRISON_3);
        city2.buildDistrict(Card.PRISON_3);
        city2.buildDistrict(Card.PRISON_3);
        city2.buildDistrict(Card.PRISON_3);
        Possession possession2 = new Possession(0,null);
        int score2 = city2.score(possession2);
        assertThat(!board.isFirst(city2)).isTrue();
        assertThat(city2.isComplete()).isTrue();
            assertThat(score2).isEqualTo(16);

    }

    @Test
    public void unPointSupPourChaqueCarte() {
        Board board = new Board();
        City city = new City(board);
        city.buildDistrict(Card.MAP_ROOM);
        Player player = new Player("Quentin",19,city,null);
        Possession possession = new Possession(0, HashSet.of(Card.PRISON_1,Card.TRADING_POST_1));
        int score = city.score(possession);
        assertThat(score).isEqualTo(7);
    }

    @Test
    public void unPointSupPourChaquePiece() {
        Board board = new Board();
        City city = new City(board);
        city.buildDistrict(Card.UNIVERSITY);
        city.buildDistrict(Card.CATHEDRAL_2);
        Possession possession = new Possession(0,null);
        int score = city.score(possession);
        assertThat(score).isEqualTo(13);
    }

}