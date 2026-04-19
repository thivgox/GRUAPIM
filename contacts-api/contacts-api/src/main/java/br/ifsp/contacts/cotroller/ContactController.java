package br.ifsp.contacts.controller;

import br.ifsp.contacts.dto.ContactRequestDTO;
import br.ifsp.contacts.dto.ContactResponseDTO;
import br.ifsp.contacts.dto.AddressResponseDTO;
import br.ifsp.contacts.model.Contact;
import br.ifsp.contacts.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    // POST ---
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactResponseDTO createContact(@Valid @RequestBody ContactRequestDTO request) {

        Contact contact = new Contact(request.getNome(), request.getTelefone(), request.getEmail());


         Contact savedContact = contactRepository.save(contact);

         return convertToResponseDTO(savedContact);
    }

    //   GET ---
    @GetMapping("/search")
    public List<ContactResponseDTO> searchContactsByName(@RequestParam String name) {
         return contactRepository.findByNomeContainingIgnoreCase(name)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    //  PATCH ---
    @PatchMapping("/{id}")
    public ContactResponseDTO updateContactParcial(@PathVariable Long id, @RequestBody ContactRequestDTO atualizacao) {

        Contact contatoExistente = contactRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado"));

        Optional.ofNullable(atualizacao.getNome()).ifPresent(contatoExistente::setNome);
        Optional.ofNullable(atualizacao.getTelefone()).ifPresent(contatoExistente::setTelefone);
        Optional.ofNullable(atualizacao.getEmail()).ifPresent(contatoExistente::setEmail);

        Contact contatoAtualizado = contactRepository.save(contatoExistente);

        return convertToResponseDTO(contatoAtualizado);
    }



    private ContactResponseDTO convertToResponseDTO(Contact contact) {
        ContactResponseDTO dto = new ContactResponseDTO();
        dto.setId(contact.getId());
        dto.setNome(contact.getNome());
        dto.setTelefone(contact.getTelefone());
        dto.setEmail(contact.getEmail());

        if (contact.getAddresses() != null) {
            List<AddressResponseDTO> addressDTOs = contact.getAddresses().stream().map(address -> {
                AddressResponseDTO addrDto = new AddressResponseDTO();
                addrDto.setId(address.getId());
                addrDto.setRua(address.getRua());
                addrDto.setCidade(address.getCidade());
                addrDto.setEstado(address.getEstado());
                addrDto.setCep(address.getCep());
                return addrDto;
            }).collect(Collectors.toList());

            dto.setAddresses(addressDTOs);
        }

        return dto;
    }
}