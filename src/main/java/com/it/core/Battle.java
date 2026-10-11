package com.it.core;


import com.it.player.Player;
import com.it.common.Skill;
import com.it.enemy.Enemy;

import java.util.Random;
import java.util.Scanner;

public class Battle {
    private Player player;
    private Enemy enemy;
    private Scanner sc = new Scanner(System.in);

    /**
     * 创建一场由指定玩家与敌人参与的战斗。
     *
     * @param player 参与战斗的玩家
     * @param enemy 参与战斗的敌人
     */
    public Battle(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    /** 开始战斗并循环处理玩家的行动，直到战斗结束或玩家逃跑。 */
    public void startBattle(){
        System.out.println("\n===== ⚔️ 战斗开始 ⚔️ =====");
        while(true){
            player.showStatus();
            enemy.showStatus();
            System.out.println("\n请选择行动：");
            System.out.println("1. 释放武学  2. 调息回内  3. 逃跑");
            System.out.print("输入数字：");
            int opt;
            try{
                opt=Integer.parseInt(sc.nextLine());
            }catch (Exception e){
                System.out.println("输入无效，请重新输入");
                continue;
            }
            if (!handleOption(opt)) {
                break;
            }
        }
    }

    /**
     * 根据玩家选择处理对应行动。
     *
     * @param opt 玩家输入的行动选项
     * @return true 表示继续战斗，false 表示结束战斗
     */
    private boolean handleOption(int opt) {
        if (opt == 3) {
            handleEscape();
            return false;
        } else if (opt == 2) {
            restoreInternalForce();
            return true;
        } else if (opt == 1) {
            return handlePlayerAttack();
        } else {
            showInvalidOption();
            return true;
        }
    }

    /** 显示玩家逃离战场的提示。 */
    private void handleEscape() {
        System.out.println("你抽身远退，成功逃离战场！");
    }

    /** 恢复玩家 15 点内力并显示调息提示。 */
    private void restoreInternalForce() {
        int restoreInternForce = 15;
        int internalForce = player.getInternalForce();
        player.setInternalForce(restoreInternForce + internalForce);
        System.out.println("你闭目调息，恢复" + restoreInternForce + "内力");
    }

    /**
     * 处理玩家攻击及敌人的反击。
     *
     * @return true 表示继续战斗，false 表示敌人已被击败
     */
    private boolean handlePlayerAttack() {
        Skill skill = selectSkill();
        if (skill == null) {
            return true;
        }
        if (!player.hasEnoughInternalForce(skill)) {
            showInsufficientInternalForce();
            return true;
        }

        int damageToEnemy = player.castSkill(skill);
        enemy.loseHP(damageToEnemy);
        if (enemy.isDead()) {
            handleEnemyDefeat();
            return false;
        }

        int damageToPlayer = enemy.enemyAttack();
        player.setHP(player.getHP() - damageToPlayer);
        if (player.isDead()) {
            handlePlayerDefeat();
            return false;
        }
        return true;
    }

    /** 显示武学列表并读取玩家选择的招式。 */
    private Skill selectSkill() {
        showSkillList();
        System.out.println();
        System.out.print("选择招式序号：");

        int skillIdx;
        try {
            skillIdx = Integer.parseInt(sc.nextLine()) - 1;
        } catch (Exception e) {
            showInvalidSkillInput();
            return null;
        }
        if (skillIdx < 0 || skillIdx >= player.getSkills().size()) {
            showInvalidSkillIndex();
            return null;
        }
        return player.getSkills().get(skillIdx);
    }

    /** 显示玩家当前可用的武学及其内力消耗。 */
    private void showSkillList() {
        System.out.println("\n===== 武学列表 =====");
        for (int i = 0; i < player.getSkills().size(); i++) {
            Skill skill = player.getSkills().get(i);
            System.out.printf("%d. %s（内力消耗：%d）%s\n", i + 1, skill.getName(), skill.getNeedInternalForce(), skill.getDesc());
        }
    }

    /** 显示招式序号输入格式错误的提示。 */
    private void showInvalidSkillInput() {
        System.out.println("招式选择无效！");
    }

    /** 显示招式序号不存在的提示。 */
    private void showInvalidSkillIndex() {
        System.out.println("不存在该招式！");
    }

    /** 显示玩家内力不足、无法施展招式的提示。 */
    private void showInsufficientInternalForce() {
        System.out.println("内力不足，无法施展！");
    }

    /** 处理敌人被击败后的奖励发放及玩家升级检查。 */
    private void handleEnemyDefeat() {
        System.out.printf("\n✅ %s被你击败！\n", enemy.getName());
        int reward = 30 + new Random().nextInt(20);
        player.setCultivation(player.getCultivation() + reward);
        System.out.printf("获得修为 +%d\n", reward);
        player.checkLevelUp();
    }

    /** 显示玩家重伤倒地、战斗失败的提示。 */
    private void handlePlayerDefeat() {
        System.out.println("\n❌ 你重伤倒地，战斗失败...");
    }

    /** 显示玩家输入了无效行动选项的提示。 */
    private void showInvalidOption() {
        System.out.println("选项不存在");
    }

}
