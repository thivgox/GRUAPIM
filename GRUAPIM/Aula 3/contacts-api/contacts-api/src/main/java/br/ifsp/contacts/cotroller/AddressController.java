package br.ifsp.contacts.controller;

import br.ifsp.contacts.dto.AddressRequestDTO;
import br.ifsp.contacts.dto.AddressResponseDTO;
import br.ifsp.contacts.model.Address;
import br.ifsp.contacts.model.Contact;
import br.ifsp.contacts.repository.AddressRepository;
import br.ifsp.contacts.repository.ContactRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contacts/{contactId}/addresses")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    //POST ---
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponseDTO addAddress(@PathVariable Long contactId, @Valid @RequestBody AddressRequestDTO request) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado"));


        Address address = new Address();
        address.setRua(request.getRua());
        address.setCidade(request.getCidade());
        address.setEstado(request.getEstado());
        address.setCep(request.getCep());
        address.setContact(contact);


        Address savedAddress = addressRepository.save(address);


        return convertToResponseDTO(savedAddress);
    }

    //   GET ---
    @GetMapping
    public List<AddressResponseDTO> getAddressesByContact(@PathVariable Long contactId) {
         if (!contactRepository.existsById(contactId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado");
        }

        return addressRepository.findByContactId(contactId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    private AddressResponseDTO convertToResponseDTO(Address address) {
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setId(address.getId());
        dto.setRua(address.getRua());
        dto.setCidade(address.getCidade());
        dto.setEstado(address.getEstado());
        dto.setCep(address.getCep());
        return dto;
    }
}