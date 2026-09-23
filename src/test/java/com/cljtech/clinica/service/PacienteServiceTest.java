package com.cljtech.clinica.service;

import com.cljtech.clinica.data.Paciente;
import com.cljtech.clinica.data.repository.PacienteRepository;
import com.cljtech.clinica.exception.RecursoNaoEncontradoException;
import com.cljtech.clinica.exception.RegraNegocioException;
import com.cljtech.clinica.mapper.EntityMapper;
import com.cljtech.clinica.model.records.PacienteRequestResponse;
import com.cljtech.clinica.model.records.PacienteResumoResponse;
import com.cljtech.clinica.service.impl.PacienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private PacienteServiceImpl pacienteService;

    private PacienteRequestResponse request;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        request = new PacienteRequestResponse(
                null, "João Silva", "12345678901", "joao@email.com",
                "11999999999", LocalDate.of(1990, 1, 1), null,
                null, null, null
        );
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("João Silva");
    }

    @Test
    void criarPacienteComSucesso() {
        when(pacienteRepository.existsByCpf(request.cpf())).thenReturn(false);
        when(pacienteRepository.existsByEmail(request.email())).thenReturn(false);
        when(entityMapper.toPaciente(request)).thenReturn(paciente);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);
        when(entityMapper.toPacienteRequestResponse(paciente)).thenReturn(request);

        PacienteRequestResponse result = pacienteService.salvar(request);

        assertNotNull(result);
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void buscarPacienteInexistenteDeveLancarExcecao() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> pacienteService.buscar(1L));
    }

    @Test
    void atualizarPacienteComSucesso() {
        when(pacienteRepository.existsById(1L)).thenReturn(true);
        when(pacienteRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(org.springframework.data.domain.Page.empty());
        when(entityMapper.toPaciente(request)).thenReturn(paciente);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);
        when(entityMapper.toPacienteRequestResponse(paciente)).thenReturn(request);

        PacienteRequestResponse result = pacienteService.atualizar(1L, request);

        assertNotNull(result);
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void deletarPacienteComSucesso() {
        when(pacienteRepository.existsById(1L)).thenReturn(true);

        pacienteService.deletar(1L);

        verify(pacienteRepository).deleteById(1L);
    }

    @Test
    void buscarResumoPorCriteriosComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Paciente> page = new PageImpl<>(List.of(paciente));
        PacienteResumoResponse resumo = new PacienteResumoResponse(1L, "João Silva", "12345678901", "11999999999", "joao@email.com");

        when(pacienteRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(entityMapper.toPacienteResumoResponse(paciente)).thenReturn(resumo);

        Page<PacienteResumoResponse> result = pacienteService.buscarResumoPorCriterios("João", null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("João Silva", result.getContent().get(0).nome());
        verify(pacienteRepository).findAll(any(Specification.class), eq(pageable));
    }
}
