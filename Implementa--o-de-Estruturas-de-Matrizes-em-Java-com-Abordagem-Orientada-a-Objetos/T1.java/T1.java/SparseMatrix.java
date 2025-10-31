// Autor: cauã patussi
import java.util.Iterator;

public class SparseMatrix implements Matrix {
    private final int rows, cols;
    private final Node[] rowSentinels;
    private final Node[] colSentinels;

    private static class Node {
        int row, col;
        double value;
        Node nextInRow, nextInCol;

        Node(int row, int col, double value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }
    }

    public SparseMatrix(Matrix m) {
        this.rows = m.rows();
        this.cols = m.cols();
        this.rowSentinels = new Node[rows];
        this.colSentinels = new Node[cols];

        for (int i = 0; i < rows; i++) {
            rowSentinels[i] = new Node(-1, -1, 0);
            rowSentinels[i].nextInRow = rowSentinels[i];
        }

        for (int j = 0; j < cols; j++) {
            colSentinels[j] = new Node(-1, -1, 0);
            colSentinels[j].nextInCol = colSentinels[j];
        }

        for (MatrixElement e : m) {
            if (e.value() != 0)
                insert(e.row(), e.col(), e.value());
        }
    }

    private void insert(int i, int j, double value) {
        Node newNode = new Node(i, j, value);

        Node rowHead = rowSentinels[i];
        Node p = rowHead;
        while (p.nextInRow != rowHead && p.nextInRow.col < j)
            p = p.nextInRow;
        newNode.nextInRow = p.nextInRow;
        p.nextInRow = newNode;

        Node colHead = colSentinels[j];
        p = colHead;
        while (p.nextInCol != colHead && p.nextInCol.row < i)
            p = p.nextInCol;
        newNode.nextInCol = p.nextInCol;
        p.nextInCol = newNode;
    }

