// Autor: cauã patussi rosa
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Vector implements Iterable<Double> {
    private final double[] data;

    public Vector(int size) {
        if (size < 0)
            throw new IllegalArgumentException("Tamanho inválido");
        this.data = new double[size];
    }

    public int size() {
        return data.length;
    }

    public double get(int i) {
        if (i < 0 || i >= data.length)
            throw new IndexOutOfBoundsException("Índice inválido");
        return data[i];
    }

    public void set(int i, double value) {
        if (i < 0 || i >= data.length)
            throw new IndexOutOfBoundsException("Índice inválido");
        data[i] = value;
    }

    @Override
    public Iterator<Double> iterator() {
        return new VectorIterator();
    }

    private class VectorIterator implements Iterator<Double> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < data.length;
        }

        @Override
        public Double next() {
            if (!hasNext()) throw new NoSuchElementException();
            return data[index++];
        }
    }
}
