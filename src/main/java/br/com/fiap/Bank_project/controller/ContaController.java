 
package br.com.fiap.Bank_project.controller;
 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.Bank_project.model.*;;

@RestController
@RequestMapping("/contas")
public class ContaController {
 
    private Logger log = LoggerFactory.getLogger(getClass());
    private List<Conta> repository = new ArrayList<>();
 
    // Listar todas as contas
    @GetMapping
    public List<Conta> index() {
        return repository;
    }
 
    @GetMapping("/")
    public String participantes() {
        return "Projeto: Sistema de Contas Bancárias ||| Integrantes: Mariana Christina RM554773 e Gabriela Moguinho RM556143";
    }
 
    // Cadastro de Conta
    @PostMapping
    public ResponseEntity<Conta> criarConta(@RequestBody Conta conta) {
        if (conta.getNomeTitular() == null || conta.getNomeTitular().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do titular é obrigatório");
        }
        if (conta.getCpfTitular() == null || conta.getCpfTitular().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF do titular é obrigatório");
        }
        if (conta.getDataAbertura() != null && conta.getDataAbertura().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de abertura não pode ser no futuro");
        }
        if (conta.getSaldo() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo inicial não pode ser negativo");
        }
        if (!List.of("corrente", "poupança", "salário").contains(conta.getTipo().toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de conta inválido");
        }
 
        repository.add(conta);
        return ResponseEntity.status(201).body(conta);
    }
 
    // Buscar conta por ID
    @GetMapping("/{id}")
    public Conta buscarContaPorId(@PathVariable Long id) {
        log.info("Buscando conta " + id);
        return getConta(id);
    }
 
    // Buscar conta por CPF
    @GetMapping("/cpf/{cpf}")
    public Conta buscarContaPorCpf(@PathVariable String cpf) {
        log.info("Buscando conta por CPF " + cpf);
        return getContaCPF(cpf);
    }
 
    // Encerrar conta
    @DeleteMapping("/{id}/encerrar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void encerrarConta(@PathVariable Long id) {
        log.info("Encerrando conta " + id);

        Conta conta = getConta(id);

        conta.setAtiva(false);

        repository = repository.stream()
                .map(c -> c.getId().equals(id) ? conta : c)
                .toList();

        log.info("Conta " + id + " foi marcada como inativa.");
    }

    // Depósito
    @PutMapping("/{id}/depositar")
    public ResponseEntity<Conta> depositar(@PathVariable Long id, @RequestBody Double valor) {
        if (valor == null || valor <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor inválido para depósito");
        }
 
        Conta conta = getConta(id);
        conta.setSaldo(conta.getSaldo() + valor);
        return ResponseEntity.ok(conta);
    }
 
    // Saque
    @PutMapping("/{id}/sacar")
    public ResponseEntity<Conta> sacar(@PathVariable Long id, @RequestBody Double valor) {
        if (valor == null || valor <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor inválido para saque");
        }
 
        Conta conta = getConta(id);
        if (conta.getSaldo() < valor) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente");
        }
 
        conta.setSaldo(conta.getSaldo() - valor);
        return ResponseEntity.ok(conta);
    }
 
    // Transferência via PIX
    @PutMapping("/pix")
    public ResponseEntity<Conta> realizarPix(@RequestBody Map<String, Object> body) {
        try {
            Long origemId = ((Number) body.get("origemId")).longValue();
            Long destinoId = ((Number) body.get("destinoId")).longValue();
            Double valor = ((Number) body.get("valor")).doubleValue();
 
            if (valor == null || valor <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor do Pix deve ser maior que zero");
            }
 
            Conta origem = getConta(origemId);
            Conta destino = getConta(destinoId);
 
            if (origem.getSaldo() < valor) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para transferência");
            }
 
            origem.setSaldo(origem.getSaldo() - valor);
            destino.setSaldo(destino.getSaldo() + valor);
 
            return ResponseEntity.ok(origem);
        } catch (ClassCastException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos na requisição");
        }
    }
 
    // Buscar conta por ID
    private Conta getConta(Long id) {
        return repository.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta " + id + " não encontrada"));
    }
 
    // Buscar conta por CPF
    private Conta getContaCPF(String cpf) {
        return repository.stream()
                .filter(c -> c.getCpfTitular().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Conta com o CPF: " + cpf + " não encontrada"));
    }
}
 