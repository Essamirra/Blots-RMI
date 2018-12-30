/**
 * Created by Proinina Maria
 */
public interface IView {
    void setConnected(boolean isConnected);
    void setPlayerName(String playerName);
    void setField(Field f);
    void updateField(Field f);
    void showWin(boolean isWinner);
    void disableControls();
    void enableControls();
    void setTurn(boolean isMyTurn);



}