    private void remove(int i, int j) {
        Node rowHead = rowSentinels[i];
        Node p = rowHead;
        while (p.nextInRow != rowHead) {
            if (p.nextInRow.col == j) {
                Node toRemove = p.nextInRow;
                p.nextInRow = toRemove.nextInRow;

                Node q = colSentinels[j];
                while (q.nextInCol != colSentinels[j]) {
                    if (q.nextInCol.row == i) {
                        q.nextInCol = q.nextInCol.nextInCol;
                        break;
                    }
                    q = q.nextInCol;
                }
                return;
            }
            p = p.nextInRow;
        }
    }
    public Matrix toFullMatrix() {
        FullMatrix fm = new FullMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            Node p = rowSentinels[i].nextInRow;
            while (p != rowSentinels[i]) {
                fm.set(p.row, p.col, p.value);
                p = p.nextInRow;
            }
        }
        return fm;
    }

    @Override
    public Matrix add(double s) {
        Matrix full = this.toFullMatrix();
        return full.add(s);
    }

    @Override
    public Matrix add(Matrix m) throws BadDimensionsException {
        if (m.rows() != rows || m.cols() != cols)
            throw new BadDimensionsException("Dimensões incompatíveis");

        if (m instanceof SparseMatrix) {
            SparseMatrix result = new SparseMatrix(this);
            for (MatrixElement e : m) {
                double sum = result.get(e.row(), e.col()) + e.value();
                result.set(e.row(), e.col(), sum);
            }
            return result;
        } else {
            return this.toFullMatrix().add(m);
        }
    }

    @Override
    public Matrix sub(double s) {
        return add(-s);
    }

    @Override
    public Matrix sub(Matrix m) throws BadDimensionsException {
        if (m.rows() != rows || m.cols() != cols)
            throw new BadDimensionsException("Dimensões incompatíveis");

        if (m instanceof SparseMatrix) {
            SparseMatrix result = new SparseMatrix(this);
            for (MatrixElement e : m) {
                double diff = result.get(e.row(), e.col()) - e.value();
                result.set(e.row(), e.col(), diff);
            }
            return result;
        } else {
            return this.toFullMatrix().sub(m);
        }
    }

    @Override
    public Matrix mul(double s) {
        SparseMatrix result = new SparseMatrix(this.toFullMatrix());
        for (MatrixElement e : this)
            result.set(e.row(), e.col(), e.value() * s);
        return result;
    }

    @Override
    public Vector mul(Vector v) throws BadDimensionsException {
        if (v.size() != cols)
            throw new BadDimensionsException("Tamanho incompatível");

        Vector result = new Vector(rows);
        for (int i = 0; i < rows; ++i) {
            double sum = 0;
            Node p = rowSentinels[i].nextInRow;
            while (p != rowSentinels[i]) {
                sum += p.value * v.get(p.col);
                p = p.nextInRow;
            }
            result.set(i, sum);
        }
        return result;
    }

    @Override
    public Matrix mul(Matrix m) throws BadDimensionsException {
        if (this.cols != m.rows())
            throw new BadDimensionsException("Dimensões incompatíveis");

        SparseMatrix result = new SparseMatrix(new FullMatrix(rows, m.cols()));
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < m.cols(); ++j) {
                double sum = 0;
                Node p = rowSentinels[i].nextInRow;
                while (p != rowSentinels[i]) {
                    sum += p.value * m.get(p.col, j);
                    p = p.nextInRow;
                }
                if (sum != 0)
                    result.set(i, j, sum);
            }
        }
        return result;
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int cols() {
        return cols;
    }

    @Override
    public int size() {
        int count = 0;
        for (int i = 0; i < rows; ++i) {
            Node p = rowSentinels[i].nextInRow;
            while (p != rowSentinels[i]) {
                count++;
                p = p.nextInRow;
            }
        }
        return count;
    }

    @Override
    public double get(int i, int j) {
        checkBounds(i, j);
        Node p = rowSentinels[i].nextInRow;
        while (p != rowSentinels[i]) {
            if (p.col == j)
                return p.value;
            if (p.col > j)
                break;
            p = p.nextInRow;
        }
        return 0;
    }

    @Override
    public Vector row(int i) {
        checkBounds(i, 0);
        Vector v = new Vector(cols);
        Node p = rowSentinels[i].nextInRow;
        while (p != rowSentinels[i]) {
            v.set(p.col, p.value);
            p = p.nextInRow;
        }
        return v;
    }

    @Override
    public Vector col(int j) {
        checkBounds(0, j);
        Vector v = new Vector(rows);
        Node p = colSentinels[j].nextInCol;
        while (p != colSentinels[j]) {
            v.set(p.row, p.value);
            p = p.nextInCol;
        }
        return v;
    }

    @Override
    public void set(int i, int j, double value) {
        checkBounds(i, j);
        if (value == 0) {
            remove(i, j);
            return;
        }
        Node p = rowSentinels[i];
        while (p.nextInRow != rowSentinels[i]) {
            if (p.nextInRow.col == j) {
                p.nextInRow.value = value;
                return;
            }
            if (p.nextInRow.col > j) break;
            p = p.nextInRow;
        }
        insert(i, j, value);
    }

    @Override
    public void setRow(int i, Vector v) throws BadDimensionsException {
        if (v.size() != cols)
            throw new BadDimensionsException("Tamanho de linha incompatível");
        for (int j = 0; j < cols; j++)
            set(i, j, v.get(j));
    }

    @Override
    public void setCol(int j, Vector v) throws BadDimensionsException {
        if (v.size() != rows)
            throw new BadDimensionsException("Tamanho de coluna incompatível");
        for (int i = 0; i < rows; i++)
            set(i, j, v.get(i));
    }

    @Override
    public Iterator<MatrixElement> iterator() {
        return new Iterator<MatrixElement>() {

            private int currentRow = 0;
            private Node current = null;

            {
                advanceToNext();
            }

            private void advanceToNext() {
                while (currentRow < rows) {
                    current = rowSentinels[currentRow].nextInRow;
                    if (current != rowSentinels[currentRow]) break;
                    currentRow++;
                }
            }

            @Override
            public boolean hasNext() {
                return currentRow < rows && current != rowSentinels[currentRow];
            }

            @Override
            public MatrixElement next() {
                MatrixElement e = new MatrixElement(current.row, current.col, current.value);
                current = current.nextInRow;
                if (current == rowSentinels[currentRow]) {
                    currentRow++;
                    advanceToNext();
                }
                return e;
            }
        };
    }

    private void checkBounds(int i, int j) {
        if (i < 0 || i >= rows || j < 0 || j >= cols)
            throw new IndexOutOfBoundsException("Índices inválidos: " + i + "," + j);
    }
}
