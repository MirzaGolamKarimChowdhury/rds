package rds;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Chatbot {
    private static final Map<Integer, String[]> gradeMap = new HashMap<>();
    
    static {
        // Initialize grading system: {minScore, letterGrade, gradePoint}
        gradeMap.put(93, new String[]{"A", "4.0"});
        gradeMap.put(90, new String[]{"A-", "3.7"});
        gradeMap.put(87, new String[]{"B+", "3.3"});
        gradeMap.put(83, new String[]{"B", "3.0"});
        gradeMap.put(80, new String[]{"B-", "2.7"});
        gradeMap.put(77, new String[]{"C+", "2.3"});
        gradeMap.put(73, new String[]{"C", "2.0"});
        gradeMap.put(70, new String[]{"C-", "1.7"});
        gradeMap.put(67, new String[]{"D+", "1.3"});
        gradeMap.put(60, new String[]{"D", "1.0"});
        gradeMap.put(0, new String[]{"F", "0.0"});
    }

    // Placeholder for course prerequisites (to be updated when provided)
    private static final Map<String, String> coursePrerequisites = new HashMap<>();
    
    static {
        // Mock prerequisites until actual data is provided
        coursePrerequisites.put("MIS107", "None");
        coursePrerequisites.put("MIS207", "MIS107");
        coursePrerequisites.put("ACT201", "None");
        coursePrerequisites.put("FIN254", "ACT201");
        coursePrerequisites.put("CSE101", "None");
        coursePrerequisites.put("MAT101", "None");
        coursePrerequisites.put("ENG102", "None");
        coursePrerequisites.put("PHY101", "MAT101");
    }

    public String calculateGrade(String scoreInput) {
        try {
            int score = Integer.parseInt(scoreInput.trim());
            if (score < 0 || score > 100) {
                return "Invalid score. Please enter a number between 0 and 100.";
            }
            for (int threshold : gradeMap.keySet().toArray(new Integer[0])) {
                if (score >= threshold) {
                    String[] gradeInfo = gradeMap.get(threshold);
                    return "Score: " + score + "\nLetter Grade: " + gradeInfo[0] + "\nGrade Point: " + gradeInfo[1];
                }
            }
        } catch (NumberFormatException e) {
            return "Invalid input. Please enter a numeric score.";
        }
        return "Error calculating grade.";
    }

    public String getPrerequisites(String courseCode) {
        courseCode = courseCode.trim().toUpperCase();
        String prereq = coursePrerequisites.getOrDefault(courseCode, "Prerequisite information not available.");
        return "Course: " + courseCode + "\nPrerequisites: " + prereq;
    }
}
