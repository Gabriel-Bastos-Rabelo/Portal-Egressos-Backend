package com.portal_egressos.portal_egressos_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal_egressos.portal_egressos_backend.models.Curso;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.repositories.CursoRepository;

import jakarta.transaction.Transactional;

@Service
public class CursoService {
    @Autowired
    private CursoRepository cursoRepo;

    @Transactional
    public Curso salvar(Curso curso){
        verificarCurso(curso);
        return cursoRepo.save(curso);
    }

    @Transactional
    public Curso atualizar(Curso curso){
        verificarCurso(curso);
        verificarId(curso);
                return cursoRepo.save(curso);
    }

    @Transactional 
    public void remover(Curso curso){
        verificarId(curso);
        cursoRepo.delete(curso);
    }
    
    public List<Curso> listarCursos() {
        return cursoRepo.findAll();
    }

    public Optional<Curso> buscarPorId(Long id){
        return cursoRepo.findById(id);
    }

    public List<Egresso> listarEgressosPorCurso(Curso curso){
        verificarId(curso);
        return cursoRepo.obterEgressosPorCurso(curso.getId());
        
    }
    private void verificarId(Curso curso) {
        if ((curso == null) || (curso.getId() == null) || (!cursoRepo.existsById(curso.getId())))
            throw new IllegalArgumentException ("ID de curso inválido!");
    }

    private void verificarCurso(Curso curso){
        if (curso == null)
            throw new IllegalArgumentException("Um curso válido deve ser infromado!");
        
        if((curso.getNome() == null ) || (curso.getNome().equals("")))
            throw new IllegalArgumentException("Nome do curso deve ser informado!");
        
        if((curso.getNivel() == null ) || (curso.getNivel().toString().equals("")))
            throw new IllegalArgumentException("Nível do curso deve ser informado!");
        
    }
}
