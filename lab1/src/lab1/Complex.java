package lab1;

public class Complex {        //данный классс представляет комплексное число(вид a + bi)

    private final double re; // действительная часть числа
    private final double im; // мнимая часть числа

    public Complex(double re, double im) {  //позволяет принимать значения a и b от пользователя
        this.re = re;
        this.im = im;
    }

    public Complex(double re) {  //констурктор, для некомплекснычх чисел по умлочанию имеет im = 0
        this(re, 0);
    }

    public double getRe() { return re; } //ивлекаем значение для действительной 
    public double getIm() { return im; } //и мнимой частей

    /**реализуем сложение (a+bi) + (c+di) = (a+c) + (b+d)i*/
    public Complex add(Complex other) {   //other - второе слагаемое
        return new Complex(this.re + other.re, this.im + other.im);  // возвращаем новый объект
    }

    /**реализуем вычитание (a+bi) - (c+di) = (a-c) + (b-d)i*/
    public Complex subtract(Complex other) {  // other — вычитаемое
        return new Complex(this.re - other.re, this.im - other.im); 
    }

    /** умножение: (a+bi)(c+di) = (ac-bd) + (ad+bc)i, учитываем i^2=-1*/
    public Complex multiply(Complex other) {  
        double newRe = this.re * other.re - this.im * other.im; // действительная часть: ac - bd
        double newIm = this.re * other.im + this.im * other.re; // мнимая часть: ad + bc
        return new Complex(newRe, newIm);
    }

    /** деление: (a+bi)/(c+di) = ((ac+bd) + (bc-ad)i) / (c^2 + d^2)
     @throws ArithmeticException если делитель равен нулю, то ловим исключение
     */
    public Complex divide(Complex other) {  // other — делитель
        double denominator = other.re * other.re + other.im * other.im;  // c^2 + d^2 (квадрат модуля делителя)
        if (denominator == 0) {  // если делитель — комплексный ноль (0 + 0i)
            throw new ArithmeticException("Деление на ноль (комплексный ноль).");     // прерываем выполнение 
        }
        double newRe = (this.re * other.re + this.im * other.im) / denominator;  // действительная часть результата
        double newIm = (this.im * other.re - this.re * other.im) / denominator;  // мнимая часть
        return new Complex(newRe, newIm);  // Возвращаем новый объект
    }

    /** модуль комплексного числа: |a + bi| = sqrt(a^2 + b^2) */
    public double abs() {   // возвращает вещественное число
        return Math.sqrt(re * re + im * im);   // квадратный корень из суммы квадратов
    }
 

    // делаем красивый вывод: 5, 2i, 3 - 4i
    @Override   // Переопределяем метод toString из класса Object
    public String toString() {    // Возвращает строковое представление
        if (im == 0) return format(re);   // если мнимой части нет — печатаем только действительную
        if (re == 0) return format(im) + "i";   // если действительной нет — только мнимую с "i"
        if (im < 0) return format(re) + " - " + format(-im) + "i";  // отрицательная мнимая — ставим знак "минус"
        return format(re) + " + " + format(im) + "i";   // обычный случай: a + bi
    }


    //форматируем число: целое — без точки, дробное — с тремя знаками после точки
    private String format(double value) { // приватный, используется только внутри класса
        if (value == Math.rint(value)) { //  округляеv к ближайшему целому
            return String.valueOf((long) value); 
        }
        return String.format("%.3f", value); 
    }

    /**сравниваем комплексные числа с учётом погрешности */
    @Override // переопределяем метод equals из Object
    public boolean equals(Object obj) { 
        if (this == obj) return true; 
        if (!(obj instanceof Complex)) return false; 
        Complex c = (Complex) obj; 
        return Math.abs(re - c.re) < 1e-9 && Math.abs(im - c.im) < 1e-9; // Сравниваем с допуском 10^-9
    }

    @Override 
    public int hashCode() { 
        return Double.hashCode(re) * 31 + Double.hashCode(im); // стандартный приём «смешивания» хэшей
    }
}
