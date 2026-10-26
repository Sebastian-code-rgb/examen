package ec.edu.taller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class GestorIncidentes {

    private Queue<IncidenteSeguridad> colaIncidentes;
    private int capacidadMaxima;

    public GestorIncidentes(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;

        this.colaIncidentes = new PriorityQueue<>(
                Comparator.comparingInt(
                        IncidenteSeguridad::getPrioridad
                )
        );
    }

    public boolean registrarIncidente(IncidenteSeguridad incidente) {

        if (incidente == null) {
            return false;
        }

        String codigo = incidente.getCodigo();

        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }

        if (existeIncidente(codigo)) {
            return false;
        }

        if (colaIncidentes.size() >= capacidadMaxima) {
            return false;
        }

        colaIncidentes.offer(incidente);
        return true;
    }

    public boolean existeIncidente(String codigo) {

        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }

        for (IncidenteSeguridad incidente : colaIncidentes) {
            if (codigo.equals(incidente.getCodigo())) {
                return true;
            }
        }

        return false;
    }

    public IncidenteSeguridad consultarSiguienteIncidente() {
        return colaIncidentes.peek();
    }

    public IncidenteSeguridad atenderSiguienteIncidente() {

        IncidenteSeguridad incidente = colaIncidentes.poll();

        if (incidente != null) {
            incidente.setEstado("ATENDIDO");
        }

        return incidente;
    }

    public int contarIncidentesPendientes() {
        return colaIncidentes.size();
    }

    public int consultarEspaciosDisponibles() {
        return capacidadMaxima - colaIncidentes.size();
    }

    public List<IncidenteSeguridad> listarIncidentes() {

        PriorityQueue<IncidenteSeguridad> copia =
                new PriorityQueue<>(
                        Comparator.comparingInt(
                                IncidenteSeguridad::getPrioridad
                        )
                );

        copia.addAll(colaIncidentes);

        List<IncidenteSeguridad> lista = new ArrayList<>();

        while (!copia.isEmpty()) {
            lista.add(copia.poll());
        }

        return lista;
    }
}
