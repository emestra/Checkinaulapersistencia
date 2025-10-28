# 🎯 Actividad: **Check‑in Aula**

🗓️ **Entrega individual**: **sábado 4 de octubre de 2025 – 11:00 PM (GMT‑3, Uruguay)**
🔗 **Entrega**: **link a un repositorio GitHub** con el proyecto que **compila y corre por consola** o GUI.

## 🔄 Actualizaciones Implementadas

### 💾 Persistencia de Datos
- Implementación de persistencia usando archivos CSV
- Los datos se guardan automáticamente en `data/inscripciones.csv`
- Carga automática de datos previos al iniciar
- Formato: `nombre|documento|curso|fecha_hora`
- Persistencia funcional tanto en modo GUI como consola

### ✅ Validación de Datos
- **Campo Nombre**: Solo acepta letras y espacios
- **Campo Documento**: Solo acepta números
- Validación implementada tanto en GUI como en consola
- Mensajes de error claros para entradas inválidas

### 🖥️ Interfaz Gráfica (GUI)
- Interfaz moderna con validación en tiempo real
- Filtros de entrada que previenen caracteres inválidos
- Sistema de búsqueda integrado
- Vista de resumen por curso

### 💻 Modo Consola
- Validación de entrada mejorada
- Mensajes de error descriptivos
- Mantiene todas las funcionalidades originales

---

## 📍 Contexto

Necesitamos un registro **muy simple** de inscripciones al inicio de clase. Debe **funcionar por consola** y traer **algunos datos de prueba** precargados. Luego, **agregá un archivo de GUI** y **modificá `main`** para poder ejecutarlo con `--gui`. Los datos son **persistentes** usando un archivo CSV.

---

## ✅ Qué debe hacer (mínimo)

* Registrar inscripciones (Nombre, Documento, Curso, Hora automática).
* Listar todas y buscar por texto.
* Mostrar un resumen básico por curso.
* **Sin validaciones complejas** (flujo básico, se permiten duplicados).

---

## 📦 Estructura del Proyecto

```
src/
└─ edu/cerp/checkin/
   ├─ model/
   │  └─ Inscripcion.java           # Modelo de datos
   ├─ logic/
   │  └─ SesionService.java         # Lógica de negocio
   ├─ persistencia/
   │  └─ ArchivoManager.java        # Gestión de persistencia CSV
   ├─ console/
   │  └─ MainConsole.java          # Interfaz de consola
   ├─ ui/
   │  └─ CheckInGUI.java           # Interfaz gráfica
   └─ App.java                     # Punto de entrada (--gui para GUI)

data/
   └─ inscripciones.csv           # Almacenamiento persistente
```

---

## 🧾 Código **completo** (consola)

### `src/edu/cerp/checkin/model/Inscripcion.java`

```java
package edu.cerp.checkin.model;

import java.time.LocalDateTime;

public class Inscripcion {
    private final String nombre;
    private final String documento;
    private final String curso; // "Prog 1" | "Prog 2" | "Base de Datos"
    private final LocalDateTime fechaHora;

    public Inscripcion(String nombre, String documento, String curso, LocalDateTime fechaHora) {
        this.nombre = nombre;
        this.documento = documento;
        this.curso = curso;
        this.fechaHora = fechaHora;
    }

    public String getNombre() { return nombre; }
    public String getDocumento() { return documento; }
    public String getCurso() { return curso; }
    public LocalDateTime getFechaHora() { return fechaHora; }
}
```

### `src/edu/cerp/checkin/logic/SesionService.java`

```java
package edu.cerp.checkin.logic;

import edu.cerp.checkin.model.Inscripcion;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/** Lógica mínima en memoria (sin validaciones complejas). */
public class SesionService {
    private final List<Inscripcion> inscripciones = new ArrayList<>();

    public void registrar(String nombre, String documento, String curso) {
        if (nombre == null || nombre.isBlank()) nombre = "(sin nombre)";
        if (documento == null) documento = "";
        if (curso == null || curso.isBlank()) curso = "Prog 1";
        inscripciones.add(new Inscripcion(nombre.trim(), documento.trim(), curso.trim(), LocalDateTime.now()));
    }

    public List<Inscripcion> listar() { return Collections.unmodifiableList(inscripciones); }

    public List<Inscripcion> buscar(String q) {
        if (q == null || q.isBlank()) return listar();
        String s = q.toLowerCase();
        return inscripciones.stream()
                .filter(i -> i.getNombre().toLowerCase().contains(s) || i.getDocumento().toLowerCase().contains(s))
                .collect(Collectors.toList());
    }

    public String resumen() {
        Map<String, Long> porCurso = inscripciones.stream()
                .collect(Collectors.groupingBy(Inscripcion::getCurso, LinkedHashMap::new, Collectors.counting()));
        StringBuilder sb = new StringBuilder();
        sb.append("Total: ").append(inscripciones.size()).append("\nPor curso:\n");
        for (var e : porCurso.entrySet()) sb.append(" - ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        return sb.toString();
    }

    /** Datos de prueba para arrancar la app. */
    public void cargarDatosDemo() {
        registrar("Ana Pérez", "51234567", "Prog 2");
        registrar("Luis Gómez", "49887766", "Prog 1");
        registrar("Camila Díaz", "53422110", "Base de Datos");
    }
}
```

