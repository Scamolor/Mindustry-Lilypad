package mindustry.world.blocks.environment;

import mindustry.world.meta.*;

/** Class for quickly defining a floor with no water and no variants. Offers no new functionality. I don't know if it'll work in modern builds, feel free to test. */
public class MetalFloor extends Floor{

    public MetalFloor(String name){
        super(name);
        variants = 0;
        attributes.set(Attribute.water, -1);
    }

    public MetalFloor(String name, int variants){
        super(name);
        this.variants = variants;
        attributes.set(Attribute.water, -1);
    }
}
