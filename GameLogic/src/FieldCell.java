import java.io.Serializable;

/**
 * Created by Proinina Maria
 */
public class FieldCell implements Serializable {
    int x;
    int y;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    boolean flag;
    public ECellState getState() {
        return state;
    }

    public void setState(ECellState state) {
        this.state = state;
    }

    ECellState state;

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    Player owner;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    boolean available;

    public FieldCell(int x, int y) {
        this.x = x;
        this.y = y;
        available = false;
        flag = false;
        owner = null;
        state = ECellState.Free;
    }

}
