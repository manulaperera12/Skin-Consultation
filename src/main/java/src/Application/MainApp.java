package src.Application;

import src.Consultation.Consultation;
import src.Main;
import src.Managers.DataManager;
import src.Users.Doctor;
import src.Users.Patient;
import src.Utils.CommonUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainApp {
    private DataManager manager;
    private JLabel emptyLabel =  new JLabel("");



    public static void centerWindow(Window frame) {
        Rectangle bounds = frame.getGraphicsConfiguration().getBounds();
        Dimension dimension = bounds.getSize();
        int x = (int) (((dimension.getWidth() - frame.getWidth()) / 2) + bounds.getMinX());
        int y = (int) (((dimension.getHeight() - frame.getHeight()) / 2) + bounds.getMinY());
        frame.setLocation(x, y);
    }

    public MainApp() {
        manager = DataManager.getInstance();

        JFrame mainFrame = new JFrame();
        mainFrame.setTitle("");
        mainFrame.setResizable(false);
        mainFrame.setSize(1600, 1070);
        centerWindow(mainFrame);


        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.decode("#D9F8C4"));

        mainPanel.setLayout(null);

        JLabel docTitle =  new JLabel("Doctors Details");
        docTitle.setHorizontalAlignment(JLabel.CENTER);

        docTitle.setForeground(Color.decode("#285430"));
        docTitle.setFont(new Font("Arial", Font.BOLD, 30));

        docTitle.setBounds(0, 40, 1600, 30);



        JPanel doctorsPanel = designDoctorsPanel();
        doctorsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        doctorsPanel.setBounds(0, 20, 1600, 260);



        mainPanel.add(docTitle);
        mainPanel.add(doctorsPanel);

        int addFormYAxis = 0;

        if (manager.getConsultationCount() > 0) {
            addFormYAxis = 650;

            JLabel consultationTitle =  new JLabel("Consultation Details");
            consultationTitle.setForeground(Color.decode("#285430"));
//            consultationTitle.setBounds(10, 250, 200, 10);

            consultationTitle.setFont(new Font("Arial", Font.BOLD, 30));
            consultationTitle.setHorizontalAlignment(JLabel.CENTER);

            consultationTitle.setBounds(0, 325, 1600, 30);


            JPanel consultationPanel = designConsultationsPanel();
            consultationPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            consultationPanel.setBounds(0, 300, 1600, 350); //JPanel of consultation

            JPanel addConsultationPanel = designFormsPanel();
            addConsultationPanel.setBounds(0, 1000, 1600, 500); //JPanel of add consultation

            mainPanel.add(consultationTitle);
            mainPanel.add(consultationPanel);
        } else {
            addFormYAxis = 280;
        }

        JLabel formTitle =  new JLabel("Add Consultation");
        formTitle.setForeground(Color.decode("#285430"));

        formTitle.setHorizontalAlignment(JLabel.CENTER);
        formTitle.setOpaque(true);
        formTitle.setBackground(Color.decode("#ABE5C4"));

        formTitle.setBounds(0, 700, 1600, 55); //tittle control of add Consultation
        formTitle.setFont(new Font("Arial", Font.BOLD, 30));



        JPanel formPanel = designFormsPanel();
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.setBounds(160, 760, 1200, 200);///////////////

        mainPanel.add(formTitle, SwingConstants.CENTER);
        mainPanel.add(formPanel);

        mainFrame.add(mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }


    private JPanel designDoctorsPanel() {
        List<Doctor> doctors = manager.getDoctors();
        String [][] data = new String[doctors.size()][];

        doctors.forEach(doctor -> {
            data[doctors.indexOf(doctor)] = new String[]{
                doctor.getLicenseNumber(),
                doctor.getName(),
                doctor.getSurName(),
                doctor.getSpecialisation().getSpecialisation(),
                doctor.getDateOfBirth(),
                doctor.getContactNumber(),
                doctor.getEmail()
            };
        });

        String [] columnNames = {
            "Medical License Number",
            "Name",
            "Surname",
            "Specialisation",
            "Date Of Birth",
            "Contact Number",
            "Email"
        };

        JLabel title =  new JLabel("Patient Details");
        title.setForeground(Color.decode("#285430"));


        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tableView = new JTable(data, columnNames);
//        tableView.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));
        tableView.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(tableView);
        scrollPane.setBounds(150,80,1200,150); //doctor details table controls


////////////////start/////////////////
        JPanel doctorsPanel = new JPanel();
        doctorsPanel.setBackground(Color.decode("#ABE5C4"));
//        doctorsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
//                "Doctor Details", TitledBorder.CENTER, TitledBorder.TOP,
//                Font.getFont("Arial"), Color.black));


        doctorsPanel.setLayout(null);
//        doctorsPanel.setBounds(110,100,1560,370);



////////////////end/////////////////




//
//        JPanel doctorsPanel = new JPanel();
//        doctorsPanel.setLayout(null);

        doctorsPanel.add(scrollPane);

        return doctorsPanel;
    }


    private JPanel designConsultationsPanel() {
        List<Consultation> consultations = manager.getConsultations();
        String [][] data = new String[consultations.size()][];

        consultations.forEach(consultation -> {
            data[consultations.indexOf(consultation)] = new String[]{
                consultation.getDoctor().getName(),
                consultation.getPatient().getName(),
                consultation.getDate(),
                consultation.getTimeSlot(),
                consultation.getNotes(),
                String.valueOf(consultation.getCost())
            };
        });

        String [] columnNames = {
                "Doctor Name",
                "Patient Name",
                "Consultation Date",
                "Consultation Time",
                "Extra Notes",
                "Consultation Fees (\u00a3)"
        };

        JLabel title =  new JLabel("Patient Details");
        title.setForeground(Color.decode("#54B435"));



        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tableView = new JTable(data, columnNames);
        tableView.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(tableView);
        scrollPane.setBounds(150,70,1200,230); //patient table



        ////////////////start/////////////////
        JPanel consultationPanel = new JPanel();
        consultationPanel.setBackground(Color.decode("#ABE5C4"));



//        JPanel consultationPanel = new JPanel();
        consultationPanel.setLayout(null);
        consultationPanel.add(scrollPane);
//        consultationPanel.setBounds(150,1000,1200,1000);


        return consultationPanel;
    }


    private JPanel designFormsPanel() {
        JPanel mainPanel = new JPanel();




        mainPanel.setBackground(Color.decode("#ABE5C4"));

//        mainPanel.setLayout(null);
//        mainPanel.setBounds(110,1000,1500,330);
//        this.add(mainPanel);


        mainPanel.setLayout(new GridLayout(1,2));

        JPanel patientPanel = new JPanel();


        patientPanel.setLayout(new GridLayout(1,2));

        JPanel patientP1 = new JPanel();


        patientP1.setLayout(new GridLayout(6,1));

        JLabel patientLabel =  new JLabel("<HTML><U>Patient Details</U></HTML>");
        patientLabel.setBounds(10, 100, 200, 10);//tittle control of Patient Details
        patientLabel.setFont(new Font("Arial", Font.BOLD, 20));

        patientLabel.setForeground(Color.decode("#54B435"));

        JLabel pNameL =  new JLabel("Patient Name:");
        pNameL.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));

        JLabel pSNameL =  new JLabel("Patient Surname:");
        pSNameL.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));

        JLabel pBDayL =  new JLabel("Patient Birthday (yyyy-MM-dd):");
        pBDayL.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));

        JLabel pContactL =  new JLabel("Patient Contact Number (+94):");
        pContactL.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));

        JLabel pEmailL =  new JLabel("Patient Email:");
        pEmailL.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));


        patientP1.add(patientLabel);
        patientP1.add(pNameL);
        patientP1.add(pSNameL);
        patientP1.add(pBDayL);
        patientP1.add(pContactL);
        patientP1.add(pEmailL);

        patientPanel.add(patientP1);

        JPanel patientP2 = new JPanel();

        patientP2.setLayout(new GridLayout(6,1));

        JLabel patientLabelEmpty =  new JLabel("");
        patientLabelEmpty.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));

        JTextField pNameT = new JTextField(10);
        pNameT.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));

        JTextField pSNameT = new JTextField(10);
        pSNameT.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));

        JTextField pBDayT = new JTextField(10);
        pBDayT.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));

        JTextField pContactT = new JTextField(10);
        pContactT.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));

        JTextField pEmailT = new JTextField(10);
        pEmailT.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));


        patientP2.add(patientLabelEmpty);
        patientP2.add(pNameT);
        patientP2.add(pSNameT);
        patientP2.add(pBDayT);
        patientP2.add(pContactT);
        patientP2.add(pEmailT);

        patientPanel.add(patientP2);

        JPanel consultationPanel = new JPanel();
        consultationPanel.setLayout(new GridLayout(1,2));

        JPanel cons1 = new JPanel();

        cons1.setLayout(new GridLayout(6,1));


        JLabel consLabel =  new JLabel("<HTML><U>Doctors Details</U></HTML>");

        consLabel.setFont(new Font("Arial", Font.BOLD, 20));
        consLabel.setForeground(Color.decode("#54B435"));

        JLabel consDName =  new JLabel("Doctors Name:");
        consDName.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));

        JLabel consDate =  new JLabel("Appointment Date (yyyy-MM-dd):");
        consDate.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));

        JLabel consTime =  new JLabel("Appointment time:");
        consTime.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));

        JLabel consBook =  new JLabel("");
        consBook.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));


        cons1.add(consLabel);
        cons1.add(consDName);
        cons1.add(consDate);
        cons1.add(consTime);
        cons1.add(consBook);
        consultationPanel.add(cons1);

        JPanel cons2 = new JPanel();
        cons2.setLayout(new GridLayout(6,1));

        JLabel consLabelEmpty =  new JLabel();
        consLabelEmpty.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));


        String[] docNames = new String[manager.getDoctorCount()];
        manager.getDoctors().forEach(doctor ->
            docNames[manager.getDoctors().indexOf(doctor)] = doctor.getName() + " " + doctor.getSurName()
        );
        JComboBox docNamesBox = new JComboBox(docNames);

        JTextField conDateT = new JTextField(10);

        String timeSlots [] = {
            "9:00AM",
            "10:00AM",
            "11:00AM",
            "12:00AM",
            "02:00PM",
            "03:00PM",
            "04:00PM",
            "05:00PM"
        };
        JComboBox timeSlotBox = new JComboBox(timeSlots);
        timeSlotBox.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));


        JButton confirmButton = new JButton("Book Appointment");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 16));
        confirmButton.setForeground(Color.decode("#003865"));


        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Patient patient = new Patient();
                patient.setName(pNameT.getText());
                patient.setSurName(pSNameT.getText());
                patient.setDateOfBirth(pBDayT.getText());
                patient.setContactNumber(pContactT.getText());
                patient.setEmail(pEmailT.getText());

                manager.addNewPatient(patient);

                Consultation consultation = new Consultation();
                consultation.setDoctor(manager.getDoctors().get(docNamesBox.getSelectedIndex()));
                consultation.setPatient(patient);
                consultation.setDate(conDateT.getText());
                consultation.setTimeSlot(timeSlots[timeSlotBox.getSelectedIndex()]);
                consultation.setCost(25);
                consultation.setNotes("No notes");

                manager.addNewConsultation(consultation);
                manager.saveData();
            }
        });

        cons2.add(consLabelEmpty);
        cons2.add(docNamesBox);
        cons2.add(conDateT);
        cons2.add(timeSlotBox);
        cons2.add(new JLabel());
        cons2.add(confirmButton);

        consultationPanel.add(cons2);

        mainPanel.add(patientPanel);
        mainPanel.add(consultationPanel);
        return mainPanel;
    }

    public static void main(String[] args) {
        new MainApp();
    }
}
