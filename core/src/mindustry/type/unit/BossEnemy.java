package mindustry.type.unit;

//Fun fact, this was one of the first classes added to Mindustry, all the way back in 2017. I've had to make a bunch of changes to get it to even work, so it's not really that useful, but it's here.

import arc.graphics.g2d.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.type.*;

public class BossEnemy extends UnitType{

    public BossEnemy(String name){
        super(name);
        health = 260;
        hitSize = 8;
        speed = 0.27f;
        range = 70;
    }
}