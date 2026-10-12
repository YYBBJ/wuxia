package com.it;

import com.it.common.Skill;
import com.it.core.Battle;
import com.it.enemy.Enemy;
import com.it.enemy.EnemyFactory;
import com.it.enemy.EnemyType;
import com.it.player.Player;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        player.setHP(70);
        player.setInternalForce(40);
        Battle battle = new Battle(player, new Enemy("测试敌人", 1), new Scanner(""));

        battle.startBattle();

        assertEquals(70, player.getHP());
        assertEquals(40, player.getInternalForce());
    }

    @Test
    public void completedBattleRestoresHealthAndInternalForce() {
        Player player = new Player("测试侠客");
        player.setHP(70);
        player.setInternalForce(40);
        Battle battle = new Battle(player, new Enemy("测试敌人", 1), new Scanner("3\n"));

        battle.startBattle();

        assertEquals(player.getMaxHP(), player.getHP());
        assertEquals(player.getMaxInternalForce(), player.getInternalForce());
    }

    @Test
    public void defeatedPlayerRecoversAfterBattle() {
        Player player = new Player("测试侠客");
        player.setHP(1);
        player.setInternalForce(50);
        Battle battle = new Battle(player, new Enemy("测试敌人", 1), new Scanner("1\n1\n"));

        battle.startBattle();

        assertEquals(player.getMaxHP(), player.getHP());
        assertEquals(player.getMaxInternalForce(), player.getInternalForce());
    }

    @Test
    public void victoryRecoversHealthAndInternalForce() {
        Player player = new Player("测试侠客");
        player.setHP(70);
        player.setInternalForce(50);
        Enemy enemy = new Enemy("测试敌人", 1);
        enemy.setHP(1);
        Battle battle = new Battle(player, enemy, new Scanner("1\n1\n"));

        battle.startBattle();

        assertEquals(player.getMaxHP(), player.getHP());
        assertEquals(player.getMaxInternalForce(), player.getInternalForce());
    }

    @Test
    public void battleLogClearlySeparatesBattleAndRounds() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            Battle battle = new Battle(
                    new Player("测试侠客"),
                    new Enemy("测试敌人", 1),
                    new Scanner("9\n3\n"));
            battle.startBattle();
        } finally {
            System.setOut(originalOut);
        }

        String battleLog = output.toString(StandardCharsets.UTF_8);
        assertTrue(battleLog.contains("第 1 回合 · 战斗情况"));
        assertTrue(battleLog.contains("遭遇敌人：测试敌人"));
        assertTrue(battleLog.contains("第 1 回合"));
        assertTrue(battleLog.contains("【当前状态】"));
        assertFalse(battleLog.contains("第 2 回合"));
        assertTrue(battleLog.contains("战斗结束"));
    }

    @Test
    public void actionResultAndUpdatedStateAppearInsideTheFrame() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            Player player = new Player("测试侠客");
            player.setInternalForce(80);
            new Battle(player, new Enemy("测试敌人", 1), new Scanner("2\n3\n")).startBattle();
        } finally {
            System.setOut(originalOut);
        }

        String battleLog = output.toString(StandardCharsets.UTF_8);
        int actionIndex = battleLog.indexOf("你闭目调息，恢复 15 点内力。");
        int frameStart = battleLog.lastIndexOf("┌", actionIndex);
        int frameEnd = battleLog.indexOf("└", actionIndex);
        assertTrue(actionIndex >= 0);
        assertTrue(frameStart >= 0 && frameEnd > actionIndex);
        String actionFrame = battleLog.substring(frameStart, frameEnd);
        assertTrue(actionFrame.contains("第 1 回合 · 战斗情况"));
        assertTrue(actionFrame.contains("内力 95/100"));
        assertTrue(actionFrame.contains("测试敌人 Lv.1"));
        assertTrue(actionFrame.contains("内力 100/100"));
        assertTrue(battleLog.contains("\u001B[H\u001B[2J"));
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

    @Test
    public void enemyTypesHaveLevelsAndMatchingInternalForce() {
        for (EnemyType type : EnemyType.values()) {
            Enemy enemy = EnemyFactory.createEnemy(type);
            int expectedInternalForce = 100 + (type.getLevel() - 1) * 15;

            assertEquals(type.getLevel(), enemy.getLevel());
            assertEquals(expectedInternalForce, enemy.getMaxInternalForce());
            assertEquals(expectedInternalForce, enemy.getInternalForce());
        }
    }
}
