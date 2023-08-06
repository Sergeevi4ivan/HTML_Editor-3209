package view;

import listeners.FrameListener;
import listeners.TabbedPaneChangeListener;
import listeners.UndoListener;
import controller.Controller;
import controller.ExceptionHandler;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {

    private Controller controller;
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);

    // панель с двумя вкладками
    private JTabbedPane tabbedPane = new JTabbedPane();

    // компонент для визуального редактирования html
    private JTextPane htmlTextPane = new JTextPane();

    // компонент для редактирования html в виде текста
    // он будет отображать код html (теги и их содержимое)
    private JEditorPane plainTextPane = new JEditorPane();

    public View() {
        try {
            // устанавливаем внешний вид и поведение (look and feel) нашего приложения такими же,
            // как это определено в системе
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    // метод получает документ у контроллера и устанавливает его в панель редактирования htmlTextPane
    public void update() {
        htmlTextPane.setDocument(controller.getDocument());
    }

    // метод показывает диалоговое окно с информацией о программе
    public void showAbout() {
        StringBuilder message = new StringBuilder("Это HTML редактор с графическим интерфейсом.\n" +
                "В качестве библиотеки для создания графического интерфейса использовался Swing.\n" +
                "А в качестве архитектурного каркаса приложения использована MVC модель.");
        JOptionPane.showMessageDialog(null, message, "О приложении", JOptionPane.INFORMATION_MESSAGE);
    }

    public void selectHtmlTab() {
        // переключаемся на вкладку html
        tabbedPane.setSelectedIndex(0);

        // сбрасываем все правки
        resetUndo();
    }

    // метод возвращает true если выбрана вкладка отображающая html в панели вкладок
    public boolean isHtmlTabSelected() {
        return tabbedPane.getSelectedIndex() == 0;
    }
    public void resetUndo() {
        // сбрасывает все правки в менеджере undoManager
        undoManager.discardAllEdits();
    }

    public void undo() {
        try {
            // отменяет последнее действие
            undoManager.undo();
        } catch (CannotUndoException e) {
            ExceptionHandler.log(e);
        }
    }
    public void redo() {
        try {
            // возвращает ранее отменённое действие
            undoManager.redo();
        } catch (CannotRedoException e) {
            ExceptionHandler.log(e);
        }
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }
    public boolean canRedo() {
        return undoManager.canRedo();
    }

    // метод будет вызываться при выборе пунктов меню
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();

        switch (command) {
            case "Новый": controller.createNewDocument();
                break;
            case "Открыть": controller.openDocument();
                break;
            case "Сохранить": controller.saveDocument();
                break;
            case "Сохранить как...": controller.saveDocumentAs();
                break;
            case "Выход": controller.exit();
                break;
            case "О программе": showAbout();
                break;

        }
        
    }

    // метод отвечает за инициализацию меню редактора
    public void initMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();

        // Инициализация меню "файл"
        MenuHelper.initFileMenu(this, jMenuBar);

        // Инициализация меню "редактировать"
        MenuHelper.initEditMenu(this, jMenuBar);

        // Инициализация меню "Стиль"
        MenuHelper.initStyleMenu(this, jMenuBar);

        // Инициализация меню "Выравнивание"
        MenuHelper.initAlignMenu(this, jMenuBar);

        // Инициализация меню "Цвет"
        MenuHelper.initColorMenu(this, jMenuBar);

        // Инициализация меню "Шрифт"
        MenuHelper.initFontMenu(this, jMenuBar);

        // Инициализация меню "Помощь"
        MenuHelper.initHelpMenu(this, jMenuBar);

        // добавлиет в верхнюю часть панели контента текущего фрейма панель меню
        getContentPane().add(jMenuBar, BorderLayout.NORTH);
    }

    // метод отвечает за инициализацию панелей редактора
    public void initEditor() {
        // тип контента "text/html" для компонента htmlTextPane
        htmlTextPane.setContentType("text/html");

        // добавляется вкладка в панель с именем "HTML"
        JScrollPane jScrollPane = new JScrollPane(htmlTextPane);
        tabbedPane.add("HTML", jScrollPane);

        // добавляется вкладка в панель с именем "Текст"
        JScrollPane jScrollPane1 = new JScrollPane(plainTextPane);
        tabbedPane.add("Текст", jScrollPane1);

        // устанавливается предпочтительный размер панели
        tabbedPane.setPreferredSize(new Dimension(800, 500));

        // создаём слушателя изменений для tabbedPane
        TabbedPaneChangeListener tabbedPaneChangeListener = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(tabbedPaneChangeListener);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    // метод отвечает за инициализацию графического интерфейса
    public void initGui() {
        initMenuBar();
        initEditor();
        pack();
    }
    public void exit() {
        controller.exit();
    }

    // метод отвечает за инициализацию представления (view)
    public void init() {
        initGui();
        FrameListener frameListener = new FrameListener(this);
        addWindowListener(frameListener);
        this.setVisible(true);
    }

    // метод вызывается, когда произошла смена выбранной вкладки
    public void selectedTabChanged() {
        // если выбрана вкладка html, то получаем его из plainTextPane и устанавливаем в контроллер,
        // если вкладка с html текстом, то получаем текст у контроллера и устанавливаем его в панель plainTextPane
        if (tabbedPane.getSelectedIndex() == 0) {
            controller.setPlainText(plainTextPane.getText());
        } else if (tabbedPane.getSelectedIndex() == 1) {
            plainTextPane.setText(controller.getPlainText());
        }

        // сбрасываем правки
        this.resetUndo();
    }

    //// getters and setters


    public UndoListener getUndoListener() {
        return undoListener;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

}
