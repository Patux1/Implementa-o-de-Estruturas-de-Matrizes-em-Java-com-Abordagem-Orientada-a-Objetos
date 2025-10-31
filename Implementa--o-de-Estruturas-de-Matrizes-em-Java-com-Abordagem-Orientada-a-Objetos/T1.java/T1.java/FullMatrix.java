// Autor: cauã patussi rosa
import java.util.Iterator;

public class FullMatrix implements Matrix {
    private final int rows;
    private final int cols;
    private final double[][] data;

    public FullMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public FullMatrix(Matrix m) {
        this.rows = m.rows();
        this.cols = m.cols();
        this.data = new double[rows][cols];
        for (MatrixElement e : m) {
            data[e.row()][e.col()] = e.value();
        }
    }

    @Override
    public Matrix add(double s) {
        FullMatrix result = new FullMatrix(rows, cols);
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < cols; ++j)
                result.data[i][j] = this.data[i][j] + s;
        return result;
    }

    @Override
    public Matrix add(Matrix m) throws BadDimensionsException {
        if (m.rows() != rows || m.cols() != cols)
            throw new BadDimensionsException("Dimensões incompatíveis");
        FullMatrix result = new FullMatrix(rows, cols);
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < cols; ++j)
                result.data[i][j] = this.data[i][j] + m.get(i, j);
        return result;
    }

    @Override
    public Matrix sub(double s) {
        return this.add(-s);
    }

    @Override
    public Matrix sub(Matrix m) throws BadDimensionsException {
        if (m.rows() != rows || m.cols() != cols)
            throw new BadDimensionsException("Dimensões incompatíveis");
        FullMatrix result = new FullMatrix(rows, cols);
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < cols; ++j)
                result.data[i][j] = this.data[i][j] - m.get(i, j);
        return result;
    }

    @Override
    public Matrix mul(double s) {
        FullMatrix result = new FullMatrix(rows, cols);
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < cols; ++j)
                result.data[i][j] = this.data[i][j] * s;
        return result;
    }

    @Override
    public Vector mul(Vector v) throws BadDimensionsException {
        if (v.size() != cols)
            throw new BadDimensionsException("Dimensão do vetor incompatível");
        Vector result = new Vector(rows);
        for (int i = 0; i < rows; ++i) {
            double sum = 0;
            for (int j = 0; j < cols; ++j)
                sum += data[i][j] * v.get(j);
            result.set(i, sum);
        }
        return result;
    }

    @Override
    public Matrix mul(Matrix m) throws BadDimensionsException {
        if (this.cols != m.rows())
            throw new BadDimensionsException("Dimensões incompatíveis");
        FullMatrix result = new FullMatrix(this.rows, m.cols());
        for (int i = 0; i < this.rows; ++i)
            for (int j = 0; j < m.cols(); ++j) {
                double sum = 0;
                for (int k = 0; k < this.cols; ++k)
                    sum += this.data[i][k] * m.get(k, j);
                result.data[i][j] = sum;
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
        return rows * cols;
    }

    @Override
    public double get(int i, int j) {
        checkBounds(i, j);
        return data[i][j];
    }

    @Override
    public Vector row(int i) {
        if (i < 0 || i >= rows)
            throw new IndexOutOfBoundsException();
        Vector v = new Vector(cols);
        for (int j = 0; j < cols; ++j)
            v.set(j, data[i][j]);
        return v;
    }

    @Override
    public Vector col(int j) {
        if (j < 0 || j >= cols)
            throw new IndexOutOfBoundsException();
        Vector v = new Vector(rows);
        for (int i = 0; i < rows; ++i)
            v.set(i, data[i][j]);
        return v;
    }

    @Override
    public void set(int i, int j, double s) {
        checkBounds(i, j);
        data[i][j] = s;
    }

    @Override
    public void setRow(int i, Vector v) throws BadDimensionsException {
        if (i < 0 || i >= rows)
            throw new IndexOutOfBoundsException();
        if (v.size() != cols)
            throw new BadDimensionsException("Tamanho da linha inválido");
        for (int j = 0; j < cols; ++j)
            data[i][j] = v.get(j);
    }

    @Override
    public void setCol(int j, Vector v) throws BadDimensionsException {
        if (j < 0 || j >= cols)
            throw new IndexOutOfBoundsException();
        if (v.size() != rows)
            throw new BadDimensionsException("Tamanho da coluna inválido");
        for (int i = 0; i < rows; ++i)
            data[i][j] = v.get(i);
    }

    @Override
    public Iterator<MatrixElement> iterator() {
        return new FullMatrixIterator();
    }

    private class FullMatrixIterator implements Iterator<MatrixElement> {
        private int i = 0, j = 0;

        @Override
        public boolean hasNext() {
            return i < rows;
        }

        @Override
        public MatrixElement next() {
            MatrixElement e = new MatrixElement(i, j, data[i][j]);
            if (++j == cols) {
                j = 0;
                ++i;
            }
            return e;
        }
    }

    private void checkBounds(int i, int j) {
        if (i < 0 || i >= rows || j < 0 || j >= cols)
            throw new IndexOutOfBoundsException("Índices fora do intervalo");
    }
}
