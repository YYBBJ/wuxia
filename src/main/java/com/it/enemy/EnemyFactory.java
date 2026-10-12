package com.it.enemy;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 简单工厂：封装敌人的类型选择和创建过程。
 */
public final class EnemyFactory {
    private EnemyFactory() {
    }

    public static Enemy createRandomEnemy() {
        EnemyType[] types = EnemyType.values();
        EnemyType type = types[ThreadLocalRandom.current().nextInt(types.length)];
        return createEnemy(type);
    }

    public static Enemy createEnemy(EnemyType type) {
        if (type == null) {
            throw new IllegalArgumentException("敌人类型不能为 null");
        }
        return switch (type) {
            case SHAN_ZEI_DAO_FEI -> createBandit();
            case MEN_PAI_DI_ZI -> createSectDisciple();
            case JIANG_HU_GAO_SHOU -> createMartialArtsExpert();
            case MO_DAO_ZONG_SHI -> createDemonicGrandmaster();
        };
    }
    
    private static Enemy createBandit() {
        return createEnemyWithType(EnemyType.SHAN_ZEI_DAO_FEI);
    }

    private static Enemy createSectDisciple() {
        return createEnemyWithType(EnemyType.MEN_PAI_DI_ZI);
    }

    private static Enemy createMartialArtsExpert() {
        return createEnemyWithType(EnemyType.JIANG_HU_GAO_SHOU);
    }

    private static Enemy createDemonicGrandmaster() {
        return createEnemyWithType(EnemyType.MO_DAO_ZONG_SHI);
    }

    private static Enemy createEnemyWithType(EnemyType type) {
        int attack = ThreadLocalRandom.current()
                .nextInt(type.getMinAttack(), type.getMaxAttack() + 1);
        return new Enemy(type.getDisplayName(), attack, type.getLevel());
    }
}
