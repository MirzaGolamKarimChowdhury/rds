package rds;

import java.util.*;
import javax.swing.JOptionPane;

public class Advising {
    private Student student;
    private List<Course> preAdvisedCourses;
    private Map<String, List<Course>> studentAdvisedCourses;
    private Map<String, List<String>> studentPreAdvisedCourseCodes;

    public Advising(Student student, List<Course> preAdvisedCourses, Map<String, List<Course>> studentAdvisedCourses, Map<String, List<String>> studentPreAdvisedCourseCodes) {
        this.student = student;
        this.preAdvisedCourses = preAdvisedCourses;
        this.studentAdvisedCourses = studentAdvisedCourses;
        this.studentPreAdvisedCourseCodes = studentPreAdvisedCourseCodes;
    }

    public List<Course> getUniqueCourses() {
        Map<String, Course> uniqueCourses = new HashMap<>();
        for (Course course : preAdvisedCourses) {
            if (!uniqueCourses.containsKey(course.getCode())) {
                uniqueCourses.put(course.getCode(), course);
            }
        }
        return new ArrayList<>(uniqueCourses.values());
    }

    public boolean preAdviseCourses(List<String> selectedCourseCodes) {
        List<String> preAdvisedCourseCodes = studentPreAdvisedCourseCodes.getOrDefault(student.getId(), new ArrayList<>());
        if (!preAdvisedCourseCodes.isEmpty()) {
            int response = JOptionPane.showConfirmDialog(null, "You have already completed pre-advising. Clear previous selections?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (response != JOptionPane.YES_OPTION) {
                return false;
            }
            preAdvisedCourseCodes.clear();
            studentAdvisedCourses.put(student.getId(), new ArrayList<>());
        }

        int totalCredits = 0;
        for (String code : selectedCourseCodes) {
            Course course = preAdvisedCourses.stream()
                    .filter(c -> c.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
            if (course == null || preAdvisedCourseCodes.contains(code)) {
                continue;
            }
            if (totalCredits + course.getCredit() > 18) {
                JOptionPane.showMessageDialog(null, "Exceeds maximum credit limit of 18 for " + course.getCode());
                return false;
            }
            preAdvisedCourseCodes.add(code);
            totalCredits += course.getCredit();
        }

        if (!preAdvisedCourseCodes.isEmpty()) {
            studentPreAdvisedCourseCodes.put(student.getId(), preAdvisedCourseCodes);
            return true;
        }
        return false;
    }

    public boolean adviseSection(Course selectedSection) {
        List<String> preAdvisedCourseCodes = studentPreAdvisedCourseCodes.getOrDefault(student.getId(), new ArrayList<>());
        if (preAdvisedCourseCodes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You must complete pre-advising first.");
            return false;
        }

        List<Course> advisedCourses = studentAdvisedCourses.getOrDefault(student.getId(), new ArrayList<>());
        String selectedCourseCode = selectedSection.getCode();
        if (advisedCourses.stream().anyMatch(c -> c.getCode().equals(selectedCourseCode))) {
            JOptionPane.showMessageDialog(null, "A section for " + selectedCourseCode + " is already advised.");
            return false;
        }
        for (Course advised : advisedCourses) {
            if (advised.conflictsWith(selectedSection)) {
                JOptionPane.showMessageDialog(null, "Time conflict with " + advised.getCode() + " (" + advised.getTimeSlot() + " " + advised.getTime() + ").");
                return false;
            }
        }
        advisedCourses.add(selectedSection);
        preAdvisedCourseCodes.remove(selectedCourseCode);
        studentAdvisedCourses.put(student.getId(), advisedCourses);
        studentPreAdvisedCourseCodes.put(student.getId(), preAdvisedCourseCodes);
        return true;
    }

    public List<Course> getAvailableSections() {
        List<String> preAdvisedCourseCodes = studentPreAdvisedCourseCodes.getOrDefault(student.getId(), new ArrayList<>());
        List<Course> availableSections = new ArrayList<>();
        for (String courseCode : preAdvisedCourseCodes) {
            availableSections.addAll(preAdvisedCourses.stream()
                    .filter(c -> c.getCode().equals(courseCode))
                    .toList());
        }
        return availableSections;
    }

    public List<Course> getAdvisedCourses() {
        return studentAdvisedCourses.getOrDefault(student.getId(), new ArrayList<>());
    }

    public List<String> getPreAdvisedCourseCodes() {
        return studentPreAdvisedCourseCodes.getOrDefault(student.getId(), new ArrayList<>());
    }

    public String getAdvisingSlip() {
        List<Course> advisedCourses = studentAdvisedCourses.getOrDefault(student.getId(), new ArrayList<>());
        StringBuilder slip = new StringBuilder();
        slip.append("=== Advising Slip ===\n");
        slip.append("Student Name: ").append(student.getName()).append("\n");
        slip.append("Student ID: ").append(student.getId()).append("\n");
        slip.append("Advised Courses:\n");
        int tuitionTotal = 0;
        for (Course course : advisedCourses) {
            slip.append(course).append("\n");
            tuitionTotal += course.getCredit() * 6500;
        }
        int studentActivityFee = 3000;
        int computerLabFee = 2500;
        int libraryFee = 1500;
        int scienceLabFee = 2500;
        int studioLabFee = 0;
        int finalTotal = tuitionTotal + studentActivityFee + computerLabFee + libraryFee + scienceLabFee + studioLabFee;
        slip.append("\nTuition Total: ").append(tuitionTotal).append("\n");
        slip.append("Student Activity Fee: ").append(studentActivityFee).append("\n");
        slip.append("Computer Lab Fee: ").append(computerLabFee).append("\n");
        slip.append("Library Fee: ").append(libraryFee).append("\n");
        slip.append("Science Lab Fee: ").append(scienceLabFee).append("\n");
        slip.append("Studio Lab Fee: ").append(studioLabFee).append("\n");
        slip.append("Final Total: ").append(finalTotal).append("\n");
        return slip.toString();
    }

    public String getPreAdvisedCoursesString() {
        StringBuilder result = new StringBuilder();
        List<String> preAdvisedCourseCodes = studentPreAdvisedCourseCodes.getOrDefault(student.getId(), new ArrayList<>());
        if (preAdvisedCourseCodes.isEmpty()) {
            result.append("No pre-advised courses.\n");
        } else {
            result.append("Pre-Advised Courses:\n");
            for (String code : preAdvisedCourseCodes) {
                Course course = preAdvisedCourses.stream()
                        .filter(c -> c.getCode().equals(code))
                        .findFirst()
                        .orElse(null);
                if (course != null) {
                    result.append(course.toPreAdvisingString()).append(" (").append(course.getCredit()).append(" credits)\n");
                }
            }
        }
        List<Course> advisedCourses = studentAdvisedCourses.getOrDefault(student.getId(), new ArrayList<>());
        if (!advisedCourses.isEmpty()) {
            result.append("\nAdvised Courses:\n");
            for (Course course : advisedCourses) {
                result.append(course).append("\n");
            }
        }
        return result.toString();
    }
}