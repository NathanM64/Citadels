package com.montaury.citadels;

import com.montaury.citadels.district.Card;
import com.montaury.citadels.player.ComputerController;
import com.montaury.citadels.player.Player;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CityTest {
    @Test
    public void test_score() {

        Board board = new Board();

        City uneCite = new City(board);

        uneCite.buildDistrict(Card.GREAT_WALL);
        uneCite.buildDistrict(Card.GREAT_WALL);
        uneCite.buildDistrict(Card.GREAT_WALL);
        uneCite.buildDistrict(Card.GREAT_WALL);
        uneCite.buildDistrict(Card.GREAT_WALL);
        uneCite.buildDistrict(Card.GREAT_WALL);
        uneCite.buildDistrict(Card.GREAT_WALL);


        Possession possession = new Possession(0, null);

        int scoreAttendu = uneCite.score(possession);

        Assertions.assertThat(scoreAttendu).isEqualTo(46);

    }

    @Test
    public void test_isComplete() {
        Board board = new Board();
        City uneCite = new City(board);
        uneCite.buildDistrict(Card.GREAT_WALL);
        uneCite.buildDistrict(Card.GREAT_WALL);
        uneCite.buildDistrict(Card.GREAT_WALL);
        uneCite.buildDistrict(Card.GREAT_WALL);
        uneCite.buildDistrict(Card.GREAT_WALL);
        uneCite.buildDistrict(Card.GREAT_WALL);
        uneCite.buildDistrict(Card.GREAT_WALL);
        uneCite.buildDistrict(Card.GREAT_WALL);
        Assertions.assertThat(uneCite.isComplete()).isEqualTo(true);
    }


    @Test
    public void test_isNotComplete() {
        Board board = new Board();
        City uneCite = new City(board);
        Assertions.assertThat(uneCite.isComplete()).isEqualTo(false);
    }


}
