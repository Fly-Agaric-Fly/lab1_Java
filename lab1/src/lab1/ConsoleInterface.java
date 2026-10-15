package lab1;

import java.util.Scanner;

public class ConsoleInterface { //класс, в котором прописан консольный интерфейс

    private final Scanner scanner; // читаем ввод пользователя из консоли
    private Matrix lastResult;     // «последний результат» — то, к чему применяем операции

    
    public ConsoleInterface() { 
        this.scanner = new Scanner(System.in); // System.in — стандартный поток ввода
    }

    
    public void run() { 
        boolean running = true; // флаг работы цикла
        while (running) { 
            printMainMenu(); 
            int choice = readInt("Ваш выбор: "); 
            switch (choice) { 
                case 1: createNewMatrix(); break;  // создать матрицу
                case 2: performOperation(); break; // выполнить операцию
                case 3: showLastResult(); break;   // показать последний результат
                case 4:                            // выход
                    running = false;               // сбрасываем флаг — цикл завершится
                    break;
                default:
                    System.out.println("Неверный пункт меню.");
            }
        }
    }

    private void printMainMenu() { 
        System.out.println("\nГЛАВНОЕ МЕНЮ"); 
        System.out.println("1. Создать новую матрицу"); 
        System.out.println("2. Выполнить операцию");    
        System.out.println("3. Показать последний результат"); 
        System.out.println("4. Выход");                 
    }

    /**создаёт новую матрицу: спрашивает размеры и каждый элемент*/
    private void createNewMatrix() { 
        System.out.println("\nСОЗДАНИЕ МАТРИЦЫ"); 
        int r = readInt("Введите число строк: ");   
        int c = readInt("Введите число столбцов: "); 

        Matrix m = new Matrix(r, c); // создаём матрицу нужного размера (пока заполнена нулями)
        for (int i = 0; i < r; i++) { 
            for (int j = 0; j < c; j++) { 
                System.out.printf("Элемент [%d][%d]:%n", i, j); 
                double re = readDouble("  действительная часть: "); 
                double im = readDouble("  мнимая часть: ");        
                m.set(i, j, new Complex(re, im)); // 
            }
        }
        lastResult = m; // сохраняем созданную матрицу как «последний результат»
        System.out.println("\nМатрица создана и сохранена как последний результат:"); 
        System.out.println(m); 
    }

    /**показывает текущий «последний результат»*/
    private void showLastResult() { 
        if (lastResult == null) { // Если ещё ничего не создавали
            System.out.println("Результат пока пуст."); 
            return; 
        }
        System.out.println("Последний результат:"); 
        System.out.println(lastResult); 
    }

    /**выполняем выбранную операцию над «последним результатом»*/
    private void performOperation() { 
        if (lastResult == null) { // Если матрица ещё не создана
            System.out.println("Сначала создайте матрицу (пункт 1)."); 
            return; 
        }

        System.out.println("\nВЫБОР ОПЕРАЦИИ"); 
        System.out.println("Текущий последний результат:"); 
        System.out.println(lastResult); // Показываем текущее состояние

        System.out.println("1. Прибавить матрицу");   
        System.out.println("2. Умножить на матрицу"); 
        System.out.println("3. Разделить на матрицу"); 
        System.out.println("4. Транспонировать");     
        System.out.println("5. Вычислить определитель"); 
        int op = readInt("Ваш выбор: "); 

        try { // оборачиваем в try — чтобы поймать ошибки от матричных операций
            switch (op) { 
                case 1: { // сложение
                    Matrix b = inputSecondMatrix(); 
                    lastResult = MatrixOperations.add(lastResult, b); 
                    System.out.println("\nРезультат сложения:"); 
                    System.out.println(lastResult); 
                    break; 
                }
                case 2: { // умножение
                    Matrix b = inputSecondMatrix(); 
                    lastResult = MatrixOperations.multiply(lastResult, b); 
                    System.out.println("\nРезультат умножения:"); 
                    System.out.println(lastResult); 
                    break;
                }
                case 3: { // деление
                    Matrix b = inputSecondMatrix(); 
                    lastResult = MatrixOperations.divide(lastResult, b); 
                    System.out.println("\nРезультат деления:"); 
                    System.out.println(lastResult); 
                    break;
                }
                case 4: { // транспонирование
                    lastResult = MatrixOperations.transpose(lastResult); 
                    System.out.println("\nРезультат транспонирования:"); 
                    System.out.println(lastResult); 
                    break;
                }
                case 5: { // определитель
                    Complex det = MatrixOperations.determinant(lastResult); 
                    System.out.println("\nОпределитель: " + det); 
                    break; 
                }
                default: // Неизвестный пункт
                    System.out.println("Неверная операция."); 
            }
        } catch (IllegalArgumentException | ArithmeticException e) { 
            System.out.println("Ошибка: " + e.getMessage()); 
        }
    }

    /**спрашивает у пользователя вторую матрицу (для операций 1–3)*/
    private Matrix inputSecondMatrix() { // Возвращает готовую матрицу
        System.out.println("\nВвод второй матрицы:"); 
        int r = readInt("Число строк: ");   
        int c = readInt("Число столбцов: "); 
        Matrix m = new Matrix(r, c); 
        for (int i = 0; i < r; i++) { 
            for (int j = 0; j < c; j++) {
                System.out.printf("Элемент [%d][%d]:%n", i, j);
                double re = readDouble("  действительная часть: "); 
                double im = readDouble("  мнимая часть: ");         
                m.set(i, j, new Complex(re, im)); 
            }
        }
        return m; 
    }

    /**читаем целое число с защитой от некорректного ввода*/
    private int readInt(String prompt) { 
        while (true) { 
            System.out.print(prompt); 
            try { 
                return Integer.parseInt(scanner.nextLine().trim()); // Читаем строку, обрезаем пробелы, превращаем в int
            } catch (NumberFormatException e) { 
                System.out.println("Ожидалось целое число."); 
            }
        }
    }

    /**читает вещественное число с защитой от некорректного ввода, принимает и точку, и запятую как разделитель*/
    private double readDouble(String prompt) { 
        while (true) { 
            System.out.print(prompt); 
            try { 
                return Double.parseDouble(scanner.nextLine().trim().replace(',', '.')); // читаем, заменяем запятую на точку
            } catch (NumberFormatException e) { 
                System.out.println("Ожидалось число."); 
            }
        }
    }
}
