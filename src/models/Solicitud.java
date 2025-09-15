package models;

public class Solicitud {
    private int idSol;
    private int emisorId;
    private int receptorId;
    private String estado;

    public Solicitud() {
    }

    public Solicitud(int idSol, int emisorId, int receptorId, String estado) {
        this.idSol = idSol;
        this.emisorId = emisorId;
        this.receptorId = receptorId;
        this.estado = estado;
    }

    public int getIdSol() {
        return idSol;
    }

    public void setIdSol(int idSol) {
        this.idSol = idSol;
    }

    public int getEmisorId() {
        return emisorId;
    }

    public void setEmisorId(int emisorId) {
        this.emisorId = emisorId;
    }

    public int getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(int receptorId) {
        this.receptorId = receptorId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
