package rds;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class RDSMainGUI {
    private static List<Student> students = new ArrayList<>();
    private static List<Course> preAdvisedCourses = new ArrayList<>();
    private static Map<String, List<Course>> studentAdvisedCourses = new HashMap<>();
    private static Map<String, List<String>> studentPreAdvisedCourseCodes = new HashMap<>();
    private static final String STUDENT_FILE = "students.txt";
    private static final String ADVISED_COURSES_FILE = "advised_courses.txt";
    private static Student loggedInStudent = null;

    public static void main(String[] args) {
        loadStudents();
        loadAdvisedCourses();
        CourseManager courseManager = new CourseManager();
        courseManager.initializeCourses();
        preAdvisedCourses = courseManager.getCourses();
        SwingUtilities.invokeLater(RDSMainGUI::showLoginWindow);
    }

    private static void loadStudents() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 14) {
                    Student student = new Student(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6],
                            parts[7], parts[8], parts[9], parts[10], parts[11], parts[12], parts[13]);
                    if (parts.length > 14) student.setNidNumber(parts[14]);
                    if (parts.length > 15) student.setBirthRegNumber(parts[15]);
                    if (parts.length > 16) student.setMaritalStatus(parts[16]);
                    if (parts.length > 17) student.setBloodGroup(parts[17]);
                    if (parts.length > 18) student.setParentAddress(parts[18]);
                    students.add(student);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No existing student data found. Starting fresh.");
        }
    }

    private static void saveStudents() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STUDENT_FILE))) {
            for (Student student : students) {
                writer.write(student.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving student data.");
        }
    }

    private static void loadAdvisedCourses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADVISED_COURSES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 8) {
                    String studentId = parts[0];
                    Course course = new Course(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], Integer.parseInt(parts[7]));
                    studentAdvisedCourses.computeIfAbsent(studentId, k -> new ArrayList<>()).add(course);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No existing advised courses found. Starting fresh.");
        }
    }

    private static void saveAdvisedCourses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADVISED_COURSES_FILE))) {
            for (Map.Entry<String, List<Course>> entry : studentAdvisedCourses.entrySet()) {
                String studentId = entry.getKey();
                for (Course course : entry.getValue()) {
                    writer.write(studentId + "|" + course.toFileString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving advised courses.");
        }
    }

    private static boolean validateEmail(String email) {
        return email.matches("^[\\w-\\.\\_]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private static void showLoginWindow() {
        JFrame frame = new JFrame("RDS Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 2, 10, 10));
        JLabel idLabel = new JLabel("ID (7 digits):");
        JTextField idField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");
        JButton forgotPasswordButton = new JButton("Forgot Password");
        JButton exitButton = new JButton("Exit");
        frame.add(idLabel);
        frame.add(idField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(createAccountButton);
        frame.add(forgotPasswordButton);
        frame.add(exitButton);
        loginButton.addActionListener(e -> {
            String idInput = idField.getText();
            String password = new String(passwordField.getPassword());
            if (idInput.length() != 7 || !idInput.matches("\\d+")) {
                JOptionPane.showMessageDialog(frame, "ID must be exactly 7 digits.");
                return;
            }
            for (Student student : students) {
                if (student.getFirst7DigitsId().equals(idInput) && student.getPassword().equals(password)) {
                    loggedInStudent = student;
                    frame.dispose();
                    showHomeWindow();
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame, "Invalid ID or Password.");
        });
        createAccountButton.addActionListener(e -> {
            frame.dispose();
            showCreateAccountWindow();
        });
        forgotPasswordButton.addActionListener(e -> {
            frame.dispose();
            showForgotPasswordWindow();
        });
        exitButton.addActionListener(e -> System.exit(0));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showCreateAccountWindow() {
        JFrame frame = new JFrame("Create New Account");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout(10, 10));

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Generated ID
        Random random = new Random();
        String id = String.format("%010d", random.nextInt(1000000000) + 1000000000);
        JLabel idLabel = new JLabel("Generated ID: " + id);

        // Account Information Panel
        JPanel accountPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        accountPanel.setBorder(BorderFactory.createTitledBorder("Account Information"));
        JLabel passwordLabel = new JLabel("Password*:");
        JPasswordField passwordField = new JPasswordField();
        passwordField.setToolTipText("Enter your password");
        JLabel degreeLabel = new JLabel("Degree Name*:");
        JTextField degreeField = new JTextField();
        degreeField.setToolTipText("Enter your degree program (e.g., B.Sc. in Computer Science)");
        accountPanel.add(passwordLabel);
        accountPanel.add(passwordField);
        accountPanel.add(degreeLabel);
        accountPanel.add(degreeField);

        // Personal Information Panel
        JPanel personalPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        personalPanel.setBorder(BorderFactory.createTitledBorder("Personal Information"));
        JLabel nameLabel = new JLabel("Full Name*:");
        JTextField nameField = new JTextField();
        nameField.setToolTipText("Enter your full name");
        JLabel dobLabel = new JLabel("Date of Birth (YYYY-MM-DD)*:");
        JTextField dobField = new JTextField();
        dobField.setToolTipText("Enter your date of birth in YYYY-MM-DD format");
        JLabel sexLabel = new JLabel("Sex*:");
        JTextField sexField = new JTextField();
        sexField.setToolTipText("Enter your sex (e.g., Male, Female)");
        JLabel citizenshipLabel = new JLabel("Citizenship*:");
        JTextField citizenshipField = new JTextField();
        citizenshipField.setToolTipText("Enter your citizenship");
        personalPanel.add(nameLabel);
        personalPanel.add(nameField);
        personalPanel.add(dobLabel);
        personalPanel.add(dobField);
        personalPanel.add(sexLabel);
        personalPanel.add(sexField);
        personalPanel.add(citizenshipLabel);
        personalPanel.add(citizenshipField);

        // Contact Information Panel
        JPanel contactPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        contactPanel.setBorder(BorderFactory.createTitledBorder("Contact Information"));
        JLabel emailLabel = new JLabel("Email*:");
        JTextField emailField = new JTextField();
        emailField.setToolTipText("Enter your email address (e.g., user@university.com)");
        JLabel cellPhoneLabel = new JLabel("Cell Phone*:");
        JTextField cellPhoneField = new JTextField();
        cellPhoneField.setToolTipText("Enter your cell phone number");
        JLabel mailingAddressLabel = new JLabel("Mailing Address*:");
        JTextField mailingAddressField = new JTextField();
        mailingAddressField.setToolTipText("Enter your mailing address");
        contactPanel.add(emailLabel);
        contactPanel.add(emailField);
        contactPanel.add(cellPhoneLabel);
        contactPanel.add(cellPhoneField);
        contactPanel.add(mailingAddressLabel);
        contactPanel.add(mailingAddressField);

        // Parent/Guardian Information Panel
        JPanel parentPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        parentPanel.setBorder(BorderFactory.createTitledBorder("Parent/Guardian Information"));
        JLabel fatherNameLabel = new JLabel("Father's Name*:");
        JTextField fatherNameField = new JTextField();
        fatherNameField.setToolTipText("Enter your father's name");
        JLabel motherNameLabel = new JLabel("Mother's Name*:");
        JTextField motherNameField = new JTextField();
        motherNameField.setToolTipText("Enter your mother's name");
        JLabel guardianNameLabel = new JLabel("Guardian's Name*:");
        JTextField guardianNameField = new JTextField();
        guardianNameField.setToolTipText("Enter your guardian's name");
        JLabel phoneNumberLabel = new JLabel("Phone Number*:");
        JTextField phoneNumberField = new JTextField();
        phoneNumberField.setToolTipText("Enter parent or guardian phone number");
        parentPanel.add(fatherNameLabel);
        parentPanel.add(fatherNameField);
        parentPanel.add(motherNameLabel);
        parentPanel.add(motherNameField);
        parentPanel.add(guardianNameLabel);
        parentPanel.add(guardianNameField);
        parentPanel.add(phoneNumberLabel);
        parentPanel.add(phoneNumberField);

        // Optional Information Panel
        JPanel optionalPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        optionalPanel.setBorder(BorderFactory.createTitledBorder("Optional Information"));
        JLabel nidLabel = new JLabel("NID Number (Optional):");
        JTextField nidField = new JTextField();
        nidField.setToolTipText("Enter your National ID number (optional)");
        JLabel birthRegLabel = new JLabel("Birth Reg Number (Optional):");
        JTextField birthRegField = new JTextField();
        birthRegField.setToolTipText("Enter your birth registration number (optional)");
        JLabel maritalStatusLabel = new JLabel("Marital Status (Optional):");
        JTextField maritalStatusField = new JTextField();
        maritalStatusField.setToolTipText("Enter your marital status (e.g., Single, Married) (optional)");
        JLabel bloodGroupLabel = new JLabel("Blood Group (Optional):");
        JTextField bloodGroupField = new JTextField();
        bloodGroupField.setToolTipText("Enter your blood group (optional)");
        JLabel parentAddressLabel = new JLabel("Parent Address (Optional):");
        JTextField parentAddressField = new JTextField();
        parentAddressField.setToolTipText("Enter your parent address (optional)");
        optionalPanel.add(nidLabel);
        optionalPanel.add(nidField);
        optionalPanel.add(birthRegLabel);
        optionalPanel.add(birthRegField);
        optionalPanel.add(maritalStatusLabel);
        optionalPanel.add(maritalStatusField);
        optionalPanel.add(bloodGroupLabel);
        optionalPanel.add(bloodGroupField);
        optionalPanel.add(parentAddressLabel);
        optionalPanel.add(parentAddressField);

        // Note about required fields
        JLabel noteLabel = new JLabel("* indicates required fields", SwingConstants.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add components to main panel
        mainPanel.add(idLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(accountPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(personalPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(contactPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(parentPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(optionalPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(noteLabel);

        // Add main panel and button panel to frame
        frame.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            String email = emailField.getText();
            String degreeName = degreeField.getText();
            String cellPhone = cellPhoneField.getText();
            String dob = dobField.getText();
            String sex = sexField.getText();
            String citizenship = citizenshipField.getText();
            String fatherName = fatherNameField.getText();
            String motherName = motherNameField.getText();
            String guardianName = guardianNameField.getText();
            String mailingAddress = mailingAddressField.getText();
            String phoneNumber = phoneNumberField.getText();
            String nidNumber = nidField.getText();
            String birthRegNumber = birthRegField.getText();
            String maritalStatus = maritalStatusField.getText();
            String bloodGroup = bloodGroupField.getText();
            String parentAddress = parentAddressField.getText();
            if (password.isEmpty() || name.isEmpty() || email.isEmpty() || degreeName.isEmpty() ||
                cellPhone.isEmpty() || dob.isEmpty() || sex.isEmpty() || citizenship.isEmpty() ||
                fatherName.isEmpty() || motherName.isEmpty() || guardianName.isEmpty() ||
                mailingAddress.isEmpty() || phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All required fields (*) must be filled.");
                return;
            }
            if (!validateEmail(email)) {
                JOptionPane.showMessageDialog(frame, "Invalid email format. Please enter a valid email (e.g., user@university.com).");
                return;
            }
            Student student = new Student(id, password, name, email, degreeName, cellPhone, dob, sex, citizenship,
                    fatherName, motherName, guardianName, mailingAddress, phoneNumber);
            student.setNidNumber(nidNumber);
            student.setBirthRegNumber(birthRegNumber);
            student.setMaritalStatus(maritalStatus);
            student.setBloodGroup(bloodGroup);
            student.setParentAddress(parentAddress);
            students.add(student);
            saveStudents();
            JOptionPane.showMessageDialog(frame, "Account created successfully! Your ID is: " + id + " (Use first 7 digits for login: " + student.getFirst7DigitsId() + ")");
            frame.dispose();
            showLoginWindow();
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
            showLoginWindow();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showForgotPasswordWindow() {
        JFrame frame = new JFrame("Forgot Password");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2, 10, 10));
        JLabel idLabel = new JLabel("10-digit Student ID:");
        JTextField idField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel cellPhoneLabel = new JLabel("Cell Phone:");
        JTextField cellPhoneField = new JTextField();
        JLabel newPasswordLabel = new JLabel("New Password:");
        JPasswordField newPasswordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField();
        JButton resetButton = new JButton("Reset");
        JButton cancelButton = new JButton("Cancel");
        frame.add(idLabel);
        frame.add(idField);
        frame.add(emailLabel);
        frame.add(emailField);
        frame.add(cellPhoneLabel);
        frame.add(cellPhoneField);
        frame.add(newPasswordLabel);
        frame.add(newPasswordField);
        frame.add(confirmPasswordLabel);
        frame.add(confirmPasswordField);
        frame.add(resetButton);
        frame.add(cancelButton);
        resetButton.addActionListener(e -> {
            String idInput = idField.getText();
            String email = emailField.getText();
            String cellPhone = cellPhoneField.getText();
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            if (idInput.length() != 10 || !idInput.matches("\\d+")) {
                JOptionPane.showMessageDialog(frame, "ID must be exactly 10 digits.");
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match.");
                return;
            }
            Student targetStudent = null;
            for (Student student : students) {
                if (student.getId().equals(idInput) && student.getEmail().equals(email) && student.getCellPhone().equals(cellPhone)) {
                    targetStudent = student;
                    break;
                }
            }
            if (targetStudent == null) {
                JOptionPane.showMessageDialog(frame, "Invalid ID, Email, or Cell Phone.");
                return;
            }
            targetStudent.setPassword(newPassword);
            saveStudents();
            JOptionPane.showMessageDialog(frame, "Password reset successfully!");
            frame.dispose();
            showLoginWindow();
        });
        cancelButton.addActionListener(e -> {
            frame.dispose();
            showLoginWindow();
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showHomeWindow() {
        JFrame frame = new JFrame("RDS - Welcome, " + loggedInStudent.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.add(new JLabel("Welcome, " + loggedInStudent.getName()));
        topPanel.add(new JLabel("ID: " + loggedInStudent.getId()));
        topPanel.add(new JLabel("Degree: " + loggedInStudent.getDegreeName()));
        frame.add(topPanel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel(new GridLayout(5, 3, 10, 10));
        JButton profileButton = new JButton("Profile");
        JButton advisingButton = new JButton("Advising");
        JButton paymentsButton = new JButton("Payments");
        JButton courseDropButton = new JButton("Course Drop");
        JButton chatbotButton = new JButton("Chatbot");
        JButton logoutButton = new JButton("Logout");
        buttonPanel.add(profileButton);
        buttonPanel.add(advisingButton);
        buttonPanel.add(paymentsButton);
        buttonPanel.add(courseDropButton);
        buttonPanel.add(chatbotButton);
        buttonPanel.add(logoutButton);
        frame.add(buttonPanel, BorderLayout.CENTER);
        profileButton.addActionListener(e -> {
            frame.dispose();
            showProfileWindow();
        });
        advisingButton.addActionListener(e -> {
            frame.dispose();
            showAdvisingWindow();
        });
        paymentsButton.addActionListener(e -> {
            frame.dispose();
            showPaymentsWindow();
        });
        courseDropButton.addActionListener(e -> {
            frame.dispose();
            showCourseDropWindow();
        });
        chatbotButton.addActionListener(e -> {
            frame.dispose();
            showChatbotWindow();
        });
        logoutButton.addActionListener(e -> {
            loggedInStudent = null;
            frame.dispose();
            showLoginWindow();
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showProfileWindow() {
        JFrame frame = new JFrame("Profile");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.append("=== Student Information ===\n");
        infoArea.append("Full Name: " + loggedInStudent.getName() + "\n");
        infoArea.append("ID: " + loggedInStudent.getId() + "\n");
        infoArea.append("Enrolled In: " + loggedInStudent.getDegreeName() + "\n");
        infoArea.append("Entry Term: " + loggedInStudent.getEntryTerm() + "\n");
        infoArea.append("Academic Status: Active\n");
        infoArea.append("CGPA: " + loggedInStudent.getCgpa() + "\n");
        infoArea.append("Credit Passed: " + loggedInStudent.getCreditPassed() + "\n");
        infoArea.append("Probation: " + loggedInStudent.getProbation() + "\n");
        infoArea.append("Major 1: " + loggedInStudent.getMajor1() + "\n");
        infoArea.append("Major 2: " + loggedInStudent.getMajor2() + "\n");
        infoArea.append("Minor: " + loggedInStudent.getMinor() + "\n");
        infoArea.append("\nPersonal Information:\n");
        infoArea.append("Cell Phone: " + loggedInStudent.getCellPhone() + "\n");
        infoArea.append("Email: " + loggedInStudent.getEmail() + "\n");
        infoArea.append("NID: " + loggedInStudent.getNidNumber() + "\n");
        infoArea.append("Birth Reg No: " + loggedInStudent.getBirthRegNumber() + "\n");
        infoArea.append("DOB: " + loggedInStudent.getDob() + "\n");
        infoArea.append("Sex: " + loggedInStudent.getSex() + "\n");
        infoArea.append("Citizenship: " + loggedInStudent.getCitizenship() + "\n");
        infoArea.append("Blood Group: " + loggedInStudent.getBloodGroup() + "\n");
        infoArea.append("Marital Status: " + loggedInStudent.getMaritalStatus() + "\n");
        infoArea.append("Mailing Address: " + loggedInStudent.getMailingAddress() + "\n");
        infoArea.append("\nParent Information:\n");
        infoArea.append("Father's Name: " + loggedInStudent.getFatherName() + "\n");
        infoArea.append("Mother's Name: " + loggedInStudent.getMotherName() + "\n");
        infoArea.append("Guardian's Name: " + loggedInStudent.getGuardianName() + "\n");
        infoArea.append("Parent Address: " + loggedInStudent.getParentAddress() + "\n");
        infoArea.append("Phone: " + loggedInStudent.getPhoneNumber() + "\n");
        frame.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton editButton = new JButton("Edit Information");
        JButton changePasswordButton = new JButton("Change Password");
        JButton backButton = new JButton("Back");
        buttonPanel.add(editButton);
        buttonPanel.add(changePasswordButton);
        buttonPanel.add(backButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        editButton.addActionListener(e -> {
            frame.dispose();
            showEditProfileWindow();
        });
        changePasswordButton.addActionListener(e -> {
            frame.dispose();
            showChangePasswordWindow();
        });
        backButton.addActionListener(e -> {
            frame.dispose();
            showHomeWindow();
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showEditProfileWindow() {
        JFrame frame = new JFrame("Edit Profile");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout(10, 10));

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Contact Information Panel
        JPanel contactPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        contactPanel.setBorder(BorderFactory.createTitledBorder("Contact Information"));
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(loggedInStudent.getEmail());
        emailField.setToolTipText("Enter your email address (e.g., user@university.com)");
        JLabel cellPhoneLabel = new JLabel("Cell Phone:");
        JTextField cellPhoneField = new JTextField(loggedInStudent.getCellPhone());
        cellPhoneField.setToolTipText("Enter your cell phone number");
        JLabel mailingAddressLabel = new JLabel("Mailing Address:");
        JTextField mailingAddressField = new JTextField(loggedInStudent.getMailingAddress());
        mailingAddressField.setToolTipText("Enter your mailing address");
        contactPanel.add(emailLabel);
        contactPanel.add(emailField);
        contactPanel.add(cellPhoneLabel);
        contactPanel.add(cellPhoneField);
        contactPanel.add(mailingAddressLabel);
        contactPanel.add(mailingAddressField);

        // Personal Information Panel
        JPanel personalPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        personalPanel.setBorder(BorderFactory.createTitledBorder("Personal Information"));
        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameField = new JTextField(loggedInStudent.getName());
        nameField.setToolTipText("Enter your full name");
        personalPanel.add(nameLabel);
        personalPanel.add(nameField);

        // Optional Information Panel
        JPanel optionalPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        optionalPanel.setBorder(BorderFactory.createTitledBorder("Optional Information"));
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        JTextField phoneNumberField = new JTextField(loggedInStudent.getPhoneNumber());
        phoneNumberField.setToolTipText("Enter an additional phone number");
        JLabel nidLabel = new JLabel("NID Number:");
        JTextField nidField = new JTextField(loggedInStudent.getNidNumber());
        nidField.setToolTipText("Enter your National ID number (optional)");
        JLabel birthRegLabel = new JLabel("Birth Reg Number:");
        JTextField birthRegField = new JTextField(loggedInStudent.getBirthRegNumber());
        birthRegField.setToolTipText("Enter your birth registration number (optional)");
        JLabel maritalStatusLabel = new JLabel("Marital Status:");
        JTextField maritalStatusField = new JTextField(loggedInStudent.getMaritalStatus());
        maritalStatusField.setToolTipText("Enter your marital status (e.g., Single, Married) (optional)");
        JLabel bloodGroupLabel = new JLabel("Blood Group:");
        JTextField bloodGroupField = new JTextField(loggedInStudent.getBloodGroup());
        bloodGroupField.setToolTipText("Enter your blood group (e.g., A+, O-) (optional)");
        optionalPanel.add(phoneNumberLabel);
        optionalPanel.add(phoneNumberField);
        optionalPanel.add(nidLabel);
        optionalPanel.add(nidField);
        optionalPanel.add(birthRegLabel);
        optionalPanel.add(birthRegField);
        optionalPanel.add(maritalStatusLabel);
        optionalPanel.add(maritalStatusField);
        optionalPanel.add(bloodGroupLabel);
        optionalPanel.add(bloodGroupField);

        // Parent Address Panel
        JPanel parentAddressPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        parentAddressPanel.setBorder(BorderFactory.createTitledBorder("Parent Address"));
        JLabel parentAddressLabel = new JLabel("Parent Address:");
        JTextField parentAddressField = new JTextField(loggedInStudent.getParentAddress());
        parentAddressField.setToolTipText("Enter your parent or guardian's address (optional)");
        parentAddressPanel.add(parentAddressLabel);
        parentAddressPanel.add(parentAddressField);

        // Note about required fields
        JLabel noteLabel = new JLabel("All fields are optional; leave blank to keep current values", SwingConstants.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add components to main panel
        mainPanel.add(personalPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(contactPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(optionalPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(parentAddressPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(noteLabel);

        // Add main panel and button panel to frame
        frame.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            String email = emailField.getText();
            if (!email.isEmpty() && !validateEmail(email)) {
                JOptionPane.showMessageDialog(frame, "Invalid email format. Please enter a valid email (e.g., user@university.com).");
                return;
            }
            if (!nameField.getText().isEmpty()) loggedInStudent.setName(nameField.getText());
            if (!email.isEmpty()) loggedInStudent.setEmail(email);
            if (!cellPhoneField.getText().isEmpty()) loggedInStudent.setCellPhone(cellPhoneField.getText());
            if (!mailingAddressField.getText().isEmpty()) loggedInStudent.setMailingAddress(mailingAddressField.getText());
            if (!phoneNumberField.getText().isEmpty()) loggedInStudent.setPhoneNumber(phoneNumberField.getText());
            loggedInStudent.setNidNumber(nidField.getText());
            loggedInStudent.setBirthRegNumber(birthRegField.getText());
            loggedInStudent.setMaritalStatus(maritalStatusField.getText());
            loggedInStudent.setBloodGroup(bloodGroupField.getText());
            loggedInStudent.setParentAddress(parentAddressField.getText());
            saveStudents();
            JOptionPane.showMessageDialog(frame, "Information updated successfully!");
            frame.dispose();
            showProfileWindow();
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
            showProfileWindow();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showChangePasswordWindow() {
        JFrame frame = new JFrame("Change Password");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(4, 2, 10, 10));
        JLabel currentPasswordLabel = new JLabel("Current Password:");
        JPasswordField currentPasswordField = new JPasswordField();
        JLabel newPasswordLabel = new JLabel("New Password:");
        JPasswordField newPasswordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        frame.add(currentPasswordLabel);
        frame.add(currentPasswordField);
        frame.add(newPasswordLabel);
        frame.add(newPasswordField);
        frame.add(confirmPasswordLabel);
        frame.add(confirmPasswordField);
        frame.add(saveButton);
        frame.add(cancelButton);
        saveButton.addActionListener(e -> {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            if (!currentPassword.equals(loggedInStudent.getPassword())) {
                JOptionPane.showMessageDialog(frame, "Incorrect current password.");
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match.");
                return;
            }
            loggedInStudent.setPassword(newPassword);
            saveStudents();
            JOptionPane.showMessageDialog(frame, "Password changed successfully!");
            frame.dispose();
            showProfileWindow();
        });
        cancelButton.addActionListener(e -> {
            frame.dispose();
            showProfileWindow();
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showAdvisingWindow() {
        JFrame frame = new JFrame("Advising");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        Advising advising = new Advising(loggedInStudent, preAdvisedCourses, studentAdvisedCourses, studentPreAdvisedCourseCodes);
        JTabbedPane tabbedPane = new JTabbedPane();

        // Pre-Advising Tab
        JPanel preAdvisingPanel = new JPanel(new BorderLayout());
        String[] preAdvisingColumns = {"Code", "Title"};
        List<Course> uniqueCourses = advising.getUniqueCourses();
        Object[][] preAdvisingData = new Object[uniqueCourses.size()][2];
        for (int i = 0; i < uniqueCourses.size(); i++) {
            Course course = uniqueCourses.get(i);
            preAdvisingData[i][0] = course.getCode();
            preAdvisingData[i][1] = course.getName();
        }
        JTable preAdvisingTable = new JTable(preAdvisingData, preAdvisingColumns);
        preAdvisingTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        preAdvisingPanel.add(new JScrollPane(preAdvisingTable), BorderLayout.CENTER);
        JButton addButton = new JButton("Add Selected Courses");
        preAdvisingPanel.add(addButton, BorderLayout.SOUTH);
        addButton.addActionListener(e -> {
            int[] selectedRows = preAdvisingTable.getSelectedRows();
            List<String> selectedCourseCodes = new ArrayList<>();
            for (int row : selectedRows) {
                selectedCourseCodes.add((String) preAdvisingTable.getValueAt(row, 0));
            }
            if (advising.addPreAdvisedCourses(selectedCourseCodes)) {
                JOptionPane.showMessageDialog(frame, "Added selected courses to pre-advising.");
                frame.dispose();
                showAdvisingWindow();
            } else {
                JOptionPane.showMessageDialog(frame, "Could not add courses, perhaps credit limit exceeded or already added.");
            }
        });
        tabbedPane.addTab("Pre-Advising", preAdvisingPanel);

        // Advising Window Tab
        JPanel advisingWindowPanel = new JPanel(new BorderLayout());
        String[] advisingColumns = {"Code", "Title", "Section", "Time", "Day", "Faculty", "Credits"};
        List<Course> availableSections = advising.getAvailableSections();
        Object[][] advisingData = new Object[availableSections.size()][7];
        for (int i = 0; i < availableSections.size(); i++) {
            Course section = availableSections.get(i);
            advisingData[i][0] = section.getCode();
            advisingData[i][1] = section.getName();
            advisingData[i][2] = section.getSection();
            advisingData[i][3] = section.getTimeSlot();
            advisingData[i][4] = section.getTime();
            advisingData[i][5] = section.getFacultyInitial();
            advisingData[i][6] = section.getCredit();
        }
        JTable advisingTable = new JTable(advisingData, advisingColumns);
        advisingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        advisingWindowPanel.add(new JScrollPane(advisingTable), BorderLayout.CENTER);
        JPanel advisingButtonPanel = new JPanel();
        JButton adviseButton = new JButton("Advise Selected Section");
        JButton refreshButton = new JButton("Refresh");
        advisingButtonPanel.add(adviseButton);
        advisingButtonPanel.add(refreshButton);
        advisingWindowPanel.add(advisingButtonPanel, BorderLayout.SOUTH);
        adviseButton.addActionListener(e -> {
            int selectedRow = advisingTable.getSelectedRow();
            if (selectedRow >= 0) {
                Course selectedSection = availableSections.get(selectedRow);
                if (advising.adviseSection(selectedSection)) {
                    JOptionPane.showMessageDialog(frame, "Section " + selectedSection.getSection() + " for " + selectedSection.getCode() + " advised successfully!");
                    saveAdvisedCourses();
                    frame.dispose();
                    showAdvisingWindow();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a section.");
            }
        });
        refreshButton.addActionListener(e -> {
            frame.dispose();
            showAdvisingWindow();
        });
        tabbedPane.addTab("Advising Window", advisingWindowPanel);

        // Advising Slip Tab
        JPanel slipPanel = new JPanel(new BorderLayout());
        JTextArea slipArea = new JTextArea(advising.getAdvisingSlip());
        slipArea.setEditable(false);
        slipPanel.add(new JScrollPane(slipArea), BorderLayout.CENTER);
        tabbedPane.addTab("Advising Slip", slipPanel);

        // View Pre-Advised Courses Tab
        JPanel managePanel = new JPanel(new BorderLayout());
        JPanel prePanel = new JPanel();
        prePanel.setLayout(new BoxLayout(prePanel, BoxLayout.Y_AXIS));
        List<String> preCodes = advising.getPreAdvisedCourseCodes();
        for (String code : preCodes) {
            Course course = preAdvisedCourses.stream().filter(c -> c.getCode().equals(code)).findFirst().orElse(null);
            if (course == null) continue;
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.add(new JLabel(code + " - " + course.getName()));
            JButton dropButton = new JButton("X");
            dropButton.setForeground(Color.RED);
            dropButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to drop " + code + " from pre-advised courses?",
                    "Confirm Drop",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    advising.dropPreAdvisedCourse(code);
                    saveAdvisedCourses();
                    JOptionPane.showMessageDialog(frame, "Dropped " + code);
                    frame.dispose();
                    showAdvisingWindow();
                }
            });
            row.add(dropButton);
            prePanel.add(row);
        }
        managePanel.add(new JScrollPane(prePanel), BorderLayout.CENTER);
        tabbedPane.addTab("View Pre-Advised", managePanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        JButton backButton = new JButton("Back");
        frame.add(backButton, BorderLayout.SOUTH);
        backButton.addActionListener(e -> {
            frame.dispose();
            showHomeWindow();
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showCourseDropWindow() {
        JFrame frame = new JFrame("Course Drop");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());

        Advising advising = new Advising(loggedInStudent, preAdvisedCourses, studentAdvisedCourses, studentPreAdvisedCourseCodes);
        String[] columns = {"Code", "Title", "Section", "Time", "Day", "Faculty", "Credits", "Drop"};
        List<Course> advisedCourses = advising.getAdvisedCourses();
        Object[][] data = new Object[advisedCourses.size()][8];
        for (int i = 0; i < advisedCourses.size(); i++) {
            Course course = advisedCourses.get(i);
            data[i][0] = course.getCode();
            data[i][1] = course.getName();
            data[i][2] = course.getSection();
            data[i][3] = course.getTimeSlot();
            data[i][4] = course.getTime();
            data[i][5] = course.getFacultyInitial();
            data[i][6] = course.getCredit();
            data[i][7] = "";
        }
        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };
        table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(table, new JCheckBox(), advising, frame));
        table.getColumnModel().getColumn(7).setPreferredWidth(50);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        frame.add(backButton, BorderLayout.SOUTH);
        backButton.addActionListener(e -> {
            frame.dispose();
            showHomeWindow();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showPaymentsWindow() {
        JFrame frame = new JFrame("Payments");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        Payment payment = new Payment(loggedInStudent, studentAdvisedCourses.getOrDefault(loggedInStudent.getId(), new ArrayList<>()));
        JTextArea statusArea = new JTextArea(payment.getAccountStatus());
        statusArea.setEditable(false);
        frame.add(new JScrollPane(statusArea), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton historyButton = new JButton("Online Payment History");
        JButton backButton = new JButton("Back");
        buttonPanel.add(historyButton);
        buttonPanel.add(backButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        historyButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, payment.getPaymentHistory()));
        backButton.addActionListener(e -> {
            frame.dispose();
            showHomeWindow();
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private static void showChatbotWindow() {
        JFrame frame = new JFrame("RDS Chatbot");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.append("Welcome to the RDS Chatbot!\n");
        chatArea.append("Commands:\n");
        chatArea.append("- 'grade <score>' to calculate grade for a score (e.g., 'grade 85')\n");
        chatArea.append("- 'prereq <course code>' to check prerequisites (e.g., 'prereq MIS207')\n");
        chatArea.append("- 'addcourse <course code> <credits> <grade>' to add a course (e.g., 'addcourse CS101 3.0 A')\n");
        chatArea.append("  Valid grades: A, A-, B+, B, B-, C+, C, C-, D+, D, F, I (Incomplete), W (Withdrawal)\n");
        chatArea.append("- 'calculate' to compute GPA for added courses\n");
        chatArea.append("- 'listcourses' to view added courses\n");
        chatArea.append("- 'clearcourses' to clear added courses\n");
        chatArea.append("- 'exit' to return to home window\n\n");
        JTextField inputField = new JTextField();
        JButton sendButton = new JButton("Send");
        JButton backButton = new JButton("Back");
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(backButton, BorderLayout.SOUTH);
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        Chatbot chatbot = new Chatbot();
        sendButton.addActionListener(e -> {
            String input = inputField.getText().trim();
            if (input.isEmpty()) {
                return;
            }
            chatArea.append("You: " + input + "\n");
           if (input.toLowerCase().startsWith("prereq ")) {
                String courseCode = input.substring(7).trim();
                String response = chatbot.getPrerequisites(courseCode);
                chatArea.append("Chatbot: " + response + "\n\n");
            } else if (input.toLowerCase().startsWith("addcourse ")) {
                String[] parts = input.substring(9).trim().split("\\s+");
                if (parts.length != 3) {
                    chatArea.append("Chatbot: Invalid format. Use 'addcourse <course code> <credits> <grade>'.\n\n");
                } else {
                    String response = chatbot.addCourse(parts[0], parts[1], parts[2]);
                    chatArea.append("Chatbot: " + response + "\n\n");
                }
            } else if (input.toLowerCase().equals("calculate")) {
                String response = chatbot.calculateGPA();
                chatArea.append("Chatbot: " + response + "\n\n");
            } else if (input.toLowerCase().equals("listcourses")) {
                String response = chatbot.listCourses();
                chatArea.append("Chatbot: " + response + "\n\n");
            } else if (input.toLowerCase().equals("clearcourses")) {
                chatbot.clearCourses();
                chatArea.append("Chatbot: All courses cleared.\n\n");
            } else if (input.toLowerCase().equals("exit")) {
                frame.dispose();
                showHomeWindow();
            } else {
                chatArea.append("Chatbot: Invalid command. Use 'grade <score>', 'prereq <course code>', 'addcourse <course code> <credits> <grade>', 'calculate', 'listcourses', 'clearcourses', or 'exit'.\n\n");
            }
            inputField.setText("");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
        inputField.addActionListener(e -> sendButton.doClick());
        backButton.addActionListener(e -> {
            frame.dispose();
            showHomeWindow();
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    // Custom renderer for the drop button
    static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("X");
            setForeground(Color.RED);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom editor for the drop button
    static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String code;
        private Advising advising;
        private JFrame frame;
        private JTable table;
        private boolean isPushed;

        public ButtonEditor(JTable table, JCheckBox checkBox, Advising advising, JFrame frame) {
            super(checkBox);
            this.table = table;
            this.advising = advising;
            this.frame = frame;
            button = new JButton("X");
            button.setOpaque(true);
            button.setForeground(Color.RED);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            code = (String) table.getValueAt(row, 0);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                if (table.getColumnCount() > 7 && table.getColumnName(7).equals("Drop")) {
                    int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to drop " + code + " from advised courses?",
                        "Confirm Drop",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        advising.dropAdvisedCourse(code);
                        JOptionPane.showMessageDialog(frame, "Dropped " + code + " and moved back to pre-advised.");
                        frame.dispose();
                        showCourseDropWindow();
                    }
                }
            }
            isPushed = false;
            return "";
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
