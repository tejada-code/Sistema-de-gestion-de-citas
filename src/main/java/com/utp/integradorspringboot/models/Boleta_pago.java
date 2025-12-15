package com.utp.integradorspringboot.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "boleta_pago")
public class Boleta_pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_detalle_cita", nullable = false, unique = true)
    private Detalle_cita detalle_cita;

    @Column(name = "monto_total", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double monto_total;

    @Column(name = "metodo_pago", nullable = false)
    private String metodo_pago;

    @Column(name = "fecha_emision")
    private LocalDateTime fecha_emision;

    public Boleta_pago() {}

    public Boleta_pago(Long id, Detalle_cita detalle_cita, Double monto_total, String metodo_pago, LocalDateTime fecha_emision) {
        this.id = id;
        this.detalle_cita = detalle_cita;
        this.monto_total = monto_total;
        this.metodo_pago = metodo_pago;
        this.fecha_emision = fecha_emision;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Detalle_cita getDetalle_cita() {
        return detalle_cita;
    }

    public void setDetalle_cita(Detalle_cita detalle_cita) {
        this.detalle_cita = detalle_cita;
    }

    public Double getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(Double monto_total) {
        this.monto_total = monto_total;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public LocalDateTime getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(LocalDateTime fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Boleta_pago)) {
            return false;
        }
        Boleta_pago other = (Boleta_pago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Boleta_pago{" + "id=" + id + ", detalle_cita=" + (detalle_cita != null ? detalle_cita.getId() : "null") + ", monto_total=" + monto_total + ", metodo_pago=" + metodo_pago + ", fecha_emision=" + fecha_emision + '}';
    }
} 