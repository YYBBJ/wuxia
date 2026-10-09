package com.it;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WuXiaGame {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("========== 武侠文字小游戏 ==========");
        System.out.print("请输入你的侠客名号：");
        String playerName = sc.nextLine();
        Player hero = new Player(playerName);
        // 敌人池，可以继续添加敌人
        List<Enemy> enemyPool = new ArrayList<>();
        enemyPool.add(new Enemy("拦路山贼",  10));
        enemyPool.add(new Enemy("毒巫",  13));
        enemyPool.add(new Enemy("黑衣剑客", 16));
        Random rand = new Random();

        while (true) {
            showMenu();
            System.out.print("请选择：");
            int menuOpt;
            try {
                menuOpt = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("输入错误！");
                continue;
            }
            if (menuOpt == 0) {
                System.out.println("江湖路远，后会有期！");
                break;
            } else if (menuOpt == 2) {
                hero.showStatus();
            } else if (menuOpt == 1) {
                //打印进度条
                System.out.println("正在探索地图");
                printProcess();
                // 随机刷敌人
                Enemy randomEnemy = enemyPool.get(rand.nextInt(enemyPool.size()));
                // 每次新建敌人实例，保证血量重置
                Enemy battleEnemy = new Enemy(randomEnemy.getName(),  randomEnemy.getAttack());
                Battle battle = new Battle(hero, battleEnemy);
                battle.startBattle();
            } else {
                System.out.println("无效选项");
            }
        }
        sc.close();
    }
    private static void printProcess(){
        Random random = new Random();
        // 先打印 []
        System.out.print("[]");
        int max = 20;
        for (int i = 0; i < max; i++) {
            // 难点：控制台不能在中间插入字符，只能覆盖，所以先打印退格，再输出=
            System.out.print("\b=]");
            try {
                Thread.sleep(random.nextInt(250));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();

    }
    public static void showMenu(){
        System.out.println("\n===== 主菜单 =====");
        System.out.println("1. 外出历练  2. 查看自身状态  0. 退出游戏");
    }
}
