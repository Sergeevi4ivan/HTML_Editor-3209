package controller;

import view.View;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {

    private View view;
    private HTMLDocument document;

    // Файл, который сейчас открыт в нашем редакторе (текущий файл)
    private File currentFile;

    public Controller(View view) {
        this.view = view;
    }

    public static void main(String[] args) {
        View view1 = new View();
        Controller controller = new Controller(view1);

        view1.setController(controller);
        view1.init();

        controller.init();

    }

    // метод создаёт новый документ
    public void createNewDocument() {
        // выбираем HTML вкладку у представления
        view.selectHtmlTab();

        // сбрасываем документ
        resetDocument();

        // устанавливаем новый заголовок окна
        view.setTitle("HTML редактор");

        // обнуляем переменную currentFile
        currentFile = null;
    }

    // метод для открытия файла
    public void openDocument() {
        // переключаем представление на html вкладку
        view.selectHtmlTab();

        // создаём новый объект для выбора файла JFileChooser
        // и устанавливаем ему в качестве фильтра объект HTMLFileFilter
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new  HTMLFileFilter());

        // показываем диалоговое окно "Open File" для выбора файла
        // сохраняем результат в переменную rezult
        int rezult = jFileChooser.showOpenDialog(view);

        // если пользователь подтвердит выбор файла
        if (rezult == JFileChooser.APPROVE_OPTION) {
            // устанавливаем новое значение currentFile
            currentFile = jFileChooser.getSelectedFile();

            // Сбрасываем документ
            resetDocument();

            // устанавливаем имя файла в качестве заголовка окна представления
            view.setTitle(currentFile.getName());

            // Вычитываем данные из currentFile с помощью FileReader
            // в документ document
            try {
                FileReader fileReader = new FileReader(currentFile);
                HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
                htmlEditorKit.read(fileReader, document, 0);
            } catch (Exception e) {
                // исключения логируем
                ExceptionHandler.log(e);
            }

            // сбрасываем правки
            view.resetUndo();
        }
    }

    // метод сохраняет файл
    public void saveDocument() {
        // переключаем представление на html вкладку
        view.selectHtmlTab();

        if (currentFile == null) {
            saveDocumentAs();
        } else {
            // переписываем содержимое документа document в объект FileWriter
            try {
                FileWriter fileWriter = new FileWriter(currentFile);
                HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
                htmlEditorKit.write(fileWriter, document, 0, document.getLength());
            } catch (Exception e) {
                // логируем исключения
                ExceptionHandler.log(e);
            }

        }

    }

    // метод сохраняет файл под новым именем
    public void saveDocumentAs() {
        // переключаем представление на html вкладку
        view.selectHtmlTab();

        // создаём новый объект для выбора файла JFileChooser
        // и устанавливаем ему в качестве фильтра объект HTMLFileFilter
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new  HTMLFileFilter());

        // показываем диалоговое окно "Save File" для выбора файла
        // сохраняем результат в переменную rezult
        int rezult = jFileChooser.showSaveDialog(view);

        // если пользователь подтвердит выбор файла
        if (rezult == JFileChooser.APPROVE_OPTION) {
            // сохраняем выбранный файл в поле currentFile
            currentFile = jFileChooser.getSelectedFile();

            // устанавливаем имя файла в качестве заголовка окна представления
            view.setTitle(currentFile.getName());

            // переписываем содержимое документа document в объект FileWriter
            try {
                FileWriter fileWriter = new FileWriter(currentFile);
                HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
                htmlEditorKit.write(fileWriter, document, 0, document.getLength());
            } catch (Exception e) {
                // логируем исключения
                ExceptionHandler.log(e);
            }
        }
    }

    // метод получает текст из документа со всеми html тегами
    public String getPlainText() {
        // через writer переписываем содержимое документа document в созданный объект
        StringWriter stringWriter = new StringWriter();
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        try {
            htmlEditorKit.write(stringWriter, document, 0, document.getLength());
        } catch (Exception e) {
            // логируем исключения
            ExceptionHandler.log(e);
        }
        return stringWriter.toString();
    }

    // метод записывает переданный текст с html тегами в документ "document"
    public void setPlainText(String text) {
        // сбрасываем документ
        resetDocument();

        // через reader вычитываем строку text в document
        StringReader stringReader = new StringReader(text);
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        try {
            htmlEditorKit.read(stringReader, document, 0);
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    // метод сбрасывает текущий документ
    public void resetDocument() {
        if (document != null) {
            // удаляем у текущего документа слушателя правок (которые можно отменить/вернуть)
            document.removeUndoableEditListener(view.getUndoListener());
        }

        // создаём новый документ по умолчанию
        document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();

        // добавляем новому документу слушателя правок
        document.addUndoableEditListener(view.getUndoListener());

        // обновляем представление
        view.update();
    }

    public void exit() {
        System.exit(0);
    }

    // метод отвечает за инициализацию контроллера
    public void init() {
        createNewDocument();
    }
    // getters and setters

    // getter для модели (в нашем случае это document)
    public HTMLDocument getDocument() {
        return document;
    }


}
