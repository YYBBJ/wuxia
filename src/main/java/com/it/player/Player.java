package com.it.player;

import com.it.common.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private String name;
    private int HP; //当前血量
    private int maxHP;//最大血量
    private int level; //等级
    private int maxInternalForce; //内力
    private int internalForce;//当前内力
    private int cultivation; //修为
    private List<Skill>skills; //技能
    private Random random=new Random();
    public Player(String name){
        this.name=name;
        this.maxHP=100;
        this.level=1;
        this.maxInternalForce=100;
        cultivation=0;
        this.HP=maxHP;
        this.internalForce=maxInternalForce;
        this.skills=new ArrayList<Skill>();
        skills.add(new Skill("流云剑式", 8, 12, "轻剑快攻，剑光一闪刺向敌人！"));
        skills.add(new Skill("长虹贯日", 20, 30, "蓄力一击，长虹剑气破空而出！"));
        skills.add(new Skill("静心诀", 12, 0, "凝神调息，恢复自身气血！"));
    }

    /**
     *
     * @param skill 技能
     * @return 返回造成的伤害
     */
    public int castSkill(Skill skill) {
        if (skill == null) {
            throw new IllegalArgumentException("技能不能为 null");
        }
        if (!hasEnoughInternalForce(skill)) {
            throw new IllegalStateException("内力不足，无法施展");
        }
        int damage=skill.getDamage();
        //减少内力
        internalForce-=skill.getNeedInternalForce();
        if (skill.getName().equals("静心诀")) {
            // 回血技能，不造成伤害
            int healed = Math.min(20 + level * 3, maxHP - HP);
            HP += healed;
            return 0;
        }else{
            //20%机率暴击
            boolean isCrit=random.nextDouble()< 0.2;
            damage=skill.getDamage()+level*4;
            if (isCrit) {
                damage*=2;
            }
        }
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

    //扣血
    public void loseHP(Skill skill) {
        if(HP<=0) return;
        HP-=skill.getDamage();
    }

    public boolean isDead() {
        return HP <= 0;
    }

    /** 战斗完成后恢复全部气血和内力。 */
    public void recoverAfterBattle() {
        HP = maxHP;
        internalForce = maxInternalForce;
    }

    /**
     * 根据当前修为提升等级。
     *
     * @return 本次提升的等级数
     */
    public int checkLevelUp(){
        int levelsGained = 0;
        while (cultivation >= level * 50) {
            level++;
            levelsGained++;
            maxHP += 25;
            maxInternalForce += 15;
            HP = maxHP;
            internalForce = maxInternalForce;
        }
        return levelsGained;
    }
    // 打印玩家状态
    public void showStatus() {
        System.out.printf("\n【%s Lv.%d】气血：%d/%d | 内力：%d/%d | 修为：%d\n",
                name, level, HP, maxHP, internalForce, maxInternalForce, cultivation);
    }

}
