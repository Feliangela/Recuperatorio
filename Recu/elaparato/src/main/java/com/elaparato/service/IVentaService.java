package com.elaparato.service;

import com.elaparato.model.Venta;

import java.util.List;

public interface IVentaService {

    public List<Venta> getVentas();

    public Venta saveVenta(Venta vent);

    public void deleteVenta(int id);

    public Venta findVenta(int id);

    public void editVenta(int id, Venta newData);

}
