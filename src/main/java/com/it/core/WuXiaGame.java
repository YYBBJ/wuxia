package com.it.core;

import com.it.player.Player;
import com.it.enemy.Enemy;
import com.it.enemy.EnemyFactory;

import java.util.Random;
import java.util.Scanner;

public class WuXiaGame {
    public static void main(String[] args) {
        System.out.println("══════════ 武侠文字小游戏 ══════════");
        System.out.print("请输入你的侠客名号：");

        Scanner sc = new Scanner(System.in);
        String playerName = sc.nextLine();
        Player hero = new Player(playerName);

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

            if (!handleMenuOption(menuOpt, hero, sc)) {
                break;
            }
        }
        sc.close();
    }

    /**
     * 根据主菜单选项执行对应动作。
     *
     * @param menuOpt 主菜单选项
     * @param hero 当前玩家
     * @param sc 读取控制台输入的扫描器
     * @return 选择退出时返回 false，否则返回 true
     */
    private static boolean handleMenuOption(int menuOpt, Player hero, Scanner sc) {
        if (menuOpt == 0) {
            System.out.println("江湖路远，后会有期！");
            return false;
        } else if (menuOpt == 2) {
            hero.showStatus();
        } else if (menuOpt == 1) {
            printProcess();
            Enemy enemy = EnemyFactory.createRandomEnemy();
            // 玩家确认迎战后才进入战斗；撤退则回到主菜单。
            if (fightOrWithdraw(sc)) {
                Battle battle = new Battle(hero, enemy, sc);
                battle.startBattle();
            }
        } else {
            System.out.println("无效选项");
        }
        return true;
    }
    //打印进度条
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
                    Thread.sleep(80+ random.nextInt(121));
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

    /**
     * 读取玩家选择，决定迎战或撤退；无效输入会重新提示。
     *
     * @param sc 读取控制台输入的扫描器
     * @return 输入 1 时返回 true（战斗），输入 2 时返回 false（撤退）
     */
    public static boolean fightOrWithdraw(Scanner sc){
        while (true) {
            System.out.println("前方发现敌人，你要如何行动？");
            System.out.println("1. 战斗  2. 撤退");
            System.out.print("请选择：");
            String choice = sc.nextLine();
            // 输入后清屏，避免控制台保留刚才的选项提示。
            clearConsole();
            if ("1".equals(choice)) {
                return true;
            }
            if ("2".equals(choice)) {
                System.out.println("你选择撤退，暂避锋芒。");
                return false;
            }
            System.out.println("输入无效，请输入 1 或 2。");
        }
    }

    private static void clearConsole() {

    }
}
