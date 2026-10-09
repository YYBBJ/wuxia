package com.it;

import lombok.Data;

import java.util.Random;

@Data
public class Enemy {
    private String name;
    private int HP;
    private int maxHP;
    private int attack;
    private Random random = new Random();
    public Enemy(String name,int attack){
        this.name = name;
        this.maxHP =100;
        this.HP=maxHP;
        this.attack=attack;
    }
    public int enemyAttack(){
        boolean heavy = random.nextDouble() < 0.25;
        int damage=heavy?attack*2:attack;
        if (heavy) {
            System.out.printf("\n💥 %s使出重击！！\n", name);
        }else {
            System.out.printf("\n%s挥招向你攻来！\n", name);
        }
        System.out.printf("你受到 %d 点伤害！\n", damage);
        return damage;
    }

    public void loseHP(int damage){
        if(HP<=0) return;
        HP-=damage;
    }

    public void showStatus() {
        System.out.printf("【%s】气血：%d/%d\n", name, HP,maxHP );
    }
}
