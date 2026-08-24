package mindustry.world.blocks.payloads;

import mindustry.gen.*;
import mindustry.world.*;

public class NuclearWarhead extends Block{
    public float radius = 100f;

    public NuclearWarhead(String name){
        super(name);
        solid = true;
        update = true;
        sync = true;
        destructible = breakable = rebuildable = true;
        placeablePlayer = false;
        explosivenessScale = 100f; //nukes are explosive!!!
        priority = 500f; //enemies will try to target this to make it explode!!!
    }

    public class NuclearWarheadBuild extends Building{

    }
}