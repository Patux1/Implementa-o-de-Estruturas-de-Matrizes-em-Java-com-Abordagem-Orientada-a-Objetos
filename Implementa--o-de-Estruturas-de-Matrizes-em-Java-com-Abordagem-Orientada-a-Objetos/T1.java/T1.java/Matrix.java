import java.util.Iterator;

public interface Matrix extends Iterable<MatrixElement> {
    Matrix add(double s);
    Matrix add(Matrix m) throws BadDimensionsException;
    Matrix sub(double s);
    Matrix sub(Matrix m) throws BadDimensionsException;
    Matrix mul(double s);
    Vector mul(Vector v) throws BadDimensionsException;
    Matrix mul(Matrix m) throws BadDimensionsException;

    int rows();
    int cols();
    int size();
    double get(int i, int j);
    Vector row(int i);
    Vector col(int j);
    void set(int i, int j, double s);
    void setRow(int i, Vector v) throws BadDimensionsException;
    void setCol(int j, Vector v) throws BadDimensionsException;
    Iterator<MatrixElement> iterator();
}
