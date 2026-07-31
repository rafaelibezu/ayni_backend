package com.ayni.backend.order.service;

import com.ayni.backend.common.exception.ResourceNotFoundException;
import com.ayni.backend.common.util.GlobalUtil;
import com.ayni.backend.common.util.SecurityUtil;
import com.ayni.backend.order.dto.AddressRequest;
import com.ayni.backend.order.dto.AddressResponse;
import com.ayni.backend.order.entity.Address;
import com.ayni.backend.order.repository.AddressRepository;
import com.ayni.backend.user.entity.User;
import com.ayni.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public List<AddressResponse> findMyAddresses() {
        String email = SecurityUtil.getCurrentUserEmail();

        return addressRepository.findByUserEmailOrderByEsPrincipalDescIdDesc(email)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public AddressResponse create(AddressRequest request) {
        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        boolean shouldBeMain = Boolean.TRUE.equals(request.esPrincipal())
                || addressRepository.findByUserEmailAndEsPrincipalTrue(email).isEmpty();

        if (shouldBeMain) {
            clearMainAddress(email);
        }

        Address address = Address.builder()
                .user(user)
                .destinatario(GlobalUtil.cleanText(request.destinatario()))
                .celular(GlobalUtil.cleanText(request.celular()))
                .documentoTipo(GlobalUtil.cleanText(request.documentoTipo()))
                .documentoNumero(GlobalUtil.cleanText(request.documentoNumero()))
                .direccion(GlobalUtil.cleanText(request.direccion()))
                .distrito(GlobalUtil.cleanText(request.distrito()))
                .referencia(GlobalUtil.cleanText(request.referencia()))
                .esPrincipal(shouldBeMain)
                .creadoEn(LocalDateTime.now())
                .build();

        return toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional
    public AddressResponse update(Long addressId, AddressRequest request) {
        String email = SecurityUtil.getCurrentUserEmail();

        Address address = addressRepository.findByIdAndUserEmail(addressId, email)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada."));

        if (Boolean.TRUE.equals(request.esPrincipal())) {
            clearMainAddress(email);
            address.setEsPrincipal(true);
        }

        address.setDestinatario(GlobalUtil.cleanText(request.destinatario()));
        address.setCelular(GlobalUtil.cleanText(request.celular()));
        address.setDocumentoTipo(GlobalUtil.cleanText(request.documentoTipo()));
        address.setDocumentoNumero(GlobalUtil.cleanText(request.documentoNumero()));
        address.setDireccion(GlobalUtil.cleanText(request.direccion()));
        address.setDistrito(GlobalUtil.cleanText(request.distrito()));
        address.setReferencia(GlobalUtil.cleanText(request.referencia()));
        address.setActualizadoEn(LocalDateTime.now());

        return toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional
    public void delete(Long addressId) {
        String email = SecurityUtil.getCurrentUserEmail();

        Address address = addressRepository.findByIdAndUserEmail(addressId, email)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada."));

        addressRepository.delete(address);
    }

    @Override
    @Transactional
    public AddressResponse setMain(Long addressId) {
        String email = SecurityUtil.getCurrentUserEmail();

        Address address = addressRepository.findByIdAndUserEmail(addressId, email)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada."));

        clearMainAddress(email);

        address.setEsPrincipal(true);
        address.setActualizadoEn(LocalDateTime.now());

        return toResponse(addressRepository.save(address));
    }

    private void clearMainAddress(String email) {
        addressRepository.findByUserEmailOrderByEsPrincipalDescIdDesc(email)
                .forEach(address -> {
                    if (Boolean.TRUE.equals(address.getEsPrincipal())) {
                        address.setEsPrincipal(false);
                        address.setActualizadoEn(LocalDateTime.now());
                        addressRepository.save(address);
                    }
                });
    }

    private AddressResponse toResponse(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getDestinatario(),
                address.getCelular(),
                address.getDocumentoTipo(),
                address.getDocumentoNumero(),
                address.getDireccion(),
                address.getDistrito(),
                address.getReferencia(),
                address.getEsPrincipal(),
                address.getCreadoEn()
        );
    }
}