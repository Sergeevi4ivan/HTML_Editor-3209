package listeners;

import view.View;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;

public class TextEditMenuListener implements MenuListener {

    private View view;

    public TextEditMenuListener(View view) {
        this.view = view;
    }

    @Override
    public void menuSelected(MenuEvent menuEvent) {
        // получаем объект, над которым было совершено действие (объект с типом JMenu)
        JMenu jMenu = (JMenu) menuEvent.getSource();

        // получаем список компонентов и для каждого пункта вызываем setEnabled
        for (Component component:
                jMenu.getMenuComponents()) {
            component.setEnabled(view.isHtmlTabSelected());
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
