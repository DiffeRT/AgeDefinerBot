import java.util.ArrayList;

public class AgeOutputContainer {
    private ArrayList<String> calcResults;
    private ArrayList<String> descriptions;

    public AgeOutputContainer() {
        calcResults = new ArrayList<>();
        descriptions = new ArrayList<>();
    }

    public void addResultWithDescription(String res, String desc) {
        calcResults.add(res);
        descriptions.add(desc);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < calcResults.size(); i++) {
            if (descriptions.get(i).isEmpty()) {
                result.append(calcResults.get(i)).append("\n");
            }
            else {
                result.append(descriptions.get(i)).append(":\n")
                        .append("    ").append(calcResults.get(i)).append("\n");
            }
        }
        return result.toString();
    }
}
