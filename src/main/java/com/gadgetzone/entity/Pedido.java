package com.gadgetzone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    private Double total;

    private String estado; // Por ejemplo: PENDIENTE, ENVIADO, CANCELADO

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<LineaPedido> lineas;
}
