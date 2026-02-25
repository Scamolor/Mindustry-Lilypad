package mindustry.world.blocks.payloads;

import mindustry.world.Block;

public class NuclearWarhead extends Block{

    public NuclearWarhead(String name){
        super(name);

        destructible = true;
        breakable = true;
        rebuildable = true;
    }
}