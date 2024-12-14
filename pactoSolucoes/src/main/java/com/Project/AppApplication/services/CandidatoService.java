package com.Project.AppApplication.services;

import com.Project.AppApplication.domain.candidato.Candidato;
import com.Project.AppApplication.domain.candidato.Experiencia;
import com.Project.AppApplication.domain.candidato.Habilidades;
import com.Project.AppApplication.repository.candidato.CandidatoRepository;
import com.Project.AppApplication.repository.candidato.ExperienciaRepository;
import com.Project.AppApplication.repository.candidato.HabilidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private ExperienciaRepository experienciaRepository;

    @Autowired
    private HabilidadeRepository habilidadeRepository;

    public List<Candidato> listarCandidatos() {
        return candidatoRepository.findAll();
    }

    public Candidato salvarCandidato(Candidato candidato) {
        return candidatoRepository.save(candidato);
    }

    public Optional<Candidato> buscarCandidatoPorId(Long id) {

        Optional<Candidato> candidatoOpt = candidatoRepository.findById(id);


        if (candidatoOpt.isPresent() && verificarFormularioPreenchido(candidatoOpt.get())) {
            return candidatoOpt;
        } else {
            return Optional.empty();

        }
    }



    public boolean verificarFormularioPreenchido(Candidato candidato) {
        return !(candidato.getNome() == null || candidato.getNome().isEmpty() ||
                candidato.getEmail() == null || candidato.getEmail().isEmpty() ||
                candidato.getTelefone() == null || candidato.getTelefone().isEmpty() ||
                candidato.getDt_nascimento() == null ||
                candidato.getEndereco() == null || candidato.getEndereco().isEmpty() ||
                candidato.getCep() == null || candidato.getCep().isEmpty() ||
                candidato.getCidade() == null || candidato.getCidade().isEmpty() ||
                candidato.getEstado() == null || candidato.getEstado().isEmpty() ||
                candidato.getArea_atuacao() == null || candidato.getArea_atuacao().isEmpty() ||
                candidato.getNv_experiencia() == null || candidato.getNv_experiencia().isEmpty());
    }

    public void deletarCandidato(Long id) {
        candidatoRepository.deleteById(id);
    }


    @Transactional
    public Candidato atualizarCandidato(Long candidatoId, Candidato candidatoAtualizado) {
        Candidato candidatoExistente = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        candidatoExistente.setNome(candidatoAtualizado.getNome());
        candidatoExistente.setTelefone(candidatoAtualizado.getTelefone());
        candidatoExistente.setDt_nascimento(candidatoAtualizado.getDt_nascimento());
        candidatoExistente.setEndereco(candidatoAtualizado.getEndereco());
        candidatoExistente.setCep(candidatoAtualizado.getCep());
        candidatoExistente.setCidade(candidatoAtualizado.getCidade());
        candidatoExistente.setEstado(candidatoAtualizado.getEstado());
        candidatoExistente.setArea_atuacao(candidatoAtualizado.getArea_atuacao());
        candidatoExistente.setNv_experiencia(candidatoAtualizado.getNv_experiencia());

        if (candidatoAtualizado.getExperiencia() != null) {
            Experiencia experienciaExistente = candidatoExistente.getExperiencia();
            if (experienciaExistente != null) {
                experienciaExistente.setDescricao(candidatoAtualizado.getExperiencia().getDescricao());
            } else {
                Experiencia novaExperiencia = new Experiencia();
                novaExperiencia.setDescricao(candidatoAtualizado.getExperiencia().getDescricao());
                experienciaRepository.save(novaExperiencia);
                candidatoExistente.setExperiencia(novaExperiencia);
            }
        }

        if (candidatoAtualizado.getHabilidades() != null) {
            Habilidades habilidadesExistentes = candidatoExistente.getHabilidades();
            if (habilidadesExistentes != null) {
                habilidadesExistentes.setDescricao(candidatoAtualizado.getHabilidades().getDescricao());
            } else {
                Habilidades novasHabilidades = new Habilidades();
                novasHabilidades.setDescricao(candidatoAtualizado.getHabilidades().getDescricao());
                habilidadeRepository.save(novasHabilidades);
                candidatoExistente.setHabilidades(novasHabilidades);
            }
        }
        return candidatoRepository.save(candidatoExistente);
    }
}
