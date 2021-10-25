package br.com.aula;

import java.util.ArrayList;
import java.util.List;

import br.com.aula.exception.*;

public class Banco {

	private List<Conta> contas = new ArrayList<Conta>();

	public Banco() {
	}

	public Banco(List<Conta> contas) {
		this.contas = contas;
	}


	public void cadastrarConta(Conta conta) throws ContaJaExistenteException, NumeroDeContaInvalidoException {
		if (conta.getNumeroConta() < 0) {
			throw new NumeroDeContaInvalidoException();
		}

		for (Conta c : contas) {
			boolean isNomeClienteIgual = c.getCliente().getNome().equals(conta.getCliente().getNome());
			boolean isNumeroContaIgual = c.getNumeroConta() == conta.getNumeroConta();

			if (isNomeClienteIgual || isNumeroContaIgual) {
				throw new ContaJaExistenteException();
			}

		}
//		throw new NumeroDeContaInvalidoException();
		this.contas.add(conta);

	}

	public void efetuarTransferencia(int numeroContaOrigem, int numeroContaDestino, int valor)
			throws ContaNaoExistenteException, ContaSemSaldoException, ValorNegativoException {

		Conta contaOrigem = this.obterContaPorNumero(numeroContaOrigem);
		Conta contaDestino = this.obterContaPorNumero(numeroContaDestino);


		boolean isContaOrigemExistente = contaOrigem != null;
		boolean isContaDestinoExistente = contaDestino != null;

		if (valor < 0)
			throw new ValorNegativoException();
		if (isContaOrigemExistente && isContaDestinoExistente) {

			boolean isContaOrigemPoupança = contaOrigem.getTipoConta().equals(TipoConta.POUPANCA);
			boolean isSaldoContaOrigemNegativo = contaOrigem.getSaldo() - valor < 0;

			if (isContaOrigemPoupança && isSaldoContaOrigemNegativo) {
				throw new ContaSemSaldoException();
			}

			contaOrigem.debitar(valor);
			contaDestino.creditar(valor);

		} else {
			throw new ContaNaoExistenteException();
		}
	}

	public Conta obterContaPorNumero(int numeroConta) {

		for (Conta c : contas) {
			if (c.getNumeroConta() == numeroConta) {
				return c;
			}
		}

		return null;
	}

	public List<Conta> obterContas() {
		return this.contas;
	}
}
