 
package br.com.fiap.Bank_project.model;
 
import java.time.LocalDate;
import java.util.Random;
 
public class Conta {
 
    private Long id;
    private String numero;
    private String agencia;
    private String nomeTitular;
    private String cpfTitular;
    private LocalDate dataAbertura;
    private double saldo;
    private boolean ativa;
    private String tipo;
 
    public Conta(Long id, String numero, String agencia, String nomeTitular, String cpfTitular, LocalDate dataAbertura, double saldo, boolean ativa, String tipo) {
        this.id = Math.abs(new Random().nextLong());
        this.numero = numero;
        this.agencia = agencia;
        this.nomeTitular = nomeTitular;
        this.cpfTitular = cpfTitular;
        this.dataAbertura = dataAbertura;
        this.saldo = saldo;
        this.ativa = ativa;
        this.tipo = tipo;
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public String getNumero() {
        return numero;
    }
 
    public void setNumero(String numero) {
        this.numero = numero;
    }
 
    public String getAgencia() {
        return agencia;
    }
 
    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }
 
    public String getNomeTitular() {
        return nomeTitular;
    }
 
    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }
 
    public String getCpfTitular() {
        return cpfTitular;
    }
 
    public void setCpfTitular(String cpfTitular) {
        this.cpfTitular = cpfTitular;
    }
 
    public LocalDate getDataAbertura() {
        return dataAbertura;
    }
 
    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }
 
    public double getSaldo() {
        return saldo;
    }
 
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
 
    public boolean isAtiva() {
        return ativa;
    }
 
    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
 
    public String getTipo() {
        return tipo;
    }
 
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}