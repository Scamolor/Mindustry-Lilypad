package mindustry.entities.comp;

import arc.*;
import arc.graphics.*;
import arc.math.geom.Vec2;
import arc.util.io.*;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import mindustry.ai.*;
import mindustry.annotations.Annotations.*;
import mindustry.async.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.type.*;
import mindustry.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;
import static mindustry.input.Binding.boost;

@Component
public abstract class SubmarineComp implements Entityc, Healthc, Velc, Unitc, Posc, WaterMovec{
    @Import
    UnitType type;
    @Import
    Vec2 vel;
    @Import
    float elevation, health, maxHealth, x, y, rotation;
    @Import
    boolean dead;
    public boolean isOnDeepwater, toggledSubmerge, underwater;

    public void submergeToggle(){
        if(Core.input.keyTap(Binding.boost)){
            toggledSubmerge = !toggledSubmerge;
        }
    }

    public boolean onDeep(){
        Tile tile = tileOn();
        return tile != null && tile.floor().isDeep();
    }

    public void updateSubmerging(boolean toggledSubmerge){
        if(dead || (this.health < 0.8 * this.maxHealth)) return;
        float target = toggledSubmerge ? -1f : 0f;
        if(toggledSubmerge && this.isOnDeepwater && (this.health >= 0.8 * this.maxHealth)){
            elevation = Mathf.approachDelta(elevation, target, type.riseSpeed);
        }else{
            elevation = Mathf.approachDelta(elevation, 0f, (type.riseSpeed * 5)); //emergency surface
        }
    }

    @Override
    public void update(){
        type.update(self());
        submergeToggle();
        updateSubmerging(toggledSubmerge);
        isOnDeepwater = this.onDeep();
        this.underwater = 0 > this.elevation;
        if(this.underwater){
            this.vel.limit(this.type().speed * 0.6f);
        }
        if(!this.onLiquid() && state.getPlanet() != Planets.tantros){
            kill();
        }
    }

    @MethodPriority(30f)
    @Replace
    @Override
    public void draw(){
        if(this.underwater){
            Drawf.underwater(() -> {
                type.draw(self());
            });
        } else {
            type.draw(self());
        }
    }
}