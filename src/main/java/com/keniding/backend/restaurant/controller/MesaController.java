package com.keniding.backend.restaurant.controller;

import com.keniding.backend.restaurant.dto.request.ActualizarPosicionMesaRequest;
import com.keniding.backend.restaurant.dto.request.CambiarEstadoMesaRequest;
import com.keniding.backend.restaurant.dto.request.CrearMesaRequest;
import com.keniding.backend.restaurant.dto.response.ApiResponse;
import com.keniding.backend.restaurant.dto.response.MesaResponse;
import com.keniding.backend.restaurant.dto.response.ResumenMesasResponse;
import com.keniding.backend.restaurant.model.HistorialMesa;
import com.keniding.backend.restaurant.service.MesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin("*")
@RequiredArgsConstructor
public class MesaController {

    private final MesaService mesaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MesaResponse>>> obtenerTodasLasMesas() {
        List<MesaResponse> mesas = mesaService.obtenerTodasLasMesas();
        return ResponseEntity.ok(new ApiResponse<>(true, "Mesas obtenidas con éxito", mesas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MesaResponse>> obtenerMesaPorId(@PathVariable Long id) {
        MesaResponse mesa = mesaService.obtenerMesaPorId(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mesa obtenida con éxito", mesa));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<MesaResponse>>> obtenerMesasPorEstado(@PathVariable String estado) {
        List<MesaResponse> mesas = mesaService.obtenerMesasPorEstado(estado);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mesas obtenidas con éxito", mesas));
    }

    @GetMapping("/ubicacion/{ubicacion}")
    public ResponseEntity<ApiResponse<List<MesaResponse>>> obtenerMesasPorUbicacion(@PathVariable String ubicacion) {
        List<MesaResponse> mesas = mesaService.obtenerMesasPorUbicacion(ubicacion);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mesas obtenidas con éxito", mesas));
    }

    @GetMapping("/zona/{codigoZona}")
    public ResponseEntity<ApiResponse<List<MesaResponse>>> obtenerMesasPorZona(@PathVariable String codigoZona) {
        List<MesaResponse> mesas = mesaService.obtenerMesasPorZona(codigoZona);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mesas obtenidas con éxito", mesas));
    }

    @GetMapping("/filtradas")
    public ResponseEntity<ApiResponse<List<MesaResponse>>> obtenerMesasFiltradas(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer capacidad) {
        List<MesaResponse> mesas = mesaService.obtenerMesasFiltradas(estado, capacidad);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mesas filtradas obtenidas con éxito", mesas));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MesaResponse>> crearMesa(@RequestBody CrearMesaRequest request) {
        MesaResponse mesa = mesaService.crearMesa(request);
        return new ResponseEntity<>(new ApiResponse<>(true, "Mesa creada con éxito", mesa), HttpStatus.CREATED);
    }

    @PutMapping("/estado")
    public ResponseEntity<ApiResponse<MesaResponse>> cambiarEstadoMesa(@RequestBody CambiarEstadoMesaRequest request) {
        MesaResponse mesa = mesaService.cambiarEstadoMesa(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Estado de mesa actualizado con éxito", mesa));
    }

    @PutMapping("/posicion")
    public ResponseEntity<ApiResponse<MesaResponse>> actualizarPosicionMesa(@RequestBody ActualizarPosicionMesaRequest request) {
        MesaResponse mesa = mesaService.actualizarPosicionMesa(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Posición de mesa actualizada con éxito", mesa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarMesa(@PathVariable Long id) {
        mesaService.eliminarMesa(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mesa eliminada con éxito", null));
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<ApiResponse<List<HistorialMesa>>> obtenerHistorialMesa(@PathVariable Long id) {
        List<HistorialMesa> historial = mesaService.obtenerHistorialMesa(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Historial de mesa obtenido con éxito", historial));
    }

    @GetMapping("/resumen")
    public ResponseEntity<ApiResponse<ResumenMesasResponse>> obtenerResumenMesas() {
        ResumenMesasResponse resumen = mesaService.obtenerResumenMesas();
        return ResponseEntity.ok(new ApiResponse<>(true, "Resumen de mesas obtenido con éxito", resumen));
    }
}
