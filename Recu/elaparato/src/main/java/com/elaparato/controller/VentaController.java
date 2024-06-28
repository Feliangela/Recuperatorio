package com.elaparato.controller;

import com.elaparato.model.Venta;
import com.elaparato.repository.IProductoRepository;
import com.elaparato.service.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VentaController {

    @Autowired
    private IVentaService ventServ;

    @Autowired
    private IProductoRepository productoRepository;

    @PostMapping("/ventas/create")
    public Venta createVenta(@RequestBody Venta nuevaVenta) {
        return ventServ.saveVenta(nuevaVenta);
    }

    @GetMapping("/ventas/getall")
    public List<Venta> getVentas() {
        return ventServ.getVentas();
    }

    @GetMapping("/ventas/{id}")
    public Venta getVentaById(@PathVariable Integer id) {
        Venta venta = ventServ.findVenta(id);
        return venta;
    }

    @PutMapping("/ventas/edit/{id}")
    public Venta editVenta(@PathVariable Integer id, @RequestBody Venta vent) {
        ventServ.editVenta(id, vent);
        return vent;
    }

    @DeleteMapping("/ventas/{id}")
    public String deleteVenta(@PathVariable Integer id) {
        ventServ.deleteVenta(id);
        return "Venta eliminada correctamente";
    }

}