package mx.unison;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ProcesadorCalificaciones {

    private static final Logger LOG =
            Logger.getLogger(ProcesadorCalificaciones.class.getName());

    public static void main(String[] args) {

        configurarLogger();

        LOG.info("Inicio de la aplicacion ProcesadorCalificaciones");

        if (args.length == 0) {
            LOG.severe("No se especifico archivo de entrada. Uso: java ProcesadorCalificaciones <archivo>");
            return;
        }

        String nombreArchivo = args[0];

        LOG.config("Archivo configurado como entrada: " + nombreArchivo);

        procesarArchivo(nombreArchivo);

        LOG.info("Fin del procesamiento");
    }

    private static void procesarArchivo(String nombreArchivo) {

        int registros = 0;
        int validos = 0;
        int invalidos = 0;
        double suma = 0;

        try (BufferedReader lector =
                     new BufferedReader(new FileReader(nombreArchivo))) {

            LOG.fine("Archivo abierto correctamente: " + nombreArchivo);

            String linea;

            while ((linea = lector.readLine()) != null) {

                registros++;

                LOG.finer("Procesando linea " + registros + ": " + linea);

                try {

                    int calificacion = Integer.parseInt(linea.trim());

                    if (calificacion < 0 || calificacion > 100) {
                        invalidos++;
                        LOG.warning("Valor fuera de rango en la linea " + registros + ": " + calificacion);
                        continue;
                    }

                    validos++;
                    suma += calificacion;

                    LOG.fine("Calificacion valida: " + calificacion);

                } catch (NumberFormatException e) {
                    invalidos++;
                    LOG.warning("Dato no numerico en la linea " + registros + ": " + linea);
                }
            }

            LOG.info("Registros procesados: " + registros);
            LOG.info("Registros validos: " + validos);
            LOG.info("Registros invalidos: " + invalidos);

            if (validos > 0) {
                double promedio = suma / validos;
                LOG.info("Promedio calculado: " + promedio);
            } else {
                LOG.warning("No se calculo promedio: no hubo calificaciones validas");
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error al procesar el archivo " + nombreArchivo, e);
        }
    }

    private static void configurarLogger() {

        try {

            LOG.setUseParentHandlers(false);
            LOG.setLevel(Level.ALL);

            ConsoleHandler consola = new ConsoleHandler();
            consola.setLevel(Level.INFO);
            LOG.addHandler(consola);

            FileHandler archivo = new FileHandler("procesador.log", true);
            archivo.setLevel(Level.ALL);
            archivo.setFormatter(new SimpleFormatter());
            LOG.addHandler(archivo);

        } catch (IOException e) {
            System.err.println("Error configurando Logger: " + e.getMessage());
        }
    }
}
