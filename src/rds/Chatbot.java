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
        coursePrerequisites.put("ECO101", "NONE");
        coursePrerequisites.put("ECO104", "ECO101");
        coursePrerequisites.put("MIS107", "NONE");
        coursePrerequisites.put("BUS251", "ENG105");
        coursePrerequisites.put("BUS172", "NONE");
        coursePrerequisites.put("BUS173", "BUS172");
        coursePrerequisites.put("BUS135", "BUS112");
        coursePrerequisites.put("ACT201", "NONE");
        coursePrerequisites.put("ACT202", "ACT201");
        coursePrerequisites.put("FIN254", "ACT201 & BUS172");
        coursePrerequisites.put("LAW200", "NONE");
        coursePrerequisites.put("INB372", "MKT202");
        coursePrerequisites.put("MKT202", "NONE");
        coursePrerequisites.put("MIS207", "MIS107");
        coursePrerequisites.put("MGT212", "NONE");
        coursePrerequisites.put("MGT351", "MGT212");
        coursePrerequisites.put("MGT314", "BUS172 & MKT202");
        coursePrerequisites.put("MGT368", "MGT212");
        coursePrerequisites.put("MGT489", "COMPLETE 100 CREDITS");
        coursePrerequisites.put("ENG103", "ENG102");
        coursePrerequisites.put("ENG105", "ENG103");
        coursePrerequisites.put("BEN205", "NONE");
        coursePrerequisites.put("PHI401", "NONE");
        coursePrerequisites.put("HIS103", "NONE");
        coursePrerequisites.put("HIS101", "NONE");
        coursePrerequisites.put("HIS102", "NONE");
        coursePrerequisites.put("HIS202", "NONE");
        coursePrerequisites.put("HIS205", "NONE");
        coursePrerequisites.put("POL101", "NONE");
        coursePrerequisites.put("POL202", "NONE");
        coursePrerequisites.put("POL104", "NONE");
        coursePrerequisites.put("PAD201", "NONE");
        coursePrerequisites.put("SOC101", "NONE");
        coursePrerequisites.put("GEO205", "NONE");
        coursePrerequisites.put("ANT101", "NONE");
        coursePrerequisites.put("BIO103", "NONE");
        coursePrerequisites.put("BIO103L", "NONE");
        coursePrerequisites.put("ENV107", "NONE");
        coursePrerequisites.put("ENV107L", "NONE");
        coursePrerequisites.put("PBH101", "NONE");
        coursePrerequisites.put("PBH101L", "NONE");
        coursePrerequisites.put("PSY101", "NONE");
        coursePrerequisites.put("PSY101L", "NONE");
        coursePrerequisites.put("PHY107", "NONE");
        coursePrerequisites.put("PHY107L", "NONE");
        coursePrerequisites.put("CHE101", "NONE");
        coursePrerequisites.put("CHE101L", "NONE");
        coursePrerequisites.put("MKT337", "MKT202");
        coursePrerequisites.put("MKT344", "MKT202");
        coursePrerequisites.put("MKT460", "MKT202");
        coursePrerequisites.put("MKT470", "BUS173/MKT202");
        coursePrerequisites.put("MKT412", "MKT202");
        coursePrerequisites.put("MKT465", "MKT202");
        coursePrerequisites.put("MKT382", "MKT202");
        coursePrerequisites.put("MKT417", "NONE");
        coursePrerequisites.put("MKT330", "NONE");
        coursePrerequisites.put("MKT450", "MKT202");
        coursePrerequisites.put("MKT355", "MKT202");
        coursePrerequisites.put("MKT445", "MKT202");
        coursePrerequisites.put("MKT475", "MKT202/MKT470");
        coursePrerequisites.put("INB400", "FIN444-BEFORE 143 BATCH");
        coursePrerequisites.put("INB490", "NONE");
        coursePrerequisites.put("INB480", "INB372");
        coursePrerequisites.put("INB410", "MKT202/INB372");
        coursePrerequisites.put("INB350", "MKT202/INB372");
        coursePrerequisites.put("INB355", "MKT202/INB372");
        coursePrerequisites.put("INB415", "MKT202/INB372");
        coursePrerequisites.put("INB450", "MKT202/INB372");
        coursePrerequisites.put("INB495", "INB372");
        coursePrerequisites.put("ACT310", "FIN254");
        coursePrerequisites.put("ACT320", "ACT310");
        coursePrerequisites.put("ACT360", "ACT202 & FIN254");
        coursePrerequisites.put("ACT370", "ACT201 & ACT202");
        coursePrerequisites.put("ACT380", "ACT201 & ACT202");
        coursePrerequisites.put("ACT460", "ACT320");
        coursePrerequisites.put("ACT430", "ACT202 & FIN254");
        coursePrerequisites.put("ACT410", "ACT201 & ACT202");
        coursePrerequisites.put("FIN433", "FIN254");
        coursePrerequisites.put("FIN435", "FIN433");
        coursePrerequisites.put("FIN440", "FIN254");
        coursePrerequisites.put("FIN444", "INB372 & FIN254");
        coursePrerequisites.put("FIN410", "FIN254 & ACT202");
        coursePrerequisites.put("FIN455", "FIN440");
        coursePrerequisites.put("FIN464", "FIN254");
        coursePrerequisites.put("FIN470", "FIN435 & FIN440");
        coursePrerequisites.put("FIN480", "FIN435 & FIN440");
        coursePrerequisites.put("MIS210", "MIS207");
        coursePrerequisites.put("MIS310", "MIS207");
        coursePrerequisites.put("MIS320", "MIS207");
        coursePrerequisites.put("MIS470", "MIS310");
        coursePrerequisites.put("MIS410", "MIS207 & BUS173");
        coursePrerequisites.put("MIS450", "MIS207 & MIS310");
        coursePrerequisites.put("MGT490", "MGT314");
        coursePrerequisites.put("MIS499", "NONE");
        coursePrerequisites.put("MGT321", "MGT212");
        coursePrerequisites.put("MGT330", "MGT321");
        coursePrerequisites.put("MGT370", "MGT321");
        coursePrerequisites.put("MGT410", "MGT321");
        coursePrerequisites.put("MGT350", "MGT321");
        coursePrerequisites.put("HRM340", "MGT351");
        coursePrerequisites.put("HRM360", "MGT351");
        coursePrerequisites.put("HRM380", "MGT351");
        coursePrerequisites.put("HRM450", "MGT351");
        coursePrerequisites.put("HRM370", "MGT351");
        coursePrerequisites.put("HRM410", "MGT351");
        coursePrerequisites.put("HRM499", "MGT351");
        coursePrerequisites.put("HRM470", "MGT351");
        coursePrerequisites.put("SCM310", "MKT202 & MGT212");
        coursePrerequisites.put("SCM320", "NONE");
        coursePrerequisites.put("SCM450", "MGT314");
        coursePrerequisites.put("SCM360", "MKT202 & MGT212");
        coursePrerequisites.put("SCM460", "NONE");
        coursePrerequisites.put("SCM390", "NONE");
        coursePrerequisites.put("MGT470", "NONE");
        coursePrerequisites.put("ECO201", "ECO101 & BUS135");
        coursePrerequisites.put("ECO204", "ECO101 & ECO104");
        coursePrerequisites.put("ECO328", "ECO101 & ECO104");
        coursePrerequisites.put("ECO348", "ECO101 & ECO104");
        coursePrerequisites.put("ECO372", "BUS173");
        coursePrerequisites.put("ECO472", "ECO372");


        
      
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

