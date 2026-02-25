package mindustry.world.blocks.payloads;

import mindustry.world.blocks.payloads.PayloadBlock;

public class BallisticSilo extends PayloadBlock{
    public BallisticSilo(String name){
        super(name);
        // placeholder; add launch logic in build class later
    }

    public class BallisticSiloBuild extends PayloadBlockBuild{
        // later: store one payload, launch, etc.
    }
}