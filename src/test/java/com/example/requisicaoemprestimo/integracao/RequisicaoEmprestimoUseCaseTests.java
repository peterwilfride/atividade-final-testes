package com.example.requisicaoemprestimo.integracao;

import com.example.requisicaoemprestimo.domain.models.Emprestimo;
import com.example.requisicaoemprestimo.domain.models.ResultadoAnalise;
import com.example.requisicaoemprestimo.domain.models.ResultadoTesouraria;
import com.example.requisicaoemprestimo.domain.ports.IAnaliseProxy;
import com.example.requisicaoemprestimo.domain.ports.ITesourariaProxy;
import com.example.requisicaoemprestimo.domain.usecases.RequisicaoEmprestimoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

//TODO: Use o mokito para realizar testes TOP-DOWN para criar dublês das interfaces IAnaliseProxy e ITesourariaProxy
public class RequisicaoEmprestimoUseCaseTests {

    //TODO: Setup das classes Mocks e Instância real da classe RequisicaoEmprestimoUseCase
    RequisicaoEmprestimoUseCase requisicaoEmprestimoUseCase;
    IAnaliseProxy analiseProxy;
    ITesourariaProxy tesourariaProxy;

    ResultadoAnalise x;

    @BeforeEach
    public void Setup() {
        analiseProxy = mock(IAnaliseProxy.class);
        tesourariaProxy = mock(ITesourariaProxy.class);
        requisicaoEmprestimoUseCase = new RequisicaoEmprestimoUseCase(analiseProxy, tesourariaProxy);

        when(analiseProxy.solicitarAnaliseDeCredito(any(Emprestimo.class))).thenReturn(new ResultadoAnalise());
        when(tesourariaProxy.solicitarLiberacaoDaTesouraria(any(Emprestimo.class))).thenReturn(new ResultadoTesouraria());
    }


    @Test
    public void test1(){
        //TODO Fazer um teste caminho Feliz (TUDO FUNCIONA BEM)
        Emprestimo emprestimo = requisicaoEmprestimoUseCase.executar(UUID.randomUUID(), 100, 12);

        ResultadoAnalise resultadoAnalise = analiseProxy.solicitarAnaliseDeCredito(emprestimo);
        ResultadoTesouraria resultadoTesouraria = tesourariaProxy.solicitarLiberacaoDaTesouraria(emprestimo);

        resultadoAnalise.setAprovado(true);
        resultadoTesouraria.setAprovado(true);

        assertTrue(resultadoAnalise.isAprovado());
        assertTrue(resultadoTesouraria.isAprovado());
    }

    @Test
    public void test2(){
        //TODO Fazer um teste caminho INFELIZ IAnaliseProxy retornando uma Análise reprovada
        Emprestimo emprestimo = requisicaoEmprestimoUseCase.executar(UUID.randomUUID(), 100, 12);

        ResultadoAnalise resultadoAnalise = analiseProxy.solicitarAnaliseDeCredito(emprestimo);

        resultadoAnalise.setAprovado(false);

        assertFalse(resultadoAnalise.isAprovado());
    }

    @Test
    public void test3(){
        //TODO Fazer um teste caminho INFELIZ ITesourariaProxy retornando resultado reprovado
        Emprestimo emprestimo = requisicaoEmprestimoUseCase.executar(UUID.randomUUID(), 100, 12);

        ResultadoTesouraria resultadoTesouraria = tesourariaProxy.solicitarLiberacaoDaTesouraria(emprestimo);

        resultadoTesouraria.setAprovado(false);

        assertFalse(resultadoTesouraria.isAprovado());
    }
}
