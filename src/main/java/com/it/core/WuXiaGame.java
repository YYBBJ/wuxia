package com.it.core;

import com.it.player.Player;
import com.it.enemy.Enemy;
import com.it.enemy.EnemyFactory;
import com.it.enemy.NormalEnemyFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WuXiaGame {
    private  static List<EnemyFactory> enemyFactories=new ArrayList<>();
    static {
        enemyFactories.add(new NormalEnemyFactory());
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("========== 武侠文字小游戏 ==========");
        System.out.print("请输入你的侠客名号：");
        String playerName = sc.nextLine();
        Player hero = new Player(playerName);


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

                // 获取敌人实例
                EnemyFactory enemyFactory = enemyFactories.get(rand.nextInt(enemyFactories.size()));
                Enemy enemy = enemyFactory.createEnemy();
                Battle battle = new Battle(hero, enemy);
                battle.startBattle();
            } else {
                System.out.println("无效选项");
            }
        }
        sc.close();
    }
    private static void printProcess(){
        Random random = new Random();
        int width = 24;
        int steps = 20;

        for (int step = 0; step <= steps; step++) {
            int percent = step * 100 / steps;
            int filled = step * width / steps;
            String bar = "█".repeat(filled) + "·".repeat(width - filled);
            String marker = step == steps ? "⚔" : "➤";

            System.out.printf("\r行走江湖  %s%s  %3d%%", bar, marker, percent);
            System.out.flush();

            if (step < steps) {
                try {
                    Thread.sleep(80 + random.nextInt(121));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("\n探索已中断。");
                    return;
                }
            }
        }
        System.out.println("\n✦ 探索完成，前方有动静！");
    }
    public static void showMenu(){
        System.out.println("\n===== 主菜单 =====");
        System.out.println("1. 外出历练  2. 查看自身状态  0. 退出游戏");
    }
}
