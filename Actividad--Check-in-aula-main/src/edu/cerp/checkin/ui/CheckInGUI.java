package edu.cerp.checkin.ui;

import edu.cerp.checkin.logic.SesionService;
import edu.cerp.checkin.model.Inscripcion;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class CheckInGUI {

    private static final String[] CURSOS = {
            "Programacion 1", "Programacion 2", "Base de Datos", "Matemáticas",
            "Física", "Química", "Algoritmos", "Redes",
            "Sistemas Operativos", "Ingeniería de Software"
    };
    private static final DateTimeFormatter FECHA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Color HEADER_COLOR = new Color(0, 0, 0);
    private static final Color BUTTON_COLOR = new Color(240, 240, 240);
    private static final Color BUTTON_BORDER_COLOR = new Color(180, 180, 180);
    private static final Color TEXT_COLOR = new Color(0, 0, 0);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 12);

    public static void show(SesionService service) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ventana principal
        JFrame ventana = new JFrame("Sistema de Check-in");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(900, 700);
        ventana.setLocationRelativeTo(null);

        // Panel principal con BoxLayout vertical
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Registro de Asistencia"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        formPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes del formulario
        JLabel nombreL = new JLabel("Nombre:");
        nombreL.setFont(LABEL_FONT);
        JTextField nombre = new JTextField(20);
        nombre.setMargin(new Insets(5, 5, 5, 5));

        JLabel documentoL = new JLabel("Documento:");
        documentoL.setFont(LABEL_FONT);
        JTextField documento = new JTextField(20);
        documento.setMargin(new Insets(5, 5, 5, 5));

        // Document filters: nombre -> solo letras y espacios; documento -> solo dígitos
        ((AbstractDocument) nombre.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null)
                    return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < string.length(); i++) {
                    char c = string.charAt(i);
                    if (Character.isLetter(c) || Character.isSpaceChar(c))
                        sb.append(c);
                }
                super.insertString(fb, offset, sb.toString(), attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text == null)
                    return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    if (Character.isLetter(c) || Character.isSpaceChar(c))
                        sb.append(c);
                }
                super.replace(fb, offset, length, sb.toString(), attrs);
            }
        });

        ((AbstractDocument) documento.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null)
                    return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < string.length(); i++) {
                    char c = string.charAt(i);
                    if (Character.isDigit(c))
                        sb.append(c);
                }
                super.insertString(fb, offset, sb.toString(), attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text == null)
                    return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    if (Character.isDigit(c))
                        sb.append(c);
                }
                super.replace(fb, offset, length, sb.toString(), attrs);
            }
        });

        JLabel cursoL = new JLabel("Curso:");
        cursoL.setFont(LABEL_FONT);
        JComboBox<String> cursoCB = new JComboBox<>(CURSOS);
        cursoCB.setPreferredSize(new Dimension(cursoCB.getPreferredSize().width, 30));

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton registrarBtn = new JButton("Registrar");
        JButton limpiarBtn = new JButton("Limpiar");

        // Estilo de botones
        registrarBtn.setBackground(BUTTON_COLOR);
        registrarBtn.setForeground(Color.BLACK);
        registrarBtn.setFocusPainted(false);
        registrarBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        registrarBtn.setBorder(BorderFactory.createLineBorder(BUTTON_BORDER_COLOR));

        limpiarBtn.setBackground(BUTTON_COLOR);
        limpiarBtn.setForeground(Color.BLACK);
        limpiarBtn.setFocusPainted(false);
        limpiarBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        limpiarBtn.setBorder(BorderFactory.createLineBorder(BUTTON_BORDER_COLOR));

        buttonPanel.add(registrarBtn);
        buttonPanel.add(limpiarBtn);

        // Tabla de registros
        DefaultTableModel tableModel = new DefaultTableModel(
                new String[] { "Nombre", "Documento", "Curso", "Fecha y Hora" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tabla = new JTable(tableModel);
        tabla.setRowHeight(25);
        tabla.setIntercellSpacing(new Dimension(10, 5));
        tabla.setGridColor(new Color(180, 180, 180));
        tabla.setShowVerticalLines(true);
        tabla.setShowHorizontalLines(true);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.setSelectionBackground(new Color(230, 230, 250));
        tabla.setSelectionForeground(Color.BLACK);

        // Configurar el encabezado de la tabla
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBorder(BorderFactory.createLineBorder(BUTTON_BORDER_COLOR));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Agregar componentes al formPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nombreL, gbc);
        gbc.gridx = 1;
        formPanel.add(nombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(documentoL, gbc);
        gbc.gridx = 1;
        formPanel.add(documento, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(cursoL, gbc);
        gbc.gridx = 1;
        formPanel.add(cursoCB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel buscarL = new JLabel("Buscar:");
        buscarL.setFont(LABEL_FONT);
        JTextField buscarField = new JTextField(20);
        buscarField.setMaximumSize(new Dimension(200, 30));
        JButton buscarBtn = new JButton("Buscar");
        buscarBtn.setBackground(BUTTON_COLOR);
        buscarBtn.setForeground(Color.BLACK);
        buscarBtn.setFocusPainted(false);
        buscarBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        buscarBtn.setBorder(BorderFactory.createLineBorder(BUTTON_BORDER_COLOR));

        searchPanel.add(buscarL);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(buscarField);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(buscarBtn);
        searchPanel.add(Box.createHorizontalGlue());

        // Panel central para mantener el searchPanel alineado a la izquierda
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // Agregar componentes al panel principal
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(centerPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Panel para la tabla con padding y título
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Lista de Asistencias"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Agregar barra de herramientas para la tabla
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton exportarBtn = new JButton("Exportar");
        JButton eliminarBtn = new JButton("Eliminar Seleccionado");
        JButton resumenBtn = new JButton("Ver Resumen");

        exportarBtn.setBackground(BUTTON_COLOR);
        exportarBtn.setForeground(Color.BLACK);
        exportarBtn.setBorder(BorderFactory.createLineBorder(BUTTON_BORDER_COLOR));
        eliminarBtn.setBackground(BUTTON_COLOR);
        eliminarBtn.setForeground(Color.BLACK);
        eliminarBtn.setBorder(BorderFactory.createLineBorder(BUTTON_BORDER_COLOR));
        resumenBtn.setBackground(BUTTON_COLOR);
        resumenBtn.setForeground(Color.BLACK);
        resumenBtn.setBorder(BorderFactory.createLineBorder(BUTTON_BORDER_COLOR));

        toolBar.add(exportarBtn);
        toolBar.add(eliminarBtn);
        toolBar.add(resumenBtn);

        tablePanel.add(toolBar, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tablePanel);

        // Funcionalidad de los botones
        registrarBtn.addActionListener(e -> {
            if (nombre.getText().trim().isEmpty() || documento.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(ventana,
                        "Por favor complete los campos nombre y documento",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            service.registrar(
                    nombre.getText(),
                    documento.getText(),
                    (String) cursoCB.getSelectedItem());

            actualizarTabla(tableModel, service.listar());
            nombre.setText("");
            documento.setText("");
            cursoCB.setSelectedIndex(0);
        });

        limpiarBtn.addActionListener(e -> {
            nombre.setText("");
            documento.setText("");
            cursoCB.setSelectedIndex(0);
        });

        buscarBtn.addActionListener(e -> {
            actualizarTabla(tableModel, service.buscar(buscarField.getText()));
        });

        // Funcionalidad de los nuevos botones
        exportarBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Reporte");
            if (fileChooser.showSaveDialog(ventana) == JFileChooser.APPROVE_OPTION) {
                // Aquí iría la lógica de exportación
                JOptionPane.showMessageDialog(ventana, "Funcionalidad de exportación en desarrollo");
            }
        });

        eliminarBtn.addActionListener(e -> {
            int row = tabla.getSelectedRow();
            if (row != -1) {
                if (JOptionPane.showConfirmDialog(ventana,
                        "¿Está seguro de eliminar este registro?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    // Aquí iría la lógica de eliminación
                    JOptionPane.showMessageDialog(ventana, "Funcionalidad de eliminación en desarrollo");
                }
            } else {
                JOptionPane.showMessageDialog(ventana,
                        "Por favor seleccione un registro para eliminar",
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        resumenBtn.addActionListener(e -> {
            String resumen = service.resumen();
            JTextArea textArea = new JTextArea(resumen);
            textArea.setEditable(false);
            JScrollPane resumenScroll = new JScrollPane(textArea);
            resumenScroll.setPreferredSize(new Dimension(300, 200));

            JDialog dialog = new JDialog(ventana, "Resumen de Asistencias", true);
            dialog.add(resumenScroll);
            dialog.pack();
            dialog.setLocationRelativeTo(ventana);
            dialog.setVisible(true);
        });

        // Búsqueda en tiempo real
        buscarField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                actualizarTabla(tableModel, service.buscar(buscarField.getText()));
            }
        });

        // Cargar datos iniciales
        actualizarTabla(tableModel, service.listar());

        ventana.add(mainPanel);
        ventana.setVisible(true);
    }

    private static void actualizarTabla(DefaultTableModel model, List<Inscripcion> inscripciones) {
        model.setRowCount(0);
        for (Inscripcion i : inscripciones) {
            model.addRow(new Object[] {
                    i.getNombre(),
                    i.getDocumento(),
                    i.getCurso(),
                    i.getFechaHora().format(FECHA_FORMATTER)
            });
        }
    }
}
