package com.ayni.backend.order.controller;

import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import com.ayni.backend.order.dto.AddressRequest;
import com.ayni.backend.order.dto.AddressResponse;
import com.ayni.backend.order.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping(ApiRoutes.USERS + "/me/addresses")
    public ApiResponse<List<AddressResponse>> findMyAddresses() {
        return ApiResponse.ok(
                "Direcciones obtenidas correctamente.",
                addressService.findMyAddresses()
        );
    }

    @PostMapping(ApiRoutes.USERS + "/me/addresses")
    public ApiResponse<AddressResponse> create(
            @Valid @RequestBody AddressRequest request
    ) {
        return ApiResponse.ok(
                "Dirección creada correctamente.",
                addressService.create(request)
        );
    }

    @PutMapping(ApiRoutes.USERS + "/me/addresses/{addressId}")
    public ApiResponse<AddressResponse> update(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request
    ) {
        return ApiResponse.ok(
                "Dirección actualizada correctamente.",
                addressService.update(addressId, request)
        );
    }

    @DeleteMapping(ApiRoutes.USERS + "/me/addresses/{addressId}")
    public ApiResponse<Void> delete(
            @PathVariable Long addressId
    ) {
        addressService.delete(addressId);

        return ApiResponse.ok(
                "Dirección eliminada correctamente.",
                null
        );
    }

    @PatchMapping(ApiRoutes.USERS + "/me/addresses/{addressId}/main")
    public ApiResponse<AddressResponse> setMain(
            @PathVariable Long addressId
    ) {
        return ApiResponse.ok(
                "Dirección principal actualizada correctamente.",
                addressService.setMain(addressId)
        );
    }
}