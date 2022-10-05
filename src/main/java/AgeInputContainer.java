import java.util.ArrayList;

public class AgeInputContainer {
    private ArrayList<String> expressions;
    private ArrayList<String> descriptions;

    public AgeInputContainer() {
        expressions = new ArrayList<>();
        descriptions = new ArrayList<>();
    }

    public void addExpressionWithDescription(String exp, String desc) {
        expressions.add(exp);
        descriptions.add(desc);
    }

    public int getSize() {
        return expressions.size();
    }

    public String getExpression(int index) {
        if (index < getSize()) {
            return expressions.get(index);
        }
        else {
            throw new RuntimeException("AgeInputContainer -> index Out Of Bounds");
        }
    }

    public String getDescription(int index) {
        if (index < getSize()) {
            return descriptions.get(index);
        }
        else {
            throw new RuntimeException("AgeInputContainer -> index Out Of Bounds");
        }
    }
}
