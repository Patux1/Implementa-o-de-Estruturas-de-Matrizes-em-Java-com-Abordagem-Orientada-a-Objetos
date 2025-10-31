// Autor: cauã patussi rosa
public class MatrixElement {
    private final int row;
    private final int col;
    private final double value;

    public MatrixElement(int row, int col, double value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    public double value() {
        return value;
    }
}
