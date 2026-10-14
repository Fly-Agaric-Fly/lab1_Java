package lab1;

public class Matrix { // класс для выполнения матричных операций над комплексными числами

    private final Complex[][] data; // двумерный массив элементов матрицы
    private final int rows;         // число строк матрицы
    private final int cols;         // число столбцов матрицы

    /**создаем матрицу заданного размера, заполненную нулями (0+0i)*/
    public Matrix(int rows, int cols) { 
        if (rows <= 0 || cols <= 0) { // проверяем, что размеры положительные
            throw new IllegalArgumentException("Размер матрицы должны быть положительным."); 
        }
        this.rows = rows; 
        this.cols = cols; 
        this.data = new Complex[rows][cols]; // Выделяем память под двумерный массив
        for (int i = 0; i < rows; i++) { 
            for (int j = 0; j < cols; j++) { 
                data[i][j] = new Complex(0, 0); // каждый элемент — комплексный ноль
            }
        }
    }

    /**создаём матрицу из готового двумерного массива*/
    public Matrix(Complex[][] values) { 
        if (values == null || values.length == 0 || values[0].length == 0) { // проверяем, что массив не пустой
            throw new IllegalArgumentException("Некорректные данные матрицы.");
        }
        this.rows = values.length; 
        this.cols = values[0].length; 
        this.data = new Complex[rows][cols]; // выделяем память под собственную копию
        for (int i = 0; i < rows; i++) { 
            if (values[i].length != cols) { // проверяем, что все строки одинаковой длины
                throw new IllegalArgumentException("Матрица должна быть прямоугольной."); 
            }
            for (int j = 0; j < cols; j++) { 
                this.data[i][j] = values[i][j]; 
            }
        }
    }

    public int getRows() { return rows; } 
    public int getCols() { return cols; } 

    public Complex get(int i, int j) { // получаем элемент по индексам
        return data[i][j]; 
    }

    public void set(int i, int j, Complex value) { // устанавливаем элемент по индексам
        data[i][j] = value; 
    }

    /**сложение матриц: C[i][j] = A[i][j] + B[i][j]*/
    public Matrix add(Matrix other) { // other — второе слагаемое
        checkSameSize(other); // Проверяем, что размеры совпадают
        Matrix result = new Matrix(rows, cols); 
        for (int i = 0; i < rows; i++) { 
            for (int j = 0; j < cols; j++) { 
                result.data[i][j] = this.data[i][j].add(other.data[i][j]);
            }
        }
        return result; 
    }

    /**умножение матриц: C[i][j] = сумма по k от A[i][k] * B[k][j]*/
    public Matrix multiply(Matrix other) { // other — второй множитель
        if (this.cols != other.rows) { // Проверяем совместимость размеров
            throw new IllegalArgumentException(
                "Несовместимые размеры для умножения: " +
                this.rows + "x" + this.cols + " и " +
                other.rows + "x" + other.cols); // Сообщение с размерами обеих матриц
        }
        Matrix result = new Matrix(this.rows, other.cols); // Результат: rows(A) × cols(B)
        for (int i = 0; i < this.rows; i++) { 
            for (int j = 0; j < other.cols; j++) { 
                Complex sum = new Complex(0, 0);
                for (int k = 0; k < this.cols; k++) { 
                    sum = sum.add(this.data[i][k].multiply(other.data[k][j])); 
                }
                result.data[i][j] = sum; 
            }
        }
        return result; 
    }

    /**транспонирование: строки становятся столбцами*/
    public Matrix transpose() { 
        Matrix result = new Matrix(cols, rows); // размеры «переворачиваются»: cols × rows
        for (int i = 0; i < rows; i++) { 
            for (int j = 0; j < cols; j++) { 
                result.data[j][i] = this.data[i][j]; // элемент [i][j] переезжает в [j][i]
            }
        }
        return result; 
    }

    /**определитель квадратной матрицы методом Гаусса*/
    public Complex determinant() { // возвращает комплексное число
        if (rows != cols) { // только для квадратных матриц
            throw new IllegalArgumentException("Определитель существует только для квадратных матриц.");
        }
        int n = rows; 
        Complex[][] a = new Complex[n][n]; // копия матрицы, чтобы не портить исходную
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++) 
                a[i][j] = data[i][j]; 

