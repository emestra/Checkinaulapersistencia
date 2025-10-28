package edu.cerp.checkin.persistencia;

import edu.cerp.checkin.model.Inscripcion;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ArchivoManager {
    private static final String DATA_DIR = "data";
    private static final String FILE_PATH = DATA_DIR + File.separator + "inscripciones.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public ArchivoManager() {
        inicializarDirectorio();
    }

    private void inicializarDirectorio() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }

    public void guardarInscripciones(List<Inscripcion> inscripciones) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Inscripcion inscripcion : inscripciones) {
                writer.println(String.format("%s|%s|%s|%s",
                        inscripcion.getNombre(),
                        inscripcion.getDocumento(),
                        inscripcion.getCurso(),
                        inscripcion.getFechaHora().format(DATE_FORMATTER)));
            }
        } catch (IOException e) {
            System.err.println("Error al guardar las inscripciones: " + e.getMessage());
        }
    }

    public List<Inscripcion> cargarInscripciones() {
        List<Inscripcion> inscripciones = new ArrayList<>();
        File archivo = new File(FILE_PATH);

        if (!archivo.exists()) {
            return inscripciones;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length == 4) {
                    inscripciones.add(new Inscripcion(
                            datos[0],
                            datos[1],
                            datos[2],
                            LocalDateTime.parse(datos[3], DATE_FORMATTER)));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar las inscripciones: " + e.getMessage());
        }

        return inscripciones;
    }
}