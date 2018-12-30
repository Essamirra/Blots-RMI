import java.awt.*;
import java.io.Serializable;

/**
 * Created by Proinina Maria
 */
public class Player implements Serializable{


    public String getName() {
        return name;
    }

    String name;

    public int getBaseX() {
        return baseX;
    }

    public int getBaseY() {
        return baseY;
    }

    public Color getC() {
        return c;
    }

    int baseX;
    int baseY;
    Color c;

    public FieldCell getPlayerBase()
    {
        return new FieldCell(baseX, baseY);
    }

    public Player(String name, int baseX, int baseY, Color c) {
        this.name = name;
        this.baseX = baseX;
        this.baseY = baseY;
        this.c = c;
    }
}
