package Domain;

import java.util.Stack;

public class Historique {
    Stack<CNC> previousStates = new Stack<>();
    Stack<CNC> redoStates = new Stack<>();
    CNC currentState;

    public Historique(CNC currentState) {
        this.currentState = currentState;
    }

    // undo
    public CNC popLastState() {
        if (!hasUndoState()) { return null;}
        CNC cnc = previousStates.pop();
        redoStates.push(currentState);
        this.currentState = cnc;
        return new CNC(currentState);
    }

    public boolean hasUndoState() {
        System.out.println(previousStates.size());
        return !previousStates.isEmpty();
    }

    public boolean hasRedoState() {
        return !redoStates.isEmpty();
    }

    // redo
    public CNC popLastUndoState() {
        if (redoStates.isEmpty()) { return null;}
        CNC cnc = redoStates.pop();
        previousStates.push(currentState);
        this.currentState = cnc;
        return new CNC(currentState);
    }

    public void saveState(CNC state) {
        previousStates.push(currentState);
        this.currentState = state;
        redoStates.clear();
    }


}
