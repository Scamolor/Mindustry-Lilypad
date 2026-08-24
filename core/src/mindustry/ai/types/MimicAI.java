// AI class from older Mindustry version.
package mindustry.ai.types;

import mindustry.annotations.Annotations.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;

public class MimicAI extends AIController{
    public @Nullable Unitc control;

    public MimicAI(@Nullable Unitc control){
        this.control = control;
    }

    public MimicAI(){
    }

    public void update(){
        if(control != null){
            unit.controlWeapons(control.isRotate(), control.isShooting());
            //TODO this isn't accurate
            unit.moveAt(Tmp.v1.set(control.vel()).limit(unit.type().speed));
            if(control.isShooting()){
                unit.aimLook(control.aimX(), control.aimY());
            }else{
                unit.lookAt(unit.vel().angle());
            }
        }
    }

    public boolean isFollowing(Playerc player){
        return control == player.unit();
    }
}
