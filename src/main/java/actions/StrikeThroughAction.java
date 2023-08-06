package actions;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import java.awt.event.ActionEvent;

// класс отвечает за "Зачёркивание"
public class StrikeThroughAction extends StyledEditorKit.StyledTextAction {

    public StrikeThroughAction() {
        super(StyleConstants.StrikeThrough.toString());
    }

    // метод реализует "зачёркивание"
    public void actionPerformed(ActionEvent actionEvent) {
        // получаем редактор, на котором произошло событие actionEvent
        JEditorPane editor = getEditor(actionEvent);
        if (editor != null) {
            // получаем текущие аттрибуты ввода редактора,
            // которые отображаются для текущего курсора или выделенного текста
            MutableAttributeSet mutableAttributeSet = getStyledEditorKit(editor).getInputAttributes();

            // создаём объект, который представляет новые аттрибуты, которые будут установлены для текста
            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
            StyleConstants.setStrikeThrough(simpleAttributeSet, !StyleConstants.isStrikeThrough(mutableAttributeSet));

            // устанавливаем новые атрибуты для текущего выделенного текста в редакторе
            // false - чтобы аттрибуты применялись не ко всему тексту
            setCharacterAttributes(editor, simpleAttributeSet, false);
        }
    }
}
