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
    private int maxInternalForce;
    private List<Skill> skills;
    private Random random = new Random();

    public Enemy(String name, int attack) {
        this(name, attack, 1);
    }

    public Enemy(String name, int attack, int level) {
        if (level < 1) {
            throw new IllegalArgumentException("敌人等级必须大于 0");
        }
        this.name = name;
        this.maxHP = 100;
        this.HP = maxHP;
        this.attack = attack;
        this.level = level;
        this.maxInternalForce = 100 + (level - 1) * 15;
        this.internalForce = maxInternalForce;
    }

    public int enemyAttack(){
        boolean heavy = random.nextDouble() < 0.25;
        return heavy ? attack * 2 : attack;
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
        if (!hasEnoughInternalForce(skill)) {
            throw new IllegalStateException("敌人内力不足，无法施展技能");
        }
        internalForce -= skill.getNeedInternalForce();
        int damage = skill.getDamage();
        System.out.printf("\n%s施展【%s】！%s\n", name, skill.getName(), skill.getDesc());
        System.out.printf("你受到 %d 点伤害！\n", damage);
        return damage;
    }

    /**
     * 判断当前内力是否足以施展指定技能。
     *
     * @param skill 要施展的技能
     * @return 内力足够时返回 true
     */
    public boolean hasEnoughInternalForce(Skill skill) {
        if (skill == null) {
            throw new IllegalArgumentException("技能不能为 null");
        }
        return internalForce >= skill.getNeedInternalForce();
    }

    public void loseHP(int damage){
        if(HP<=0) return;
        HP-=damage;
    }

    public boolean isDead() {
        return HP <= 0;
    }

    public void showStatus() {
        System.out.printf("【%s Lv.%d】气血：%d/%d | 内力：%d/%d%n",
                name, level, HP, maxHP, internalForce, maxInternalForce);
    }
    
}
