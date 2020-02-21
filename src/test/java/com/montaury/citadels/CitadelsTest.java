package com.montaury.citadels;

import com.montaury.citadels.character.Character;
import com.montaury.citadels.district.Card;
import com.montaury.citadels.district.District;
import com.montaury.citadels.district.DistrictType;
import com.montaury.citadels.player.ComputerController;
import com.montaury.citadels.player.Player;
import com.montaury.citadels.round.Group;
import io.vavr.collection.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;


import static org.junit.Assert.*;

public class CitadelsTest {

    @Test
    public void test_getAvailableRolesForDestruction_should_not_return_bishop_if_alive()
    {
        Board board = new Board();
        City cite1 = new City(board);
        cite1.buildDistrict(Card.GREAT_WALL);
        Player p1 = new Player("toto", 18, cite1, new ComputerController());

        City cite2 = new City(board);
          cite2.buildDistrict(Card.GREAT_WALL);
        Player p2 = new Player("tata", 17, cite2, new ComputerController());

        List<Group> groups = List.of(new Group(p1, Character.WARLORD), new Group(p2, Character.BISHOP));

        List<Character> characters = Citadels.getAvailableRolesForDestruction(groups);
        Assertions.assertThat(characters).hasSize(1);
        Assertions.assertThat(characters.get(0)).isEqualTo(Character.WARLORD);
    }


    @Test
    public void test_getAvailableRolesForDestruction_should_return_bishop_if_murdered()
    {
        Board board = new Board();

        City cite1 = new City(board);
        cite1.buildDistrict(Card.GREAT_WALL);
        Player p1 = new Player("toto", 18, cite1, new ComputerController());
        City cite2 = new City(board);
        cite2.buildDistrict(Card.GREAT_WALL);
        Player p2 = new Player("tata", 17, cite2, new ComputerController());

        Group bishopGroup = new Group(p2, Character.BISHOP);
        bishopGroup.murder();

        List<Group> groups = List.of(new Group(p1, Character.WARLORD), bishopGroup);

        List<Character> characters = Citadels.getAvailableRolesForDestruction(groups);

        Assertions.assertThat(characters).hasSize(2);
        Assertions.assertThat(characters.get(0)).isEqualTo(Character.WARLORD);
        Assertions.assertThat(characters.get(1)).isEqualTo(Character.BISHOP);
    }

    @Test
    public void test_getAvailableRolesForDestruction_should_not_return_role_if_player_city_has_no_district()
    {
        Board board = new Board();
        City cite1 = new City(board);
        Player p1 = new Player("toto", 18, cite1, new ComputerController());
        List<Group> groups = List.of(new Group(p1, Character.WARLORD));

        List<Character> characters = Citadels.getAvailableRolesForDestruction(groups);

        Assertions.assertThat(characters).hasSize(0);
    }

    @Test
    public void test_getPlayerDestructableDistrict_should_return_emty_if_distrits_too_expensive()
    {

        Citadels.getPlayerDestructableDistrict();
    }

}