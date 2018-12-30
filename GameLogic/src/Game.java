import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Proinina Maria
 */
public class Game implements Serializable{
    public int getRows() {
        return rows;
    }

    int rows;

    public int getCols() {
        return cols;
    }

    int cols;

    public List<Player> getPlayers() {
        return players;
    }

    List<Player> players;

    public Player getDefeatedPlayers() {
        return defeatedPlayer ;
    }

    private Player defeatedPlayer;

    public int getActionsInTurn() {
        return actionsInTurn;
    }

    public void setActionsInTurn(int actionsInTurn) {
        this.actionsInTurn = actionsInTurn;
    }

    private int actionsInTurn;

    public int getRemainingKlops() {
        return remainingKlops;
    }

    public void setRemainingKlops(int remainingKlops) {
        this.remainingKlops = remainingKlops;
    }

    private int remainingKlops;

    public Field getField() {
        return field;
    }

    Field field;

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }


    int currentPlayer;

    public Game(int rows, int cols, List<Player> players, int actionsInTurn) {

        this.rows = rows;
        this.cols = cols;
        this.players = players;
        this.actionsInTurn = actionsInTurn;
        reset();

    }

    public void reset() {

        field = new Field(rows, cols);
        for (Player p : players) {
            field.getCell(p.getBaseY(), p.getBaseX()).setOwner(p);
            field.getCell(p.getBaseY(), p.getBaseX()).setState(ECellState.Base);

        }

        remainingKlops = actionsInTurn;
        currentPlayer = 0;



    }




    public void makeTurn(int x, int y) {
        if (!checkCoordinates(x, y)) return;
        FieldCell cell = field.getCell(x, y);
//        if (!cell.isAvailable())
//            return;
        //history?
        cell.setOwner(getCurrentPlayer());
        switch (cell.getState()) {
            case Alive:
                cell.setState(ECellState.Dead);
                break;
            case Base:
                cell.setState(ECellState.Dead);
                break;
            case Free:
                cell.setState(ECellState.Alive);
                break;
        }
        remainingKlops--;

        //switch turn?

    }

    public int updateCellsAvailable() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                field.getCell(j, i).setFlag(false);
            }
        }
        List<FieldCell> avail = markAvailableCells(field.getCell(getCurrentPlayer().getBaseX(), getCurrentPlayer().getBaseY()));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                field.getCell(i, j).setAvailable(avail.contains(field.getCell(i, j)));
            }
        }
        return avail.size();
    }

    protected List<FieldCell> markAvailableCells(FieldCell base) {

        List<FieldCell> avails = new LinkedList<>();
        if (base.getOwner() == getCurrentPlayer() || !base.isFlag())

        {
            base.setFlag(true);
            for (FieldCell cell : getNearbyCells(base)
                    ) {
                if (cell.isFlag()) continue;
                if (cell.getOwner() == getCurrentPlayer()) {
                    for (FieldCell c : markAvailableCells(cell)
                            ) {
                        avails.add(c);
                    }
                    continue;
                }
                if (cell.getState() != ECellState.Free && cell.getState() != ECellState.Alive)
                    continue;
                cell.setFlag(true);
                avails.add(cell);
            }
        }
        return avails;
    }

    private boolean checkCoordinates(int x, int y) {

        return x >= 0 && y >= 0 && x < cols && y < rows;
    }

    public void MakeTurn(FieldCell cell) {
        makeTurn(cell.y, cell.x);
    }

    public List<FieldCell> getNearbyCells(FieldCell c) {
        List<FieldCell> nearbyes = new ArrayList<>();
        int cx = c.y;
        int cy = c.x;
        boolean xNotMin = cx != 0;
        boolean xNotMax = cx < cols - 1;

        if (cy != 0) {
            if (xNotMin) nearbyes.add(field.getCell(cx - 1, cy - 1));
            nearbyes.add(field.getCell(cx, cy - 1));
            if (xNotMax) nearbyes.add(field.getCell(cx + 1, cy - 1));
        }

        if (xNotMin) nearbyes.add(field.getCell(cx - 1, cy));
        if (xNotMax) nearbyes.add(field.getCell(cx + 1, cy));

        if (cy != rows - 1) {
            if (xNotMin) nearbyes.add(field.getCell(cx - 1, cy + 1));
            nearbyes.add(field.getCell(cx, cy + 1));
            if (xNotMax) nearbyes.add(field.getCell(cx + 1, cy + 1));
        }
        return nearbyes;

    }

    public boolean isPlayerDefeated(Player p) {
        return defeatedPlayer == p;
    }
    public void switchTurn()
    {
        currentPlayer = (currentPlayer+1)%2;
        remainingKlops = actionsInTurn;


    }
    public void detectDefeatedPlayer()
    {

    }
    public FieldCell getFirstAlive(Player p)
    {
        for(int i = 0; i < rows; i ++)
        {
            for(int j = 0; j< cols; j++)
            {
                if(field.getCell(j,i).getOwner().getName().equals(p.getName()) && field.getCell(j,i).getState() == ECellState.Alive)
                    return field.getCell(j,i);
            }
        }
        return null;
    }

    public boolean checkGameEnd() {
        if(remainingKlops > 0 && markAvailableCells(players.get(currentPlayer).getPlayerBase()).size() == 0)
            return true;
        else return false;

    }
}
