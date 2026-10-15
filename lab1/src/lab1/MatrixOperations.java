package lab1;

public final class MatrixOperations { //класс для операций над матрицами

    private MatrixOperations() {}

    /**сложение двух матриц*/
    public static Matrix add(Matrix a, Matrix b) {
        return a.add(b); 
    }

    /**умножение двух матриц*/
    public static Matrix multiply(Matrix a, Matrix b) { 
        return a.multiply(b); 
    }

    /**деление матриц: A / B = A * B^(-1)*/
    public static Matrix divide(Matrix a, Matrix b) { // a — делимое, b — делитель
        return a.divide(b); // проверяем размеры и обратимость
    }

    /**транспонирование матрицы.*/
    public static Matrix transpose(Matrix a) { 
        return a.transpose(); 
    }

    /**определитель квадратной матрицы*/
    public static Complex determinant(Matrix a) {
        return a.determinant(); 
    }

    /** обратная матрица*/
    public static Matrix inverse(Matrix a) { 
        return a.inverse(); 
    }
}
