package com.it.enemy;

public class NormalEnemyFactory implements EnemyFactory{
    @Override
    public Enemy createEnemy() {
        return new Enemy("===",10);
    }
}
