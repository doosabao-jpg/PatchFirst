package proyecto.modelo;

public class Incidente {
    private int idIncidente;
    private String fecha;
    private String hora;
    private String ipOrigen;
    private int idDispositivo;
    private int idTipoAtaque;
    private int idTecnico;
    private int valorIndicador;
    private int probabilidad;
    private int impacto;
    private int nivelRiesgo;
    private String criticidad;
    private String estado;
    private int tiempoRespuestaMin;

    public Incidente() {
    }

    public Incidente(int idIncidente, String fecha, String hora, String ipOrigen, int idDispositivo, int idTipoAtaque, int idTecnico, int valorIndicador, int probabilidad, int impacto, String estado, int tiempoRespuestaMin) {
        this.idIncidente = idIncidente;
        this.fecha = fecha;
        this.hora = hora;
        this.ipOrigen = ipOrigen;
        this.idDispositivo = idDispositivo;
        this.idTipoAtaque = idTipoAtaque;
        this.idTecnico = idTecnico;
        this.valorIndicador = valorIndicador;
        this.probabilidad = probabilidad;
        this.impacto = impacto;
        this.estado = estado;
        this.tiempoRespuestaMin = tiempoRespuestaMin;
        this.nivelRiesgo = calcularNivelRiesgo();
        this.criticidad = calcularCriticidad();
    }

    public int calcularNivelRiesgo() {
        return probabilidad * impacto;
    }

    public String calcularCriticidad() {
        int nivel = calcularNivelRiesgo();
        if (nivel >= 15) {
            return "Alta";
        } else {
            if (nivel >= 8) {
                return "Media";
            } else {
                return "Baja";
            }
        }
    }

    public int getIdIncidente() {
        return idIncidente;
    }

    public void setIdIncidente(int idIncidente) {
        this.idIncidente = idIncidente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getIpOrigen() {
        return ipOrigen;
    }

    public void setIpOrigen(String ipOrigen) {
        this.ipOrigen = ipOrigen;
    }

    public int getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(int idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public int getIdTipoAtaque() {
        return idTipoAtaque;
    }

    public void setIdTipoAtaque(int idTipoAtaque) {
        this.idTipoAtaque = idTipoAtaque;
    }

    public int getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(int idTecnico) {
        this.idTecnico = idTecnico;
    }

    public int getValorIndicador() {
        return valorIndicador;
    }

    public void setValorIndicador(int valorIndicador) {
        this.valorIndicador = valorIndicador;
    }

    public int getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(int probabilidad) {
        this.probabilidad = probabilidad;
    }

    public int getImpacto() {
        return impacto;
    }

    public void setImpacto(int impacto) {
        this.impacto = impacto;
    }

    public int getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(int nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public String getCriticidad() {
        return criticidad;
    }

    public void setCriticidad(String criticidad) {
        this.criticidad = criticidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getTiempoRespuestaMin() {
        return tiempoRespuestaMin;
    }

    public void setTiempoRespuestaMin(int tiempoRespuestaMin) {
        this.tiempoRespuestaMin = tiempoRespuestaMin;
    }
    
}