
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

public class KeyBinder {

    private HashMap<Integer, Action> bindingMap;

    public KeyBinder() {
        bindingMap = new HashMap<Integer, Action>();
        bindingMap.put(KeyEvent.VK_LEFT, Action.MOVE_LEFT);
        bindingMap.put(KeyEvent.VK_RIGHT, Action.MOVE_RIGHT);
        bindingMap.put(KeyEvent.VK_C, Action.HOLD);
        bindingMap.put(KeyEvent.VK_SPACE, Action.HARD_DROP);
        bindingMap.put(KeyEvent.VK_DOWN, Action.SOFT_DROP);
        bindingMap.put(KeyEvent.VK_UP, Action.ROTATE_CLOCKWISE);
        bindingMap.put(KeyEvent.VK_SHIFT, Action.HOLD);
        bindingMap.put(KeyEvent.VK_P, Action.TOGGLE_PAUSE);
        bindingMap.put(KeyEvent.VK_X, Action.ROTATE_COUNTER_CLOCKWISE);
        bindingMap.put(KeyEvent.VK_CONTROL, Action.ROTATE_COUNTER_CLOCKWISE);
    }

    public void setKeyBinding(int keyCode, Action action) {
        bindingMap.put(keyCode, action);
    }

    public void removeKeyBinding(int keyCode) {
        bindingMap.remove(keyCode);
    }

    public boolean hasBinding(int keyCode) {
        return bindingMap.containsKey(keyCode);
    }

    public Set<Integer> getKeys() {
        return bindingMap.keySet();
    }

    public Action actionForKey(int keyCode) {
        return bindingMap.get(keyCode);
    }

    public int keyForAction(Action action) {
        for (Entry<Integer,Action> e : bindingMap.entrySet()) {
            if (e.getValue() == action) {
                return e.getKey();
            }
        }
        System.out.println("welp");
        return 0;
    }
}
