package com.portal_egressos.portal_egressos_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.repositories.CursoRepository;

import jakarta.transaction.Transactional;

@Service
public class CursoService {
    @Autowired
    private CursoRepository cursoRepositorio;

    @Transactional
    public Curso salvarCurso(Curso curso) {
        verificarCurso(curso);
        return cursoRepositorio.save(curso);
    }

    @Transactional
    public Curso atualizarCurso(Curso curso) {
        verificarCursoId(curso);

        Curso cursoExistente = cursoRepositorio.findById(curso.getId()).get();

        if (curso.getNome() != null && !curso.getNome().isEmpty()) {
            cursoExistente.setNome(curso.getNome());
        }
        if (curso.getNivel() != null && !curso.getNivel().isEmpty()) {
            cursoExistente.setNivel(curso.getNivel());
        }
        return cursoRepositorio.save(cursoExistente);
    }

    @Transactional
    public void removerCurso(Curso curso) {
        verificarCursoId(curso);
        cursoRepositorio.delete(curso);
    }

    public List<Curso> listarCursos() {
        return cursoRepositorio.findAll();
    }

    public Curso buscarPorId(Long id) {
        Curso curso = cursoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com id: " + id));
        return curso;
    }

    public List<Egresso> listarEgressosPorCurso(Curso curso) {
        verificarCursoId(curso);
        return cursoRepositorio.obterEgressosPorCurso(curso.getId());

    }

    public int listarQuantidadeDeEgressosPorCurso(Curso curso) {
        verificarCursoId(curso);
        return cursoRepositorio.obterQuantidadeDeEgressosPorCurso(curso.getId());
    }

    private void verificarCursoId(Curso curso) {
        if ((curso == null) || (curso.getId() == null) || (!cursoRepositorio.existsById(curso.getId())))
            throw new RegraNegocioRunTime("ID de curso inválido!");
    }

    private void verificarCurso(Curso curso) {
        if (curso == null)
            throw new RegraNegocioRunTime("Um curso válido deve ser infromado.");

        if ((curso.getNome() == null) || (curso.getNome().equals("")))
            throw new RegraNegocioRunTime("Nome do curso deve ser informado.");

        if ((curso.getNivel() == null) || (curso.getNivel().toString().equals("")))
            throw new RegraNegocioRunTime("Nível do curso deve ser informado.");

    }

    public Curso save(Curso curso) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
}
