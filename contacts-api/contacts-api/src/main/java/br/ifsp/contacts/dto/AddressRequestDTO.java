package br.ifsp.contacts.dto;

import jakarta.validation.constraints.NotBlank;

public class AddressRequestDTO {

    @NotBlank(message = "A rua não pode estar vazia")
    private String rua;

    @NotBlank(message = "A cidade não pode estar vazia")
    private String cidade;

    @NotBlank(message = "O estado não pode estar vazio")
    private String estado;

    @NotBlank(message = "O CEP não pode estar vazio")
    private String cep;

    // --- GETTERS E SETTERS ---
    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
}