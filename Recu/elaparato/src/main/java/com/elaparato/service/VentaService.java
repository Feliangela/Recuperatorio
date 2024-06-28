package com.elaparato.service;

import com.elaparato.model.Producto;
import com.elaparato.model.Venta;
import com.elaparato.repository.IProductoRepository;
import com.elaparato.repository.IVentaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaService implements IVentaService{

    @Autowired
    private IVentaRepository ventaRepo;

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private EntityManager em;

    @Override
    public List<Venta> getVentas() {
        return ventaRepo.findAll();
    }

    @Transactional
    public Venta saveVenta(Venta nuevaVenta) {
        if (!nuevaVenta.getListaProductos().isEmpty()) {

            List<Integer> productoIds = nuevaVenta.getListaProductos().stream()
                    .map(Producto::getId)
                    .collect(Collectors.toList());

            List<Producto> productos = productoRepository.findAllById(productoIds);

            nuevaVenta.setListaProductos(productos);

            productos.forEach(producto -> producto.getListaVentas().add(nuevaVenta));
        }

        return ventaRepo.save(nuevaVenta);
    }

    @Transactional
    @Override
    public void deleteVenta(int id) {
        Venta venta = ventaRepo.findById(id).orElse(null);
        if (venta != null) {
            venta.getListaProductos().forEach(producto -> producto.getListaVentas().remove(venta));
            venta.getListaProductos().clear();
            venta.getListaProductos().forEach(productoRepository::save);
            ventaRepo.delete(venta);
        }
    }

    @Override
    public Venta findVenta(int id) {
       return ventaRepo.findById(id).orElse(null);
    }

    @Transactional
    public void editVenta(int id, Venta newData) {
        Venta venta = em.find(Venta.class, id);
        if (venta != null) {
            venta.setFecha(newData.getFecha());
            venta.setListaProductos(newData.getListaProductos());
            em.merge(venta);
        }
    }

}
