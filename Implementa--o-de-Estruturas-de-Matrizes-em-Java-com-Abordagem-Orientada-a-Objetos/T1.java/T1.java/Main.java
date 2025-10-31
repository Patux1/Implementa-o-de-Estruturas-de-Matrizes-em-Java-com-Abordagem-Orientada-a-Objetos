// Autor: Cauã Patussi Rosa

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("==== TESTES COM FullMatrix ====");
            Matrix fm = new FullMatrix(3, 3);
            fm.set(0, 0, 1);
            fm.set(0, 1, 2);
            fm.set(1, 1, 3);
            fm.set(2, 2, 4);
            printMatrix("FullMatrix Inicial", fm);

            Vector v = new Vector(3);
            v.set(0, 1);
            v.set(1, 2);
            v.set(2, 3);

            Vector resVec = fm.mul(v);
            System.out.print("Produto FullMatrix * Vector: ");
            for (double d : resVec) System.out.print(d + " ");
            System.out.println();

            Matrix fAdd = fm.add(1);
            printMatrix("FullMatrix + 1", fAdd);

            Matrix fSub = fm.sub(1);
            printMatrix("FullMatrix - 1", fSub);

            Matrix fMul = fm.mul(2);
            printMatrix("FullMatrix * 2", fMul);

            Matrix fm2 = new FullMatrix(fm);
            fm2.set(0, 0, 5);
            Matrix fAdd2 = fm.add(fm2);
            printMatrix("FullMatrix + FullMatrix", fAdd2);

            System.out.println("\n==== TESTES COM SparseMatrix ====");
            SparseMatrix sm = new SparseMatrix(fm);
            printMatrix("SparseMatrix criada a partir da FullMatrix", sm);

            Matrix smAdd = sm.add(1);
            printMatrix("SparseMatrix + 1", smAdd);

            Matrix smSub = sm.sub(0.5);
            printMatrix("SparseMatrix - 0.5", smSub);

            Matrix smMul = sm.mul(2);
            printMatrix("SparseMatrix * 2", smMul);

            SparseMatrix sm2 = new SparseMatrix(sm);
            sm2.set(1, 1, 1);
            Matrix smAdd2 = sm.add(sm2);
            printMatrix("SparseMatrix + SparseMatrix", smAdd2);

            Matrix smFullMul = sm.mul(fm);
            printMatrix("SparseMatrix * FullMatrix", smFullMul);

            Matrix fullSparseMul = fm.mul(sm);
            printMatrix("FullMatrix * SparseMatrix", fullSparseMul);

            Matrix smToFull = sm.toFullMatrix();
            printMatrix("SparseMatrix convertida para FullMatrix", smToFull);

            System.out.println("\n==== TESTES DE EXCEÇÕES ====");

            // Soma com dimensões incompatíveis
            try {
                Matrix invalidMatrix = new FullMatrix(2, 2);
                fm.add(invalidMatrix);
            } catch (BadDimensionsException e) {
                System.out.println("[ERRO OK] BadDimensionsException na soma: " + e.getMessage());
            }

            // setRow com vetor de tamanho errado
            try {
                Vector linhaErrada = new Vector(5);
                fm.setRow(0, linhaErrada);
            } catch (BadDimensionsException e) {
                System.out.println("[ERRO OK] BadDimensionsException em setRow: " + e.getMessage());
            }

            // setCol com vetor de tamanho errado
            try {
                Vector colunaErrada = new Vector(1);
                fm.setCol(0, colunaErrada);
            } catch (BadDimensionsException e) {
                System.out.println("[ERRO OK] BadDimensionsException em setCol: " + e.getMessage());
            }

            // get fora dos limites
            try {
                fm.get(-1, 10);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("[ERRO OK] IndexOutOfBoundsException em get(): " + e.getMessage());
            }

            // set fora dos limites
            try {
                fm.set(100, 0, 9.9);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("[ERRO OK] IndexOutOfBoundsException em set(): " + e.getMessage());
            }

            // Multiplicação com matriz de dimensão errada
            try {
                Matrix bad = new FullMatrix(1, 5);
                fm.mul(bad);
            } catch (BadDimensionsException e) {
                System.out.println("[ERRO OK] BadDimensionsException em multiplicação: " + e.getMessage());
            }

            // Produto com vetor de tamanho inválido
            try {
                Vector errado = new Vector(5);
                sm.mul(errado);
            } catch (BadDimensionsException e) {
                System.out.println("[ERRO OK] BadDimensionsException em produto matriz * vetor: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("[ERRO] " + e.getMessage());
        }
    }

    private static void printMatrix(String title, Matrix m) {
        System.out.println("\n" + title + " (" + m.rows() + "x" + m.cols() + ")");
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) {
                System.out.print(m.get(i, j) + "\t");
            }
            System.out.println();
        }
    }
}
