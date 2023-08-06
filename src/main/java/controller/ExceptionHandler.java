package controller;


// класс - обработчик исключительных ситуаций
public class ExceptionHandler extends Exception{

    // метод выводит краткое описание проблемы
    public static void log(Exception e) {
        System.out.println(e.toString());
    }
}
