package com.it.enemy;

/** 敌人类型及对应的攻击力范围。 */
public enum EnemyType {
    SHAN_ZEI_DAO_FEI("山贼盗匪", 1, 10, 20),
    MEN_PAI_DI_ZI("门派弟子", 2, 20, 30),
    JIANG_HU_GAO_SHOU("江湖高手", 3, 30, 40),
    MO_DAO_ZONG_SHI("魔道宗师", 4, 40, 50);

    private final String displayName;
    private final int level;
    private final int minAttack;
    private final int maxAttack;

    EnemyType(String displayName, int level, int minAttack, int maxAttack) {
        this.displayName = displayName;
        this.level = level;
        this.minAttack = minAttack;
        this.maxAttack = maxAttack;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }

    public int getMinAttack() {
        return minAttack;
    }

    public int getMaxAttack() {
        return maxAttack;
    }
}