### `src/edu/cerp/checkin/console/MainConsole.java`

```java
package edu.cerp.checkin.console;

import edu.cerp.checkin.logic.SesionService;
import edu.cerp.checkin.model.Inscripcion;

import java.util.List;
import java.util.Scanner;

public class MainConsole {
    public static void run(SesionService service){
        Scanner sc = new Scanner(System.in);
        int op = -1;
        while (op != 0) {
            System.out.println("\n== Check-in Aula (Consola) ==");
            System.out.println("1) Registrar  2) Listar  3) Buscar  4) Resumen  0) Salir");
            System.out.print("> ");
            String s = sc.nextLine().trim();
            if (s.isEmpty()) continue;
            try { op = Integer.parseInt(s); } catch(Exception e){ op = -1; }
            switch (op) {
                case 1 -> {
                    System.out.print("Nombre: "); String n = sc.nextLine();
                    System.out.print("Documento: "); String d = sc.nextLine();
                    System.out.print("Curso [Prog 1/Prog 2/Base de Datos]: "); String c = sc.nextLine();
                    service.registrar(n,d,c);
                    System.out.println("✔ Registrado");
                }
                case 2 -> listar(service.listar());
                case 3 -> { System.out.print("Buscar: "); String q = sc.nextLine(); listar(service.buscar(q)); }
                case 4 -> System.out.println(service.resumen());
                case 0 -> System.out.println("Adiós");
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private static void listar(List<Inscripcion> ls){
        System.out.println("Nombre | Documento | Curso | Hora");
        for (Inscripcion i: ls) {
            System.out.println(i.getNombre()+" | "+i.getDocumento()+" | "+i.getCurso()+" | "+i.getFechaHora());
        }
    }
}
```

### `src/edu/cerp/checkin/App.java`

```java
package edu.cerp.checkin;

import edu.cerp.checkin.console.MainConsole;
import edu.cerp.checkin.logic.SesionService;

public class App {
    public static void main(String[] args) {
        boolean usarGui = false;
        for (String a : args) if ("--gui".equalsIgnoreCase(a)) usarGui = true;

        SesionService service = new SesionService(null);
        service.cargarDatosDemo(); // datos de prueba

        if (usarGui) {
            // TODO (AHORA): crear edu.cerp.checkin.ui.CheckInGUI con método estático show(service)
            // y descomentar la línea siguiente para lanzar tu GUI.
            // edu.cerp.checkin.ui.CheckInGUI.show(service);
            System.out.println("⚠ GUI no implementada. Corre sin --gui para usar consola.");
        } else {
            MainConsole.run(service);
        }
    }
}
```

---

## 🧩 Lo que debés hacer **AHORA** (GUI mínima + cambio en `main`)

1. **Crear** `src/edu/cerp/checkin/ui/CheckInGUI.java` con **una ventana mínima** (por ejemplo, `JFrame` con un formulario simple y un botón “Registrar” que llame a `service.registrar(...)` y una lista que muestre `service.listar()`).

   * Debe tener un método **estático** `show(SesionService service)` que muestre la ventana.
   * Reutilizá `SesionService` (no reescribas lógica en la GUI).
2. **Modificar `App.java`**: descomentar la llamada a `CheckInGUI.show(service);` y ejecutar con `--gui`.

   * Ejemplo: `java edu.cerp.checkin.App --gui`

---

## � Compilación y Ejecución

### Compilar el proyecto
```bash
javac -d out src/edu/cerp/checkin/App.java src/edu/cerp/checkin/console/MainConsole.java src/edu/cerp/checkin/ui/CheckInGUI.java src/edu/cerp/checkin/logic/SesionService.java src/edu/cerp/checkin/model/Inscripcion.java src/edu/cerp/checkin/persistencia/ArchivoManager.java
```

### Ejecutar en modo GUI
```bash
java -cp out edu.cerp.checkin.App --gui
```

### Ejecutar en modo Consola
```bash
java -cp out edu.cerp.checkin.App
```

## 💾 Persistencia de Datos

He elegido implementar la persistencia usando archivos CSV por las siguientes razones:

1. **Simplicidad**: El formato CSV es fácil de leer y escribir, tanto para el programa como para los usuarios.
2. **Compatibilidad**: Los archivos CSV se pueden abrir y editar con Excel o Google Sheets.
3. **Transparencia**: Al ser texto plano, facilita la depuración y el aprendizaje.
4. **Sin dependencias**: No requiere bibliotecas adicionales.

## 📤 Entrega

* **Código fuente** completo con estructura actualizada
* **README.md** con documentación detallada
* **Persistencia.md** con detalles de implementación
* **.gitignore** actualizado (incluye /data/)
* **Capturas de pantalla** de la aplicación en ejecución
