package com.ayni.backend.order.service;

import com.ayni.backend.order.dto.AddressRequest;
import com.ayni.backend.order.dto.AddressResponse;

import java.util.List;

public interface AddressService {

    List<AddressResponse> findMyAddresses();

    AddressResponse create(AddressRequest request);

    AddressResponse update(Long addressId, AddressRequest request);

    void delete(Long addressId);

    AddressResponse setMain(Long addressId);
}