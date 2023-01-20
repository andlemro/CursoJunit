package org.adlemro.junit5app.ejemplos.models;

import java.math.BigDecimal;

import org.adlemro.junit5app.ejemplos.exceptions.DineroInsuficienteException;

public class Cuenta {

	private String persona;
	private BigDecimal saldo;
	private Banco banco;
	
	public Cuenta(String persona, BigDecimal saldo) {
		super();
		this.persona = persona;
		this.saldo = saldo;
	}
	
	public String getPersona() {
		return persona;
	}
	public void setPersona(String persona) {
		this.persona = persona;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public void debito(BigDecimal monto) {
		 BigDecimal nuevoSaldo = this.saldo.subtract(monto);
		 if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0 ) {
			 throw new DineroInsuficienteException("Dinero Insuficiente");
		 }
		 this.saldo = nuevoSaldo;
	}
	
	public void credito(BigDecimal monto) {
		this.saldo = this.saldo.add(monto);
	}

	@Override
	public String toString() {
		return "Cuenta [persona=" + persona + ", saldo=" + saldo + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		// Se valida que el objeto no sea nulo y que la instancia sea la misma
		if(!(obj instanceof Cuenta)) {
			return false;
		}
		Cuenta c = (Cuenta) obj;
		// Valida que los atributos no esten en nulos
		if (this.persona == null || this.saldo == null) {
			return false;
		}
		
		// Valida que los atributos sean los mismos
		return this.persona.equals(c.getPersona()) && this.saldo.equals(c.getSaldo());
	}
	
	
}
