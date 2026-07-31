package com.ayni.backend.dashboard.dto;

import java.math.BigDecimal;

public record AdminDashboardSummaryResponse(
        long totalUsuarios,
        long totalProductos,
        long totalCategorias,
        long totalPedidos,
        long pedidosPendientes,
        long pagosEnRevision,
        BigDecimal ventasValidadas
) {
}