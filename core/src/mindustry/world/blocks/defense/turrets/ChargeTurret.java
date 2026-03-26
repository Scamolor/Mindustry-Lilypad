//basically useless

package mindustry.world.blocks.defense.turrets;

import arc.audio.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.type.*;
import mindustry.gen.*;
//import mindustry.world.blocks.defense.turrets.*;

import static mindustry.Vars.*;

public class ChargeTurret extends PowerTurret{
    public float chargeTime = 30f;
    public int chargeEffects = 5;
    public float chargeMaxDelay = 10f;
    public Effect chargeEffect = Fx.none;
    public Effect chargeBeginEffect = Fx.none;
    public Sound chargeSound = Sounds.none;

    public ChargeTurret(String name){
        super(name);
    }

    public class ChargeTurretBuild extends PowerTurretBuild{
        public boolean shooting;

        @Override
        public void shoot(BulletType ammo){
            useAmmo();

            this.trns(rotation, size * tilesize / 2f);
            chargeBeginEffect.at(x + this.x, y + this.y, rotation);
            chargeSound.at(x + this.x, y + this.y, 1);
            
            for(int i = 0; i < chargeEffects; i++){
                Time.run(Mathf.random(chargeMaxDelay), () -> {
                    if(!isValid()) return;
                    this.trns(rotation, size * tilesize / 2f);
                    chargeEffect.at(x + this.x, y + this.y, rotation);
                });
            }

            shooting = true;

            Time.run(chargeTime, () -> {
                if(!isValid()) return;
                this.trns(rotation, size * tilesize / 2f);
                recoil = 1f;
                heat = 1f;
                shooting = false;
            });
        }

        @Override
        public boolean shouldTurn(){
            return !shooting;
        }
    }
}
