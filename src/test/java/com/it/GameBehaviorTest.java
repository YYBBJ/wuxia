package com.it;

import com.it.common.Skill;
import com.it.core.Battle;
import com.it.enemy.Enemy;
import com.it.player.Player;
import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class GameBehaviorTest {
    @Test
    public void meditationDoesNotExceedMaximumInternalForce() {
        Player player = new Player("测试侠客");
        player.setInternalForce(95);
        Battle battle = new Battle(player, new Enemy("测试敌人", 1), new Scanner("2\n3\n"));

        battle.startBattle();

        assertEquals(player.getMaxInternalForce(), player.getInternalForce());
    }

    @Test
    public void battleEndsWhenInputIsClosed() {
        Player player = new Player("测试侠客");
        Battle battle = new Battle(player, new Enemy("测试敌人", 1), new Scanner(""));

        battle.startBattle();

        assertEquals(100, player.getHP());
    }

    @Test
    public void healingSkillRestoresHealthAndReturnsNoDamage() {
        Player player = new Player("测试侠客");
        player.setHP(90);
        Skill healingSkill = player.getSkills().get(2);

        int damage = player.castSkill(healingSkill);

        assertEquals(100, player.getHP());
        assertEquals(0, damage);
        assertEquals(100, player.getInternalForce());
    }

    @Test
    public void levelUpProcessesAllEarnedLevels() {
        Player player = new Player("测试侠客");
        player.setCultivation(120);

        player.checkLevelUp();

        assertEquals(3, player.getLevel());
        assertEquals(150, player.getMaxHP());
        assertEquals(130, player.getMaxInternalForce());
        assertEquals(150, player.getHP());
        assertEquals(130, player.getInternalForce());
    }
}
