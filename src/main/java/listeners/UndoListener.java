package listeners;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

// класс будет следить правками, которые можно отменить или вернуть
public class UndoListener implements UndoableEditListener {

    private UndoManager undoManager;

    public UndoListener(UndoManager undoManager) {
        this.undoManager = undoManager;
    }


    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        // получаем правку и добавляем её в undoManager
        undoManager.addEdit(e.getEdit());
    }
}
