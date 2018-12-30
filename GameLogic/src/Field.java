import java.io.Serializable;

/**
 * Created by Proinina Maria
 */
public class Field implements Serializable {
    private FieldCell[][] field;
    private int rows;
    private int cols;

    public Field(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        field = new FieldCell[rows][cols];
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                field[i][j] = new FieldCell(j,i);
            }
        }

    }


    public FieldCell getCell(int i, int j)
    {
        return field[i][j];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
