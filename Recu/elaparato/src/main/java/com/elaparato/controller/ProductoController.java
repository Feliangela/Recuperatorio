package com.elaparato.controller;
import com.elaparato.model.Producto;
import com.elaparato.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductoController {

    @Autowired
    private IProductoService prodServ;


    @PostMapping("/productos/create")
    public String createProducto(@RequestBody Producto prod) {
        prodServ.saveProducto(prod);
        return "Producto creado correctamente";
    }

    @GetMapping("/productos/getall")
    public List<Producto> getProductos () {
        return prodServ.getProductos();
    }

    @GetMapping("/productos/{id}")
    public Producto getProductById(@PathVariable Integer id){
        Producto producto = prodServ.findProducto(id);
        return producto;
    }

    @PutMapping("/productos/edit/{id}")
    public String editProducto(@PathVariable Integer id, @RequestBody Producto prod) {
        prodServ.editProducto(prod);
        return "Producto editado correctamente";
    }

    @DeleteMapping("/productos/{id}")
    public String deleteProducto(@PathVariable Integer id){
        prodServ.deleteProducto(id);
        return "Producto eliminado correctamente";
    }
}
