package com.gadgetzone.repository;

import com.gadgetzone.entity.Pedido;
import com.gadgetzone.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);

    List<Pedido> findByUsuario(Usuario usuario);


}
