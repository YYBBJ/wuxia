package com.it.enemy;

import com.it.common.Skill;
import lombok.Data;

import java.util.List;
import java.util.Random;

@Data
public class Enemy {
    private String name;
    private int HP;
    private int maxHP;
    private int attack;
    private int level;
    private int internalForce;
    private List<Skill> skills;
    private Random random = new Random();

    public Enemy(String name,int attack){
        this.name = name;
        this.maxHP =100;
        this.HP=maxHP;
        this.attack=attack;
        this.level=1;
        this.internalForce=100;
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

    /**
     * 施展指定技能并返回造成的伤害。
     *
     * @param skill 敌人使用的技能
     * @return 技能造成的伤害
     */
    public int castSkill(Skill skill) {
        if (skill == null) {
            throw new IllegalArgumentException("技能不能为 null");
        }
        internalForce -= skill.getNeedInternalForce();
        int damage = skill.getDamage();
        System.out.printf("\n%s施展【%s】！%s\n", name, skill.getName(), skill.getDesc());
        System.out.printf("你受到 %d 点伤害！\n", damage);
        return damage;
    }

    public void loseHP(int damage){
        if(HP<=0) return;
        HP-=damage;
    }

    public boolean isDead() {
        return HP <= 0;
    }

    public void showStatus() {
        System.out.printf("【%s Lv.%d】气血：%d/%d\n", name,level, HP,maxHP );
    }
    
}
