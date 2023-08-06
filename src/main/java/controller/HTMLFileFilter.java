package controller;

import javax.swing.filechooser.FileFilter;
import java.io.File;

// класс с фильтрами используется для открытия или сохранения файла
public class HTMLFileFilter extends FileFilter {

    // метод для отображения файлов в окне только с расширением html и htm
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        if (f.getName().toLowerCase().endsWith(".html") || f.getName().toLowerCase().endsWith(".htm")) {
            return true;
        }
        return false;
    }

    // метод для отображения в окне выбора файла в описании доступных
    // типов файлов текста "HTML и HTM файлы"
    @Override
    public String getDescription() {
        return "HTML и HTM файлы";
    }
}
