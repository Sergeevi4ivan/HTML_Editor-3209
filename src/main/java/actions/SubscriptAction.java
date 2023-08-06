package actions;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import java.awt.event.ActionEvent;

// класс отвечает за "Подстрочный знак"
public class SubscriptAction extends StyledEditorKit.StyledTextAction {



    public SubscriptAction() {
        super(StyleConstants.Subscript.toString());
    }

    // метод реализует "подстрочный знак"
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // получаем редактор, на котором произошло событие actionEvent
        JEditorPane jEditorPane = getEditor(actionEvent);
        if (jEditorPane != null) {
            // получаем текущие аттрибуты ввода редактора,
            // которые отображаются для текущего курсора или выделенного текста
            MutableAttributeSet mutableAttributeSet = getStyledEditorKit(jEditorPane).getInputAttributes();

            // создаём объект, который представляет новые аттрибуты, которые будут установлены для текста
            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
            StyleConstants.setSubscript(simpleAttributeSet, !StyleConstants.isSubscript(mutableAttributeSet));

            // устанавливаем новые атрибуты для текущего выделенного текста в редакторе
            // false - чтобы аттрибуты применялись не ко всему тексту
            setCharacterAttributes(jEditorPane, simpleAttributeSet, false);
        }
    }
}
