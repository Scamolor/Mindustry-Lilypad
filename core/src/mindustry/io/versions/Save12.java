package mindustry.io.versions;

import arc.*;
import arc.struct.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.io.*;
import mindustry.mod.*;

import java.io.*;

import static mindustry.Vars.*;

/** Changes data patches to be read before content, and adds support for more complex data patch IO. */
public class Save12 extends SaveVersion{

    public Save12(){
        super(12);
    }
}
