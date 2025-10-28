# Persistencia de Datos

En este proyecto, elegí implementar la persistencia usando archivos CSV (Valores Separados por Pipe "|") por las siguientes razones:

1. **Simplicidad**: El formato CSV es fácil de leer y escribir, tanto para el programa como para los humanos.
2. **Compatibilidad**: Los archivos CSV se pueden abrir y editar con Excel o Google Sheets, lo que facilita la visualización y manipulación de datos para fines educativos.
3. **Transparencia**: Al ser un formato de texto plano, los estudiantes pueden ver y entender fácilmente cómo se almacenan los datos.
4. **Sin dependencias externas**: No requiere bibliotecas adicionales, manteniendo el proyecto simple y autónomo.

Los datos se almacenan en `data/inscripciones.csv`, con cada línea representando una inscripción en el formato:
```
nombre|documento|curso|fecha_hora
```

La persistencia se gestiona automáticamente:
- Al iniciar la aplicación, se cargan los datos existentes
- Cada nueva inscripción se guarda inmediatamente
- Los datos persisten entre sesiones, tanto en modo consola como GUI