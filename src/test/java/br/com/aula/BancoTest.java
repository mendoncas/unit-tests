package br.com.aula;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import br.com.aula.exception.*;
import org.junit.Assert;
import org.junit.Test;

public class BancoTest {

	@Test
	public void deveCadastrarConta() throws ContaJaExistenteException, NumeroDeContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
		assertEquals(1, banco.obterContas().size());
	}

	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaNumeroRepetido() throws ContaJaExistenteException, NumeroDeContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}

	@Test(expected = NumeroDeContaInvalidoException.class)
	public void naoDeveCadastrarContaNumeroInvalido() throws NumeroDeContaInvalidoException, ContaJaExistenteException {
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, -2, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(contaOrigem);
		Assert.fail();
	}

	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadasrarNomeExistente() throws ContaJaExistenteException, NumeroDeContaInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Maria");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 321, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}

//	antes de realizar transferência, checa se origem e destino existem
	@Test(expected = ContaNaoExistenteException.class)
	public void checaExistenciaDasContas() throws ContaNaoExistenteException, ContaSemSaldoException, ValorNegativoException {

		Banco banco = new Banco();
		banco.efetuarTransferencia(123, 321, 5000);
	}

//	antes de realizar transferencia, checa saldo suficiente
	@Test(expected = ContaSemSaldoException.class)
	public void checaSaldoParaTransferencia() throws ContaNaoExistenteException, ContaSemSaldoException, NumeroDeContaInvalidoException, ContaJaExistenteException, ValorNegativoException {
		// Cenario
		Cliente cliente = new Cliente("Maria");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Joqo");
		Conta conta2 = new Conta(cliente2, 321, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		banco.efetuarTransferencia(321, 123, 9000);
		Assert.fail();
	}

	@Test(expected = ValorNegativoException.class)
	public void checaValorNegativo() throws ContaSemSaldoException, ContaNaoExistenteException, ValorNegativoException {
		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, -100);

		Assert.fail();
	}
	@Test
	public void deveEfetuarTransferenciaContasCorrentes() throws ContaSemSaldoException, ContaNaoExistenteException, ValorNegativoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(-100, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}
}
