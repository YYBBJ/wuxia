package com.it.core;

import com.it.common.Skill;
import com.it.enemy.Enemy;
import com.it.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class Battle {
    private final Player player;
    private final Enemy enemy;
    private final Scanner sc;

    /**
     * 创建一场由指定玩家与敌人参与的战斗。
     *
     * @param player 参与战斗的玩家
     * @param enemy 参与战斗的敌人
     */
    public Battle(Player player, Enemy enemy) {
        this(player, enemy, new Scanner(System.in));
    }

    /**
     * 创建一场使用指定输入源的战斗。
     *
     * @param player 参与战斗的玩家
     * @param enemy 参与战斗的敌人
     * @param sc 读取玩家操作的扫描器
     */
    public Battle(Player player, Enemy enemy, Scanner sc) {
        this.player = player;
        this.enemy = enemy;
        this.sc = sc;
    }

    /** 开始战斗并循环处理玩家的行动，直到战斗结束或玩家逃跑。 */
    public void startBattle() {
        clearConsole();
        int round = 1;
        printBattleReport(round, List.of("遭遇敌人：" + enemy.getName(), "等待你的行动。"));

        while (true) {
            printActionPrompt(round);
            int option;
            try {
                option = Integer.parseInt(sc.nextLine());
            } catch (NoSuchElementException e) {
                System.out.println("输入已结束，战斗结束。");
                break;
            } catch (Exception e) {
                System.out.println("输入无效，请重新输入。");
                continue;
            }

            List<String> actionMessages = new ArrayList<>();
            TurnResult result = handleOption(option, actionMessages);
            if (result == TurnResult.RETRY) {
                continue;
            }

            clearConsole();
            printBattleReport(round, actionMessages);
            if (result == TurnResult.BATTLE_OVER) {
                break;
            }
            round++;
        }

        System.out.println("\n══════════════════ 战斗结束 ══════════════════");
    }

    private enum TurnResult {
        RETRY,
        TURN_COMPLETED,
        BATTLE_OVER
    }

    /** 根据玩家选择处理对应行动。 */
    private TurnResult handleOption(int option, List<String> messages) {
        if (option == 3) {
            messages.add("你抽身远退，成功逃离战场。");
            return TurnResult.BATTLE_OVER;
        }
        if (option == 2) {
            messages.add(restoreInternalForce());
            return TurnResult.TURN_COMPLETED;
        }
        if (option == 1) {
            return handlePlayerAttack(messages);
        }

        System.out.println("选项不存在。");
        return TurnResult.RETRY;
    }

    /** 恢复玩家最多 15 点内力，并返回本次实际恢复量。 */
    private String restoreInternalForce() {
        int before = player.getInternalForce();
        int after = Math.min(player.getMaxInternalForce(), before + 15);
        player.setInternalForce(after);
        return "你闭目调息，恢复 " + (after - before) + " 点内力。";
    }

    /** 处理玩家攻击及敌人的反击。 */
    private TurnResult handlePlayerAttack(List<String> messages) {
        Skill skill = selectSkill();
        if (skill == null) {
            return TurnResult.RETRY;
        }
        if (!player.hasEnoughInternalForce(skill)) {
            System.out.println("内力不足，无法施展！");
            return TurnResult.RETRY;
        }

        int playerHealthBefore = player.getHP();
        int damageToEnemy = player.castSkill(skill);
        int healing = player.getHP() - playerHealthBefore;
        if (healing > 0 || skill.getName().equals("静心诀")) {
            messages.add("你施展【" + skill.getName() + "】，恢复 " + healing + " 点气血。");
        } else {
            int normalDamage = skill.getDamage() + player.getLevel() * 4;
            String criticalMessage = damageToEnemy > normalDamage ? "触发暴击，" : "";
            messages.add("你施展【" + skill.getName() + "】" + criticalMessage
                    + "对" + enemy.getName() + "造成 " + damageToEnemy + " 点伤害。");
        }

        enemy.loseHP(damageToEnemy);
        if (enemy.isDead()) {
            handleEnemyDefeat(messages);
            return TurnResult.BATTLE_OVER;
        }

        int damageToPlayer = enemy.enemyAttack();
        String attackDescription = damageToPlayer > enemy.getAttack() ? "使出重击" : "发起反击";
        player.setHP(Math.max(0, player.getHP() - damageToPlayer));
        messages.add(enemy.getName() + attackDescription + "，你受到 " + damageToPlayer + " 点伤害。");
        if (player.isDead()) {
            messages.add("你重伤倒地，战斗失败。");
            return TurnResult.BATTLE_OVER;
        }
        return TurnResult.TURN_COMPLETED;
    }

    /** 显示玩家当前可用的武学及其内力消耗，并读取招式选择。 */
    private Skill selectSkill() {
        System.out.println("\n===== 武学列表 =====");
        for (int i = 0; i < player.getSkills().size(); i++) {
            Skill skill = player.getSkills().get(i);
            System.out.printf("%d. %s（内力消耗：%d）%s%n",
                    i + 1, skill.getName(), skill.getNeedInternalForce(), skill.getDesc());
        }
        System.out.print("选择招式序号：");

        int skillIndex;
        try {
            skillIndex = Integer.parseInt(sc.nextLine()) - 1;
        } catch (Exception e) {
            System.out.println("招式选择无效！");
            return null;
        }
        if (skillIndex < 0 || skillIndex >= player.getSkills().size()) {
            System.out.println("不存在该招式！");
            return null;
        }
        return player.getSkills().get(skillIndex);
    }

    /** 处理敌人被击败后的奖励及升级信息。 */
    private void handleEnemyDefeat(List<String> messages) {
        messages.add("你击败了" + enemy.getName() + "。");
        int reward = 30 + new Random().nextInt(20);
        player.setCultivation(player.getCultivation() + reward);
        messages.add("获得修为 +" + reward + "。");
        int levelsGained = player.checkLevelUp();
        if (levelsGained > 0) {
            messages.add("突破境界，升至 Lv." + player.getLevel()
                    + "；气血上限 +" + (levelsGained * 25)
                    + "，内力上限 +" + (levelsGained * 15) + "。");
        }
    }

    private void printActionPrompt(int round) {
        System.out.printf("\n第 %d 回合，请选择行动：%n", round);
        System.out.println("1. 释放武学  2. 调息回内  3. 逃跑");
        System.out.print("输入数字：");
    }

    private void printBattleReport(int round, List<String> messages) {
        String title = "第 " + round + " 回合 · 战斗情况";
        List<String> lines = new ArrayList<>(messages);
        lines.add("");
        lines.add("【当前状态】");
        lines.add("侠客【" + player.getName() + " Lv." + player.getLevel() + "】：气血 "
                + player.getHP() + "/" + player.getMaxHP() + " | 内力 "
                + player.getInternalForce() + "/" + player.getMaxInternalForce()
                + " | 修为 " + player.getCultivation());
        lines.add(enemy.getName() + " Lv." + enemy.getLevel() + "：气血 "
                + enemy.getHP() + "/" + enemy.getMaxHP() + " | 内力 "
                + enemy.getInternalForce() + "/" + enemy.getMaxInternalForce());

        int width = displayWidth(title);
        for (String line : lines) {
            width = Math.max(width, displayWidth(line));
        }

        String border = "─".repeat(width + 2);
        System.out.println("\n┌" + border + "┐");
        printBoxLine(title, width);
        System.out.println("├" + border + "┤");
        for (String line : lines) {
            printBoxLine(line, width);
        }
        System.out.println("└" + border + "┘");
    }

    private void printBoxLine(String line, int width) {
        System.out.println("│ " + line + " ".repeat(width - displayWidth(line)) + " │");
    }

    private int displayWidth(String text) {
        int width = 0;
        for (int offset = 0; offset < text.length();) {
            int codePoint = text.codePointAt(offset);
            offset += Character.charCount(codePoint);
            int type = Character.getType(codePoint);
            if (type == Character.NON_SPACING_MARK
                    || type == Character.COMBINING_SPACING_MARK
                    || type == Character.ENCLOSING_MARK
                    || codePoint == 0x200D
                    || (codePoint >= 0xFE00 && codePoint <= 0xFE0F)) {
                continue;
            }
            width += codePoint <= 0x00FF ? 1 : 2;
        }
        return width;
    }

    /** 使用终端控制序列清除旧的菜单、提示和战报。 */
    private void clearConsole() {
        System.out.print("\u001B[H\u001B[2J");
        System.out.flush();
    }
}
