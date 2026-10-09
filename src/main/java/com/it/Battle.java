package com.it;


import java.util.Random;
import java.util.Scanner;

public class Battle {
    private Player player;
    private Enemy enemy;
    private Scanner sc = new Scanner(System.in);

    public Battle(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

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
            if (opt == 3) {
                System.out.println("你抽身远退，成功逃离战场！");
                break;
            }else if(opt==2){
                int restoreInternForce=15;
                int internalForce = player.getInternalForce();
                player.setInternalForce(restoreInternForce+internalForce);
                System.out.println("你闭目调息，恢复" + restoreInternForce + "内力");
            }else if(opt==1){
                System.out.println("\n===== 武学列表 =====");
                for (int i = 0; i < player.getSkills().size(); i++) {
                    Skill s = player.getSkills().get(i);
                    System.out.printf("%d. %s（内力消耗：%d）%s\n", i + 1, s.getName(), s.getNeedInternalForce(), s.getDesc());
                }
                System.out.println();
                System.out.print("选择招式序号：");
                int skillIdx;
                try {
                    skillIdx = Integer.parseInt(sc.nextLine()) - 1;
                } catch (Exception e) {
                    System.out.println("招式选择无效！");
                    continue;
                }
                if (skillIdx < 0 || skillIdx >= player.getSkills().size()) {
                    System.out.println("不存在该招式！");
                    continue;
                }
                Skill skill = player.getSkills().get(skillIdx);
                if(player.getInternalForce()<skill.getNeedInternalForce()){
                    System.out.println("内力不足，无法施展！");
                    continue;
                }
                //玩家攻击
                int damageToEnemy = player.castSkill(skill);
                enemy.loseHP(damageToEnemy);
                if(enemy.getHP()<=0){
                    System.out.printf("\n✅ %s被你击败！\n", enemy.getName());
                    int reward = 30 + new Random().nextInt(20);
                    player.setCultivation(player.getCultivation()+reward);
                    System.out.printf("获得修为 +%d\n", reward);
                    player.checkLevelUp();
                    break;
                }

                //敌人攻击
                int damageToPlayer = enemy.enemyAttack();
                player.setHP(player.getHP()-damageToPlayer);
                if(player.getHP()<=0){
                    System.out.println("\n❌ 你重伤倒地，战斗失败...");
                }


            }else{
                System.out.println("选项不存在");
            }
        }
    }

}