        Complex det = new Complex(1, 0); 
        for (int i = 0; i < n; i++) { // идём по диагонали
            int pivot = i; // ищем ведущий элемент в столбце i
            while (pivot < n && a[pivot][i].abs() < 1e-12) pivot++; // пропускаем «нулевые» строки
            if (pivot == n) return new Complex(0, 0); // весь столбец нулевой — det = 0

            if (pivot != i) { // если ведущий элемент не на диагонали
                Complex[] tmp = a[i]; a[i] = a[pivot]; a[pivot] = tmp; // меняем строки местами
                det = det.multiply(new Complex(-1, 0)); // каждая перестановка меняет знак det
            }
            det = det.multiply(a[i][i]); // умножаем det на диагональный элемент
            for (int j = i + 1; j < n; j++) { 
                Complex factor = a[j][i].divide(a[i][i]);
                for (int k = i; k < n; k++) { 
                    a[j][k] = a[j][k].subtract(factor.multiply(a[i][k]));
                }
            }
        }
        return det; // итог — произведение диагональных элементов с учётом знака
    }

    /**деление матриц: A / B = A * B^(-1)*/
    public Matrix divide(Matrix other) { // other — делитель
        if (other.rows != other.cols) { // проверяем, что делитель квадратный
            throw new IllegalArgumentException("Делитель должен быть квадратной матрицей.");
        }
        if (this.cols != other.rows) { // проверяем совместимость размеров
            throw new IllegalArgumentException("Несовместимые размеры для деления.");
        }
        Matrix inverse = other.inverse(); // считаем обратную матрицу делителя
        return this.multiply(inverse); // умножаем A на обратную
    }

    /**обратная матрица через метод Гаусса-Жордана*/
    public Matrix inverse() {
        if (rows != cols) { 
            throw new IllegalArgumentException("Обратная матрица существует только для квадратных матриц.");
        }
        int n = rows; // Размер
        Complex[][] a = new Complex[n][2 * n]; // расширенная матрица [A | I]
        for (int i = 0; i < n; i++) { 
            for (int j = 0; j < n; j++) { 
                a[i][j] = data[i][j]; 
                a[i][j + n] = (i == j) ? new Complex(1, 0) : new Complex(0, 0); 
            }
        }

        for (int i = 0; i < n; i++) { 
            int pivot = i; 
            while (pivot < n && a[pivot][i].abs() < 1e-12) pivot++; // пропускаем нулевые
            if (pivot == n) { // если весь столбец нулевой
                throw new ArithmeticException("Матрица вырождена, обратной не существует.");
            }
            if (pivot != i) { 
                Complex[] tmp = a[i]; a[i] = a[pivot]; a[pivot] = tmp;
            }

            Complex diag = a[i][i]; // диагональный элемент
            for (int j = 0; j < 2 * n; j++) { 
                a[i][j] = a[i][j].divide(diag);
            }

            for (int k = 0; k < n; k++) { // обнуляем столбец i во всех остальных строках
                if (k == i) continue; // кроме самой строки i
                Complex factor = a[k][i]; 
                for (int j = 0; j < 2 * n; j++) { 
                    a[k][j] = a[k][j].subtract(factor.multiply(a[i][j])); 
                }
            }
        }

        Matrix result = new Matrix(n, n); // результат n × n
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++) 
                result.data[i][j] = a[i][j + n]; 
        return result; 
    }

    /**пПроверка: одинаковые ли размеры у двух матриц */
    private void checkSameSize(Matrix other) { 
        if (this.rows != other.rows || this.cols != other.cols) { // сравниваем размеры
            throw new IllegalArgumentException("Размеры матриц должны совпадать"); 
        }
    }

    /**вывод матрицы в консоль.*/
    @Override // Переопределяем toString из Object
    public String toString() { 
        StringBuilder sb = new StringBuilder(); 
        for (int i = 0; i < rows; i++) {
            sb.append("[ "); 
            for (int j = 0; j < cols; j++) { 
                sb.append(String.format("%-18s", data[i][j].toString()));
            }
            sb.append("]\n"); 
        }
        return sb.toString(); 
    }
}